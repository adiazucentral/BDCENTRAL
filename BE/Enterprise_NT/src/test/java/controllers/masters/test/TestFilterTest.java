package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.TestFilter;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
 * Prueba unitaria sobre el api rest /api/group
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/08/2017
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
public class TestFilterTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public TestFilterTest()
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
    public void test_01_getAllTestFilterNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab39", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4   , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36        , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23) "
                + " VALUES (   1   ,     2   , '102'  , 'LU', 'Leucocitos', 1      , 42     , 0      , 200    , 1      , 2       , 1       , 1       , 1       , 0       , 0       , 0       , 0       , 1       , '2017-09-13 11:24:27.468', 1      , 1      , 0       , 1       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4     , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36        , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23) "
                + " VALUES (2      , 2       , '103'  , 'NI'  , 'Ninfositos', 1      ,42      , 0      , 200    , 1      , 2       ,  1      , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-09-13 11:24:27.468' , 1      , 1      , 0       , 0       , 0       , 0, 1,1,1,1,1,1)", null);

        mockMvc.perform(get("/api/groups").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertTestFilter() throws Exception
    {
        TestBasic test1 = new TestBasic();
        test1.setId(1);
        test1.setSelected(true);

        TestBasic test2 = new TestBasic();
        test2.setId(2);
        test2.setSelected(true);

        TestFilter group = new TestFilter();
        group.setName("Microbiologia");
        group.setCode("MC");
        group.getUser().setId(1);
        group.setTests(new ArrayList<>());
        group.getTests().add(test1);
        group.getTests().add(test2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(group);

        mockMvc.perform(post("/api/groups")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Microbiologia")))
                .andExpect(jsonPath("$.code", is("MC")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllTestFilter() throws Exception
    {
        mockMvc.perform(get("/api/groups").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getTestFilterById() throws Exception
    {
        mockMvc.perform(get("/api/groups/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Microbiologia")))
                .andExpect(jsonPath("$.code", is("MC")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.tests[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getTestFilterByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/groups/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_getTestFilterByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/groups/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_insertTestFilterError() throws Exception
    {
        TestFilter group = new TestFilter();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(group);

        mockMvc.perform(post("/api/groups")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_08_updateTestFilter() throws Exception
    {
        TestBasic test1 = new TestBasic();
        test1.setId(1);
        test1.setSelected(true);

        TestBasic test2 = new TestBasic();
        test2.setId(2);
        test2.setSelected(true);

        TestFilter group = new TestFilter();
        group.setId(1);
        group.setName("Quimica");
        group.setCode("QM");
        group.getUser().setId(1);
        group.setTests(new ArrayList<>());
        group.getTests().add(test1);
        group.getTests().add(test2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(group);

        mockMvc.perform(put("/api/groups")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Quimica")))
                .andExpect(jsonPath("$.code", is("QM")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_9_getTestFilterByName() throws Exception
    {
        mockMvc.perform(get("/api/groups/filter/name/" + "Quimica").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Quimica")))
                .andExpect(jsonPath("$.code", is("QM")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateTestFilterError() throws Exception
    {
        TestFilter group = new TestFilter();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(group);

        mockMvc.perform(put("/api/groups")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }
}
