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
import net.cltech.enterprisent.domain.masters.pathology.Study;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import net.cltech.enterprisent.service.interfaces.masters.pathology.StudyTypeService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
 * Prueba unitaria sobre el api rest /api/pathology/studytype
 *
 * @version 1.0.0
 * @author omendez
 * @since 27/10/2020
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
public class StudyTypeTest 
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private StudyTypeService studyTypeService;
    private MockMvc mockMvc;
    private String token;
    
    public StudyTypeTest(){}
    
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
    public void test_01_studyTypeNoData() throws Exception {
        mockMvc.perform(get("/api/pathology/studytype").header("Authorization", token))
                .andExpect(status().isNoContent());    
    }

    @Test
    public void test_02_addStudyType() throws Exception
    {
        StudyType studyType = new StudyType();
        studyType.setCode("Q");
        studyType.setName("Biopsia");
        studyType.setStatus(1);
        studyType.getUserCreated().setId(1);
        
        studyType.getStudies().add(new Study(1));
        studyType.getStudies().add(new Study(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(studyType);

        mockMvc.perform(post("/api/pathology/studytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()));
    }
    
    @Test
    public void test_03_findById() throws Exception
    {
        mockMvc.perform(get("/api/pathology/studytype/filter/id/" + 1)
                .header("Authorization", token))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.code", is("Q")))
                .andExpect(jsonPath("$.name", is("Biopsia")))
                .andExpect(jsonPath("$.status", is(1)));
    }
    
    @Test
    public void test_04_findByCode() throws Exception
    {
        mockMvc.perform(get("/api/pathology/studytype/filter/code/" + "Q")
                .header("Authorization", token))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.code", is("Q")))
                .andExpect(jsonPath("$.name", is("Biopsia")))
                .andExpect(jsonPath("$.status", is(1)));
    }
    
    @Test
    public void test_05_findByName() throws Exception
    {
        mockMvc.perform(get("/api/pathology/studytype/filter/name/" + "Biopsia")
                .header("Authorization", token))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.code", is("Q")))
                .andExpect(jsonPath("$.name", is("Biopsia")))
                .andExpect(jsonPath("$.status", is(1)));
    }
    
    @Test
    public void test_06_findByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/studytype/filter/id/" + 0)
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_07_findByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/studytype/filter/code/" + "H")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_08_findByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/studytype/filter/name/" + "Estudio")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_09_editStudyType() throws Exception
    {
        StudyType studyType = new StudyType();
        studyType.setId(1);
        studyType.setCode("A");
        studyType.setName("Estudio 1");
        studyType.setStatus(1);
        studyType.getUserUpdated().setId(1);
        
        studyType.getStudies().add(new Study(1));
        studyType.getStudies().add(new Study(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(studyType);

        mockMvc.perform(put("/api/pathology/studytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.code", is("A")))
                .andExpect(jsonPath("$.name", is("Estudio 1")))
                .andExpect(jsonPath("$.status", is(1)));
    }
    
    @Test
    public void test_10_addStudyTypeError() throws Exception
    {
        StudyType studyType = new StudyType();
        studyType.setCode("");
        studyType.setName("");
        studyType.setStatus(-1);
        studyType.getUserCreated().setId(-1);
       
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(studyType);

        mockMvc.perform(post("/api/pathology/studytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_11_editStudyTypeError() throws Exception
    {
        StudyType studyType = new StudyType();
        studyType.setId(1);
        studyType.setCode("");
        studyType.setName("");
        studyType.setStatus(-1);
        studyType.getUserUpdated().setId(-1);
       
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(studyType);

        mockMvc.perform(put("/api/pathology/studytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_12_getByState() throws Exception
    {
        mockMvc.perform(get("/api/pathology/studytype/filter/state/" + 1).header("Authorization", token))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_13_getByStateNoContent() throws Exception
    {
        mockMvc.perform(get("/api/pathology/studytype/filter/state/" + 2).header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
