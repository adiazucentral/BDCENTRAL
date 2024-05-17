/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.MicrobiologyDestination;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.microbiology.Task;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.filters.MicrobiologyFilter;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobialDetection;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyTask;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import net.cltech.enterprisent.tools.DateTools;
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
 * Prueba unitaria sobre el api rest /api/microbiology
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/09/2017
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
public class MicrobiologyTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;
    MicrobiologyGrowth growth;

    public MicrobiologyTest()
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
    public void test_01_getOrderMicrobiologyError() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.execTestUpdateScript("DELETE FROM lab42", null);
        TestScript.execTestUpdateScript("DELETE FROM lab52", null);
        TestScript.execTestUpdateScript("DELETE FROM lab53", null);
        TestScript.execTestUpdateScript("DELETE FROM lab87", null);
        mockMvc.perform(get("/api/microbiology/growth/order/201709080001/sample/1").header("Authorization", token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("0|The sample has not been configured")));
    }

    @org.junit.Test
    public void test_02_getOrderMicrobiology() throws Exception
    {
        /*DESTINOS*/
        TestScript.execTestUpdateScript("INSERT INTO lab53 VALUES (1, '01', 'INICIAL', '', 44, '#FFFFFF', '2017-10-12 15:07:40.984', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab53 VALUES (2, '02', 'INTERNO 1', '', 45, '#b01818', '2017-10-13 08:47:11.84', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab53 VALUES (3, '03', 'INTERNO 2', '', 45, '#09167c', '2017-10-13 08:47:24.442', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab53 VALUES (4, '04', 'EXTERNO 1', '', 46, '#15ae86', '2017-10-13 08:47:40.05', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab53 VALUES (5, '05', 'EXTERNO 2', '', 46, '#319d2f', '2017-10-13 08:47:57.938', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab53 VALUES (6, '06', 'CONTROL', '', 47, '#e8e216', '2017-10-13 08:48:24.482', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab53 VALUES (7, '07', 'FINAL 1', '', 48, '#ff0000', '2017-10-13 08:48:37.137', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab53 VALUES (8, '08', 'FINAL 2', '', 48, '#f45600', '2017-10-13 08:48:57.792', 2, 1);", null);
        /*ASIGNACION DE DESTINOS*/
        TestScript.execTestUpdateScript("INSERT INTO lab52 VALUES (1, 1, 1, 1, '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab52 VALUES (2, 2, 1, 1, '2017-10-13 08:50:07.289', 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab52 VALUES (3, 1, 1, 2, '2017-10-13 09:57:09.086', 1, 1);", null);
        /*RUTA DE DESTINOS*/
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (1, 1, 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (2, 2, 1, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (3, 4, 1, 3);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (4, 6, 1, 4);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (5, 7, 1, 5);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (6, 1, 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (7, 2, 2, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (8, 3, 2, 3);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (9, 7, 2, 4);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (10, 8, 2, 5);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (11, 1, 3, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (12, 2, 3, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab42 VALUES (13, 7, 3, 3);", null);
        /*RUTA - EXAMENES*/
        TestScript.execTestUpdateScript("INSERT INTO lab87 VALUES (2, 6);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab87 VALUES (3, 7);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab87 VALUES (7, 6);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab87 VALUES (8, 7);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab87 VALUES (12, 8);", null);
        /*MEDIO DE CULTIVO*/
        TestScript.execTestUpdateScript("DELETE FROM lab155", null);
        TestScript.execTestUpdateScript("INSERT INTO lab155 VALUES (1, '1', 'MEDIO CULTIVO 1', 1, '2017-10-12 15:07:40.984', 1);", null);
        /*RELACION EXAMEN MEDIO DE CULTIVO*/
        TestScript.execTestUpdateScript("DELETE FROM lab164", null);
        TestScript.execTestUpdateScript("INSERT INTO lab164 VALUES (8, 1, 1);", null);
        /*SITIO ANATOMICO*/
        TestScript.execTestUpdateScript("DELETE FROM lab158", null);
        TestScript.execTestUpdateScript("INSERT INTO lab158 VALUES (1, 'PIERNA', 'PI', '2017-10-12 15:07:40.984', 1, 1);", null);
        /*METODO DE RECOLECCION*/
        TestScript.execTestUpdateScript("DELETE FROM lab201", null);
        TestScript.execTestUpdateScript("INSERT INTO lab201 VALUES (1, 'TOPO', '2017-10-12 15:07:40.984', 1, 1);", null);
        /*PROCEDIMIENTO*/
        TestScript.execTestUpdateScript("DELETE FROM lab156;", null);
        TestScript.execTestUpdateScript("INSERT INTO lab156 VALUES (1, '1', 'PROCEDIMIENTO 1', '2017-10-13 08:49:39.94', 1, 1);", null);
        /*TAREAS*/
        TestScript.execTestUpdateScript("DELETE FROM lab169;", null);
        TestScript.execTestUpdateScript("INSERT INTO lab169 VALUES (1, 'ANTIBIOGRAMA MANUAL', '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab169 VALUES (2, 'CATALASA', '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("UPDATE lab39 SET lab39c27 = 0 WHERE lab39c1 = 8;", null);

        mockMvc.perform(get("/api/microbiology/growth/order/201709080001/sample/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tests[0].id", is(8)))
                .andExpect(jsonPath("$.tests[0].name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$.sample.id", is(1)))
                .andExpect(jsonPath("$.sample.name", is("SANGRE")))
                .andExpect(jsonPath("$.order.orderNumber", is(201709080001L)));
    }

    @org.junit.Test
    public void test_03_listOrderPendindVerification() throws Exception
    {
        TestTools.getResponseString(mockMvc.perform(post("/api/sampletrackings/verify/order/201709080001/sample/1").header("Authorization", token)));
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setInit(201709080001l);
        filter.setEnd(201709089999l);
        filter.setRangeType(Filter.RANGE_TYPE_ORDER);

        String jsonContent = Tools.jsonObject(filter);

        mockMvc.perform(patch("/api/microbiology/verification/pending")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                //                .andExpect(jsonPath("$[0].resultTest", hasSize(1)));
                .andExpect(jsonPath("$[?(@.orderNumber == 201709080001)].resultTest", hasSize(1)));
    }

    @org.junit.Test
    public void test_04_verifyOrderMicrobiology() throws Exception
    {

        growth = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/microbiology/growth/order/201709080001/sample/1").header("Authorization", token))), MicrobiologyGrowth.class);

        //Se selecciona el examen por defecto que va a manejar el medio de cultivo
        growth.setTest(growth.getTests().get(0));

        growth.getAnatomicalSite().setId(1);
        growth.getCollectionMethod().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(growth);

        mockMvc.perform(post("/api/microbiology/growth")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_05_growthOrderMicrobiology() throws Exception
    {

        growth = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/microbiology/growth/order/201709080001/sample/1").header("Authorization", token))), MicrobiologyGrowth.class);

        growth.setMediaCultures(new ArrayList<>());
        growth.getMediaCultures().add(new MediaCulture());
        growth.getMediaCultures().get(0).setId(1);
        growth.getProcedures().add(new Procedure());
        growth.getProcedures().get(0).setId(1);
        growth.getCollectionMethod().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(growth);

        mockMvc.perform(put("/api/microbiology/growth")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("1"));
    }

    @org.junit.Test
    public void test_06_getOrderMicrobiologyGrowth() throws Exception
    {
        mockMvc.perform(get("/api/microbiology/growth/order/201709080001/sample/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.test.id", is(8)))
                .andExpect(jsonPath("$.test.name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$.sample.id", is(1)))
                .andExpect(jsonPath("$.sample.name", is("SANGRE")))
                .andExpect(jsonPath("$.dateGrowth", notNullValue()))
                .andExpect(jsonPath("$.userGrowth.id", is(2)))
                .andExpect(jsonPath("$.order.orderNumber", is(201709080001L)));
    }

    @org.junit.Test
    public void test_07_microbialDetection() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab77;", null);
        TestScript.execTestUpdateScript("INSERT INTO lab77 VALUES (1, '001', 'GP', 'GRANPOSITIVO', '2017-10-13 08:49:39.94', 1, 1, 1);", null);
        TestScript.execTestUpdateScript("DELETE FROM lab76;", null);
        TestScript.execTestUpdateScript("INSERT INTO lab76 VALUES (1, 'MICROORGANISMO 1', '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab76 VALUES (2, 'MICROORGANISMO 2', '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab76 VALUES (3, 'MICROORGANISMO 3', '2017-10-13 08:49:39.94', 1, 1);", null);
        TestScript.execTestUpdateScript("DELETE FROM lab206;", null);
        TestScript.execTestUpdateScript("INSERT INTO lab206 VALUES (1, 1, null);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab206 VALUES (1, 2, null);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab206 VALUES (1, 3, null);", null);
        TestScript.execTestUpdateScript("DELETE FROM lab79;", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (1,'Amikacina','2018-01-30 08:23:03.48',1,1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (2,'Acinetobacter','2018-02-06 10:14:24.93',1,1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (3,'Amoxacilina/Acido Clavulánico','2018-02-06 10:15:37.993',3,1);", null);
        TestScript.execTestUpdateScript("DELETE FROM lab78;", null);
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
    public void test_08_getMicrobialDetection() throws Exception
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
    public void test_09_resultMicrobiologyNoContent() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170101);
        filter.setEnd((long) 20170101);
        filter.setTest(8);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/result")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_10_resultMicrobiology() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));
        filter.setTest(8);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/result")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_11_resultOrderMicrobiologyNoContent() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170101);
        filter.setEnd((long) 20170101);
        filter.setTest(8);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/result/order")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_12_resultOrderMicrobiology() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));
        filter.setTest(8);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/result/order")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_13_insertAntibioticsMicrobiology() throws Exception
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
    public void test_14_updateDeleteAntibioticsMicrobiology() throws Exception
    {
        MicrobialDetection microbialDetection = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/microbiology/microbialdetection/order/201709080001/test/8").header("Authorization", token))), MicrobialDetection.class);
        List<ResultMicrobiology> results = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(get("/api/microbiology/antiobiotics/microbialdetection/" + microbialDetection.getMicroorganisms().get(0).getIdMicrobialDetection()).header("Authorization", token))), ResultMicrobiology.class);
        results.get(0).setCmi("100");
        results.get(0).setInterpretationCMI("INTERMEDIO");
        results.get(0).setSelected(true);
        results.get(1).setSelected(false);
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
                .andExpect(content().string("1"));
    }

    @org.junit.Test
    public void test_15_insertTaskMicrobiologyMediaCulture() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab207;", null);
        TestScript.execTestUpdateScript("INSERT INTO lab207 VALUES (1, '1', 'DESTINO 1', 1, '2017-10-13 08:49:39.94', 1, 1);", null);

        MicrobiologyTask microbiologyTask = new MicrobiologyTask();
        microbiologyTask.setOrder(201709080001L);
        microbiologyTask.setIdTest(8);
        microbiologyTask.setIdRecord(1);
        microbiologyTask.setComment("Comentario 1");

        MicrobiologyDestination destination = new MicrobiologyDestination();
        destination.setId(1);
        microbiologyTask.setDestination(destination);

        microbiologyTask.getTasks().add(new Task(1));
        microbiologyTask.getTasks().add(new Task(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(microbiologyTask);

        mockMvc.perform(post("/api/microbiology/mediaculture/tasks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_16_insertTaskMicrobiologyProcedure() throws Exception
    {
        MicrobiologyTask microbiologyTask = new MicrobiologyTask();
        microbiologyTask.setOrder(201709080001L);
        microbiologyTask.setIdTest(8);
        microbiologyTask.setIdRecord(1);
        microbiologyTask.setComment("Comentario 1");

        MicrobiologyDestination destination = new MicrobiologyDestination();
        destination.setId(1);
        microbiologyTask.setDestination(destination);

        microbiologyTask.getTasks().add(new Task(1));
        microbiologyTask.getTasks().add(new Task(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(microbiologyTask);

        mockMvc.perform(post("/api/microbiology/procedure/tasks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @org.junit.Test
    public void test_17_getOrderMicrobiologyTasks() throws Exception
    {
        mockMvc.perform(get("/api/microbiology/tasks/order/201709080001/sample/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.test.id", is(8)))
                .andExpect(jsonPath("$.test.name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$.sample.id", is(1)))
                .andExpect(jsonPath("$.sample.name", is("SANGRE")))
                .andExpect(jsonPath("$.dateGrowth", notNullValue()))
                .andExpect(jsonPath("$.order.orderNumber", is(201709080001L)))
                .andExpect(jsonPath("$.mediaCultures[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_18_listOrderMicrobiologyTasksComment() throws Exception
    {
        MicrobiologyTask microbiologyTask = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/microbiology/tasks/order/201709080001/sample/1").header("Authorization", token))), MicrobiologyGrowth.class).getMediaCultures().get(0).getTasks().get(0);
        microbiologyTask.setComment("Comentario 2");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(microbiologyTask);

        mockMvc.perform(put("/api/microbiology/tasks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));

        mockMvc.perform(get("/api/microbiology/tracking/tasks/comment/id/" + microbiologyTask.getId() + "/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)));
    }

    @org.junit.Test
    public void test_19_listTrackingMicrobiologyTasks() throws Exception
    {
        mockMvc.perform(get("/api/microbiology/tracking/tasks/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[*].order", containsInAnyOrder(201709080001L, 201709080001L, 201709080001L)))
                .andExpect(jsonPath("$[*].idTest", containsInAnyOrder(8, 8, 8)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 1, 2)));
    }

    @org.junit.Test
    public void test_20_resultReporter() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/report/tasks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)));
    }

    @org.junit.Test
    public void test_21_resultReporterNoContent() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/report/tasks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_22_resultRestart() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/restart/tasks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_23_resultReporterNoContent() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) 20170908);
        filter.setEnd((long) 20170908);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/report/tasks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_24_resultMicrobiologyFilterReport() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));
        filter.setTest(8);
        filter.setReport(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/result")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));;
    }

    @org.junit.Test
    public void test_25_resultMicrobiologyFilterReportNoContent() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));
        filter.setReport(2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/result")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_26_resultMicrobiologyFilterSample() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));
        filter.setCodeSample("1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/result")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_27_resultMicrobiologyFilterReportSampleNoContent() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_DATE);
        filter.setInit((long) DateTools.dateToNumber(new Date()));
        filter.setEnd((long) DateTools.dateToNumber(new Date()));
        filter.setCodeSample("2");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/result")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_28_microbiologyPendingState() throws Exception
    {
        MicrobiologyFilter filter = new MicrobiologyFilter();
        filter.setRangeType(Filter.RANGE_TYPE_ORDER);
        filter.setInit(201709080001l);
        filter.setEnd(201709089999l);
        filter.setPendingStates(Arrays.asList(1, 2, 3));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/microbiology/count/pendingstate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("total", is(1)))
                .andExpect(jsonPath("result", is(0)));
    }

    @org.junit.Test
    public void test_29_getResultTemplateNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab51", null);
        TestScript.execTestUpdateScript("DELETE FROM lab58", null);
        TestScript.execTestUpdateScript("DELETE FROM lab146", null);

        mockMvc.perform(get("/api/microbiology/templates/order/201709080001/test/8").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_30_insertResultTemplate() throws Exception
    {
        /*PLANTILLA DE RESULTADOS*/
        TestScript.execTestUpdateScript("INSERT INTO lab51 VALUES (1, 'RECUENTO Y CULTIVO', 'MUESTRA CONTAMINADA', 1, 8);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab51 VALUES (2, 'INFORME', '', 2, 8);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab51 VALUES (3, 'TIEMPO DE INCUBACIÓN', '', 3, 8);", null);
        /*OPCIONES - PLANTILLA DE RESULTADOS*/
        TestScript.execTestUpdateScript("INSERT INTO lab146 VALUES ('Indice bacilar', 1, 0, 1);", null);

        TestScript.execTestUpdateScript("INSERT INTO lab146 VALUES ('Preliminar', 1, 0, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab146 VALUES ('Final', 2, 0, 2);", null);

        TestScript.execTestUpdateScript("INSERT INTO lab146 VALUES ('24 horas  Negativo para bacterias y hongos  Método: Bact/Alert  Registro No.', 1, 0, 3);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab146 VALUES ('48 horas  Negativo para bacterias y hongos  Método: Bact/Alert  Registro No.', 2, 0, 3);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab146 VALUES ('72 horas  Negativo para bacterias y hongos  Método: Bact/Alert  Registro No.', 3, 0, 3);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab146 VALUES ('7 días  Negativo para bacterias y hongos  Método: Bact/Alert  Registro No.', 4, 0, 3);", null);

        mockMvc.perform(get("/api/microbiology/templates/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].option", is("RECUENTO Y CULTIVO")))
                .andExpect(jsonPath("$[0].comment", is("MUESTRA CONTAMINADA")))
                .andExpect(jsonPath("$[0].results.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].sort", is(1)))
                .andExpect(jsonPath("$[1].option", is("INFORME")))
                .andExpect(jsonPath("$[1].comment", is("")))
                .andExpect(jsonPath("$[1].results.[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].sort", is(2)))
                .andExpect(jsonPath("$[2].option", is("TIEMPO DE INCUBACIÓN")))
                .andExpect(jsonPath("$[2].comment", is("")))
                .andExpect(jsonPath("$[2].results.[*]", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[2].sort", is(3)));

        List<OptionTemplate> templates;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        templates = mapper.readValue(TestTools.getResponseString(mockMvc.perform(get("/api/microbiology/templates/order/201709080001/test/8").header("Authorization", token))), mapper.getTypeFactory().constructCollectionType(List.class, OptionTemplate.class));

        templates.get(0).setResult("Indice bacilar REQUERIDO");
        templates.get(1).setResult("Preliminar Orden 1");
        templates.get(2).setResult("24 horas  Negativo para bacterias y hongos  Método: Bact/Alert  Registro No. 1");

        String jsonContent = mapper.writeValueAsString(templates);

        mockMvc.perform(post("/api/microbiology/templates/order/201709080001/test/8")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("3"));
    }

    @org.junit.Test
    public void test_31_getResultTemplateWithResult() throws Exception
    {
        mockMvc.perform(get("/api/microbiology/templates/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].option", is("RECUENTO Y CULTIVO")))
                .andExpect(jsonPath("$[0].comment", is("MUESTRA CONTAMINADA")))
                .andExpect(jsonPath("$[0].result", is("Indice bacilar REQUERIDO")))
                .andExpect(jsonPath("$[0].results.[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].sort", is(1)))
                .andExpect(jsonPath("$[1].option", is("INFORME")))
                .andExpect(jsonPath("$[1].comment", is("")))
                .andExpect(jsonPath("$[1].result", is("Preliminar Orden 1")))
                .andExpect(jsonPath("$[1].results.[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].sort", is(2)))
                .andExpect(jsonPath("$[2].option", is("TIEMPO DE INCUBACIÓN")))
                .andExpect(jsonPath("$[2].comment", is("")))
                .andExpect(jsonPath("$[2].result", is("24 horas  Negativo para bacterias y hongos  Método: Bact/Alert  Registro No. 1")))
                .andExpect(jsonPath("$[2].results.[*]", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[2].sort", is(3)));
    }

    @org.junit.Test
    public void test_32_getTestsMicrobiologySample() throws Exception
    {
        mockMvc.perform(get("/api/microbiology/tests/sample/1/order/201709080001")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(8)))
                .andExpect(jsonPath("$[0].code", is("003")))
                .andExpect(jsonPath("$[0].name", is("CONFIDENCIAL")))
                .andExpect(jsonPath("$[0].abbr", is("CP")));
    }

}
