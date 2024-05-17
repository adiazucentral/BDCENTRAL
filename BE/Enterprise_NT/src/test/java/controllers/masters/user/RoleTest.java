package controllers.masters.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.user.Module;
import net.cltech.enterprisent.domain.masters.user.Role;
import net.cltech.enterprisent.service.interfaces.masters.user.RoleService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
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
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba requirementaria sobre el api rest /api/roles
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/04/2017
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
public class RoleTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private RoleService roleService;
    private MockMvc mockMvc;
    private String token;

    public RoleTest()
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
    public void test_01_initRole() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab82 WHERE lab82c1 not in (1,2)", null);
        mockMvc.perform(get("/api/roles").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)));

    }

    @Test
    public void test_02_addRole() throws Exception
    {
        Role role = new Role();
        role.setName("NewRol");
        role.setAdministrator(false);

        role.getUser().setId(1);

        role.getModules().add(new Module(3));
        role.getModules().add(new Module(4));
        role.getModules().add(new Module(34));
        role.getModules().add(new Module(29));
        role.getModules().add(new Module(46));
        role.getModules().add(new Module(11));
        role.getModules().add(new Module(87));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(role);

        mockMvc.perform(post("/api/roles/")
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
        Role rol = roleService.findByName("NewRol");
        mockMvc.perform(get("/api/roles/filter/id/" + rol.getId())
                .header("Authorization", token))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.administrator", is(false)))
                .andExpect(jsonPath("$.name", is("NewRol")))
                .andExpect(jsonPath("$.modules[1].submodules", hasSize(9)))
                .andExpect(jsonPath("$.modules[1].submodules[?(@.id==203)].submodules[*].id", hasSize(5)));

    }

    @Test
    public void test_04_editRole() throws Exception
    {
        Role role = roleService.findByName("NewRol");
        role.setName("Billing Mod");
        role.setAdministrator(false);
        role.setState(true);
        role.getModules().clear();
        role.getModules().add(new Module(4));
        role.getModules().add(new Module(34));
        role.getModules().add(new Module(29));
        role.getModules().add(new Module(46));
        role.getModules().add(new Module(87));

        role.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(role);

        mockMvc.perform(put("/api/roles/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Billing Mod")))
                .andExpect(jsonPath("$.id", is(role.getId())))
                .andExpect(jsonPath("$.administrator", is(false)))
                .andExpect(jsonPath("$.state", is(true)))
                .andExpect(jsonPath("$.modules", hasSize(5)))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }

    @Test
    public void test_05_findByName() throws Exception
    {
        mockMvc.perform(get("/api/roles/filter/name/Billing Mod")
                .header("Authorization", token))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.administrator", is(false)))
                .andExpect(jsonPath("$.name", is("Billing Mod")))
                .andExpect(jsonPath("$.modules[1].submodules", hasSize(9)))
                .andExpect(jsonPath("$.modules[1].submodules[?(@.access==true)].id", empty()));
    }

    @Test
    public void test_06_AdministartorRoleModification() throws Exception
    {
        Role role = new Role();
        role.setId(1);
        role.setName("Billing");
        role.setAdministrator(false);

        role.getUser().setId(1);
        role.getModules().add(new Module(3));

        mockMvc.perform(put("/api/roles/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(role)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", contains("6|Administrator")));
        role.getUser().setId(2);
        mockMvc.perform(put("/api/roles/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(role)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", contains("6|Administrator")));

    }

    @Test
    public void test_07_duplicateRole() throws Exception
    {
        Role role = new Role();
        role.setName("Billing Mod");
        role.setAdministrator(false);

        role.setUser(new AuthorizedUser());
        role.getUser().setId(1);

        role.getModules().add(new Module(2));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(role);

        mockMvc.perform(post("/api/roles/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", contains("1|name")));
    }

    @Test
    public void test_08_getByState() throws Exception
    {
        mockMvc.perform(get("/api/roles/filter/state/" + "true").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_09_addRoleByModules() throws Exception
    {
        Role role = new Role();
        role.setId(1);
        role.getModules().add(new Module(2));
        role.getModules().add(new Module(3));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(role);

        mockMvc.perform(post("/api/roles/modules")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

}
