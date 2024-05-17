package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.PopulationGroup;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
import tools.TestTools;

/**
 * Prueba sobre el api rest /api/populationgroups
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/05/2017
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
public class PopulationGroupTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public PopulationGroupTest()
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
        mockMvc.perform(get("/api/populationgroups").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_add() throws Exception
    {
        PopulationGroup add = new PopulationGroup();
        add.setName("Niños");
        add.setSex(7);
        add.setUnit(1);
        add.setInitialRange(1);
        add.setFinalRange(20);

        add.setUser(new AuthorizedUser());
        add.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(add);

        mockMvc.perform(post("/api/populationgroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Niños")))
                .andExpect(jsonPath("$.sex", is(7)))
                .andExpect(jsonPath("$.unit", is(1)))
                .andExpect(jsonPath("$.initialRange", is(1)))
                .andExpect(jsonPath("$.finalRange", is(20)))
                .andExpect(jsonPath("$.state", is(true)));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/populationgroups").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_edit() throws Exception
    {
        PopulationGroup edit = new PopulationGroup();

        edit.setId(1);

        edit.setName("Adolecente");
        edit.setSex(8);
        edit.setUnit(2);
        edit.setInitialRange(2);
        edit.setFinalRange(22);
        edit.setState(false);

        edit.setUser(new AuthorizedUser());
        edit.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(edit);

        mockMvc.perform(put("/api/populationgroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Adolecente")))
                .andExpect(jsonPath("$.sex", is(8)))
                .andExpect(jsonPath("$.unit", is(2)))
                .andExpect(jsonPath("$.initialRange", is(2)))
                .andExpect(jsonPath("$.finalRange", is(22)))
                .andExpect(jsonPath("$.state", is(false)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()))
                .andExpect(jsonPath("$.state", is(false)));
    }

    @Test
    public void test_05_duplicateFields() throws Exception
    {
        PopulationGroup bean = new PopulationGroup();
        bean.setId(1);

        bean.setName("Adolecente");
        bean.setSex(8);
        bean.setUnit(2);
        bean.setInitialRange(2);
        bean.setFinalRange(22);
        bean.setState(false);

        bean.setUser(new AuthorizedUser());
        bean.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bean);

        mockMvc.perform(post("/api/populationgroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name")));
    }

    @Test
    public void test_06_emptyField() throws Exception
    {
        PopulationGroup population = new PopulationGroup();

        population.setUser(new AuthorizedUser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(population);

        mockMvc.perform(post("/api/populationgroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|userId", "0|initialRange", "0|finalRange", "0|sex", "0|unit")));
    }

    @Test
    public void test_07_invalidField() throws Exception
    {
        PopulationGroup population = new PopulationGroup();

        population.setId(1);

        population.setName("Kids");
        population.setSex(10);
        population.setUnit(5);
        population.setInitialRange(2);
        population.setFinalRange(22);
        population.setState(true);
        population.setUser(new AuthorizedUser());
        population.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(population);

        mockMvc.perform(post("/api/populationgroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("3|sex", "3|unit")));
    }

    @Test
    public void test_08_getByState() throws Exception
    {
        mockMvc.perform(get("/api/populationgroups/filter/state/" + "false").header("Authorization", token))
                .andExpect(status().isOk());
    }

}
