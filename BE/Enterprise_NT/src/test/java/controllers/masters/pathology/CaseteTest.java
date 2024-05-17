/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.pathology.Casete;
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
 * Prueba unitaria sobre el api rest /api/pathology/casete
 *
 * @version 1.0.0
 * @author omendez
 * @since 07/04/2021
 * @see Creacion
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes
        = {
            MongoTestAppContext.class,
            TestAppContext.class
        })
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CaseteTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public CaseteTest() {
    }
    
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
        user.setBranch(1);
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }
    
    @Test
    public void test_01_caseteNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/casete").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_insertCasete() throws Exception
    {
        Casete casete = new Casete();
        casete.setCode("BLA");
        casete.setName("Blanco");
        casete.setStatus(1);
        casete.setColour("#ffffff");
        casete.getUserCreated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(casete);

        mockMvc.perform(post("/api/pathology/casete")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("BLA")))
                .andExpect(jsonPath("$.name", is("Blanco")))
                .andExpect(jsonPath("$.colour", is("#ffffff")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.createdAt", Matchers.notNullValue())); 
    }
    
    @Test
    public void test_03_getAllCasetes() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getCaseteById() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete/filter/id/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getCaseteByCode() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete/filter/code/" + "BLA").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getCaseteByName() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete/filter/name/" + "Blanco").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_07_getCaseteByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete/filter/id/" + 0).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getCaseteByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete/filter/code/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getCaseteByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_10_insertCaseteError() throws Exception
    {
        Casete casete = new Casete();
        casete.setCode("");
        casete.setName("");
        casete.setStatus(0);
        casete.setColour("");
        casete.getUserCreated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(casete);

        mockMvc.perform(post("/api/pathology/casete")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_updateCasete() throws Exception
    {
        Casete casete = new Casete();
        casete.setId(1);
        casete.setCode("NEG");
        casete.setName("Negro");
        casete.setStatus(1);
        casete.setColour("#000000");
        casete.getUserUpdated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(casete);

        mockMvc.perform(put("/api/pathology/casete")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("NEG")))
                .andExpect(jsonPath("$.name", is("Negro")))
                .andExpect(jsonPath("$.colour", is("#000000")))
                .andExpect(jsonPath("$.updatedAt", Matchers.notNullValue()));
    }

    @Test
    public void test_12_updateCaseteError() throws Exception
    {
        Casete casete = new Casete();
        casete.setId(-1);
        casete.setCode("");
        casete.setName("");
        casete.setStatus(1);
        casete.setColour("");
        casete.getUserUpdated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(casete);

        mockMvc.perform(put("/api/pathology/casete")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_13_getAllCasetesByState() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete/filter/state/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_14_getAllCaseteByStateNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/casete/filter/state/" + 2).header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
