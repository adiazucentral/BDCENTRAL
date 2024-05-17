package net.cltech.enterprisent.controllers.operation.widgets;

import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.operation.widgets.WidgetEntry;
import net.cltech.enterprisent.domain.operation.widgets.WidgetOrderEntry;
import net.cltech.enterprisent.domain.operation.widgets.WidgetSample;
import net.cltech.enterprisent.service.interfaces.operation.widgets.WidgetService;
import net.cltech.enterprisent.tools.JWT;
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
 * Controlador de servicios Rest sobre la verificacion de la muestra
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/09/2017
 * @see Creacion
 */
@Api(
        name = "Widgets",
        group = "Widgets",
        description = "Servicios Rest sobre los widgets de la aplicación."
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/widgets")
public class WidgetController
{

    @Autowired
    private WidgetService widgetService;

    //------------ CONSULTA WIDGET MUESTRAS ----------------
    @ApiMethod(
            description = "Obtiene los valores para los widgets de ruta de la muestra",
            path = "/api/widgets/sample",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = WidgetSample.class)
    @RequestMapping(value = "/sample", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WidgetSample> getWidgetSampleToday(
            HttpServletRequest request
    ) throws Exception
    {
        WidgetSample widget = widgetService.getWidgetSample(JWT.decode(request).getBranch());
        if (widget == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(widget, HttpStatus.OK);
        }
    }

    //------------ CONSULTA WIDGET MUESTRAS ----------------
    @ApiMethod(
            description = "Obtiene los valores para los widgets de ingreso de muestra",
            path = "/api/widgets/orderentry/{date}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = WidgetSample.class)
    @RequestMapping(value = "/orderentry/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WidgetOrderEntry> widgetOrderEntry(
            HttpServletRequest request,
            @ApiPathParam(name = "date", description = "Fecha en formato yyyyMMdd") @PathVariable("date") int date
    ) throws Exception
    {
        WidgetOrderEntry widget = widgetService.widgetOrderEntry(date, JWT.decode(request).getBranch(), JWT.decode(request).getId());
        if (widget == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(widget, HttpStatus.OK);
        }
    }

    //------------ CONSULTA WIDGET INGRESO ----------------
    @ApiMethod(
            description = "Obtiene los valores para los widgets de ingreso",
            path = "/api/widgets/entry/{date}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = WidgetEntry.class)
    @RequestMapping(value = "/entry/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WidgetEntry> widgetEntry(
            HttpServletRequest request,
            @ApiPathParam(name = "date", description = "Fecha en formato yyyyMMdd") @PathVariable("date") int date
    ) throws Exception
    {
        WidgetEntry widget = widgetService.widgetEntry(date, JWT.decode(request).getBranch());
        if (widget == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(widget, HttpStatus.OK);
        }
    }
}
