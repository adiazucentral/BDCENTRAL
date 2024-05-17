/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
public class OrderActivationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OrderService orderService;
    private MockMvc mockMvc;
    private String token;

    public OrderActivationTest()
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
    public void test_01_getInactiveOrders() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.execTestUpdateScript("DELETE FROM lab30 ", null);
        TestScript.execTestUpdateScript("INSERT INTO lab30 values(1,'Pruebas','Pruebas',16,'20171027 08:39:26.241',1,1) ", null);

        Reason reason = new Reason();
        reason.setInit(201710040001L);
        reason.setEnd(201710040005L);
        reason.setComment("Borrado de pruebas unitarias");
        reason.setDeleteType(1);
        reason.getMotive().setId(1);

        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit(20171004L);
        filter.setEnd(20171004L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(put("/api/specialdeletes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent));

        jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/audits/filter/deleted")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$[*].order.orderNumber", containsInAnyOrder(201710040001L, 201710040002L, 201710040003L, 201710040004L, 201710040005L)))
                .andExpect(jsonPath("$[0].last.reason.id", is(1)))
                .andExpect(jsonPath("$[0].last.user.id", is(1)));
    }

    @org.junit.Test
    public void test_02_activateOrder() throws Exception
    {
        List<Long> orders = new ArrayList<>();
        orders.add(201710040002L);
        orders.add(201710040004L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String jsonContent = mapper.writeValueAsString(orders);
        System.out.println(jsonContent);
        mockMvc.perform(patch("/api/orders/activate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201710040002L, 201710040004L)));
    }

    @org.junit.Test
    public void test_03_getInactiveOrders() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit(20171004L);
        filter.setEnd(20171004L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/audits/filter/deleted")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[*].order.orderNumber", containsInAnyOrder(201710040001L, 201710040003L, 201710040005L)))
                .andExpect(jsonPath("$[0].last.reason.id", is(1)))
                .andExpect(jsonPath("$[0].last.user.id", is(1)));
    }

}
