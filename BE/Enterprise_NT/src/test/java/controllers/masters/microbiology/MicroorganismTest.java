package controllers.masters.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.SensitivityDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.MicroorganismAntibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MicroorganismService;
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
 * Prueba sobre el api rest /api/microorganisms
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
public class MicroorganismTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MicroorganismService microService;
    @Autowired
    private SensitivityDao sensitivityDao;
    @Autowired
    private SensitivityService sensitivityService;
    private MockMvc mockMvc;
    private String token;

    public MicroorganismTest()
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
        mockMvc.perform(get("/api/microorganisms").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_add() throws Exception
    {
        Microorganism micro = new Microorganism();
        micro.setName("Adenovirus");

        micro.setUser(new AuthorizedUser());
        micro.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(micro);

        mockMvc.perform(post("/api/microorganisms/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Adenovirus")))
                .andExpect(jsonPath("$.state", is(true)));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_getById() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/filter/id/{id}".replace("{id}", "1")).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Adenovirus")))
                .andExpect(jsonPath("$.state", is(true)));
    }

    @Test
    public void test_05_edit() throws Exception
    {
        Microorganism refrigerator = new Microorganism();

        refrigerator.setId(1);

        refrigerator.setName("Nevera de Muestras");
        refrigerator.setState(false);

        refrigerator.setUser(new AuthorizedUser());
        refrigerator.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(refrigerator);

        mockMvc.perform(put("/api/microorganisms/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Nevera de Muestras")))
                .andExpect(jsonPath("$.state", is(false)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }

    @Test
    public void test_06_getByName() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/filter/name/{name}".replace("{name}", "Nevera de Muestras")).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Nevera de Muestras")))
                .andExpect(jsonPath("$.state", is(false)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }

    @Test
    public void test_07_duplicateFields() throws Exception
    {
        Microorganism bean = new Microorganism();

        bean.setName("nevera de muestras");
        bean.setState(false);

        bean.setUser(new AuthorizedUser());
        bean.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bean);

        mockMvc.perform(post("/api/microorganisms/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name")));
    }

    @Test
    public void test_08_emptyField() throws Exception
    {
        Microorganism population = new Microorganism();

        population.setUser(new AuthorizedUser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(population);

        mockMvc.perform(post("/api/microorganisms/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|userId")));
    }

    @Test
    public void test_09_getByState() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/filter/state/" + "false").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_10_import() throws Exception
    {

        List<Microorganism> importMicro = new ArrayList<>(0);

        importMicro.add(new Microorganism(1, "Trichophyton tonsurans"));
        importMicro.add(new Microorganism(1, "trichophyton tonsurans"));
        importMicro.add(new Microorganism(1, "  "));
        importMicro.add(new Microorganism(1, "Trichophyton violaceum"));
        importMicro.add(new Microorganism(1, "Zygosaccharomyces spp."));
        importMicro.add(new Microorganism(1, null));
        importMicro.add(new Microorganism(1, "Yersinia ruckeri"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(importMicro);

        mockMvc.perform(post("/api/microorganisms/import/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));
    }

    @Test
    public void test_11_okImport() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void test_12_assignMicro() throws Exception
    {
        Sensitivity sensitivity = new Sensitivity();
        List<Microorganism> microList = microService.filterByState(true);
        sensitivity.setCode("MT1");
        sensitivity.setAbbr("AMT1");
        sensitivity.setName("Micro Antibiogram Test");
        sensitivity.getUser().setId(1);
        sensitivity = sensitivityDao.create(sensitivity);
        for (Microorganism microorganism : microList)
        {
            microorganism.getSensitivity().setId(sensitivity.getId());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(microList);

        mockMvc.perform(put("/api/microorganisms/sensitivity")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("" + microList.size()));

    }

    @Test
    public void test_13_getSensitivityNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/sensitivity/microorganism/2/test/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_14_getSensitivityNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/sensitivity/microorganism/2/test/0")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    //-----------MICROORGANISMO - ANTIBIOTICO-----------
    @Test
    public void test_15_listMicroorganismAntibioticNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab79;", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (1,'Amikacina','2018-01-30 08:23:03.48',1,1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (2,'Acinetobacter','2018-02-06 10:14:24.93',1,1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab79 VALUES (3,'Amoxacilina/Acido Clavulánico','2018-02-06 10:15:37.993',3,1);", null);

        mockMvc.perform(get("/api/microorganisms/antibiotics")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_16_insertMicroorganismAntibioticNoContent() throws Exception
    {
        MicroorganismAntibiotic microAntibiotic = new MicroorganismAntibiotic();
        microAntibiotic.getMicroorganism().setId(1);
        microAntibiotic.getAntibiotic().setId(1);
        microAntibiotic.setMethod((short) 1);
        microAntibiotic.setInterpretation((short) 1);
        microAntibiotic.setValueMin("15");
        microAntibiotic.getOperation().setId(50);
        microAntibiotic.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(microAntibiotic);

        mockMvc.perform(post("/api/microorganisms/antibiotics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_17_listMicroorganismAntibioticContent() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/antibiotics")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].microorganism.id", is(1)))
                .andExpect(jsonPath("$[0].antibiotic.id", is(1)))
                .andExpect(jsonPath("$[0].method", is(1)))
                .andExpect(jsonPath("$[0].interpretation", is(1)))
                .andExpect(jsonPath("$[0].valueMin", is("15")))
                .andExpect(jsonPath("$[0].operation.id", is(50)));
    }

    @Test
    public void test_18_updateMicroorganismAntibioticNoContent() throws Exception
    {
        MicroorganismAntibiotic microAntibiotic = new MicroorganismAntibiotic();
        microAntibiotic.getMicroorganism().setId(1);
        microAntibiotic.getAntibiotic().setId(1);
        microAntibiotic.setMethod((short) 1);
        microAntibiotic.setInterpretation((short) 1);
        microAntibiotic.setValueMin("90");
        microAntibiotic.setValueMax("");
        microAntibiotic.getOperation().setId(51);
        microAntibiotic.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(microAntibiotic);

        mockMvc.perform(put("/api/microorganisms/antibiotics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_19_getMicroorganismAntibioticNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/antibiotics/filter/2/1/1/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_20_listAntibioticsByMicroorganismNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/antibiotics/microorganism/2")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_21_listAntibioticsByMicroorganismContent() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/antibiotics/microorganism/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].microorganism.id", is(1)))
                .andExpect(jsonPath("$[0].antibiotic.id", is(1)))
                .andExpect(jsonPath("$[0].method", is(1)))
                .andExpect(jsonPath("$[0].interpretation", is(1)))
                .andExpect(jsonPath("$[0].valueMin", is("90")))
                .andExpect(jsonPath("$[0].operation.id", is(51)));
    }

    @Test
    public void test_22_getMicroorganismAntibioticContent() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/antibiotics/filter/1/1/1/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.microorganism.id", is(1)))
                .andExpect(jsonPath("$.antibiotic.id", is(1)))
                .andExpect(jsonPath("$.method", is(1)))
                .andExpect(jsonPath("$.interpretation", is(1)))
                .andExpect(jsonPath("$.valueMin", is("90")))
                .andExpect(jsonPath("$.operation.id", is(51)));
    }

    @Test
    public void test_23_deleteMicroorganismAntibioticContent() throws Exception
    {
        mockMvc.perform(delete("/api/microorganisms/antibiotics/filter/1/1/1/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_24_getMicroorganismAntibioticNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microorganisms/antibiotics/filter/1/1/1/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test()
    {
        Microorganism config = new Microorganism(1, "Trichophyton tonsurans");
        config.getUser().setId(1);

        Microorganism config1 = new Microorganism(1, "trichophyton tonsurans");
        config1.getUser().setId(1);
        assert (config.equals(config1));
    }

}
