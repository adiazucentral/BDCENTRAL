package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.integration.homebound.ProfileHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.TestHomeBound;
import net.cltech.enterprisent.domain.masters.demographic.PypDemographic;
import net.cltech.enterprisent.domain.masters.test.AutomaticTest;
import net.cltech.enterprisent.domain.masters.test.Concurrence;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Profile;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.TestByLaboratory;
import net.cltech.enterprisent.domain.masters.test.TestByService;
import net.cltech.enterprisent.domain.masters.test.TestInformation;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationHomeBoundService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
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
 * Servicios para el acceso a la informacion de la maestro Pruebas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/06/2017
 * @see Creacion
 */
@Api(
        name = "Prueba",
        group = "Prueba",
        description = "Servicios de informacion de la maestro Pruebas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/tests")
public class TestController
{

    @Autowired
    private TestService testService;

    @Autowired
    private IntegrationHomeBoundService integrationHomeBoundService;

    //------------ LISTAR POR TIPO, ESTADO Y AREA ----------------
    @ApiMethod(
            description = "Lista las pruebas registradas por tipo, estado y area. <br> "
            + "Tipo de prueba: 0 -> Examenes, 1 -> Perfiles, 2 -> Paquetes, 3 -> Examenes y Perfiles, 4 -> Perfiles y Paquetes, 5 -> Todos. <br>"
            + "Estado: 0 -> Todos, 1 -> Activos, 2 -> Inactivos. <br>"
            + "Area: idArea, 0 -> Todas",
            path = "/api/tests/filter/type/{type}/state/{state}/area/{area}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/filter/type/{type}/state/{state}/area/{area}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> list(
            @ApiPathParam(name = "type", description = "Tipo Prueba") @PathVariable(name = "type") int type,
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") int state,
            @ApiPathParam(name = "area", description = "Area") @PathVariable(name = "area") int area
    ) throws Exception
    {
        List<TestBasic> list = testService.list(type, state == 0 ? null : state == 1, area == 0 ? null : area);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
        //------------ LISTAR POR TIPO, ESTADO Y AREA ----------------
    @ApiMethod(
            description = "Lista las pruebas registradas por tipo, estado y area. <br> "
            + "Tipo de prueba: 0 -> Examenes, 1 -> Perfiles, 2 -> Paquetes, 3 -> Examenes y Perfiles, 4 -> Perfiles y Paquetes, 5 -> Todos. <br>"
            + "Estado: 0 -> Todos, 1 -> Activos, 2 -> Inactivos. <br>"
            + "Area: idArea, 0 -> Todas",
            path = "/api/tests/filter/laboratory/type/{type}/state/{state}/area/{area}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/filter/laboratory/type/{type}/state/{state}/area/{area}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listTestLaboratory(
            @ApiPathParam(name = "type", description = "Tipo Prueba") @PathVariable(name = "type") int type,
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") int state,
            @ApiPathParam(name = "area", description = "Area") @PathVariable(name = "area") int area
    ) throws Exception
    {
        List<TestBasic> list = testService.listBranch(type, state == 0 ? null : state == 1, area == 0 ? null : area);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR PRUEBAS DE FORMULA ----------------
    @ApiMethod(
            description = "Lista las pruebas en las que se encuentre la prueba en formula.",
            path = "/api/tests/filter/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/filter/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listByTest(
            @ApiPathParam(name = "test", description = "Prueba") @PathVariable(name = "test") int test
    ) throws Exception
    {
        List<TestBasic> list = testService.list(test);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR POR TIPO DE RESULTADO ----------------
    @ApiMethod(
            description = "Lista pruebas por tipo de resultado. <br>"
            + "Tipo de Resultado: 1 -> Numerico, 2 -> Texto.<br>"
            + "Estado: 0 -> Todos, 1 -> Activos, 2 -> Inactivos. <br>"
            + "Area: idArea, 0 -> Todas. <br>"
            + "Procesamiento Por: 1 -> Manual, 2 -> Middleware, 0 -> Todos",
            path = "/api/tests/filter/resulttype/{resulttype}/state/{state}/area/{area}/processingby/{processingby}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/filter/resulttype/{resulttype}/state/{state}/area/{area}/processingby/{processingby}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listByResultType(
            @ApiPathParam(name = "resulttype", description = "Tipo de resultado de la Prueba") @PathVariable(name = "resulttype") int resulttype,
            @ApiPathParam(name = "state", description = "Estado de Prueba") @PathVariable(name = "state") int state,
            @ApiPathParam(name = "area", description = "Area de la Prueba") @PathVariable(name = "area") int area,
            @ApiPathParam(name = "processingby", description = "Procesar por de la Prueba") @PathVariable(name = "processingby") int processingby
    ) throws Exception
    {
        List<TestBasic> list = testService.listByResultType(resulttype, state == 0 ? null : state == 1, area == 0 ? null : area, processingby == 0 ? null : (short) processingby);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR POR EXAMENES CONFIDENCIALES ----------------
    @ApiMethod(
            description = "Lista pruebas por tipo de resultado. <br>"
            + "Tipo de Resultado: 1 -> Numerico, 2 -> Texto.<br>"
            + "Estado: 0 -> Todos, 1 -> Activos, 2 -> Inactivos. <br>"
            + "Area: idArea, 0 -> Todas. <br>"
            + "Procesamiento Por: 1 -> Manual, 2 -> Middleware, 0 -> Todos",
            path = "/api/tests/filter/confidentials",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/filter/confidentials", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listTestConfidentials() throws Exception
    {
        List<TestBasic> list = testService.listTestConfidential();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR EXAMEN POR LABORATORIO ----------------
    @ApiMethod(
            description = "Lista pruebas por tipo de resultado.",
            path = "/api/tests/laboratory/{laboratory}/branch/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/laboratory/{laboratory}/branch/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestByLaboratory>> listTestLaboratory(
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int branch,
            @ApiPathParam(name = "laboratory", description = "Laboratorio") @PathVariable(name = "laboratory") int laboratory
    ) throws Exception
    {
        List<TestByLaboratory> list = testService.listTestLaboratory(branch, laboratory);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR CONCURRENCIAS ----------------
    @ApiMethod(
            description = "Lista las posibles concurrencias.",
            path = "/api/tests/concurrence/typetest/{typetest}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Concurrence.class)
    @RequestMapping(value = "/concurrence/typetest/{typetest}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Concurrence>> listConcurrencesNew(
            @ApiPathParam(name = "typetest", description = "Tipo de Prueba") @PathVariable(name = "typetest") int typetest
    ) throws Exception
    {
        List<Concurrence> list = testService.getConcurrence(typetest);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR VALORES DE REFERENCIA POR PRUEBA ----------------
    @ApiMethod(
            description = "Lista los valores de referencia por prueba.",
            path = "/api/tests/referencevalues/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ReferenceValue.class)
    @RequestMapping(value = "/referencevalues/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReferenceValue>> listReferenceValues(
            @ApiPathParam(name = "test", description = "Id Prueba") @PathVariable(name = "test") int test
    ) throws Exception
    {
        List<ReferenceValue> list = testService.getReferenceValues(test);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR LABORATORIO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un laboratorio por prueba.",
            path = "/api/tests/laboratory/test/{test}/branch/{branch}/grouptype/{grouptype}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/laboratory/test/{test}/branch/{branch}/grouptype/{grouptype}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Laboratory> getLaboratory(
            @ApiPathParam(name = "test", description = "Prueba") @PathVariable(name = "test") int test,
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int branch,
            @ApiPathParam(name = "grouptype", description = "Grupo Tipo de Orden") @PathVariable(name = "grouptype") int groupType
    ) throws Exception
    {
        Laboratory laboratory = testService.getLaboratory(branch, test, groupType);
        if (laboratory == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(laboratory, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una prueba",
            path = "/api/tests/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Test> getById(
            @ApiPathParam(name = "id", description = "Id de la prueba") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Test test = testService.get(id, null, null, null);
        if (test == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(test, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una prueba",
            path = "/api/tests/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Test> getByCode(
            @ApiPathParam(name = "code", description = "Codigo de la prueba") @PathVariable(name = "code") String code
    ) throws Exception
    {
        Test test = testService.get(null, code, null, null);
        if (test == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(test, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una prueba",
            path = "/api/tests/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Test> getByName(
            @ApiPathParam(name = "name", description = "Nombre de la prueba") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Test test = testService.get(null, null, name, null);
        if (test == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(test, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ABREVIATURA ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una prueba",
            path = "/api/tests/filter/abbr/{abbr}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/filter/abbr/{abbr}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Test> getByAbbr(
            @ApiPathParam(name = "abbr", description = "Abreviatura de la prueba") @PathVariable(name = "abbr") String abbr
    ) throws Exception
    {
        Test test = testService.get(null, null, null, abbr);
        if (test == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(test, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva prueba",
            path = "/api/tests",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Test> create(
            @ApiBodyObject(clazz = Test.class) @RequestBody Test test
    ) throws Exception
    {
        return new ResponseEntity<>(testService.create(test), HttpStatus.OK);
    }

    //------------ INSERTAR PRUEBAS POR LABORATORIO ----------------
    @ApiMethod(
            description = "Insertar pruebas por laboratorio",
            path = "/api/tests/laboratory",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestByLaboratory.class)
    @RequestMapping(value = "/laboratory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createTestByLaboratory(
            @ApiBodyObject(clazz = TestByLaboratory.class) @RequestBody List<TestByLaboratory> tests
    ) throws Exception
    {
        long time = System.currentTimeMillis();
        int result = testService.insertTestLaboratory(tests);
        System.out.println("Se demora: " + (System.currentTimeMillis() - time));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //------------ INSERTAR VALORES DE REFERENCIA ----------------
    @ApiMethod(
            description = "Insertar valores de referencia por examen",
            path = "/api/tests/referencevalues",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ReferenceValue.class)
    @RequestMapping(value = "/referencevalues", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReferenceValue> createReferenceValues(
            @ApiBodyObject(clazz = ReferenceValue.class) @RequestBody ReferenceValue referenceValue
    ) throws Exception
    {
        return new ResponseEntity<>(testService.insertReferenceValues(referenceValue), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una prueba",
            path = "/api/tests",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Test> update(
            @ApiBodyObject(clazz = Test.class) @RequestBody Test test
    ) throws Exception
    {
        return new ResponseEntity<>(testService.update(test), HttpStatus.OK);
    }

    //------------ ACTUALIZAR VALOR DE REFERENCIA ----------------
    @ApiMethod(
            description = "Actualizar un valor de referencia",
            path = "/api/tests/referencevalues",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ReferenceValue.class)
    @RequestMapping(value = "/referencevalues", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReferenceValue> update(
            @ApiBodyObject(clazz = ReferenceValue.class) @RequestBody ReferenceValue referenceValue
    ) throws Exception
    {
        return new ResponseEntity<>(testService.updateReferenceValue(referenceValue), HttpStatus.OK);
    }

    //------------ LISTAR PRUEBAS CON CONCURRENCIAS ----------------
    @ApiMethod(
            description = "Lista las pruebas registradas que tienen concurrencias",
            path = "/api/tests/concurrence",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/concurrence", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listConcurrences() throws Exception
    {
        List<TestBasic> list = testService.listConcurrences();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ Actualizar el orden de impresion ----------------
    @ApiMethod(
            description = "Actualizar el orden de impresion",
            path = "/api/tests/printorder",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/printorder", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> printOrder(
            @ApiBodyObject(clazz = Test.class) @RequestBody List<TestBasic> tests
    ) throws Exception
    {
        return new ResponseEntity<>(testService.updatePrintOrder(tests), HttpStatus.OK);
    }

    //------------ Actualizar impuesto por exámen ----------------
    @ApiMethod(
            description = "Actualiza impuestos para examenes",
            path = "/api/tests/tax",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/tax", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateTestTax(
            @ApiBodyObject(clazz = Test.class) @RequestBody List<TestBasic> tests
    ) throws Exception
    {
        return new ResponseEntity<>(testService.updateTestTax(tests), HttpStatus.OK);
    }

    //------------ ACTUALIZAR DELTA ----------------
    @ApiMethod(
            description = "Actualiza valores delta check para una prueba",
            path = "/api/tests/deltacheck",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestBasic.class)
    @RequestMapping(value = "/deltacheck", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestBasic> updateDelta(
            @ApiBodyObject(clazz = TestBasic.class) @RequestBody TestBasic test
    ) throws Exception
    {
        return new ResponseEntity<>(testService.updateDeltacheck(test), HttpStatus.OK);

    }

    //------------ ACTUALIZAR DIAS DE PROCESAMIENTO ----------------
    @ApiMethod(
            description = "Actualiza los dias de procesamiento de una prueba",
            path = "/api/tests/processingdays",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestBasic.class)
    @RequestMapping(value = "/processingdays", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateProcessingDays(
            @ApiBodyObject(clazz = TestBasic.class) @RequestBody List<TestBasic> tests
    ) throws Exception
    {
        return new ResponseEntity<>(testService.updateProcessingDays(tests), HttpStatus.OK);
    }

    //------------ BORRAR VALOR DE REFERENCIA ----------------
    @ApiMethod(
            description = "Inactivar un valor de referencia",
            path = "/api/tests/referencevalues/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/referencevalues/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> inactivateReferenceValues(
            @ApiPathParam(name = "id", description = "Id del valor de referencia") @PathVariable(name = "id") int id
    ) throws Exception
    {
        testService.inactivateReferenceValues(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //------------ LISTAR PRUEBAS AUTOMATICAS POR PRUEBA ----------------
    @ApiMethod(
            description = "Lista pruebas automaticas por prueba.",
            path = "/api/tests/automatictests/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AutomaticTest.class)
    @RequestMapping(value = "/automatictests/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AutomaticTest>> listAutomaticTest(
            @ApiPathParam(name = "test", description = "Id Prueba") @PathVariable(name = "test") int test
    ) throws Exception
    {
        List<AutomaticTest> list = testService.listAutomaticTest(test);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ INSERTAR PRUEBAS AUTOMATICAS ----------------
    @ApiMethod(
            description = "Insertar pruebas automaticas por examen",
            path = "/api/tests/automatictests/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AutomaticTest.class)
    @RequestMapping(value = "/automatictests/test/{test}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createAutomaticTest(
            @ApiPathParam(name = "test", description = "Id Prueba") @PathVariable(name = "test") int test,
            @ApiBodyObject(clazz = AutomaticTest.class) @RequestBody List<AutomaticTest> automaticTests
    ) throws Exception
    {
        return new ResponseEntity<>(testService.insertAutomaticTest(automaticTests, test), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualización agil de examenes",
            path = "/api/tests",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> patch(
            @ApiBodyObject(clazz = Test.class) @RequestBody Test test
    ) throws Exception
    {
        testService.patch(test);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista pruebas asignadas a un item de PyP.",
            path = "/api/tests/pyp/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PypDemographic.class)
    @RequestMapping(value = "/pyp/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PypDemographic> listPypTest(
            @ApiPathParam(name = "id", description = "Id Item PyP") @PathVariable(name = "id") int id
    ) throws Exception
    {
        PypDemographic list = testService.getPypTest(id);
        if (list.getTests().isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Insertar pruebas para un demografico PyP",
            path = "/api/tests/pyp/",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/pyp/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createPyPTest(
            @ApiBodyObject(clazz = PypDemographic.class) @RequestBody PypDemographic demographic
    ) throws Exception
    {
        return new ResponseEntity<>(testService.insertPyPTest(demographic), HttpStatus.OK);
    }

    //------------ LISTAR PRUEBAS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Lista pruebas de microbiologia",
            path = "/api/tests/microbiology",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AutomaticTest.class)
    @RequestMapping(value = "/microbiology", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listTestMicrobiology() throws Exception
    {
        List<TestBasic> list = testService.testMicrobilogy();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR PRUEBAS DE MICROBIOLOGIA - MEDIO DE CULTIVO ----------------
    @ApiMethod(
            description = "Lista pruebas de microbiologia - medio de cultivo.",
            path = "/api/tests/microbiology/culture/{culture}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AutomaticTest.class)
    @RequestMapping(value = "/microbiology/culture/{culture}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listTestMediaCulture(
            @ApiPathParam(name = "culture", description = "Indica si se consultan examenes asociados a un medio de cultivo.") @PathVariable(name = "culture") boolean culture
    ) throws Exception
    {
        List<TestBasic> list = testService.testMediaCulture(culture);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ INSERTAR PRUEBAS POR SERVICIO ----------------
    @ApiMethod(
            description = "Insertar pruebas por servicio",
            path = "/api/tests/services",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestByService.class)
    @RequestMapping(value = "/services", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestByService> insertTestByService(
            @ApiBodyObject(clazz = TestByService.class) @RequestBody TestByService test
    ) throws Exception
    {
        return new ResponseEntity<>(testService.insertTestByService(test), HttpStatus.OK);
    }

    //------------ LISTAR PRUEBAS POR SERVICIO ----------------
    @ApiMethod(
            description = "Lista pruebas por servicio.",
            path = "/api/tests/services/{service}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestByService.class)
    @RequestMapping(value = "/services/{service}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestByService>> listTestByService(
            @ApiPathParam(name = "service", description = "Id Servicio") @PathVariable(name = "service") int service
    ) throws Exception
    {
        List<TestByService> list = testService.listTestByService(service);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ OBTENER EXAMENES PARA INGRESO DE ORDENES ----------------
    @ApiMethod(
            description = "Obtiene los examenes activos para ver en ingreso de ordenes",
            path = "/api/tests/filter/order_entry",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AutomaticTest.class)
    @RequestMapping(value = "/filter/order_entry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> getTestByOrderEntry() throws Exception
    {
        List<TestBasic> list = testService.getForOrderEntry();
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ OBTENER LOS REQUISITOS DE VARIOS EXAMENES ----------------
    @ApiMethod(
            description = "Obtiene los requisitos de los examenes enviados",
            path = "/api/tests/requirements",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Requirement.class)
    @RequestMapping(value = "/requirements", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Requirement>> getRequirement(
            @ApiBodyObject(clazz = Test.class) @RequestBody List<Test> tests
    ) throws Exception
    {
        List<Requirement> list = testService.getRequirements(tests);
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ OBTENER LOS HIJOS DE UN PERFIL O PAQUETE ----------------
    @ApiMethod(
            description = "Obtiene los requisitos de los examenes enviados",
            path = "/api/tests/childs/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/childs/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Test>> getChilds(
            @ApiPathParam(name = "id", description = "Id del perfil o paquete") @PathVariable("id") int id
    ) throws Exception
    {
        List<Test> list = testService.getChilds(id);
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ OBTIENE LOS PERFILES Y PAQUETES DE LA APLICACION ----------------
    @ApiMethod(
            description = "Obtiene los perfiles y paquetes de la aplicacion con sus examenes",
            path = "/api/tests/profiles",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Profile.class)
    @RequestMapping(value = "/profiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Profile>> getProfile() throws Exception
    {
        List<Profile> list = testService.getProfiles();
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ LISTAR SEDES, LABORATORIOS Y EXAMENES ASOCIADOS----------------
    @ApiMethod(
            description = "Lista las sedes, laboratorios y pruebas asociadas",
            path = "/api/tests/branch/laboratory",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestByLaboratory.class)
    @RequestMapping(value = "/branch/laboratory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestByLaboratory>> getTestByBranchByLaboratory() throws Exception
    {
        List<TestByLaboratory> list = testService.getTestByBranchByLaboratory(null, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR SEDES, LABORATORIOS Y EXAMENES ASOCIADOS----------------
    @ApiMethod(
            description = "Obtiene los examenes por las sedes, laboratorios y pruebas asociadas",
            path = "/api/tests/branch/laboratory/test/{test}/branch/{branch}/laboratory/{laboratory}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestByLaboratory.class)
    @RequestMapping(value = "/branch/laboratory/test/{test}/branch/{branch}/laboratory/{laboratory}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestByLaboratory>> getTestByBranchByLaboratory(
            @ApiPathParam(name = "test", description = "Id del examen") @PathVariable("test") int test,
            @ApiPathParam(name = "branch", description = "Id de la sede") @PathVariable("branch") int branch,
            @ApiPathParam(name = "laboratory", description = "Id del labhoratorio") @PathVariable("laboratory") int laboratory
    ) throws Exception
    {
        List<TestByLaboratory> list = testService.getTestByBranchByLaboratory(test, branch, laboratory);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR EXAMENES POR ID Y TIPO----------------
    @ApiMethod(
            description = "Lista las examnes por id y tipo",
            path = "/api/tests/informationbyid/{test}/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestInformation.class)
    @RequestMapping(value = "/informationbyid/{test}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestInformation> informationByid(
            @ApiPathParam(name = "test", description = "Id del examen") @PathVariable("test") long test,
            @ApiPathParam(name = "type", description = "Tipo del examen") @PathVariable("type") int type
    ) throws Exception
    {
        TestInformation testInfo = testService.informationByid(test, type);
        if (testInfo == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(testInfo, HttpStatus.OK);
        }
    }
    
        //------------ LISTAR EXAMENES PARA HOME BOUND----------------
    @ApiMethod(
            description = "Lista pruebas de Home Bound",
            path = "/api/tests/filter/viewInOrder",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestHomeBound.class)
    @RequestMapping(value = "/filter/viewInOrder", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestHomeBound>> listTesHomeBound() throws Exception
    {
        List<TestHomeBound> listTesHomeBound = integrationHomeBoundService.testHomeBound();
        if (listTesHomeBound.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listTesHomeBound, HttpStatus.OK);
        }
    }

    //------------ LISTAR EXAMENES CON PERMISO PARA HOME BOUND----------------
    @ApiMethod(
            description = "Lista pruebas de Home Bound",
            path = "/api/tests/filter/userPermission",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestHomeBound.class)
    @RequestMapping(value = "/filter/userPermission", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProfileHomeBound>> userPermission() throws Exception
    {
        List<ProfileHomeBound> listTesHomeBound = integrationHomeBoundService.userPermissionHomeBound();
        if (listTesHomeBound.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listTesHomeBound, HttpStatus.OK);
        }
    }
}
