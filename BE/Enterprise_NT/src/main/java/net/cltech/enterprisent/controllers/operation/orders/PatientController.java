package net.cltech.enterprisent.controllers.operation.orders;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.PatientPhoto;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.domain.operation.reports.PatientReport;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.log.patient.PatientLog;
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
 * Controlador de servicios rest sobre pacientes
 *
 * @version 1.0.0
 * @author dcortes
 * @since 4/07/2017
 * @see Para cuando se crea una clase incluir
 */
@Api(
        name = "Pacientes",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre ordenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/patients")
public class PatientController
{

    @Autowired
    private PatientService patientService;

    @ApiMethod(
            description = "Obtiene un paciente buscandolo por id",
            path = "/api/patients/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> get(
            @ApiPathParam(name = "id", description = "Id DB") @PathVariable("id") int id
    ) throws Exception
    {
        Patient patient = patientService.get(id);
        if (patient != null)
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene un paciente buscandolo por numero de historia",
            path = "/api/patients/filter/patientId/{patientId}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/filter/patientId/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> get(
            @ApiPathParam(name = "patientId", description = "Numero de Historia") @PathVariable("patientId") String patientId
    ) throws Exception
    {
        Patient patient = patientService.get(patientId);
        if (patient != null)
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene un paciente buscandolo por demografico no codificado",
            path = "/api/patients/filter/demographicId/{demographicId}/demographicValue/{demographicValue}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/filter/demographicId/{demographicId}/demographicValue/{demographicValue}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> getPatienByDemographic(
            @ApiPathParam(name = "demographicId", description = "Id Demografico no codificado") @PathVariable("demographicId") int demographicId,
            @ApiPathParam(name = "demographicValue", description = "Valor a buscar") @PathVariable("demographicValue") String demographicValue
    ) throws Exception
    {
        Patient patient = patientService.getPatienByDemographic(demographicId, demographicValue);
        if (patient != null)
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene un paciente buscandolo por numero de historia y tipo de documento",
            path = "/api/patients/filter/patientId/{patientId}/documentType/{documentType}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/filter/patientId/{patientId}/documentType/{documentType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> get(
            @ApiPathParam(name = "patientId", description = "Numero de Historia") @PathVariable("patientId") String patientId,
            @ApiPathParam(name = "documentType", description = "Id tipo de documento") @PathVariable("documentType") int documentType
    ) throws Exception
    {
        Patient patient = patientService.get(patientId, documentType, 0);
        if (patient != null)
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Crea un nuevo paciente",
            path = "/api/patients",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> create(
            @ApiBodyObject(clazz = Patient.class) @RequestBody Patient patient,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(patientService.create(patient, JWT.decode(request).getId()), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualiza la informacion de un paciente",
            path = "/api/patients",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> update(
            @ApiBodyObject(clazz = Patient.class) @RequestBody Patient patient,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(patientService.update(patient, JWT.decode(request).getId()), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene el paciente de una orden",
            path = "/api/patients/filter/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicValue.class)
    @RequestMapping(value = "/filter/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> get(
            @ApiPathParam(name = "order", description = "Numero de Orden") @PathVariable("order") long order
    ) throws Exception
    {
        Patient patient = patientService.get(order);
        if (patient != null)
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el paciente de una orden en formato de listado de demograficos",
            path = "/api/patients/filter/map/demographics/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/filter/map/demographics/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicValue>> getAsDemographics(
            @ApiPathParam(name = "order", description = "Numero de Orden") @PathVariable("order") long order
    ) throws Exception
    {
        List<DemographicValue> demos = patientService.getAsListDemographics(order);
        if (demos != null)
        {
            return new ResponseEntity<>(demos, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el paciente por historia en formato de listado de demograficos",
            path = "/api/patients/filter/patient_id/{patientId}/map/demographics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/filter/patient_id/{patientId}/map/demographics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicValue>> getByPatientIdAsDemographics(
            @ApiPathParam(name = "patientId", description = "Historia") @PathVariable("patientId") String patientId
    ) throws Exception
    {
        List<DemographicValue> demos = patientService.getByPatientIdAsListDemographics(patientId);
        if (demos != null)
        {
            return new ResponseEntity<>(demos, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el paciente por historia y tipo de documento en formato de listado de demograficos",
            path = "/api/patients/filter/patient_id/{patientId}/documenttype/{documentType}/map/demographics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/filter/patient_id/{patientId}/documenttype/{documentType}/map/demographics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicValue>> getByPatientIdAsDemographics(
            @ApiPathParam(name = "patientId", description = "Historia") @PathVariable("patientId") String patientId,
            @ApiPathParam(name = "documentType", description = "Id tipo documento") @PathVariable("documentType") int documentType
    ) throws Exception
    {
        List<DemographicValue> demos = patientService.getByPatientIdAsListDemographics(patientId, documentType, 0);
        if (demos != null)
        {
            return new ResponseEntity<>(demos, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene la foto del paciente",
            path = "/api/patients/filter/{id}/photo",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PatientPhoto.class)
    @RequestMapping(value = "/filter/{id}/photo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientPhoto> getPhoto(
            @ApiPathParam(name = "id", description = "Id base de datos") @PathVariable("id") int id
    ) throws Exception
    {
        PatientPhoto photo = patientService.getPhoto(id);
        if (photo != null)
        {
            return new ResponseEntity(photo, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualia la foto del paciente",
            path = "/api/patients/photo",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PatientPhoto.class)
    @RequestMapping(value = "/photo", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientPhoto> updatePhoto(
            @ApiBodyObject(clazz = PatientPhoto.class) @RequestBody PatientPhoto photo
    ) throws Exception
    {
        patientService.updatePhoto(photo);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene el paciente por nombres, apellidos y fechas de nacimiento",
            path = "/api/patients/filter/lastName/{lastName}/surName/{surName}/name1/{name1}/name2/{name2}/sex/{sex}/birthday/{birthday}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/filter/lastName/{lastName}/surName/{surName}/name1/{name1}/name2/{name2}/sex/{sex}/birthday/{birthday}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> getPatientBy(
            @ApiPathParam(name = "lastName", description = "Primer apellido") @PathVariable("lastName") String lastName,
            @ApiPathParam(name = "surName", description = "Segundo apellido") @PathVariable("surName") String surName,
            @ApiPathParam(name = "name1", description = "Primer nombre") @PathVariable("name1") String name1,
            @ApiPathParam(name = "name2", description = "Segundo nombre") @PathVariable("name2") String name2,
            @ApiPathParam(name = "sex", description = "Sexo") @PathVariable("sex") Integer sex,
            @ApiPathParam(name = "birthday", description = "Fecha de nacimiento") @PathVariable("birthday") Long birthday
    ) throws Exception
    {
        List<Patient> patients = patientService.getPatientBy(lastName, surName, name1, name2, sex, birthday);
        if (patients != null)
        {
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //REGISTRAR HISTORICO DEL PACIENTE
    @ApiMethod(
            description = "Registra el historico del paciente",
            path = "/api/patients/patientHistory",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Result.class)
    @RequestMapping(value = "/patientHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoricalResult> createPatientHistory(
            @ApiBodyObject(clazz = HistoricalResult.class) @RequestBody HistoricalResult historicalResult
    ) throws Exception
    {
        return new ResponseEntity<>(patientService.createPatientHistory(historicalResult, -1), HttpStatus.OK);
    }

    //ACTUALIZAR REGISTRO HISTORICO DEL PACIENTE
    @ApiMethod(
            description = "Actualizar registro historico del paciente",
            path = "/api/patients/patientHistory",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Result.class)
    @RequestMapping(value = "/patientHistory", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoricalResult> updatePatientHistory(
            @ApiBodyObject(clazz = HistoricalResult.class) @RequestBody HistoricalResult historicalResult
    ) throws Exception
    {
        return new ResponseEntity<>(patientService.updatePatientHistory(historicalResult), HttpStatus.OK);
    }

    //CANTIDAD DE REGISTROS
    @ApiMethod(
            description = "Obtiene la cantidad total de pacientes",
            path = "/api/patients/numberpatients",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Result.class)
    @RequestMapping(value = "/numberpatients", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> numberPatients() throws Exception
    {
        return new ResponseEntity<>(patientService.numberPatients(), HttpStatus.OK);
    }

    //REGISTROS DE PACIENTE POR PAGINA
    @ApiMethod(
            description = "Obtiene un listado de pacientes por paginacion",
            path = "/api/patients/listpatientsbypag",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Result.class)
    @RequestMapping(value = "/listpatientsbypag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> listPatientsByPag(
            @ApiBodyObject(clazz = PatientReport.class) @RequestBody PatientReport patientReport
    ) throws Exception
    {
        List<Patient> list = patientService.listPatientsByPag(patientReport);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene un paciente con la informacion más basica por tipo de documento y documento",
            path = "/api/patients/getBasicPatientInformation/documentType/{documentType}/document/{document}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/getBasicPatientInformation/documentType/{documentType}/document/{document}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> getBasicPatientInformation(
            @ApiPathParam(name = "documentType", description = "Tipo de documento") @PathVariable("documentType") int documentType,
            @ApiPathParam(name = "document", description = "Documento del paciente") @PathVariable("document") String history
    ) throws Exception
    {
        Patient patient = patientService.getBasicPatientInformation(documentType, history);
        if (patient == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene una lista de pacientes con la informacion más basica por un rango de fechas en las que se hayan generado ordenes",
            path = "/api/patients/getBasicPatientInformation/initialDate/{initialDate}/endDate/{endDate}/patientStatus/{patientStatus}/filterType/{filterType}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getBasicPatientInformation/initialDate/{initialDate}/endDate/{endDate}/patientStatus/{patientStatus}/filterType/{filterType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> getBasicPatientInformationByDate(
            @ApiPathParam(name = "initialDate", description = "Fecha inicial") @PathVariable("initialDate") Integer initialDate,
            @ApiPathParam(name = "endDate", description = "Fecha final") @PathVariable("endDate") Integer endDate,
            @ApiPathParam(name = "patientStatus", description = "Estado del paciente") @PathVariable("patientStatus") int patientStatus,
            @ApiPathParam(name = "filterType", description = "Tipo de filtro") @PathVariable("filterType") int filterType
    ) throws Exception
    {
        List<Patient> patients = patientService.getBasicPatientInformationByDate(initialDate, endDate, patientStatus, filterType);
        if (patients == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(patients, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Actualiza la informacion basica del paciente",
            path = "/api/patients/updateBasicPatientInformation",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/updateBasicPatientInformation", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBasicPatientInformation(
            @ApiBodyObject(clazz = Patient.class) @RequestBody Patient patient
    ) throws Exception
    {
        int rowsAffected = patientService.updateBasicPatientInformation(patient);
        if (rowsAffected == -1)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Actualiza la informacion basica del paciente",
            path = "/api/patients/updateStatePatient",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/updateStatePatient", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateStatePatient(
            @ApiBodyObject(clazz = Patient.class) @RequestBody Patient patient
    ) throws Exception
    {
        int rowsAffected = patientService.updateStatePatient(patient);
        if (rowsAffected == -1)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
