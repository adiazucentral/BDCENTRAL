package controllers.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.tools.Constants;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Agregar una descripcion de la clase
 *
 * @version 1.0.0
 * @author eacuna
 * @since 24/11/2017
 * @see Para cuando se crea una clase incluir
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    MongoTestAppContext.class,
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderSearchTest
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

    @org.junit.Test
    public void test_01_serchByRecord() throws Exception
    {
        TestScript.createInitialOrders();

        mockMvc.perform(get("/api/searchorders/record/80189895/document/0/init/0/end/0")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].orders", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].orders[*].orderNumber", containsInAnyOrder(201709080002L, 201710040006L, 201710040007L)));

        mockMvc.perform(get("/api/searchorders/record/80189895/document/0/init/20171101/end/20171124")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/searchorders/record/801898951/document/0/init/0/end/0")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_02_serchByLastName() throws Exception
    {
        mockMvc.perform(get("/api/searchorders/lastname/acu/gender/" + ListEnum.Gender.MALE.getValue() + "/init/0/end/0")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].orders[*].orderNumber", containsInAnyOrder(201709080002L, 201710040006L, 201710040007L)));

        mockMvc.perform(get("/api/searchorders/lastname/acur/gender/" + ListEnum.Gender.MALE.getValue() + "/init/0/end/0")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

    }

    @org.junit.Test
    public void test_03_serchByDates() throws Exception
    {
        mockMvc.perform(get("/api/searchorders/init/20170908/end/0")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orders[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)));

        mockMvc.perform(get("/api/searchorders/init/20170908/end/20171005")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].orders[*].orderNumber", Matchers.hasSize(12)));

        mockMvc.perform(get("/api/searchorders/init/20171101/end/20171125")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

    }

    @org.junit.Test
    public void test_04_serchByOrders() throws Exception
    {

        mockMvc.perform(get("/api/searchorders/order/201709080001")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));

        mockMvc.perform(get("/api/searchorders/order/201709080100")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

    }

    @org.junit.Test
    public void test_05_serchFilter() throws Exception
    {
        TestScript.execTestUpdateScript("update lab22 set lab22c7 = '12325' where lab22c1 = ?", 201710040006l);
        OrderSearchFilter filter = new OrderSearchFilter(0, 20170908l, 20171005l);
        filter.setDemographics(new ArrayList<>());
        filter.getDemographics().add(new FilterDemographic(Constants.ORDER_HIS, "1232"));

        mockMvc.perform(patch("/api/searchorders/filter")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(filter)))
                .andExpect(status().isNoContent());

        filter = new OrderSearchFilter();
        filter.setDemographics(new ArrayList<>());
        filter.getDemographics().add(new FilterDemographic(Constants.ORDER_HIS, "12325"));
        mockMvc.perform(patch("/api/searchorders/filter")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201710040006L)));
    }

    @org.junit.Test
    public void test_06_ordersWithPatient() throws Exception
    {
        OrderSearchFilter order = new OrderSearchFilter();
        order.setRangeType(1);
        order.setInit(201709080001L);
        order.setEnd(201710040010L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);

        mockMvc.perform(post("/api/searchorders/orderswithpatient")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].orderNumber", Matchers.hasSize(12)));
    }
}
