package controllers.masters.opportunity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
 * Prueba requirementaria sobre el api rest /api/binds
 *
 * @version 1.0.0
 * @author eacuna
 * @since 15/02/2018
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
public class BindTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public BindTest()
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
    }

    @Test
    public void test_01_noContent() throws Exception
    {
        mockMvc.perform(get("/api/binds").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_add() throws Exception
    {
        Bind bind = new Bind();
        bind.setName("1");
        bind.setMinimum(1l);
        bind.setMaximum(160l);

        bind.setUser(new AuthorizedUser());
        bind.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bind);

        mockMvc.perform(post("/api/binds/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void test_03_content() throws Exception
    {
        mockMvc.perform(get("/api/binds").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("1")))
                .andExpect(jsonPath("$[0].minimum", is(1)))
                .andExpect(jsonPath("$[0].maximum", is(160)));
    }

    @Test
    public void test_04_editBind() throws Exception
    {
        Bind bind = new Bind();
        bind.setId(1);
        bind.setName("2");
        bind.setMinimum(14l);
        bind.setMaximum(26l);
        bind.setState(false);

        bind.setUser(new AuthorizedUser());
        bind.getUser().setId(1);

        String jsonContent = Tools.jsonObject(bind);

        mockMvc.perform(put("/api/binds/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));

    }

    @Test
    public void test_05_contentById() throws Exception
    {
        mockMvc.perform(get("/api/binds/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("2")))
                .andExpect(jsonPath("$.minimum", is(14)))
                .andExpect(jsonPath("$.maximum", is(26)));
    }

    @Test
    public void test_06_bindErrorDuplicate() throws Exception
    {
        Bind bind = new Bind();
        bind.setName("2");
        bind.setMinimum(14l);
        bind.setMaximum(26l);
        bind.setState(false);

        bind.setUser(new AuthorizedUser());
        bind.getUser().setId(1);

        String jsonContent = Tools.jsonObject(bind);

        mockMvc.perform(post("/api/binds/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name")));
    }

    @Test
    public void test_07_bindError() throws Exception
    {
        Bind bind = new Bind();
        String jsonContent = Tools.jsonObject(bind);
        mockMvc.perform(post("/api/binds/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|user")));
    }

}
