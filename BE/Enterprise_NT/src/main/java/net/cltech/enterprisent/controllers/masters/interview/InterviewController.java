package net.cltech.enterprisent.controllers.masters.interview;

import java.util.List;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.interview.InterviewDestinations;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.interview.TypeInterview;
import net.cltech.enterprisent.service.interfaces.masters.interview.InterviewService;
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
 * Servicios para el acceso a la informacion del maestro Entrevista
 *
 * @version 1.0.0
 * @author enavas
 * @since 18/08/2017
 * @see Creacion
 */
@Api(
        name = "Entrevista",
        group = "Entrevista",
        description = "Servicios de informacion del maestro Preguntas y Respuestas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/interviews")
public class InterviewController
{

    @Autowired
    private InterviewService interviewService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las entrevistas registradas",
            path = "/api/interviews",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Interview.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Interview>> list() throws Exception
    {
        List<Interview> list = interviewService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una entrevista",
            path = "/api/interviews/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Interview.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Interview> getById(
            @ApiPathParam(name = "id", description = "Id de la pregunta") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Interview interview = interviewService.get(id, null);
        if (interview == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(interview, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una entrevista",
            path = "/api/interviews/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Interview.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Interview> getByName(
            @ApiPathParam(name = "name", description = "Nombre de la entrevista") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Interview interview = interviewService.get(null, name);
        if (interview == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(interview, HttpStatus.OK);
        }
    }

    //------------ PREGUNTAS AOCIADAS EN LA ENTREVISTA ----------------
    @ApiMethod(
            description = "Obtiene las preguntas de una entrevista",
            path = "/api/interviews/filter/question/{idinterview}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Question.class)
    @RequestMapping(value = "/filter/question/{idinterview}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getByQuestion(
            @ApiPathParam(name = "idinterview", description = "Id de la  entrevista") @PathVariable(name = "idinterview") int idinterview
    ) throws Exception
    {
        List<Question> questions = interviewService.listQuestion(idinterview);
        if (questions.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }
    }

    //------------ TIPO DE ENTREVISTA AOCIADAS EN LA ENTREVISTA ----------------
    @ApiMethod(
            description = "Obtiene los tipos de entrevista de una entrevista",
            path = "/api/interviews/filter/typeinterview/idinterview/{idinterview}/type/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TypeInterview.class)
    @RequestMapping(value = "/filter/typeinterview/idinterview/{idinterview}/type/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TypeInterview>> getByTypeInterview(
            @ApiPathParam(name = "idinterview", description = "Id de la  entrevista") @PathVariable(name = "idinterview") int idinterview,
            @ApiPathParam(name = "type", description = "Tipo de Entrevista 1.Orden - 2.Laboratorio - 3.Examen ") @PathVariable(name = "type") int type
    ) throws Exception
    {
        List<TypeInterview> typeInterviews = interviewService.listTypeInterview(idinterview, type);
        if (typeInterviews.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(typeInterviews, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva entrevista",
            path = "/api/interviews",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Interview.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Interview> create(
            @ApiBodyObject(clazz = Interview.class) @RequestBody Interview interview
    ) throws Exception
    {
        return new ResponseEntity<>(interviewService.create(interview), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una entrevista",
            path = "/api/interviews",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Interview.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Interview> update(
            @ApiBodyObject(clazz = Interview.class) @RequestBody Interview interview
    ) throws Exception
    {
        return new ResponseEntity<>(interviewService.update(interview), HttpStatus.OK);
    }
    
    //------------ OBTIENE TODAS LAS RESPUESTAS ASOCIADAS A UNA ORDEN TANTO ABIERTAS COMO CERRADAS ----------------
    @ApiMethod(
            description = "Obtiene con el id de la orden todas las respuestas asociadas a esta orden",
            path = "/api/interviews/getAnswersByOrder/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getAnswersByOrder/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Answer>> getAnswersByOrder(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        try
        {
            List<Answer> listAnswersForOrder = interviewService.getAnswersByOrder(idOrder);
            if (!listAnswersForOrder.isEmpty())
            {
                return new ResponseEntity<>(listAnswersForOrder, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    //------------ CREAR RELACION ENTREVISTA - DESTINO ----------------
    @ApiMethod(
            description = "Crear relacion prueba medio de cultivo",
            path = "/api/interviews/destinationsofinterview",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/destinationsofinterview", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createMediaCultureTest(
            @ApiBodyObject(clazz = InterviewDestinations.class) @RequestBody InterviewDestinations interviewDestinations
    ) throws Exception
    {
        return new ResponseEntity<>(interviewService.createDestinationsInterview(interviewDestinations), HttpStatus.OK);
    }
    
    //------------ TODAS LAS PREGUNTAS AOCIADAS EN LA ENTREVISTA ----------------
    @ApiMethod(
            description = "Obtiene las preguntas de una entrevista",
            path = "/api/interviews/filter/allquestions/{idinterview}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Question.class)
    @RequestMapping(value = "/filter/allquestions/{idinterview}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getByAllQuestions(
            @ApiPathParam(name = "idinterview", description = "Id de la  entrevista") @PathVariable(name = "idinterview") int idinterview
    ) throws Exception
    {
        List<Question> questions = interviewService.listQuestionByInterview(idinterview);
        if (questions.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }
    }
    
    
}
