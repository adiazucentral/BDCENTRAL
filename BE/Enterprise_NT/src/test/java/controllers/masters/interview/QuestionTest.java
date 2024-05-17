/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.interview;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Question;
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
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/question
 *
 * @version 1.0.0
 * @author cmartin
 * @since 16/08/2017
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
public class QuestionTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public QuestionTest()
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

    //RESPUESTAS    
    @Test
    public void test_01_getAllAnswerNoContent() throws Exception
    {
        mockMvc.perform(get("/api/questions/answer")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertAnswer() throws Exception
    {
        Answer answer = new Answer();
        answer.setName("Si");
        answer.getUser().setId(1);
        answer.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(answer);

        mockMvc.perform(post("/api/questions/answer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Si")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_insertAnswer2() throws Exception
    {
        Answer answer = new Answer();
        answer.setName("No");
        answer.getUser().setId(1);
        answer.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(answer);

        mockMvc.perform(post("/api/questions/answer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("No")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_04_getAllAnswer() throws Exception
    {
        mockMvc.perform(get("/api/questions/answer").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getAnswerById() throws Exception
    {
        mockMvc.perform(get("/api/questions/answer/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Si")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_06_getAnswerByName() throws Exception
    {
        mockMvc.perform(get("/api/questions/answer/filter/name/" + "Si").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Si")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_07_getAnswerByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/questions/answer/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getAnswerByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/questions/answer/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_insertAnswerError() throws Exception
    {
        Answer answer = new Answer();
        answer.setName("Si");
        answer.getUser().setId(1);
        answer.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(answer);

        mockMvc.perform(post("/api/questions/answer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_10_updateAnswer() throws Exception
    {
        Answer answer = new Answer();
        answer.setId(1);
        answer.setName("Si Aplica");
        answer.getUser().setId(1);
        answer.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(answer);

        mockMvc.perform(put("/api/questions/answer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Si Aplica")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_11_getQuestionByName() throws Exception
    {
        mockMvc.perform(get("/api/questions/answer/filter/name/" + "Si Aplica").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Si Aplica")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_12_updateQuestionError() throws Exception
    {
        Answer answer = new Answer();
        answer.setId(1);
        answer.setName("No");
        answer.getUser().setId(1);
        answer.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(answer);

        mockMvc.perform(put("/api/questions/answer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }

    //PREGUNTAS
    @Test
    public void test_13_getAllQuestionNoContent() throws Exception
    {
        mockMvc.perform(get("/api/questions")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_14_insertQuestion() throws Exception
    {
        Answer answer1 = new Answer();
        answer1.setId(1);
        answer1.setSelected(true);

        Answer answer2 = new Answer();
        answer2.setId(2);
        answer2.setSelected(true);

        Question question = new Question();
        question.setName("Fumo");
        question.setQuestion("¿Usted fumo en las ultima 24 horas?");
        question.setOpen(false);
        question.setControl((short) 4);
        question.getUser().setId(1);
        question.setState(true);
        question.setRequired(true);
        question.getAnswers().add(answer1);
        question.getAnswers().add(answer2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(question);

        mockMvc.perform(post("/api/questions")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Fumo")))
                .andExpect(jsonPath("$.question", is("¿Usted fumo en las ultima 24 horas?")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_15_getAllQuestion() throws Exception
    {
        mockMvc.perform(get("/api/questions").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_16_getQuestionById() throws Exception
    {
        mockMvc.perform(get("/api/questions/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Fumo")))
                .andExpect(jsonPath("$.question", is("¿Usted fumo en las ultima 24 horas?")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_17_getQuestionByName() throws Exception
    {
        mockMvc.perform(get("/api/questions/filter/name/" + "Fumo").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Fumo")))
                .andExpect(jsonPath("$.question", is("¿Usted fumo en las ultima 24 horas?")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_18_getQuestionByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/questions/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_19_getQuestionByQuestionNoContent() throws Exception
    {
        mockMvc.perform(get("/api/questions/filter/question/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_20_getQuestionByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/questions/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_21_insertQuestionError() throws Exception
    {
        Answer answer1 = new Answer();
        answer1.setId(1);
        answer1.setSelected(true);

        Answer answer2 = new Answer();
        answer2.setId(2);
        answer2.setSelected(true);

        Question question = new Question();
        question.setName("Fumo");
        question.setQuestion("¿Usted fumo en las ultima 24 horas?");
        question.setOpen(false);
        question.setControl((short) 8);
        question.getUser().setId(1);
        question.setState(true);
        question.getAnswers().add(answer1);
        question.getAnswers().add(answer2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(question);

        mockMvc.perform(post("/api/questions")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_22_updateQuestion() throws Exception
    {
        Answer answer1 = new Answer();
        answer1.setId(1);
        answer1.setSelected(true);

        Answer answer2 = new Answer();
        answer2.setId(2);
        answer2.setSelected(true);

        Question question = new Question();
        question.setId(1);
        question.setName("Fumar");
        question.setQuestion("¿Usted fuma?");
        question.setOpen(false);
        question.setControl((short) 4);
        question.setRequired(true);
        question.getUser().setId(1);
        question.setState(true);
        question.getAnswers().add(answer1);
        question.getAnswers().add(answer2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(question);

        mockMvc.perform(put("/api/questions")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Fumar")))
                .andExpect(jsonPath("$.question", is("¿Usted fuma?")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_23_getQuestionByName() throws Exception
    {
        mockMvc.perform(get("/api/questions/filter/name/" + "Fumar").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Fumar")))
                .andExpect(jsonPath("$.question", is("¿Usted fuma?")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_24_updateQuestionError() throws Exception
    {
        Answer answer1 = new Answer();
        answer1.setId(1);
        answer1.setSelected(true);

        Answer answer2 = new Answer();
        answer2.setId(2);
        answer2.setSelected(true);

        Question question = new Question();
        question.setId(1);
        question.setName("Fumar");
        question.setQuestion("¿Usted fuma?");
        question.setOpen(false);
        question.setControl((short) 8);
        question.getUser().setId(1);
        question.setState(true);
        question.getAnswers().add(answer1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(question);

        mockMvc.perform(put("/api/questions")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }
}
