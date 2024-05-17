package net.cltech.enterprisent.controllers.tools;

import net.cltech.enterprisent.domain.tools.EventsResponse;
import net.cltech.enterprisent.service.interfaces.tools.events.EventsOrderService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios para eventos
 *
 * @version 1.0.0
 * @author equijano
 * @since 14/01/2019
 * @see Creacion
 */
@Api(
        name = "Eventos",
        group = "Herramientas",
        description = "Servicios de eventos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/events")
public class EventsController
{

    @Autowired
    private EventsOrderService eventsOrderService;

    //------------ VALIDAR URL ----------------
    @ApiMethod(
            description = "Validar url",
            path = "/api/events/validateUrl",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = EventsResponse.class)
    @RequestMapping(value = "/validateUrl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventsResponse> validateUrl(
            @ApiBodyObject(clazz = EventsResponse.class) @RequestBody EventsResponse url
    ) throws Exception
    {
        EventsResponse resp = eventsOrderService.validateUrl(url);
        if (resp.getResponse().equals("Validar url"))
        {
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
