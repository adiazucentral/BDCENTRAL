package controllers.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.common.FilterOrder;
import net.cltech.enterprisent.domain.operation.common.FilterOrderHeader;
import net.cltech.enterprisent.domain.operation.common.PrintOrder;
import net.cltech.enterprisent.domain.operation.common.PrintOrderInfo;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeSample;
import net.cltech.enterprisent.domain.operation.reports.ReportBarcode;
import net.cltech.enterprisent.domain.operation.reports.SendPrint;
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
 * Prueba unitaria sobre el api rest /api/reports
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
public class ReportTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ReportTest()
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
        filter.setTypeReport(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
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
        filter.setTypeReport(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
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
        filter.setTypeReport(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
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
        filter.setTypeReport(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_05_getFilterPrevious() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_06_getFilterReprintFinalReportNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(true);
        filter.setTypeReport(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

//    @org.junit.Test
//    public void test_07_getFilterReprintFinalReport() throws Exception
//    {
//        TestScript.execTestUpdateScript("UPDATE lab57 SET lab57c1='BIEN', lab57c8 = 4 WHERE lab22c1 IN (201709080001, 201709080002) AND lab39c1 IN (7)");
//        Filter filter = new Filter();
//        filter.setRangeType(1);
//        filter.setInit(201709080001L);
//        filter.setEnd(201709080010L);
//        filter.setReprintFinalReport(true);
//        filter.setTypeReport(0);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String jsonContent = mapper.writeValueAsString(filter);
//
//        mockMvc.perform(patch("/api/reports")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
//                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
//                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
//                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
//                .andExpect(jsonPath("$[0].resultTest", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].resultTest[0].testId", is(7)))
//                .andExpect(jsonPath("$[0].resultTest[0].result", is("BIEN")))
//                .andExpect(jsonPath("$[1].resultTest[0].testId", is(7)))
//                .andExpect(jsonPath("$[1].resultTest[0].result", is("BIEN")))
//                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
//    }
    @org.junit.Test
    public void test_08_getFilterCopyNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_09_getFilterFinalReport() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.execTestUpdateScript("UPDATE lab57 SET lab57c1='BIEN', lab57c8 = 4 WHERE lab22c1 IN (201709080001, 201709080002) AND lab39c1 IN (7)");

        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[0].resultTest", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].resultTest[0].testId", is(7)))
                .andExpect(jsonPath("$[0].resultTest[0].result", is("BIEN")))
                .andExpect(jsonPath("$[1].resultTest[0].testId", is(7)))
                .andExpect(jsonPath("$[1].resultTest[0].result", is("BIEN")))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

//    @org.junit.Test
//    public void test_10_getFilterFinalReportNoContent() throws Exception
//    {
//        Filter filter = new Filter();
//        filter.setRangeType(1);
//        filter.setInit(201709080001L);
//        filter.setEnd(201709080010L);
//        filter.setReprintFinalReport(false);
//        filter.setTypeReport(1);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String jsonContent = mapper.writeValueAsString(filter);
//
//        mockMvc.perform(patch("/api/reports")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isNoContent());
//    }
//    @org.junit.Test
//    public void test_11_getFilterCopy() throws Exception
//    {
//        Filter filter = new Filter();
//        filter.setRangeType(1);
//        filter.setInit(201709080001L);
//        filter.setEnd(201709080010L);
//        filter.setReprintFinalReport(false);
//        filter.setTypeReport(3);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        String jsonContent = mapper.writeValueAsString(filter);
//
//        mockMvc.perform(patch("/api/reports")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
//                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
//                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
//                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
//                .andExpect(jsonPath("$[0].resultTest", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].resultTest[0].testId", is(7)))
//                .andExpect(jsonPath("$[0].resultTest[0].result", is("BIEN")))
//                .andExpect(jsonPath("$[1].resultTest[0].testId", is(7)))
//                .andExpect(jsonPath("$[1].resultTest[0].result", is("BIEN")))
//                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
//    }
    @org.junit.Test
    public void test_12_getFilterPreviousTwo() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/reports")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].orderNumber", containsInAnyOrder(201709080001L, 201709080002L)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDateShort", containsInAnyOrder(20170908, 20170908)))
                .andExpect(jsonPath("$[*].createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_13_ordersBarcode() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(post("/api/reports/ordersbarcode")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_14_reportsByOrder() throws Exception
    {
        ReportBarcode report = new ReportBarcode();
        report.setCount(0);
        report.setDemographics(new ArrayList<>(0));
        report.setInit(201709080001L);
        report.setEnd(201709080010L);
        report.setRangeType(1);
        List<BarcodeSample> samples = new ArrayList<>();
        samples.add(new BarcodeSample(1, 1));
        report.setSamples(samples);

        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        report.setOrdersprint(Tools.jsonList(TestTools.getResponseString(mockMvc.perform(post("/api/reports/ordersbarcode")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))), Order.class));
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(report);

        mockMvc.perform(post("/api/reports")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_15_listSerials_noContend() throws Exception
    {
        mockMvc.perform(get("/api/reports/serials")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_16_createSerial() throws Exception
    {
        mockMvc.perform(post("/api/reports/serial")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_17_listSerials() throws Exception
    {
        mockMvc.perform(get("/api/reports/serials")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_18_orderHeader() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);
        String jsonRespons = TestTools.getResponseString(mockMvc.perform(patch("/api/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent)));

        mockMvc.perform(post("/api/reports/orderheader")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_19_orderBody() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);
        Order order = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(
                post("/api/reports/orderheader")
                        .header("Authorization", token)
                        .contentType(TestTools.APPLICATION_JSON_UTF8)
                        .content(jsonContent))), Order.class).get(0);

        FilterOrder filterOrder = new FilterOrder();
        order.setTests(new ArrayList<>(0));
        order.setDemographics(new ArrayList<>(0));
        filterOrder.setOrder(order);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(filterOrder);

        mockMvc.perform(post("/api/reports/orderbody")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$.createdDateShort", is(20170908)))
                .andExpect(jsonPath("$.createdDateShort", is(20170908)))
                .andExpect(jsonPath("$.createdDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_20_sendPrinting() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);
        Order order = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(
                post("/api/reports/orderheader")
                        .header("Authorization", token)
                        .contentType(TestTools.APPLICATION_JSON_UTF8)
                        .content(jsonContent))), Order.class).get(0);

        FilterOrder filterOrder = new FilterOrder();
        order.setTests(new ArrayList<>(0));
        order.setDemographics(new ArrayList<>(0));
        filterOrder.setOrder(order);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(filterOrder);

        order = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(post("/api/reports/orderbody")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))), Order.class);
    }

    @org.junit.Test
    public void test_21_printingByType() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);
        Order order = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(
                post("/api/reports/orderheader")
                        .header("Authorization", token)
                        .contentType(TestTools.APPLICATION_JSON_UTF8)
                        .content(jsonContent))), Order.class).get(0);

        FilterOrderHeader filterOrderHeader = new FilterOrderHeader();
        filterOrderHeader.setRangeType(1);
        filterOrderHeader.setInit(201709080001L);
        filterOrderHeader.setEnd(201709080010L);
        filterOrderHeader.setReprintFinalReport(false);
        filterOrderHeader.setTypeReport(1);
        PrintOrder printOrder = new PrintOrder();
        PrintOrderInfo printOrderInfo = new PrintOrderInfo();
        printOrderInfo.setOrder(order);
        printOrder.setListOrders(Arrays.asList(printOrderInfo));
        filterOrderHeader.setPrintOrder(Arrays.asList(printOrder));
        filterOrderHeader.setPrintingType(1);
        filterOrderHeader.setCompleteOrder(1);

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(filterOrderHeader);

        mockMvc.perform(post("/api/reports/printingbytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().is2xxSuccessful());
    }

    @org.junit.Test
    public void test_22_printingByTypeBarcode() throws Exception
    {

        FilterOrderHeader filterOrderHeader = new FilterOrderHeader();
        filterOrderHeader.setRangeType(1);
        filterOrderHeader.setInit(201709080001L);
        filterOrderHeader.setEnd(201709080010L);
        filterOrderHeader.setReprintFinalReport(false);
        filterOrderHeader.setTypeReport(1);
        filterOrderHeader.setPrintingType(2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filterOrderHeader);

        mockMvc.perform(post("/api/reports/printingbytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_23_sendPrinting() throws Exception
    {
        SendPrint sendPrint = new SendPrint();
        sendPrint.setJson("{}");
        sendPrint.setSerial("1");
        sendPrint.setType(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sendPrint);

        mockMvc.perform(post("/api/reports/printws")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().is5xxServerError());
    }

    @org.junit.Test
    public void test_24_printingReport() throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);
        Order order = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(
                post("/api/reports/orderheader")
                        .header("Authorization", token)
                        .contentType(TestTools.APPLICATION_JSON_UTF8)
                        .content(jsonContent))), Order.class).get(0);

        FilterOrderHeader filterOrderHeader = new FilterOrderHeader();
        filterOrderHeader.setRangeType(1);
        filterOrderHeader.setInit(201709080001L);
        filterOrderHeader.setEnd(201709080010L);
        filterOrderHeader.setReprintFinalReport(false);
        filterOrderHeader.setTypeReport(1);
        filterOrderHeader.setCompleteOrder(1);

        PrintOrder printOrder = new PrintOrder();
        PrintOrderInfo printOrderInfo = new PrintOrderInfo();
        printOrderInfo.setOrder(order);
        printOrder.setListOrders(Arrays.asList(printOrderInfo));
        filterOrderHeader.setPrintOrder(Arrays.asList(printOrder));
        filterOrderHeader.setPrintingType(1);

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(filterOrderHeader);

        mockMvc.perform(post("/api/reports/printingreport")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber", is(201709080001L)));
    }

    @org.junit.Test
    public void test_25_reportsByOrder() throws Exception
    {
        ReportBarcode report = new ReportBarcode();
        report.setCount(0);
        report.setDemographics(new ArrayList<>(0));
        report.setInit(201709080001L);
        report.setEnd(201709080010L);
        report.setRangeType(1);
        List<BarcodeSample> samples = new ArrayList<>();
        samples.add(new BarcodeSample(1, 1));
        report.setSamples(samples);

        Filter filter = new Filter();
        filter.setRangeType(1);
        filter.setInit(201709080001L);
        filter.setEnd(201709080010L);
        filter.setReprintFinalReport(false);
        filter.setTypeReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        report.setOrdersprint(Tools.jsonList(TestTools.getResponseString(mockMvc.perform(post("/api/reports/ordersbarcode")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))), Order.class));
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(report);

        mockMvc.perform(post("/api/reports/ordersbarcode")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
//    @org.junit.Test
//    public void test_26_getBase64() throws Exception
//    {
//        mockMvc.perform(get("/api/reports/getBase64/idOrder/201709080001"))
//                .andExpect(status().isOk());
//    }
}
