package controllers.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.Provider;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
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
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/providers
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/05/2018
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
public class ProviderTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ProviderTest()
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
    public void test_01_getAll_NoContent() throws Exception
    {
        mockMvc.perform(get("/api/providers").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insert() throws Exception
    {

        Provider provider = new Provider();
        provider.setNit("1007100108");
        provider.setName("Cltech");
        provider.setPhone("7777777");
        provider.setCode("123A");
        provider.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(provider);

        mockMvc.perform(post("/api/providers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nit", is("1007100108")))
                .andExpect(jsonPath("$.name", is("Cltech")))
                .andExpect(jsonPath("$.phone", is("7777777")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllProvider() throws Exception
    {
        mockMvc.perform(get("/api/providers").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getProviderById() throws Exception
    {
        mockMvc.perform(get("/api/providers/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nit", is("1007100108")))
                .andExpect(jsonPath("$.name", is("Cltech")))
                .andExpect(jsonPath("$.phone", is("7777777")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getProviderByNit() throws Exception
    {
        mockMvc.perform(get("/api/providers/filter/nit/" + "1007100108").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_07_getProviderByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/providers/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getProviderByNitNoContent() throws Exception
    {
        mockMvc.perform(get("/api/providers/filter/nit/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_getProviderByNameContent() throws Exception
    {
        mockMvc.perform(get("/api/providers/filter/name/" + "Cltech").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_11_insertProviderError() throws Exception
    {
        Provider provider = new Provider();
        provider.setNit("1007100108");
        provider.setName("CAMC");
        provider.setPhone("7777777");
        provider.setCode("123A");
        provider.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(provider);

        mockMvc.perform(post("/api/providers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_12_updateProvider() throws Exception
    {
        Provider provider = new Provider();
        provider.setId(1);
        provider.setNit("1007100108");
        provider.setName("CAMC SA");
        provider.setPhone("7777");
        provider.setCode("123B");
        provider.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(provider);

        mockMvc.perform(put("/api/providers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nit", is("1007100108")))
                .andExpect(jsonPath("$.name", is("CAMC SA")))
                .andExpect(jsonPath("$.phone", is("7777")))
                .andExpect(jsonPath("$.code", is("123B")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_13_getProviderByName() throws Exception
    {
        mockMvc.perform(get("/api/providers/filter/name/" + "CAMC SA").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nit", is("1007100108")))
                .andExpect(jsonPath("$.name", is("CAMC SA")))
                .andExpect(jsonPath("$.phone", is("7777")))
                .andExpect(jsonPath("$.code", is("123B")));
    }

    @Test
    public void test_14_updateProviderError() throws Exception
    {
        Provider provider = new Provider();
        provider.setId(-1);
        provider.setNit("1007100108");
        provider.setName("CAMC SA");
        provider.setPhone("7777");
        provider.setCode("123A");
        provider.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(provider);

        mockMvc.perform(put("/api/providers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

}
