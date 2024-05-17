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
import net.cltech.enterprisent.domain.masters.pathology.Field;
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
 * Prueba unitaria sobre el api rest /api/pathology/field
 *
 * @version 1.0.0
 * @author omendez
 * @since 09/06/2021
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
public class FieldTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public FieldTest() {
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
    public void test_01_fieldNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/field").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_insertField() throws Exception
    {
        Field field = new Field();
        field.setName("Color");
        field.setType(1);
        field.setGrid(2);
        field.setRequired(1);
        field.setStatus(1);
        field.getUserCreated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(field);

        mockMvc.perform(post("/api/pathology/field")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Color")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.createdAt", Matchers.notNullValue())); 
    }
    
    @Test
    public void test_03_getAllFields() throws Exception
    {
        mockMvc.perform(get("/api/pathology/field").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getFieldById() throws Exception
    {
        mockMvc.perform(get("/api/pathology/field/filter/id/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getFieldByName() throws Exception
    {
        mockMvc.perform(get("/api/pathology/field/filter/name/" + "Color").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getFieldByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/field/filter/id/" + 0).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_getFieldByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/field/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_08_insertFieldError() throws Exception
    {
        Field field = new Field();
        field.setName("");
        field.setType(null);
        field.setGrid(null);
        field.setRequired(null);
        field.setStatus(null);
        field.getUserCreated().setId(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(field);

        mockMvc.perform(post("/api/pathology/field")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_09_updateField() throws Exception
    {
        Field field = new Field();
        field.setId(1);
        field.setName("Medidas");
        field.setType(1);
        field.setGrid(2);
        field.setRequired(1);
        field.setStatus(1);
        field.getUserUpdated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(field);

        mockMvc.perform(put("/api/pathology/field")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Medidas")))
                .andExpect(jsonPath("$.updatedAt", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateFieldError() throws Exception
    {
        Field field = new Field();
        field.setId(-1);
        field.setName("");
        field.setType(null);
        field.setGrid(null);
        field.setRequired(null);
        field.setStatus(null);
        field.getUserCreated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(field);

        mockMvc.perform(put("/api/pathology/field")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_getAllFieldsByState() throws Exception
    {
        mockMvc.perform(get("/api/pathology/field/filter/state/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_12_getAllFieldByStateNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/field/filter/state/" + 2).header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
