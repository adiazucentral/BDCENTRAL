package controllers.operation.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.Arrays;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.results.BatchResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import net.cltech.enterprisent.domain.operation.results.UpdateResult;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class BatchResulTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public BatchResulTest()
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
        TestScript.execTestUpdateScript("UPDATE lab57 set lab57c8 = 1", null);

        BatchResultFilter filter = new BatchResultFilter();
        filter.setRangeType(BatchResultFilter.RANGE_TYPE_ORDER);
        filter.setInit(201710040001l);
        filter.setEnd(201710040005l);

        filter.setTest(5);
        filter.setResult("5.2");
        filter.setComment("asaaaa");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/results/batch")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("3"));
    }

    @org.junit.Test
    public void test_02_generate() throws Exception
    {
        BatchResultFilter filter = new BatchResultFilter();
        filter.setRangeType(BatchResultFilter.RANGE_TYPE_ORDER);
        filter.setInit(201710040001l);
        filter.setEnd(201710040005l);

        filter.setTest(5);
        filter.setResult("5.2");
        filter.setComment("asaaaa");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/results/batch")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("0"));
    }

    @org.junit.Test
    public void test_03_resultInformacion() throws Exception
    {
        ResultFilter filter = new ResultFilter();
        filter.setFilterId(1);
        filter.setFirstOrder(201710040001L);
        filter.setLastOrder(201710040001L);
        filter.setTestList(Arrays.asList(5));
        filter.setUserId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/results/orders/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].order", is(201710040001L)));
    }

    @org.junit.Test
    public void test_04_resultRerun() throws Exception
    {
        ResultFilter filter = new ResultFilter();
        filter.setFilterId(1);
        filter.setFirstOrder(201710040001L);
        filter.setLastOrder(201710040001L);
        filter.setTestList(Arrays.asList(5));
        filter.setUserId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);
        ResultTest result = Tools.jsonList(
                TestTools.getResponseString(mockMvc.perform(patch("/api/results/orders/tests")
                        .header("Authorization", token)
                        .contentType(TestTools.APPLICATION_JSON_UTF8)
                        .content(jsonContent))), ResultTest.class).get(0);
        result.setNewState(LISEnum.ResultTestState.RERUN.getValue());
        result.setResultRepetition(new ResultTestRepetition());
        result.getResultRepetition().setType('R');
        result.getResultRepetition().setReasonId(1);
        result.getResultRepetition().setReasonComment("Se repitio el resultado");

        jsonContent = mapper.writeValueAsString(result);

        mockMvc.perform(post("/api/results")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));

        jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/results/orders/tests")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].order", is(201710040001L)))
                .andExpect(jsonPath("$[0].repeatedResultValue", Matchers.notNullValue()));
        TestScript.createInitialOrders();
    }
    
    @Test
    public void test_05_updateResultForExam()throws Exception
    {

        UpdateResult updateResult = new UpdateResult();
        updateResult.setNumberOrder(Long.parseLong("201710040010"));
        updateResult.setOldExamIdentifier(6);
        updateResult.setNewExamIdentifier(7); 

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(updateResult);

        mockMvc.perform(put("/api/results/updateResultForExam")
                .header("Authorization", token)
                .content(jsonContent)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    
     @Test
    public void test_01_demographicQueryff() throws Exception {
        mockMvc.perform(get("/api/results/updateResultForEx"))
                .andExpect(status().isOk());
    }

}
