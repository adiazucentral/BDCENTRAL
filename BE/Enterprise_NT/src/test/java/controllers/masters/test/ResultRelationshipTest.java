package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Alarm;
import net.cltech.enterprisent.domain.masters.test.ResultRelationship;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba sobre el api rest /api/relationships
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/07/2017
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
public class ResultRelationshipTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ResultRelationshipTest()
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
        TestScript.execTestUpdateScript("DELETE FROM lab39");
        TestScript.execTestUpdateScript("DELETE FROM lab73");
        TestScript.execTestUpdateScript("DELETE FROM lab70");

        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (2, 2, '1002', 'GLU', 'GLUCOSA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 0, 0, 0, 0, 0, NULL, 1, NULL)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab73 VALUES (1, 'INCONGRUENTE', '', now(), 1, 1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab70(lab70c1,lab70c2,lab70c3,lab70c4,lab70c5,lab70c6,lab04c1,lab07c1)VALUES(1,'Diabetes','Es diabetico?',0,5,now(),1,1) ", null);
        TestScript.execTestUpdateScript("INSERT INTO lab70(lab70c1,lab70c2,lab70c3,lab70c4,lab70c5,lab70c6,lab04c1,lab07c1)VALUES(2,'Drogas','Ha consumido alguna droga en las ultimas 24h?',0,5,now(),1,1) ", null);

        mockMvc.perform(get("/api/resultrelationships/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_add() throws Exception
    {
        Alarm alarm = new Alarm(1);
        alarm.setRules(new ArrayList<>());
        ResultRelationship relationshp = new ResultRelationship();
        relationshp.setType(1);
        relationshp.setTest(new TestBasic(1));
        relationshp.setOperator("<");
        relationshp.setResult("1.6");
        alarm.getRules().add(relationshp);

        relationshp = new ResultRelationship();
        relationshp.setType(1);
        relationshp.setTest(new TestBasic(2));
        relationshp.setOperator(">=");
        relationshp.setResult("3.6");
        alarm.getRules().add(relationshp);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(alarm);

        mockMvc.perform(post("/api/resultrelationships/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/resultrelationships/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].test.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$[*].operator", containsInAnyOrder("<", ">=")))
                .andExpect(jsonPath("$[*].result", containsInAnyOrder("1.6", "3.6")));
    }

    @Test
    public void test_04_addQuestion() throws Exception
    {
        Alarm alarm = new Alarm(1);
        alarm.setRules(new ArrayList<>());
        ResultRelationship relationshp = new ResultRelationship();
        relationshp.setType(1);
        relationshp.setTest(new TestBasic(1));
        relationshp.setOperator("<");
        relationshp.setResult("1.6");
        alarm.getRules().add(relationshp);

        relationshp = new ResultRelationship();
        relationshp.setType(2);
        relationshp.setQuestion(new Question(1));
        relationshp.setOperator("=");
        relationshp.setResult("3");
        alarm.getRules().add(relationshp);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(alarm);

        mockMvc.perform(post("/api/resultrelationships/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void test_05_getQuestion() throws Exception
    {
        mockMvc.perform(get("/api/resultrelationships/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*].test.id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].question.id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].operator", containsInAnyOrder("<", "=")))
                .andExpect(jsonPath("$[*].result", containsInAnyOrder("1.6", "3")));
    }

    @Test
    public void test_06_addError() throws Exception
    {
        Alarm alarm = new Alarm(5);
        alarm.setRules(new ArrayList<>());
        ResultRelationship relationshp = new ResultRelationship();
        relationshp.setType(1);
        relationshp.setOperator("<");
        relationshp.setResult("1.6");
        alarm.getRules().add(relationshp);

        relationshp = new ResultRelationship();
        relationshp.setType(6);
        relationshp.setOperator("=");
        relationshp.setResult("3");
        alarm.getRules().add(relationshp);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(alarm);

        mockMvc.perform(post("/api/resultrelationships/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("3|alarm", "0|test|0", "3|type|1")));
    }
}
