package controllers.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
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
 * Prueba unitaria sobre el api rest /api/patients
 *
 * @version 1.0.0
 * @author dcortes
 * @since 04/07/2017
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
public class PatientTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DemographicDao demographicDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private DemographicService demographicService;
    private MockMvc mockMvc;
    private String token;

    public PatientTest()
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
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_insert() throws Exception
    {
        TestScript.createInitialOrders();
        Assert.assertTrue(true);
        //Crea el demographic de la historia
        Demographic demographic1 = new Demographic();
        demographic1.setName("CIUDADS");
        demographic1.setOrigin("H");
        demographic1.setEncoded(false);
        demographic1.setObligatory(Short.parseShort("0"));
        demographic1.setOrdering(Short.parseShort("8"));
        demographic1.setStatistics(false);
        demographic1.setLastOrder(false);
        demographic1.getUser().setId(1);
        demographic1.setModify(true);
        demographicDao.create(demographic1);
        ordersDao.addDemographicToPatient(demographic1);

        //Obtiene los demograficos
        List<Demographic> demographics = demographicService.list(true);
        Calendar calendar = Calendar.getInstance();
        Patient patient = new Patient();
        patient.setId(10);
        patient.setPatientId("1234567");
        patient.setDocumentType(new DocumentType());
        patient.getDocumentType().setId(1);
        patient.setName1("NOMBRE 3");
        patient.setName2("NOMBRE 4");
        patient.setLastName("APELLIDO 5");
        patient.setSurName("APELLIDO 6");
        patient.setSex(new Item(7));
        calendar.set(1987, 0, 19, 0, 0, 0);
        patient.setBirthday(calendar.getTime());
        patient.setEmail("informacion0@cltech.net");
        patient.setSize(new BigDecimal(180));
        patient.setWeight(new BigDecimal(87.5));
        patient.setRace(null);

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
                        value.setCodifiedId(1);
                    }
                    else
                    {
                        value.setNotCodifiedValue("12345");
                    }
                    patient.getDemographics().add(value);
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(patient);

        mockMvc.perform(post("/api/patients")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastUpdateDate", Matchers.notNullValue()));

        patient = new Patient();
        patient.setId(11);
        patient.setPatientId("12345678");
        patient.setDocumentType(new DocumentType());
        patient.getDocumentType().setId(1);
        patient.setName1("NOMBRE 3");
        patient.setName2("NOMBRE 4");
        patient.setLastName("APELLIDO 5");
        patient.setSurName("APELLIDO 6");
        patient.setSex(new Item(7));
        calendar.set(1987, 0, 19, 0, 0, 0);
        patient.setBirthday(calendar.getTime());
        patient.setEmail("informacion0@cltech.net");
        patient.setSize(new BigDecimal(180));
        patient.setWeight(new BigDecimal(87.5));
        patient.setRace(null);

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
                        value.setCodifiedId(1);
                    }
                    else
                    {
                        value.setNotCodifiedValue("12345");
                    }
                    patient.getDemographics().add(value);
                }
            }
        }
        jsonContent = mapper.writeValueAsString(patient);

        mockMvc.perform(post("/api/patients")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastUpdateDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_02_update() throws Exception
    {
        List<Demographic> demographics = demographicService.list(true);
        Calendar calendar = Calendar.getInstance();
        Patient patient = new Patient();
        patient.setId(0);
        patient.setPatientId("123456_UPDATE");
        patient.setName1("NOMBRE_1_UPDATE");
        patient.setName2("NOMBRE_2_UPDATE");
        patient.setLastName("APELLIDO_1_UPDATE");
        patient.setSurName("APELLIDO_2_UPDATE");
        patient.setSex(new Item(8));
        calendar.set(1988, 4, 19, 0, 0, 0);
        patient.setBirthday(calendar.getTime());
        patient.setEmail("informacion@cltech.net");
        patient.setSize(new BigDecimal(180));
        patient.setWeight(new BigDecimal(87.5));
        patient.setRace(null);

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
                        value.setCodifiedId(2);
                    }
                    else
                    {
                        value.setNotCodifiedValue(null);
                    }
                    patient.getDemographics().add(value);
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(patient);
        mockMvc.perform(put("/api/patients")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.name1", is("NOMBRE_1_UPDATE")));
    }

    @org.junit.Test
    public void test_03_getById() throws Exception
    {
        mockMvc.perform(get("/api/patients/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.patientId", is("1007100108")))
                .andExpect(jsonPath("$.name1", is("Carlos")))
                .andExpect(jsonPath("$.name2", is("Alberto")))
                .andExpect(jsonPath("$.lastName", is("Martin")))
                .andExpect(jsonPath("$.surName", is("Castro")))
                .andExpect(jsonPath("$.sex.id", is(7)))
                .andExpect(jsonPath("$.email", is("a@b.c")))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.weight", is(50)));
    }

    @org.junit.Test
    public void test_04_getByPatientId() throws Exception
    {
        mockMvc.perform(get("/api/patients/filter/patientId/1234567")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.patientId", is("1234567")))
                .andExpect(jsonPath("$.name1", is("NOMBRE 3")))
                .andExpect(jsonPath("$.name2", is("NOMBRE 4")))
                .andExpect(jsonPath("$.lastName", is("APELLIDO 5")))
                .andExpect(jsonPath("$.surName", is("APELLIDO 6")))
                .andExpect(jsonPath("$.sex.id", is(7)))
                .andExpect(jsonPath("$.email", is("informacion0@cltech.net")))
                .andExpect(jsonPath("$.size", is(180)))
                .andExpect(jsonPath("$.weight", is(87.5)));
    }

    @org.junit.Test
    public void test_05_getPatientBy() throws Exception
    {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(1988, 4, 19, 0, 0, 0);
//        String url = "/api/patients/filter/lastName/{lastName}/surName/{surName}/name1/{name1}/name2/{name2}/sex/{sex}/birthday/{birthday}"
//                .replace("{name1}", null)
//                .replace("{name2}", null)
//                .replace("{lastName}", null)
//                .replace("{surName}", null)
//                .replace("{sex}", null)
//                .replace("{birthday}", Long.toString(calendar.getTimeInMillis()));
//        mockMvc.perform(get(url)
//                .header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name1", is("NOMBRE_1_UPDATE")))
//                .andExpect(jsonPath("$[0].name2", is("NOMBRE_2_UPDATE")))
//                .andExpect(jsonPath("$[0].lastName", is("APELLIDO_1_UPDATE")))
//                .andExpect(jsonPath("$[0].surName", is("APELLIDO_2_UPDATE")))
//                .andExpect(jsonPath("$[0].sex.id", is(8)))
//                .andExpect(jsonPath("$[0].birthday", is(calendar.getTime().toString())));
    }

    @org.junit.Test
    public void test_06_createPatientHistory() throws Exception
    {
        TestScript.deleteData("lab17");
        HistoricalResult historicalResult = new HistoricalResult();
        historicalResult.setPatientId(1);
        historicalResult.setTestId(1);
        historicalResult.setLastResult("POSITIVO");
        historicalResult.setLastResultUser(new User());
        historicalResult.getLastResultUser().setId(15);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(historicalResult);
        mockMvc.perform(post("/api/patients/patientHistory")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.lastResultDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_07_updatePatientHistory() throws Exception
    {
        HistoricalResult historicalResult = new HistoricalResult();
        historicalResult.setPatientId(1);
        historicalResult.setTestId(1);
        historicalResult.setLastResult("NEGATIVO");
        historicalResult.setSecondLastResult("POSITIVO");
        historicalResult.setLastResultUser(new User());
        historicalResult.getLastResultUser().setId(15);
        historicalResult.setSecondLastResultUser(new User());
        historicalResult.getSecondLastResultUser().setId(15);
        historicalResult.setSecondLastResultDate(new Date());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(historicalResult);
        mockMvc.perform(patch("/api/patients/patientHistory")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.lastResultDate", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_08_getPatienByDemographic() throws Exception
    {
        mockMvc.perform(get("/api/patients/filter/demographicId/-1/demographicValue/11")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_09_getBasicPatientInformation() throws Exception
    {
        mockMvc.perform(get("/api/patients/getBasicPatientInformation/documentType/1/document/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_10_getBasicPatientInformationByDate() throws Exception
    {
        mockMvc.perform(get("/api/patients/getBasicPatientInformation/initialDate/20171004/endDate/20170908/patientStatus/0")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_11_updateBasicPatientInformation() throws Exception
    {
        Patient patientTwo = new Patient();
        patientTwo.setId(6);
        patientTwo.setName1(Tools.encrypt("Sujeto"));
        patientTwo.setName2(Tools.encrypt("Su Uno"));
        patientTwo.setLastName(Tools.encrypt("Pruebas"));
        patientTwo.setSurName(Tools.encrypt("PRU"));
        patientTwo.setBirthday(null);
        // patientTwo.setStatus(3);
        patientTwo.getSex().setId(7);
        // patientTwo.setDataTribunal("Data tribunal de prueba");
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(patientTwo);
        
        mockMvc.perform(put("/api/patients/updateBasicPatientInformation")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }
}
