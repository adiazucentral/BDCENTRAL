package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Diagnostic;
import net.cltech.enterprisent.domain.masters.test.DiagnosticByTest;
import net.cltech.enterprisent.domain.masters.test.ListDiagnosticByTest;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestByDiagnostic;
import net.cltech.enterprisent.service.impl.enterprisent.masters.test.DiagnosticServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.masters.test.DiagnosticService;
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
 * Servicios para el acceso a la informacion del maestro Diagnosticos
 *
 * @version 1.0.0
 * @author enavas
 * @since 21/06/2017
 * @see Creacion
 */
@Api(
        name = "Diagnostico",
        group = "Prueba",
        description = "Servicios sobre los Diagnosticos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/diagnostics")
public class DiagnosticController
{

    @Autowired
    private DiagnosticService diagnosticService;
    @Autowired
    private DiagnosticServiceEnterpriseNT diagnosticServiceEnterpriseNT;

    @ApiMethod(
            description = "Lista los diagnosticos registrados",
            path = "/api/diagnostics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Diagnostic>> list() throws Exception
    {
        List<Diagnostic> list = diagnosticService.list();
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
            description = "Obtiene la informacion de un diagnostico",
            path = "/api/diagnostics/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Diagnostic>> getByid(
            @ApiPathParam(name = "id", description = "Id del diagnostico") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<Diagnostic> list = diagnosticService.get(id, null, null, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un diagnostico",
            path = "/api/diagnostics/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Diagnostic> getByCode(
            @ApiPathParam(name = "code", description = "Codigo del diagnostico 8 caracteres ") @PathVariable(name = "code") String code
    ) throws Exception
    {
        List<Diagnostic> list = diagnosticService.get(null, code, null, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list.get(0), HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un diagnostico",
            path = "/api/diagnostics/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Diagnostic> getByName(
            @ApiPathParam(name = "name", description = "Nombre del diagnostico") @PathVariable(name = "name") String name
    ) throws Exception
    {
        List<Diagnostic> list = diagnosticService.get(null, null, name, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list.get(0), HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR TIPO ----------------
    @ApiMethod(
            description = "Obtiene los diagnosticos filtrado por tipo",
            path = "/api/diagnostics/filter/type/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(value = "/filter/type/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Diagnostic>> getBytype(
            @ApiPathParam(name = "type", description = "Tipo de diagnostico") @PathVariable(name = "type") int type
    ) throws Exception
    {
        List<Diagnostic> list = diagnosticService.get(null, null, null, type, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ESTADO ----------------
    @ApiMethod(
            description = "Obtiene los diagnosticos filtrado por estado",
            path = "/api/diagnostics/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Diagnostic>> getByState(
            @ApiPathParam(name = "state", description = "Estado del diagnosticos") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Diagnostic> list = diagnosticService.get(null, null, null, null, state);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo diagnostico",
            path = "/api/diagnostics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Diagnostic> create(
            @ApiBodyObject(clazz = Diagnostic.class) @RequestBody Diagnostic diagnostic
    ) throws Exception
    {
        return new ResponseEntity<>(diagnosticService.create(diagnostic), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un diagnostico",
            path = "/api/diagnostics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Diagnostic> update(
            @ApiBodyObject(clazz = Diagnostic.class) @RequestBody Diagnostic diagnostic
    ) throws Exception
    {
        return new ResponseEntity<>(diagnosticService.update(diagnostic), HttpStatus.OK);
    }

    //------------ IMPORTAR ----------------
    @ApiMethod(
            description = "Importar diagnosticos",
            path = "/api/diagnostics/import",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> importDiagnostic(
            @ApiBodyObject(clazz = Diagnostic.class) @RequestBody List<Diagnostic> diagnostics
    ) throws Exception
    {
        return new ResponseEntity<>(diagnosticService.createAll(diagnostics), HttpStatus.OK);
    }

    //------------ ASIGNACION ----------------
    @ApiMethod(
            description = "Actualiza asignacion de diagnosticos al examen",
            path = "/api/diagnostics/assign/test",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/assign/test", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assignAntibiotics(
            @ApiBodyObject(clazz = DiagnosticByTest.class) @RequestBody(required = true) DiagnosticByTest update
    ) throws Exception
    {
        return new ResponseEntity<>(diagnosticService.assignToTest(update), HttpStatus.OK);
    }

    //------------ ASIGNACION ----------------
    @ApiMethod(
            description = "Actualiza asignacion de exmanes al diagnostico",
            path = "/api/diagnostics/assign/diagnostic",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/assign/diagnostic", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assignTestToDiagnostic(
            @ApiBodyObject(clazz = TestByDiagnostic.class) @RequestBody(required = true) TestByDiagnostic update
    ) throws Exception
    {
        return new ResponseEntity<>(diagnosticService.assignToTest(update), HttpStatus.OK);
    }

    //------------ CONSULTA POR EXAMEN ----------------
    @ApiMethod(
            description = "Obtiene los diagnosticos filtrado por examen",
            path = "/api/diagnostics/filter/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(value = "/filter/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Diagnostic>> getByState(
            @ApiPathParam(name = "test", description = "Id del examen") @PathVariable(name = "test") int test
    ) throws Exception
    {
        List<Diagnostic> list = diagnosticService.listByTest(test);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ PRUEBAS POR DIAGNOSTICO ----------------
    @ApiMethod(
            description = "Obtiene las pruebas que estan asociadas a un diagnostico",
            path = "/api/diagnostics/test/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/test/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Test>> getByDiagnostic(
            @ApiPathParam(name = "id", description = "Id del diagnostico") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        List<Test> list = diagnosticService.listTests(id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ PRUEBAS POR DIAGNOSTICO ----------------
    @ApiMethod(
            description = "Obtiene las pruebas y se mascan si estan asociadas a un diagnostico",
            path = "/api/diagnostics/test/filter/diagnostic/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/test/filter/diagnostic/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Test>> getTestWithDiagnostic(
            @ApiPathParam(name = "id", description = "Id del diagnostico") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        List<Test> list = diagnosticService.listTestWithDiagnostic(id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CREAR TARIFAS POR CLIENTE ----------------
    @ApiMethod(
            description = "Agrupa diagnostico por pruebas",
            path = "/api/diagnostics/groupbytest",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/groupbytest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DiagnosticByTest>> groupByTest(
            @ApiBodyObject(clazz = String.class) @RequestBody ListDiagnosticByTest listDiagnosticByTest
    ) throws Exception
    {
        List<DiagnosticByTest> list = diagnosticServiceEnterpriseNT.listTest(listDiagnosticByTest);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene los diagnosticos por orden",
            path = "/api/diagnostics/diagnosticsbyorder/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Diagnostic.class)
    @RequestMapping(value = "/diagnosticsbyorder/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Diagnostic>> ListDiagnostic(
            @ApiPathParam(name = "order", description = "Numero de orden") @PathVariable("order") long order
    ) throws Exception
    {
        List<Diagnostic> list = diagnosticService.ListDiagnostics(order);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

}
