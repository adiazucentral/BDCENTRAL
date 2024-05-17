package net.cltech.enterprisent.controllers.masters.test;

import net.cltech.enterprisent.domain.masters.test.GeneralTemplateOption;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.service.interfaces.masters.test.TemplateService;
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
 * Servicios REST plantilla resultado
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 01/08/2017
 * @see Creacion
 */
@Api(
        group = "Prueba",
        name = "Plantilla resultado",
        description = "Servicios sobre plantilla resultados"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/templates")
public class TemplateController
{
    @Autowired
    private TemplateService service;

    @ApiMethod(
            description = "Obtiene plantilla resultados de un examen",
            path = "/api/templates/filter/test/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = GeneralTemplateOption.class)
    @RequestMapping(value = "/filter/test/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralTemplateOption> getById(
            @ApiPathParam(name = "id", description = "Id de examen") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        GeneralTemplateOption records = service.getById(id);
        if (records != null)
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(
            description = "Crea plantilla resultado",
            path = "/api/templates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(
            @ApiBodyObject(clazz = OptionTemplate.class) @RequestBody GeneralTemplateOption create
    ) throws Exception
    {
        service.create(create);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina plantilla",
            path = "/api/templates/test/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @RequestMapping(value = "/test/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(
            @ApiPathParam(name = "id", description = "Id de examen") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
