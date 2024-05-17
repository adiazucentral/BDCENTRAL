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
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
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
 * Prueba unitaria sobre el api rest /api/pathology/specimen
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
public class SpecimenTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public SpecimenTest() {}
    
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
    public void test_01_specimenNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/specimen").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }
    
    @Test
    public void test_02_noContentStudy() throws Exception
    {
       mockMvc.perform(get("/api/pathology/specimen/study").header("Authorization", token))
               .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_03_insertSpecimen() throws Exception
    {
        TestScript.createInitialOrders();

        Specimen specimen = new Specimen();
        specimen.setId(1);
        specimen.setCode("ESP");
        specimen.setName("Muestra patologia");
        
        List<Specimen> subsamples = new ArrayList<>();

        Specimen subsample1 = new Specimen();
        subsample1.setId(2);
        subsample1.setCode("BIG");
        subsample1.setName("Biopsia Gastrica");
        
        subsamples.add(subsample1);
        
        Specimen subsample2 = new Specimen();
        subsample2.setId(3);
        subsample2.setCode("BIP");
        subsample2.setName("Biopsia de Prostata");
        
        subsamples.add(subsample2);

        specimen.setSubSamples(subsamples);
        
        specimen.getUserCreated().setId(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(specimen);

        mockMvc.perform(post("/api/pathology/specimen/subsamples")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk()); 
    }
   
    @Test
    public void test_04_getStudy() throws Exception {

        TestScript.createInitialOrders();

        mockMvc.perform(get("/api/pathology/specimen/study")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
}