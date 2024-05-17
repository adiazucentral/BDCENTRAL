/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.microbiology.AnalyzerMicrobiologyDestination;
import net.cltech.enterprisent.domain.masters.microbiology.MicrobiologyDestination;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/microbiologydestinations
 *
 * @version 1.0.0
 * @author cmartin
 * @since 15/02/2018
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
public class MicrobiologyDestinationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public MicrobiologyDestinationTest()
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
    public void test_01_getAllMicrobiologyDestinationNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microbiologydestinations").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertMicrobiologyDestination() throws Exception
    {
        MicrobiologyDestination destination = new MicrobiologyDestination();
        List<AnalyzerMicrobiologyDestination> listAnalyzers = new ArrayList<>();
        destination.setCode("1");
        destination.setName("LECTURA");
        destination.setReportTask(true);
        destination.getUser().setId(1);
        destination.setAnalyzersMicrobiologyDestinations(listAnalyzers);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destination);

        mockMvc.perform(post("/api/microbiologydestinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1")))
                .andExpect(jsonPath("$.name", is("LECTURA")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllMicrobiologyDestination() throws Exception
    {
        mockMvc.perform(get("/api/microbiologydestinations").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getMicrobiologyDestinationById() throws Exception
    {
        mockMvc.perform(get("/api/microbiologydestinations/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1")))
                .andExpect(jsonPath("$.name", is("LECTURA")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getMicrobiologyDestinationByCode() throws Exception
    {
        mockMvc.perform(get("/api/microbiologydestinations/filter/code/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1")))
                .andExpect(jsonPath("$.name", is("LECTURA")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_06_getMicrobiologyDestinationByName() throws Exception
    {
        mockMvc.perform(get("/api/microbiologydestinations/filter/name/" + "LECTURA").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1")))
                .andExpect(jsonPath("$.name", is("LECTURA")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_07_getMicrobiologyDestinationByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microbiologydestinations/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getMicrobiologyDestinationByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microbiologydestinations/filter/code/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getMicrobiologyDestinationByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/microbiologydestinations/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_insertMicrobiologyDestinationError() throws Exception
    {
        MicrobiologyDestination destination = new MicrobiologyDestination();
        destination.setCode("1");
        destination.setName("LECTURA");
        destination.setReportTask(false);
        destination.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destination);

        mockMvc.perform(post("/api/microbiologydestinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_09_updateMicrobiologyDestination() throws Exception
    {
        MicrobiologyDestination destination = new MicrobiologyDestination();
        destination.setId(1);
        destination.setCode("1");
        destination.setName("Copernico");
        destination.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destination);

        mockMvc.perform(put("/api/microbiologydestinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("1")))
                .andExpect(jsonPath("$.name", is("Copernico")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateMicrobiologyDestinationError() throws Exception
    {
        MicrobiologyDestination destination = new MicrobiologyDestination();
        destination.setId(-1);
        destination.setCode("1");
        destination.setName("COPERNICO");
        destination.setState(false);
        destination.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destination);

        mockMvc.perform(put("/api/microbiologydestinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
}
