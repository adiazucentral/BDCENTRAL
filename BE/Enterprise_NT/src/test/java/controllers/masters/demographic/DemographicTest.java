package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.configuration.DemographicReportEncryption;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
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
 * Prueba unitaria sobre el api rest /api/demographics
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/05/2017
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
public class DemographicTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public DemographicTest()
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
    public void test_01_insertDemographicOrder() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setName("TARIFA2");
        demographic.setOrigin("O");
        demographic.setEncoded(true);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("2"));
        demographic.setStatistics(true);
        demographic.setLastOrder(true);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));

        demographic = new Demographic();
        demographic.setName("TARIFA3");
        demographic.setOrigin("H");
        demographic.setEncoded(true);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("2"));
        demographic.setStatistics(true);
        demographic.setLastOrder(true);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_02_insertDemographicOrderError() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setName("TARIFA4");
        demographic.setOrigin("O");
        demographic.setEncoded(true);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("2"));
        demographic.setStatistics(true);
        demographic.setLastOrder(true);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
        TestScript.execTestUpdateScript("DELETE FROM lab62");
        TestScript.execTestUpdateScript("ALTER SEQUENCE lab62_lab62c1_seq RESTART WITH 1");
    }

    @Test
    public void test_03_getAllDemographicsNoContent() throws Exception
    {
        mockMvc.perform(get("/api/demographics").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_04_insertDemographic() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setName("TARIFA");
        demographic.setOrigin("O");
        demographic.setEncoded(false);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("1"));
        demographic.setStatistics(true);
        demographic.setLastOrder(true);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_insertDemographicError() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setName("TARIFA");
        demographic.setOrigin("O");
        demographic.setEncoded(false);
        demographic.setObligatory(Short.parseShort("4"));
        demographic.setOrdering(Short.parseShort("1"));
        demographic.setStatistics(true);
        demographic.setLastOrder(true);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_06_updateDemographic() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setId(1);
        demographic.setName("TARIFA");
        demographic.setOrigin("H");
        demographic.setEncoded(false);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("1"));
        demographic.setStatistics(false);
        demographic.setLastOrder(false);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(put("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.origin", is("H")))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_07_updateDemographicError() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setId(-1);
        demographic.setName("TARIFA");
        demographic.setOrigin("H");
        demographic.setEncoded(false);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("1"));
        demographic.setStatistics(false);
        demographic.setLastOrder(false);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(put("/api/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_08_getAllDemographics() throws Exception
    {
        mockMvc.perform(get("/api/demographics").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_09_getDemographicById() throws Exception
    {
        mockMvc.perform(get("/api/demographics/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_10_getDemographicByName() throws Exception
    {
        mockMvc.perform(get("/api/demographics/filter/name/" + "TARIFA").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_11_getDemographicBySort() throws Exception
    {
        mockMvc.perform(get("/api/demographics/filter/sort/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_12_getDemographicByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/demographics/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_13_getDemographicByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/demographics/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_14_getDemographicBySortNoContent() throws Exception
    {
        mockMvc.perform(get("/api/demographics/filter/sort/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_15_getAllDemographics() throws Exception
    {
        mockMvc.perform(get("/api/demographics").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_16_updateOrder() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setId(1);
        demographic.setOrdering(Short.parseShort("2"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(Arrays.asList(demographic));

        mockMvc.perform(patch("/api/demographics/ordering")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

//    @Test
//    public void test_12_getAllDemographicByState() throws Exception
//    {
//        mockMvc.perform(get("/api/demographics/filter/state/" + true).header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
//    }
//
//    @Test
//    public void test_13_getAllDemographicByStateNoContent() throws Exception
//    {
//        mockMvc.perform(get("/api/demographics/filter/state/" + false).header("Authorization", token))
//                .andExpect(status().isNoContent());
//    }
    
    @Test
    public void test_17_saveDemographicReportEncrypt() throws Exception
    {
        List<DemographicReportEncryption> listDemo = new ArrayList<>();
        DemographicReportEncryption demoOne = new DemographicReportEncryption();
        
        // Agrego valores al objeto
        demoOne.setIdDemographic(1);
        demoOne.setIdDemographicItem(1);
        demoOne.setEncryption(1);
        // Guardo ese objeto en la lista
        listDemo.add(demoOne);
        
        // Sobre escribo los valores previos
        demoOne.setIdDemographic(1);
        demoOne.setIdDemographicItem(2);
        demoOne.setEncryption(1);
        // y este nuevo objeto lo agrego en la lista
        listDemo.add(demoOne);
        
        demoOne.setIdDemographic(1);
        demoOne.setIdDemographicItem(3);
        demoOne.setEncryption(1);
        listDemo.add(demoOne);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(listDemo);

        mockMvc.perform(post("/api/demographics/saveDemographicReportEncrypt")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_18_getDemographicReportEncryptById() throws Exception
    {
        mockMvc.perform(get("/api/demographics/getDemographicReportEncrypt/idDemographic/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }
}
