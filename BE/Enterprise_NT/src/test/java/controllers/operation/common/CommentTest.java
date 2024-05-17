/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.operation.microbiology.CommentMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/comment
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/03/2018
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
public class CommentTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public CommentTest()
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
        user.setBranch(1);
        user.setId(-1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_listCommentsOrderNoContent() throws Exception
    {
        TestScript.createInitialOrders();

        mockMvc.perform(get("/api/comments/order/201709080001")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_02_listCommentsPatientNoContent() throws Exception
    {
        mockMvc.perform(get("/api/comments/patient/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_03_listCommentsMicrobiologyTestNoContent() throws Exception
    {
        mockMvc.perform(get("/api/comments/microbiology/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_04_listCommentsMicrobiologySampleNoContent() throws Exception
    {
        mockMvc.perform(get("/api/comments/microbiology/order/201709080001/sample/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_05_insertCommentsOrder() throws Exception
    {
        List<CommentOrder> comments = new ArrayList<>();
        CommentOrder comment1 = new CommentOrder();
        comment1.setIdRecord(201709080001L);
        comment1.setType((short) 1);
        comment1.setComment("Comentario de la orden 1");
        comment1.setState((short) 1);
        comment1.getUser().setId(1);

        CommentOrder comment2 = new CommentOrder();
        comment2.setIdRecord(201709080001L);
        comment2.setType((short) 1);
        comment2.setComment("Comentario de la orden 2");
        comment2.setState((short) 1);
        comment2.getUser().setId(1);

        comments.add(comment1);
        comments.add(comment2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comments);

        mockMvc.perform(put("/api/comments/order")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_06_insertCommentsPatient() throws Exception
    {
        List<CommentOrder> comments = new ArrayList<>();
        CommentOrder comment1 = new CommentOrder();
        comment1.setIdRecord(1L);
        comment1.setType((short) 2);
        comment1.setComment("Diagnostico 1");
        comment1.setState((short) 1);
        comment1.getUser().setId(1);

        CommentOrder comment2 = new CommentOrder();
        comment2.setIdRecord(1L);
        comment2.setType((short) 2);
        comment2.setComment("Diagnostico 2");
        comment2.setState((short) 1);
        comment2.getUser().setId(1);

        comments.add(comment1);
        comments.add(comment2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comments);

        mockMvc.perform(put("/api/comments/order")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_07_insertCommentsMicrobiology() throws Exception
    {
        List<CommentMicrobiology> comments = new ArrayList<>();
        CommentMicrobiology comment1 = new CommentMicrobiology();
        comment1.setOrder(201709080001L);
        comment1.setIdSample(1);
        comment1.setIdTest(8);
        comment1.setComment("Comentario de microbiologia 1");
        comment1.setState((short) 1);
        comment1.getUser().setId(1);

        CommentMicrobiology comment2 = new CommentMicrobiology();
        comment2.setOrder(201709080001L);
        comment2.setIdSample(1);
        comment2.setIdTest(8);
        comment2.setComment("Comentario de microbiologia 2");
        comment2.setState((short) 1);
        comment2.getUser().setId(1);

        comments.add(comment1);
        comments.add(comment2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comments);

        mockMvc.perform(put("/api/comments/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_08_listCommentsOrder() throws Exception
    {
        TestScript.createInitialOrders();

        mockMvc.perform(get("/api/comments/order/201709080001")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].idRecord", containsInAnyOrder(201709080001L, 201709080001L)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$[*].comment", containsInAnyOrder("Comentario de la orden 1", "Comentario de la orden 2")))
                .andExpect(jsonPath("$[*].user.id", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_09_listCommentsPatient() throws Exception
    {
        mockMvc.perform(get("/api/comments/patient/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].idRecord", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(2, 2)))
                .andExpect(jsonPath("$[*].comment", containsInAnyOrder("Diagnostico 2", "Diagnostico 1")))
                .andExpect(jsonPath("$[*].user.id", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_10_listCommentsMicrobiologyTest() throws Exception
    {
        mockMvc.perform(get("/api/comments/microbiology/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].order", containsInAnyOrder(201709080001L, 201709080001L)))
                .andExpect(jsonPath("$[*].idTest", containsInAnyOrder(8, 8)))
                .andExpect(jsonPath("$[*].idSample", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$[*].comment", containsInAnyOrder("Comentario de microbiologia 1", "Comentario de microbiologia 2")))
                .andExpect(jsonPath("$[*].user.id", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_11_listCommentsMicrobiologySample() throws Exception
    {
        mockMvc.perform(get("/api/comments/microbiology/order/201709080001/sample/1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].order", containsInAnyOrder(201709080001L, 201709080001L)))
                .andExpect(jsonPath("$[*].idTest", containsInAnyOrder(8, 8)))
                .andExpect(jsonPath("$[*].idSample", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$[*].comment", containsInAnyOrder("Comentario de microbiologia 1", "Comentario de microbiologia 2")))
                .andExpect(jsonPath("$[*].user.id", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_12_updateCommentMicrobiology() throws Exception
    {
        List<CommentMicrobiology> comments = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(get("/api/comments/microbiology/order/201709080001/test/8").header("Authorization", token))), CommentMicrobiology.class);
        comments.get(0).setComment("Comentario de Microbiologia Actualizado");
        comments.get(0).setState((short) 2);
        comments.get(1).setState((short) 3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comments);

        mockMvc.perform(put("/api/comments/microbiology")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_13_listCommentsMicrobiologyTest() throws Exception
    {
        mockMvc.perform(get("/api/comments/microbiology/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].order", containsInAnyOrder(201709080001L)))
                .andExpect(jsonPath("$[*].idTest", containsInAnyOrder(8)))
                .andExpect(jsonPath("$[*].idSample", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].comment", containsInAnyOrder("Comentario de Microbiologia Actualizado")))
                .andExpect(jsonPath("$[*].user.id", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_14_updateCommentOrder() throws Exception
    {
        List<CommentOrder> comments = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(get("/api/comments/order/201709080001").header("Authorization", token))), CommentOrder.class);
        comments.get(0).setComment("Comentario de la Orden Actualizado");
        comments.get(0).setState((short) 2);
        comments.get(1).setState((short) 3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(comments);

        mockMvc.perform(put("/api/comments/order")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @org.junit.Test
    public void test_15_listCommentsMicrobiologyTest() throws Exception
    {
        mockMvc.perform(get("/api/comments/order/201709080001")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].idRecord", containsInAnyOrder(201709080001L)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].comment", containsInAnyOrder("Comentario de la Orden Actualizado")))
                .andExpect(jsonPath("$[*].user.id", Matchers.notNullValue()));
    }

    @org.junit.Test
    public void test_16_listCommentsMicrobiologySample() throws Exception
    {
        mockMvc.perform(get("/api/comments/microbiology/tracking/order/201709080001/test/8")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[*].order", containsInAnyOrder(201709080001L, 201709080001L, 201709080001L, 201709080001L)))
                .andExpect(jsonPath("$[*].idTest", containsInAnyOrder(8, 8, 8, 8)))
                .andExpect(jsonPath("$[*].idSample", containsInAnyOrder(1, 1, 1, 1)))
                .andExpect(jsonPath("$[*].state", containsInAnyOrder(1, 1, 2, 3)))
                .andExpect(jsonPath("$[*].user.id", Matchers.notNullValue()));
    }
}
