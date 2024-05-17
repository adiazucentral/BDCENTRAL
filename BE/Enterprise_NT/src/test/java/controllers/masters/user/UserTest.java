package controllers.masters.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.user.RoleByUser;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.masters.user.UserByBranchByAreas;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.hasSize;
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
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/users
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/05/2017
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
public class UserTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserDao userDao;
    private MockMvc mockMvc;
    private String token;

    public UserTest()
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
    public void test_01_getAllUser() throws Exception
    {
        mockMvc.perform(get("/api/users").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_02_insertUser() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);
        User user = new User();
        user.setName("Carlos");
        user.setLastName("Martin");
        user.setUserName("cmartin");
        user.setPassword("12345");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(30);
        user.setIdentification("1007100108");
        user.setEmail("a@b.c");
        user.setSignatureCode("12345");
        user.setMaxDiscount(0.0);
        user.getType().setId(11);
        user.setConfidential(true);
        user.setPrintInReports(true);
        user.setAddExams(true);
        user.setSecondValidation(true);
        user.setEditPatients(true);
        user.setQuitValidation(true);
        user.setCreatingItems(true);
        user.setPrintResults(true);

        RoleByUser role = new RoleByUser();
        role.getRole().setId(1);
        role.setAccess(true);
        user.getRoles().add(role);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_03_insertUserError() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);
        User user = new User();
        user.setName("Carlos");
        user.setLastName("Martin");
        user.setUserName("cmartin");
        user.setPassword("12345");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("1007100108");
        user.setEmail("a@b.c");
        user.setSignatureCode("12345");
        user.setMaxDiscount(0.0);
        user.getType().setId(11);
        user.setConfidential(true);
        user.setPrintInReports(true);
        user.setAddExams(true);
        user.setSecondValidation(true);
        user.setEditPatients(true);
        user.setQuitValidation(true);
        user.setCreatingItems(true);
        user.setPrintResults(true);

        RoleByUser role = new RoleByUser();
        role.getRole().setId(1);
        role.setAccess(true);
        user.getRoles().add(role);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_04_getUserById() throws Exception
    {
        mockMvc.perform(get("/api/users/" + "3").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getUserByUsername() throws Exception
    {
        mockMvc.perform(get("/api/users/filter/username/" + "cmartin").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getUserByIdentification() throws Exception
    {
        mockMvc.perform(get("/api/users/filter/identification/" + "1007100108").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_07_getUserBySignatureCode() throws Exception
    {
        mockMvc.perform(get("/api/users/filter/signaturecode/" + "12345").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_08_getUserByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/users/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getUserByUsernameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/users/filter/username/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_getUserByIdentificationNoContent() throws Exception
    {
        mockMvc.perform(get("/api/users/filter/identification/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_getUserBySignatureCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/users/filter/signaturecode/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_12_updateUser() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);
        User user = userDao.findByUserName("cmartin");
        user.setName("Carlos A");
        user.setLastName("Martin C");
        user.setUserName("cmartin");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.setPasswordExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("1007100108");
        user.setEmail("cmartin.cltech@gmail.com");
        user.setSignatureCode("12345");
        user.setMaxDiscount(0.0);
        user.getType().setId(12);
        user.setConfidential(true);
        user.setPrintInReports(true);
        user.setAddExams(true);
        user.setSecondValidation(true);
        user.setEditPatients(true);
        user.setQuitValidation(true);
        user.setCreatingItems(true);
        user.setPrintResults(true);

        RoleByUser role = new RoleByUser();
        role.getRole().setId(1);
        role.setAccess(true);
        user.getRoles().add(role);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Carlos A")))
                .andExpect(jsonPath("$.lastName", is("Martin C")))
                .andExpect(jsonPath("$.email", is("cmartin.cltech@gmail.com")))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_13_updateUserError() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 1);
        User user = new User();
        user.setId(-1);
        user.setName("Carlos A");
        user.setLastName("Martin C");
        user.setUserName("cmartin");
        user.setPassword("12345");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.setPasswordExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("1007100108");
        user.setEmail("cmartin.cltech@gmail.com");
        user.setSignatureCode("12345");
        user.setMaxDiscount(0.0);
        user.getType().setId(12);
        user.setConfidential(true);
        user.setPrintInReports(true);
        user.setAddExams(true);
        user.setSecondValidation(true);
        user.setEditPatients(true);
        user.setQuitValidation(true);
        user.setCreatingItems(true);
        user.setPrintResults(true);

        RoleByUser role = new RoleByUser();
        role.getRole().setId(1);
        role.setAccess(true);
        user.getRoles().add(role);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_14_getByBranchAreasNoContent() throws Exception
    {
        UserByBranchByAreas filter = new UserByBranchByAreas();
        filter.setAreas(Arrays.asList(1, 2));
        filter.setIdbranch(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(post("/api/users/getbybranchareas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_15_getByBranchAreasError() throws Exception
    {
        UserByBranchByAreas filter = new UserByBranchByAreas();
        filter.setAreas(null);
        filter.setIdbranch(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(post("/api/users/getbybranchareas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void test_16_getByBranchAreas() throws Exception
    {
        TestScript.deleteData("lab05");
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.execTestUpdateScript("INSERT INTO lab93(lab93c1, lab93c2, lab04c1, lab05c1) VALUES (1, 1, 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab69(lab69c1, lab69c2, lab04c1, lab43c1) VALUES (1, 0, 1, 1);", null);
        TestScript.execTestUpdateScript("INSERT INTO lab69(lab69c1, lab69c2, lab04c1, lab43c1) VALUES (1, 0, 1, 2);", null);
        UserByBranchByAreas filter = new UserByBranchByAreas();
        filter.setAreas(Arrays.asList(1, 2));
        filter.setIdbranch(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(post("/api/users/getbybranchareas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userName", is("lismanager")))
                .andExpect(jsonPath("$[0].name", is("Super")));
    }

    @Test
    public void test_17_insertUserIntegration() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);

        User user = new User();
        user.setName("Integracion");
        user.setUserName("Integra2");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("123441234");
        user.setPassword("1234");
        user.getType().setId(13);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users/bytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_18_insertUserIntegrationError() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);

        User user = new User();
        user.setName("Integracion");
        user.setUserName("Integra2");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("123441234");
        user.getType().setId(13);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users/bytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_19_getUserByIdIntegration() throws Exception
    {
        mockMvc.perform(get("/api/users/" + Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/users/filter/username/" + "Integra2").header("Authorization", token))), User.class).getId()).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_20_insertUserAnalyzer() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);

        User user = new User();
        user.setName("Analizador");
        user.setUserName("Analisa2");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("12344123");
        user.setDestination(new Destination());
        user.getDestination().setId(1);
        user.getType().setId(12);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users/bytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_21_insertUserAnalyzerError() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);

        User user = new User();
        user.setName("Analizador");
        user.setUserName("Analisa2");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("12344123");
        user.getType().setId(12);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users/bytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_22_getUserByAnalyzer() throws Exception
    {
        mockMvc.perform(get("/api/users/" + Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/users/filter/username/" + "Analisa2").header("Authorization", token))), User.class).getId()).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_23_updateUserIntegration() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);

        User user = new User();
        user.setId(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/users/filter/username/" + "Integra2").header("Authorization", token))), User.class).getId());
        user.setName("Integracion");
        user.setUserName("Integra3");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("123441234");
        user.setPassword("1234");
        user.getType().setId(13);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/bytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.userName", is("Integra3")))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_24_updateUserIntegrationError() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);

        User user = new User();
        user.setId(6);
        user.setName("Integracion");
        user.setUserName("Integra2");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.getType().setId(13);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/bytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_25_updateAnalyzerUser() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);

        User user = new User();
        user.setId(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/users/filter/username/" + "Analisa2").header("Authorization", token))), User.class).getId());
        user.setName("Analizador");
        user.setUserName("Analisa3");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("12344123");
        user.setDestination(new Destination());
        user.getDestination().setId(1);
        user.getType().setId(12);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/bytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.userName", is("Analisa3")))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_26_updateUserAnalyzerError() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Timestamp timestampExp = new Timestamp(new Date().getTime() + 132512652);

        User user = new User();
        user.setId(7);
        user.setName("Analizador");
        user.setUserName("Analisa2");
        user.setActivation(timestamp);
        user.setExpiration(timestampExp);
        user.getUser().setId(1);
        user.setIdentification("12344123");
        user.getType().setId(12);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/bytype")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_27_listDeactivate() throws Exception
    {
        mockMvc.perform(get("/api/users/listdeactivate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
                
    }

    @Test
    public void test_28_deactivateUser() throws Exception
    {
        User user = new User();
        user.setId(5);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/deactivate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/listdeactivate")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_29_getUsersAnalyzers() throws Exception
    {
        mockMvc.perform(get("/api/users/getUsersAnalyzers")
                .header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
}
