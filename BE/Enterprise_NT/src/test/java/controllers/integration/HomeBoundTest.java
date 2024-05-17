package controllers.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.homebound.AccountHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Appointment;
import net.cltech.enterprisent.domain.integration.homebound.BillingTestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.BranchHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DemographicHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DocumentTypeHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.GenderHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.PatientHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.PhysicianHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.QuestionHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.RateHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.SampleHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Shift;
import net.cltech.enterprisent.domain.integration.homebound.UserHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Zone;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/test
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 20/02/2020
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
public class HomeBoundTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public HomeBoundTest()
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
        user.setId(1);
        user.setBranch(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_patchTestHomBound() throws Exception
    {
        List<Long> listOrders = new ArrayList<>();
        listOrders.add(202004270001L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(listOrders);

        mockMvc.perform(patch("/api/homebound/listorders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_02_listDocumentTypes() throws Exception
    {
        mockMvc.perform(get("/api/documentTypes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_03_listAccounts() throws Exception
    {
        mockMvc.perform(get("/api/clients")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_04_listGenresLanguage() throws Exception
    {
        mockMvc.perform(get("/api/sex/es")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_05_listPaymentType() throws Exception
    {
        mockMvc.perform(get("/api/paymentTypes/es")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_06_getPatientHomeBound() throws Exception
    {
        mockMvc.perform(get("/api/homebound/patients/filter/0/1/es")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_07_listOrderTypes() throws Exception
    {
        mockMvc.perform(get("/api/ordertypes/es")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_08_getPatientByIdHomeBound() throws Exception
    {
        mockMvc.perform(get("/api/homebound/patients/1/es")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_09_createPatientHomeBound() throws Exception
    {
        PatientHomeBound patient = new PatientHomeBound();
        DocumentTypeHomeBound document = new DocumentTypeHomeBound();
        GenderHomeBound gender = new GenderHomeBound();
        List<DemographicHomeBound> listDemos = new ArrayList<>();

        patient.setPatientId("9");
        document.setId(2);
        patient.setDocumentType(document);
        patient.setLastName("Avisa");
        patient.setSurName("");
        patient.setName1("Yulius");
        patient.setName2("Malfoy");
        gender.setId(7);
        patient.setSex(gender);
        patient.setBirthday(new Date());
        patient.setEmail("correoFalseeee@hotmail.com");
        patient.setDemographics(listDemos);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(patient);

        mockMvc.perform(post("/api/homebound/patients")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_10_updatePatientHomeBound() throws Exception
    {
        PatientHomeBound patient = new PatientHomeBound();
        DocumentTypeHomeBound document = new DocumentTypeHomeBound();
        GenderHomeBound gender = new GenderHomeBound();
        List<DemographicHomeBound> listDemos = new ArrayList<>();

        patient.setId(1);
        patient.setPatientId("9");
        document.setId(2);
        patient.setDocumentType(document);
        patient.setLastName("Avisa");
        patient.setSurName("Santos");
        patient.setName1("Yulius");
        patient.setName2("Malfoy");
        gender.setId(7);
        patient.setSex(gender);
        patient.setBirthday(new Date());
        patient.setEmail("correoFalseeee@hotmail.com");
        patient.setDemographics(listDemos);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(patient);

        mockMvc.perform(put("/api/homebound/patients")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_11_getRatetByIdHomeBound() throws Exception
    {
        mockMvc.perform(get("/api/homebound/homebound/tests/price/6/2046")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_12_updateSampleTake() throws Exception
    {
        Appointment appointment = new Appointment();
        SampleHomeBound sample = new SampleHomeBound();
        List<SampleHomeBound> samples = new ArrayList<>();

        sample.setId(1);
        samples.add(sample);

        appointment.setOrderNumber(201709080001L);
        appointment.setSamples(samples);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(appointment);

        mockMvc.perform(put("/api/homebound/sampletake")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_13_updateInterview() throws Exception
    {
        Appointment appointment = new Appointment();
        QuestionHomeBound question = new QuestionHomeBound();
        List<QuestionHomeBound> questions = new ArrayList<>();

        question.setId(83);
        question.setName("5599");
        question.setControl(5);
        question.setQuestion("HA INGERIDO LICOR");
        question.setAnswers(new ArrayList<>());
        questions.add(question);

        appointment.setOrderNumber(201709080001L);
        appointment.setInterview(questions);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(appointment);

        mockMvc.perform(put("/api/homebound/interview")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_14_createAppointment() throws Exception
    {

        Appointment appointment = new Appointment();
        appointment.setId(232);
        appointment.setDate(2018112456);

        Zone zone = new Zone();
        zone.setId(1);
        zone.setCode("01");
        zone.setName("suba");
        zone.setState(false);
        zone.setSelected(false);
        appointment.setZone(zone);
        UserHomeBound phlebotomist = new UserHomeBound();
        phlebotomist.setId(25);
        phlebotomist.setName("BACNH");
        phlebotomist.setAdministrator(true);
        phlebotomist.setAdministrative(true);
        phlebotomist.setPhlebotomist(true);
        phlebotomist.setAdministrativeHospitable(false);
        phlebotomist.setPhlebotomistHospitable(false);
        appointment.setPhlebotomist(phlebotomist);
        Shift shift = new Shift();
        shift.setId(1);
        shift.setName("Zona1");
        shift.setInit(640);
        shift.setEnd(1100);
        List<Integer> days = Arrays.asList(1, 2, 3, 4);
        shift.setDays(days);
        shift.setState(false);
        shift.setSelected(false);
        PatientHomeBound patient = new PatientHomeBound();
        patient.setId(1);
        List<DemographicHomeBound> demographics = new ArrayList<>();
        DemographicHomeBound demographic = new DemographicHomeBound();
        demographic.setId(29);
        demographic.setCoded(true);
        demographic.setValue("");
        demographic.setItem(0);
        demographics.add(demographic);
        patient.setDemographics(demographics);
        appointment.setPatient(patient);
        appointment.setAddress("KR 48c #58-69 sur");
        appointment.setPhone("3123565510");
        PhysicianHomeBound physician = new PhysicianHomeBound();
        physician.setId(2);
        physician.setCode("2");
        physician.setName("DR. GUSTAVO GARCIA");
        appointment.setPhysician(physician);
        AccountHomeBound account = new AccountHomeBound();
        account.setId(2);
        account.setCode("EPS00101");
        account.setName("ALIANSALUD EPS - PLAN");
        appointment.setAccount(account);
        RateHomeBound rate = new RateHomeBound();
        rate.setId(1);
        rate.setCode("1");
        rate.setName("Tarifa 3");
        appointment.setRate(rate);
        appointment.setType("A");
        BranchHomeBound branch = new BranchHomeBound();
        branch.setId(4);
        branch.setCode("11");
        branch.setName("Centro Médico Occidente");
        appointment.setBranch(branch);
        appointment.setIdService(1);
        List<BillingTestHomeBound> billingTests = new ArrayList<>();
        BillingTestHomeBound billingTest1 = new BillingTestHomeBound();
        BillingTestHomeBound billingTest2 = new BillingTestHomeBound();
        billingTest1.setId(57);
        billingTest1.setCode("1001");
        billingTest1.setAbbr("GLI");
        billingTest1.setName("GLICEMIA BASAL");
        billingTest1.setRate(rate);
        billingTests.add(billingTest1);
        billingTest2.setId(57);
        billingTest2.setCode("1005");
        billingTest2.setAbbr("gli2h");
        billingTest2.setName("GLICEMIA 2 HORAS");
        billingTest2.setRate(rate);
        billingTests.add(billingTest2);
        appointment.setBillingTests(billingTests);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(appointment);

        mockMvc.perform(post("/api/appointments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_15_updateAppointment() throws Exception
    {
        Appointment appointment = new Appointment();
        appointment.setId(232);
        appointment.setDate(2018112456);

        Zone zone = new Zone();
        zone.setId(1);
        zone.setCode("01");
        zone.setName("suba");
        zone.setState(false);
        zone.setSelected(false);
        appointment.setZone(zone);
        UserHomeBound phlebotomist = new UserHomeBound();
        phlebotomist.setId(25);
        phlebotomist.setName("BACNH");
        phlebotomist.setAdministrator(true);
        phlebotomist.setAdministrative(true);
        phlebotomist.setPhlebotomist(true);
        phlebotomist.setAdministrativeHospitable(false);
        phlebotomist.setPhlebotomistHospitable(false);
        appointment.setPhlebotomist(phlebotomist);
        Shift shift = new Shift();
        shift.setId(1);
        shift.setName("Zona1");
        shift.setInit(640);
        shift.setEnd(1100);
        List<Integer> days = Arrays.asList(1, 2, 3, 4);
        shift.setDays(days);
        shift.setState(false);
        shift.setSelected(false);
        PatientHomeBound patient = new PatientHomeBound();
        patient.setId(1);
        List<DemographicHomeBound> demographics = new ArrayList<>();
        DemographicHomeBound demographic = new DemographicHomeBound();
        demographic.setId(29);
        demographic.setCoded(true);
        demographic.setValue("");
        demographic.setItem(0);
        patient.setDemographics(demographics);
        appointment.setPatient(patient);
        appointment.setAddress("KR 48c #58-69 sur");
        appointment.setPhone("3123565510");
        PhysicianHomeBound physician = new PhysicianHomeBound();
        physician.setId(2);
        physician.setCode("2");
        physician.setName("DR. GUSTAVO GARCIA");
        appointment.setPhysician(physician);
        AccountHomeBound account = new AccountHomeBound();
        account.setId(2);
        account.setCode("EPS00101");
        account.setName("ALIANSALUD EPS - PLAN");
        appointment.setAccount(account);
        RateHomeBound rate = new RateHomeBound();
        rate.setId(1);
        rate.setCode("1");
        rate.setName("Tarifa 3");
        appointment.setRate(rate);
        appointment.setType("A");
        BranchHomeBound branch = new BranchHomeBound();
        branch.setId(4);
        branch.setCode("11");
        branch.setName("Centro Médico Occidente");
        appointment.setBranch(branch);
        appointment.setIdService(1);
        List<BillingTestHomeBound> billingTests = new ArrayList<>();
        BillingTestHomeBound billingTest1 = new BillingTestHomeBound();
        BillingTestHomeBound billingTest2 = new BillingTestHomeBound();
        billingTest1.setId(57);
        billingTest1.setCode("1001");
        billingTest1.setAbbr("GLI");
        billingTest1.setName("GLICEMIA BASAL");
        billingTest1.setRate(rate);
        billingTests.add(billingTest1);
        billingTest2.setId(57);
        billingTest2.setCode("1005");
        billingTest2.setAbbr("gli2h");
        billingTest2.setName("GLICEMIA 2 HORAS");
        billingTest2.setRate(rate);
        billingTests.add(billingTest2);
        appointment.setBillingTests(billingTests);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(appointment);

        mockMvc.perform(put("/api/homebound/reprogram")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    //--SERVCIO DE SKYLAB PARA TRAER LLAVE DE CONFIGURACION--//
    @org.junit.Test
    public void test_16_getUrlSecurity() throws Exception
    {
        mockMvc.perform(get("/api/settings/UrlSecurity")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    //--SERVCIO QUE RETORNA LISTA DE CITAS DE ACUERDO AL NUMERO DE ORDEN--//
    @org.junit.Test
    public void test_17_getAppoints() throws Exception
    {
        List<Long> listOrders = new ArrayList<>();

        listOrders.add(Long.parseLong("202002070024"));
        listOrders.add(Long.parseLong("201806077502"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(listOrders);

        mockMvc.perform(post("/api/homebound/orders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isNoContent());
    }
}
