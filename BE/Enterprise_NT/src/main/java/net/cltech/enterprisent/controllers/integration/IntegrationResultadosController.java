package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.domain.DTO.integration.resultados.OrderToSearch;
import net.cltech.enterprisent.domain.DTO.integration.resultados.ResponseOrderDetailResultHIS;
import net.cltech.enterprisent.domain.integration.his.ResultsQueryFilter;
import net.cltech.enterprisent.domain.integration.resultados.BeanForUpdateStatusSendHis;
import net.cltech.enterprisent.domain.integration.resultados.RequestOrdersResultados;
import net.cltech.enterprisent.domain.integration.resultados.RequestUpdateSendResult;
import net.cltech.enterprisent.domain.integration.resultados.ResponseDetailMicroorganisms;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderDetailResult;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderResult;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationResultadosService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.tools.Tools;
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
 * Controlador de acceso a los servicios de interfaz de resultados
 *
 * @version 1.0.0
 * @author javila
 * @since 04/03/2020
 * @see Creación
 */
@Api(
        name = "Servicios para interfaz de resultados",
        group = "Integración",
        description = "Servicios generales de integración con otras aplicaciones"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/resultados")
public class IntegrationResultadosController
{

    @Autowired
    private IntegrationResultadosService serviceResultados;
    @Autowired
    private ConfigurationService configurationService;

    // ---------------- Registros de ordenes desde una orden inicial hasta una horden final ------------------
    @ApiMethod(
            description = "Listar ordenes desde una orden con sus respectivos demograficos sean codificacos, no codificados, dinamicos, y fijos",
            path = "/api/integration/resultados/getOrders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponseOrderResult.class)
    @RequestMapping(value = "/getOrders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseOrderResult>> fromOrderUntilOrder(
            @ApiBodyObject(clazz = RequestOrdersResultados.class) @RequestBody RequestOrdersResultados requestOrdersResultados
    ) throws Exception
    {
        try
        {
            List<ResponseOrderResult> listOrderIds = serviceResultados.ordersRange(requestOrdersResultados);
            if (listOrderIds == null || listOrderIds.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
            {
                return new ResponseEntity<>(listOrderIds, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // ---------------- Registros de ordenes desde una orden inicial hasta una horden final ------------------
    @ApiMethod(
            description = "Listar ordenes desde una orden con sus respectivos demograficos sean codificacos, no codificados, dinamicos, y fijos",
            path = "/api/integration/resultados/getorderspendingsendhis",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderList.class)
    @RequestMapping(value = "/getorderspendingsendhis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> orderspendingsendhis() throws Exception
    {
        try
        {
            List<OrderList> listOrder = serviceResultados.orderspendingsendhis();

            if (listOrder == null || listOrder.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
            {
                return new ResponseEntity<>(listOrder, HttpStatus.OK);
            }

        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

     @ApiMethod(
            description = "Listar ordenes desde una orden con sus respectivos demograficos sean codificacos, no codificados, dinamicos, y fijos",
            path = "/api/integration/resultados/updateSentCentralSystem",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )

    @ApiResponseObject(clazz = OrderList.class)
    @RequestMapping(value = "/updateSentCentralSystem", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateSentCentralSystem(@RequestBody List<BeanForUpdateStatusSendHis> beans) throws Exception

    {
        try
        {
            beans.forEach(bean ->
            {
                try
                {
                    serviceResultados.updateSentCentralSystem(bean.getNumberOrder(), bean.getCodeTest(), null);
                } catch (Exception ex)
                {
                    Logger.getLogger(IntegrationResultadosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // ---------------- Resultados para la interfaz de resultados por orden y sistema central ------------------
    @ApiMethod(
            description = "Resultados para la interfaz de resultados por orden y sistema central",
            path = "/api/integration/resultados/getResult/idOrder/{idOrder}/centralSystem/{centralSystem}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponseOrderResult.class)
    @RequestMapping(value = "/getResult/idOrder/{idOrder}/centralSystem/{centralSystem}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseOrderDetailResult>> resultsByOrderByCentralCodes(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable(name = "idOrder") long idOrder,
            @ApiPathParam(name = "centralSystem", description = "Sistema central") @PathVariable(name = "centralSystem") int centralSystem
    ) throws Exception
    {
        try
        {
            List<ResponseOrderDetailResult> listOrderIds = serviceResultados.resultsByOrderByCentralCodes(idOrder, centralSystem, true);
            if (listOrderIds == null)
            {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (listOrderIds.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
            {
                return new ResponseEntity<>(listOrderIds, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

        //------RESULTADOS POR ORDEN Y CENTRAL SYSTEM
    @ApiMethod(
            description = "Resultado para interfaz HIS por orden y centralSistem",
            path = "/api/integration/resultados/resultsByCentralSystemByOrder",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = ResponseOrderResult.class)
    @RequestMapping(value = "/resultsByCentralSystemByOrder", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseOrderDetailResultHIS>> resultsByCentralSystemByOrder(
            @ApiBodyObject(clazz = ResultsQueryFilter.class) @RequestBody OrderToSearch order
    ) throws Exception
    {
        List<ResponseOrderDetailResultHIS> listOrderIds = serviceResultados.resultsByOrderByCentralCodes(order.getNumberOrder(), order.getCentralSystemId());

        if (listOrderIds == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (listOrderIds.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listOrderIds, HttpStatus.OK);
        }
    }

    //------ORDEN CABECERA PARA RESULTADOS HIS
    @ApiMethod(
            description = "Cabecera para la interfaz de resultados por filtro de sistema central, días de consulta",
            path = "/api/integration/resultados/getOrderByCentralSystem",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )

    @ApiResponseObject(clazz = ResponseOrderResult.class)
    @RequestMapping(value = "/getOrderByCentralSystem", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity< List<ResponseOrderResult>> headOrderByResultByCentralSystem(
            @ApiBodyObject(clazz = ResultsQueryFilter.class) @RequestBody ResultsQueryFilter resultsQueryFilter
    ) throws Exception
    {
        try
        {
            long fromOrder = Tools.createInitialOrder(configurationService.getIntValue("DigitosOrden"), resultsQueryFilter.getDays());
            long untilOrder = Tools.createFinalOrder(configurationService.getIntValue("DigitosOrden"), resultsQueryFilter.getDays());
            List<ResponseOrderResult> listOrderIds = serviceResultados.ordersRange(new RequestOrdersResultados(fromOrder, untilOrder, resultsQueryFilter.getCentralSystemId()));
            if (listOrderIds == null || listOrderIds.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
            {
                return new ResponseEntity<>(listOrderIds, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // ---------------- ACTUALIZAR EL ESTADO DEL RESULTADO Y FECHA DE ACTUALIZACION DEL MISMO ------------------
    @ApiMethod(
            description = "Actualizar el estado del resultado con su respectiva fecha de actualización",
            path = "/api/integration/resultados/updateResultStateAndDateUpdate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/updateResultStateAndDateUpdate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateResultStateAndDateUpdate(
            @ApiBodyObject(clazz = RequestUpdateSendResult.class) @RequestBody RequestUpdateSendResult requestUpdateSendResult
    ) throws Exception
    {
        try
        {
            int updateResult = serviceResultados.updateResultStateAndDateUpdate(requestUpdateSendResult);
            return new ResponseEntity<>((updateResult != 0), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ---------------- ACTUALIZAR EL ESTADO DEL RESULTADO r Sistema Centtral------------------
    @ApiMethod(
            description = "Actualizar el estado del resultado con su respectiva fecha de actualización por centralSystem",
            path = "/api/integration/resultados/updateResultStateAndDateUpdateForcentralSystem",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/updateResultStateAndDateUpdateForcentralSystem", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateResultStateAndDateUpdateForcentralSystem(
            @ApiBodyObject(clazz = RequestUpdateSendResult.class) @RequestBody RequestUpdateSendResult requestUpdateSendResult
    ) throws Exception
    {
        try
        {

            int updateResult = serviceResultados.updateResultStateAndDateUpdate(requestUpdateSendResult);
            return new ResponseEntity<>((updateResult != 0), HttpStatus.OK);

        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ---------------- RESULTADOS DE MICROORGANISMOS ANTIBIOTICOS Y ANTIBIOGRAMAS ------------------
    @ApiMethod(
            description = "Resultados de microorganismos, antibioticos y antibiogramas por id orden y id test",
            path = "/api/integration/resultados/getAntibiograms/idTest/{idTest}/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponseDetailMicroorganisms.class)
    @RequestMapping(value = "/getAntibiograms/idTest/{idTest}/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseDetailMicroorganisms>> listDetailMicroorganisms(
            @ApiPathParam(name = "idTest", description = "Id del examen") @PathVariable(name = "idTest") int idTest,
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable(name = "idOrder") long idOrder
    ) throws Exception
    {
        try
        {
            List<ResponseDetailMicroorganisms> listResponseDetailMicro = serviceResultados.listDetailMicroorganisms(idOrder, idTest);
            if (!listResponseDetailMicro.isEmpty() && listResponseDetailMicro != null)
            {
                return new ResponseEntity<>(listResponseDetailMicro, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiMethod(
            description = "Verifica una muestra, para una orden y un examen especifico",
            path = "/api/integration/resultados/verifySampleLih/idTest/{idTest}/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/verifySampleLih/idTest/{idTest}/idOrder/{idOrder}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> verifySampleLih(
            @ApiPathParam(name = "idTest", description = "Id del examen") @PathVariable(name = "idTest") int idTest,
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable(name = "idOrder") long idOrder
    ) throws Exception
    {
        try
        {
            if (serviceResultados.verifySampleLih(idOrder, idTest))
            {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ---------------- Registros de ordenes desde una orden inicial hasta una horden final ------------------
    @ApiMethod(
            description = "Listar ordenes desde una orden con sus respectivos demograficos sean codificacos, no codificados, dinamicos, y fijos",
            path = "/api/integration/resultados/getOrdersForOthers",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponseOrderResult.class)
    @RequestMapping(value = "/getOrdersForOthers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseOrderResult>> getOrdersForOthers(
            @ApiBodyObject(clazz = RequestOrdersResultados.class) @RequestBody RequestOrdersResultados requestOrdersResultados
    ) throws Exception
    {
        try
        {
            List<ResponseOrderResult> listOrderIds = serviceResultados.getOrdersForOthers(requestOrdersResultados);
            if (listOrderIds.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
            {
                return new ResponseEntity<>(listOrderIds, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // ---------------- Resultados para la interfaz de resultados por orden y sistema central (SUIZALAB) ------------------
    @ApiMethod(
            description = "Resultados para la interfaz de resultados por orden y sistema central",
            path = "/api/integration/resultados/getResultForSuizalab/idOrder/{idOrder}/centralSystem/{centralSystem}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponseOrderResult.class)
    @RequestMapping(value = "/getResultForSuizalab/idOrder/{idOrder}/centralSystem/{centralSystem}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseOrderDetailResult>> resultsForSuizalab(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable(name = "idOrder") long idOrder,
            @ApiPathParam(name = "centralSystem", description = "Sistema central") @PathVariable(name = "centralSystem") int centralSystem
    ) throws Exception
    {
        List<ResponseOrderDetailResult> listOrderIds = serviceResultados.resultsByOrderByCentralCodes(idOrder, centralSystem, false);
        if (listOrderIds == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (listOrderIds.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listOrderIds, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Consulta los resultados de las ordenes que seran enviadas a MyM segun ciertos parametros de busqueda",
            path = "/api/integration/resultados/getMyMResults/idOrder/{idOrder}/centralSystem/{centralSystem}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResponseOrderResult.class)
    @RequestMapping(value = "/getMyMResults/idOrder/{idOrder}/centralSystem/{centralSystem}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResponseOrderDetailResult>> getMyMResults(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable(name = "idOrder") long idOrder,
            @ApiPathParam(name = "centralSystem", description = "Sistema central") @PathVariable(name = "centralSystem") int centralSystem
    ) throws Exception
    {
        try
        {
            List<ResponseOrderDetailResult> listOrderIds = serviceResultados.getMyMResults(idOrder, centralSystem, true);
            if (listOrderIds == null)
            {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (listOrderIds.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
            {
                return new ResponseEntity<>(listOrderIds, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
