/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.microbiology.TestProcedure;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.ProcedureService;
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
 * Servicios para el acceso a la informacion del maestro Procedimiento
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/08/2017
 * @see Creacion
 */
@Api(
        name = "Procedimiento",
        group = "Microbiología",
        description = "Servicios de informacion del maestro Procedimiento"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/procedures")
public class ProcedureController
{

    @Autowired
    private ProcedureService procedureService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los procedimientos registrados",
            path = "/api/procedures",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Procedure.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Procedure>> list() throws Exception
    {
        List<Procedure> list = procedureService.list();
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
            description = "Lista los procedimientos registrados por estado",
            path = "/api/procedures/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Procedure.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Procedure>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Procedure> list = procedureService.list(state);
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
            description = "Obtiene la informacion de un procedimiento",
            path = "/api/procedures/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Procedure.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Procedure> getById(
            @ApiPathParam(name = "id", description = "Id del procedimiento") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Procedure procedure = procedureService.get(id, null, null);
        if (procedure == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(procedure, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un procedimiento",
            path = "/api/procedures/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Procedure.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Procedure> getByCode(
            @ApiPathParam(name = "code", description = "Codigo del procedimiento") @PathVariable(name = "code") String code
    ) throws Exception
    {
        Procedure procedure = procedureService.get(null, code, null);
        if (procedure == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(procedure, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un procedimiento",
            path = "/api/procedures/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Procedure.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Procedure> getByName(
            @ApiPathParam(name = "name", description = "Nombre del procedimiento") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Procedure procedure = procedureService.get(null, null, name);
        if (procedure == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(procedure, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo procedimiento",
            path = "/api/procedures",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Procedure.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Procedure> create(
            @ApiBodyObject(clazz = Procedure.class) @RequestBody Procedure procedure
    ) throws Exception
    {
        return new ResponseEntity<>(procedureService.create(procedure), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un procedimiento",
            path = "/api/procedures",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Procedure.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Procedure> update(
            @ApiBodyObject(clazz = Procedure.class) @RequestBody Procedure procedure
    ) throws Exception
    {
        return new ResponseEntity<>(procedureService.update(procedure), HttpStatus.OK);
    }

    //------------ CREAR RELACION CON PRUEBA-PROCEDIMIENTO----------------
    @ApiMethod(
            description = "Inserta examenes con relacion prueba-procedimiento",
            path = "/api/procedures/testprocedure",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/testprocedure", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertTestProcedure(
            @ApiBodyObject(clazz = TestProcedure.class) @RequestBody List<TestProcedure> testProcedures
    ) throws Exception
    {
        return new ResponseEntity<>(procedureService.insertTestProcedure(testProcedures), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista los examenes con relacion prueba-procedimiento",
            path = "/api/procedures/testprocedure/filter/idtest/{idtest}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestProcedure.class)
    @RequestMapping(value = "/testprocedure/filter/idtest/{idtest}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestProcedure>> listTestProcedure(
            @ApiPathParam(name = "idtest", description = "Id del examen") @PathVariable(name = "idtest") Integer idtest
    ) throws Exception
    {
        List<TestProcedure> testProcedures = procedureService.listTestProcedure(idtest);
        if (testProcedures.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(testProcedures, HttpStatus.OK);
        }
    }

}
