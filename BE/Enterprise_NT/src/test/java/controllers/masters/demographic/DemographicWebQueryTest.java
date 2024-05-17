package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.sql.Timestamp;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.DemographicWebQuery;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.hasSize;
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
 * Prueba unitaria al api rest /api/demographicwebquery
 * 
 * @version 1.0.0
 * @author javila
 * @since 27/01/2020
 * @see Creación
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    MongoTestAppContext.class,
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DemographicWebQueryTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public DemographicWebQueryTest() {
    }
    
    @Before
    public void setUp() throws Exception{
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
    public void test_01_insertDemopgraphicWebQuery() throws Exception{
        Timestamp timestamp = Timestamp.valueOf("2020-01-28 23:24:58");
        DemographicWebQuery demographicWebQuery = new DemographicWebQuery();
        demographicWebQuery.setUser("JULI");
        demographicWebQuery.setPassword("19732cq");
        demographicWebQuery.setPasswordExpirationDate(null);
        demographicWebQuery.setDateOfLastEntry(timestamp);
        demographicWebQuery.setNumberFailedAttempts(1);
        demographicWebQuery.setDemographic(12);
        demographicWebQuery.setIdDemographicItem(11);
        demographicWebQuery.setState(true);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicWebQuery);
        
        mockMvc.perform(post("/api/demographicwebquery")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.user", is("JULI")))
                .andExpect(jsonPath("$.passwordExpirationDate", Matchers.nullValue()))
                .andExpect(jsonPath("$.numberFailedAttempts", is(1)))
                .andExpect(jsonPath("$.demographic", is(12)))
                .andExpect(jsonPath("$.idDemographicItem", is(11)))
                .andExpect(jsonPath("$.state", is(true)));
    }
    
    @Test
    public void test_02_insertDemopgraphicWebQuery_Error() throws Exception{
        
        DemographicWebQuery demographicWebQuery = new DemographicWebQuery();
        demographicWebQuery.setUser("JULI");
        demographicWebQuery.setPassword("123456");
        demographicWebQuery.setPasswordExpirationDate(null);
        demographicWebQuery.setDemographic(12);
        demographicWebQuery.setIdDemographicItem(11);
        demographicWebQuery.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicWebQuery);
               
        mockMvc.perform(put("/api/demographicwebquery")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_03_listAll() throws Exception{
        mockMvc.perform(get("/api/demographicwebquery/all").header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    public void test_04_updateDemopgraphicWebQuery() throws Exception{
        Timestamp timestamp = Timestamp.valueOf("2020-01-28 23:24:58");
        DemographicWebQuery demographicWebQuery = new DemographicWebQuery();
        demographicWebQuery.setId(1);
        demographicWebQuery.setUser("julianS");
        demographicWebQuery.setPassword("Funciona");
        demographicWebQuery.setPasswordExpirationDate(timestamp);
        demographicWebQuery.setState(true);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicWebQuery);
        
        mockMvc.perform(put("/api/demographicwebquery")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_05_getFilterByIdDemographic() throws Exception{        
        mockMvc.perform(get("/api/demographicwebquery/filter/idDemographicItem/"+"12"+"/demographicwebquery/"+"11").header(token, "Authorization"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_06_listdeactivate() throws Exception{
        mockMvc.perform(get("/api/demographicwebquery/listdeactivate").header(token, "Authorization"))
                .andExpect(status().isOk());
    }
    
      
    @Test
    public void test_07_deactivate() throws Exception{
        DemographicWebQuery demographicWebQuery = new DemographicWebQuery();
        demographicWebQuery.setId(1);
        demographicWebQuery.setUser("julianS");
          
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicWebQuery);
        
        mockMvc.perform(put("/api/demographicwebquery")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.user", is("julianS")))
                .andExpect(jsonPath("$.state", is(false)));
    }
}
