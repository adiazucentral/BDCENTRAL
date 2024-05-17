package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.masters.test.ResultTemplate;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.contains;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/templates
 *
 * @version 1.0.0
 * @author eacuna
 * @since 01/08/2017
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
public class TemplateTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public TemplateTest()
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
    public void test_01_NoContent() throws Exception
    {
        mockMvc.perform(get("/api/templates/filter/test/" + 1)
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_newTemplate() throws Exception
    {
        TestScript.deleteData("lab39");
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        List<OptionTemplate> templateList = new ArrayList<>();
        OptionTemplate template = new OptionTemplate();
        template.setOption("Option 1");
        template.setIdTest(1);
        template.getResults().add(new ResultTemplate("Globulos blancos"));
        template.getResults().add(new ResultTemplate("Hematocritos"));

        templateList.add(template);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(templateList);
        mockMvc.perform(post("/api/templates/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_03_getTemplates() throws Exception
    {
        mockMvc.perform(get("/api/templates/filter/test/" + 1)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].option", is("Option 1")))
                .andExpect(jsonPath("$[0].idTest", is(1)))
                .andExpect(jsonPath("$[0].results", hasSize(2)))
                .andExpect(jsonPath("$[0].results[*].result", contains("Globulos blancos", "Hematocritos")));
    }

    @Test
    public void test_04_deleteTemplates() throws Exception
    {
        mockMvc.perform(delete("/api/templates/test/" + 1)
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_05_NoContent() throws Exception
    {
        mockMvc.perform(get("/api/templates/filter/test/" + 1)
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

}
