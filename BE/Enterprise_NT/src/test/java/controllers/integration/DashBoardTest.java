package controllers.integration;

import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Objeto que representa las pruebas unitarias de la integracion con dashboard
 *
 * @version 1.0.0
 * @author equijano
 * @since 10/12/2018
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
public class DashBoardTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

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
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

//    @Test
//    public void test_01_configuration() throws Exception
//    {
//        TestScript.createInitialOrders();
//        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = 'http://192.168.1.211:8080/Dashboards' WHERE lab98c1 = 'UrlDashBoard'", null);
//        mockMvc.perform(get("/api/dashboard/configuration")
//                .contentType(TestTools.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
//    }
    @Test
    public void test_02_licence() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = 'http://192.168.1.211:8080/Dashboards' WHERE lab98c1 = 'UrlLIS'", null);
        mockMvc.perform(get("/api/dashboard/ValidateKeyAccess/true")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_authenticationDashboard() throws Exception
    {
        mockMvc.perform(get("/api/dashboard/Get_User/lismanager/cltechmanager")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_05_getAllTestsDashBoard() throws Exception
    {
        
        
        
        mockMvc.perform(get("/api/dashboard/Get_Exams")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
           
    }
    
        @org.junit.Test
    public void test_06_listProductivity() throws Exception
    {
        mockMvc.perform(get("/Get_SectionProductivityByIdSection/1/2/3")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
           
    }


}
