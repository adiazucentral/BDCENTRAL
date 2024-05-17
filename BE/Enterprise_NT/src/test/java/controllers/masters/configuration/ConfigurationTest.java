package controllers.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.configuration.InitialConfiguration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.tools.DateTools;
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
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @Test
    public void test_00_initialConfiguration_error() throws Exception
    {
        InitialConfiguration initial = new InitialConfiguration();
        initial.setConfig(new ArrayList<>());
        initial.getConfig().add(new Configuration(Configuration.KEY_ENTITY, "CLtech"));
        initial.getConfig().add(new Configuration(Configuration.KEY_ABVR, "CLT"));
        initial.getConfig().add(new Configuration(Configuration.KEY_PHONE_FORMAT, "##-#####"));

        mockMvc.perform(put("/api/configuration/start/settings")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(initial)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|branch", "0|user")));
        initial.setBranch(new Branch());
        initial.getBranch().setAbbreviation("GRL");
//        initial.getBranch().setName("General");
        initial.getBranch().setCode("1");
        initial.getBranch().setPhone("222222");
        initial.getBranch().setResponsable("testuser");

        initial.setUser(new User());
        initial.getUser().setPassword("12345");
        initial.getUser().setExpiration(new Date(LocalDate.now().plusYears(5).toEpochDay()));

        mockMvc.perform(put("/api/configuration/start/settings")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(initial)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name")));

    }

    @Test
    public void test_01_initialConfiguration() throws Exception
    {
        InitialConfiguration initial = new InitialConfiguration();
        initial.setConfig(new ArrayList<>());
        initial.getConfig().add(new Configuration(Configuration.KEY_ENTITY, "CLtech"));
        initial.getConfig().add(new Configuration(Configuration.KEY_ABVR, "CLT"));
        initial.getConfig().add(new Configuration(Configuration.KEY_PHONE_FORMAT, "##-#####"));

        initial.setBranch(new Branch());
        initial.getBranch().setAbbreviation("GRL");
        initial.getBranch().setName("General");
        initial.getBranch().setCode("1");
        initial.getBranch().setPhone("222222");
        initial.getBranch().setResponsable("testuser");
        initial.getBranch().setMinimum(2);
        initial.getBranch().setMaximum(11);

        String userJson = TestTools.getResponseString(mockMvc.perform(get("/api/users/filter/username/" + "admin").header("Authorization", token)));
        initial.setUser(Tools.jsonObject(userJson, User.class));
        initial.getUser().setPassword("12345");
        initial.getUser().setExpiration(DateTools.asDate(LocalDate.now().plusYears(5)));
        mockMvc.perform(put("/api/configuration/start/settings")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(initial)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_02_getAllConfiguration() throws Exception
    {
        mockMvc.perform(get("/api/configuration").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(204)));
    }

    @Test
    public void test_03_getKey() throws Exception
    {
        mockMvc.perform(get("/api/configuration/" + "Cliente").header("Authorization", token))
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
    public void test_05_updateKey() throws Exception
    {
        List<Configuration> configurations = new ArrayList<>(0);
        Configuration config = new Configuration();
        config.setKey("Entidad");
        config.setValue("CLTech");
        configurations.add(config);

        config = new Configuration();
        config.setKey("FormatoFecha");
        config.setValue("dd/MM/yyyy");
        configurations.add(config);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(configurations);

        mockMvc.perform(put("/api/configuration")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].key", is("Entidad")))
                .andExpect(jsonPath("$[0].value", is("CLTech")))
                .andExpect(jsonPath("$[1].key", is("FormatoFecha")))
                .andExpect(jsonPath("$[1].value", is("dd/MM/yyyy")));
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

    @Test
    public void test_07_updateSecurity() throws Exception
    {
        List<Configuration> configurations = new ArrayList<>(0);
        Configuration config = new Configuration();
        config.setKey("UrlSecurity");
        config.setValue("http://192.168.1.6:8080/Security_NT_PRU");
        configurations.add(config);

        config = new Configuration();
        config.setKey("UrlDischarge");
        config.setValue("http://192.168.1.147/API_GOD");
        configurations.add(config);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(configurations);

        mockMvc.perform(put("/api/configuration/updatesecurity")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].key", is("UrlSecurity")))
                .andExpect(jsonPath("$[0].value", is("http://192.168.1.6:8080/Security_NT_PRU")))
                .andExpect(jsonPath("$[1].key", is("UrlDischarge")))
                .andExpect(jsonPath("$[1].value", is("http://192.168.1.147/API_GOD")));
    }

    @Test
    public void test_08_updateSecurityError() throws Exception
    {
        List<Configuration> configurations = new ArrayList<>(0);
        Configuration config = new Configuration();
        config.setKey("UrlSecurity");
        config.setValue("http://192.168.1.6:8080/Security_NT_PRU");
        configurations.add(config);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(configurations);

        mockMvc.perform(put("/api/configuration/updatesecurity")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|keys incorrect")));
    }
    
    
    
    
}
