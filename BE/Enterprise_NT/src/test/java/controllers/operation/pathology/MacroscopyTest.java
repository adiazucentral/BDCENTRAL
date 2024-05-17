/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.domain.masters.pathology.MacroscopyTemplate;
import net.cltech.enterprisent.domain.operation.pathology.Macroscopy;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
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
 * Prueba unitaria sobre el api rest /api/pathology/macroscopy
 *
 * @version 1.0.0
 * @author omendez
 * @since 18/06/2021
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
public class MacroscopyTest 
{
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public MacroscopyTest() {
    }
    
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
    public void test_01_noContentPendingTranscripts() throws Exception
    {
        mockMvc.perform(get("/api/pathology/macroscopy/filter/transcription").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_02_insertDescription() throws Exception
    {
        Macroscopy macroscopy = new Macroscopy();
        macroscopy.setCasePat(1);
        macroscopy.getPathologist().setId(1);
        macroscopy.setTranscription(1);
        macroscopy.getAudio().setId(1);
        macroscopy.setAuthorization(1);
        macroscopy.getAuthorizer().setId(1);
        List<MacroscopyTemplate> list = new ArrayList<>();
        MacroscopyTemplate template = new MacroscopyTemplate();
        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.setTemplate(1);
        field.setValue("Respuesta");
        fields.add(field);
        template.setFields(fields);
        list.add(template);
        macroscopy.setTemplates(list);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(macroscopy);

        mockMvc.perform(post("/api/pathology/macroscopy")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id", Matchers.notNullValue()))
            .andExpect(jsonPath("$.createdAt", Matchers.notNullValue())); 
    }
    
    @Test
    public void test_03_insertMacroscopyError() throws Exception
    {
        Macroscopy macroscopy = new Macroscopy();
        macroscopy.setCasePat(0);
        macroscopy.getPathologist().setId(0);
        macroscopy.setTranscription(null);
        macroscopy.getAudio().setId(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(macroscopy);

        mockMvc.perform(post("/api/pathology/macroscopy")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_04_updateMacroscopy() throws Exception
    {
        Macroscopy macroscopy = new Macroscopy();
        macroscopy.setId(1);
        macroscopy.setCasePat(1);
        macroscopy.getPathologist().setId(1);
        macroscopy.setTranscription(1);
        macroscopy.getAudio().setId(0);
        macroscopy.setAuthorization(1);
        macroscopy.getAuthorizer().setId(1);
        List<MacroscopyTemplate> list = new ArrayList<>();
        MacroscopyTemplate template = new MacroscopyTemplate();
        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.setTemplate(1);
        field.setValue("Respuesta Modificada");
        fields.add(field);
        template.setFields(fields);
        list.add(template);
        macroscopy.setTemplates(list);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(macroscopy);

        mockMvc.perform(put("/api/pathology/macroscopy")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_05_updateMacroscopyError() throws Exception
    {
        Macroscopy macroscopy = new Macroscopy();
        macroscopy.setId(1);
        macroscopy.setCasePat(0);
        macroscopy.getPathologist().setId(0);
        macroscopy.setTranscription(null);
        macroscopy.getAudio().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(macroscopy);

        mockMvc.perform(put("/api/pathology/macroscopy")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_06_pendingTranscriptsNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/macroscopy/filter/transcription").header("Authorization", token))
            .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_07_insertTranscription() throws Exception
    {
        Macroscopy macroscopy = new Macroscopy();
        macroscopy.setId(1);
        macroscopy.setCasePat(1);
        macroscopy.getTranscriber().setId(1);
        macroscopy.setTranscription(0);
        macroscopy.setDraft(1);
        macroscopy.setAuthorization(1);
        List<MacroscopyTemplate> list = new ArrayList<>();
        MacroscopyTemplate template = new MacroscopyTemplate();
        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.setTemplate(1);
        field.setValue("Respuesta");
        fields.add(field);
        template.setFields(fields);
        list.add(template);
        macroscopy.setTemplates(list);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(macroscopy);

        mockMvc.perform(post("/api/pathology/macroscopy/transcription")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_08_insertTranscriptionError() throws Exception
    {
        Macroscopy macroscopy = new Macroscopy();
        macroscopy.setCasePat(0);
        macroscopy.getPathologist().setId(0);
        macroscopy.getTranscriber().setId(0);
        macroscopy.setTranscription(null);
        macroscopy.getAudio().setId(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(macroscopy);

        mockMvc.perform(post("/api/pathology/macroscopy/transcription")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_09_pendingAuthorizationsNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/macroscopy/filter/authorization/1").header("Authorization", token))
            .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_10_insertAuthorization() throws Exception
    {
        Macroscopy macroscopy = new Macroscopy();
        macroscopy.setId(1);
        macroscopy.setCasePat(1);
        macroscopy.setDraft(1);
        List<MacroscopyTemplate> list = new ArrayList<>();
        MacroscopyTemplate template = new MacroscopyTemplate();
        List<Field> fields = new ArrayList<>();
        Field field = new Field();
        field.setTemplate(1);
        field.setValue("Respuesta");
        fields.add(field);
        template.setFields(fields);
        list.add(template);
        macroscopy.setTemplates(list);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(macroscopy);

        mockMvc.perform(post("/api/pathology/macroscopy/authorization")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_11_insertAuthorizationError() throws Exception
    {
        Macroscopy macroscopy = new Macroscopy();
        macroscopy.setCasePat(0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(macroscopy);

        mockMvc.perform(post("/api/pathology/macroscopy/authorization")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isInternalServerError());
    }
}
