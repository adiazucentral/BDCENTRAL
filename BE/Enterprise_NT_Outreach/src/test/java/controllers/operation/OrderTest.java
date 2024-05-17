/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestAppContext;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.operation.Filter;
import net.cltech.outreach.tools.Constants;
import net.cltech.outreach.tools.JWT;
import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/orders
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/05/2018
 * @see Creación
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String tokenPh;
    private String tokenAc;
    private String tokenP;

    public OrderTest()
    {
    }

    @Before
    public void setUp() throws Exception
    {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        //--Generar token de medico
        TestScript.execTestUpdateScript("UPDATE lab19 set lab19c17='eacunar', lab19c18 = 'deLN3a9Z7Xs='", null);
        AuthorizedUser userPh = new AuthorizedUser();
        userPh.setId(1);
        userPh.setName("Edwin");
        userPh.setLastName("Acuña");
        userPh.setUserName("eacunar");
        userPh.setPassword("123456");
        userPh.setEmail("eacunar@cltech.net");
        userPh.setType(Constants.PHYSICIAN);
        tokenPh = JWT.generate(userPh, 1);

        //--Generar token de paciente
        TestScript.execTestUpdateScript("DELETE FROM lab21 WHERE lab21c1 = 1", null);
        TestScript.execTestUpdateScript("INSERT INTO lab21 (lab21c1,lab21c2,lab21c3,lab21c4,lab21c5,lab21c6,lab80c1,lab21c7,lab21c8,lab21c9,lab21c10,lab21c11,lab21c12,lab04c1,lab08c1,lab54c1,lab21c18,lab21c19) VALUES (1, '10IUEsRP64sIZgspdLsKYA==', 'NSjDTJu014Q=', '+5j1V2yNc8Q=', 'SdbsT2RnFj4=', 'K5/kmGNsFgs=', 7, '1970-04-26 18:37:00.051', 'a@b.c', '10', 50, NULL, '2017-09-12 16:38:58.075', 1, 1, 1, 'cltech', 'deLN3a9Z7Xs=')", null);
        AuthorizedUser userP = new AuthorizedUser();
        userP.setId(1);
        userP.setName("Carlos");
        userP.setLastName("Martin");
        userP.setUserName("cltech");
        userP.setPassword("123456");
        userP.setEmail("a@b.c");
        userP.setType(Constants.PATIENT);
        tokenP = JWT.generate(userP, 1);

        //--Generar token de cliente
        AuthorizedUser userAc = new AuthorizedUser();
        userAc.setId(1);
        userAc.setName("CAMC");
        userAc.setUserName("cmartincl");
        userAc.setPassword("12345");
        userAc.setEmail("abc@cltech.net");
        userAc.setType(Constants.ACCOUNT);
        tokenAc = JWT.generate(userAc, 1);

        //--Generar token de usuario
        AuthorizedUser userU = new AuthorizedUser();
        userU.setId(1);
        userU.setName("Carlos");
        userU.setLastName("Martin");
        userU.setUserName("cmartin");
        userU.setPassword("12345");
        userU.setEmail("a@b.c");
        userU.setType(Constants.USERLIS);
    }

    @org.junit.Test
    public void test_01_ordersForPhysicianDate() throws Exception
    {
        TestScript.createInitialOrders();
        Filter filter = new Filter();
        filter.setDateNumber(20170908);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenPh)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(2)));
    }

    @org.junit.Test
    public void test_02_ordersForPhysicianDateNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setDateNumber(20170909);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenPh)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_03_ordersForPhysicianHistory() throws Exception
    {
        Filter filter = new Filter();
        filter.setDocumentType(1);
        filter.setPatientId("1007100108");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenPh)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(5)));
    }

    @org.junit.Test
    public void test_04_ordersForPhysicianHistoryNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setDocumentType(2);
        filter.setPatientId("1007100108");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenPh)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_05_ordersForPatientOrder() throws Exception
    {
        Filter filter = new Filter();
        filter.setOrder(201709080001L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenP)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(1)));
    }

    @org.junit.Test
    public void test_06_ordersForPatientOrderNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setOrder(201710040010L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenP)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_07_ordersForAccountPatient() throws Exception
    {
        Filter filter = new Filter();
        filter.setName1("Carlos");
        filter.setName2("Alberto");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenAc)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(5)));
    }

    @org.junit.Test
    public void test_08_ordersForAccountPatient() throws Exception
    {
        Filter filter = new Filter();
        filter.setLastName("Martin");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenAc)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(5)));
    }

    @org.junit.Test
    public void test_09_ordersForAccountPatientNoContent() throws Exception
    {
        Filter filter = new Filter();
        filter.setLastName("Mart");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(filter);

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenAc)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_10_ordersForUserAll() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(new Filter());

        mockMvc.perform(patch("/api/orders/filter")
                .header("Authorization", tokenAc)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(5)));
    }

}
