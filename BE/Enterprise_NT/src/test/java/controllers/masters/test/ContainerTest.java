package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.Arrays;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Container;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
 * Prueba unitaria sobre el api rest /api/containers
 *
 * @version 1.0.0
 * @author enavas
 * @since 16/04/2017
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

public class ContainerTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ContainerTest()
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
    public void test_01_getnotallContainer() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab56", null);
        mockMvc.perform(get("/api/containers").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_addContainer() throws Exception
    {

        Container container = new Container();
        AuthorizedUser usu = new AuthorizedUser();
        container.setId(0);
        container.setImage("");
        container.setName("prueba");
        container.setPriority(1);
        container.setState(true);
        usu.setId(1);
        container.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(container);

        mockMvc.perform(post("/api/containers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(container.getName())));
    }

    @Test
    public void test_03_getAllContainer() throws Exception
    {
        mockMvc.perform(get("/api/containers").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_erroraddContainer() throws Exception
    {

        Container container = new Container();
        AuthorizedUser usu = new AuthorizedUser();

        container.setId(0);
        container.setImage("");
        container.setName("prueba");
        container.setPriority(1);
        container.setState(true);
        usu.setId(1);
        container.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(container);

        mockMvc.perform(post("/api/containers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void test_05_modifyContainer() throws Exception
    {

        Container container = new Container();
        AuthorizedUser usu = new AuthorizedUser();

        container.setId(1);
        container.setImage("");
        container.setName("prueba");
        container.setPriority(1);
        container.setState(true);
        usu.setId(1);
        container.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(container);

        mockMvc.perform(put("/api/containers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(container.getName())));
    }

    @Test
    public void test_06_errormodifyContainer() throws Exception
    {

        Container container = new Container();
        AuthorizedUser usu = new AuthorizedUser();

        container.setId(2);
        container.setImage("");
        container.setName("prueba");
        container.setPriority(1);
        container.setState(true);
        usu.setId(1);
        container.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(container);

        mockMvc.perform(put("/api/containers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void test_07_getidContainer() throws Exception
    {
        mockMvc.perform(get("/api/containers/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_08_errorgetidContainer() throws Exception
    {
        mockMvc.perform(get("/api/containers/filter/id/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getnameContainer() throws Exception
    {
        mockMvc.perform(get("/api/containers/filter/name/" + "prueba").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_10_errorgetnameContainer() throws Exception
    {
        mockMvc.perform(get("/api/containers/filter/name/" + "prueba2").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_updatePriority() throws Exception
    {
        Container container = new Container();
        AuthorizedUser usu = new AuthorizedUser();

        container.setId(1);
        container.setPriority(2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(Arrays.asList(container));

        mockMvc.perform(patch("/api/containers/priority")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}
