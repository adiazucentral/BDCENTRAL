package controllers.masters.interview;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.interview.TypeInterview;
import static net.cltech.enterprisent.tools.Constants.ORDER;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
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
 * Prueba unitaria sobre el api rest /api/interviews
 *
 * @version 1.0.0
 * @author enavas
 * @since 22/08/2017
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
public class InterviewTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public InterviewTest()
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
    public void test_01_getAllInterviewNoContent() throws Exception
    {
        mockMvc.perform(get("/api/interviews").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertInterview() throws Exception
    {

        Interview interview = new Interview();

        interview.setId(1);
        interview.setName("Prueba");
        interview.setType((short) ORDER);
        interview.setPanic(true);

        Question question = new Question();
        question.setId(1);
        question.setOrder(1);

        interview.getQuestions().add(question);

        TypeInterview typeInterview = new TypeInterview();
        typeInterview.setId(1);

        interview.getTypeInterview().add(typeInterview);
        interview.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(interview);

        mockMvc.perform(post("/api/interviews")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Prueba")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllInterview() throws Exception
    {
        mockMvc.perform(get("/api/interviews").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getInterviewById() throws Exception
    {
        mockMvc.perform(get("/api/interviews/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Prueba")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getAnswerByName() throws Exception
    {
        mockMvc.perform(get("/api/interviews/filter/name/" + "Prueba").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Prueba")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_06_UpdateInterview() throws Exception
    {

        Interview interview = new Interview();

        interview.setId(1);
        interview.setName("Prueba2");
        interview.setType((short) ORDER);
        interview.setPanic(true);

        Question question = new Question();
        question.setId(1);
        question.setOrder(1);

        interview.getQuestions().add(question);

        TypeInterview typeInterview = new TypeInterview();
        typeInterview.setId(1);

        interview.getTypeInterview().add(typeInterview);
        interview.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(interview);

        mockMvc.perform(put("/api/interviews")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Prueba2")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_07_getInterviewByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/interviews/filter/id/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getAnswerByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/interviews/filter/name/" + "Prueba").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_insertInterviewError() throws Exception
    {

        Interview interview = new Interview();

        interview.setType((short) ORDER);
        interview.setPanic(true);

        Question question = new Question();
        question.setId(1);
        question.setOrder(1);

        interview.getQuestions().add(question);

        TypeInterview typeInterview = new TypeInterview();
        typeInterview.setId(1);

        interview.getTypeInterview().add(typeInterview);
        interview.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(interview);

        mockMvc.perform(post("/api/interviews")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_10_UpdateInterviewError() throws Exception
    {

        Interview interview = new Interview();

        interview.setName("Prueba2");
        interview.setType((short) ORDER);
        interview.setPanic(true);

        Question question = new Question();
        question.setId(1);
        question.setOrder(1);

        interview.getQuestions().add(question);

        TypeInterview typeInterview = new TypeInterview();
        typeInterview.setId(1);

        interview.getTypeInterview().add(typeInterview);
        interview.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(interview);

        mockMvc.perform(put("/api/interviews")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void test_12_getAnswersByOrder() throws Exception
    {
        mockMvc.perform(get("/api/interviews/getAnswersByOrder/idOrder/201709080001")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }
}