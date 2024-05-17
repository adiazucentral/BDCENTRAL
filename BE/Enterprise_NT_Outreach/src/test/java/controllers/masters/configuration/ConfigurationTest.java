package controllers.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.masters.configuration.Configuration;
import net.cltech.outreach.tools.JWT;
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
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/configuration
 *
 * @version 1.0.0
 * @author eacuna
 * @since 23/04/2018
 * @see Creacion
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        user.setId(-1);
        token = JWT.generate(user, 1);
    }

    @Test
    public void test_01_getAllConfiguration() throws Exception
    {
        mockMvc.perform(get("/api/configuration").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(37)));
    }

    @Test
    public void test_02_getKey() throws Exception
    {
        mockMvc.perform(get("/api/configuration/" + "Titulo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_02_getKeyNotFound() throws Exception
    {
        mockMvc.perform(get("/api/configuration/" + "LlaveNoExistente"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_03_updateKey() throws Exception
    {
        List<Configuration> configurations = new ArrayList<>(0);
        Configuration config = new Configuration();
        config.setKey("Titulo");
        config.setValue("CLTech");
        configurations.add(config);

        config = new Configuration();
        config.setKey("Historico");
        config.setValue("True");
        configurations.add(config);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(configurations);

        mockMvc.perform(put("/api/configuration")
                //                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].key", is("Titulo")))
                .andExpect(jsonPath("$[0].value", is("CLTech")))
                .andExpect(jsonPath("$[1].key", is("Historico")))
                .andExpect(jsonPath("$[1].value", is("True")));
    }
}
