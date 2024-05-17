package controllers.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.skl.RequestSampleDestination;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.tracking.SampleTracking;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTake;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTakeTracking;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.service.impl.enterprisent.operation.tracking.SampleTrackingServiceEnterpriseNT;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import org.junit.Assert;
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
 * Prueba unitaria sobre el api rest /api/sampletrackings
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/09/2017
 * @see Creacion
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes
        =
        {
            MongoTestAppContext.class,
            TestAppContext.class
        })
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleTrackingTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;
    List<Question> questions;

    public SampleTrackingTest()
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
    public void test_01_verifyFailedNoMaking() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = 'True' WHERE lab98c1='TomaMuestra'");
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab159 RESTART IDENTITY;");
        mockMvc.perform(post("/api/sampletrackings/verify/order/201709080001/sample/1").header("Authorization", token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("4")));
    }

    @org.junit.Test
    public void test_04_takeSample() throws Exception
    {
        TestScript.execTestUpdateScript("INSERT INTO lab159 (lab22c1, lab24c1, lab159c1, lab04c1, lab159c2, lab53c1, lab05c1) VALUES (201709080001, 1, 2, 1, '2017-09-11 15:54:54.863', null, 1)");

        mockMvc.perform(get("/api/sampletrackings/take/order/201709080001/sample/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_05_takeFailedVerified() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/take/order/201709080001/sample/1").header("Authorization", token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("1")));
    }

    @org.junit.Test
    public void test_06_retakeSample() throws Exception
    {
        Reason reason = new Reason();
        reason.setComment("La muestra se encuentra en mal estado.");
        reason.getMotive().setId(1);
        reason.getTests().add(new Test(6));
        reason.getTests().add(new Test(7));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(post("/api/sampletrackings/retake/order/201709080001/sample/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_07_postponementFailedRejected() throws Exception
    {
        Reason reason = new Reason();
        reason.setComment("La muestra se encuentra en mal estado.");
        reason.getMotive().setId(1);
        reason.getTests().add(new Test(6));
        reason.getTests().add(new Test(7));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(post("/api/sampletrackings/retake/order/201709080001/sample/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("0|tests")));
    }

    @org.junit.Test
    public void test_08_rejectFailedOrder() throws Exception
    {
        Reason reason = new Reason();
        reason.setComment("La muestra se encuentra en mal estado.");
        reason.getMotive().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(post("/api/sampletrackings/reject/order/20170908/sample/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.errorFields[0]", is("0|order")));
    }

    @org.junit.Test
    public void test_09_rejectFailedSample() throws Exception
    {
        Reason reason = new Reason();
        reason.setComment("La muestra se encuentra en mal estado.");
        reason.getMotive().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(post("/api/sampletrackings/reject/order/201709080001/sample/3")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("1|sample")));
    }

    @org.junit.Test
    public void test_10_verifySample() throws Exception
    {
        TestScript.execTestUpdateScript("INSERT INTO lab159 VALUES (201709080002,1,3,1,'2020-04-28 14:33:27.057',null,1, null)");
        mockMvc.perform(post("/api/sampletrackings/verify/order/201709080002/sample/1")
                .header("Authorization", token).contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_11_verifyFailedVerified() throws Exception
    {
        mockMvc.perform(post("/api/sampletrackings/verify/order/201709080100/sample/1")
                .header("Authorization", token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(2)));
    }

    @org.junit.Test
    public void test_12_rejectSample() throws Exception
    {
        Reason reason = new Reason();
        reason.setComment("La muestra se encuentra en mal estado.");
        reason.getMotive().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(post("/api/sampletrackings/reject/order/201709080001/sample/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_13_rejectFailedRejected() throws Exception
    {
        Reason reason = new Reason();
        reason.setComment("La muestra se encuentra en mal estado.");
        reason.getMotive().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(post("/api/sampletrackings/reject/order/201709080001/sample/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("2")));
    }

    //TOMA DE MUESTRA EN VERIFICACION
    @org.junit.Test
    public void test_15_takeAndVerifySample() throws Exception
    {
        //ELIMINAR REGISTROS
        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = 'False' WHERE lab98c1='TomaMuestra'");
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab57 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab144 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab159 RESTART IDENTITY;", null);
        //ESTADO ACTUAL DE LA MUESTRA
        TestScript.execTestUpdateScript("INSERT INTO lab159 (lab22c1, lab24c1, lab159c1, lab04c1, lab159c2, lab53c1, lab05c1) VALUES (201709080001, 1, 2, 1, '2017-09-11 15:54:54.863', null, 1)");
        //RESULTADOS
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080001, 8, NULL, NULL, NULL, '2017-09-13 11:33:13.552855', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080001, 6, NULL, NULL, NULL, '2017-09-13 11:34:10.914229', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 9, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080001, 9, NULL, NULL, NULL, '2017-09-13 11:34:43.352058', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080001, 7, NULL, NULL, NULL, '2017-09-13 11:34:12.439341', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 9, 10, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080001, 10, NULL, NULL, NULL, '2017-09-13 11:42:29.666415', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080002, 8, NULL, NULL, NULL, '2017-09-13 11:44:18.923104', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080002, 6, NULL, NULL, NULL, '2017-09-13 11:44:20.116507', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 9, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080002, 7, NULL, NULL, NULL, '2017-09-13 11:44:21.712427', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 9, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 VALUES (201709080002, 9, NULL, NULL, NULL, '2017-09-13 11:52:11.687382', NULL, NULL, NULL, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1)", null);

        mockMvc.perform(post("/api/sampletrackings/verify/order/201709080001/sample/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_18_getRouteDestinationNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab42 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab52 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab53 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab87 RESTART IDENTITY;", null);
        mockMvc.perform(get("/api/sampletrackings/verify/destination/order/201709080001/sample/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_19_getRouteDestination() throws Exception
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

        mockMvc.perform(get("/api/sampletrackings/verify/destination/order/201709080001/sample/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destinationRoutes[*]", Matchers.hasSize(5)))
                .andExpect(jsonPath("$.destinationRoutes[0].verify", is(false)))
                .andExpect(jsonPath("$.destinationRoutes[0].id", is(1)))
                .andExpect(jsonPath("$.destinationRoutes[0].destination.id", is(1)))
                .andExpect(jsonPath("$.destinationRoutes[1].verify", is(false)))
                .andExpect(jsonPath("$.destinationRoutes[1].id", is(2)))
                .andExpect(jsonPath("$.destinationRoutes[1].destination.id", is(2)))
                .andExpect(jsonPath("$.destinationRoutes[2].verify", is(false)))
                .andExpect(jsonPath("$.destinationRoutes[2].id", is(3)))
                .andExpect(jsonPath("$.destinationRoutes[2].destination.id", is(4)))
                .andExpect(jsonPath("$.destinationRoutes[3].verify", is(false)))
                .andExpect(jsonPath("$.destinationRoutes[3].id", is(4)))
                .andExpect(jsonPath("$.destinationRoutes[3].destination.id", is(6)))
                .andExpect(jsonPath("$.destinationRoutes[4].verify", is(false)))
                .andExpect(jsonPath("$.destinationRoutes[4].id", is(5)))
                .andExpect(jsonPath("$.destinationRoutes[4].destination.id", is(7)));
    }

    @org.junit.Test
    public void test_20_verifyDestinationFailed() throws Exception
    {
        VerifyDestination verify = new VerifyDestination();
        verify.setOrder(201709080001L);
        verify.setDestination(1);
        verify.setSample("10");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(verify);

        mockMvc.perform(post("/api/sampletrackings/verify/destination")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @org.junit.Test
    public void test_21_verifyDestination1() throws Exception
    {
        VerifyDestination verify = new VerifyDestination();
        verify.setOrder(201709080001L);
        verify.setDestination(1);
        verify.setSample("1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(verify);

        mockMvc.perform(post("/api/sampletrackings/verify/destination")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_22_verifyDestination2() throws Exception
    {
        VerifyDestination verify = new VerifyDestination();
        verify.setOrder(201709080001L);
        verify.setDestination(2);
        verify.setSample("1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(verify);

        mockMvc.perform(post("/api/sampletrackings/verify/destination")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_23_getDestinationVerify() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/verify/destination/order/201709080001/sample/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.destinationRoutes[*]", Matchers.hasSize(5)))
                .andExpect(jsonPath("$.destinationRoutes[0].verify", is(true)))
                .andExpect(jsonPath("$.destinationRoutes[0].id", is(1)))
                .andExpect(jsonPath("$.destinationRoutes[0].destination.id", is(1)))
                .andExpect(jsonPath("$.destinationRoutes[1].verify", is(true)))
                .andExpect(jsonPath("$.destinationRoutes[1].id", is(2)))
                .andExpect(jsonPath("$.destinationRoutes[1].destination.id", is(2)))
                .andExpect(jsonPath("$.destinationRoutes[2].verify", is(false)))
                .andExpect(jsonPath("$.destinationRoutes[2].id", is(3)))
                .andExpect(jsonPath("$.destinationRoutes[2].destination.id", is(4)))
                .andExpect(jsonPath("$.destinationRoutes[3].verify", is(false)))
                .andExpect(jsonPath("$.destinationRoutes[3].id", is(4)))
                .andExpect(jsonPath("$.destinationRoutes[3].destination.id", is(6)))
                .andExpect(jsonPath("$.destinationRoutes[4].verify", is(false)))
                .andExpect(jsonPath("$.destinationRoutes[4].id", is(5)))
                .andExpect(jsonPath("$.destinationRoutes[4].destination.id", is(7)));
    }

    @org.junit.Test
    public void test_24_getInterviewNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab44 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab70 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab90 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab91 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab92 RESTART IDENTITY;", null);
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab66 RESTART IDENTITY;", null);

        mockMvc.perform(get("/api/sampletrackings/interview/order/201709080001").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_25_getInterview() throws Exception
    {
        /*ENTREVISTA*/
        TestScript.execTestUpdateScript("INSERT INTO lab44 VALUES (3, 'ORDEN', 1, 0, 1, 2, '2017-10-18 15:31:25.715');", null);
        TestScript.execTestUpdateScript("INSERT INTO lab44 VALUES (1, 'GENERAL', 2, 0, 1, 2, '2017-10-18 15:31:34.934');", null);
        TestScript.execTestUpdateScript("INSERT INTO lab44 VALUES (2, 'EXAMENES', 3, 0, 1, 2, '2017-10-18 15:31:39.666');", null);
        /*ENTREVISTA - TIPO DE ENTREVISTA*/
        TestScript.execTestUpdateScript("INSERT INTO lab66 VALUES (3, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab66 VALUES (3, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab66 VALUES (1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab66 VALUES (2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab66 VALUES (2, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab66 VALUES (2, 3);", null);
        /*PREGUNTAS*/
        TestScript.execTestUpdateScript("INSERT INTO lab70 VALUES (2, 'Fumado', '¿Ha Fumado En Las Ultimas 24 Horas?', 0, 5, '2017-10-12 15:12:47.69', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab70 VALUES (3, 'Enfermedades', '¿Tiene alguna enfermedad crónica?', 0, 5, '2017-10-12 15:13:47.771', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab70 VALUES (4, 'Enfermades 2', '¿Cuales Enfermedades Crónicas Tiene?', 1, 1, '2017-10-12 15:14:16.825', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab70 VALUES (1, 'Tomado', '¿Ha Tomado En Las Ultimas 24 Horas?', 0, 5, '2017-10-18 15:33:14.294', 2, 1);", null);
        /*RESPUESTAS*/
        TestScript.execTestUpdateScript("INSERT INTO lab90 VALUES (1, 'SI', '2017-10-12 15:12:19.583', 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab90 VALUES (2, 'NO', '2017-10-12 15:12:22.84', 2, 1);", null);
        /*PREGUNTAS - RESPUESTAS*/
        TestScript.execTestUpdateScript("INSERT INTO lab91 VALUES (2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab91 VALUES (2, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab91 VALUES (3, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab91 VALUES (3, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab91 VALUES (1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab91 VALUES (1, 2);", null);
        /*ENTREVISTA - PREGUNTAS*/
        TestScript.execTestUpdateScript("INSERT INTO lab92 VALUES (3, 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab92 VALUES (3, 3, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab92 VALUES (3, 4, 3);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab92 VALUES (1, 3, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab92 VALUES (1, 4, 2);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab92 VALUES (2, 2, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab92 VALUES (2, 1, 2);", null);

        mockMvc.perform(get("/api/sampletrackings/interview/order/201709080001")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].name", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].question", Matchers.notNullValue()))
                .andExpect(jsonPath("$[0].open", is(false)))
                .andExpect(jsonPath("$[0].answers[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].name", Matchers.notNullValue()))
                .andExpect(jsonPath("$[1].question", Matchers.notNullValue()))
                .andExpect(jsonPath("$[1].open", is(false)))
                .andExpect(jsonPath("$[1].answers[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[2].name", Matchers.notNullValue()))
                .andExpect(jsonPath("$[2].question", Matchers.notNullValue()))
                .andExpect(jsonPath("$[2].open", is(true)))
                .andExpect(jsonPath("$[2].answers[*]", Matchers.hasSize(0)));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        questions = mapper.readValue(TestTools.getResponseString(mockMvc.perform(get("/api/sampletrackings/interview/order/201709080001").header("Authorization", token))), mapper.getTypeFactory().constructCollectionType(List.class, Question.class));

        questions.get(0).getAnswers().get(0).setSelected(true);
        questions.get(1).setInterviewAnswer("...");
//        questions.get(2).getAnswers().get(1).setSelected(true);

        String jsonContent = mapper.writeValueAsString(questions);

        mockMvc.perform(post("/api/sampletrackings/interview/order/201709080001")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_quality_sample_ok() throws Exception
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime taken = now.minusMinutes(10);

        SampleTracking tracking = new SampleTracking();
        tracking.setState(LISEnum.ResultSampleState.COLLECTED.getValue());
        tracking.setDate(Date.from(taken.atZone(ZoneId.systemDefault()).toInstant()));

        Sample sample = new Sample(1);
        sample.setQualityTime(20l);
        sample.setQualityPercentage(60);
        sample.setSampleTrackings(Arrays.asList(tracking));

        SampleTrackingServiceEnterpriseNT service = new SampleTrackingServiceEnterpriseNT();
        int flag = service.getSampleQuality(sample);
        Assert.assertEquals("ok", 1, flag);

        taken = LocalDateTime.now().minusMinutes(15);
        sample.getSampleTrackings().get(0).setDate(Date.from(taken.atZone(ZoneId.systemDefault()).toInstant()));
        flag = service.getSampleQuality(sample);
        Assert.assertEquals("alarm", 2, flag);

        taken = LocalDateTime.now().minusMinutes(21);
        sample.getSampleTrackings().get(0).setDate(Date.from(taken.atZone(ZoneId.systemDefault()).toInstant()));
        flag = service.getSampleQuality(sample);
        Assert.assertEquals("fail", 3, flag);
//
        sample.setSampleTrackings(new ArrayList<>());
        sample.setQualityPercentage(0);
        flag = service.getSampleQuality(sample);
        Assert.assertEquals("alarm", 2, flag);

        sample.setSampleTrackings(new ArrayList<>());
        sample.setQualityPercentage(60);
        flag = service.getSampleQuality(sample);
        Assert.assertEquals("not taken yet", 1, flag);

    }

    @org.junit.Test
    public void test_05_trackingAlarmInterview() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/alarminterview/201709080001").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_06_sampletaketest() throws Exception
    {

        TestSampleTakeTracking obj = new TestSampleTakeTracking(8, 2, 5, null);
        List<TestSampleTakeTracking> list = new ArrayList<>();
        list.add(obj);
        TestSampleTake obj1 = new TestSampleTake(Long.parseLong("201709080001"), "1", list);

        ObjectMapper mapper = new ObjectMapper();

        String jsonContent = mapper.writeValueAsString(obj1);

        mockMvc.perform(post("/api/sampletrackings/sampletaketest")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));

    }

    @org.junit.Test
    public void test_07_listsampletakebyorder() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/listsampletakebyorder/order/201709080001/sample/1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_29_updateStateToCheck() throws Exception
    {
        RequestSampleDestination requestSampleDestination = new RequestSampleDestination();
        requestSampleDestination.setIdBranch(1);
        requestSampleDestination.setIdOrder(201709080001L);
        requestSampleDestination.setIdSample(1);
        requestSampleDestination.setDestinations("1,2,3,4,5");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(requestSampleDestination);

        mockMvc.perform(put("/api/sampletrackings/updateStateToCheck")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_31_thisOrderExists() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/orderExists/idOrder/201709080001")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_32_isSampleCheck() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/isSampleCheck/idOrder/201709080001/idRoute/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_33_isSampleCheckSimple() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/isSampleCheckSimple/idOrder/201709080001/sampleId/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_34_getInterviewAnswerDestination() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/interview/answer/destination/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_35_getInterviewDestination() throws Exception
    {
        /*ENTREVISTA*/
        TestScript.execTestUpdateScript("INSERT INTO lab44 VALUES (4, 'DESTINO', 4, 0, 1, 2, '2020-05-06 15:31:39.666');", null);
        /*ENTREVISTA*/
        TestScript.execTestUpdateScript("INSERT INTO lab23 VALUES (201709080001, 1, 1,'', '2020-05-06 15:31:39.666', 1, 1, 3, 1, 1);", null);
        /*ENTREVISTA - TIPO DE ENTREVISTA*/
        TestScript.execTestUpdateScript("INSERT INTO lab66 VALUES (4, 1);", null);
        /*PREGUNTAS*/
        TestScript.execTestUpdateScript("INSERT INTO lab92 VALUES (4, 1, 1);", null);

        mockMvc.perform(get("/api/sampletrackings/interview/order/201709080001/sample/1/destination/3/branch/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_36_post_InterviewDestination() throws Exception
    {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        questions = mapper.readValue(TestTools.getResponseString(mockMvc.perform(get("/api/sampletrackings/interview/order/201709080001").header("Authorization", token))), mapper.getTypeFactory().constructCollectionType(List.class, Question.class));

        questions.get(0).getAnswers().get(0).setSelected(true);
        questions.get(1).setInterviewAnswer("...");
//        questions.get(2).getAnswers().get(1).setSelected(true);

        String jsonContent = mapper.writeValueAsString(questions);

        mockMvc.perform(post("/api/sampletrackings/interview/order/201709080001/sample/1/destination/3/branch/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @org.junit.Test
    public void test_37_getSamplesByTemperatureAndDate() throws Exception
    {
        mockMvc.perform(get("/api/sampletrackings/getSamplesByTemperatureAndDate/initialDate/20200416/endDate/20200430/idSample/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
}
