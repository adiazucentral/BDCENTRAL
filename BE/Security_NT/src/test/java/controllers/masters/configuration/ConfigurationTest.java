package controllers.masters.configuration;

import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.securitynt.domain.common.AuthorizedUser;
import net.cltech.securitynt.domain.masters.configuration.Configuration;
import net.cltech.securitynt.tools.JWT;
import net.cltech.securitynt.tools.Tools;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/configuration
 *
 * @version 1.0.0
 * @author dcortes
 * @since 16/04/2017
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
@Transactional(transactionManager = "transactionManager")
public class ConfigurationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ConfigurationTest()
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
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()), 1);
    }

    @Test
    public void test_02_getAllConfiguration() throws Exception
    {
        mockMvc.perform(get("/api/configuration").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(164)));
    }

    @Test
    public void test_03_getKey() throws Exception
    {
        mockMvc.perform(get("/api/configuration/" + "Entidad").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getKeyNotFound() throws Exception
    {
        mockMvc.perform(get("/api/configuration/" + "LlaveNoExistente").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_tokenExpiration() throws Exception
    {
        mockMvc.perform(get("/api/configuration/tokenexpiration")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.value", is("60")));
    }
}
