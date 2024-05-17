package net.cltech.enterprisent.controllers.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.cltech.enterprisent.controllers.tools.JWTPatient;
import net.cltech.enterprisent.domain.integration.mobile.AuthenticationData;
import net.cltech.enterprisent.domain.integration.mobile.AuthorizedPatient;
import net.cltech.enterprisent.domain.integration.mobile.ChangePassword;
import net.cltech.enterprisent.domain.integration.mobile.JWTTokenPatient;
import net.cltech.enterprisent.domain.integration.mobile.LisPatient;
import net.cltech.enterprisent.domain.integration.mobile.OrderEt;
import net.cltech.enterprisent.domain.integration.mobile.PatientEmailUpdate;
import net.cltech.enterprisent.domain.integration.mobile.RestorePassword;
import net.cltech.enterprisent.domain.integration.mobile.TestEt;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMobileService;
import net.cltech.enterprisent.tools.Constants;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de servicios para la app Móvil.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 19/08/2020
 * @see Creación
 */
@Api(
        name = "App Móvil",
        group = "Integración",
        description = "Servicios de generales de integración con app móvil"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/mobile")
public class IntegrationMobileController
{

    @Autowired
    private IntegrationMobileService mobileService;

    //----------------------- OBTIENE EL TOKEN PARA LA APP MÓVIL AUTENTICANDOSE CON EL EMAIL-------
    @ApiMethod(
            description = "Realiza la autenticación del usuario, esta funcion retorna un token de sesion para la app movil",
            path = "/api/mobile/authentications/patient",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = JWTTokenPatient.class)
    @RequestMapping(value = "/authentications/patient", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTTokenPatient> getEtUserAuthentication(
            @ApiBodyObject(clazz = AuthenticationData.class) @RequestBody AuthenticationData user
    ) throws Exception, Exception
    {
        JWTTokenPatient token = new JWTTokenPatient();
        AuthorizedPatient authorizedUser = mobileService.getEtUserAuthentication(user.getUser(), user.getPassword());
        //Permite un ingreso temporal a un usuario de desarrollo
        if (authorizedUser != null)
        {
            token.setSuccess(true);
            token.setUser(authorizedUser);
            token.setToken(JWTPatient.generate(authorizedUser, Constants.TOKEN_AUTH_USER));
        } else
        {
            token.setSuccess(false);
            token.setToken("");
        }
        return new ResponseEntity(token, HttpStatus.OK);
    }

    //-----------------CAMBIAR CONTRASEÑA EN EL APLICATIVO APP MÓVIL------------------------
    @ApiMethod(
            description = "Actualizar contraseña de un usuario de la app móvil",
            path = "/api/mobile/patients",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWTPatient")
    @ApiResponseObject(clazz = ChangePassword.class)
    @RequestMapping(value = "/patients", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChangePassword> changePassword(
            @ApiBodyObject(clazz = ChangePassword.class) @RequestBody ChangePassword userPassword
    ) throws Exception, InvocationTargetException
    {
        ChangePassword changePassword = mobileService.changePassword(userPassword);
        return new ResponseEntity<>(changePassword, HttpStatus.OK);
    }

    //----------------------- Obtiene un paciente por número de historia -----------------------
    @ApiMethod(
            description = "Obtiene un paciente por número de historia",
            path = "/api/mobile/patients/filter/patientId/{patientId}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = LisPatient.class)
    @RequestMapping(value = "/patients/filter/patientId/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LisPatient> getPatientByPatientId(
            @ApiPathParam(name = "patientId", description = "Número de historia del paciente") @PathVariable(name = "patientId") String patientId
    ) throws Exception, Exception
    {
        LisPatient patient = mobileService.getPatientByPatientId(patientId);
        if (patient != null)
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    //------------ LISTA ORDENES PARA LA APP MOVIL ----------------
    @ApiMethod(
            description = "Obtiene las ordenes del paciente autenticado para la app móvil",
            path = "/api/mobile/orders/filter/patient/{limit}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWTPatient")
    @ApiResponseObject(clazz = OrderEt.class)
    @RequestMapping(value = "/orders/filter/patient/{limit}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderEt>> getEtOrders(
            @ApiPathParam(name = "limit", description = "Numero de ordenes a consultar")
            @PathVariable("limit") int limit
    ) throws Exception
    {
        List<OrderEt> listOrderEt = mobileService.getEtOrders(limit);
        if (listOrderEt.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listOrderEt, HttpStatus.OK);
        }
    }

    //------------ LISTA EXAMENES MARCADOS PARA MOSTRAR EN ORDENES ----------------
    @ApiMethod(
            description = "Obtiene los examenes que estan marcados para ver en ingreso de ordenes",
            path = "/api/mobile/tests/filter/viewInOrder",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWTPatient")
    @ApiResponseObject(clazz = TestEt.class)
    @RequestMapping(value = "/tests/filter/viewInOrder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestEt>> getEtTest() throws Exception
    {
        List<TestEt> listTestEt = mobileService.getEtTest();
        if (listTestEt.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listTestEt, HttpStatus.OK);
        }
    }

    //-----------------RESTAURAR CONTRASEÑA EN EL APLICATIVO APP MÓVIL------------------------
    @ApiMethod(
            description = "Restaurar la contraseña del paciente en la aplicación",
            path = "/api/mobile/patients/restorePassword",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = RestorePassword.class)
    @RequestMapping(value = "/patients/restorePassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestorePassword> restorPatientPassword(
            @ApiBodyObject(clazz = LisPatient.class) @RequestBody LisPatient lisPatient
    ) throws Exception
    {
        RestorePassword restorePassword = mobileService.restorPatientPassword(lisPatient);
        return new ResponseEntity<>(restorePassword, HttpStatus.OK);
    }

    //------------ OBTIENE EL PDF DE LOS RESULTADOS DE UNA ORDEN PARA LA APP MOVIL --------
    @ApiMethod(
            description = "Obtiene el pdf de los resultados de una orden para la app móvil",
            path = "/api/mobile/reports/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = "application/pdf",
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWTPatient")
    @ApiResponseObject(clazz = byte[].class)
    @RequestMapping(value = "/reports/{order}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> getPDFOrder(
            @ApiPathParam(name = "order", description = "Numero de la orden") @PathVariable("order") long order
    ) throws Exception
    {
        byte[] outputReport = null;

        outputReport = mobileService.getPDFOrder(order);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "pdf"));
        headers.setContentDispositionFormData("attachment", "x.pdf");
        headers.setContentLength(outputReport.length);

        if (outputReport.length == 0)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<byte[]>(outputReport, headers, HttpStatus.OK);
        }
    }
    
    //------------ ACTUALIZA EL CORREO ELECTRONICO DE UN PACIENTE DESDE EL APP MOVIL --------
    @ApiMethod(
            description = "Actualiza el correo electronico de un paciente según el id del paciente que se envie",
            path = "/api/mobile/patientEmailUpdate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWTPatient")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/patientEmailUpdate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> patientEmailUpdate(
            @ApiBodyObject(clazz = PatientEmailUpdate.class) @RequestBody PatientEmailUpdate patient
    ) throws Exception
    {
        return new ResponseEntity<>(mobileService.patientEmailUpdate(patient), HttpStatus.OK);
    }
}
