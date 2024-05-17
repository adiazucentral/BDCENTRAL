/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation.orders;

import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/listorder funcionalidades de
 * asignación de ordenes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 04/10/2017
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
public class UnlockConcurrencyTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OrderService orderService;
    private MockMvc mockMvc;
    private String token;

    public UnlockConcurrencyTest()
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
    public void test_01_deleteAllConcurrency() throws Exception
    {
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (0,NULL,201711020001,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (0,NULL,201711020002,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (0,NULL,201711020003,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (1,1,80189895,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (1,1,123456789,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (1,1,987654321,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (1,2,519231,1,now())", null);

        mockMvc.perform(delete("/api/concurrencies")
                .header("Authorization", token))
                .andExpect(content().string("7"));

    }

    @org.junit.Test
    public void test_02_deleteOrderConcurrency() throws Exception
    {

        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (0,NULL,201711020001,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (0,NULL,201711020002,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (0,NULL,201711020003,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (1,1,80189895,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (1,1,123456789,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (1,1,987654321,1,now())", null);
        TestScript.execTestUpdateScript("INSERT INTO lab113 VALUES (1,2,519231,1,now())", null);

        mockMvc.perform(delete("/api/concurrencies/order/{order}".replace("{order}", "201711020002"))
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("1"));
    }

    @org.junit.Test
    public void test_03_deleteHistoryConcurrency() throws Exception
    {

        mockMvc.perform(delete("/api/concurrencies/record/{type}/{record}".replace("{record}", "80189895").replace("{type}", "1"))
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

}
