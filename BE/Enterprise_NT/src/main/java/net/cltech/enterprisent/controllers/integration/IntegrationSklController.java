package net.cltech.enterprisent.controllers.integration;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.integration.skl.ContainerSkl;
import net.cltech.enterprisent.domain.integration.skl.InterviewInformedConsent;
import net.cltech.enterprisent.domain.integration.skl.ListTestOrderSkl;
import net.cltech.enterprisent.domain.integration.skl.OrderConsent;
import net.cltech.enterprisent.domain.integration.skl.OrderConsentBase64;
import net.cltech.enterprisent.domain.integration.skl.OrderInformedConsent;
import net.cltech.enterprisent.domain.integration.skl.RequestSampleDestination;
import net.cltech.enterprisent.domain.integration.skl.SklAnswer;
import net.cltech.enterprisent.domain.integration.skl.SklOrderAnswer;
import net.cltech.enterprisent.domain.integration.skl.SklOrderToTake;
import net.cltech.enterprisent.domain.integration.skl.SklQuestion;
import net.cltech.enterprisent.domain.integration.skl.SklSampleDestination;
import net.cltech.enterprisent.domain.integration.skl.TestConsent;
import net.cltech.enterprisent.domain.integration.skl.patientTestPending;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.RequestFormatDate;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationSklService;
import net.cltech.enterprisent.tools.log.integration.SigaLog;
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
 * Servicios para el acceso a la informacion del SKL
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/05/2020
 * @see Creacion
 */
