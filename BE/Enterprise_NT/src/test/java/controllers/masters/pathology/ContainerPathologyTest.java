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
import net.cltech.enterprisent.domain.masters.pathology.ContainerPathology;
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
 * Prueba unitaria sobre el api rest /api/pathology/container
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
public class ContainerPathologyTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ContainerPathologyTest() {}
    
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
    public void test_01_containerNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/container").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_insertContainer() throws Exception
    {
        ContainerPathology container = new ContainerPathology();
        container.setName("Lamina");
        container.setStatus(1);
        container.setPrint(1);
        container.getUserCreated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(container);

        mockMvc.perform(post("/api/pathology/container")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Lamina")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.status", Matchers.notNullValue()))
                .andExpect(jsonPath("$.createdAt", Matchers.notNullValue())); 
    }
    
    @Test
    public void test_03_getAllContainers() throws Exception
    {
        mockMvc.perform(get("/api/pathology/container").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getContainerById() throws Exception
    {
        mockMvc.perform(get("/api/pathology/container/filter/id/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getAreaByName() throws Exception
    {
        mockMvc.perform(get("/api/pathology/container/filter/name/" + "Lamina").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getAreaByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/container/filter/id/" + 0).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_getContainerByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/container/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_08_insertContainerError() throws Exception
    {
        ContainerPathology container = new ContainerPathology();
        container.setName("");
        container.setStatus(0);
        container.setPrint(0);
        container.getUserCreated().setId(-1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(container);

        mockMvc.perform(post("/api/pathology/container")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_09_updateContainer() throws Exception
    {
        ContainerPathology container = new ContainerPathology();
        container.setId(1);
        container.setName("Bloque");
        container.setStatus(1);
        container.setPrint(0);
        container.getUserUpdated().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(container);

        mockMvc.perform(put("/api/pathology/container")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Bloque")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.status", Matchers.notNullValue()))
                .andExpect(jsonPath("$.updatedAt", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateContainerError() throws Exception
    {
        ContainerPathology container = new ContainerPathology();
        container.setId(1);
        container.setName("");
        container.setStatus(0);
        container.setPrint(0);
        container.getUserUpdated().setId(-1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(container);

        mockMvc.perform(put("/api/pathology/container")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_getAllContainerByState() throws Exception
    {
        mockMvc.perform(get("/api/pathology/container/filter/state/" + 1).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_12_getAllContainerByStateNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/container/filter/state/" + -1).header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
}
