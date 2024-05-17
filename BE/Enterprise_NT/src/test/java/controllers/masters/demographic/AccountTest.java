package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
 * Prueba unitaria sobre el api rest /api/accounts
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/04/2017
 * @see Creacion
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes
        = {
            MongoTestAppContext.class,
            TestAppContext.class
        })
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public AccountTest() {
    }

    @Before
    public void setUp() throws Exception {

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
    public void test_01_getAllAccountNoContent() throws Exception {
        mockMvc.perform(get("/api/accounts").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertAccount() throws Exception {
        Account account = new Account();
        account.setNit("1007100108");
        account.setName("CAMC");
        account.setNamePrint("CAMC");
        account.setPhone("7777777");
        account.setEpsCode("123A");
        account.setInstitutional(true);
        account.setUsername("cmartincl");
        account.setPassword("12345");
        account.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(account);

        mockMvc.perform(post("/api/accounts")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nit", is("1007100108")))
                .andExpect(jsonPath("$.name", is("CAMC")))
                .andExpect(jsonPath("$.phone", is("7777777")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.epsCode", is("123A")))
                .andExpect(jsonPath("$.discount", is(10.0)))
                .andExpect(jsonPath("$.institutional", is(true)))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllAccount() throws Exception {
        mockMvc.perform(get("/api/accounts").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getAccountById() throws Exception {
        mockMvc.perform(get("/api/accounts/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nit", is("1007100108")))
                .andExpect(jsonPath("$.name", is("CAMC")))
                .andExpect(jsonPath("$.phone", is("7777777")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.epsCode", is("123A")))
                .andExpect(jsonPath("$.discount", is(10.0)))
                .andExpect(jsonPath("$.institutional", is(true)))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getAccountByOrdering() throws Exception {
        mockMvc.perform(get("/api/accounts/filter/nit/" + "1007100108").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_06_getAccountByAbbr() throws Exception {
        mockMvc.perform(get("/api/accounts/filter/codeeps/" + "123A").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_07_getAccountByIdNoContent() throws Exception {
        mockMvc.perform(get("/api/accounts/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getAccountByOrderingNoContent() throws Exception {
        mockMvc.perform(get("/api/accounts/filter/nit/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getAccountByAbbrNoContent() throws Exception {
        mockMvc.perform(get("/api/accounts/filter/codeeps/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_getAccountByNameNoContent() throws Exception {
        mockMvc.perform(get("/api/accounts/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_insertAccountError() throws Exception {
        Account account = new Account();
        account.setNit("1007100108");
        account.setName("CAMC");
        account.setNamePrint("CAMC");
        account.setPhone("7777777");
        account.setEpsCode("123A");
        account.setInstitutional(true);
        account.setUsername("cmartincl");
        account.setPassword("12345");
        account.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(account);

        mockMvc.perform(post("/api/accounts")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_12_updateAccount() throws Exception {
        Account account = new Account();
        account.setId(1);
        account.setNit("1007100108");
        account.setName("CAMC SA");
        account.setNamePrint("CAMC");
        account.setPhone("7777");
        account.setEpsCode("123B");
        account.setInstitutional(false);
        account.setUsername("cmartincl");
        account.setPassword("12345");
        account.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(account);

        mockMvc.perform(put("/api/accounts")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.nit", is("1007100108")))
                .andExpect(jsonPath("$.name", is("CAMC SA")))
                .andExpect(jsonPath("$.phone", is("7777")))
                .andExpect(jsonPath("$.epsCode", is("123B")))
                .andExpect(jsonPath("$.discount", is(0.0)))
                .andExpect(jsonPath("$.institutional", is(false)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_13_getAccountByName() throws Exception {
        mockMvc.perform(get("/api/accounts/filter/name/CAMC SA"))
                .andExpect(status().isOk());

    }

    @Test
    public void test_14_updateAccountError() throws Exception {
        Account account = new Account();
        account.setId(-1);
        account.setNit("1007100108");
        account.setName("CAMC SA");
        account.setPhone("7777");
        account.setEpsCode("123A");
        account.setInstitutional(true);
        account.setUsername("cmartincl");
        account.setPassword("12345");
        account.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(account);

        mockMvc.perform(put("/api/accounts")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_15_insertRateCol() throws Exception {
        Rate rate = new Rate();
        rate.setCode("PRUEBA 2017");
        rate.setName("PRUEBA 2017");
        rate.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/rates")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("PRUEBA 2017")))
                .andExpect(jsonPath("$.name", is("PRUEBA 2017")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_16_ListRates() throws Exception {
        mockMvc.perform(get("/api/accounts/ratesofaccount/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_17_ListRates() throws Exception {
        mockMvc.perform(get("/api/accounts/ratesofaccount/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_18_InsertRates() throws Exception {
        List<RatesOfAccount> ratesOfAccounts = new ArrayList<>(0);
        ratesOfAccounts.add(new RatesOfAccount(1, 1));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(ratesOfAccounts);

        mockMvc.perform(post("/api/accounts/ratesofaccount")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void test_19_InsertRates2() throws Exception {
        List<RatesOfAccount> ratesOfAccounts = new ArrayList<>(0);
        ratesOfAccounts.add(new RatesOfAccount(1, 1));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(ratesOfAccounts);

        mockMvc.perform(post("/api/accounts/ratesofaccount")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void test_20_DeleteRates() throws Exception {
        mockMvc.perform(delete("/api/accounts/ratesofaccount/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}
