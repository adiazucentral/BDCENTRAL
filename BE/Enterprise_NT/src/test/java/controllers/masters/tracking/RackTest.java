package controllers.masters.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
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
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba sobre el api rest /api/racks
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/05/2018
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
public class RackTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public RackTest()
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
        user.setBranch(1);
        token = TestTools.userToken(mockMvc, JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue())));
    }

    @Test
    public void test_01_noContent() throws Exception
    {
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab16 RESTART IDENTITY;", null);
        mockMvc.perform(get("/api/racks").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_add() throws Exception
    {
        Rack rack = new Rack();
        rack.setCode("1");
        rack.setName("Bodega");
        rack.setType(1);

        mockMvc.perform(post("/api/racks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(rack)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Bodega")))
                .andExpect(jsonPath("$.state", is(0)));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/racks").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_getById() throws Exception
    {
        mockMvc.perform(get("/api/racks/filter/id/{id}".replace("{id}", "1")).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Bodega")));
    }

    @Test
    public void test_05_edit() throws Exception
    {
        Rack rack = Tools.jsonObject(
                TestTools.getResponseString(mockMvc.perform(get("/api/racks/filter/id/{id}".replace("{id}", "1")).header("Authorization", token))),
                Rack.class);
        rack.setName("Grilla 1");
        rack.setState(LISEnum.RackState.CLOSED.getValue());
        rack.setRow(4);
        rack.setColumn(3);

        mockMvc.perform(put("/api/racks/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(rack)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Grilla 1")))
                .andExpect(jsonPath("$.state", is(0)))
                .andExpect(jsonPath("$.row", is(4)))
                .andExpect(jsonPath("$.column", is(3)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }

    @Test
    public void test_06_getByName() throws Exception
    {
        mockMvc.perform(get("/api/racks/filter/name/{name}".replace("{name}", "Grilla 1")).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Grilla 1")))
                .andExpect(jsonPath("$.row", is(4)))
                .andExpect(jsonPath("$.column", is(3)))
                .andExpect(jsonPath("$.state", is(0)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }

    @Test
    public void test_07_duplicateFields() throws Exception
    {
        Rack bean = new Rack();

        bean.setName("grilla 1");
        bean.setType(1);
        bean.setUser(new AuthorizedUser());
        bean.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bean);

        mockMvc.perform(post("/api/racks/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name")));
    }

    @Test
    public void test_08_emptyField() throws Exception
    {
        Rack rack = new Rack();

        mockMvc.perform(post("/api/racks/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(rack)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|type")));
    }

    @Test
    public void test_09_getByState() throws Exception
    {
        mockMvc.perform(get("/api/racks/filter/state/" + "false").header("Authorization", token))
                .andExpect(status().isOk());
    }

}
