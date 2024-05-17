package controllers.operation.result.worklist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.results.worklist.WorklistFilter;
import net.cltech.enterprisent.service.interfaces.masters.test.WorksheetService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/worklist Hojas de trabajo
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/10/2017
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
public class WorklistTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WorksheetService worksheetService;
    private MockMvc mockMvc;
    private String token;

    public WorklistTest()
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
    public void test_01_generate() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.execTestUpdateScript("DELETE from lab38", null);
        TestScript.execTestUpdateScript("DELETE from lab37", null);

        TestScript.execTestUpdateScript("INSERT INTO lab37 values(1,'QUIMICA',1,1,0,1,now(),1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab38 values(1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab38 values(1,2)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab38 values(1,3)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab38 values(1,9)", null);

        WorklistFilter filter = new WorklistFilter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);
        filter.setResultPending(false);
        filter.getWorkLists().add(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/worklists/generate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.group", is(1)))
                .andExpect(jsonPath("$.orders[*]", Matchers.hasSize(9)))
                .andExpect(jsonPath("$.orders[*].orderNumber", containsInAnyOrder(201710040001L, 201710040002L, 201710040003L, 201709080001L, 201709080002L, 201710040005l, 201710040006l, 201710040007l, 201710040009l)))
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 201710040001)].tests[*].id", containsInAnyOrder(1, 2, 3, 9)))
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 201710040001)].tests[?(@.selected==true)].id", containsInAnyOrder(9)))
                .andExpect(jsonPath("$.orders[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_02_already_generated() throws Exception
    {
        WorklistFilter filter = new WorklistFilter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);
        filter.setResultPending(false);
        filter.getWorkLists().add(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/worklists/generate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_03_secuenceList() throws Exception
    {

        mockMvc.perform(get("/api/worklists/secuence/{worksheet}".replace("{worksheet}", "1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].number", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[0].user.id", is(1)))
                .andExpect(jsonPath("$[0].date", notNullValue()));
    }

    @org.junit.Test
    public void test_04_previous() throws Exception
    {

        mockMvc.perform(get("/api/worklists/filter/group/{group}/worklist/{worklist}".replace("{group}", "1").replace("{worklist}", "1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.group", is(1)))
                .andExpect(jsonPath("$.orders[*]", Matchers.hasSize(9)))
                .andExpect(jsonPath("$.orders[*].orderNumber", containsInAnyOrder(201710040001L, 201710040002L, 201710040003L, 201709080002L, 201709080001L, 201710040005L, 201710040006L, 201710040007L, 201710040009L)))
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 201710040001)].tests[*].id", containsInAnyOrder(1, 2, 3, 9)))
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 201710040001)].tests[?(@.selected==true)].id", containsInAnyOrder(9)))
                .andExpect(jsonPath("$.orders[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_05_reset() throws Exception
    {

        mockMvc.perform(delete("/api/worklists/{worklist}".replace("{worklist}", "1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("14"));
    }

    @org.junit.Test
    public void test_06_secuenceListNoContent() throws Exception
    {

        mockMvc.perform(get("/api/worklists/secuence/{worksheet}".replace("{worksheet}", "1"))
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_07_generate_exclude() throws Exception
    {
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040001, 1, '2017-09-13 11:33:13.552855', 1, 0, 4, NULL, NULL, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040001, 2, '2017-09-13 11:33:13.552855', 1, 0, 4, NULL, NULL, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040001, 3, '2017-09-13 11:33:13.552855', 1, 0, 4, NULL, NULL, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("UPDATE lab37 set lab37c5 = 1", null);

        WorklistFilter filter = new WorklistFilter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);
        filter.setResultPending(false);
        filter.getWorkLists().add(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/worklists/generate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.group", is(1)))
                .andExpect(jsonPath("$.orders[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.orders[*].orderNumber", containsInAnyOrder(201710040001L)))
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 201710040001)].tests[*].id", containsInAnyOrder(1, 2, 3, 9)))
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 201710040001)].tests[?(@.selected==true)].id", containsInAnyOrder(1, 2, 3, 9)));
    }

    @org.junit.Test
    public void test_08_generate_no_exclude() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab37 set lab37c5 = 0", null);

        WorklistFilter filter = new WorklistFilter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);
        filter.setResultPending(false);
        filter.getWorkLists().add(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/worklists/generate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.group", is(2)))
                .andExpect(jsonPath("$.orders[*]", Matchers.hasSize(8)))
                .andExpect(jsonPath("$.orders[*].orderNumber", containsInAnyOrder(201710040002L, 201710040005L, 201710040006L, 201710040007L, 201710040009L, 201710040003L, 201709080001L, 201709080002L)))
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 201710040002)].tests[*].id", containsInAnyOrder(1, 2, 3, 9)))
                .andExpect(jsonPath("$.orders[?(@.orderNumber == 201710040002)].tests[?(@.selected==true)].id", containsInAnyOrder(9)));
    }
}