@Api(
        name = "Integracion SKL",
        group = "Integración",
        description = "servicios para la integracion con el skl"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/skl")
public class IntegrationSklController
{

    @Autowired
    private IntegrationSklService integrationSklService;

    @ApiMethod(
            description = "Lista los recipientes registrados",
            path = "/api/integration/skl/containers",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ContainerSkl.class)
    @RequestMapping(value = "/containers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContainerSkl>> list() throws Exception
    {
        List<ContainerSkl> list = integrationSklService.listContainer();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ OBTIENE LISTADO DE RECIPIENTES DEPENDIENDO DEL ID DEL EXAMEN ----------------
    @ApiMethod(
            description = "Obtiene todos los recipientes de una lista de examenes separados por comas",
            path = "/api/integration/skl/containers/getRecipientOrderByTestList/listTests/{listTests}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/containers/getRecipientOrderByTestList/listTests/{listTests}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContainerSkl>> getRecipientOrderByTestList(
            @ApiPathParam(name = "listTests", description = "Cadena de examenes separados por comas") @PathVariable("listTests") String listTests
    ) throws Exception
    {
        List<ContainerSkl> listContainers = integrationSklService.getRecipientOrderByTestList(listTests);
        if (!listContainers.isEmpty())
        {
            return new ResponseEntity<>(listContainers, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene todos los recipientes asignados a una orden",
            path = "/api/integration/skl/containers/getRecipients/idOrder/{idOrder}/pendingToTake/{pendingToTake}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "containers/getRecipients/idOrder/{idOrder}/pendingToTake/{pendingToTake}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContainerSkl>> getRecipients(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder,
            @ApiPathParam(name = "pendingToTake", description = "Pendiente para toma de muestra") @PathVariable("pendingToTake") int pendingToTake
    ) throws Exception
    {
        List<ContainerSkl> listContainers = integrationSklService.getRecipients(idOrder, pendingToTake);
        if (!listContainers.isEmpty())
        {
            return new ResponseEntity<>(listContainers, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el codigo del tipo de una orden",
            path = "/api/integration/skl/getOrderCode/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/getOrderCode/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderCode(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSklService.getOrderCode(idOrder), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene la información basica del paciente de una orden",
            path = "/api/integration/skl/getPatientBasicInfo/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/getPatientBasicInfo/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPatientBasicInfo(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        String pacient = integrationSklService.getPatientBasicInfo(idOrder);
        if (!pacient.isEmpty())
        {
            return new ResponseEntity<>(pacient, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //----------------------- OBTIENE EL TIPO DE UNA ORDEN POR EL ID DE LA ORDEN -----------------------
    @ApiMethod(
            description = "Obtiene el tipo de orden por el id de la orden",
            path = "/api/integration/skl/getOrderType/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderType.class)
    @RequestMapping(value = "/getOrderType/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderType(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        String orderType = integrationSklService.getOrderType(idOrder);
        if (!orderType.isEmpty())
        {
            return new ResponseEntity<>(orderType, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ LISTAR ABREVIATURA DE LOS EXAMENES ----------------
    @ApiMethod(
            description = "Lista la abreviatura de los examenes",
            path = "/api/integration/skl/gettest",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/gettest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> listTests(
            @ApiBodyObject(clazz = ListTestOrderSkl.class) @RequestBody ListTestOrderSkl resultListTest
    ) throws Exception
    {
        List<String> listTests = integrationSklService.getTestByTestsIdSample(resultListTest);
        if (listTests.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listTests, HttpStatus.OK);
        }
    }

    //------------ TOMAR MUESTRA ----------------
    @ApiMethod(
            description = "Toma la muestra ingresada.",
            path = "/api/integration/skl/nextDestination/order/{order}/sample/{sampleCode}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/nextDestination/order/{order}/sample/{sampleCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> nextDestination(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sampleCode", description = "Muestra") @PathVariable(name = "sampleCode") String sampleCode
    ) throws Exception
    {
        int idNextDest = integrationSklService.nextDestination(idOrder, sampleCode);
        return new ResponseEntity<>(idNextDest, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista la abreviatura de los examenes",
            path = "/api/integration/skl/getPatientFormat",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/getPatientFormat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> getPatientFormat(
            @ApiBodyObject(clazz = RequestFormatDate.class) @RequestBody RequestFormatDate formateDate
    ) throws Exception
    {
        Patient patient = integrationSklService.getPatientByOrder(formateDate);
        if (patient != null)
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ COMENTARIOS DE LA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene los comentarios de una orden.",
            path = "/api/integration/skl/comments/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/comments/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listCommentOrder(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        String comments;
        comments = integrationSklService.listCommentOrder(idOrder);
        if (comments.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    //------------ DIAGNOSTICO DEL PACIENTE ----------------
    @ApiMethod(
            description = "Obtiene el diagnostico permanente de un paciente.",
            path = "/api/integration/skl/comments/patient/{patient}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/comments/patient/{patient}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listCommentPatien(
            @ApiPathParam(name = "patient", description = "Paciente") @PathVariable(name = "patient") int idPatient
    ) throws Exception
    {
        String comments;
        comments = integrationSklService.listCommentPatient(idPatient);
        if (comments.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    //------------ OBTIENE LAS PREGUNTAS ASOCIADAS A UNA ORDEN PARA SKL ----------------
    @ApiMethod(
            description = "Obtiene con el id de la orden todas las preguntas asociadas a esta orden",
            path = "/api/integration/skl/getOrderQuestions/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SklQuestion.class)
    @RequestMapping(value = "/getOrderQuestions/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SklQuestion>> getOrderQuestions(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        try
        {
            List<SklQuestion> listQuestionsForOrder = integrationSklService.getOrderQuestions(idOrder);
            if (!listQuestionsForOrder.isEmpty())
            {
                return new ResponseEntity<>(listQuestionsForOrder, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //------------ OBTIENE LAS RESPUESTAS ASOCIADAS A UNA PREGUNTA PARA SKL ----------------
    @ApiMethod(
            description = "Obtiene las respuestas cerradas asociadas a una pregunta",
            path = "/api/integration/skl/getAvailableAnswer/idQuestion/{idQuestion}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SklAnswer.class)
    @RequestMapping(value = "/getAvailableAnswer/idQuestion/{idQuestion}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SklAnswer>> getAvailableAnswer(
            @ApiPathParam(name = "idQuestion", description = "Id de la pregunta") @PathVariable("idQuestion") int idQuestion
    ) throws Exception
    {
        try
        {
            List<SklAnswer> listAnswers = integrationSklService.listAnswersByIdQuestion(idQuestion);
            if (listAnswers != null && !listAnswers.isEmpty())
            {
                return new ResponseEntity<>(listAnswers, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ OBTIENE LAS RESPUESTAS ASOCIADAS A UNA PREGUNTA PARA SKL ----------------
    @ApiMethod(
            description = "Obtiene todas las respuestas asociadas a una pregunta",
            path = "/api/integration/skl/getAnswers/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SklAnswer.class)
    @RequestMapping(value = "/getAnswers/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SklAnswer>> getAnswers(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        try
        {
            List<SklAnswer> listAnswers = integrationSklService.listAnswersByIdOrder(idOrder);
            if (listAnswers != null && !listAnswers.isEmpty())
            {
                return new ResponseEntity<>(listAnswers, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ OBTIENE LAS RESPUESTAS ASOCIADAS A UNA PREGUNTA PARA SKL ----------------
    @ApiMethod(
            description = "Obtiene todas las respuestas asociadas a una pregunta",
            path = "/api/integration/skl/getGeneralInterview",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SklAnswer.class)
    @RequestMapping(value = "/getGeneralInterview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SklQuestion>> getGeneralInterview() throws Exception
    {
        try
        {
            List<SklQuestion> listQuestions = integrationSklService.getGeneralInterview();
            if (listQuestions != null && !listQuestions.isEmpty())
            {
                return new ResponseEntity<>(listQuestions, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ OBTIENE LA LISTA DE DESTINOS DE LA MUESTRA ----------------
    @ApiMethod(
            description = "Obtiene la lista de destinos de la muestra",
            path = "/api/integration/skl/getSampleDestinations",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getSampleDestinations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SklSampleDestination>> getSampleDestinations(
            @ApiBodyObject(clazz = RequestSampleDestination.class) @RequestBody RequestSampleDestination requestSampleDestination
    ) throws Exception
    {
        try
        {
            List<SklSampleDestination> destinationList = integrationSklService.getSampleDestinations(requestSampleDestination);
            if (destinationList.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
            {
                return new ResponseEntity<>(destinationList, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ OBTIENE EL ID DE LA PREGUNTA QUE FUE ENVIADA COMO PARAMETRO SOLO SI ESA PREGUNTA EXISTE PARA ESA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene el id de la pregunta solo si el id de la orden y el id de la pregunta coinciden entre si con el registro, validando la existencia de esta pregunta para esa orden",
            path = "/api/integration/skl/getOrderQuestionId/idOrder/{idOrder}/idQuestion/{idQuestion}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/getOrderQuestionId/idOrder/{idOrder}/idQuestion/{idQuestion}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getOrderQuestionId(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder,
            @ApiPathParam(name = "idQuestion", description = "Id de la pregunta") @PathVariable("idQuestion") int idQuestion
    ) throws Exception
    {
        try
        {
            Integer idOfTheQuestion = integrationSklService.getOrderQuestionId(idOrder, idQuestion);
            if (idOfTheQuestion != null)
            {
                return new ResponseEntity<>(idOfTheQuestion, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualiza la respueta a una pregunta de entrevista",
            path = "/api/integration/skl/updateQuestion",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/updateQuestion", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateAnswer(
            @ApiBodyObject(clazz = SklOrderAnswer.class) @RequestBody SklOrderAnswer answer
    ) throws Exception
    {
        try
        {
            integrationSklService.updateAnswer(answer);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e)
        {
            SigaLog.info("ERROR IN UPDATE ANSWER IS " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    //------------ CREAR UNA NUEVA RESPUESTA PARA SKL----------------
    @ApiMethod(
            description = "Crea una nueva respuesta",
            path = "/api/integration/skl/questions/answer",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SklOrderAnswer.class)
    @RequestMapping(value = "/questions/answer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SklOrderAnswer> createAnswer(
            @ApiBodyObject(clazz = SklOrderAnswer.class) @RequestBody SklOrderAnswer answer
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSklService.createAnswer(answer), HttpStatus.OK);
    }

    //------------ Verifica la muestra en un destino ----------------
    @ApiMethod(
            description = "Verifica la muestra en un destino",
            path = "/api/integration/skl/checkSample/idSample/{idSample}/idDestination/{idDestination}/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Date.class)
    @RequestMapping(value = "/checkSample/idSample/{idSample}/idDestination/{idDestination}/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Date> checkSample(
            @ApiPathParam(name = "idSample", description = "Id de la muestra") @PathVariable("idSample") int idSample,
            @ApiPathParam(name = "idDestination", description = "Id del destino") @PathVariable("idDestination") int idDestination,
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        Date dateToVerified = integrationSklService.checkSample(idSample, idDestination, idOrder);
        if (dateToVerified == null)
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(dateToVerified, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Verifica la muestra con destinos ingresada.",
            path = "/api/integration/skl/destinationinitial/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/destinationinitial/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> destinationInitial(
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int idSample
    ) throws Exception
    {
        Integer destinationInitial = integrationSklService.destinationInitial(idSample);
        if (destinationInitial > 0)
        {

            return new ResponseEntity<>(destinationInitial, HttpStatus.OK);
        } else
        {

            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes con muestras pendientes por toma",
            path = "/api/integration/skl/getOrdersToTake/date/{date}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getOrdersToTake/date/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SklOrderToTake>> getOrdersToTake(
            @ApiPathParam(name = "date", description = "Fecha formato yyyyMMdd") @PathVariable(name = "date") int date
    ) throws Exception
    {
        List<SklOrderToTake> ordersToTakes = integrationSklService.getOrdersToTake(date);
        if (ordersToTakes != null && !ordersToTakes.isEmpty())
        {
            return new ResponseEntity<>(ordersToTakes, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    //------------ LISTAR LAS PREGUNTAS DE UNA ENTREVISTA CON CONSENTIMIENTO INFORMADO ----------------
    @ApiMethod(
            description = "Lista las preguntas con consentimiento informado",
            path = "/api/integration/skl/interviewinformedconsent",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = InterviewInformedConsent.class)
    @RequestMapping(value = "/interviewinformedconsent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InterviewInformedConsent>> listInformedConsent() throws Exception
    {
        List<InterviewInformedConsent> listConsent = integrationSklService.listInformedConsent();
        if (listConsent.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listConsent, HttpStatus.OK);
        }
    }

    //------------ OBTENER LOS EXAMENES CON CONSENTIMIENTO INFORMADO POR ORDEN ----------------    
    @ApiMethod(
            description = "Lista las pruebas con consentimiento firmado por orden",
            path = "/api/integration/skl/informedconsent/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderConsent.class)
    @RequestMapping(value = "/informedconsent/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderConsent> getOrderConsent(
            @ApiPathParam(name = "order", description = "Id de la orden") @PathVariable("order") long order
    ) throws Exception
    {
        OrderConsent orderConsent = integrationSklService.getOrderConsent(order);
        if (orderConsent != null)
        {
            return new ResponseEntity<>(orderConsent, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    //------------ CREA UN NUEVO CONSENTIMIENTO INFORMADO ----------------
    @ApiMethod(
            description = "Crea un nuevo consentimiento",
            path = "/api/integration/skl/newconsent",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderInformedConsent.class)
    @RequestMapping(value = "/newconsent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderInformedConsent> createConsent(
            @ApiBodyObject(clazz = OrderInformedConsent.class) @RequestBody OrderInformedConsent consentAnswer
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSklService.createConsent(consentAnswer), HttpStatus.OK);
    }

    //------------ OBTENER TODA LA INFORMACION POR ORDEN CON BASE 64 ----------------    
    @ApiMethod(
            description = "Toda la informacion por orden con base 64",
            path = "/api/integration/skl/consentbase64/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderConsentBase64.class)
    @RequestMapping(value = "/consentbase64/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderConsentBase64> getAllConsentInformed(
            @ApiPathParam(name = "order", description = "Id de la orden") @PathVariable("order") long order
    ) throws Exception
    {
        OrderConsentBase64 consentBase64 = integrationSklService.getAllConsentInformed(order);
        if (consentBase64 != null)
        {
            return new ResponseEntity<>(consentBase64, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    //------------ OBTENER LISTADO DE ORDENES POR HISTORIA Y TIPO DE DOCUMENTO----------------
    @ApiMethod(
            description = "Toda la informacion en listado de ordenes con base 64",
            path = "/api/integration/skl/consentbase64/history/{history}/documenttype/{documenttype}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/consentbase64/history/{history}/documenttype/{documenttype}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderConsentBase64>> getListBase64(
            @ApiPathParam(name = "history", description = "Nombre de la historia") @PathVariable("history") String history,
            @ApiPathParam(name = "documenttype", description = "tipo de documento") @PathVariable("documenttype") int documenttype
    ) throws Exception
    {
        List<OrderConsentBase64> listContainers = integrationSklService.getListBase64(history, documenttype);
        if (!listContainers.isEmpty())
        {
            return new ResponseEntity<>(listContainers, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ OBTENER LISTADO DE EXAMENES PERTENECIENTES A UNA MUESTRA ----------------    
    @ApiMethod(
            description = "listar examenes pertenecientes a una muestra",
            path = "/api/integration/skl/tests/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestConsent.class)
    @RequestMapping(value = "/tests/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestConsent>> getListTestsSample(
            @ApiPathParam(name = "order", description = "Id de la orden") @PathVariable("order") long order,
            @ApiPathParam(name = "sample", description = "Codigo de la muestra") @PathVariable("sample") String sample
    ) throws Exception
    {
        List<TestConsent> listTests = integrationSklService.getListTestsSample(order, sample);
        if (listTests != null)
        {
            return new ResponseEntity<>(listTests, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    // --------Consulta de pacientes con examenes pendientes-----
    @ApiMethod(
            description = "Listar examenes pendientes de paciente",
            path = "/api/integration/skl/patientTestPending/document/{document}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/patientTestPending/document/{document}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<patientTestPending>> getPatientTestPending(
            @ApiPathParam(name = "document", description = "No. documento del paciente") @PathVariable("document") String document
    ) throws Exception
    {

        List<patientTestPending> listPe = integrationSklService.getPatientTestPending(document);
        if (listPe != null)
        {
            return new ResponseEntity<>(listPe, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
