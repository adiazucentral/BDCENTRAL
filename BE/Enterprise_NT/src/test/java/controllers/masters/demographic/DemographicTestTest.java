/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.DemographicTest;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicTestService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
 * Prueba unitaria sobre el api rest /api/demographictest
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/02/2022
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
public class DemographicTestTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DemographicTestService demographicTestService;
    private MockMvc mockMvc;
    private String token;
    
    public DemographicTestTest(){}
    
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
    public void test_01_demographicTestNoData() throws Exception {
        mockMvc.perform(get("/api/demographictest").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_addDemographicTest() throws Exception
    {
        DemographicTest demographic = new DemographicTest();
        
        demographic.setIdDemographic1(1);
        demographic.setValueDemographic1(1);
        demographic.setIdDemographic2(1);
        demographic.setValueDemographic2(1);
        demographic.setIdDemographic3(1);
        demographic.setValueDemographic3(1);
        
        demographic.getTests().add(1);
        demographic.getTests().add(2);
        
        demographic.getUser().setId(1);
       
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographictest")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()));
    }
    
    @Test
    public void test_03_findById() throws Exception
    {
        mockMvc.perform(get("/api/demographictest/filter/id/" + 1)
                .header("Authorization", token))
                .andExpect(jsonPath("$.id", notNullValue()));
    }
    
    @Test
    public void test_04_findByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/demographictest/filter/id/" + 0)
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_05_editDemographicTest() throws Exception
    {
        DemographicTest demographic = new DemographicTest();
        demographic.setId(1);
        demographic.setIdDemographic1(1);
        demographic.setValueDemographic1(1);
        demographic.setIdDemographic2(1);
        demographic.setValueDemographic2(1);
        demographic.setIdDemographic3(1);
        demographic.setValueDemographic3(1);
        
        demographic.getTests().add(1);
        demographic.getTests().add(2);
        
        demographic.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(put("/api/demographictest")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()));
    }
    
    @Test
    public void test_06_addDemographicTestError() throws Exception
    {
        DemographicTest demographic = new DemographicTest();
        
        demographic.setIdDemographic1(0);
        demographic.setValueDemographic1(0);
        demographic.setIdDemographic2(0);
        demographic.setValueDemographic2(0);
        demographic.setIdDemographic3(0);
        demographic.setValueDemographic3(0);

        demographic.getUser().setId(1);
       
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographictest")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_07_editDemographicTestError() throws Exception
    {
        DemographicTest demographic = new DemographicTest();
        
        demographic.setId(1);
        demographic.setIdDemographic1(0);
        demographic.setValueDemographic1(0);
        demographic.setIdDemographic2(0);
        demographic.setValueDemographic2(0);
        demographic.setIdDemographic3(0);
        demographic.setValueDemographic3(0);

        demographic.getUser().setId(1);
       
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(put("/api/demographictest")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
}
