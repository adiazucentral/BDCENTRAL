/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.FeeSchedule;
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
 * Prueba unitaria sobre el api rest /api/feeSchedules
 *
 * @version 1.0.0
 * @author enavas
 * @since 10/08/2017
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
public class FeeScheduleTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public FeeScheduleTest()
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
    public void test_01_getAllFeeScheduleNoContent() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab116", null);
        mockMvc.perform(get("/api/feeschedules").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertFeeSchedule() throws Exception
    {
        FeeSchedule feeSchedule = new FeeSchedule();

        feeSchedule.setName("prueba");
        feeSchedule.setInitialDate(new Date());
        feeSchedule.setEndDate(new Date());
        feeSchedule.setAutomatically(true);
        feeSchedule.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(feeSchedule);

        mockMvc.perform(post("/api/feeschedules")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("prueba")))
                .andExpect(jsonPath("$.automatically", is(true)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllFeeSchedule() throws Exception
    {
        mockMvc.perform(get("/api/feeschedules").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getFeeScheduleById() throws Exception
    {
        mockMvc.perform(get("/api/feeschedules/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("prueba")))
                .andExpect(jsonPath("$.automatically", is(true)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getFeeScheduleByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/feeschedules/filter/id/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_FeeScheduleByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/feeschedules/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_insertFeeScheduleError() throws Exception
    {
        FeeSchedule feeSchedule = new FeeSchedule();

        feeSchedule.setName("prueba");
        feeSchedule.setInitialDate(new Date());
        feeSchedule.setEndDate(new Date());
        feeSchedule.setAutomatically(true);
        feeSchedule.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(feeSchedule);

        mockMvc.perform(post("/api/feeschedules")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_08_updateFeeSchedule() throws Exception
    {
        FeeSchedule feeSchedule = new FeeSchedule();
        feeSchedule.setId(1);
        feeSchedule.setName("prueba2");
        feeSchedule.setInitialDate(new Date());
        feeSchedule.setEndDate(new Date());
        feeSchedule.setAutomatically(false);
        feeSchedule.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(feeSchedule);

        mockMvc.perform(put("/api/feeschedules")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("prueba2")))
                .andExpect(jsonPath("$.automatically", is(false)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_9_getFeeScheduleByName() throws Exception
    {
        mockMvc.perform(get("/api/feeschedules/filter/name/" + "prueba2").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("prueba2")))
                .andExpect(jsonPath("$.automatically", is(false)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateFeeScheduleError() throws Exception
    {
        FeeSchedule feeSchedule = new FeeSchedule();

        feeSchedule.setName("prueba2");
        feeSchedule.setInitialDate(new Date());
        feeSchedule.setEndDate(new Date());
        feeSchedule.setAutomatically(false);
        feeSchedule.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(feeSchedule);

        mockMvc.perform(put("/api/feeschedules")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }

}
