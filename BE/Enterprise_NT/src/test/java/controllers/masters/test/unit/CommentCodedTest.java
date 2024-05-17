/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.test.unit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.CommentCoded;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
 * Prueba unitaria sobre el api rest /api/comments
 *
 * @version 1.0.0
 * @author enavas
 * @since 02/05/2017
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
public class CommentCodedTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public CommentCodedTest()
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
    public void test_01_getnotallComments() throws Exception
    {
        mockMvc.perform(get("/api/comments").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_addComment() throws Exception
    {

        CommentCoded comment = new CommentCoded();
        AuthorizedUser usu = new AuthorizedUser();
        comment.setId(0);
        comment.setCode("pru");
        comment.setMessage("prueba");
        comment.setState(true);
        comment.setApply(1);
        comment.setDiagnostic(1);
        usu.setId(1);
        comment.setUser(usu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comment);
        mockMvc.perform(post("/api/comments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(comment.getCode())));
    }

    @Test
    public void test_03_getallComments() throws Exception
    {
        mockMvc.perform(get("/api/comments").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_ErroraddComment() throws Exception
    {

        CommentCoded comment = new CommentCoded();
        AuthorizedUser usu = new AuthorizedUser();
        comment.setId(0);
        comment.setCode("pru");
        comment.setMessage("prueba");
        comment.setState(true);
        comment.setApply(5);
        comment.setDiagnostic(5);
        usu.setId(1);
        comment.setUser(usu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comment);
        mockMvc.perform(post("/api/comments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_05_modifyComment() throws Exception
    {

        CommentCoded comment = new CommentCoded();
        AuthorizedUser usu = new AuthorizedUser();
        comment.setId(1);
        comment.setCode("pru2");
        comment.setMessage("prueba2");
        comment.setState(true);
        comment.setApply(2);
        comment.setDiagnostic(1);
        usu.setId(1);
        comment.setUser(usu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comment);
        mockMvc.perform(put("/api/comments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is(comment.getCode())));
    }

    @Test
    public void test_06_errormodifyComment() throws Exception
    {
        CommentCoded comment = new CommentCoded();
        AuthorizedUser usu = new AuthorizedUser();
        comment.setId(1);
        comment.setCode("pru2");
        comment.setMessage("prueba2");
        comment.setState(true);
        comment.setApply(8);
        comment.setDiagnostic(6);
        usu.setId(1);
        comment.setUser(usu);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comment);
        mockMvc.perform(put("/api/comments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_07_getidComment() throws Exception
    {
        mockMvc.perform(get("/api/comments/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_08_errorgetidComment() throws Exception
    {
        mockMvc.perform(get("/api/comments/filter/id/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getcodeComment() throws Exception
    {
        mockMvc.perform(get("/api/comments/filter/code/" + "pru2").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_10_errorgetcodeComment() throws Exception
    {
        mockMvc.perform(get("/api/comments/filter/code/" + "pra").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_getapplyComment() throws Exception
    {
        mockMvc.perform(get("/api/comments/filter/apply/" + "2").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_12_errorgetapplyComment() throws Exception
    {
        mockMvc.perform(get("/api/comments/filter/apply/" + "9").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_13_getstateComment() throws Exception
    {
        mockMvc.perform(get("/api/comments/filter/state/" + "true").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_14_errorgetstateComment() throws Exception
    {
        mockMvc.perform(get("/api/comments/filter/state/" + "false").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

}
