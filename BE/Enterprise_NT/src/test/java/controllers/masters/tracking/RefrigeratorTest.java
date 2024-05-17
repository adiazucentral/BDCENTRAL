package controllers.masters.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.tracking.Refrigerator;
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
 * Prueba sobre el api rest /api/refrigerators
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/06/2017
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
public class RefrigeratorTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;
    private Branch newBranch;

    public RefrigeratorTest()
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
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));

        newBranch = TestTools.firstBranch(mockMvc, token);
    }

    @Test
    public void test_01_noContent() throws Exception
    {

        mockMvc.perform(get("/api/refrigerators").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_add() throws Exception
    {
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setName("Bodega");

        refrigerator.setBranch(newBranch);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(refrigerator);

        mockMvc.perform(post("/api/refrigerators/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Bodega")))
                .andExpect(jsonPath("$.state", is(true)));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/refrigerators").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_getById() throws Exception
    {
        mockMvc.perform(get("/api/refrigerators/filter/id/{id}".replace("{id}", "1")).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Bodega")))
                .andExpect(jsonPath("$.state", is(true)));
    }

    @Test
    public void test_05_edit() throws Exception
    {
        Refrigerator refrigerator = Tools.jsonObject(
                TestTools.getResponseString(mockMvc.perform(get("/api/refrigerators/filter/id/{id}".replace("{id}", "1")).header("Authorization", token))),
                Refrigerator.class);
        refrigerator.setName("Nevera de Muestras");
        refrigerator.setState(false);

        mockMvc.perform(put("/api/refrigerators/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(refrigerator)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Nevera de Muestras")))
                .andExpect(jsonPath("$.state", is(false)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }

    @Test
    public void test_06_getByName() throws Exception
    {
        mockMvc.perform(get("/api/refrigerators/filter/name/{name}".replace("{name}", "Nevera de Muestras")).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Nevera de Muestras")))
                .andExpect(jsonPath("$.state", is(false)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }

    @Test
    public void test_07_duplicateFields() throws Exception
    {
        Refrigerator bean = new Refrigerator();

        bean.setName("nevera de muestras");
        bean.setState(false);

        bean.setUser(new AuthorizedUser());
        bean.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bean);

        mockMvc.perform(post("/api/refrigerators/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name", "0|branch")));
    }

    @Test
    public void test_08_emptyField() throws Exception
    {
        Refrigerator population = new Refrigerator();

        population.setUser(new AuthorizedUser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(population);

        mockMvc.perform(post("/api/refrigerators/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|branch")));
    }

    @Test
    public void test_09_getByState() throws Exception
    {
        mockMvc.perform(get("/api/refrigerators/filter/state/" + "false").header("Authorization", token))
                .andExpect(status().isOk());
    }

}
