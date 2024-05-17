package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.StandardizationDemographic;
import net.cltech.enterprisent.domain.masters.test.CentralSystem;
import net.cltech.enterprisent.domain.masters.test.Standardization;
import net.cltech.enterprisent.domain.masters.user.StandardizationUser;
import net.cltech.enterprisent.service.interfaces.masters.test.CentralSystemService;
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
 * Servicios para el acceso a la informacion del maestro Sistema Central
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/04/2017
 * @see Creacion
 */
@Api(
        name = "Sistema Central",
        group = "Prueba",
        description = "Servicios de informacion del maestro Sistema Central"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/centralsystems")
public class CentralSystemController
{

    @Autowired
    private CentralSystemService centralSystemService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los sistemas centrales registrados",
            path = "/api/centralsystems",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CentralSystem.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CentralSystem>> list() throws Exception
    {
        List<CentralSystem> list = centralSystemService.list();
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
            description = "Lista los sistemas centrales registrados por estado",
            path = "/api/centralsystems/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CentralSystem.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CentralSystem>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<CentralSystem> list = centralSystemService.list(state);
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
            description = "Obtiene la informacion de un sistema central",
            path = "/api/centralsystems/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CentralSystem.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CentralSystem> getById(
            @ApiPathParam(name = "id", description = "Id del sistema central") @PathVariable(name = "id") int id
    ) throws Exception
    {
        CentralSystem centralSystem = centralSystemService.get(id, null);
        if (centralSystem == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(centralSystem, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un sistema central",
            path = "/api/centralsystems/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CentralSystem.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CentralSystem> getByName(
            @ApiPathParam(name = "name", description = "Nombre del sistema central") @PathVariable(name = "name") String name
    ) throws Exception
    {
        CentralSystem centralSystem = centralSystemService.get(null, name);
        if (centralSystem == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(centralSystem, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE EL ID----------------
    @ApiMethod(
            description = "Obtiene la informacion de un sistema central",
            path = "/api/centralsystems/filter/nameid/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = CentralSystem.class)
    @RequestMapping(value = "/filter/nameid/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getIdByName(
            @ApiPathParam(name = "name", description = "Nombre del sistema central") @PathVariable(name = "name") String name
    ) throws Exception
    {
        CentralSystem centralSystem = centralSystemService.get(null, name);
        if (centralSystem == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(centralSystem.getId(), HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo sistema central",
            path = "/api/centralsystems",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CentralSystem.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CentralSystem> create(
            @ApiBodyObject(clazz = CentralSystem.class) @RequestBody CentralSystem centralSystem
    ) throws Exception
    {
        return new ResponseEntity<>(centralSystemService.create(centralSystem), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un sistema central",
            path = "/api/centralsystems",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CentralSystem.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CentralSystem> update(
            @ApiBodyObject(clazz = CentralSystem.class) @RequestBody CentralSystem centralSystem
    ) throws Exception
    {
        return new ResponseEntity<>(centralSystemService.update(centralSystem), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Homologa examen de un sistema central",
            path = "/api/centralsystems/standardization/test/",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Standardization.class)
    @RequestMapping(value = "/standardization/test/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Standardization> addTestStandardization(
            @ApiBodyObject(clazz = Standardization.class) @RequestBody Standardization standardization
    ) throws Exception
    {
        return new ResponseEntity<>(centralSystemService.addStandardizationTest(standardization), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista los examenes para su homologacion",
            path = "/api/centralsystems/standardization/test/{system}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Standardization.class)
    @RequestMapping(value = "/standardization/test/{system}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Standardization>> standardizationList(
            @ApiPathParam(name = "system", description = "Sistema central") @PathVariable(name = "system") int system
    ) throws Exception
    {
        List<Standardization> list = centralSystemService.standardizationList(system, false);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Verifica si ya se encuentra un código homologado ",
            path = "/api/centralsystems/standardization/test/exists/{system}/{test}/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/standardization/test/exists/{system}/{test}/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> standardizationCodeExists(@ApiPathParam(name = "system", description = "Sistema central") @PathVariable(name = "system") int system,
            @ApiPathParam(name = "test", description = "Examen") @PathVariable(name = "test") int test,
            @ApiPathParam(name = "code", description = "Código homologacion") @PathVariable(name = "code") String code) throws Exception
    {
        Boolean exists = false;
        if (code != null && !code.trim().isEmpty())
        {
            exists = centralSystemService.standardizationCodeExists(system, code.trim().toUpperCase(), test);
        }
        return new ResponseEntity<>(exists, HttpStatus.OK);

    }

    //------------ LISTA DE DEMOGRAFICOS ITEM ----------------
    @ApiMethod(
            description = "Lista de demograficos item por sistema central y demografico",
            path = "/api/centralsystems/standardization/demographics/system/{system}/demographic/{demographic}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StandardizationDemographic.class)
    @RequestMapping(value = "/standardization/demographics/system/{system}/demographic/{demographic}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StandardizationDemographic>> standardizationDemographics(
            @ApiPathParam(name = "system", description = "Sistema central") @PathVariable(name = "system") int system,
            @ApiPathParam(name = "demographic", description = "Demografico") @PathVariable(name = "demographic") int demographic
    ) throws Exception
    {
        List<StandardizationDemographic> list = centralSystemService.demographicsItemList(system, demographic);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ INSERTAR HOMOLOGACION DE DEMOGRAFICOS ----------------
    @ApiMethod(
            description = "Homologa demograficos item de un demografico y sistema central",
            path = "/api/centralsystems/standardization/demographics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StandardizationDemographic.class)
    @RequestMapping(value = "/standardization/demographics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardizationDemographic> addTestStandardization(
            @ApiBodyObject(clazz = StandardizationDemographic.class) @RequestBody StandardizationDemographic demographic
    ) throws Exception
    {
        return new ResponseEntity<>(centralSystemService.insertStandardizationDemographic(demographic), HttpStatus.OK);
    }

    //------------ VALIDAR CODIGO DE HOMOLOGACIÓN ----------------
    @ApiMethod(
            description = "Verifica si ya se encuentra un código homologado ",
            path = "/api/centralsystems/standardization/demographics/exists/{system}/{demographic}/{demographicitem}/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/standardization/demographics/exists/{system}/{demographic}/{demographicitem}/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> standardizationDemoCodeExists(@ApiPathParam(name = "system", description = "Sistema central") @PathVariable(name = "system") int system,
            @ApiPathParam(name = "demographic", description = "Demografico") @PathVariable(name = "demographic") int demographic,
            @ApiPathParam(name = "demographicitem", description = "Item del Demografico") @PathVariable(name = "demographicitem") int demographicItem,
            @ApiPathParam(name = "code", description = "Código homologacion") @PathVariable(name = "code") String code) throws Exception
    {
        Boolean exists = false;
        if (code != null && !code.trim().isEmpty())
        {
            exists = centralSystemService.standardizationCodeExists(system, code.trim().toUpperCase(), demographic, demographicItem);
        }
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    //------------ IMPORTAR ----------------
    @ApiMethod(
            description = "Importar homologacion de demograficos.",
            path = "/api/centralsystems/standardization/demographics/import",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StandardizationDemographic.class)
    @RequestMapping(value = "/standardization/demographics/import", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> Import(
            @ApiBodyObject(clazz = StandardizationDemographic.class) @RequestBody List<StandardizationDemographic> demographics
    ) throws Exception
    {
        return new ResponseEntity<>(centralSystemService.insertStandardizationDemographicAll(demographics), HttpStatus.OK);
    }

    //------------ LISTA DE USUARIOS POR SISTEMA CENTRAL ----------------
    @ApiMethod(
            description = "Lista de usuarios por sistema central",
            path = "/api/centralsystems/standardization/users/system/{system}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StandardizationUser.class)
    @RequestMapping(value = "/standardization/users/system/{system}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StandardizationUser>> standardizationUsers(
            @ApiPathParam(name = "system", description = "Sistema central") @PathVariable(name = "system") int system
    ) throws Exception
    {
        List<StandardizationUser> list = centralSystemService.usersList(system);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ INSERTAR HOMOLOGACION DE USUARIOS ----------------
    @ApiMethod(
            description = "Homologa usuarios",
            path = "/api/centralsystems/standardization/users",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StandardizationUser.class)
    @RequestMapping(value = "/standardization/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardizationUser> addUserStandardization(
            @ApiBodyObject(clazz = StandardizationDemographic.class) @RequestBody StandardizationUser user
    ) throws Exception
    {
        return new ResponseEntity<>(centralSystemService.insertStandardizationUser(user), HttpStatus.OK);
    }

    //------------ VALIDAR CODIGO DE HOMOLOGACIÓN ----------------
    @ApiMethod(
            description = "Verifica si ya se encuentra un código homologado ",
            path = "/api/centralsystems/standardization/users/exists/{system}/{user}/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/standardization/users/exists/{system}/{user}/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> standardizationUserCodeExists(@ApiPathParam(name = "system", description = "Sistema central") @PathVariable(name = "system") int system,
            @ApiPathParam(name = "user", description = "Usuario") @PathVariable(name = "user") int user,
            @ApiPathParam(name = "code", description = "Código homologacion") @PathVariable(name = "code") String code) throws Exception
    {
        Boolean exists = false;
        if (code != null && !code.trim().isEmpty())
        {
            exists = centralSystemService.standardizationUserCodeExists(system, code.trim().toUpperCase(), user);
        }
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    //------------ CONSULTA POR NOMBRE PARA INTERFAZ ----------------
    @ApiResponseObject(clazz = CentralSystem.class)
    @RequestMapping(value = "/filter/nameCentralSystem/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getStatusCentralSystemForName(@PathVariable(name = "name") String name
    ) throws Exception
    {
        CentralSystem centralSystem = centralSystemService.get(null, name);
        if (centralSystem == null)
        {
            return new ResponseEntity<>(false, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(centralSystem.isState(), HttpStatus.OK);
        }
    }

}
