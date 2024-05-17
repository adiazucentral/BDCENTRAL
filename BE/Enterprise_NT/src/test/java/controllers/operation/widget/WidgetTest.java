package controllers.operation.widget;

import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.Calendar;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.FixMethodOrder;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre los widget
 *
 * @version 1.0.0
 * @author equijano
 * @since 22/07/2019
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
public class WidgetTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public WidgetTest()
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

    @org.junit.Test
    public void test_01_widgetOrderEntry() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = 'http://192.168.1.4:8080/SIGA-Demo' WHERE lab98c1 = 'UrlSIGA'", null);
        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = '1' WHERE lab98c1 = 'OrdenesSIGA'", null);
        mockMvc.perform(get("/api/widgets/orderentry/" + DateTools.dateToNumber(new Date()))
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_02_widgetEntry() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE sta2 SET sta2c20 = '" + DateTools.dateToNumber(DateTools.changeDate(new Date(), Calendar.DAY_OF_YEAR, (-1) * 7)) + "' WHERE sta2c1 = '201709080001'", null);
        mockMvc.perform(get("/api/widgets/entry/20170908")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.branch", is(1)))
                .andExpect(jsonPath("$.ordersByDays[0]", is(0)));
    }

}
