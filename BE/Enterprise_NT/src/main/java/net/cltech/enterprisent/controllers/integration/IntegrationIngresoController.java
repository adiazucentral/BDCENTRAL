package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.DTO.integration.order.OrderDto;
import net.cltech.enterprisent.domain.DTO.integration.order.ServiceResponse;
import net.cltech.enterprisent.domain.integration.ingreso.RequestBarCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestCentralCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestDemographicAuto;
import net.cltech.enterprisent.domain.integration.ingreso.RequestHomologationDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestHomologationTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestItemCentralCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTest;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsHomologationDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsHomologationTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsSample;
import net.cltech.enterprisent.domain.integration.ingreso.ResponseBarCode;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationIngresoService;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de acceso a los servicios de interfaz de ingreso
 *
 * @version 1.0.0
 * @author bvalero
 * @since 22/01/2020
 * @see Creación
 */
@Api(
        name = "Servicios para interfaz de ingreso",
        group = "Integración",
        description = "Servicios generales de integración con otras aplicaciones"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/ingreso")
public class IntegrationIngresoController
{

    @Autowired
    private IntegrationIngresoService serviceIngreso;

    //------------ LISTA LA HOMOLOGACION DE ITEM DE DEMOGRAFICOS POR SISTEMA CENTRAL ----------------
    @ApiMethod(
            description = "Lista de itemDemografico Homologado",
            path = "/api/integration/ingreso/DemographicsHomologationSystem",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponsHomologationDemographicIngreso.class)
    @RequestMapping(value = "/DemographicsHomologationSystem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsHomologationDemographicIngreso> demographicHomologation(
            @ApiBodyObject(clazz = RequestHomologationDemographicIngreso.class) @RequestBody RequestHomologationDemographicIngreso homo
    ) throws Exception
    {
        return new ResponseEntity<>(serviceIngreso.demographicHomologation(homo), HttpStatus.OK);
    }

    //------------- RETORNAR OBJETO DE RESPUESTA A LA HOMOLOGACION DEL ITEM DE EXAMENES -------------
    @ApiMethod(
            description = "Homologación de examenes",
            path = "/api/integration/ingreso/TestHomologationSystem",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponsHomologationTestIngreso.class)
    @RequestMapping(value = "/TestHomologationSystem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsHomologationTestIngreso> testHomologation(
            @ApiBodyObject(clazz = ResponsHomologationTestIngreso.class) @RequestBody RequestHomologationTestIngreso requestHomologationTestIngreso
    ) throws Exception
    {
        return new ResponseEntity<>(serviceIngreso.testHomologation(requestHomologationTestIngreso), HttpStatus.OK);
    }

    //------------------ CREACION AUTOMATICA DE DEMOGRAFICO --------------------
    @ApiMethod(
            description = "Creación automatica de demograficos segun sea su existencia",
            path = "/api/integration/ingreso/autoCreationDemographic",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponsHomologationTestIngreso.class)
    @RequestMapping(value = "/autoCreationDemographic", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> automaticDemographicCreation(
            @ApiBodyObject(clazz = ResponsHomologationTestIngreso.class) @RequestBody RequestDemographicAuto requestDemographicAuto
    ) throws Exception
    {
        return new ResponseEntity<>(serviceIngreso.autoCreationDemographic(requestDemographicAuto), HttpStatus.OK);
    }

    // ------------------ ACTUALIZACION DEL CODIGO CENTRAL -------------------
    @ApiMethod(
            description = "Actualización del codigo central",
            path = "/api/integration/ingreso/updateCentralCode",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/updateCentralCode", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateCentralCode(
            @ApiBodyObject(clazz = RequestItemCentralCode.class) @RequestBody RequestCentralCode centralCode
    ) throws Exception
    {
        try
        {
            return new ResponseEntity<>(serviceIngreso.updateCentralCode(centralCode), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------- OBTENER MUESTRAS POR ID ORDEN Y POR EL ID DE EL EXAMEN ------------------
    @ApiMethod(
            description = "Obtener muestras por id de orden y por el id del examen",
            path = "/api/integration/ingreso/getSampleByIdOrderAndIdTest",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponsSample.class)
    @RequestMapping(value = "/getSampleByIdOrderAndIdTest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsSample> getSampleByIdOrderAndIdTest(
            @ApiBodyObject(clazz = RequestCentralCode.class) @RequestBody RequestCentralCode requestCentralCode
    ) throws Exception
    {
        try
        {
            return new ResponseEntity<>(serviceIngreso.getSampleByIdOrderAndIdTest(requestCentralCode), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------- IMPRECION DEL CODIGO DE BARRAS ------------------
    @ApiMethod(
            description = "Impresión del codigo de barras",
            path = "/api/integration/ingreso/getBarCodePrinter",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponseBarCode.class)
    @RequestMapping(value = "/getBarCodePrinter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBarCode> getBarCode(
            @ApiBodyObject(clazz = RequestBarCode.class) @RequestBody RequestBarCode requestBarCode
    ) throws Exception
    {
        try
        {
            return new ResponseEntity<>(serviceIngreso.getBarcode(requestBarCode), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------- ENVIO DE CAMBIOS DE ESTADO CHECK-IN ------------------
    @ApiMethod(
            description = "Envio de cambios de estado para check-in",
            path = "/api/integration/ingreso/getMessageChangeStateTest/idOrder/{idOrder}/executedFrom/{executedFrom}/muestra/{muestra}/examenes/{examenes}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/getMessageChangeStateTest/idOrder/{idOrder}/executedFrom/{executedFrom}/muestra/{muestra}/examenes/{examenes}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMessageTest(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable(name = "idOrder") long idOrder,
            @ApiPathParam(name = "executedFrom", description = "lugar de procedencia") @PathVariable(name = "executedFrom") String executedFrom,
            @ApiPathParam(name = "muestra", description = "muestra de rechazo o verificacion") @PathVariable(name = "muestra") Integer muestra,
            @ApiPathParam(name = "examenes", description = "examenes de eliminacion") @PathVariable(name = "examenes") String examenes
    ) throws Exception
    {
        try
        {
            String responsMessage = serviceIngreso.getMessageTest(idOrder, executedFrom, muestra, examenes, null);
            if (!responsMessage.isEmpty())
            {
                return new ResponseEntity<>(responsMessage, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(
            description = "Obtiene una lista de todas las ordenes LIS con un id de orden HIS",
            path = "/api/integration/ingreso/getOrdersByOrderHIS/externalId/{externalId}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getOrdersByOrderHIS/externalId/{externalId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getOrdersByOrderHIS(
            @ApiPathParam(name = "externalId", description = "Id del HIS") @PathVariable(name = "externalId") String externalId
    ) throws Exception
    {
        try
        {
            List<Order> listOrdersLIS = serviceIngreso.getOrdersByOrderHIS(externalId);
            if (!listOrdersLIS.isEmpty())
            {
                return new ResponseEntity<>(listOrdersLIS, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ VERIIFICA SI EL NUMERO DE ORDEN EXISTE ----------------
    @ApiMethod(
            description = "consulta la existencia de la orden LIS con un id de orden HIS y el codigo de la sede",
            path = "/api/integration/ingreso/getOrdersByOrderHIS/externalId/{externalId}/branch/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/getOrdersByOrderHIS/externalId/{externalId}/branch/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> thisOrderExists(
            @ApiPathParam(name = "externalId", description = "Id del HIS") @PathVariable(name = "externalId") String externalId,
            @ApiPathParam(name = "branch", description = "codigo de la sede") @PathVariable(name = "branch") String branch
    ) throws Exception
    {
        try
        {
            return new ResponseEntity<>(serviceIngreso.getValidOrdersByHIS(externalId, branch), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ Consulta los items demograficos y sus codigos de homologacion de un sistema central ----------------
    @ApiMethod(
            description = "Consulta los items demograficos y sus codigos de homologacion",
            path = "/api/integration/ingreso/getOrdersByOrderHIS/centralSystem/{system}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/getOrdersByOrderHIS/centralSystem/{system}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsHomologationDemographicIngreso> getDemographicCentralSystem(
            @ApiPathParam(name = "system", description = "sistema central") @PathVariable(name = "system") Integer system
    ) throws Exception
    {
        try
        {
            return new ResponseEntity<>(serviceIngreso.getDemographicCentralSystem(system), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ Consulta los examenes ----------------
    @ApiMethod(
            description = "Consulta los examenes por su id",
            path = "/api/integration/ingreso/getTestsByOrderHIS/idTest/{idTest}/idBranch/{idBranch}/orderType/{orderType}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RequestTest.class)
    @RequestMapping(value = "/getTestsByOrderHIS/idTest/{idTest}/idBranch/{idBranch}/orderType/{orderType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestTest> getTestsByOrderHIS(
            @ApiPathParam(name = "idTest", description = "Id del examen") @PathVariable(name = "idTest") Integer idTest,
            @ApiPathParam(name = "idBranch", description = "Id de la sede") @PathVariable(name = "idBranch") Integer idBranch,
            @ApiPathParam(name = "orderType", description = "Tipo de orden") @PathVariable(name = "orderType") Integer orderType
    ) throws Exception
    {
        try
        {
            return new ResponseEntity<>(serviceIngreso.getTestsByOrderHIS(idTest, idBranch, orderType), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ Consulta los items demograficos y sus codigos de homologacion de un sistema central ----------------
    @ApiMethod(
            description = "Crea un codigo de homologación aleatoreo para un item demografico inexistente, pero si este existe devuelve el codigo del mismo",
            path = "/api/integration/ingreso/createRandomCode/nameDemographicItem/{nameDemographicItem}/idDemographic/{idDemographic}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/createRandomCode/nameDemographicItem/{nameDemographicItem}/idDemographic/{idDemographic}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRandomCode(
            @ApiPathParam(name = "nameDemographicItem", description = "Nombre del item demografico") @PathVariable(name = "nameDemographicItem") String nameDemoItem,
            @ApiPathParam(name = "idDemographic", description = "Id del demografico el cual tiene el item") @PathVariable(name = "idDemographic") int idDemographic
    ) throws Exception
    {
        try
        {
            return new ResponseEntity<>(serviceIngreso.createRandomCode(nameDemoItem, idDemographic), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ------------------ ACTUALIZACION DEL CODIGO CENTRAL (MYM) -------------------
    @ApiMethod(
            description = "Actualización del codigo central (MYM)",
            path = "/api/integration/ingreso/updateCentralCodeMyM",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/updateCentralCodeMyM", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateCentralCodeMyM(
            @ApiBodyObject(clazz = RequestItemCentralCode.class) @RequestBody RequestCentralCode centralCode
    ) throws Exception
    {
        try
        {
            return new ResponseEntity<>(serviceIngreso.updateCentralCodeMyM(centralCode), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(
            description = "Crea una orden del His al Lis",
            path = "/api/integration/ingreso/createOrder",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceResponse.class)
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse> createOrder(@ApiBodyObject(clazz = OrderDto.class) @RequestBody OrderDto orderDto)
    {
        return new ResponseEntity<>(serviceIngreso.create(orderDto), HttpStatus.OK);
    }
}
