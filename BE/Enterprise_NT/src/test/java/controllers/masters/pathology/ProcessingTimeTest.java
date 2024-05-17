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
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.pathology.ProcessingTime;
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
 * Prueba unitaria sobre el api rest /api/pathology/processingtime
 *
 * @version 1.0.0
 * @author omendez
 * @since 13/07/2021
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
public class ProcessingTimeTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ProcessingTimeTest() {
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
    public void test_01_processingTimeNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/processingtime").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_insertProcessingTime() throws Exception
    {
        ProcessingTime processingTime = new ProcessingTime();
        processingTime.setTime("06:30");
        processingTime.setStatus(1);
        processingTime.getUserCreated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(processingTime);

        mockMvc.perform(post("/api/pathology/processingtime")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.time", is("06:30")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.createdAt", Matchers.notNullValue())); 
    }
    
    @Test
    public void test_03_getAllProcessingTime() throws Exception
    {
        mockMvc.perform(get("/api/pathology/processingtime").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getProcessingTimeById() throws Exception
    {
        mockMvc.perform(get("/api/pathology/processingtime/filter/id/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getProcessingTimeByTime() throws Exception
    {
        mockMvc.perform(get("/api/pathology/processingtime/filter/time/" + "06:30").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getProcessingTimeByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/processingtime/filter/id/" + 0).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_getProcessingTimeByTimeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/processingtime/filter/time/" + "8:00").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_08_insertProcessingTimeError() throws Exception
    {
        ProcessingTime processingTime = new ProcessingTime();
        processingTime.setTime("");
        processingTime.setStatus(-1);
        processingTime.getUserCreated().setId(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(processingTime);

        mockMvc.perform(post("/api/pathology/processingtime")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_09_updateProcessingTime() throws Exception
    {
        ProcessingTime processingTime = new ProcessingTime();
        processingTime.setId(1);
        processingTime.setTime("09:00");
        processingTime.setStatus(1);
        processingTime.getUserUpdated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(processingTime);

        mockMvc.perform(put("/api/pathology/processingtime")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.time", is("09:00")))
                .andExpect(jsonPath("$.updatedAt", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateProcessingTimeError() throws Exception
    {
        ProcessingTime processingTime = new ProcessingTime();
        processingTime.setId(-1);
        processingTime.setTime("");
        processingTime.setStatus(-1);
        processingTime.getUserUpdated().setId(-1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(processingTime);

        mockMvc.perform(put("/api/pathology/processingtime")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_getAllProcessingTimeByState() throws Exception
    {
        mockMvc.perform(get("/api/pathology/processingtime/filter/state/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_12_getAllProcessingTimeByStateNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/processingtime/filter/state/" + 2).header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
