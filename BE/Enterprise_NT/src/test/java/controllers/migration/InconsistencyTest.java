/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.migration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.dao.impl.postgresql.masters.demographic.BranchDaoPostgreSQL;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.operation.orders.Order;
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
 * Prueba unitaria sobre el api rest /api/inconsistencies
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/11/2017
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
public class InconsistencyTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private BranchDaoPostgreSQL branchDaoPostgreSQL;
    private MockMvc mockMvc;
    private String token;
    List<Question> questions;

    public InconsistencyTest()
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
    public void test_01_insertOrderError() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.createInitialBranch(new ArrayList<>(Arrays.asList("1")));
        TestScript.execTestUpdateScript("DELETE FROM lab33", null);
        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = '0' WHERE lab98c1 = 'DemograficoInconsistensias'", null);
        TestScript.execTestUpdateScript("UPDATE lab98 SET lab98c2 = 'False' WHERE lab98c1 = 'ManejoTipoDocumento'", null);
        Order order = new Order();
        order.setOrderNumber(201711090001L);
        order.setCreatedDateShort(20171109);
        order.getType().setId(1);
        order.getBranch().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);

        mockMvc.perform(post("/api/inconsistencies/order")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("1|Order not valid must contains patient")));
    }

    @org.junit.Test
    public void test_02_insertOrderWithInconsistency() throws Exception
    {
        Order order = new Order();
        order.setOrderNumber(201711090002L);
        order.setCreatedDateShort(20171109);
        order.getType().setId(1);
        order.getType().setCode("R");
        order.getPatient().setPatientId("1007100108");
        order.getPatient().setName1("Carl");
        order.getPatient().setName2("Alber");
        order.getPatient().setLastName("Mart");
        order.getPatient().setSurName("Cast");
        order.getPatient().getSex().setId(7);
        order.getPatient().setBirthday(new Date(1506371297332L));
        order.getPatient().setEmail("cmartin.cltech@gmail.com");
        order.getBranch().setId(1);

        Test test = new Test();
        test.setId(1);
        test.setTestType((short) 0);
        test.getArea().setId(2);
        test.setRate(new Rate(1));
        test.setPrice(new BigDecimal(0));
        Laboratory laboratory = new Laboratory();
        laboratory.setId(1);
        test.setLaboratory(laboratory);
        order.getTests().add(test);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(order);

        mockMvc.perform(post("/api/inconsistencies/order")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.orderNumber", is(201711090002L)));
    }

    @org.junit.Test
    public void test_03_getInconsistencyToday() throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        mockMvc.perform(get("/api/inconsistencies/init/" + timestamp.getTime() + "/end/" + timestamp.getTime()).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].orderNumber", is(201711090002L)))
                .andExpect(jsonPath("$[0].patientHIS.patientId", is("1007100108")))
                .andExpect(jsonPath("$[0].patientHIS.name1", is("Carl")))
                .andExpect(jsonPath("$[0].patientHIS.name2", is("Alber")))
                .andExpect(jsonPath("$[0].patientHIS.lastName", is("Mart")))
                .andExpect(jsonPath("$[0].patientHIS.surName", is("Cast")))
                .andExpect(jsonPath("$[0].patientLIS.patientId", is("1007100108")))
                .andExpect(jsonPath("$[0].patientLIS.name1", is("Carlos")))
                .andExpect(jsonPath("$[0].patientLIS.name2", is("Alberto")))
                .andExpect(jsonPath("$[0].patientLIS.lastName", is("Martin")))
                .andExpect(jsonPath("$[0].patientLIS.surName", is("Castro")))
                .andExpect(jsonPath("$[0].inconsistencies", is("name1,name2,lastName,surName,birthday,")));
    }

    @org.junit.Test
    public void test_04_resolveInconsistency() throws Exception
    {
        mockMvc.perform(put("/api/inconsistencies/resolve/order/201711090002/lis/false").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));
    }

    @org.junit.Test
    public void test_05_resolveInconsistencyNoExist() throws Exception
    {
        mockMvc.perform(put("/api/inconsistencies/resolve/order/201711090002/lis/false").header("Authorization", token))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errorFields[0]", is("0|inconsistency")));
    }
}
