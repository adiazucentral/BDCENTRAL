package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.contains;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
 * Prueba unitaria sobre el api rest /api/demographics/exclude
 *
 * @version 1.0.0
 * @author eacuna
 * @since 03/08/2017
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
public class DemographicExcludeTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public DemographicExcludeTest()
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
        TestScript.deleteData("lab39");
        mockMvc.perform(get("/api/demographics/exclude/id/1/item/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insert() throws Exception
    {

        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (2, 2, '1002', 'GLU', 'GLUCOSA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 0, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (3, 3, '1003', 'COL', 'COLESTEROL', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (4, 4, '1004', 'TRI', 'TRIGLICERIDOS', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (5, 5, '101', 'CH', 'CUADRO HEMATICO', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 1, 0, 0, 0, NULL, 1, NULL)", null);

        List<ExcludeTest> excludes = new ArrayList<>();
        excludes.add(new ExcludeTest());
        excludes.get(excludes.size() - 1).setId(1);
        excludes.get(excludes.size() - 1).setDemographicItem(1);
        excludes.get(excludes.size() - 1).getTest().setId(1);
        excludes.add(new ExcludeTest());
        excludes.get(excludes.size() - 1).setId(1);
        excludes.get(excludes.size() - 1).setDemographicItem(1);
        excludes.get(excludes.size() - 1).getTest().setId(3);
        excludes.add(new ExcludeTest());
        excludes.get(excludes.size() - 1).setId(1);
        excludes.get(excludes.size() - 1).setDemographicItem(1);
        excludes.get(excludes.size() - 1).getTest().setId(5);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(excludes);

        mockMvc.perform(put("/api/demographics/exclude/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/demographics/exclude/id/1/item/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].test.id", contains(1, 3, 4, 5)))
                .andExpect(jsonPath("$[*].test.selected", contains(true, true, false, true)));

    }

    @Test
    public void test_04_delete() throws Exception
    {
        mockMvc.perform(delete("/api/demographics/exclude/id/1/item/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

    }

    @Test
    public void test_05_noContent() throws Exception
    {
        mockMvc.perform(get("/api/demographics/exclude/id/1/item/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].test.id", contains(1, 3, 4, 5)))
                .andExpect(jsonPath("$[*].test.selected", contains(false, false, false, false)));

    }

}
