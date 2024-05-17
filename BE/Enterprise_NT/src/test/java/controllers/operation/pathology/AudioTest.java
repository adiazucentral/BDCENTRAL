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
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.pathology.Audio;
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
 * Prueba unitaria sobre el api rest /api/pathology/audio
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/06/2021
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
public class AudioTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public AudioTest() {
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
    public void test_01_insertAudio() throws Exception
    {
        Audio audio = new Audio();
        audio.setName("1623869353");
        audio.setExtension(".wav");
        audio.setUrl("/pathology/macroscopy/1623869353.wav");
        audio.getUserCreated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(audio);

        mockMvc.perform(post("/api/pathology/audio")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.name", is("1623869353")))
            .andExpect(jsonPath("$.extension", is(".wav")))
            .andExpect(jsonPath("$.url", is("/pathology/macroscopy/1623869353.wav")))
            .andExpect(jsonPath("$.id", Matchers.notNullValue()))
            .andExpect(jsonPath("$.createdAt", Matchers.notNullValue())); 
    }
    
    @Test
    public void test_02_insertAudioError() throws Exception
    {
        Audio audio = new Audio();
        audio.setName("");
        audio.setExtension("");
        audio.setUrl("");
        audio.getUserCreated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(audio);

        mockMvc.perform(post("/api/pathology/audio")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_03_updateAudio() throws Exception
    {
        Audio audio = new Audio();
        audio.setId(1);
        audio.setName("1623869343");
        audio.setExtension(".wav");
        audio.setUrl("/pathology/macroscopy/1623869343.wav");
        audio.getUserUpdated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(audio);

        mockMvc.perform(put("/api/pathology/audio")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.name", is("1623869343")))
            .andExpect(jsonPath("$.extension", is(".wav")))
            .andExpect(jsonPath("$.url", is("/pathology/macroscopy/1623869343.wav")))
            .andExpect(jsonPath("$.updatedAt", Matchers.notNullValue()));
    }
    
    @Test
    public void test_04_updateAudioError() throws Exception
    {
        Audio audio = new Audio();
        audio.setId(0);
        audio.setName("");
        audio.setExtension("");
        audio.setUrl("");
        audio.getUserUpdated().setId(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(audio);

        mockMvc.perform(put("/api/pathology/audio")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
    
}
