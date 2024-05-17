package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
 * Prueba sobre el api rest /api/ordertypes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 16/05/2017
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
public class OrderTypeTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public OrderTypeTest()
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
    public void test_01_initContent() throws Exception
    {
        mockMvc.perform(get("/api/ordertypes").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void test_02_add() throws Exception
    {
        OrderType add = new OrderType();
        add.setName("VIP");
        add.setCode("V");
        add.setColor("FFFFF");

        add.setUser(new AuthorizedUser());
        add.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(add);

        mockMvc.perform(post("/api/ordertypes/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.state", is(true)))
                .andExpect(jsonPath("$.name", is("VIP")))
                .andExpect(jsonPath("$.color", is("FFFFF")));
    }

    @Test
    public void test_03_edit() throws Exception
    {
        OrderType edit = new OrderType();

        edit.setId(6);
        edit.setCode("L");
        edit.setName("VIP +");
        edit.setState(false);
        edit.setColor("00000");

        edit.setUser(new AuthorizedUser());
        edit.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(edit);

        mockMvc.perform(put("/api/ordertypes/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("VIP +")))
                .andExpect(jsonPath("$.code", is("L")))
                .andExpect(jsonPath("$.state", is(false)))
                .andExpect(jsonPath("$.color", is("00000")))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }

    @Test
    public void test_05_duplicateFields() throws Exception
    {
        OrderType duplicate = new OrderType();
        duplicate.setName("VIP +");
        duplicate.setCode("R");

        duplicate.setUser(new AuthorizedUser());
        duplicate.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(duplicate);

        mockMvc.perform(post("/api/ordertypes/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name", "1|code")));
    }

    @Test
    public void test_05_emptyField() throws Exception
    {
        OrderType bean = new OrderType();

        bean.setUser(new AuthorizedUser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bean);

        mockMvc.perform(post("/api/ordertypes/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|userId", "0|code")));
    }

    @Test
    public void test_edit_staticTypes() throws Exception
    {
        OrderType edit = new OrderType();

        edit.setId(1);
        edit.setCode("Z");
        edit.setName("Edit");
        edit.setState(false);
        edit.setColor("red");

        edit.setUser(new AuthorizedUser());
        edit.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(edit);

        mockMvc.perform(put("/api/ordertypes/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Edit")))
                .andExpect(jsonPath("$.state", is(true)))
                .andExpect(jsonPath("$.color", is("red")))
                .andExpect(jsonPath("$.code", is("R")))
                .andExpect(jsonPath("$.lastTransaction", notNullValue()));
    }
    
    @Test
    public void test_06_getOrderType() throws Exception
    {   
        TestScript.createInitialOrders();
        mockMvc.perform(get("/api/ordertypes/getOrderType/idOrder/201709080001")
                .header(token, "Authorization"))
                .andExpect(status().isOk());
    }
}
