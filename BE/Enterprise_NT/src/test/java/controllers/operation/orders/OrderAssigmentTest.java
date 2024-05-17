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
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.ListEnum;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
public class OrderAssigmentTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OrderService orderService;
    private MockMvc mockMvc;
    private String token;

    public OrderAssigmentTest()
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
        user.setId(-1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_noPatientOrder() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.execTestUpdateScript("UPDATE lab22 set lab21c1 = 0 WHERE lab22c1 in (201710040001,201710040002)", null);
        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/filter/nopatient")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201710040001L, 201710040002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20171004, 20171004)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_02_assignPatientError() throws Exception
    {
        Order order = new Order();
        order.setOrderNumber(201709089999L);
        order.getPatient().setId(8);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);

        mockMvc.perform(patch("/api/orders/assign/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_03_assignPatient() throws Exception
    {
        Order order = new Order();
        order.setOrderNumber(201710040001L);
        order.getPatient().setId(3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);

        mockMvc.perform(patch("/api/orders/assign/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("1"));

    }

    @org.junit.Test
    public void test_04_noPatientOrder() throws Exception
    {

        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/filter/nopatient")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201710040002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20171004)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_05_assignPatient() throws Exception
    {
        Order order = new Order();
        order.setOrderNumber(201710040002L);
        order.getPatient().setId(2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);

        mockMvc.perform(patch("/api/orders/assign/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("1"));

    }

    @org.junit.Test
    public void test_06_noPatientOrder_noContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/filter/nopatient")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_07_assignPatientTestVerifyError() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab39 set lab39c6 = ? WHERE lab39c1 = 8", ListEnum.Gender.FEMALE.getValue());
        Order order = new Order();
        order.setOrderNumber(201710040003L);
        order.getPatient().setId(3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);

        mockMvc.perform(patch("/api/orders/assign/verify")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("3|test|8|003|CONFIDENCIAL|2")));

    }

    @org.junit.Test
    public void test_08_assignPatientTestError() throws Exception
    {
        Order order = new Order();
        order.setOrderNumber(201710040003L);
        order.getPatient().setId(3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);

        mockMvc.perform(patch("/api/orders/assign/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("3|test|8|003|CONFIDENCIAL|2")));
    }
}
