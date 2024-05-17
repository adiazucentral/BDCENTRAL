package controllers.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.pathology.ContainerPathology;
import net.cltech.enterprisent.domain.masters.pathology.Fixative;
import net.cltech.enterprisent.domain.masters.pathology.Organ;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import net.cltech.enterprisent.domain.operation.pathology.Case;
import net.cltech.enterprisent.domain.operation.pathology.SamplePathology;
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
import tools.TestScript;
import tools.TestTools;

/**
* Prueba unitaria sobre el api rest /api/pathology/case
*
* @version 1.0.0
* @author omendez
* @since 24/02/2021
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
public class CaseTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public CaseTest()
    {}
    
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
        user.setBranch(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }
    
    @Test
    public void test_01_noContentById() throws Exception
    {
        mockMvc.perform(get("/api/pathology/case/filter/id/2").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_02_noContentByStudyType() throws Exception
    {
        mockMvc.perform(get("/api/pathology/case/filter/studytype/2/number/2").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_03_noContentByEntryDate() throws Exception
    {
        mockMvc.perform(get("/api/pathology/case/filter/entryDate/20210202/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void test_04_create() throws Exception 
    {        
        StudyType studyType = new StudyType();
        studyType.setCode("Q");
        studyType.setName("Biopsia");
        studyType.setStatus(1);
        studyType.getUserCreated().setId(1);
        
        studyType.getStudies().add(new Study(1));
        studyType.getStudies().add(new Study(2));

        String response = TestTools.getResponseString(mockMvc.perform(post("/api/pathology/studytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(studyType))));
        
        studyType = Tools.jsonObject(response, StudyType.class);
                
        Case casePat = new Case();
        casePat.setStudyType(studyType);
        
        AuthorizedUser user = new AuthorizedUser();
        user.setId(1);
        
        Branch branch = new Branch();
        branch.setCode("test1");
        branch.setAbbreviation("test1");
        branch.setName("branch1");
        branch.setMinimum(1);
        branch.setMaximum(999);
        branch.setUser(user);
        response = TestTools.getResponseString(mockMvc.perform(post("/api/branches")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(branch))));
        
        branch = Tools.jsonObject(response, Branch.class);
        
        casePat.setBranch(branch);
        
        List<Specimen> specimens = new ArrayList<>();
        
        Specimen specimen = new Specimen();
        
        specimen.setId(1);
        
        Organ organ = new Organ();
        organ.setCode("HIG");
        organ.setName("Higado");
        organ.setStatus(1);
        organ.getUserCreated().setId(1);
       
        response = TestTools.getResponseString(mockMvc.perform(post("/api/pathology/organ")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(organ))));
        
        organ = Tools.jsonObject(response, Organ.class);
        
        specimen.setOrgan(organ);
        
        specimen.setSubSample(1);
        
        specimen.getUserCreated().setId(1);
        
        List<SamplePathology> samples = new ArrayList<>();
        
        SamplePathology sample = new SamplePathology();
        
        ContainerPathology container = new ContainerPathology();
        container.setName("Lamina");
        container.setStatus(1);
        container.getUserCreated().setId(1);

        response = TestTools.getResponseString(mockMvc.perform(post("/api/pathology/container")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(container))));
        
        container = Tools.jsonObject(response, ContainerPathology.class);
        
        sample.setContainer(container);
       
        Fixative fixative = new Fixative();
        fixative.setCode("FOR");
        fixative.setName("Formol");
        fixative.setStatus(1);
        fixative.getUserCreated().setId(1);
        
        response = TestTools.getResponseString(mockMvc.perform(post("/api/pathology/fixative")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(fixative))));
        
        fixative = Tools.jsonObject(response, Fixative.class);
        
        sample.setFixative(fixative);

        sample.setQuantity(1);
        
        samples.add(sample);
        
        specimen.setSamples(samples);
      
        specimen.getStudies().add(new Study(1));
        specimen.getStudies().add(new Study(2));
        
        specimens.add(specimen);
       
        casePat.setSpecimens(specimens);
        casePat.setCreatedDateShort(20210224);
        casePat.getOrder().setNumberOrder(202005210001L);
        casePat.getUserCreated().setId(1);
        casePat.setPathologist(null);
               
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(casePat);
        
        mockMvc.perform(post("/api/pathology/case")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_05_update() throws Exception 
    {
        TestScript.createInitialCases();      
        
        Case casePat = new Case();
        casePat.setId(1);
        casePat.setNumberCase(202103030001L);
        casePat.getStudyType().setId(1);
        casePat.setCreatedDateShort(20210303);
        casePat.getOrder().setNumberOrder(201710040010L);
        casePat.getBranch().setId(2);
        casePat.getUserUpdated().setId(1);
        casePat.setPathologist(null);

        List<Specimen> specimens = new ArrayList<>();
        
        Specimen specimen = new Specimen();
        
        specimen.setId(1);
        specimen.getOrgan().setId(1);
        specimen.setSubSample(1);
        
        List<SamplePathology> samples = new ArrayList<>();
        SamplePathology sample = new SamplePathology();
        sample.setQuantity(1);
        sample.getContainer().setId(1);
        sample.getFixative().setId(1);
        samples.add(sample);
        
        specimen.setSamples(samples);
        specimen.getStudies().add(new Study(6));
        
        specimens.add(specimen);
        
        casePat.setSpecimens(specimens);
                
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(casePat);
        
        mockMvc.perform(put("/api/pathology/case")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_06_getCaseById() throws Exception 
    {
        mockMvc.perform(get("/api/pathology/case/filter/id/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_07_getCaseByStudyType() throws Exception 
    {

        String response = TestTools.getResponseString(mockMvc.perform(get("/api/pathology/case/filter/id/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)));
        
        Case casePat = Tools.jsonObject(response, Case.class);
        
        mockMvc.perform(get("/api/pathology/case/filter/studytype/1/number/" + casePat.getNumberCase())
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_08_getCaseByEntryDate() throws Exception 
    {
        String response = TestTools.getResponseString(mockMvc.perform(get("/api/pathology/case/filter/id/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)));
        
        Case casePat = Tools.jsonObject(response, Case.class);
        
        mockMvc.perform(get("/api/pathology/case/filter/entryDate/" + casePat.getCreatedDateShort() + "/2")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
    
    @Test
    public void test_09_changeStatus() throws Exception 
    {
        TestScript.createInitialCases();      
        
        Case casePat = new Case();
        casePat.setId(1);
        casePat.getUserUpdated().setId(1);
        casePat.setStatus(2);
      
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(casePat);
        
        mockMvc.perform(put("/api/pathology/case/changestatus")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}
