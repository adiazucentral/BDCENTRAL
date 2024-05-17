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
import net.cltech.enterprisent.domain.masters.test.CellularCounter;
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
public class CellularCounterTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public CellularCounterTest()
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
    public void test_01_getAllCellularCounterNoContent() throws Exception
    {
        mockMvc.perform(get("/api/cellularcounters").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertCellularCounter() throws Exception
    {
        CellularCounter cellularCounter = new CellularCounter();
        cellularCounter.setKey("A");
        cellularCounter.setText("Globulos Blancos");
        cellularCounter.setSum(true);
        cellularCounter.setType((short) 1);
        cellularCounter.getTest().setId(1);
        cellularCounter.getUser().setId(1);
        cellularCounter.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(cellularCounter);

        mockMvc.perform(post("/api/cellularcounters")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.key", is("A")))
                .andExpect(jsonPath("$.text", is("Globulos Blancos")))
                .andExpect(jsonPath("$.type", is(1)))
                .andExpect(jsonPath("$.sum", is(true)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllCellularCounter() throws Exception
    {
        mockMvc.perform(get("/api/cellularcounters").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getCellularCounterById() throws Exception
    {
        mockMvc.perform(get("/api/cellularcounters/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("A")))
                .andExpect(jsonPath("$.text", is("Globulos Blancos")))
                .andExpect(jsonPath("$.type", is(1)))
                .andExpect(jsonPath("$.sum", is(true)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getCellularCounterByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/cellularcounters/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_06_getCellularCounterByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/cellularcounters/filter/key/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_07_insertCellularCounterError() throws Exception
    {
        CellularCounter cellularCounter = new CellularCounter();
        cellularCounter.setKey("A");
        cellularCounter.setText("Globulos Blancos");
        cellularCounter.setSum(true);
        cellularCounter.setType((short) 6);
        cellularCounter.getTest().setId(1);
        cellularCounter.getUser().setId(1);
        cellularCounter.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(cellularCounter);

        mockMvc.perform(post("/api/cellularcounters")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_08_updateCellularCounter() throws Exception
    {
        CellularCounter cellularCounter = new CellularCounter();
        cellularCounter.setId(1);
        cellularCounter.setKey("B");
        cellularCounter.setText("Globulos Blancos");
        cellularCounter.setSum(true);
        cellularCounter.setType((short) 1);
        cellularCounter.getTest().setId(1);
        cellularCounter.getUser().setId(1);
        cellularCounter.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(cellularCounter);

        mockMvc.perform(put("/api/cellularcounters")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.key", is("B")))
                .andExpect(jsonPath("$.text", is("Globulos Blancos")))
                .andExpect(jsonPath("$.type", is(1)))
                .andExpect(jsonPath("$.sum", is(true)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_9_getCellularCounterByName() throws Exception
    {
        mockMvc.perform(get("/api/cellularcounters/filter/key/" + "B").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.key", is("B")))
                .andExpect(jsonPath("$.text", is("Globulos Blancos")))
                .andExpect(jsonPath("$.type", is(1)))
                .andExpect(jsonPath("$.sum", is(true)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_10_updateCellularCounterError() throws Exception
    {
        CellularCounter cellularCounter = new CellularCounter();
        cellularCounter.setId(1);
        cellularCounter.setKey("B");
        cellularCounter.setText("Globulos Blancos");
        cellularCounter.setSum(true);
        cellularCounter.setType((short) 0);
        cellularCounter.getTest().setId(1);
        cellularCounter.getUser().setId(1);
        cellularCounter.setState(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(cellularCounter);

        mockMvc.perform(put("/api/cellularcounters")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }
}
