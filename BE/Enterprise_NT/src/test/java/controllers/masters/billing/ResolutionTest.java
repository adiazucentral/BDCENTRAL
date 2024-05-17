/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.Provider;
import net.cltech.enterprisent.domain.masters.billing.Resolution;
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
 * Prueba unitaria sobre el api rest /api/resolutions
 *
 * @version 1.0.0
 * @author cmartin
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
public class ResolutionTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ResolutionTest()
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
    public void test_01_insertProvider() throws Exception
    {

        Provider provider = new Provider();
        provider.setNit("1007100");
        provider.setName("Cltech1");
        provider.setPhone("7777777");
        provider.setCode("123AB");
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
                .andExpect(jsonPath("$.nit", is("1007100")))
                .andExpect(jsonPath("$.name", is("Cltech1")))
                .andExpect(jsonPath("$.phone", is("7777777")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_01_getAllResolutionNoContent() throws Exception
    {
        mockMvc.perform(get("/api/resolutions").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertResolution() throws Exception
    {
        Resolution resolution = new Resolution();
        resolution.setResolutionDIAN("11111");
        resolution.setFromNumber(1);
        resolution.setToNumber(5000);
        resolution.setPrefix("A");
        resolution.setInitialNumber(1);
        resolution.getProvider().setId(1);
        resolution.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(resolution);

        mockMvc.perform(post("/api/resolutions")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.resolutionDIAN", is("11111")))
                .andExpect(jsonPath("$.fromNumber", is(1)))
                .andExpect(jsonPath("$.toNumber", is(5000)))
                .andExpect(jsonPath("$.prefix", is("A")))
                .andExpect(jsonPath("$.initialNumber", is(1)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllResolution() throws Exception
    {
        mockMvc.perform(get("/api/resolutions").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getResolutionById() throws Exception
    {
        mockMvc.perform(get("/api/resolutions/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.resolutionDIAN", is("11111")))
                .andExpect(jsonPath("$.fromNumber", is(1)))
                .andExpect(jsonPath("$.toNumber", is(5000)))
                .andExpect(jsonPath("$.prefix", is("A")))
                .andExpect(jsonPath("$.initialNumber", is(1)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_06_getResolutionByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/resolutions/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_updateReceiver() throws Exception
    {
        Resolution resolution = new Resolution();
        resolution.setId(1);
        resolution.setResolutionDIAN("1ABCD");
        resolution.setFromNumber(1);
        resolution.setToNumber(5000);
        resolution.setPrefix("A");
        resolution.setInitialNumber(2000);
        resolution.getProvider().setId(1);
        resolution.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(resolution);

        mockMvc.perform(put("/api/resolutions")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.resolutionDIAN", is("1ABCD")))
                .andExpect(jsonPath("$.fromNumber", is(1)))
                .andExpect(jsonPath("$.toNumber", is(5000)))
                .andExpect(jsonPath("$.prefix", is("A")))
                .andExpect(jsonPath("$.initialNumber", is(2000)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }
}
