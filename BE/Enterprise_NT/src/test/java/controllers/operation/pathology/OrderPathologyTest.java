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
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.pathology.SampleRejection;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
* Prueba unitaria sobre el api rest /api/pathology/order
*
* @version 1.0.0
* @author omendez
* @since 07/10/2020
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
public class OrderPathologyTest
{
   @Autowired
   private WebApplicationContext webApplicationContext;
   private MockMvc mockMvc;
   private String token;
   
   public OrderPathologyTest()
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
        user.setBranch(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }
   
    @Test
    public void test_01_noContentOrder() throws Exception
    {
        mockMvc.perform(get("/api/pathology/order").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
   
    @Test
    public void test_02_getOrderPathology() throws Exception 
    {
        TestScript.createInitialOrders();

        mockMvc.perform(get("/api/pathology/order")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
   
    @Test
    public void test_03_noContentOrderFilters() throws Exception
    {       
         ResultFilter resultFilter = new ResultFilter();
         resultFilter.setFirstOrder(-1);
         resultFilter.setLastOrder(-1);
         resultFilter.setFirstDate(1);
         resultFilter.setLastDate(-1);

         ObjectMapper mapper = new ObjectMapper();
         mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
         String jsonContent = mapper.writeValueAsString(resultFilter);

         mockMvc.perform(patch("/api/pathology/order/filters")
                 .header("Authorization", token)
                 .contentType(TestTools.APPLICATION_JSON_UTF8)
                 .content(jsonContent))
                 .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_04_getOrderFilters() throws Exception
    {       
         ResultFilter resultFilter = new ResultFilter();
         resultFilter.setFirstOrder(-1);
         resultFilter.setLastOrder(-1);
         resultFilter.setFirstDate(20171001);
         resultFilter.setLastDate(20171031);

         ObjectMapper mapper = new ObjectMapper();
         mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
         String jsonContent = mapper.writeValueAsString(resultFilter);

         mockMvc.perform(patch("/api/pathology/order/filters")
                 .header("Authorization", token)
                 .contentType(TestTools.APPLICATION_JSON_UTF8)
                 .content(jsonContent))
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));

    }
    
    @Test
    public void test_05_sampleReject() throws Exception 
    {    
        TestScript.execTestUpdateScript("DELETE FROM lab30 ", null);
        TestScript.execTestUpdateScript("INSERT INTO lab30 values(1,'Pruebas','Pruebas',63,'20171027 08:39:26.241',1,1) ", null);
        
        SampleRejection rejection = new SampleRejection();
        rejection.getMotive().setId(1);
        rejection.setOrderNumber(201710040010L);
        rejection.getStudyType().setId(1);
        rejection.setObservation("Observacion");
        rejection.getUserCreated().setId(1);
                
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rejection);
        
        mockMvc.perform(post("/api/pathology/order/rejection")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.createdAt", Matchers.notNullValue()));
    }
    
    @Test
    public void test_06_ActiveSamples() throws Exception
    {      
        List<SampleRejection> samples = new ArrayList<>();
        
        SampleRejection sample = new SampleRejection();
        
        sample.setOrderNumber(201710040010L);
        sample.getStudyType().setId(1);
        
        samples.add(sample);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(samples);

        mockMvc.perform(patch("/api/pathology/order/activate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
}