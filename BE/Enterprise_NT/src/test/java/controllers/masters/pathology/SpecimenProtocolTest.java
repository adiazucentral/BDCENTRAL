/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.pathology.Sheet;
import net.cltech.enterprisent.domain.masters.pathology.SpecimenProtocol;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/pathology/protocol
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/01/2021
 * @see Creacion
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes
        = {
            MongoTestAppContext.class,
            TestAppContext.class
        })
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SpecimenProtocolTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public SpecimenProtocolTest() {}
    
    @Before
    public void setUp() throws Exception {

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
    
    @Test
    public void test_01_protocolNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/protocol").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_insertProtocol() throws Exception
    {
        SpecimenProtocol protocol = new SpecimenProtocol();
        protocol.getSpecimen().setId(1);
        protocol.getCasete().setId(1);
        protocol.getOrgan().setId(1);
        protocol.setQuantity(1);
        protocol.setProcessingHours(6);
        
        List<Sheet> sheets = new ArrayList<>();
        
        Sheet sheet = new Sheet();
        sheet.getColoration().setId(1);
        sheet.setQuantity(1);
        sheets.add(sheet);
  
        protocol.setSheets(sheets);
        protocol.getUserCreated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(protocol);

        mockMvc.perform(post("/api/pathology/protocol")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_03_getAllProtocols() throws Exception
    {
        mockMvc.perform(get("/api/pathology/protocol").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getProtocolById() throws Exception
    {
        mockMvc.perform(get("/api/pathology/protocol/filter/id/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getOrganByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/protocol/filter/id/" + 0).header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_06_insertProtocolError() throws Exception
    {
        SpecimenProtocol protocol = new SpecimenProtocol();
        protocol.getSpecimen().setId(0);
        protocol.getCasete().setId(0);
        protocol.getOrgan().setId(0);
        protocol.setQuantity(0);
        protocol.setProcessingHours(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(protocol);

        mockMvc.perform(post("/api/pathology/protocol")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_07_updateProtocol() throws Exception
    {
        SpecimenProtocol protocol = new SpecimenProtocol();
        protocol.setId(1);
        protocol.getSpecimen().setId(1);
        protocol.getCasete().setId(1);
        protocol.getOrgan().setId(1);
        protocol.setQuantity(1);
        protocol.setProcessingHours(8);
        
        List<Sheet> sheets = new ArrayList<>();
        
        Sheet sheet = new Sheet();
        sheet.getColoration().setId(1);
        sheet.setQuantity(1);
        sheets.add(sheet);
  
        protocol.setSheets(sheets);
        protocol.getUserCreated().setId(1);
        protocol.getUserUpdated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(protocol);

        mockMvc.perform(put("/api/pathology/protocol")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_08_updateOrganError() throws Exception
    {
        SpecimenProtocol protocol = new SpecimenProtocol();
        protocol.getSpecimen().setId(0);
        protocol.getCasete().setId(0);
        protocol.setQuantity(0);
        protocol.setProcessingHours(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(protocol);

        mockMvc.perform(put("/api/pathology/protocol")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_09_getProtocolBySpecimen() throws Exception
    {
        mockMvc.perform(get("/api/pathology/protocol/filter/specimen/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_10_getOrganBySpecimenNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/protocol/filter/specimen/" + 0).header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
