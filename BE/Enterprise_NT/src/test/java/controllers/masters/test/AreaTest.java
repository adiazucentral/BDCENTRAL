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
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
 * Prueba unitaria sobre el api rest /api/areas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/04/2017
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
public class AreaTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public AreaTest()
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
    public void test_01_insertArea() throws Exception
    {
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab43 RESTART IDENTITY;", null);
        Area area = new Area();
        area.setOrdering(Short.valueOf("1"));
        area.setAbbreviation("A1");
        area.setName("Area 1");
        area.setColor("#000000");
        area.setPartialValidation(false);
        area.getType().setId(2);
        area.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(area);

        mockMvc.perform(post("/api/areas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.ordering", is(1)))
                .andExpect(jsonPath("$.abbreviation", is("A1")))
                .andExpect(jsonPath("$.name", is("Area 1")))
                .andExpect(jsonPath("$.color", is("#000000")))
                .andExpect(jsonPath("$.partialValidation", is(false)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_02_getAllArea() throws Exception
    {
        mockMvc.perform(get("/api/areas").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_03_getAreaById() throws Exception
    {
        mockMvc.perform(get("/api/areas/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getAreaByOrdering() throws Exception
    {
        mockMvc.perform(get("/api/areas/filter/ordering/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_05_getAreaByAbbr() throws Exception
    {
        mockMvc.perform(get("/api/areas/filter/abbr/" + "A1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getAreaByName() throws Exception
    {
        mockMvc.perform(get("/api/areas/filter/name/" + "Area 1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_07_getAreaByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/areas/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getAreaByOrderingNoContent() throws Exception
    {
        mockMvc.perform(get("/api/areas/filter/ordering/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getAreaByAbbrNoContent() throws Exception
    {
        mockMvc.perform(get("/api/areas/filter/abbr/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_getAreaByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/areas/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_insertAreaError() throws Exception
    {
        Area area = new Area();
        area.setOrdering(Short.valueOf("0"));
        area.setAbbreviation("S0");
        area.setName("");
        area.setColor("#000000");
        area.setPartialValidation(false);
        area.getType().setId(2);
        area.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(area);

        mockMvc.perform(post("/api/areas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_12_updateArea() throws Exception
    {
        Area area = new Area();
        area.setId(1);
        area.setOrdering(Short.valueOf("1"));
        area.setAbbreviation("A");
        area.setName("AREA");
        area.setColor("#000000");
        area.setState(true);
        area.setPartialValidation(false);
        area.getType().setId(3);
        area.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(area);

        mockMvc.perform(put("/api/areas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.ordering", is(1)))
                .andExpect(jsonPath("$.abbreviation", is("A")))
                .andExpect(jsonPath("$.name", is("AREA")))
                .andExpect(jsonPath("$.color", is("#000000")))
                .andExpect(jsonPath("$.partialValidation", is(false)))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_13_updateAreaError() throws Exception
    {
        Area area = new Area();
        area.setId(-1);
        area.setOrdering(Short.valueOf("0"));
        area.setAbbreviation("S0");
        area.setName("SECCION CERO");
        area.setColor("#000000");
        area.setState(true);
        area.setPartialValidation(false);
        area.getType().setId(3);
        area.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(area);

        mockMvc.perform(put("/api/areas")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_14_getAllAreaByState() throws Exception
    {
        mockMvc.perform(get("/api/areas/filter/state/" + true).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_15_getAllAreaByStateNoContent() throws Exception
    {
        mockMvc.perform(get("/api/areas/filter/state/" + false).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_16_getAllAreasDashBoard() throws Exception
    {
        mockMvc.perform(get("/api/dashboard/Get_Sections").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("AREA")));
    }
}
