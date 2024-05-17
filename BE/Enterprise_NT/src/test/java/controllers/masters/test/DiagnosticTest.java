package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Diagnostic;
import net.cltech.enterprisent.domain.masters.test.DiagnosticByTest;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestByDiagnostic;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.hasSize;
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
 * Prueba unitaria sobre el api rest /api/diagnostic
 *
 * @version 1.0.0
 * @author enavas
 * @since 22/06/2017
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
public class DiagnosticTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public DiagnosticTest()
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
    public void test_01_getnotallDiagnostics() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_02_addDiagnostic() throws Exception
    {
        Diagnostic diagnostic = new Diagnostic();
        AuthorizedUser usu = new AuthorizedUser();
        diagnostic.setId(1);
        diagnostic.setCode("prueba12");
        diagnostic.setName("prueba");
        diagnostic.getType().setId(39);
        usu.setId(1);
        diagnostic.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(diagnostic);

        mockMvc.perform(post("/api/diagnostics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(diagnostic.getCode())));
    }

    @org.junit.Test
    public void test_03_getAllDiagnostics() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_04_erroraddDiagnostic() throws Exception
    {
        Diagnostic diagnostic = new Diagnostic();
        AuthorizedUser usu = new AuthorizedUser();
        diagnostic.setId(0);
        diagnostic.setCode("prueba12");
        diagnostic.setName("prueba");
        diagnostic.getType().setId(39);
        usu.setId(1);
        diagnostic.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(diagnostic);

        mockMvc.perform(post("/api/diagnostics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @org.junit.Test
    public void test_05_modifyDiagnostic() throws Exception
    {

        Diagnostic diagnostic = new Diagnostic();
        AuthorizedUser usu = new AuthorizedUser();
        diagnostic.setId(1);
        diagnostic.setCode("prueba13");
        diagnostic.setName("prueba3");
        diagnostic.getType().setId(39);
        usu.setId(1);
        diagnostic.setUser(usu);
        diagnostic.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(diagnostic);

        mockMvc.perform(put("/api/diagnostics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(diagnostic.getCode())));
    }

    @org.junit.Test
    public void test_06_errormodifyDiagnostic() throws Exception
    {

        Diagnostic diagnostic = new Diagnostic();
        AuthorizedUser usu = new AuthorizedUser();
        diagnostic.setId(1);
        diagnostic.setCode("prueba12");
        diagnostic.setName("prueba2");
        diagnostic.getType().setId(45);
        usu.setId(1);

        diagnostic.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(diagnostic);

        mockMvc.perform(put("/api/diagnostics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @org.junit.Test
    public void test_07_getcodeDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/code/" + "prueba13").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_08_getNotcodeDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/code/" + "prueba").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_09_getnameDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/name/" + "prueba3").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_10_getNotnameDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/name/" + "prueba").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_11_gettypeDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/type/" + "39").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_12_getNottypeDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/type/" + "25").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_13_getstateDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/state/" + "true").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_14_getNotstateDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/state/" + "false").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_15_getidDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_16_getNoidDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/id/" + "2").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_17_importDiagnostic() throws Exception
    {

        List<Diagnostic> diagnostics = new ArrayList<>(0);

        diagnostics.add(new Diagnostic(1, "prueba14", "import1", 39));
        diagnostics.add(new Diagnostic(1, "prueba15", "import2", 39));
        diagnostics.add(new Diagnostic(1, "prueba16", "import3", 39));
        diagnostics.add(new Diagnostic(1, "prueba17", "import4", 39));
        diagnostics.add(new Diagnostic(1, "prueba18", "import5", 39));
        diagnostics.add(new Diagnostic(1, "prueba19", "import6", 39));
        diagnostics.add(new Diagnostic(1, "prueba20", "import7", 39));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(diagnostics);

        mockMvc.perform(post("/api/diagnostics/import")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));

    }

    @org.junit.Test
    public void test_18_NotimportDiagnostic() throws Exception
    {

        List<Diagnostic> diagnostics = new ArrayList<>(0);

        diagnostics.add(new Diagnostic(1, "prueba14", "import1", 45));
        diagnostics.add(new Diagnostic(1, "prueba15", "import2", 45));
        diagnostics.add(new Diagnostic(1, "prueba16", "import3", 45));
        diagnostics.add(new Diagnostic(1, "prueba17", "import4", 45));
        diagnostics.add(new Diagnostic(1, "prueba18", "import5", 45));
        diagnostics.add(new Diagnostic(1, "prueba19", "import6", 45));
        diagnostics.add(new Diagnostic(1, "prueba21", "import7", 1));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(diagnostics);

        mockMvc.perform(post("/api/diagnostics/import")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }

    @org.junit.Test
    public void test_19_listTestNoDiagnostic() throws Exception
    {
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab39", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (2, 2, '1002', 'GLU', 'GLUCOSA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 0, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (3, 3, '1003', 'COL', 'COLESTEROL', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        mockMvc.perform(get("/api/diagnostics/filter/test/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[?(@.selected == true)]", hasSize(0)));
    }

    @org.junit.Test
    public void test_20_AssignTestDiagnosis() throws Exception
    {

        DiagnosticByTest relation = new DiagnosticByTest();
        relation.setIdTest(1);

        relation.setDiagnostics(new ArrayList<>());
        relation.getDiagnostics().add(new Diagnostic(1));
        relation.getDiagnostics().add(new Diagnostic(2));
        relation.getDiagnostics().add(new Diagnostic(3));

        String jsonContent = Tools.jsonObject(relation);

        mockMvc.perform(put("/api/diagnostics/assign/test")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

    }

    @org.junit.Test
    public void test_21_listTestDiagnostic() throws Exception
    {
        mockMvc.perform(get("/api/diagnostics/filter/test/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[?(@.selected==true)]", hasSize(3)));
    }

    @org.junit.Test
    public void test_22_assignTestToDiagnostic() throws Exception
    {
        TestByDiagnostic relation = new TestByDiagnostic();
        relation.setIdDiagnostic(1);

        relation.setTests(new ArrayList<>());
        Test test = new Test();
        test.setId(1);
        relation.getTests().add(test);

        String jsonContent = Tools.jsonObject(relation);
        mockMvc.perform(put("/api/diagnostics/assign/diagnostic").header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}
