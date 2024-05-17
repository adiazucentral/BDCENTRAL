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
import net.cltech.enterprisent.domain.masters.billing.Receiver;
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
 * Prueba unitaria sobre el api rest /api/receivers
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/07/2017
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
public class ReceiverTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ReceiverTest()
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
    public void test_01_getAllReceiverNoContent() throws Exception
    {
        mockMvc.perform(get("/api/receivers").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertReceiver() throws Exception
    {
        Receiver receiver = new Receiver();
        receiver.setName("Receptor");
        receiver.setApplicationReceiverCode("COD1");
        receiver.setReceiverID("REC1");
        receiver.setInterchangeReceiver("INT1");
        receiver.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(receiver);

        mockMvc.perform(post("/api/receivers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Receptor")))
                .andExpect(jsonPath("$.applicationReceiverCode", is("COD1")))
                .andExpect(jsonPath("$.receiverID", is("REC1")))
                .andExpect(jsonPath("$.interchangeReceiver", is("INT1")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllReceiver() throws Exception
    {
        mockMvc.perform(get("/api/receivers").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getReceiverById() throws Exception
    {
        mockMvc.perform(get("/api/receivers/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Receptor")))
                .andExpect(jsonPath("$.applicationReceiverCode", is("COD1")))
                .andExpect(jsonPath("$.receiverID", is("REC1")))
                .andExpect(jsonPath("$.interchangeReceiver", is("INT1")));
    }

    @Test
    public void test_05_getReceiverByName() throws Exception
    {
        mockMvc.perform(get("/api/receivers/filter/name/" + "Receptor").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Receptor")))
                .andExpect(jsonPath("$.applicationReceiverCode", is("COD1")))
                .andExpect(jsonPath("$.receiverID", is("REC1")))
                .andExpect(jsonPath("$.interchangeReceiver", is("INT1")));
    }

    @Test
    public void test_06_getReceiverByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/receivers/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_getReceiverByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/receivers/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_insertReceiverError() throws Exception
    {
        Receiver receiver = new Receiver();
        receiver.setName("Receptor");
        receiver.setApplicationReceiverCode("COD1");
        receiver.setReceiverID("REC1");
        receiver.setInterchangeReceiver("INT1");
        receiver.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(receiver);

        mockMvc.perform(post("/api/receivers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_09_updateReceiver() throws Exception
    {
        Receiver receiver = new Receiver();
        receiver.setId(1);
        receiver.setName("Occidente");
        receiver.setApplicationReceiverCode("COD01");
        receiver.setReceiverID("REC01");
        receiver.setInterchangeReceiver("INT01");
        receiver.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(receiver);

        mockMvc.perform(put("/api/receivers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Occidente")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateReceiverError() throws Exception
    {
        Receiver receiver = new Receiver();
        receiver.setId(-1);
        receiver.setName("Occidente");
        receiver.setApplicationReceiverCode("COD01");
        receiver.setReceiverID("REC01");
        receiver.setInterchangeReceiver("INT01");
        receiver.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(receiver);

        mockMvc.perform(put("/api/receivers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_getReceiverUpById() throws Exception
    {
        mockMvc.perform(get("/api/receivers/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Occidente")))
                .andExpect(jsonPath("$.applicationReceiverCode", is("COD01")))
                .andExpect(jsonPath("$.receiverID", is("REC01")))
                .andExpect(jsonPath("$.interchangeReceiver", is("INT01")));
    }

    @Test
    public void test_12_getReceiverUpByName() throws Exception
    {
        mockMvc.perform(get("/api/receivers/filter/name/" + "Occidente").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Occidente")))
                .andExpect(jsonPath("$.applicationReceiverCode", is("COD01")))
                .andExpect(jsonPath("$.receiverID", is("REC01")))
                .andExpect(jsonPath("$.interchangeReceiver", is("INT01")));
    }
}
