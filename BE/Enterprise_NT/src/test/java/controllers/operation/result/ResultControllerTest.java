package controllers.operation.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.results.CentralSystemResults;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria al api rest /api/results/updateResultForExam
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 11/02/2020
 * @see Creación
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes
        =
        {
            TestAppContext.class
        })
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResultControllerTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ResultControllerTest()
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

    @Test
    public void test_01_updateResultForEx() throws Exception
    {
        mockMvc.perform(get("/api/results/updateResultForEx"))
                .andExpect(status().isOk());

    }

    @Test
    public void test_02_getTestByOrderSample() throws Exception
    {
        mockMvc.perform(get("/api/results/gettestby/order/201805073081/sample/2"))
                .andExpect(status().isOk());

    }

    @Test
    public void test_03_getAreasByOrderSample() throws Exception
    {
        mockMvc.perform(get("/api/results/getareasby/order/201805073081/sample/2"))
                .andExpect(status().isOk());

    }

    @Test
    public void test_04_getSamplesToTake() throws Exception
    {
        mockMvc.perform(get("/api/results/getsamplestotake/order/201806193396"))
                .andExpect(status().isOk());

    }

    @Test
    public void test_05_getProfiles() throws Exception
    {
        mockMvc.perform(get("/api/results/getprofiles/order/201806193396"))
                .andExpect(status().isOk());

    }

    @Test
    public void test_06_childTake() throws Exception
    {
        mockMvc.perform(get("/api/results/childtake/order/201806193396/idprofil/2019"))
                .andExpect(status().isOk());

    }

    @Test
    public void test_07_getCheckProfile() throws Exception
    {
        mockMvc.perform(get("/api/results/checkprofile/order/201806193396/profile/2019"))
                .andExpect(status().isOk());

    }

    @Test
    public void test_08_getSampleTaken() throws Exception
    {
        mockMvc.perform(get("/api/results/sampletaken/order/201806193396/codesample/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_09_sendOrderResultsCentralSystem() throws Exception
    {
        CentralSystemResults centralSystemResults = new CentralSystemResults();
        centralSystemResults.setStartDate("2020-07-16");
        centralSystemResults.setEndDate("2020-07-17");
        centralSystemResults.setIncludeTests("1,2,3,4");
        centralSystemResults.setCentralSystem(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(centralSystemResults);
        
        mockMvc.perform(post("/api/results/sendOrderResultsCentralSystem")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_10_updateSentCentralSystem() throws Exception
    {
        mockMvc.perform(put("/api/results/updateSentCentralSystem/idOrder/201709080001/idTest/8/idCentralSystem/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_11_findShippedOrdersCentralSystem() throws Exception
    {
        mockMvc.perform(get("/api/results/findShippedOrders/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_12_removeDevalidatedResults() throws Exception
    {
        mockMvc.perform(delete("/api/results/removeDevalidatedResults/1/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_13_updateReferenceValues() throws Exception
    {
        mockMvc.perform(put("/api/results/updateReferenceValues/idOrder/202103040001")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
}


