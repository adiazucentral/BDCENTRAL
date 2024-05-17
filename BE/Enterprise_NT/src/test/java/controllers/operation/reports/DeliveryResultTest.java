package controllers.operation.reports;

import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.reports.DeliveryResult;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/deliveryresults
 *
 * @version 1.0.0
 * @author eacuna
 * @since 27/11/2017
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
public class DeliveryResultTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;
    List<Question> questions;

    public DeliveryResultTest()
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
    public void test_01_noPrintedOrders() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        DeliveryResult delivery = new DeliveryResult();
        delivery.setOrders(Arrays.asList(201709080001L));
        delivery.setReceivesPerson("Diego Cortes");
        mockMvc.perform(post("/api/deliveryresults/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(delivery)))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @org.junit.Test
    public void test_02_printedOrdersError() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab57 set lab57c8 = ? WHERE lab22c1 in(201710040008,201710040009)", LISEnum.ResultTestState.DELIVERED.getValue());
        DeliveryResult delivery = new DeliveryResult();

        mockMvc.perform(post("/api/deliveryresults/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(delivery)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[0]", is("0|person")))
                .andExpect(jsonPath("$.errorFields[1]", is("0|order")));
    }

    @org.junit.Test
    public void test_03_printedOrders() throws Exception
    {
        DeliveryResult delivery = new DeliveryResult();
        delivery.setOrders(Arrays.asList(201710040008L, 201710040009L));
        delivery.setReceivesPerson("Diego Cortes");

        mockMvc.perform(post("/api/deliveryresults/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(delivery)))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_04_listDeliveriesPendingByDate() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab57 set lab57c8 = ?", LISEnum.ResultTestState.DELIVERED.getValue());
        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit(20170908l);
        filter.setEnd(20171004l);

        mockMvc.perform(patch("/api/deliveryresults/pending")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(9)));
    }

    @org.junit.Test
    public void test_05_listDeliveriesPendingByRange() throws Exception
    {

        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080000l);
        filter.setEnd(201710049999l);

        mockMvc.perform(patch("/api/deliveryresults/pending")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(9)));
    }

    @org.junit.Test
    public void test_06_printedOrders() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab57 set lab57c8 = ?", LISEnum.ResultTestState.DELIVERED.getValue());
        DeliveryResult delivery = new DeliveryResult();
        delivery.setOrders(Arrays.asList(201709080001l, 201709080002l, 201710040001l, 201710040002l, 201710040003l, 201710040004l, 201710040005l, 201710040006l, 201710040007l, 201710040008l, 201710040009l, 201710040010l));
        delivery.setReceivesPerson("Esteban");

        mockMvc.perform(post("/api/deliveryresults/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(delivery)))
                .andExpect(status().isOk())
                .andExpect(content().string("11"));
    }

    @org.junit.Test
    public void test_07_listDeliveriesByDate() throws Exception
    {

        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit(20170908l);
        filter.setEnd(20171004l);

        mockMvc.perform(patch("/api/deliveryresults/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(13)))
                .andExpect(jsonPath("$[?(@.orderNumber == 201710040008)]", hasSize(2)))
                .andExpect(jsonPath("$[?(@.orderNumber == 201710040008)].receivesPerson", containsInAnyOrder("Diego Cortes", "Esteban")))
                .andExpect(jsonPath("$[?(@.orderNumber == 201710040008)].resultTest[*].testId", containsInAnyOrder(5, 5)));
    }

    @org.junit.Test
    public void test_08_listDeliveriesByRange() throws Exception
    {

        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080000l);
        filter.setEnd(201710049999l);

        mockMvc.perform(patch("/api/deliveryresults/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(13)))
                .andExpect(jsonPath("$[?(@.orderNumber == 201710040008)]", hasSize(2)))
                .andExpect(jsonPath("$[?(@.orderNumber == 201710040008)].receivesPerson", containsInAnyOrder("Diego Cortes", "Esteban")))
                .andExpect(jsonPath("$[?(@.orderNumber == 201710040008)].resultTest[*].testId", containsInAnyOrder(5, 5)));
    }

    @org.junit.Test
    public void test_09_listDeliveriesByRangeAndDate() throws Exception
    {

        Filter filter = new Filter();
        filter.setRangeType(2);
        filter.setInit(1l);
        filter.setEnd(2l);
        filter.setInitDate(20170908);
        filter.setEndDate(20171004);

        mockMvc.perform(patch("/api/deliveryresults/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(4)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001l, 201709080002l, 201710040001l, 201710040002l)));
    }

}
