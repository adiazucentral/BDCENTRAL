/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCultureTest;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MediaCultureService;
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
 * Servicios para el acceso a la informacion del maestro de Medio de cultivo
 *
 * @version 1.0.0
 * @author enavas
 * @since 11/08/2017
 * @see Creacion
 */
@Api(
        name = "Medio de Cultivo",
        group = "Prueba",
        description = "Servicios de informacion del maestro de Medio de cultivo"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/mediacultures")
public class MediaCultureController
{

    @Autowired
    private MediaCultureService mediaCultureService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los medio de cultivo registrados",
            path = "/api/mediacultures",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MediaCulture.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MediaCulture>> list() throws Exception
    {
        List<MediaCulture> list = mediaCultureService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR POR ESTADO ----------------
    @ApiMethod(
            description = "Lista los medio de cultivo registradas por estado",
            path = "/api/mediacultures/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MediaCulture.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MediaCulture>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<MediaCulture> list = mediaCultureService.list(state);
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
            description = "Obtiene la informacion de un medio de cultivo",
            path = "/api/mediacultures/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MediaCulture.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MediaCulture> getById(
            @ApiPathParam(name = "id", description = "Id del medio de cultivo") @PathVariable(name = "id") int id
    ) throws Exception
    {
        MediaCulture mediaCulture = mediaCultureService.get(id, null, null);
        if (mediaCulture == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(mediaCulture, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un medio de cultivo",
            path = "/api/mediacultures/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MediaCulture.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MediaCulture> getByCode(
            @ApiPathParam(name = "code", description = "Codigo del medio de cultivo") @PathVariable(name = "code") String code
    ) throws Exception
    {
        MediaCulture mediaCulture = mediaCultureService.get(null, code, null);
        if (mediaCulture == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(mediaCulture, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un medio de cultivo",
            path = "/api/mediacultures/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MediaCulture.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MediaCulture> getByName(
            @ApiPathParam(name = "name", description = "Nombre del medio de cultivo") @PathVariable(name = "name") String name
    ) throws Exception
    {
        MediaCulture mediaCulture = mediaCultureService.get(null, null, name);
        if (mediaCulture == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(mediaCulture, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un medio de cultivo",
            path = "/api/mediacultures",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MediaCulture.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MediaCulture> create(
            @ApiBodyObject(clazz = MediaCulture.class) @RequestBody MediaCulture mediaCulture
    ) throws Exception
    {
        return new ResponseEntity<>(mediaCultureService.create(mediaCulture), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un medio de cultivo",
            path = "/api/mediacultures",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MediaCulture.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MediaCulture> update(
            @ApiBodyObject(clazz = MediaCulture.class) @RequestBody MediaCulture mediaCulture
    ) throws Exception
    {
        return new ResponseEntity<>(mediaCultureService.update(mediaCulture), HttpStatus.OK);
    }

    //------------ CREAR RELACION CON PRUEBA-MEDIO DE CULTIVO ----------------
    @ApiMethod(
            description = "Crear relacion prueba medio de cultivo",
            path = "/api/mediacultures/testofmediacultures",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/testofmediacultures", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createMediaCultureTest(
            @ApiBodyObject(clazz = MediaCultureTest.class) @RequestBody MediaCultureTest mediaCultureTest
    ) throws Exception
    {
        return new ResponseEntity<>(mediaCultureService.createMediaCultureTest(mediaCultureTest), HttpStatus.OK);
    }

    //------------ CONSULTA POR ID EXAMEN ----------------
    @ApiMethod(
            description = "Obtiene los medios de cultivo de una prueba",
            path = "/api/mediacultures/filter/test/{testid}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MediaCulture.class)
    @RequestMapping(value = "/filter/test/{testid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MediaCultureTest> getByIdTest(
            @ApiPathParam(name = "testid", description = "Id del examen") @PathVariable(name = "testid") int testid
    ) throws Exception
    {
        MediaCultureTest mediaCultureTest = mediaCultureService.listMediaCulture(testid);
        if (mediaCultureTest.getMediaCultures().isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(mediaCultureTest, HttpStatus.OK);
        }
    }

    //------------ CONSULTA EXAMEN ASOCIADO A UN MEDIO DE CULTIVO Y MUESTRA ----------------
    @ApiMethod(
            description = "Obtiene el examen que este asociado al medio de cultivo y muestra.",
            path = "/api/mediacultures/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestBasic.class)
    @RequestMapping(value = "/test/{test}/mediaculture/{mediaculture}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestBasic> getMediaCultureTest(
            @ApiPathParam(name = "test", description = "Id del examen") @PathVariable(name = "test") int idTest
    ) throws Exception
    {
        TestBasic test = mediaCultureService.getMediaCultureTest(idTest);
        if (test == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(test, HttpStatus.OK);
        }
    }

}
