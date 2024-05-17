/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthenticationSession;
import net.cltech.enterprisent.domain.common.AuthenticationUser;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.common.JWTToken;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/sessionviewer
 *
 * @version 1.0.0
 * @author equijano
 * @since 30/11/2018
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
public class AuthenticationSessionTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public AuthenticationSessionTest()
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
        user.setBranch(1);
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_create() throws Exception
    {

        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = 'http://192.168.1.6:8080/Security_NT_PRU' WHERE lab98c1 = 'UrlSecurity';");
        TestScript.deleteData("lab100");

        AuthenticationSession auth = new AuthenticationSession();
        auth.setIdSession("1");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(auth);
        mockMvc.perform(post("/api/sessionviewer/deleteBySession")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));

        auth = new AuthenticationSession();
        auth.setIdSession("2");
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(auth);
        mockMvc.perform(post("/api/sessionviewer/deleteBySession")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));

        AuthenticationSession authenticationSession = new AuthenticationSession();
        authenticationSession.setIdSession("1");
        authenticationSession.setIp("0:0:0:0:0:0:0:1:57427");
        authenticationSession.setBranch(1);

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(authenticationSession);

        mockMvc.perform(post("/api/sessionviewer")
                .header("Authorization", getToken())
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.idSession", is("1")))
                .andExpect(jsonPath("$.dateRegister", Matchers.notNullValue()));

        authenticationSession = new AuthenticationSession();
        authenticationSession.setIdSession("2");
        authenticationSession.setIp("0:0:0:0:0:0:0:1:57428");
        authenticationSession.setBranch(1);
        jsonContent = mapper.writeValueAsString(authenticationSession);

        mockMvc.perform(post("/api/sessionviewer")
                .header("Authorization", getToken())
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.idSession", is("2")))
                .andExpect(jsonPath("$.dateRegister", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_02_list() throws Exception
    {
        mockMvc.perform(get("/api/sessionviewer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].idSession", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].ip", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_03_deleteBySession() throws Exception
    {
        AuthenticationSession auth = new AuthenticationSession();
        auth.setIdSession("1");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(auth);
        mockMvc.perform(post("/api/sessionviewer/deleteBySession")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("1"));
    }

    @org.junit.Test
    public void test_04_deleteAll() throws Exception
    {
        mockMvc.perform(delete("/api/sessionviewer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    
 

    private String getToken() throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("lismanager");
        user.setPassword("cltechmanager");
        user.setBranch(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        token = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(post("/api/authentication")
                .header("Authorization", "")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))), JWTToken.class).getToken();

        return token;
    }
    
  
}
