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
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.domain.masters.pathology.MacroscopyTemplate;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/pathology/template
 *
 * @version 1.0.0
 * @author omendez
 * @since 11/06/2021
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
public class MacroscopyTemplateTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public MacroscopyTemplateTest() {
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
    public void test_01_getFieldsByIdNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/template/fields/" + -1).header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_insertFieldsBySpecimen() throws Exception
    {

        List<Field> fields = new ArrayList<>();
        
        Field field = new Field();
        field.setName("Color");
        field.setType(1);
        field.setGrid(2);
        field.setRequired(1);
        field.setStatus(1);
        field.setOrder(1);
        field.getUserCreated().setId(1);
       
        String response = TestTools.getResponseString(mockMvc.perform(post("/api/pathology/field")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(field))));
        
        field = Tools.jsonObject(response, Field.class);
        fields.add(field);
        MacroscopyTemplate template = new MacroscopyTemplate();
        
        template.getSpecimen().setId(1);
        template.setFields(fields);
        template.getUserCreated().setId(1);
       
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(template);

        mockMvc.perform(post("/api/pathology/template")
            .header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8)
            .content(jsonContent))
            .andExpect(status().isOk()); 
    }
    
    @Test
    public void test_03_getFieldsById() throws Exception {
        mockMvc.perform(get("/api/pathology/template/fields/" + 1).header("Authorization", token)
            .contentType(TestTools.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));    
    }
}
