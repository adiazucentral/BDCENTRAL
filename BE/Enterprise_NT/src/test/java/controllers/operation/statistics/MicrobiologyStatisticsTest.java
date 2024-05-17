/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.Arrays;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.filters.StatisticFilter;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobialDetection;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
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
 * Prueba unitaria sobre el api rest /api/worklist Hojas de trabajo
 *
 * @version 1.0.0
 * @author cmartin
 * @since 03/04/2018
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
public class MicrobiologyStatisticsTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private String token;

    public MicrobiologyStatisticsTest()
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
    public void test_01_statisticMicrobiologyNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab76;", null);
        TestScript.execTestUpdateScript("DELETE FROM lab77;", null);
        TestScript.execTestUpdateScript("DELETE FROM lab78;", null);
        TestScript.execTestUpdateScript("DELETE FROM lab79;", null);
        TestScript.execTestUpdateScript("DELETE FROM lab204;", null);
        TestScript.execTestUpdateScript("DELETE FROM lab205;", null);
        TestScript.execTestUpdateScript("DELETE FROM lab206;", null);

        TestScript.createInitialOrders();

        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170000);
        filter.setEnd((long) 20171005);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_02_microbialDetection() throws Exception
    {

        TestScript.execTestUpdateScript("INSERT INTO lab77 VALUES (1, '001', 'GP', 'GRANPOSITIVO', '2017-10-13 08:49:39.94', 1, 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab76 VALUES (1, 'MICROORGANISMO 1', '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab76 VALUES (2, 'MICROORGANISMO 2', '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab76 VALUES (3, 'MICROORGANISMO 3', '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab206 VALUES (1, 1, null);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab206 VALUES (1, 2, null);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab206 VALUES (1, 3, null);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (1,'Amikacina','2018-01-30 08:23:03.48',1,1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (2,'Acinetobacter','2018-02-06 10:14:24.93',1,1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (3,'Amoxacilina/Acido Clavulánico','2018-02-06 10:15:37.993',3,1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab78 VALUES (1,1,1,1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab78 VALUES (1,2,1,1);", null);

        MicrobialDetection microbialDetection = new MicrobialDetection();
        microbialDetection.setOrder(201709080001L);
        microbialDetection.setTest(8);

        microbialDetection.getMicroorganisms().add(new Microorganism(1));
        microbialDetection.getMicroorganisms().add(new Microorganism(2));
        microbialDetection.getMicroorganisms().add(new Microorganism(3));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(microbialDetection);

        mockMvc.perform(post("/api/microbiology/microbialdetection")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("3"));
    }

    @org.junit.Test
    public void test_03_getMicrobialDetection() throws Exception
    {
        mockMvc.perform(get("/api/microbiology/microbialdetection/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order", is(201709080001L)))
                .andExpect(jsonPath("$.test", is(8)))
                .andExpect(jsonPath("$.microorganisms[*]", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.microorganisms[0].id", is(1)))
                .andExpect(jsonPath("$.microorganisms[1].id", is(2)))
                .andExpect(jsonPath("$.microorganisms[2].id", is(3)));
    }

    @org.junit.Test
    public void test_04_insertAntibioticsMicrobiology() throws Exception
    {
        MicrobialDetection microbialDetection = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/microbiology/microbialdetection/order/201709080001/test/8").header("Authorization", token))), MicrobialDetection.class);
        List<ResultMicrobiology> results = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(get("/api/microbiology/antiobiotics/microbialdetection/" + microbialDetection.getMicroorganisms().get(0).getIdMicrobialDetection()).header("Authorization", token))), ResultMicrobiology.class);
        results.get(0).setCmi("10");
        results.get(0).setInterpretationCMI("RESISTENTE");
        results.get(0).setSelected(true);

        results.get(1).setCmi("5");
        results.get(1).setInterpretationCMI("SENSIBLE");
        results.get(1).setSelected(true);
        microbialDetection.getMicroorganisms().get(0).setResultsMicrobiology(results);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(microbialDetection.getMicroorganisms().get(0));

        mockMvc.perform(post("/api/microbiology/antiobiotics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_05_statisticMicrobiologysContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$[0].patient.id", is(1)))
                .andExpect(jsonPath("$[0].results[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].id", is(8)))
                .andExpect(jsonPath("$[0].results[0].code", is("003")))
                .andExpect(jsonPath("$[0].results[0].name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$[0].results[0].microorganisms[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].id", is(1)))
                //                .andExpect(jsonPath("$[0].results[0].microorganisms[0].idMicrobialDetection", is(4)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*].idAntibiotic", containsInAnyOrder(1, 2)));
    }

    @org.junit.Test
    public void test_06_statisticMicrobiologysTestsContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);
        filter.setTests(Arrays.asList(8));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$[0].patient.id", is(1)))
                .andExpect(jsonPath("$[0].results[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].id", is(8)))
                .andExpect(jsonPath("$[0].results[0].code", is("003")))
                .andExpect(jsonPath("$[0].results[0].name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$[0].results[0].microorganisms[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].id", is(1)))
                //                .andExpect(jsonPath("$[0].results[0].microorganisms[0].idMicrobialDetection", is(4)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*].idAntibiotic", containsInAnyOrder(1, 2)));
    }

    @org.junit.Test
    public void test_07_statisticMicrobiologysTestsNoContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);
        filter.setTests(Arrays.asList(1));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_08_statisticMicrobiologysSamplesContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);
        filter.setSamples(Arrays.asList(1));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$[0].patient.id", is(1)))
                .andExpect(jsonPath("$[0].results[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].id", is(8)))
                .andExpect(jsonPath("$[0].results[0].code", is("003")))
                .andExpect(jsonPath("$[0].results[0].name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$[0].results[0].microorganisms[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].id", is(1)))
                //                .andExpect(jsonPath("$[0].results[0].microorganisms[0].idMicrobialDetection", is(4)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*].idAntibiotic", containsInAnyOrder(1, 2)));
    }

    @org.junit.Test
    public void test_09_statisticMicrobiologysSamplesNoContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);
        filter.setSamples(Arrays.asList(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_10_statisticMicrobiologysMicroorganismsContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);
        filter.setMicroorganisms(Arrays.asList(1));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$[0].patient.id", is(1)))
                .andExpect(jsonPath("$[0].results[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].id", is(8)))
                .andExpect(jsonPath("$[0].results[0].code", is("003")))
                .andExpect(jsonPath("$[0].results[0].name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$[0].results[0].microorganisms[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].id", is(1)))
                //                .andExpect(jsonPath("$[0].results[0].microorganisms[0].idMicrobialDetection", is(4)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*].idAntibiotic", containsInAnyOrder(1, 2)));
    }

    @org.junit.Test
    public void test_11_statisticMicrobiologysAntibioticsNoContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);
        filter.setSamples(Arrays.asList(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_12_statisticMicrobiologysAntibioticsContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);
        filter.setAntibiotics(Arrays.asList(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$[0].patient.id", is(1)))
                .andExpect(jsonPath("$[0].results[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].id", is(8)))
                .andExpect(jsonPath("$[0].results[0].code", is("003")))
                .andExpect(jsonPath("$[0].results[0].name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$[0].results[0].microorganisms[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].id", is(1)))
                //                .andExpect(jsonPath("$[0].results[0].microorganisms[0].idMicrobialDetection", is(4)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].results[0].microorganisms[0].resultsMicrobiology[*].idAntibiotic", containsInAnyOrder(2)));
    }

    @org.junit.Test
    public void test_09_statisticMicrobiologysAntibioticsNoContent() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);
        filter.setSamples(Arrays.asList(3));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/statistics/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_13_statisticMicrobiologyWhonet() throws Exception
    {
        StatisticFilter filter = new StatisticFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20171005);

        String jsonContent = Tools.jsonObject(filter);

        mockMvc.perform(patch("/api/statistics/microbiology/whonet")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)));
    }

}
