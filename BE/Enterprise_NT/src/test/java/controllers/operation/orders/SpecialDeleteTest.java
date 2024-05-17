/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
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
 * Prueba unitaria sobre el api rest /api/specialdeletes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/09/2017
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
public class SpecialDeleteTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;
    List<Question> questions;

    public SpecialDeleteTest()
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
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_specialDeleteOrder() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>());
        Reason reason = new Reason();
        reason.setDeleteType(1);
        reason.setInit(201710040001L);
        reason.setEnd(201710040001L);
        reason.getMotive().setId(1);
        reason.setComment("...");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(put("/api/specialdeletes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201710040001L)))
                .andExpect(jsonPath("$[0].state", is(0)));
    }

    @org.junit.Test
    public void test_02_specialDeleteOrderNoContent() throws Exception
    {
        Reason reason = new Reason();
        reason.setDeleteType(1);
        reason.setInit(201710040001L);
        reason.setEnd(201710040001L);
        reason.getMotive().setId(1);
        reason.setComment("...");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(put("/api/specialdeletes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_03_specialDeleteTestComplete() throws Exception
    {
        Reason reason = new Reason();
        reason.setDeleteType(3);
        reason.setInit(201710040003L);
        reason.setEnd(201710040003L);
        reason.getMotive().setId(1);
        reason.setComment("...");
        reason.getTests().add(new Test(6));
        reason.getTests().add(new Test(7));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(put("/api/specialdeletes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201710040003L)))
                .andExpect(jsonPath("$[0].tests[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].tests[0].id", is(6)));
    }

    @org.junit.Test
    public void test_04_specialDeleteTestCompleteNoContent() throws Exception
    {
        Reason reason = new Reason();
        reason.setDeleteType(3);
        reason.setInit(201710040003L);
        reason.setEnd(201710040003L);
        reason.getMotive().setId(1);
        reason.setComment("...");
        reason.getTests().add(new Test(6));
        reason.getTests().add(new Test(7));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(put("/api/specialdeletes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_05_specialDeleteTestNoReportedFailed() throws Exception
    {
        Reason reason = new Reason();
        reason.setDeleteType(4);
        reason.setInit(201710040004L);
        reason.setEnd(201710040004L);
        reason.getMotive().setId(1);
        reason.setComment("...");
        reason.getTests().add(new Test(6));
        reason.getTests().add(new Test(8));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(put("/api/specialdeletes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("3|tests|201710040004")));
    }

    @org.junit.Test
    public void test_06_specialDeleteTestNoReported() throws Exception
    {
        Reason reason = new Reason();
        reason.setDeleteType(4);
        reason.setInit(201710040002L);
        reason.setEnd(201710040002L);
        reason.getMotive().setId(1);
        reason.setComment("...");
        reason.getTests().add(new Test(6));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(put("/api/specialdeletes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201710040002L)))
                .andExpect(jsonPath("$[0].tests[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].tests[0].id", is(6)));
    }

    @org.junit.Test
    public void test_07_specialDeleteTestCompleteNoContent() throws Exception
    {
        Reason reason = new Reason();
        reason.setDeleteType(4);
        reason.setInit(201710040002L);
        reason.setEnd(201710040002L);
        reason.getMotive().setId(1);
        reason.setComment("...");
        reason.getTests().add(new Test(6));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

        mockMvc.perform(put("/api/specialdeletes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_08_specialDeleteResult() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab57 SET lab57c8 = 2 WHERE lab22c1 = 201710040004 AND lab39c1 = 8");

        Reason reason = new Reason();
        reason.setDeleteType(2);
        reason.setInit(201710040004L);
        reason.setEnd(201710040004L);
        reason.getMotive().setId(1);
        reason.setComment("...");
        reason.getTests().add(new Test(8));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

//        mockMvc.perform(put("/api/specialdeletes")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].orderNumber", is(201710040004L)))
//                .andExpect(jsonPath("$[0].tests[*]", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].tests[0].id", is(8)));
    }

    @org.junit.Test
    public void test_09_specialDeleteResultNoContent() throws Exception
    {
        Reason reason = new Reason();
        reason.setDeleteType(2);
        reason.setInit(201710040004L);
        reason.setEnd(201710040004L);
        reason.getMotive().setId(1);
        reason.setComment("...");
        reason.getTests().add(new Test(8));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(reason);

//        mockMvc.perform(put("/api/specialdeletes")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(jsonContent))
//                .andExpect(status().isNoContent());
    }
}
