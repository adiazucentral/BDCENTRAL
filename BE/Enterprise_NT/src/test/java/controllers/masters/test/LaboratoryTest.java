package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
 * Prueba sobre el api rest /api/laboratories
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/05/2017
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
public class LaboratoryTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public LaboratoryTest()
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
    public void test_01_noContent() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab40");
        mockMvc.perform(get("/api/laboratories").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_add() throws Exception
    {
        Laboratory add = new Laboratory();
        add.setName("CLtech laboratory");
        add.setCode(1);
        add.setAddress("Av siempreviva");
        add.setPhone("22553124");
        add.setContact("Contacto");
        add.setPath("c:\\path\\psm.txt");
        add.setType((short) 1);
        add.setUrl("http://interfaces:8080/MiddlewareManager");
        add.setEntry(true);
        add.setUser(new AuthorizedUser());
        add.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(add);

        mockMvc.perform(post("/api/laboratories/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("CLtech laboratory")))
                .andExpect(jsonPath("$.code", is(1)))
                .andExpect(jsonPath("$.address", is("Av siempreviva")))
                .andExpect(jsonPath("$.phone", is("22553124")))
                .andExpect(jsonPath("$.contact", is("Contacto")))
                .andExpect(jsonPath("$.type", is(1)))
                .andExpect(jsonPath("$.path", is("c:\\path\\psm.txt")));
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/laboratories").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_edit() throws Exception
    {
        Laboratory edit = new Laboratory();

        edit.setId(1);

        edit.setName("CLtech laboratory mod");
        edit.setCode(2);
        edit.setAddress("Av siempreviva mod");
        edit.setPhone("22553124 mod");
        edit.setContact("Contacto mod");
        edit.setPath("c:/path/psmod.txt");
        edit.setType((short) 2);
        edit.setState(false);
        edit.setUrl("http://interfaces:8080/MiddlewareManager");
        edit.setEntry(true);

        edit.setUser(new AuthorizedUser());
        edit.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(edit);

        mockMvc.perform(put("/api/laboratories/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("CLtech laboratory mod")))
                .andExpect(jsonPath("$.code", is(2)))
                .andExpect(jsonPath("$.address", is("Av siempreviva mod")))
                .andExpect(jsonPath("$.phone", is("22553124 mod")))
                .andExpect(jsonPath("$.contact", is("Contacto mod")))
                .andExpect(jsonPath("$.type", is(2)))
                .andExpect(jsonPath("$.path", is("c:/path/psmod.txt")))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()))
                .andExpect(jsonPath("$.state", is(false)));
    }

    @Test
    public void test_05_duplicateFields() throws Exception
    {
        Laboratory bean = new Laboratory();
        bean.setName("CLtech laboratory mod");
        bean.setCode(2);
        bean.setType((short) 1);

        bean.setUser(new AuthorizedUser());
        bean.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bean);
        mockMvc.perform(post("/api/laboratories")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name", "1|code")));
    }

    @Test
    public void test_06_emptyField() throws Exception
    {
        Laboratory bean = new Laboratory();

        bean.setUser(new AuthorizedUser());
        bean.setType((short) 1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bean);

        mockMvc.perform(post("/api/laboratories/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|userId", "0|code")));
    }

    @Test
    public void test_07_invalidType() throws Exception
    {
        Laboratory bean = new Laboratory();
//        bean.setCode(5);
        bean.setName("Referencia");
        bean.setType((short) 3);

        bean.setUser(new AuthorizedUser());
        bean.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bean);

        mockMvc.perform(post("/api/laboratories/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("3|type", "0|code")));
    }

    @Test
    public void test_08_listProcessing() throws Exception
    {
        Laboratory edit = new Laboratory();

        edit.setId(1);

        edit.setName("CLtech laboratory mod");
        edit.setCode(2);
        edit.setAddress("Av siempreviva mod");
        edit.setPhone("22553124 mod");
        edit.setContact("Contacto mod");
        edit.setPath("c:/path/psmod.txt");
        edit.setType((short) 2);
        edit.setState(true);
        edit.setUrl("http://interfaces:8080/MiddlewareManager");
        edit.setEntry(true);

        edit.setUser(new AuthorizedUser());
        edit.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(edit);

        mockMvc.perform(put("/api/laboratories/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/laboratories/listprocessing")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url", is("http://interfaces:8080/MiddlewareManager")));
    }

}
