package controllers.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.skl.InformedConsentAnswer;
import net.cltech.enterprisent.domain.integration.skl.OrderInformedConsent;
import net.cltech.enterprisent.domain.integration.skl.RequestSampleDestination;
import net.cltech.enterprisent.domain.integration.skl.SklOrderAnswer;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest General Controller SKl
 *
 * @version 1.0.0
 * @author enavas
 * @since 16/04/2017
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
public class GeneralSklControllerTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public GeneralSklControllerTest()
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
        user.setId(-1);
        user.setBranch(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @Test
    public void test_01_getAllContainer() throws Exception
    {

        mockMvc.perform(get("/api/integration/skl/containers").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_02_getRecipientOrderByTestList() throws Exception
    {

        mockMvc.perform(get("/api/integration/skl/containers/getRecipientOrderByTestList/listTests/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_03_comment_order() throws Exception
    {

        mockMvc.perform(get("/api/integration/skl/comments/order/201805033079")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_04_comment_patient() throws Exception
    {

        mockMvc.perform(get("/api/integration/skl/comments/patient/4").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_05_getOrderQuestions() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/getOrderQuestions/idOrder/201709080001")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_getAvailableAnswer() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/getAvailableAnswer/idQuestion/1")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_getAnswers() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/getAnswers/idOrder/201709080001")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getGeneralInterview() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/getGeneralInterview")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getSampleDestinations() throws Exception
    {
        RequestSampleDestination requestSampleDestination = new RequestSampleDestination();
        requestSampleDestination.setIdBranch(1);
        requestSampleDestination.setState(1);
        requestSampleDestination.setIdOrder(201709080001L);
        requestSampleDestination.setIdSample(1);
        requestSampleDestination.setOrderType("4");

        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(requestSampleDestination);

        mockMvc.perform(post("/api/integration/skl/getSampleDestinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_getRecipients() throws Exception
    {
        TestScript.createInitialOrders();
        mockMvc.perform(get("/api/integration/skl/containers/getRecipients/idOrder/201709080001/pendingToTake/0")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_getRecipientOrderByTestList() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/containers/getRecipientOrderByTestList/listTests/1,2,3,4")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_12_getOrderQuestionId() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/getOrderQuestionId/idOrder/201709080001/idQuestion/2")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_13_updateAnswer() throws Exception
    {
        SklOrderAnswer answer = new SklOrderAnswer();

        answer.setOrder(201709080001L);
        answer.setIdQuestion(1);
        answer.setIdSelectAnswer(1);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(answer);

        mockMvc.perform(put("/api/integration/skl/updateQuestion")
                .header(token, "Authorization")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void test_14_insertAnswer() throws Exception
    {

        TestScript.execTestUpdateScript("INSERT INTO lab70 VALUES (33, '¿Se encuentra en ayunas', '¿Se encuentra en ayunas', 1, 1, '2019-06-25 08:54:59.573', 8, 1)");
        SklOrderAnswer answer = new SklOrderAnswer();
        answer.setOrder(2020);
        answer.setIdPatient(1);
        answer.setIdQuestion(33);
        answer.setIdSelectAnswer(1);
        answer.setTextAnswer("prueba");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(answer);

        mockMvc.perform(post("/api/integration/skl/questions/answer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());

    }

    @Test
    public void test_15_getDestinationinitialBySample() throws Exception
    {
        TestScript.execTestUpdateScript("INSERT INTO lab52 VALUES (27, 0, 1, 4, '2019-06-25 08:54:59.573', 1, 1)");

        mockMvc.perform(get("/api/integration/skl/destinationinitial/sample/4").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_16_checkSample() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/checkSample/idSample/1/idDestination/1/idOrder/201709080001")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_17_getOrdersToTake() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/getOrdersToTake/date/20170908")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_18_geListInformedConsent() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/interviewinformedconsent")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_19_getListInformedConsentWithOrder() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/informedconsent/order/201809030008")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_20_newConsent() throws Exception
    {
        OrderInformedConsent orderInformedConsent = new OrderInformedConsent();
        InformedConsentAnswer informedConsentAnswer = new InformedConsentAnswer();
        informedConsentAnswer.setOrder(1234566789);
        informedConsentAnswer.setIdTest(213456);

        orderInformedConsent.setAnswer(informedConsentAnswer);
        orderInformedConsent.setDocument("Esto es un pruba");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(orderInformedConsent);

        mockMvc.perform(post("/api/integration/skl/newconsent")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_21_getConsentBase64() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/consentbase64/201809030008")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void test_22_getOrdersConsentBase64() throws Exception
    {
        mockMvc.perform(get("/consentbase64/history/77/documenttype/2")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void test_22_getListTestsSample() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/tests/order/202006020092/sample/2")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }
    @Test
    public void test_23_getPatientTestPending() throws Exception
    {
        mockMvc.perform(get("/api/integration/skl/patientTestPending/document/2063174")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

}
