package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.AutomaticTest;
import net.cltech.enterprisent.domain.masters.test.Concurrence;
import net.cltech.enterprisent.domain.masters.test.LiteralResult;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.TestByLaboratory;
import net.cltech.enterprisent.domain.masters.test.TestByService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/test
 *
 * @version 1.0.0
 * @author cmartin
 * @since 28/06/2017
 * @see Creacion
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    MongoTestAppContext.class,
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public TestTest()
    {
    }

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        AuthorizedUser user = new AuthorizedUser();
        user.setUserName("tests");
        user.setLastName("Pruebas");
        user.setName("CLTech");
        user.setId(-1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_getAllTestNoContent() throws Exception
    {
        TestScript.deleteData("lab39");
        TestScript.deleteData("lab145");
        mockMvc.perform(get("/api/tests/filter/type/0/state/0/area/0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_02_insertTest() throws Exception
    {
        Test test = new Test();
        test.setTestType((short) 0);
        test.getArea().setId(1);
        test.setCode("1001");
        test.setName("HEMOGLOBINA");
        test.setAbbr("HMG");
        test.getGender().setId(7);
        test.setResultType((short) 1);
        test.setDeliveryDays(2);
        test.setSelfValidation(1);
        test.setPrintOnReport((short) 1);
        test.setProcessingBy((short) 1);
        test.getUser().setId(1);
        Requirement requirement = new Requirement();
        requirement.setId(1);
        test.getRequirements().add(requirement);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(post("/api/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1001")))
                .andExpect(jsonPath("$.name", is("HEMOGLOBINA")))
                .andExpect(jsonPath("$.abbr", is("HMG")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_03_getAllTest() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/0/state/0/area/0").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_04_getAllTestState() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/0/state/1/area/0").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_05_getAllTestStateArea() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/0/state/1/area/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_06_getTestById() throws Exception
    {
        mockMvc.perform(get("/api/tests/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1001")))
                .andExpect(jsonPath("$.name", is("HEMOGLOBINA")))
                .andExpect(jsonPath("$.abbr", is("HMG")));
    }

    @org.junit.Test
    public void test_07_getTestByCode() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/code/" + "1001").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1001")))
                .andExpect(jsonPath("$.name", is("HEMOGLOBINA")))
                .andExpect(jsonPath("$.abbr", is("HMG")));
    }

    @org.junit.Test
    public void test_08_getTestByName() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/name/" + "HEMOGLOBINA").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1001")))
                .andExpect(jsonPath("$.name", is("HEMOGLOBINA")))
                .andExpect(jsonPath("$.abbr", is("HMG")));
    }

    @org.junit.Test
    public void test_09_getTestByAbbr() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/abbr/" + "HMG").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1001")))
                .andExpect(jsonPath("$.name", is("HEMOGLOBINA")))
                .andExpect(jsonPath("$.abbr", is("HMG")));
    }

    @org.junit.Test
    public void test_10_getTestByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/tests/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_11_getTestByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/code/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_12_getTestByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_13_getTestByAbbrNoContent() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/abbr/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_14_insertTestError() throws Exception
    {
        Test test = new Test();
        test.setCode("1001");
        test.setName("HEMOGLOBINA");
        test.setAbbr("HMG");
        test.getGender().setId(7);
        test.setResultType((short) 1);
        test.setDeliveryDays(1);
        test.setSelfValidation(1);
        test.setPrintOnReport((short) 1);
        test.setProcessingBy((short) 1);
        test.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(post("/api/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @org.junit.Test
    public void test_15_updateTest() throws Exception
    {
        Test test = new Test();
        test.setId(1);
        test.setTestType((short) 0);
        test.getArea().setId(1);
        test.setCode("1001");
        test.setName("HEMOGLOBINA");
        test.setAbbr("HMG");
        test.getGender().setId(7);
        test.setResultType((short) 1);
        test.setDeliveryDays(5);
        test.setSelfValidation(2);
        test.setPrintOnReport((short) 2);
        test.setProcessingBy((short) 2);
        test.getUser().setId(1);

        Requirement requirement = new Requirement();
        requirement.setId(1);
        test.getRequirements().add(requirement);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(put("/api/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1001")))
                .andExpect(jsonPath("$.name", is("HEMOGLOBINA")))
                .andExpect(jsonPath("$.abbr", is("HMG")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_16_updateTestError() throws Exception
    {
        Test test = new Test();
        test.setId(1);
        test.setCode("1001");
        test.setName("HEMOGLOBINA");
        test.setAbbr("HMG");
        test.getGender().setId(1);
        test.setResultType((short) 1);
        test.setDeliveryDays(1);
        test.setSelfValidation(1);
        test.setPrintOnReport((short) 1);
        test.setProcessingBy((short) 1);
        test.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(put("/api/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    //PRUEBAS GENERALES EN CONSULTAS
    @org.junit.Test
    public void test_17_insertPanel() throws Exception
    {
        Test test = new Test();
        test.setTestType((short) 1);
        test.getArea().setId(1);
        test.setCode("101");
        test.setName("CUADRO HEMATICO");
        test.setAbbr("CH");
        test.getGender().setId(7);
        test.setDeliveryDays(1);
        test.setSelfValidation(1);
        test.setPrintOnReport((short) 1);
        test.setProcessingBy((short) 1);
        test.getUser().setId(1);

        Requirement requirement = new Requirement();
        requirement.setId(1);
        test.getRequirements().add(requirement);

        Concurrence concurrence1 = new Concurrence();
        concurrence1.getConcurrence().setId(1);
        test.getConcurrences().add(concurrence1);
        Concurrence concurrence2 = new Concurrence();
        concurrence2.getConcurrence().setId(2);
        test.getConcurrences().add(concurrence2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(post("/api/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("101")))
                .andExpect(jsonPath("$.name", is("CUADRO HEMATICO")))
                .andExpect(jsonPath("$.abbr", is("CH")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_18_insertPackage() throws Exception
    {
        Test test = new Test();
        test.setTestType((short) 2);
        test.getArea().setId(1);
        test.setCode("1");
        test.setName("CLTECH");
        test.setAbbr("CLTECH");
        test.getGender().setId(7);
        test.setDeliveryDays(1);
        test.setSelfValidation(1);
        test.setPrintOnReport((short) 1);
        test.setProcessingBy((short) 1);
        test.getUser().setId(1);

        Requirement requirement = new Requirement();
        requirement.setId(1);
        test.getRequirements().add(requirement);

        Concurrence concurrence1 = new Concurrence();
        concurrence1.getConcurrence().setId(1);
        test.getConcurrences().add(concurrence1);
        Concurrence concurrence2 = new Concurrence();
        concurrence2.getConcurrence().setId(2);
        test.getConcurrences().add(concurrence2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(post("/api/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1")))
                .andExpect(jsonPath("$.name", is("CLTECH")))
                .andExpect(jsonPath("$.abbr", is("CLTECH")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_19_getAllPanel() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/1/state/0/area/0").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_20_getAllPanelState() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/1/state/1/area/0").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_21_getAllPanelStateArea() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/1/state/1/area/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_22_getAllPackage() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/2/state/0/area/0").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_23_getAllPackageState() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/2/state/1/area/0").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_24_getAllPackageStateArea() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/2/state/1/area/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_25_insertTestByLaboratory() throws Exception
    {
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.deleteData("lab40");
        TestScript.execTestUpdateScript("INSERT INTO lab40 (lab40c1, lab40c2, lab40c3, lab40c4, lab40c5, lab40c6, lab40c7, lab40c8, lab40c9, lab04c1, lab07c1, lab40c10, lab40c11, lab40c12) VALUES (1, 1, 'GENERAL', '', '', '', 1, '', '2017-09-13 11:34:12.439341', 1, 1, 'http://interfaces:8080/MiddlewareManager', 1, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab40 (lab40c1, lab40c2, lab40c3, lab40c4, lab40c5, lab40c6, lab40c7, lab40c8, lab40c9, lab04c1, lab07c1, lab40c10, lab40c11, lab40c12) VALUES (2, 1, 'LAB', '', '', '', 1, '', '2017-09-13 11:34:12.439341', 1, 1, 'http://interfaces:8080/MiddlewareManager', 1, 1)", null);
        List<TestByLaboratory> tests = new ArrayList<>();
        TestByLaboratory test1 = new TestByLaboratory();
        test1.getTest().setId(1);
        test1.setIdBranch(2);
        test1.setIdLaboratory(1);
        test1.setUrgency((short) 1);
        test1.setRoutine((short) 2);

        TestByLaboratory test2 = new TestByLaboratory();
        test2.getTest().setId(1);
        test2.setIdBranch(2);
        test2.setIdLaboratory(2);
        test2.setUrgency((short) 2);
        test2.setRoutine((short) 1);

        tests.add(test1);
        tests.add(test2);

        ObjectMapper mapperT = new ObjectMapper();
        mapperT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentT = mapperT.writeValueAsString(tests);

        mockMvc.perform(post("/api/tests/laboratory")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentT))
                .andExpect(status().isOk());

        tests = new ArrayList<>();
        test1 = new TestByLaboratory();
        test1.getTest().setId(1);
        test1.setIdBranch(2);
        test1.setIdLaboratory(1);
        test1.setUrgency((short) 1);
        test1.setRoutine((short) 2);

        test2 = new TestByLaboratory();
        test2.getTest().setId(1);
        test2.setIdBranch(2);
        test2.setIdLaboratory(2);
        test2.setUrgency((short) 2);
        test2.setRoutine((short) 1);

        tests.add(test1);
        tests.add(test2);

        mapperT = new ObjectMapper();
        mapperT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContentT = mapperT.writeValueAsString(tests);

        mockMvc.perform(post("/api/tests/laboratory")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentT))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_26_getAllTestByLaboratory() throws Exception
    {
        mockMvc.perform(get("/api/tests/laboratory/1/branch/2").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_27_getLaboratory() throws Exception
    {
        //Se duplican registros
        mockMvc.perform(get("/api/tests/laboratory/test/1/branch/2/grouptype/1").header("Authorization", token))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_28_putPrinterorder() throws Exception
    {

        List<TestBasic> tests = new ArrayList<>(0);

        tests.add(new TestBasic(1, 1));
        tests.add(new TestBasic(2, 2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(tests);

        mockMvc.perform(put("/api/tests/printorder")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

    }

    @org.junit.Test
    public void test_29_notPutPrinterorder() throws Exception
    {
        List<TestBasic> tests = new ArrayList<>(0);

        tests.add(new TestBasic(1, 1));
        tests.add(new TestBasic(2, null));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(tests);

        mockMvc.perform(put("/api/tests/printorder")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @org.junit.Test
    public void test_30_getAllReferenceValuesNoContent() throws Exception
    {
        mockMvc.perform(get("/api/tests/referencevalues/test/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_31_insertReferenceValues() throws Exception
    {
        ReferenceValue referenceValue = new ReferenceValue();
        referenceValue.getTest().setId(1);
        referenceValue.setUnitAge((short) 1);
        referenceValue.setAgeMin(1);
        referenceValue.setAgeMax(200);
        referenceValue.setNormalMin((BigDecimal) BigDecimal.ONE);
        referenceValue.setNormalMax((BigDecimal) new BigDecimal(99));
        referenceValue.setPanicMin((BigDecimal) new BigDecimal(200));
        referenceValue.setPanicMax((BigDecimal) new BigDecimal(500));
        referenceValue.setComment("");
        referenceValue.setPanic(new LiteralResult());
        referenceValue.getPanic().setId(1);
        referenceValue.getGender().setId(7);
        referenceValue.getRace().setId(1);
        referenceValue.getUser().setId(1);
        referenceValue.getNormal().setId(0);

        ObjectMapper mapperT = new ObjectMapper();
        mapperT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentT = mapperT.writeValueAsString(referenceValue);

        mockMvc.perform(post("/api/tests/referencevalues")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentT))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_32_updateReferenceValues() throws Exception
    {
        ReferenceValue referenceValue = new ReferenceValue();
        referenceValue.getTest().setId(1);
        referenceValue.setUnitAge((short) 1);
        referenceValue.setAgeMin(1);
        referenceValue.setAgeMax(200);
        referenceValue.setNormalMin((BigDecimal) BigDecimal.ONE);
        referenceValue.setNormalMax((BigDecimal) new BigDecimal(99));
        referenceValue.setPanicMin((BigDecimal) new BigDecimal(200));
        referenceValue.setPanicMax((BigDecimal) new BigDecimal(500));
        referenceValue.setComment("");
        referenceValue.setPanic(new LiteralResult());
        referenceValue.getPanic().setId(2);
        referenceValue.getGender().setId(7);
        referenceValue.getRace().setId(1);
        referenceValue.getUser().setId(1);
        referenceValue.getNormal().setId(0);

        ObjectMapper mapperT = new ObjectMapper();
        mapperT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentT = mapperT.writeValueAsString(referenceValue);

        mockMvc.perform(post("/api/tests/referencevalues")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentT))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_33_getAllReferenceValues() throws Exception
    {
        mockMvc.perform(get("/api/tests/referencevalues/test/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_34_updateProcessingDays() throws Exception
    {
        List<TestBasic> testBasics = new ArrayList<>();

        TestBasic testBasic1 = new TestBasic();
        testBasic1.setId(1);
        testBasic1.setProcessingDays("12345");

        TestBasic testBasic2 = new TestBasic();
        testBasic2.setId(2);
        testBasic2.setProcessingDays("123");

        testBasics.add(testBasic1);
        testBasics.add(testBasic2);

        ObjectMapper mapperT = new ObjectMapper();
        mapperT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentT = mapperT.writeValueAsString(testBasics);

        mockMvc.perform(put("/api/tests/processingdays")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentT))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_35_getAllTest() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/type/0/state/0/area/0").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].processingDays", contains("12345")));
    }

    @org.junit.Test
    public void test_36_getAllAutomaticTestNoContent() throws Exception
    {
        mockMvc.perform(get("/api/tests/automatictests/test/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_37_insertAutomaticTests() throws Exception
    {
        List<AutomaticTest> automaticTests = new ArrayList<>();

        AutomaticTest automaticTest1 = new AutomaticTest();
        automaticTest1.setId(1);
        automaticTest1.setResult1("1");
        automaticTest1.setResult2("5");
        automaticTest1.getAutomaticTest().setId(1);
        automaticTest1.getSign().setId(52);

        AutomaticTest automaticTest2 = new AutomaticTest();
        automaticTest2.setId(1);
        automaticTest2.setResult1("10");
        automaticTest2.setResult2("45");
        automaticTest2.getAutomaticTest().setId(2);
        automaticTest2.getSign().setId(53);

        automaticTests.add(automaticTest1);
        automaticTests.add(automaticTest2);

        ObjectMapper mapperT = new ObjectMapper();
        mapperT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentT = mapperT.writeValueAsString(automaticTests);

        mockMvc.perform(post("/api/tests/automatictests/test/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentT))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_38_getAllAutomaticTest() throws Exception
    {
        mockMvc.perform(get("/api/tests/automatictests/test/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_39_patchDuplicate() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab39", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (2, 2, '1002', 'GLU', 'GLUCOSA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 0, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (3, 3, '1003', 'COL', 'COLESTEROL', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (4, 4, '1004', 'TRI', 'TRIGLICERIDOS', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (5, 5, '101', 'CH', 'CUADRO HEMATICO', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 1, 0, 0, 0, NULL, 1, NULL)", null);

        Test test = new Test();
        test.setId(1);
        test.setCode("1003");
        test.setAbbr("TRI");
        test.setName("CUADRO HEMATICO");
        test.getArea().setId(1);
        test.getUnit().setId(1);
        test.getSample().setId(1);
        test.setTestType((short) 0);
        test.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(patch("/api/tests/").header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("1|code", "1|abbreviation", "1|name")))
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_40_patchEmpty() throws Exception
    {
        Test test = new Test();

        test.setTestType((short) 0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(patch("/api/tests/").header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("0|id", "0|code", "0|name", "0|user", "0|area", "0|abbreviation")))
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_41_patchTest() throws Exception
    {
        Test test = new Test();
        test.setId(1);
        test.setCode("M1001");
        test.setName("MHEMOGLOBINA");
        test.setAbbr("AHMG");
        test.getArea().setId(1);
        test.getUnit().setId(1);
        test.getSample().setId(1);
        test.setTestType((short) 0);
        test.getUser().setId(1);
        test.setPrintOrder(5);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(patch("/api/tests/").header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());

    }

    @org.junit.Test
    public void test_42_patchTestById() throws Exception
    {
        mockMvc.perform(get("/api/tests/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("M1001")))
                .andExpect(jsonPath("$.name", is("MHEMOGLOBINA")))
                .andExpect(jsonPath("$.printOrder", is(5)))
                .andExpect(jsonPath("$.abbr", is("AHMG")));
    }

    @org.junit.Test
    public void test_43_insertTestByservice() throws Exception
    {
        TestByService test = new TestByService();
        test.getService().setId(1);
        test.getTest().setId(1);
        test.setExpectedTime(10);
        test.setMaximumTime(20);

        ObjectMapper mapperT = new ObjectMapper();
        mapperT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentT = mapperT.writeValueAsString(test);

        mockMvc.perform(post("/api/tests/services")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentT))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_44_getAllAutomaticTest() throws Exception
    {
        mockMvc.perform(get("/api/tests/services/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_46_getTestByBranchByLaboratory() throws Exception
    {
        mockMvc.perform(get("/api/tests/branch/laboratory")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_47_getTestByBranchByLaboratory() throws Exception
    {
        mockMvc.perform(get("/api/tests/branch/laboratory/test/1/branch/2/laboratory/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_48_HomeBound() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/viewInOrder").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_49_HomeBound_Licenses() throws Exception
    {
        mockMvc.perform(get("/api/authentication/licenses").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

}
