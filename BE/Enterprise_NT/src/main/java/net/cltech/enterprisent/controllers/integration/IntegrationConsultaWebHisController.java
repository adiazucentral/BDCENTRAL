package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.results.TestHistory;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationConsultaWebHisService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de acceso a los servicios de consulta web his
 *
 * @version 1.0.0
 * @author Julian
 * @since 26/03/2020
 * @see Creación
 */
@Api(
        name = "Integración Consulta Web His",
        group = "Integración",
        description = "Servicios para la integración de la consulta web HIS"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/webQueryHis")
public class IntegrationConsultaWebHisController
{

    @Autowired
    private IntegrationConsultaWebHisService integrationConsultaWebHisService;

    // -------- SE OBTENDRAN TODOS LOS EXAMENES REALIZADOS AL PACIENTE QUE SE ENCUENTRE EN LA ORDEN ENVIADA DESDE EL HIS
    @ApiMethod(
            description = "Se obtendran todos los examenes realizados al paciente que se encuentre asociado a la orden que a sido enviada desde el HIS",
            path = "/api/webQueryHis/getAllTestsForPatientByIdOrderHis/idOrderHis/{idOrderHis}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/getAllTestsForPatientByIdOrderHis/idOrderHis/{idOrderHis}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> getAllTestsForPatientByIdOrderHis(
            @ApiPathParam(name = "idOrderHis", description = "Id de la orden enviada desde el HIS") @PathVariable("idOrderHis") String idOrderHis
    ) throws Exception
    {
        try
        {
            Patient listTests = integrationConsultaWebHisService.getAllTestOfPatientByIdOrderHis(idOrderHis);
            if (listTests != null)
            {
                return new ResponseEntity<>(listTests, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // -------- SE OBTENDRA TODO EL HISTORIAL DE UN PACIENTE ---------------
    @ApiMethod(
            description = "Se obtienen todas las historias asignadas a un paciente con respecto a un determinado examen",
            path = "/api/webQueryHis/allPacientHistory/idPatient/{idPatient}/idTest/{idTest}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/allPacientHistory/idPatient/{idPatient}/idTest/{idTest}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestHistory>> allPacientHistory(
            @ApiPathParam(name = "idPatient", description = "Id del paciente") @PathVariable("idPatient") int idPatient,
            @ApiPathParam(name = "idTest", description = "Id del examen") @PathVariable("idTest") int idTest
    ) throws Exception
    {
        List<TestHistory> listTests = integrationConsultaWebHisService.allPacientHistory(idPatient, idTest);
        if (!listTests.isEmpty())
        {
            return new ResponseEntity<>(listTests, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
