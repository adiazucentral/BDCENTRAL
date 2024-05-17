package controllers.masters.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import net.cltech.securitynt.dao.interfaces.masters.demographic.BranchDao;
import net.cltech.securitynt.dao.interfaces.masters.user.RolDao;
import net.cltech.securitynt.dao.interfaces.masters.user.UserDao;
import net.cltech.securitynt.domain.common.AuthenticationUser;
import net.cltech.securitynt.domain.masters.user.Module;
import net.cltech.securitynt.domain.masters.user.Role;
import net.cltech.securitynt.domain.masters.user.RoleByUser;
import net.cltech.securitynt.domain.masters.user.User;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.After;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/configuration
 *
 * @version 1.0.0
 * @author dcortes
 * @since 16/04/2017
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
public class AuthenticationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BranchDao branchDao;
    private MockMvc mockMvc;
    private User userTest;

    public AuthenticationTest() throws Exception
    {

    }

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @After
    public void reset() throws Exception
    {
        LocalDate temporalDate = LocalDate.now();
        TestScript.execTestUpdateScript("UPDATE lab04 SET lab04c6 = ?, lab04c7 = ?, lab04c8 = ? WHERE lab04c4 = 'eacuna'",
                java.sql.Date.valueOf(temporalDate),
                java.sql.Date.valueOf(temporalDate.plusDays(5)),
                java.sql.Date.valueOf(temporalDate));
    }

    @Test
    public void test_01_successLogin() throws Exception
    {
        TestScript.deleteData("lab04");
        TestScript.deleteData("lab05");
        TestScript.deleteData("lab93");
        TestScript.createInitialBranch(new ArrayList<>());
        TestScript.execTestUpdateScript("INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('Super', 'User', 'lismanager', '9AVAz9/hWlkxiGca+0hOTQ==', 1, now(), now(), now(), now(), null, null, null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);");
        TestScript.execTestUpdateScript("INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('System', 'User', 'system', '9AVAz9/hWlkxiGca+0hOTQ==', 1, now(), now(), now(), now(), null, null, null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);");
        TestScript.execTestUpdateScript("INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('Administrator', 'User', 'admin', '9AVAz9/hWlkxiGca+0hOTQ==', 0, now(), now(), now(), now(), null, 'Cltech1', null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);");
//        TestScript.execTestUpdateScript("INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('Edwin', 'Acuña', 'eacuna', 'mbTaDmOJhTM=', 0, now(), now(), now(), now(), null, 'Cltech1', null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);");
        createUser();
        TestScript.execTestUpdateScript("INSERT INTO lab93 (lab93c1, lab93c2, lab04c1, lab05c1) VALUES (1,1,4,2);");
        TestScript.execTestUpdateScript("INSERT INTO lab93 (lab93c1, lab93c2, lab04c1, lab05c1) VALUES (2,1,4,1);");
        TestScript.execTestUpdateScript("INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('Licenses', 'User', 'integration', 'L1QZeF2KfPPqqFh0Knz1RnN/9i1jEiaV', 1, now(), now(), now(), now(), null, null, null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);");
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("lismanager");
        user.setPassword("cltechmanager");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_02_failPassword() throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("lismanager");
        user.setPassword("cltechmanage");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[0]", is("5|invalid password")));
    }

    @Test
    public void test_03_failUsername() throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("lismanagerr");
        user.setPassword("cltechmanage");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[0]", is("4|invalid user")));

    }

    @Test
    public void test_04_activationUser() throws Exception
    {
        LocalDate temporalDate = LocalDate.now().plusDays(5);
        TestScript.execTestUpdateScript("UPDATE lab04 SET lab04c6 = ? WHERE lab04c4 = 'eacuna'", java.sql.Date.valueOf(temporalDate));
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("eacuna");
        user.setPassword("12345");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("6|activation date")));

    }

    @Test
    public void test_05_expirationPassword() throws Exception
    {
        LocalDate temporalDate = LocalDate.now().minusDays(5);
        TestScript.execTestUpdateScript("UPDATE lab04 SET lab04c8= ? WHERE lab04c4 = 'eacuna'", java.sql.Date.valueOf(temporalDate));
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("eacuna");
        user.setPassword("12345");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("6|password expiration date")));
    }

    @Test
    public void test_06_expirationUser() throws Exception
    {
        LocalDate temporalDate = LocalDate.now().minusDays(5);
        TestScript.execTestUpdateScript("UPDATE lab04 SET lab04c7= ? WHERE lab04c4 = 'eacuna'", java.sql.Date.valueOf(temporalDate));
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("eacuna");
        user.setPassword("12345");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("6|user expiration date")));
    }

    @Test
    public void test_07_invalidBranches() throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("eacuna");
        user.setPassword("12345");
        user.setBranch(5);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("7|invalid branche")));

    }

    @Test
    public void test_08_succedBrancheAccess() throws Exception
    {
        Integer assigned = branchDao.list().stream().filter(b -> b.isState()).findFirst().orElse(null).getId();

        AuthenticationUser user = new AuthenticationUser();
        user.setUser("eacuna");
        user.setPassword("12345");
        user.setBranch(assigned);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_09_successLoginCredentials() throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("integration");
        user.setPassword("integration12345");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    private void createUser() throws Exception
    {
        userTest = userDao.findByUserName("eacuna");
        if (userTest == null)
        {
            userTest = new User();
            Role supervisorRol = new Role();
            Role demographicRol = new Role();

            supervisorRol.setName("Supervisor");
            demographicRol.setName("Demographic");

            supervisorRol.getModules().add(new Module(2));
            supervisorRol.getModules().add(new Module(3));
            supervisorRol.getModules().add(new Module(4));
            supervisorRol.getModules().add(new Module(5));
            supervisorRol.getModules().add(new Module(6));
            supervisorRol.getModules().add(new Module(7));
            supervisorRol.getUser().setId(1);

            demographicRol.getModules().add(new Module(2));
            demographicRol.getModules().add(new Module(6));
            demographicRol.getModules().add(new Module(13));
            demographicRol.getModules().add(new Module(14));
            demographicRol.getModules().add(new Module(15));
            demographicRol.getModules().add(new Module(16));
            demographicRol.getModules().add(new Module(17));
            demographicRol.getModules().add(new Module(18));
            demographicRol.getUser().setId(1);

            userTest.setName("Edwin");
            userTest.setLastName("Acuña");
            userTest.setUserName("eacuna");
            userTest.setPassword("12345");
            userTest.setActivation(new Date());
            userTest.getUser().setId(1);
            userTest.setIdentification("123456789");
            userTest.setMaxDiscount(0.0);
            userTest.getType().setId(11);

            supervisorRol = rolDao.create(supervisorRol);
            demographicRol = rolDao.create(demographicRol);

            userTest.getRoles().add(new RoleByUser(supervisorRol.getId()));
            userTest.getRoles().add(new RoleByUser(demographicRol.getId()));

            userTest = userDao.create(userTest, 365);
        }
    }
}
