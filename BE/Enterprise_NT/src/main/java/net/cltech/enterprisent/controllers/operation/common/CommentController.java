/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.common;

import java.util.List;
import net.cltech.enterprisent.domain.operation.microbiology.CommentMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
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
 * Controlador de servicios Rest sobre estadisticas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 28/02/2018
 * @see Creacion
 */
@Api(
        name = "Comentarios Orden",
        group = "Operación - Común",
        description = "Servicios Rest sobre los comentarios de la orden, microbiologia, resultados y el diagnostico permante del paciente"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/comments")
public class CommentController
{

    @Autowired
    private CommentService commentService;

    //------------ COMENTARIOS DE LA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene los comentarios de una orden.",
            path = "/api/comments/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CommentOrder.class)
    @RequestMapping(value = "/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentOrder>> listCommentOrder(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        List<CommentOrder> comments = commentService.listCommentOrder(idOrder, null);
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
            path = "/api/comments/patient/{patient}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CommentOrder.class)
    @RequestMapping(value = "/patient/{patient}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentOrder>> listCommentOrder(
            @ApiPathParam(name = "patient", description = "Paciente") @PathVariable(name = "patient") int idPatient
    ) throws Exception
    {
        List<CommentOrder> comments = commentService.listCommentOrder(null, idPatient);
        if (comments.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    //------------ COMENTARIOS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene los comentarios de microbiologia por orden y examen.",
            path = "/api/comments/microbiology/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CommentMicrobiology.class)
    @RequestMapping(value = "/microbiology/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentMicrobiology>> listCommentMicrobiologyTest(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "test", description = "Examen") @PathVariable(name = "test") int idTest
    ) throws Exception
    {
        List<CommentMicrobiology> comments = commentService.listCommentMicrobiology(order, idTest, null);
        if (comments.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    //------------ COMENTARIOS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene los comentarios de microbiologia por orden y muestra.",
            path = "/api/comments/microbiology/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CommentMicrobiology.class)
    @RequestMapping(value = "/microbiology/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentMicrobiology>> listCommentMicrobiologySample(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int idSample
    ) throws Exception
    {
        List<CommentMicrobiology> comments = commentService.listCommentMicrobiology(order, null, idSample);
        if (comments.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    //------------ COMENTARIOS DE ORDEN O PACIENTE ----------------
    @ApiMethod(
            description = "Inserta comentarios de la orden.",
            path = "/api/comments/order",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CommentOrder.class)
    @RequestMapping(value = "/order", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertCommentOrder(
            @ApiBodyObject(clazz = CommentOrder.class) @RequestBody List<CommentOrder> commentsOrder
    ) throws Exception
    {
        return new ResponseEntity<>(commentService.commentOrder(commentsOrder), HttpStatus.OK);
    }

    //------------ COMENTARIOS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Inserta comentarios de microbiologia.",
            path = "/api/comments/microbiology",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CommentMicrobiology.class)
    @RequestMapping(value = "/microbiology", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertCommentMicrobiology(
            @ApiBodyObject(clazz = CommentMicrobiology.class) @RequestBody List<CommentMicrobiology> commentsMicrobiology
    ) throws Exception
    {
        return new ResponseEntity<>(commentService.commentMicrobiology(commentsMicrobiology), HttpStatus.OK);
    }

    //------------ TRAZABILIDAD DE COMENTARIOS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene los comentarios de microbiologia por orden y muestra.",
            path = "/api/comments/microbiology/tracking/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CommentMicrobiology.class)
    @RequestMapping(value = "/microbiology/tracking/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentMicrobiology>> listCommentMicrobiologyTracking(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "test", description = "Examen") @PathVariable(name = "test") int idTest
    ) throws Exception
    {
        List<CommentMicrobiology> comments = commentService.listCommentMicrobiologyTracking(order, idTest);
        if (comments.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    //------------ TRAZABILIDAD DE COMENTARIOS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene los comentarios de orden o paciente.",
            path = "/api/comments/order/tracking/record/{record}/type/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CommentMicrobiology.class)
    @RequestMapping(value = "/order/tracking/record/{record}/type/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentOrder>> listCommentOrderTracking(
            @ApiPathParam(name = "record", description = "Orden o id Paciente") @PathVariable(name = "record") long record,
            @ApiPathParam(name = "type", description = "Indica si la consulta es hacia la orden o el paciente") @PathVariable(name = "type") int type
    ) throws Exception
    {
        List<CommentOrder> comments = commentService.listCommentOrderTracking(record, type);
        if (comments.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }
}
