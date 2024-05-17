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
import net.cltech.enterprisent.domain.masters.pathology.Organ;
import net.cltech.enterprisent.domain.masters.pathology.Pathologist;
import net.cltech.enterprisent.domain.masters.user.User;
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
import tools.TestTools;
import tools.TestScript;

/**
 * Prueba unitaria sobre el api rest /api/pathology/pathologist
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/10/2020
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
public class PathologistTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public PathologistTest() {}
    
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
    public void test_01_pathologistNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/pathologist").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_insertOrgansByPathologist() throws Exception
    {
        //Rol de patologo
        TestScript.execTestUpdateScript("INSERT INTO lab84(lab84c1,lab04c1,lab82c1)"
                + " VALUES (1, 1, 3) ");
        
        List<Organ> organs = new ArrayList<>();
        
        Organ organ = new Organ();
        organ.setCode("HIG");
        organ.setName("Higado");
        organ.setStatus(1);
        organ.getUserCreated().setId(1);
       
        String response = TestTools.getResponseString(mockMvc.perform(post("/api/pathology/organ")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(organ))));
        
       
        organ = Tools.jsonObject(response, Organ.class);
        
        organs.add(organ);
        
        Pathologist pathologist = new Pathologist();
        
        User user = new User();
        user.setId(1);

        pathologist.setPathologist(user);
        pathologist.setOrgans(organs);
        pathologist.getUserCreated().setId(1);
       
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(pathologist);

        mockMvc.perform(post("/api/pathology/pathologist")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk()); 
    }
    
    @Test
    public void test_03_getPathologists() throws Exception {

        mockMvc.perform(get("/api/pathology/pathologist")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_04_getpathologistByIdNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/pathologist/filter/id/" + -1).header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_04_getpathologistById() throws Exception {
        mockMvc.perform(get("/api/pathology/pathologist/filter/id/" + 1).header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));    
    }
}
