package net.cltech.enterprisent.controllers.masters.common;

import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.service.interfaces.common.ListService;
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
 * Servicios para el acceso a las lista de items
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/04/2017
 * @see Creacion
 */
@Api(
        name = "Lista",
        group = "General",
        description = "Servicios de informacion de lista de items"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/lists")
public class ListController
{

    @Autowired
    private ListService service;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Listas de items",
            path = "/api/lists/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Item>> list(
            @ApiPathParam(name = "id", description = "Id del item padre") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<Item> list = service.list(id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar el valor adicional de un item de lista",
            path = "/api/lists",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Item.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> update(
            @ApiBodyObject(clazz = Item.class) @RequestBody Item item
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(item), HttpStatus.OK);
    }
}
