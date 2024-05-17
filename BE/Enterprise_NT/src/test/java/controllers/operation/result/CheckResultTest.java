package controllers.operation.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
public class CheckResultTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private String token;

    public CheckResultTest()
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
    public void test_01_pending_verification() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        Filter filter = new Filter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);
        filter.setOrderType(0);
        filter.setFilterState(Arrays.asList(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/checkresults/pending")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(6)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201710040005l, 201710040006l, 201710040007l, 201710040008l, 201710040009l, 201710040010l)));
    }

    @org.junit.Test
    public void test_02_pending_result() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab57 set lab57c8 = 0, lab57c16 = 4 where lab22c1 in (201710040005,201710040006)  ");

        Filter filter = new Filter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);
        filter.setOrderType(0);
        filter.setFilterState(Arrays.asList(3));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/checkresults/pending")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201710040005l, 201710040006l)));
    }

    @org.junit.Test
    public void test_03_test_patient_history() throws Exception
    {

        mockMvc.perform(get("/api/checkresults/results/record/80189895/document/0/test/25")
                .header("Authorization", token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/checkresults/results/record/80189895/document/0/test/2")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].order", containsInAnyOrder(201710040006l, 201710040007l)));
    }

    @org.junit.Test
    public void test_04_test_repeats() throws Exception
    {
        mockMvc.perform(get("/api/results/repeats/order/201709080001/test/7")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].result", containsInAnyOrder("25", "30")));
    }

    @org.junit.Test
    public void test_05_test_repeats_nocontent() throws Exception
    {
        mockMvc.perform(get("/api/results/repeats/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
