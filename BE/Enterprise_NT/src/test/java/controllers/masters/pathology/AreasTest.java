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
import net.cltech.enterprisent.domain.masters.pathology.Area;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/pathology/areas
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/10/2020
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
public class AreasTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public AreasTest() {
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
    public void test_01_areasNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/areas").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_insertArea() throws Exception
    {
        Area area = new Area();
        area.setCode("Macro");
        area.setName("Macroscopia");
        area.setStatus(1);
        area.getUserCreated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(area);

        mockMvc.perform(post("/api/pathology/areas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("Macro")))
                .andExpect(jsonPath("$.name", is("Macroscopia")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.createdAt", Matchers.notNullValue())); 
    }
    
    @Test
    public void test_03_getAllAreas() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getAreaById() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas/filter/id/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getAreaByCode() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas/filter/code/" + "Macro").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getAreaByName() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas/filter/name/" + "Macroscopia").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_07_getAreaByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas/filter/id/" + 0).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getAreaByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas/filter/code/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getAreaByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_10_insertAreaError() throws Exception
    {
        Area area = new Area();
        area.setCode("");
        area.setName("");
        area.setStatus(0);
        area.getUserCreated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(area);

        mockMvc.perform(post("/api/pathology/areas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_updateArea() throws Exception
    {
        Area area = new Area();
        area.setId(1);
        area.setCode("Micro");
        area.setName("Microscopia");
        area.setStatus(1);
        area.getUserUpdated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(area);

        mockMvc.perform(put("/api/pathology/areas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("Micro")))
                .andExpect(jsonPath("$.name", is("Microscopia")))
                .andExpect(jsonPath("$.updatedAt", Matchers.notNullValue()));
    }

    @Test
    public void test_12_updateAreaError() throws Exception
    {
        Area area = new Area();
        area.setId(-1);
        area.setCode("");
        area.setName("");
        area.setStatus(1);
        area.getUserUpdated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(area);

        mockMvc.perform(put("/api/pathology/areas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_13_getAllAreaByState() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas/filter/state/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_14_getAllAreaByStateNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/areas/filter/state/" + 2).header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}


    

