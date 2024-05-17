/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.microbiology.TestProcedure;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
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
 * Prueba unitaria sobre el api rest /api/procedures
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/08/2017
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
public class ProcedureTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ProcedureTest()
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

    @Test
    public void test_01_getAllProcedureNoContent() throws Exception
    {
        mockMvc.perform(get("/api/procedures").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertProcedure() throws Exception
    {
        Procedure procedure = new Procedure();
        procedure.setCode("T");
        procedure.setName("TOMAR");
        procedure.getUser().setId(1);
        procedure.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(procedure);

        mockMvc.perform(post("/api/procedures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("T")))
                .andExpect(jsonPath("$.name", is("TOMAR")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllProcedure() throws Exception
    {
        mockMvc.perform(get("/api/procedures").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getProcedureById() throws Exception
    {
        mockMvc.perform(get("/api/procedures/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("T")))
                .andExpect(jsonPath("$.name", is("TOMAR")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getProcedureByCode() throws Exception
    {
        mockMvc.perform(get("/api/procedures/filter/code/" + "T").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("T")))
                .andExpect(jsonPath("$.name", is("TOMAR")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_06_getProcedureByName() throws Exception
    {
        mockMvc.perform(get("/api/procedures/filter/name/" + "TOMAR").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("T")))
                .andExpect(jsonPath("$.name", is("TOMAR")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_07_getProcedureByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/procedures/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getProcedureByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/procedures/filter/code/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getProcedureByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/procedures/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_insertProcedureError() throws Exception
    {
        Procedure procedure = new Procedure();
        procedure.setCode("T");
        procedure.setName("TOMAR");
        procedure.getUser().setId(1);
        procedure.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(procedure);

        mockMvc.perform(post("/api/procedures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_updateProcedure() throws Exception
    {
        Procedure procedure = new Procedure();
        procedure.setId(1);
        procedure.setCode("T1");
        procedure.setName("TOMAR");
        procedure.getUser().setId(1);
        procedure.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(procedure);

        mockMvc.perform(put("/api/procedures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("T1")))
                .andExpect(jsonPath("$.name", is("TOMAR")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_12_getProcedureByCode() throws Exception
    {
        mockMvc.perform(get("/api/procedures/filter/code/" + "T1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("T1")))
                .andExpect(jsonPath("$.name", is("TOMAR")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_13_getProcedureByName() throws Exception
    {
        mockMvc.perform(get("/api/procedures/filter/name/" + "TOMAR").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("T1")))
                .andExpect(jsonPath("$.name", is("TOMAR")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_14_updateProcedureError() throws Exception
    {
        Procedure procedure = new Procedure();
        procedure.setId(-1);
        procedure.setCode("T1");
        procedure.setName("TOMAR");
        procedure.getUser().setId(1);
        procedure.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(procedure);

        mockMvc.perform(put("/api/procedures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void test_15_getProcedureByTest() throws Exception
    {
        mockMvc.perform(get("/api/procedures/testprocedure/filter/idtest/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_16_getProcedureByTestNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab156 SET lab07c1=0 WHERE lab156c1=1", null);
        mockMvc.perform(get("/api/procedures/testprocedure/filter/idtest/" + "1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_17_insertProcedureTest() throws Exception
    {
        List<TestProcedure> testprocedures = new ArrayList<>();
        TestProcedure testProcedure = new TestProcedure();
        testProcedure.getProcedure().setCode("code456");
        testProcedure.getProcedure().setName("prueba3");
        testProcedure.getProcedure().setSelected(true);
        testProcedure.getProcedure().setDefaultvalue(true);
        testProcedure.getProcedure().setConfirmatorytest(525);
        testProcedure.getProcedure().setConfirmatorytestcode("256");
        testProcedure.getProcedure().setConfirmatorytestname("cultivo");
        testProcedure.getProcedure().getUser().setId(1);
        testProcedure.getProcedure().setId(1);
        testProcedure.getTest().setId(1);

        testprocedures.add(testProcedure);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(testprocedures);

        mockMvc.perform(post("/api/procedures/testprocedure")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_18_insertProcedureTestError() throws Exception
    {
        List<TestProcedure> testprocedures = new ArrayList();
        TestProcedure testProcedure = new TestProcedure();
        testProcedure.getTest().setId(1);
        testprocedures.add(testProcedure);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(testprocedures);

        mockMvc.perform(post("/api/procedures/testprocedure")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

}
