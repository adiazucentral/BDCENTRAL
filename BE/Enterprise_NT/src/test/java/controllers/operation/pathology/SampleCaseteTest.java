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
import net.cltech.enterprisent.domain.operation.pathology.SampleCasete;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
* Prueba unitaria sobre el api rest /api/pathology/samples/casetes
*
* @version 1.0.0
* @author omendez
* @since 06/05/2021
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
public class SampleCaseteTest 
{
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public SampleCaseteTest()
    {}
    
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
        user.setBranch(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }
    
    @Test
    public void test_01_getBySpecimenNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/samples/casetes/1/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_02_create() throws Exception 
    {    
        TestScript.createInitialCases(); 
        
        List<SampleCasete> samples = new ArrayList<>();       
        SampleCasete sample = new SampleCasete();
        sample.setSample(1);
        sample.setQuantity(1);
        sample.setConsecutive("A");
        sample.getCasete().setId(1);
        
        samples.add(sample);
              
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(samples);
        
        mockMvc.perform(post("/api/pathology/samples/casetes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_03_getCaseById() throws Exception 
    {
        mockMvc.perform(get("/api/pathology/samples/casetes/1/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_04_createError() throws Exception
    {
        List<SampleCasete> samples = new ArrayList<>();       
        SampleCasete sample = new SampleCasete();
        sample.setSample(0);
        sample.setQuantity(0);
        sample.setConsecutive("");
        sample.getCasete().setId(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(samples);

        mockMvc.perform(post("/api/pathology/samples/casetes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_05_changeStatus() throws Exception
    {
        List<SampleCasete> samples = new ArrayList<>();       
        SampleCasete sample = new SampleCasete();
        sample.getCasete().setId(1);
        sample.setStatus(1);
        
        samples.add(sample);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(samples);
        
        mockMvc.perform(patch("/api/pathology/samples/casetes/status")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}
