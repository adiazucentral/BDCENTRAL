/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCultureTest;
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
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/mediaCulteres
 *
 * @version 1.0.0
 * @author enavas
 * @since 10/08/2017
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
public class MediaCultereTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public MediaCultereTest()
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
    public void test_01_getAllMediaCultureNoContent() throws Exception
    {
        mockMvc.perform(get("/api/mediacultures").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertMediaCulture() throws Exception
    {
        MediaCulture mediaCultere = new MediaCulture();

        mediaCultere.setCode("code123");
        mediaCultere.setName("prueba");
        mediaCultere.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(mediaCultere);

        mockMvc.perform(post("/api/mediacultures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("prueba")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllMediaCulture() throws Exception
    {
        mockMvc.perform(get("/api/mediacultures").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getMediaCultureById() throws Exception
    {
        mockMvc.perform(get("/api/mediacultures/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("prueba")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getMediaCultureByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/mediacultures/filter/id/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_MediaCultureByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/mediacultures/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_MediaCultureError() throws Exception
    {
        MediaCulture mediaCultere = new MediaCulture();

        mediaCultere.setCode("code123");
        mediaCultere.setName("prueba");
        mediaCultere.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(mediaCultere);

        mockMvc.perform(post("/api/mediacultures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_08_updateMediaCulture() throws Exception
    {
        MediaCulture mediaCultere = new MediaCulture();

        mediaCultere.setId(1);
        mediaCultere.setCode("code321");
        mediaCultere.setName("prueba2");
        mediaCultere.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(mediaCultere);

        mockMvc.perform(put("/api/mediacultures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("prueba2")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_09_getMediaCultureByName() throws Exception
    {
        mockMvc.perform(get("/api/mediacultures/filter/name/" + "prueba2").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("prueba2")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateMediaCultureError() throws Exception
    {
        MediaCulture mediaCultere = new MediaCulture();

        mediaCultere.setCode("code321");
        mediaCultere.setName("prueba2");
        mediaCultere.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(mediaCultere);

        mockMvc.perform(put("/api/mediacultures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_getMediaCultureByCode() throws Exception
    {
        mockMvc.perform(get("/api/mediacultures/filter/code/" + "code321").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("prueba2")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_12_getMediaCultureByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/mediacultures/filter/code/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_13_getMediaCultureByTest() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab155 SET lab07c1=1 WHERE lab155c1=1", null);
        mockMvc.perform(get("/api/mediacultures/filter/test/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_14_getMediaCultureByTestNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab155 SET lab07c1=0 WHERE lab155c1=1", null);
        mockMvc.perform(get("/api/mediacultures/filter/test/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_15_insertMediaCultureTest() throws Exception
    {
        MediaCulture mediaCultere = new MediaCulture();
        MediaCultureTest mediaCultureTest = new MediaCultureTest();
        mediaCultere.setCode("code456");
        mediaCultere.setName("prueba3");
        mediaCultere.getUser().setId(1);
        mediaCultere.setId(1);
        mediaCultere.setDefectValue(true);

        mediaCultureTest.getMediaCultures().add(mediaCultere);
        mediaCultureTest.setTestId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(mediaCultureTest);

        mockMvc.perform(post("/api/mediacultures/testofmediacultures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_16_insertMediaCultureTestError() throws Exception
    {

        MediaCultureTest mediaCultureTest = new MediaCultureTest();
        mediaCultureTest.setTestId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(mediaCultureTest);

        mockMvc.perform(post("/api/mediacultures/testofmediacultures")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

}
