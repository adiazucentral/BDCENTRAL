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
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.Worksheet;
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
 * Prueba unitaria sobre el api rest /api/worksheet
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/07/2017
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
public class WorksheetTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public WorksheetTest()
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
    public void test_01_getAllWorksheetNoContent() throws Exception
    {
        mockMvc.perform(get("/api/worksheets").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertWorksheet() throws Exception
    {
        TestBasic test1 = new TestBasic();
        test1.setId(1);
        test1.setSelected(true);

        TestBasic test2 = new TestBasic();
        test2.setId(2);
        test2.setSelected(true);

        Worksheet worksheet = new Worksheet();
        worksheet.setName("Microbiologia");
        worksheet.setExclusive(false);
        worksheet.setMicrobiology(true);
        worksheet.getUser().setId(1);
        worksheet.getTests().add(test1);
        worksheet.getTests().add(test2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(worksheet);

        mockMvc.perform(post("/api/worksheets")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Microbiologia")))
                .andExpect(jsonPath("$.microbiology", is(true)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllWorksheet() throws Exception
    {
        mockMvc.perform(get("/api/worksheets").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getWorksheetById() throws Exception
    {
        mockMvc.perform(get("/api/worksheets/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Microbiologia")))
                .andExpect(jsonPath("$.microbiology", is(true)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getWorksheetByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/worksheets/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_getWorksheetByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/worksheets/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_insertWorksheetError() throws Exception
    {
        Worksheet worksheet = new Worksheet();
        worksheet.setName("Microbiologia");
        worksheet.setExclusive(false);
        worksheet.setMicrobiology(true);
        worksheet.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(worksheet);

        mockMvc.perform(post("/api/worksheets")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_08_updateWorksheet() throws Exception
    {
        TestBasic test1 = new TestBasic();
        test1.setId(1);
        test1.setSelected(true);

        TestBasic test2 = new TestBasic();
        test2.setId(2);
        test2.setSelected(true);

        Worksheet worksheet = new Worksheet();
        worksheet.setId(1);
        worksheet.setName("Quimica");
        worksheet.setType((short) 1);
        worksheet.setOrientation((short) 1);
        worksheet.setExclusive(true);
        worksheet.setMicrobiology(false);
        worksheet.getUser().setId(1);
        worksheet.getTests().add(test1);
        worksheet.getTests().add(test2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(worksheet);

        mockMvc.perform(put("/api/worksheets")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Quimica")))
                .andExpect(jsonPath("$.type", is(1)))
                .andExpect(jsonPath("$.orientation", is(1)))
                .andExpect(jsonPath("$.exclusive", is(true)))
                .andExpect(jsonPath("$.microbiology", is(false)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_9_getWorksheetByName() throws Exception
    {
        mockMvc.perform(get("/api/worksheets/filter/name/" + "Quimica").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Quimica")))
                .andExpect(jsonPath("$.type", is(1)))
                .andExpect(jsonPath("$.orientation", is(1)))
                .andExpect(jsonPath("$.exclusive", is(true)))
                .andExpect(jsonPath("$.microbiology", is(false)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateWorksheetError() throws Exception
    {
        Worksheet worksheet = new Worksheet();
        worksheet.setId(1);
        worksheet.setName("Quimica");
        worksheet.setType((short) 1);
        worksheet.setOrientation((short) 1);
        worksheet.setExclusive(true);
        worksheet.setMicrobiology(false);
        worksheet.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(worksheet);

        mockMvc.perform(put("/api/worksheets")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }
}
