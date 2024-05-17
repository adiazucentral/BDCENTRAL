package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.AgeGroup;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.ListEnum;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
import tools.TestTools;

/**
 * Prueba requirementaria sobre el api rest /api/agegroups
 *
 * @version 1.0.0
 * @author eacuna
 * @since 31/01/2018
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
public class AgeGroupTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public AgeGroupTest()
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
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @Test
    public void test_01_noContent() throws Exception
    {
        mockMvc.perform(get("/api/agegroups").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_addGroup() throws Exception
    {
        AgeGroup ageGroup = new AgeGroup();
        ageGroup.setCode("01");
        ageGroup.setName("Infantes");
        ageGroup.setGender(new Item(ListEnum.Gender.BOTH.getValue()));
        ageGroup.setUnitAge((short) 2);
        ageGroup.setAgeMin(1);
        ageGroup.setAgeMax(160);

        ageGroup.setUser(new AuthorizedUser());
        ageGroup.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(ageGroup);

        mockMvc.perform(post("/api/agegroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void test_03_content() throws Exception
    {
        mockMvc.perform(get("/api/agegroups").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].code", is("01")))
                .andExpect(jsonPath("$[0].name", is("Infantes")))
                .andExpect(jsonPath("$[0].gender.id", is(ListEnum.Gender.BOTH.getValue())))
                .andExpect(jsonPath("$[0].unitAge", is(2)))
                .andExpect(jsonPath("$[0].ageMin", is(1)))
                .andExpect(jsonPath("$[0].ageMax", is(160)));
    }

    @Test
    public void test_04_editAgeGroup() throws Exception
    {
        AgeGroup ageGroup = new AgeGroup();
        ageGroup.setId(1);
        ageGroup.setCode("001");
        ageGroup.setName("Juventud");
        ageGroup.setGender(new Item(ListEnum.Gender.MALE.getValue()));
        ageGroup.setUnitAge((short) 1);
        ageGroup.setAgeMin(14);
        ageGroup.setAgeMax(26);
        ageGroup.setState(false);

        ageGroup.setUser(new AuthorizedUser());
        ageGroup.getUser().setId(1);

        String jsonContent = Tools.jsonObject(ageGroup);

        mockMvc.perform(put("/api/agegroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));

    }

    @Test
    public void test_05_contentById() throws Exception
    {
        mockMvc.perform(get("/api/agegroups/filter/id/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is("001")))
                .andExpect(jsonPath("$.name", is("Juventud")))
                .andExpect(jsonPath("$.gender.id", is(ListEnum.Gender.MALE.getValue())))
                .andExpect(jsonPath("$.unitAge", is(1)))
                .andExpect(jsonPath("$.ageMin", is(14)))
                .andExpect(jsonPath("$.ageMax", is(26)));
    }

    @Test
    public void test_06_ageGroupErrorDuplicate() throws Exception
    {
        AgeGroup ageGroup = new AgeGroup();
        ageGroup.setCode("001");
        ageGroup.setName("Juventud");
        ageGroup.setGender(new Item(ListEnum.Gender.MALE.getValue()));
        ageGroup.setUnitAge((short) 1);
        ageGroup.setAgeMin(14);
        ageGroup.setAgeMax(26);
        ageGroup.setState(false);

        ageGroup.setUser(new AuthorizedUser());
        ageGroup.getUser().setId(1);

        String jsonContent = Tools.jsonObject(ageGroup);

        mockMvc.perform(post("/api/agegroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name", "1|code")));
    }

    @Test
    public void test_07_ageGroupError() throws Exception
    {
        AgeGroup role = new AgeGroup();

        String jsonContent = Tools.jsonObject(role);

        mockMvc.perform(post("/api/agegroups/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|code", "0|userId", "0|gender", "0|unitAge")));
    }

}
