package net.cltech.enterprisent.controllers.integration;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardCommon;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardLicense;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardProductivity;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardSample;
import net.cltech.enterprisent.domain.integration.dashboard.DashboardConfiguration;
import net.cltech.enterprisent.domain.integration.dashboard.DashboardUrl;
import net.cltech.enterprisent.domain.integration.dashboard.TestDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.TestValidDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.UserDashboard;
import net.cltech.enterprisent.domain.integration.resultados.BeanForUpdateStatusSendHis;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationDashBoardService;
import org.jsondoc.core.annotation.Api;
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
 * Controlador de servicios Rest sobre tableros.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/07/2018
 * @see Creacion
 */
@Api(
        name = "Integracion DashBoard",
        group = "Integración",
        description = "Servicios Rest sobre ordenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/dashboard")
public class IntegrationDashBoardController
{

    @Autowired
    private IntegrationDashBoardService dashBoardService;

    //------------ LISTAR SERVICIOS ----------------
    @ApiMethod(
            description = "Lista los servicios registrados.",
            path = "/api/dashboard/Get_Services",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/Get_Services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DashBoardCommon>> listServices() throws Exception
    {
        List<DashBoardCommon> list = dashBoardService.listServices();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR SEDES ----------------
    @ApiMethod(
            description = "Lista las sedes registradas.",
            path = "/api/dashboard/Get_Branches",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/Get_Branches", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DashBoardCommon>> listBranches() throws Exception
    {
        List<DashBoardCommon> list = dashBoardService.listBranches();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

 
    //------------ LISTAR EXAMENES ----------------
    @ApiMethod(
            description = "Lista los examenes para el dashboard",
            path = "/api/dashboard/Get_Exams",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/Get_Exams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestDashboard>> listTests() throws Exception
    {
        List<TestDashboard> list = dashBoardService.listTestsDashboard();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR AREAS ----------------
    @ApiMethod(
            description = "Lista los examenes registrados.",
            path = "/api/dashboard/Get_Sections",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/Get_Sections", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DashBoardCommon>> listAreas() throws Exception
    {
        List<DashBoardCommon> list = dashBoardService.listAreas();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR PRODUCTIVIDAD ----------------
    @ApiMethod(
            description = "Lista la prodictividad por sede y seccion.",
            path = "/api/dashboard/Get_SectionProductivityByIdSection/{branch}/{section}/{hours}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = DashBoardProductivity.class)
    @RequestMapping(value = "/Get_SectionProductivityByIdSection/{branch}/{section}/{hours}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DashBoardProductivity>> listProductivity(
            @ApiPathParam(name = "branch", description = "Id de la sede") @PathVariable("branch") int branch,
            @ApiPathParam(name = "section", description = "Id de la sección") @PathVariable("section") int section,
            @ApiPathParam(name = "hours", description = "Cantidad de horas") @PathVariable("hours") int hours
    ) throws Exception
    {
        List<DashBoardProductivity> list = dashBoardService.listProductivity(branch, section, hours);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ VERIFICA LA URL ----------------
    @ApiMethod(
            description = "Verificacion de la url de la integracion con Dashboard",
            path = "/api/dashboard/verification/url",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/verification/url", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> testUrl(
            @ApiBodyObject(clazz = DashboardUrl.class) @RequestBody DashboardUrl url
    ) throws Exception
    {
        return new ResponseEntity<>(dashBoardService.testUrl(url), HttpStatus.OK);
    }

    //------------ OBTIENE LOS TABLEROS CONFIGURADOS ----------------
    @ApiMethod(
            description = "Obtiene la lista de tableros configurados",
            path = "/api/dashboard/configuration",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = DashboardConfiguration.class)
    @RequestMapping(value = "/configuration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DashboardConfiguration>> configuration() throws Exception
    {
        return new ResponseEntity<>(dashBoardService.configuration(), HttpStatus.OK);
    }

    //------------ LISTA DE MUESTRAS ----------------
    @ApiMethod(
            description = "Obtiene la lista de muestras",
            path = "/api/dashboard/Get_Samples",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = DashboardConfiguration.class)
    @RequestMapping(value = "/Get_Samples", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DashBoardSample>> listSamples() throws Exception
    {
        return new ResponseEntity<>(dashBoardService.listSamples(), HttpStatus.OK);
    }

    //------------ AUTENTICACION PARA TABLEROS ----------------
    @ApiMethod(
            description = "Autenticación para tableros",
            path = "/api/dashboard/Get_User/{user}/{password}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/Get_User/{user}/{password}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDashboard>> authentication(
            @ApiPathParam(name = "user", description = "Usuario") @PathVariable("user") String user,
            @ApiPathParam(name = "password", description = "Password") @PathVariable("password") String password
    ) throws Exception
    {
        return new ResponseEntity<>(dashBoardService.getUserDashboard(user, password), HttpStatus.OK);
    }

    //------------ LICENCIA PARA TABLEROS ----------------
    @ApiMethod(
            description = "Obtiene los tableros licenciados",
            path = "/api/dashboard/ValidateKeyAccess/{isAdmin}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK"
    )
    @ApiResponseObject(clazz = DashBoardLicense.class)
    @RequestMapping(value = "/ValidateKeyAccess/{isAdmin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DashBoardLicense>> licence(
            @ApiPathParam(name = "isAdmin", description = "Es administrador") @PathVariable("isAdmin") boolean admin
    ) throws Exception
    {
        List<DashBoardLicense> records = dashBoardService.licenses(admin);
        if (records != null)
        {
            return new ResponseEntity(records, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    // ------------------------- Obtiene el licenciamiento de los tableros -------------------
    @ApiMethod(
            description = "Obtiene el licenciamiento de los tableros",
            path = "/api/dashboard/boardLicenses",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK"
    )
    @ApiResponseObject(clazz = HashMap.class)
    @RequestMapping(value = "/boardLicenses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Boolean>> boardLicenses() throws Exception
    {
        return new ResponseEntity(dashBoardService.boardLicenses(), HttpStatus.OK);
    }
        
    // ---------------- Resultados para la interfaz de resultados por orden y sistema central ------------------
    @ApiMethod(
            description = "Resultados para la interfaz de resultados por orden y sistema central",
            path = "/api/dashboard/getTestValid/{days}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = TestValidDashboard.class)
    @RequestMapping(value = "/getTestValid/{days}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestValidDashboard> resultsByOrderByCentralCodes(
            @ApiPathParam(name = "days", description = "days") @PathVariable("days") int days
    ) throws Exception
    {
        try
        {
            return new ResponseEntity(dashBoardService.getTestSendDashoboard(days), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    
     // ---------------- Resultados para la interfaz de resultados por orden y sistema central ------------------
    @ApiMethod(
            description = "Resultados para la interfaz de resultados por orden y sistema central",
            path = "/api/dashboard/getTestEntry/{days}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = TestValidDashboard.class)
    @RequestMapping(value = "/getTestEntry/{days}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestValidDashboard> getTestEntry(
            @ApiPathParam(name = "days", description = "days") @PathVariable("days") int days
    ) throws Exception
    {
        try
        {
            return new ResponseEntity(dashBoardService.getTestEntry(days), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @ApiMethod(
            description = "actualiza los examenes que han sido enviados al tableros",
            path = "/api/dashboard/updateSentDashboard",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/updateSentDashboard", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateSentDashboard(@RequestBody List<BeanForUpdateStatusSendHis> beans) throws Exception
    {
        try
        {
            beans.forEach(bean ->
            {
                try
                {
                    dashBoardService.updateSentDashboard(bean.getNumberOrder(), bean.getIdTest());
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
    
    @ApiMethod(
        description = "actualiza los examenes que han sido enviados al tableros en el ingreso",
        path = "/api/dashboard/updateSentDashboardEntry",
        visibility = ApiVisibility.PUBLIC,
        verb = ApiVerb.PATCH,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/updateSentDashboardEntry", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateSentDashboardEntry(@RequestBody List<BeanForUpdateStatusSendHis> beans) throws Exception
    {
        try
        {
           beans.forEach(bean ->
            {
                try
                {
                    dashBoardService.updateSentDashboardEntry(bean.getNumberOrder(), bean.getIdTest());
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
}
