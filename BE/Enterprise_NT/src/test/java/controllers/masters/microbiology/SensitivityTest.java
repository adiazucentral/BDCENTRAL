package controllers.masters.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.AntibioticDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.microbiology.Antibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.AntibioticBySensitivity;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.SensitivityService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
 * Prueba sobre el api rest /api/sensitivities
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/06/2017
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
public class SensitivityTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AntibioticDao antibioticDao;
    @Autowired
    private SensitivityService sensitivityService;
    private MockMvc mockMvc;
    private String token;

    public SensitivityTest()
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
    public void test_02_add() throws Exception
    {
        TestScript.deleteData("lab79");
        Sensitivity sensitivity = new Sensitivity();
        sensitivity.setCode("01");
        sensitivity.setAbbr("GRAPOS");
        sensitivity.setName("GRAM POSITIVO");
        sensitivity.setSuppressionRule(true);
        sensitivity.setState(false);

        sensitivity.setUser(new AuthorizedUser());
        sensitivity.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sensitivity);

        mockMvc.perform(post("/api/sensitivities/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.code", is("01")))
                .andExpect(jsonPath("$.abbr", is("GRAPOS")))
                .andExpect(jsonPath("$.name", is("GRAM POSITIVO")))
                .andExpect(jsonPath("$.state", is(true)))
                .andExpect(jsonPath("$.suppressionRule", is(true)));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/sensitivities").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_getById() throws Exception
    {
        Integer id = sensitivityService.findByCode("01").getId();
        mockMvc.perform(get("/api/sensitivities/filter/id/{id}".replace("{id}", id.toString())).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.code", is("01")))
                .andExpect(jsonPath("$.abbr", is("GRAPOS")))
                .andExpect(jsonPath("$.name", is("GRAM POSITIVO")))
                .andExpect(jsonPath("$.state", is(true)))
                .andExpect(jsonPath("$.suppressionRule", is(true)));
    }

    @Test
    public void test_05_edit() throws Exception
    {
        Sensitivity sensitivity = new Sensitivity();

        sensitivity.setId(1);

        sensitivity.setCode("00");
        sensitivity.setAbbr("GRAPOSM");
        sensitivity.setName("GRAM POSITIVO MOD");
        sensitivity.setState(false);
        sensitivity.setSuppressionRule(false);

        sensitivity.setUser(new AuthorizedUser());
        sensitivity.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sensitivity);

        mockMvc.perform(put("/api/sensitivities/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("00")))
                .andExpect(jsonPath("$.abbr", is("GRAPOSM")))
                .andExpect(jsonPath("$.name", is("GRAM POSITIVO MOD")))
                .andExpect(jsonPath("$.state", is(false)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()))
                .andExpect(jsonPath("$.suppressionRule", is(false)));
    }

    @Test
    public void test_06_getByName() throws Exception
    {
        mockMvc.perform(get("/api/sensitivities/filter/name/{name}".replace("{name}", "Gram Positivo MOD")).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("00")))
                .andExpect(jsonPath("$.abbr", is("GRAPOSM")))
                .andExpect(jsonPath("$.name", is("GRAM POSITIVO MOD")))
                .andExpect(jsonPath("$.state", is(false)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()))
                .andExpect(jsonPath("$.suppressionRule", is(false)));
    }

    @Test
    public void test_07_duplicateFields() throws Exception
    {
        Sensitivity sensitivity = new Sensitivity();

        sensitivity.setCode("00");
        sensitivity.setAbbr("GRAPOSM");
        sensitivity.setName("GRAM POSITIVO MOD");
        sensitivity.setState(false);

        sensitivity.setUser(new AuthorizedUser());
        sensitivity.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sensitivity);

        mockMvc.perform(post("/api/sensitivities/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name", "1|abbr", "1|code")));
    }

    @Test
    public void test_08_emptyField() throws Exception
    {
        Sensitivity sensitivity = new Sensitivity();

        sensitivity.setUser(new AuthorizedUser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sensitivity);

        mockMvc.perform(post("/api/sensitivities/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|userId", "0|abbr", "0|code")));
    }

    @Test
    public void test_09_getByState() throws Exception
    {
        mockMvc.perform(get("/api/sensitivities/filter/state/" + "false").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_10_antibioticBySensitivity() throws Exception
    {

        List<Antibiotic> antibioticList = new ArrayList<>();
        List<AntibioticBySensitivity> relationList = new ArrayList<>();
        antibioticList.add(new Antibiotic("anti1"));
        antibioticList.add(new Antibiotic("anti2"));
        antibioticList.add(new Antibiotic("anti3"));
        antibioticList.add(new Antibiotic("anti4"));
        antibioticList.add(new Antibiotic("anti5"));
        antibioticList.add(new Antibiotic("anti6"));
        antibioticList.add(new Antibiotic("anti7"));

        for (Antibiotic antibiotic : antibioticList)
        {
            antibiotic.getUser().setId(1);
            antibiotic = antibioticDao.create(antibiotic);
            relationList.add(new AntibioticBySensitivity(1, 1, null, antibiotic));
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(relationList);

        mockMvc.perform(put("/api/sensitivities/antibiotics").header("Authorization", token)
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));
    }

    @Test
    public void test_11_antibioticBySensitivityError() throws Exception
    {

        List<Antibiotic> antibioticList = antibioticDao.list();
        List<AntibioticBySensitivity> relationList = new ArrayList<>();

        for (Antibiotic antibiotic : antibioticList)
        {
            relationList.add(new AntibioticBySensitivity(1, 1, null, antibiotic));
        }
        relationList.get(6).getAntibiotic().setId(null);
        relationList.get(5).setLine(4);
        relationList.get(4).setUnit(5);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(relationList);

        mockMvc.perform(put("/api/sensitivities/antibiotics").header("Authorization", token)
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|antibioticId", "3|unit|5", "3|line|6")));
    }

    @Test
    public void test_12_filterBySensitivity() throws Exception
    {
        mockMvc.perform(get("/api/antibiotics/filter/sensitivity/" + 1)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(7)));

    }

    @Test
    public void test_13_deleteSensitivity() throws Exception
    {
        mockMvc.perform(delete("/api/sensitivities/antibiotics/" + 1)
                .header("Authorization", token)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("7"));

    }

    @Test
    public void test_14_noContent() throws Exception
    {
        TestScript.deleteData("lab77");
        mockMvc.perform(get("/api/sensitivities").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_15_generalSensitivity() throws Exception
    {
        Sensitivity sensitivity = new Sensitivity();
        sensitivity.setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sensitivity);

        mockMvc.perform(put("/api/sensitivities/general")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

}
