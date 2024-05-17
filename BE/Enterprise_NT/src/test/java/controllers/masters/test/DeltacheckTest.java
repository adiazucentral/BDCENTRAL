package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.math.BigDecimal;
import net.cltech.enterprisent.dao.interfaces.masters.test.TestDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba sobre el api rest /api/tests/deltacheck
 *
 * @version 1.0.0
 * @author eacuna
 * @sinc18/07/2017
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
public class DeltacheckTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private String token;

    @Autowired
    public void setTestDao(TestDao dao)
    {
    }

    public DeltacheckTest()
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
    public void test_01_noContent() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab39", null);
        mockMvc.perform(get("/api/tests/filter/resulttype/1/state/1/area/0/processingby/0")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_emptyField() throws Exception
    {
        TestBasic test = new TestBasic();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(put("/api/tests/deltacheck/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|deltadays", "0|deltamin", "0|deltamax", "0|userId", "0|id")));
    }

    @Test
    public void test_03_update() throws Exception
    {
        try
        {
            TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
            TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (2, 1, '905721', 'CN', 'Cianuro', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 0, 0, 0, 0, 0, NULL, 1, NULL)", null);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        TestBasic test = new TestBasic(1, 0);
        test.setDeltacheckDays(10);
        test.setDeltacheckMin(new BigDecimal(90.0));
        test.setDeltacheckMax(new BigDecimal(100.0));
        test.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);
        mockMvc.perform(put("/api/tests/deltacheck/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_getAll() throws Exception
    {
        mockMvc.perform(get("/api/tests/filter/resulttype/1/state/1/area/0/processingby/0")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].deltacheckDays", is(10)))
                .andExpect(jsonPath("$[0].deltacheckMin", is(90.0)))
                .andExpect(jsonPath("$[0].deltacheckMax", is(100.0)));

    }

    public void test_05_getById() throws Exception
    {
        mockMvc.perform(get("/api/tests/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deltacheckDays", is(10)))
                .andExpect(jsonPath("$.deltacheckMin", is(90.0)))
                .andExpect(jsonPath("$.deltacheckMax", is(100.0)));
        TestScript.execTestUpdateScript("DELETE FROM lab39", null);
    }
}
