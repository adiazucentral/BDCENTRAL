package controllers.masters.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/samples
 *
 * @version 1.0.0
 * @author eacuna
 * @since 25/08/2017
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
public class SubSampleTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public SubSampleTest()
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
    public void test_01_filterTypeNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab24", null);
        TestScript.execTestUpdateScript("DELETE FROM lab56", null);

        TestScript.execTestUpdateScript("INSERT INTO lab56 VALUES (1, 'Tubo', null, now(), 1, 1, 1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (1, 'M_OJO', 1, 1, 1, null, 1, now(), 1, 0, 1, 1, '1,3', 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (2, 'M_SANGRE', 1, 1, 1, null, 1, now(), 1, 0, 1, 2, 3, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (3, 'CULTIVO', 1, 1, 1, null, 1, now(), 1, 0, 1, 3, 3, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (4, 'COPROCULTIVO', 1, 1, 1, null, 1, now(), 1, 0, 1, 4, 3, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (5, 'SANGRE', 1, 1, 1, null, 1, now(), 1, 1, 1, 5, 1, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (6, 'ORINA', 1, 1, 1, null, 1, now(), 1, 1, 1, 6, 1, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (7, 'SUERO', 1, 1, 1, null, 1, now(), 1, 0, 1, 7, 2, 1)", null);
        mockMvc.perform(get("/api/samples/filter/type/{types}".replace("{types}", "3")).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_getSubSamplesNoContent() throws Exception
    {
        mockMvc.perform(get("/api/samples/subsamples/{sampleId}".replace("{sampleId}", "1"))
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_03_filterType() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab24 set lab07c1 = 1", null);
        mockMvc.perform(get("/api/samples/filter/type/{types}".replace("{types}", "1,3"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[*].id", not(contains(7))));
    }

    @Test
    public void test_04_getSubSamples() throws Exception
    {
        mockMvc.perform(get("/api/samples/subsamples/{sampleId}".replace("{sampleId}", "4"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].selected", contains(false, false, false)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 3)));
    }

    @Test
    public void test_05_assignSample() throws Exception
    {
        Sample sample = new Sample();
        sample.getUser().setId(4);
        sample.setId(3);//CULTIVO
        sample.getSubSamples().add(new Sample(1));//M_OJO
        sample.getSubSamples().add(new Sample(2));//SANGRE

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sample);

        mockMvc.perform(post("/api/samples/subsamples/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void test_06_assignedSamples() throws Exception
    {
        mockMvc.perform(get("/api/samples/subsamples/{sampleId}".replace("{sampleId}", "3"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[?(@.id==1)].selected", contains(true)))
                .andExpect(jsonPath("$[?(@.id==2)].selected", contains(true)))
                .andExpect(jsonPath("$[?(@.id==4)].selected", contains(false)));
    }

    @Test
    public void test_07_deleteSubSamples() throws Exception
    {
        Sample sample = new Sample();
        sample.getUser().setId(4);
        sample.setId(3);//CULTIVO

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sample);

        mockMvc.perform(post("/api/samples/subsamples/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    public void test_08_deletedSubSamples() throws Exception
    {
        mockMvc.perform(get("/api/samples/subsamples/{sampleId}".replace("{sampleId}", "3"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[?(@.id==1)].selected", contains(false)))
                .andExpect(jsonPath("$[?(@.id==2)].selected", contains(false)))
                .andExpect(jsonPath("$[?(@.id==4)].selected", contains(false)));
    }

}
