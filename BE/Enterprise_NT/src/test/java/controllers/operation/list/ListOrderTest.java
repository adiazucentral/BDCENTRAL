package controllers.operation.list;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
 * Prueba unitaria sobre el api rest /api/listorder
 *
 * @version 1.0.0
 * @author cmartin
 * @since 13/09/2017
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
public class ListOrderTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ListOrderTest()
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
    public void test_01_getFilterRangeDate() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());

        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20170915);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_02_getFilterRangeDateNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20170010);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_03_getFilterRangeOrder() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_04_getFilterRangeOrderNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709000001L);
        filter.setEnd(201709080000L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_05_getFilterOrderType() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setOrderType(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_06_getFilterOrderTypeNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709000001L);
        filter.setEnd(201709080000L);
        filter.setOrderType(3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_07_getFilterCheckAll() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setCheck(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].tests.[*].id", containsInAnyOrder(9, 8)))
                .andExpect(jsonPath("$[1].tests.[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].tests.[*].id", containsInAnyOrder(9, 8)));
    }

    @org.junit.Test
    public void test_08_getFilterCheckVerify() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setCheck(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);
        
        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[0].tests.[*].id", containsInAnyOrder(9)))
                .andExpect(jsonPath("$[0].tests.[*].result.sampleState", containsInAnyOrder(4)))
                .andExpect(jsonPath("$[1].tests.[*]", Matchers.hasSize(2)));
    }

    @org.junit.Test
    public void test_09_getFilterCheckNoVerifyNoContent() throws Exception
    {
//        Filter filter = new Filter();
//        filter.setRangeType(1);
//        filter.setInit(201709080001L);
//        filter.setEnd(201709080010L);
//        filter.setCheck(2);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String jsonContent = mapper.writeValueAsString(filter);
//
//        mockMvc.perform(patch("/api/listorders")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_10_getFilterCheckNoVerifyAndConfidential() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setCheck(2);
        filter.setTestFilterType(3);
        filter.setTests(Arrays.asList(6, 7, 8, 9, 10));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908)))
                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].tests.[*].id", containsInAnyOrder(8)))
                .andExpect(jsonPath("$[0].tests.[*].result.sampleState", containsInAnyOrder(2)));
    }

    @org.junit.Test
    public void test_11_getFilterTestAll() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setTestFilterType(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].tests.[*].id", containsInAnyOrder(9, 8)))
                .andExpect(jsonPath("$[0].tests.[*].area.id", containsInAnyOrder(2, 3)))
                .andExpect(jsonPath("$[1].tests.[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].tests.[*].id", containsInAnyOrder(9, 8)))
                .andExpect(jsonPath("$[1].tests.[*].area.id", containsInAnyOrder(2, 3)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_12_getFilterTestSection() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setTestFilterType(1);
        filter.setTests(Arrays.asList(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].tests.[*].id", containsInAnyOrder(9)))
                .andExpect(jsonPath("$[0].tests.[*].area.id", containsInAnyOrder(2)))
                .andExpect(jsonPath("$[1].tests.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[1].tests.[*].id", containsInAnyOrder(9)))
                .andExpect(jsonPath("$[1].tests.[*].area.id", containsInAnyOrder(2)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_13_getFilterTestSectionNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setTestFilterType(1);
        filter.setTests(Arrays.asList(4));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_14_getFilterTestTestsNoConfidential() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setTestFilterType(2);
        filter.setTests(Arrays.asList(6, 7, 8, 9, 10));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].tests.[0].id", is(9)))
                .andExpect(jsonPath("$[0].tests.[0].area.id", is(2)))
                .andExpect(jsonPath("$[0].tests.[0].confidential", is(false)))
                .andExpect(jsonPath("$[0].tests.[0].testType", is(1)))
                .andExpect(jsonPath("$[1].tests.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[1].tests.[0].id", is(9)))
                .andExpect(jsonPath("$[1].tests.[0].area.id", is(2)))
                .andExpect(jsonPath("$[1].tests.[0].confidential", is(false)))
                .andExpect(jsonPath("$[1].tests.[0].testType", is(1)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_15_getFilterTestTestsConfidential() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setTestFilterType(3);
        filter.setTests(Arrays.asList(6, 7, 8, 9, 10));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].tests.[0].id", is(8)))
                .andExpect(jsonPath("$[0].tests.[0].area.id", is(3)))
                .andExpect(jsonPath("$[0].tests.[0].confidential", is(true)))
                .andExpect(jsonPath("$[0].tests.[0].testType", is(0)))
                .andExpect(jsonPath("$[1].tests.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[1].tests.[0].id", is(8)))
                .andExpect(jsonPath("$[1].tests.[0].area.id", is(3)))
                .andExpect(jsonPath("$[1].tests.[0].confidential", is(true)))
                .andExpect(jsonPath("$[1].tests.[0].testType", is(0)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_16_insertDemographicCiudad() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab62");
        TestScript.execTestUpdateScript("ALTER SEQUENCE lab62_lab62c1_seq RESTART WITH 1");

        Demographic demographic = new Demographic();
        demographic.setName("CIUDAD");
        demographic.setOrigin("H");
        demographic.setEncoded(true);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("1"));
        demographic.setStatistics(true);
        demographic.setLastOrder(true);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_17_insertDemographic() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setName("SALA");
        demographic.setOrigin("O");
        demographic.setEncoded(true);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("2"));
        demographic.setStatistics(true);
        demographic.setLastOrder(true);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_18_getFilterDemographicsHistory() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab63");

        TestScript.execTestUpdateScript("INSERT INTO lab63 VALUES (1, 'BOG', 'Bogotá', 1, '', '2017-09-12 17:20:08.523', 1, 1);");
        TestScript.execTestUpdateScript("INSERT INTO lab63 VALUES (2, 'MED', 'Medellín', 1, '', '2017-09-12 17:20:22.819', 1, 1);");
        TestScript.execTestUpdateScript("INSERT INTO lab63 VALUES (3, 'BAR', 'Barranquilla', 1, '', '2017-09-12 17:20:32.684', 1, 1);");

        TestScript.execTestUpdateScript("INSERT INTO lab63 VALUES (4, 'SE', 'Sala de Espera', 1, '', '2017-09-12 17:20:08.523', 2, 1);");
        TestScript.execTestUpdateScript("INSERT INTO lab63 VALUES (5, 'SU', 'Sala de Urgencias', 1, '', '2017-09-12 17:20:22.819', 2, 1);");

        TestScript.execTestUpdateScript("UPDATE lab22 SET lab_demo_2 = 4 WHERE lab22c1 = 201709080001");
        TestScript.execTestUpdateScript("UPDATE lab22 SET lab_demo_2 = 5 WHERE lab22c1 = 201709080002");
        TestScript.execTestUpdateScript("UPDATE lab21 SET lab_demo_1 = 1 WHERE lab21c1 = 1");
        TestScript.execTestUpdateScript("UPDATE lab21 SET lab_demo_1 = 2 WHERE lab21c1 = 2");
        TestScript.execTestUpdateScript("UPDATE lab21 SET lab_demo_1 = 3 WHERE lab21c1 = 3");

        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(1, Arrays.asList(1, 2)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$[0].createdDateShort", is(20170908)))
                .andExpect(jsonPath("$[0].patient.id", is(1)))
                .andExpect(jsonPath("$[0].patient.demographics.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].patient.demographics.[0].idDemographic", is(1)))
                .andExpect(jsonPath("$[0].patient.demographics.[0].codifiedId", is(1)))
                .andExpect(jsonPath("$[0].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_19_getFilterDemographicsHistoryNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(1, Arrays.asList(4)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_20_getFilterDemographicsOrder() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(2, Arrays.asList(5)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080002L)))
                .andExpect(jsonPath("$[0].createdDateShort", is(20170908)))
                .andExpect(jsonPath("$[0].patient.id", is(3)))
                .andExpect(jsonPath("$[0].patient.demographics.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].patient.demographics.[0].idDemographic", is(1)))
                .andExpect(jsonPath("$[0].patient.demographics.[0].codifiedId", is(3)))
                .andExpect(jsonPath("$[0].demographics.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].demographics.[0].idDemographic", is(2)))
                .andExpect(jsonPath("$[0].demographics.[0].codifiedId", is(5)))
                .andExpect(jsonPath("$[0].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_20_1_getFilterDemographicsOrder() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(2, Arrays.asList(5)));
        filter.getDemographics().add(new FilterDemographic(Constants.BRANCH, Arrays.asList(2)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080002L)))
                .andExpect(jsonPath("$[0].createdDateShort", is(20170908)))
                .andExpect(jsonPath("$[0].patient.id", is(3)))
                .andExpect(jsonPath("$[0].patient.demographics.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].patient.demographics.[0].idDemographic", is(1)))
                .andExpect(jsonPath("$[0].patient.demographics.[0].codifiedId", is(3)))
                .andExpect(jsonPath("$[0].demographics.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].demographics.[0].idDemographic", is(2)))
                .andExpect(jsonPath("$[0].demographics.[0].codifiedId", is(5)))
                .andExpect(jsonPath("$[0].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_20_2_getFilterDemographicsOrder() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(2, Arrays.asList(5)));
        filter.getDemographics().add(new FilterDemographic(Constants.BRANCH, Arrays.asList(1)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_21_getFilterDemographicsOrderNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(2, Arrays.asList(1)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_22_getFilterDemographicsStaticHistory() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(Constants.DOCUMENT_TYPE, Arrays.asList(2)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$[0].createdDateShort", is(20170908)))
                .andExpect(jsonPath("$[0].patient.id", is(1)))
                .andExpect(jsonPath("$[0].patient.documentType.id", is(2)))
                .andExpect(jsonPath("$[0].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_23_getFilterDemographicsHistoryStaticNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(Constants.DOCUMENT_TYPE, Arrays.asList(4)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_24_getFilterDemographicsOrderStatic() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(Constants.BRANCH, Arrays.asList(2)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080002L)))
                .andExpect(jsonPath("$[0].createdDateShort", is(20170908)))
                .andExpect(jsonPath("$[0].branch.id", is(2)))
                .andExpect(jsonPath("$[0].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_25_getFilterDemographicsOrderStaticNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.getDemographics().add(new FilterDemographic(Constants.BRANCH, Arrays.asList(3)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_26_getFilterPackageDescriptionTrue() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setPackageDescription(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$[0].createdDateShort", is(20170908)))
                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].tests.[0].id", is(9)))
                .andExpect(jsonPath("$[0].tests.[0].area.id", is(2)))
                .andExpect(jsonPath("$[0].tests.[0].confidential", is(false)))
                .andExpect(jsonPath("$[0].tests.[0].pack.id", is(10)))
                .andExpect(jsonPath("$[0].tests.[0].testType", is(1)))
                .andExpect(jsonPath("$[0].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_27_getFilterPackageDescriptionFalse() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

//    //MUESTRAS RECHAZADAS O EN RETOMA
//    @org.junit.Test
//    public void test_28_getRejectFilterNoContent() throws Exception
//    {
//        FilterRejectSample filter = new FilterRejectSample();
//        filter.setInit((long) 20170908);
//        filter.setEnd((long) 20170908);
//        filter.setBranch(1);
//        filter.setRejectSample(true);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String jsonContent = mapper.writeValueAsString(filter);
//
//        mockMvc.perform(patch("/api/listorders/rejectsample")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isNoContent());
//    }
//
//    @org.junit.Test
//    public void test_29_verifySample() throws Exception
//    {
//        //ACTUALIZAR ESTADO DE LOS EXAMENES ASOCIADOS A LA ORDEN
//        TestScript.execTestUpdateScript("UPDATE lab57 SET lab57c8 = 2 WHERE lab22c1 = 201709080001", null);
//
//        mockMvc.perform(post("/api/sampletrackings/verify/order/201709080001/sample/1").header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.messageCode", is(0)))
//                .andExpect(jsonPath("$.tests[*]", Matchers.hasSize(3)))
//                .andExpect(jsonPath("$.tests[1].result.branchVerify.id", is(1)))
//                .andExpect(jsonPath("$.tests[1].result.userVerify.id", is(-1)))
//                .andExpect(jsonPath("$.tests[1].result.dateVerify", Matchers.notNullValue()))
//                .andExpect(jsonPath("$.createdDate", Matchers.notNullValue()));
//    }
//
//    @org.junit.Test
//    public void test_30_rejectSample() throws Exception
//    {
//        //ACTUALIZAR ESTADO DE LOS EXAMENES ASOCIADOS A LA ORDEN
//        TestScript.execTestUpdateScript("DELETE FROM lab144 WHERE lab22c1 = 201709080001", null);
//
//        Reason reason = new Reason();
//        reason.setComment("La muestra se encuentra en mal estado.");
//        reason.getMotive().setId(1);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String jsonContent = mapper.writeValueAsString(reason);
//
//        mockMvc.perform(post("/api/sampletrackings/reject/order/201709080001/sample/1")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.messageCode", is(0)))
//                .andExpect(jsonPath("$.tests[*]", Matchers.hasSize(3)))
//                .andExpect(jsonPath("$.tests[1].result.branchVerify.id", is(1)))
//                .andExpect(jsonPath("$.tests[1].result.userVerify.id", is(-1)))
//                .andExpect(jsonPath("$.tests[1].result.dateVerify", Matchers.notNullValue()))
//                .andExpect(jsonPath("$.tests[1].result.branchReject.id", is(1)))
//                .andExpect(jsonPath("$.tests[1].result.userReject.id", is(-1)))
//                .andExpect(jsonPath("$.tests[1].result.dateReject", Matchers.notNullValue()))
//                .andExpect(jsonPath("$.createdDate", Matchers.notNullValue()));
//    }
//
//    @org.junit.Test
//    public void test_31_getRejectFilter() throws Exception
//    {
//        FilterRejectSample filter = new FilterRejectSample();
//        filter.setInit((long) 20170908);
//        filter.setEnd((long) 20170908);
//        filter.setBranch(1);
//        filter.setRejectSample(true);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String jsonContent = mapper.writeValueAsString(filter);
//
//        mockMvc.perform(patch("/api/listorders/rejectsample")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
//                .andExpect(jsonPath("$[0].createdDateShort", is(20170908)))
//                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(3)))
//                .andExpect(jsonPath("$[0].createdDate", Matchers.notNullValue()));
//    }
//    @org.junit.Test
//    public void test_32_getRetomaFilter() throws Exception
//    {
//        //ACTUALIZAR ESTADO DE LOS EXAMENES ASOCIADOS A LA ORDEN
//        TestScript.execTestUpdateScript("UPDATE lab57 SET lab57c8 = 5 WHERE lab22c1 = 201709080001", null);
//
//        FilterRejectSample filter = new FilterRejectSample();
//        filter.setInit((long) 20170908);
//        filter.setEnd((long) 20170908);
//        filter.setBranch(1);
//        filter.setRejectSample(false);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String jsonContent = mapper.writeValueAsString(filter);
//
//        mockMvc.perform(patch("/api/listorders/rejectsample")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
//                .andExpect(jsonPath("$[0].createdDateShort", is(20170908)))
//                .andExpect(jsonPath("$[0].tests.[*]", Matchers.hasSize(4)))
//                .andExpect(jsonPath("$[0].createdDate", Matchers.notNullValue()));
//    }
    //LABORATORIO DE REFERENCIA
    @org.junit.Test
    public void test_33_getLaboratoryFilterNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setLaboratories(Arrays.asList(3));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/laboratory")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_34_getLaboratoryFilter() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setLaboratories(Arrays.asList(1, 2));
        filter.setLaboratory(1);
        filter.setRemission(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/laboratory")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_35_getLaboratoryFilterHL7NoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setLaboratories(Arrays.asList(3));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/laboratory/hl7")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_36_getLaboratoryHL7Filter() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setLaboratories(Arrays.asList(1, 2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/laboratory/hl7")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }
    
    @org.junit.Test
    public void test_37_listPendingExams() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/filter/listPendingExams")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isNoContent());
    }
    
    
     @org.junit.Test
    public void test_38_getBranch() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());

        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20170915);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/testcheckbybranch")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }
    
    @org.junit.Test
    public void test_39_getFilterRemission() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());

        Filter filter = new Filter();
        filter.setRangeType(0);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20170915);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/listorders/remission")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }
}
