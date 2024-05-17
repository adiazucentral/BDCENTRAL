package controllers.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.PaymentType;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
 * Prueba unitaria sobre el api rest /api/paymenttypes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 29/08/2017
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
public class PaymentTypeTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public PaymentTypeTest()
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
    public void test_01_getAll() throws Exception
    {
        mockMvc.perform(get("/api/paymenttypes").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Efectivo")))
                .andExpect(jsonPath("$[0].card", is(false)))
                .andExpect(jsonPath("$[0].bank", is(false)))
                .andExpect(jsonPath("$[0].number", is(false)))
                .andExpect(jsonPath("$[0].adjustment", is(false)));
    }

    @Test
    public void test_02_insert() throws Exception
    {
        PaymentType payment = new PaymentType();
        payment.setName("Cheque");
        payment.getUser().setId(1);
        payment.setBank(true);
        payment.setNumber(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(payment);

        mockMvc.perform(post("/api/paymenttypes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Cheque")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getById() throws Exception
    {
        mockMvc.perform(get("/api/paymenttypes/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Efectivo")))
                .andExpect(jsonPath("$.card", is(false)))
                .andExpect(jsonPath("$.bank", is(false)))
                .andExpect(jsonPath("$.number", is(false)))
                .andExpect(jsonPath("$.adjustment", is(false)));
    }

    @Test
    public void test_04_getByName() throws Exception
    {
        mockMvc.perform(get("/api/paymenttypes/filter/name/" + "cheque").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Cheque")))
                .andExpect(jsonPath("$.card", is(false)))
                .andExpect(jsonPath("$.bank", is(true)))
                .andExpect(jsonPath("$.number", is(true)))
                .andExpect(jsonPath("$.adjustment", is(false)));
    }

    @Test
    public void test_05_duplicateFields() throws Exception
    {
        PaymentType payment = new PaymentType();

        payment.setName("cheque");

        payment.setUser(new AuthorizedUser());
        payment.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(payment);

        mockMvc.perform(post("/api/paymenttypes/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("1|name")));
    }

    @Test
    public void test_06_emptyField() throws Exception
    {
        PaymentType payment = new PaymentType();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(payment);

        mockMvc.perform(post("/api/paymenttypes/")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorFields[*]", containsInAnyOrder("0|name", "0|user")));
    }

    @Test
    public void test_07_update() throws Exception
    {
        PaymentType bank = new PaymentType();
        bank.setId(2);
        bank.setName("Bono");
        bank.setNumber(true);
        bank.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(bank);

        mockMvc.perform(put("/api/paymenttypes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Bono")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_08_getAllPaymentType() throws Exception
    {
        mockMvc.perform(get("/api/paymenttypes").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Bono", "Efectivo")));
    }

    @Test
    public void test_09_listByState() throws Exception
    {
        mockMvc.perform(get("/api/paymenttypes/filter/state/true").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Efectivo")))
                .andExpect(jsonPath("$[0].card", is(false)))
                .andExpect(jsonPath("$[0].bank", is(false)))
                .andExpect(jsonPath("$[0].number", is(false)))
                .andExpect(jsonPath("$[0].adjustment", is(false)));
        mockMvc.perform(get("/api/paymenttypes/filter/state/false").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Bono")))
                .andExpect(jsonPath("$[0].card", is(false)))
                .andExpect(jsonPath("$[0].bank", is(false)))
                .andExpect(jsonPath("$[0].number", is(true)))
                .andExpect(jsonPath("$[0].adjustment", is(false)));
    }

}
