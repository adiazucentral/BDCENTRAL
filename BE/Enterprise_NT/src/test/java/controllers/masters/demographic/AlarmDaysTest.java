/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/demographics/alarmDays
 *
 * @version 1.0.0
 * @author mmunoz
 * @since 14/08/2017
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
public class AlarmDaysTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public AlarmDaysTest()
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
    public void test_01_noContentD() throws Exception
    {
        TestScript.deleteData("lab39");
        mockMvc.perform(get("/api/demographics/alarmdays/filter/demographic/1/demographicitem/1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertD() throws Exception
    {

        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, 6, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (2, 2, '1002', 'GLU', 'GLUCOSA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 0, 0, 0, 0, 0, 2, 4, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (3, 3, '1003', 'COL', 'COLESTEROL', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, 5, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (4, 4, '1004', 'TRI', 'TRIGLICERIDOS', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 7, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (5, 5, '101', 'CH', 'CUADRO HEMATICO', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 1, 0, 0, 0, 1, 1, NULL)", null);

        net.cltech.enterprisent.domain.masters.test.AlarmDays alarmDays = new net.cltech.enterprisent.domain.masters.test.AlarmDays();
        alarmDays.getDemographic().setId(1);
        alarmDays.getDemographic().setDemographicItem(1);
        alarmDays.getTest().add(new TestBasic(1));
        alarmDays.getTest().get(alarmDays.getTest().size() - 1).setDeltacheckDays(6);

        alarmDays.getTest().add(new TestBasic(3));
        alarmDays.getTest().get(alarmDays.getTest().size() - 1).setDeltacheckDays(5);

        alarmDays.getTest().add(new TestBasic(5));
        alarmDays.getTest().get(alarmDays.getTest().size() - 1).setDeltacheckDays(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(alarmDays);

        mockMvc.perform(put("/api/demographics/alarmdays/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    public void test_03_okD() throws Exception
    {
        mockMvc.perform(get("/api/demographics/alarmdays/filter/demographic/1/demographicitem/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 3, 4, 5)))
                .andExpect(jsonPath("$[*].deltacheckDays", containsInAnyOrder(6, 5, 0, 1)));

    }

    @Test
    public void test_04_deleteD() throws Exception
    {
        mockMvc.perform(delete("/api/demographics/alarmdays/filter/demographic/1/demographicitem/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

    }

    @Test
    public void test_5_noContentD() throws Exception
    {
        mockMvc.perform(get("/api/demographics/alarmdays/filter/demographic/1/demographicitem/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 3, 4, 5)))
                .andExpect(jsonPath("$[*].deltacheckDays", containsInAnyOrder(0, 0, 0, 0)));

    }

   

}
