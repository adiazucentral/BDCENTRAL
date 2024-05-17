package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Standardization;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/centralsystems/standardization/test
 *
 * @version 1.0.0
 * @author eacuna
 * @since 26/07/2017
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
public class StandardizationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public StandardizationTest()
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
    public void test_01_getAllNoContent() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab118 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab61 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab39 RESTART IDENTITY;", null);
        mockMvc.perform(get("/api/centralsystems/standardization/test/" + 1)
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_getAllNoTestRelation() throws Exception
    {
        try
        {

            TestScript.execTestUpdateScript("INSERT INTO lab118 VALUES (1, 'CUPS', 0, now(),1, 1, 1)", null);
            TestScript.execTestUpdateScript("INSERT INTO lab118 VALUES (2, 'EHR', 0, now(),1, 1, 1)", null);

            TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
            TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (2, 2, '1002', 'GLU', 'GLUCOSA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 0, 0, 0, 0, 0, NULL, 1, NULL)", null);
            TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (3, 3, '1003', 'COL', 'COLESTEROL', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
            TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (4, 4, '1004', 'TRI', 'TRIGLICERIDOS', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
            TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (5, 5, '101', 'CH', 'CUADRO HEMATICO', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 1, 0, 0, 0, NULL, 1, NULL)", null);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        mockMvc.perform(get("/api/centralsystems/standardization/test/" + 1)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].codes", containsInAnyOrder(hasSize(0), hasSize(0), hasSize(0), hasSize(0))));
    }

    @Test
    public void test_03_homologate() throws Exception
    {
        Standardization cups = new Standardization();
        cups.setId(1);
        cups.getCodes().add("T1");
        cups.getCentralSystem().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(cups);

        mockMvc.perform(post("/api/centralsystems/standardization/test/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getAllTestRelation() throws Exception
    {
        mockMvc.perform(get("/api/centralsystems/standardization/test/" + 1)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].codes", contains(hasSize(1), hasSize(0), hasSize(0), hasSize(0))))
                .andExpect(jsonPath("$[0].codes", contains("T1")));
    }

    @Test
    public void test_05_homologateMoreCodes() throws Exception
    {
        Standardization cups = new Standardization();
        cups.setId(5);
        cups.getCodes().add("P1");
        cups.getCodes().add("P2");
        cups.getCodes().add("P3");
        cups.getCentralSystem().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(cups);

        mockMvc.perform(post("/api/centralsystems/standardization/test/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getAllTestRelation() throws Exception
    {
        mockMvc.perform(get("/api/centralsystems/standardization/test/" + 1)
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_07_CodeExists() throws Exception
    {
        Standardization exists = new Standardization();
        exists.getCentralSystem().setId(1);
        exists.setId(5);
        exists.getCodes().add("T1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(exists);

        mockMvc.perform(get("/api/centralsystems/standardization/test/exists/1/5/T1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void test_08_CodeNotExists() throws Exception
    {
        Standardization exists = new Standardization();
        exists.getCentralSystem().setId(1);
        exists.setId(5);
        exists.getCodes().add("T2");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(exists);

        mockMvc.perform(get("/api/centralsystems/standardization/test/exists/1/5/T2")
                .header("Authorization", token)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
        exists.setId(5);
        exists.getCodes().set(0, "P1");
        //Falso cuando existe pero el examen es del mismo examen
        mockMvc.perform(get("/api/centralsystems/standardization/test/exists/1/5/P1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
        TestScript.execTestUpdateScript("DELETE FROM lab61", null);
        TestScript.execTestUpdateScript("DELETE FROM lab39", null);
    }
}
