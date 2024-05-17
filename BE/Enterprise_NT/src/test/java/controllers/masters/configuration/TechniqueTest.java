/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Technique;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/techniques
 *
 * @version 1.0.0
 * @author MPerdomo
 * @since 25/04/2017
 * @see
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    MongoTestAppContext.class,
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TechniqueTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public TechniqueTest()
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
    public void test_01_getAllTechniquesNoContent() throws Exception
    {
        mockMvc.perform(get("/api/techniques").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_createTechniqueCodeInternalError() throws Exception
    {
        Technique technique = new Technique();
        technique.setName("Tecnica 1");
        technique.setState(true);
        technique.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(technique);

        mockMvc.perform(post("/api/techniques")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_03_createTechniqueNameInternalError() throws Exception
    {
        Technique technique = new Technique();
        technique.setCode("TE-1");
        technique.setState(true);
        technique.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(technique);

        mockMvc.perform(post("/api/techniques")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_04_createTechniqueUserInternalError() throws Exception
    {
        Technique technique = new Technique();
        technique.setCode("TE-1");
        technique.setName("Tecnica 1");
        technique.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(technique);

        mockMvc.perform(post("/api/techniques")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_05_createTechnique() throws Exception
    {
        Technique technique = new Technique();
        technique.setCode("TE-1");
        technique.setName("Tecnica 1");
        technique.setState(true);
        technique.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(technique);

        mockMvc.perform(post("/api/techniques")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_06_getTechniqueByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/techniques/filter/id/" + "2").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_getTechniqueById() throws Exception
    {
        mockMvc.perform(get("/api/techniques/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_08_getTechniqueByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/techniques/filter/code/" + "T-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getTechniqueByCode() throws Exception
    {
        mockMvc.perform(get("/api/techniques/filter/code/" + "TE-1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_10_getTechniqueByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/techniques/filter/name/" + "Tech 1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_getTechniqueByName() throws Exception
    {
        mockMvc.perform(get("/api/techniques/filter/name/" + "Tecnica 1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_12_getAllTechniques() throws Exception
    {
        mockMvc.perform(get("/api/techniques").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_14_updateTechniqueError() throws Exception
    {
        Technique technique = new Technique();
        technique.setId(-1);
        technique.setCode("TEC-1");
        technique.setName("Tecnica 1.1");
        technique.setState(true);
        technique.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(technique);

        mockMvc.perform(put("/api/techniques")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_15_updateTechnique() throws Exception
    {
        Technique technique = new Technique();
        technique.setId(1);
        technique.setCode("TEC-1");
        technique.setName("Tecnica 1.1");
        technique.setState(true);
        technique.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(technique);

        mockMvc.perform(put("/api/techniques")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}
