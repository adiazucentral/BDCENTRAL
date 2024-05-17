package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.SpecialistDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.Specialist;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba sobre el api rest /api/physicians
 *
 * @version 1.0.0
 * @author eacuna
 * @since 23/05/2017
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
public class PhysicianTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private SpecialistDao specialistDao;
    private MockMvc mockMvc;
    private String token;

    public PhysicianTest()
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
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));

    }

    @Test
    public void test_01_noContent() throws Exception
    {
        mockMvc.perform(get("/api/physicians").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_add() throws Exception
    {
        Specialist specialist = new Specialist();
        specialist.setName("Ginecologo");

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

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(physician);

        mockMvc.perform(post("/api/physicians/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.identification", is("80189895")))
                .andExpect(jsonPath("$.name", is("Edwin")))
                .andExpect(jsonPath("$.lastName", is("Acuña")))
                .andExpect(jsonPath("$.phone", is("Phone123")))
                .andExpect(jsonPath("$.fax", is("FAX123")))
                .andExpect(jsonPath("$.address1", is("calle 123")))
                .andExpect(jsonPath("$.address2", is("calle 456")))
                .andExpect(jsonPath("$.city", is("Bogota")))
                .andExpect(jsonPath("$.obs", is("Esta es una prueba")))
                .andExpect(jsonPath("$.mmis", is("IMS123")))
                .andExpect(jsonPath("$.license", is("L123")))
                .andExpect(jsonPath("$.zipCode", is("555")))
                .andExpect(jsonPath("$.npi", is("NPI123")))
                .andExpect(jsonPath("$.institutional", is(true)))
                .andExpect(jsonPath("$.additionalReport", is(true)))
                .andExpect(jsonPath("$.userName", is("eacuna")))
                .andExpect(jsonPath("$.specialty.id", is(specialist.getId())))
                .andExpect(jsonPath("$.email", is("eacunar@cltech.net")))
                .andExpect(jsonPath("$.state", is("FL")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/physicians").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_edit() throws Exception
    {
        Physician physician = new Physician();

        Specialist specialty = new Specialist();
        specialty.setName("Physician Test");

        physician.setUser(new AuthorizedUser());
        physician.getUser().setId(1);

        physician.setId(1);
        physician.setIdentification("801898951");
        physician.setName("Edwin M");
        physician.setLastName("Acuña M");
        physician.setPhone("Phone123M");
        physician.setFax("FAX123M");
        physician.setAddress1("calle 123M");
        physician.setAddress2("calle 456M");
        physician.setCity("BogotaM");
        physician.setZipCode("555M");
        physician.setObs("Esta es una prueba M");
        physician.setMmis("IMS123 M");
        physician.setLicense("L123 M");
        physician.setNpi("NPI123 M");
        physician.setInstitutional(false);
        physician.setAdditionalReport(false);
        physician.setPassword("123456");
        physician.setEmail("eacuna@cltech.net");
        physician.setUserName("eacuna@cltech.net");
        physician.setCode("01");
        physician.setState("CA");
        physician.setSpecialty(specialty);
        physician.setActive(false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(physician);

        mockMvc.perform(put("/api/physicians")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.identification", is("801898951")))
                .andExpect(jsonPath("$.name", is("Edwin M")))
                .andExpect(jsonPath("$.lastName", is("Acuña M")))
                .andExpect(jsonPath("$.phone", is("Phone123M")))
                .andExpect(jsonPath("$.fax", is("FAX123M")))
                .andExpect(jsonPath("$.address1", is("calle 123M")))
                .andExpect(jsonPath("$.address2", is("calle 456M")))
                .andExpect(jsonPath("$.city", is("BogotaM")))
                .andExpect(jsonPath("$.obs", is("Esta es una prueba M")))
                .andExpect(jsonPath("$.mmis", is("IMS123 M")))
                .andExpect(jsonPath("$.license", is("L123 M")))
                .andExpect(jsonPath("$.zipCode", is("555M")))
                .andExpect(jsonPath("$.npi", is("NPI123 M")))
                .andExpect(jsonPath("$.institutional", is(false)))
                .andExpect(jsonPath("$.additionalReport", is(false)))
                .andExpect(jsonPath("$.userName", is("eacuna@cltech.net")))
                .andExpect(jsonPath("$.email", is("eacuna@cltech.net")))
                .andExpect(jsonPath("$.state", is("CA")))
                .andExpect(jsonPath("$.active", is(false)));
    }

    @Test
    public void test_05_filter_id() throws Exception
    {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mockMvc.perform(get("/api/physicians/filter/id/{id}".replace("{id}", "1"))
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_duplicateFields() throws Exception
    {
        Configuration billingConfiguration = new Configuration(Configuration.KEY_BILLING, Configuration.BILLING_GENERAL);
        configurationDao.update(billingConfiguration);

        Physician physician = new Physician();
        physician.setIdentification("801898951");
        physician.setName("Edwin M");
        physician.setInstitutional(false);
        physician.setAdditionalReport(false);
        physician.setMmis("IMS123 M");
        physician.setLicense("L123 M");
        physician.setUserName("eacuna@cltech.net");
        physician.setEmail("eacuna@cltech.net");
        physician.setCode("01");

        physician.setUser(new AuthorizedUser());
        physician.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(physician);

        mockMvc.perform(post("/api/physicians/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|identification", "1|email", "1|code")));
    }

    @Test
    public void test_07_duplicateFieldsUSA() throws Exception
    {

        Configuration billingConfiguration = new Configuration(Configuration.KEY_BILLING, Configuration.BILLING_USA);
        configurationDao.update(billingConfiguration);

        Physician physician = new Physician();
        physician.setIdentification("801898951");
        physician.setName("Edwin M");
        physician.setInstitutional(false);
        physician.setAdditionalReport(false);
        physician.setMmis("IMS123 M");
        physician.setLicense("L123 M");
        physician.setUserName("eacuna@cltech.net");
        physician.setEmail("eacuna@cltech.net");
        physician.setCode("01");

        physician.setUser(new AuthorizedUser());
        physician.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(physician);

        mockMvc.perform(post("/api/physicians/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|identification", "1|email", "1|license", "1|code")));
    }

    @Test
    public void test_08_emptyField() throws Exception
    {
        Configuration billingConfiguration = new Configuration(Configuration.KEY_BILLING, Configuration.BILLING_GENERAL);
        configurationDao.update(billingConfiguration);

        Physician physician = new Physician();

        physician.setUser(new AuthorizedUser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(physician);

        mockMvc.perform(post("/api/physicians/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|userId", "0|identification", "0|code", "0|email")));
    }

    @Test
    public void test_09_emptyFieldUSA() throws Exception
    {
        Configuration billingConfiguration = new Configuration(Configuration.KEY_BILLING, Configuration.BILLING_USA);
        configurationDao.update(billingConfiguration);

        Physician physician = new Physician();

        physician.setUser(new AuthorizedUser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(physician);

        mockMvc.perform(post("/api/physicians/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|userId", "0|email", "0|identification", "0|license", "0|code")));
    }

    @Test
    public void test_10_getByState() throws Exception
    {
        mockMvc.perform(get("/api/physicians/filter/state/" + "false").header("Authorization", token))
                .andExpect(status().isOk());
    }

}
