/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.test;

//import net.cltech.enterprisent.domain.masters.test.Container;
//import net.cltech.enterprisent.domain.masters.user.User;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Requirement;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;
//import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/trackings
 *
 * @version 1.0.0
 * @author enavas
 * @since 27/04/2017
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
public class TrackingTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;
    private Integer idToken;

    public TrackingTest()
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
        token = TestTools.userToken(mockMvc, JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue())));
        idToken = TestTools.idUserToken(mockMvc, JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue())));
    }

    @Test
    public void test_01_getdateTrackingsUserNoContent() throws Exception
    {
        TestScript.deleteData("lab34");
        TestScript.deleteData("lab36");
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        String strDate = sm.format(new Date());
        mockMvc.perform(get("/api/trackings/filter/date/" + strDate + "/" + strDate + "/user/" + 1).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_getdateTrackings() throws Exception
    {
        TestScript.deleteData("lab39");
        net.cltech.enterprisent.domain.masters.test.Test test = new net.cltech.enterprisent.domain.masters.test.Test();
        test.setTestType((short) 0);
        test.getArea().setId(2);
        test.setCode("90001");
        test.setName("HEMOGLOBINAS1");
        test.setAbbr("HMG1");
        test.getGender().setId(7);
        test.setResultType((short) 2);
        test.setDeliveryDays(3);
        test.setSelfValidation(4);
        test.setPrintOnReport((short) 5);
        test.setProcessingBy((short) 15);
        test.getUser().setId(1);
        Requirement requirement = new Requirement();
        requirement.setId(2);
        test.getRequirements().add(requirement);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(test);

        mockMvc.perform(post("/api/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("90001")))
                .andExpect(jsonPath("$.name", is("HEMOGLOBINAS1")))
                .andExpect(jsonPath("$.abbr", is("HMG1")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        String strDate = sm.format(new Date());
        mockMvc.perform(get("/api/trackings/filter/dates/" + strDate + "/" + strDate).header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_03_getdateTrackingsUser() throws Exception
    {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        String strDate = sm.format(new Date());
        mockMvc.perform(get("/api/trackings/filter/date/" + strDate + "/" + strDate + "/user/" + this.idToken).header("Authorization", token))
                .andExpect(status().isOk());
    }

}
