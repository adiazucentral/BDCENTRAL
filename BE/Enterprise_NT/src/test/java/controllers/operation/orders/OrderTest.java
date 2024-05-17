package controllers.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.SpecialistDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.demographic.Specialist;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.FilterSubsequentPayments;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Agregar una descripcion de la clase
 *
 * @version 1.0.0
 * @author dcortes
 * @since 18/07/2017
 * @see Para cuando se crea una clase incluir
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes
        = {
            MongoTestAppContext.class,
            TestAppContext.class
        })
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private SpecialistDao specialistDao;

    @Before
    public void setUp() throws Exception {
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

    @Test
    public void test_01_create() throws Exception {

        //Crea el demographico de la historia
        Demographic demographic1 = new Demographic();
        demographic1.setName("CIUDAD");
        demographic1.setOrigin("H");
        demographic1.setEncoded(false);
        demographic1.setObligatory(Short.parseShort("0"));
        demographic1.setOrdering(Short.parseShort("8"));
        demographic1.setStatistics(false);
        demographic1.setLastOrder(false);
        demographic1.getUser().setId(1);
        demographic1.setModify(true);
        
        ResultActions result = mockMvc.perform(post("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(demographic1)));
        demographic1 = Tools.jsonObject(TestTools.getResponseString(result), Demographic.class);

        //Obtiene los demograficos
        List<Demographic> demographics = demographicService.list(true);
        Calendar calendar = Calendar.getInstance();
        Patient patient = new Patient();
        patient.setPatientId("123456");
        patient.setName1("NOMBRE 1");
        patient.setName2("NOMBRE 2");
        patient.setLastName("APELLIDO 1");
        patient.setSurName("APELLIDO 2");
        calendar.set(1987, 0, 19);
        patient.setBirthday(calendar.getTime());
        patient.setEmail("informacion@cltech.net");
        patient.setSize(new BigDecimal(180));
        patient.setWeight(new BigDecimal(87.5));
        patient.getDocumentType().setId(1);
        patient.getRace().setId(1);
        patient.getSex().setId(9);
        List<DemographicValue> demographicList = new ArrayList<>();
        DemographicValue demoValue = new DemographicValue();
        demoValue.setIdDemographic(demographic1.getId());
        demographicList.add(demoValue);
        
        if (!demographics.isEmpty())
        {
            DemographicValue value = null;
            for (Demographic demographic : demographics)
            {
                if (demographic.getOrigin().equals("H"))
                {
                    value = new DemographicValue();
                    value.setIdDemographic(demographic.getId());
                    if (demographic.isEncoded())
                    {
                        value.setCodifiedId(3);
                    } else
                    {
                        value.setNotCodifiedValue("123456");
                    }
                    patient.getDemographics().add(value);
                }
            }
        }
        
        String response = TestTools.getResponseString(mockMvc.perform(post("/api/patients")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(patient))));
        
        patient = Tools.jsonObject(response, Patient.class);
        Order order = new Order();
        order.setCreatedDateShort(20200521);
        OrderType type = new OrderType();
        type.setId(1);
        type.setCode("V");
        order.setType(type);
        order.setPatient(patient);
        order.setHomebound(false);
        order.setMiles(0);
        order.setActive(true);
        order.setExternalId("123456");
        
        AuthorizedUser user = new AuthorizedUser();
        user.setId(1);
        
        Branch branch = new Branch();
        branch.setCode("test1");
        branch.setAbbreviation("test1");
        branch.setName("branch1");
        branch.setMinimum(1);
        branch.setMaximum(999);
        branch.setUser(user);
        response = TestTools.getResponseString(mockMvc.perform(post("/api/branches")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(branch))));
        
        branch = Tools.jsonObject(response, Branch.class);
        order.setBranch(branch);
        
        ServiceLaboratory service = new ServiceLaboratory();
        service.setCode("123");
        service.setName("Urgencias");
        service.setUser(user);
        response = TestTools.getResponseString(mockMvc.perform(post("/api/services")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(service))));
        
        service = Tools.jsonObject(response, ServiceLaboratory.class);
        order.setService(service);
        
        Specialist specialist = new Specialist();
        specialist.setName("Prueb");
        
        specialist.getUser().setId(1);
        
        specialist = specialistDao.create(specialist);
        
        Physician physician = new Physician();
        physician.setIdentification("80189895");
        physician.setName("Edwin");
        physician.setLastName("Acuña");
        physician.setPhone("Phone123");
        physician.setFax("FAX123");
        physician.setAddress1("calle 123");
        physician.setAddress2("calle 456");
        physician.setCity("Bogota");
        physician.setZipCode("555");
        physician.setObs("Esta es una prueba");
        physician.setMmis("IMS123");
        physician.setLicense("L123");
        physician.setNpi("NPI123");
        physician.setInstitutional(true);
        physician.setAdditionalReport(true);
        physician.setUserName("eacuna");
        physician.setPassword("12345");
        physician.setEmail("eacunar@cltech.net");
        physician.setCode("01");
        physician.setState("FL");
        physician.setSpecialty(specialist);
        
        physician.setUser(new AuthorizedUser());
        physician.getUser().setId(1);
        response = TestTools.getResponseString(mockMvc.perform(post("/api/physicians")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(physician))));
        
        physician = Tools.jsonObject(response, Physician.class);
        order.setPhysician(physician);
        
        Account account = new Account();
        account.setNit("845614");
        account.setEpsCode("123456");
        account.setName("Colsanitas");
        account.setNamePrint("Colsanitas");
        account.setUser(user);
        response = TestTools.getResponseString(mockMvc.perform(post("/api/accounts")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(account))));
        
        account = Tools.jsonObject(response, Account.class);
        order.setAccount(account);
        
        Rate rate = new Rate();
        rate.setCode("123456");
        rate.setName("Particular");
        rate.setUser(user);
        response = TestTools.getResponseString(mockMvc.perform(post("/api/rates")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(rate))));
        
        rate = Tools.jsonObject(response, Rate.class);
        order.setRate(rate);
        order.setOrderNumber(202005210001L);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);
        
        TestScript.deleteData("lab24");
        TestScript.deleteData("lab39");
        TestScript.deleteData("lab57");
        //Muestra
        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (1, 'SANGRE', 1, 3, 1, 'DESCRIPCION', 10, '2017-09-11 13:48:21.775', 1, 1, 1, '1', '1,2,3', 1)");
        //Examen
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4      , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36        , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23)"
                + " VALUES (8      , 3      , '003'  , 'CP'   , 'CONFIDENCIAL', 1      , 7     , 0      , 200    , 1      , 2       , 1       , 1       , 1       , 1       , 0       , 0       , 0       , 1       ,'2017-09-13 11:23:52.872' , 1      , 1      , 0       , 0       , 0       , 0, 1,1,1,1,1,1)");
        // Resultado
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (202005210001, 8, '2017-09-13 11:33:13.552855', 1, 0, 4, NULL, NULL, 1, 20170908, 1)");
        
        mockMvc.perform(post("/api/orders")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_02_priceOfTest() throws Exception {
        TestScript.createInitialOrders();
        TestScript.execTestUpdateScript("DELETE FROM lab55");
        TestScript.execTestUpdateScript("INSERT INTO lab55 VALUES (1, 1, 100)");

        mockMvc.perform(get("/api/orders/price/test/1/rate/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("100"));
    }

    @Test
    public void test_03_priceOfTestNoRate() throws Exception {
        mockMvc.perform(get("/api/orders/price/test/1/rate/2")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("0"));
    }

    @Test
    public void test_04_get_getByEntryDateRecalled() throws Exception {
        mockMvc.perform(get("/api/orders/filter/recalled/entryDate/20170913/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());

    }

    @Test
    public void test_05_getByOrderRecalled() throws Exception {

        mockMvc.perform(get("/api/orders/filter/recalled/order/201709080001/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_getByPatientInfoRecalled() throws Exception {

        mockMvc.perform(get("/api/orders/filter/recalled/info/Martin/undefined/undefined/undefined/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_getRecalledOrder() throws Exception {
        TestScript.execTestUpdateScript("INSERT INTO lab221 (lab22c1_1, lab22c1_2) VALUES (201709080001, 201709080002); ");
        mockMvc.perform(get("/api/orders/filter/recalled/order/" + 201709080001L)
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.daughterOrder[0]", is(201709080002L)));
    }

    @Test
    public void test_08_isValidOrder() throws Exception {
        mockMvc.perform(get("/api/orders/isValidOrder/idOrder/1")
                .header(token, "Authorization"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_09_getPatientBasicInfo() throws Exception {
        mockMvc.perform(get("/api/orders/getPatientBasicInfo/idOrder/201709080001")
                .header(token, "Authorization"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_10_getRequirements() throws Exception {
        mockMvc.perform(get("/api/orders/getRequirements/idOrder/201709080001")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    
    @Test
    public void test_12_getOrderCode() throws Exception {
        mockMvc.perform(get("/api/orders/getOrderCode/idOrder/201709080001")
                .header(token, "Authorization"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_13_getPatientForExternalIdOrder() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab22 SET lab22c7 = 'IL2002000106' WHERE lab22c1 = 201710040010", null);
        mockMvc.perform(get("/api/orders/getPatientForExternalIdOrder/idExternalOrder/IL2002000106")
                .header(token, "Authorization"))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_14_getDemographics() throws Exception
    {
        mockMvc.perform(get("/api/orders/demographics/H")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_15_subsequentPayments() throws Exception
    {
        FilterSubsequentPayments filter = new FilterSubsequentPayments();
        filter.setInitialDate(20200915);
        filter.setInitialDate(20201022);
        filter.setOutstandingBalance(false);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(filter);
        
        mockMvc.perform(post("/api/orders/filter/subsequentPayments")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_16_getLastOrderNoContent() throws Exception {
        
        TestScript.createInitialOrders();
        mockMvc.perform(get("/api/orders/last/patient/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }
}
