/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
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
 * Prueba unitaria sobre el api rest /api/rates
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/06/2017
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
public class RateTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public RateTest()
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
    public void test_01_getAllRateNoContent() throws Exception
    {
        TestScript.deleteData("lab904");
        mockMvc.perform(get("/api/rates").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertRateCol() throws Exception
    {
        Rate rate = new Rate();
        rate.setCode("ISS 2001");
        rate.setName("ISS 2001");
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
                .andExpect(jsonPath("$.code", is("ISS 2001")))
                .andExpect(jsonPath("$.name", is("ISS 2001")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllRateCol() throws Exception
    {
        mockMvc.perform(get("/api/rates").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getRateByIdCol() throws Exception
    {
        mockMvc.perform(get("/api/rates/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS 2001")))
                .andExpect(jsonPath("$.name", is("ISS 2001")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getRateByCodeCol() throws Exception
    {
        mockMvc.perform(get("/api/rates/filter/code/" + "ISS 2001").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS 2001")))
                .andExpect(jsonPath("$.name", is("ISS 2001")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_06_getRateByNameCol() throws Exception
    {
        mockMvc.perform(get("/api/rates/filter/name/" + "ISS 2001").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS 2001")))
                .andExpect(jsonPath("$.name", is("ISS 2001")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_07_getRateByIdNoContentCol() throws Exception
    {
        mockMvc.perform(get("/api/rates/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getRateByCodeNoContentCol() throws Exception
    {
        mockMvc.perform(get("/api/rates/filter/code/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getRateByNameNoContentCol() throws Exception
    {
        mockMvc.perform(get("/api/rates/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_insertRateErrorCol() throws Exception
    {
        Rate rate = new Rate();
        rate.setCode("ISS 2001");
        rate.setName("ISS 2001");
        rate.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/rates")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_updateRateCol() throws Exception
    {
        Rate rate = new Rate();
        rate.setId(1);
        rate.setCode("ISS 2005");
        rate.setName("ISS 2005");
        rate.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(put("/api/rates")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS 2005")))
                .andExpect(jsonPath("$.name", is("ISS 2005")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_12_updateRateErrorCol() throws Exception
    {
        Rate rate = new Rate();
        rate.setId(-1);
        rate.setCode("ISS 2007");
        rate.setName("ISS 2007");
        rate.setState(false);
        rate.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(put("/api/rates")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    //----------USA-----------
    @Test
    public void test_13_getPayersNoContent() throws Exception
    {
        mockMvc.perform(get("/api/rates/payers").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_14_insertRateUSA() throws Exception
    {
        Rate rate = new Rate();
        rate.setCode("ISS");
        rate.setName("ISS");
        rate.setAddress("Cll 1");
        rate.setAddAddress("Cll 2");
        rate.setCity("San Francisco");
        rate.setDepartment("California");
        rate.setPhone("74589554");
        rate.setEmail("a@b.c");
        rate.setPostalCode("123");
        rate.setWebPage("www.a.com");
        rate.setCheckPaid(true);
        rate.setTypePayer(2);
        rate.setAssingAllAccounts(true);
        rate.setApplyDiagnostics(true);
        rate.setCheckCPTRelation(true);
        rate.setHomebound(true);
        rate.setVenipunture(true);
        rate.setApplyTypePayer(true);
        rate.setClaimCode("123");
        rate.setEligibility(true);
        rate.setInterchangeSender("");
        rate.setInterchangeQualifier("");
        rate.setApplicationSendCode("");
        rate.setLabSubmitter("");
        rate.setIdentificationPayer("");
        rate.setFormatMemberId("");
        rate.setReceiver(0);
        rate.setConsecutive("");
        rate.setOutputFileName("");
        rate.setClaimType(2);
        rate.setTransactionType(2);
        rate.setSupplierSignature(true);
        rate.setAssingBenefits(true);
        rate.setElectronicClaim(true);
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
                .andExpect(jsonPath("$.code", is("ISS")))
                .andExpect(jsonPath("$.name", is("ISS")))
                .andExpect(jsonPath("$.address", is("Cll 1")))
                .andExpect(jsonPath("$.addAddress", is("Cll 2")))
                .andExpect(jsonPath("$.city", is("San Francisco")))
                .andExpect(jsonPath("$.department", is("California")))
                .andExpect(jsonPath("$.phone", is("74589554")))
                .andExpect(jsonPath("$.postalCode", is("123")))
                .andExpect(jsonPath("$.webPage", is("www.a.com")))
                .andExpect(jsonPath("$.typePayer", is(2)))
                .andExpect(jsonPath("$.claimCode", is("123")))
                .andExpect(jsonPath("$.receiver", is(0)))
                .andExpect(jsonPath("$.claimType", is(2)))
                .andExpect(jsonPath("$.transactionType", is(2)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_15_getRateByIdUSA() throws Exception
    {
        mockMvc.perform(get("/api/rates/" + "2").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS")))
                .andExpect(jsonPath("$.name", is("ISS")))
                .andExpect(jsonPath("$.address", is("Cll 1")))
                .andExpect(jsonPath("$.addAddress", is("Cll 2")))
                .andExpect(jsonPath("$.city", is("San Francisco")))
                .andExpect(jsonPath("$.department", is("California")))
                .andExpect(jsonPath("$.phone", is("74589554")))
                .andExpect(jsonPath("$.postalCode", is("123")))
                .andExpect(jsonPath("$.webPage", is("www.a.com")))
                .andExpect(jsonPath("$.typePayer", is(2)))
                .andExpect(jsonPath("$.claimCode", is("123")))
                .andExpect(jsonPath("$.receiver", is(0)))
                .andExpect(jsonPath("$.claimType", is(2)))
                .andExpect(jsonPath("$.transactionType", is(2)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_16_getRateByCodeUSA() throws Exception
    {
        mockMvc.perform(get("/api/rates/filter/code/" + "ISS").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS")))
                .andExpect(jsonPath("$.name", is("ISS")))
                .andExpect(jsonPath("$.address", is("Cll 1")))
                .andExpect(jsonPath("$.addAddress", is("Cll 2")))
                .andExpect(jsonPath("$.city", is("San Francisco")))
                .andExpect(jsonPath("$.department", is("California")))
                .andExpect(jsonPath("$.phone", is("74589554")))
                .andExpect(jsonPath("$.postalCode", is("123")))
                .andExpect(jsonPath("$.webPage", is("www.a.com")))
                .andExpect(jsonPath("$.typePayer", is(2)))
                .andExpect(jsonPath("$.claimCode", is("123")))
                .andExpect(jsonPath("$.receiver", is(0)))
                .andExpect(jsonPath("$.claimType", is(2)))
                .andExpect(jsonPath("$.transactionType", is(2)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_17_getRateByNameUSA() throws Exception
    {
        mockMvc.perform(get("/api/rates/filter/name/" + "ISS").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS")))
                .andExpect(jsonPath("$.name", is("ISS")))
                .andExpect(jsonPath("$.address", is("Cll 1")))
                .andExpect(jsonPath("$.addAddress", is("Cll 2")))
                .andExpect(jsonPath("$.city", is("San Francisco")))
                .andExpect(jsonPath("$.department", is("California")))
                .andExpect(jsonPath("$.phone", is("74589554")))
                .andExpect(jsonPath("$.postalCode", is("123")))
                .andExpect(jsonPath("$.webPage", is("www.a.com")))
                .andExpect(jsonPath("$.typePayer", is(2)))
                .andExpect(jsonPath("$.claimCode", is("123")))
                .andExpect(jsonPath("$.receiver", is(0)))
                .andExpect(jsonPath("$.claimType", is(2)))
                .andExpect(jsonPath("$.transactionType", is(2)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_18_insertRateErrorUSA() throws Exception
    {
        Rate rate = new Rate();
        rate.setCode("ISS");
        rate.setName("ISS");
        rate.setAddress("Cll 1");
        rate.setAddAddress("Cll 2");
        rate.setCity("San Francisco");
        rate.setDepartment("California");
        rate.setPhone("74589554");
        rate.setEmail("a@b.c");
        rate.setPostalCode("123");
        rate.setWebPage("www.a.com");
        rate.setCheckPaid(true);
        rate.setTypePayer(1);
        rate.setAssingAllAccounts(true);
        rate.setApplyDiagnostics(true);
        rate.setCheckCPTRelation(true);
        rate.setHomebound(true);
        rate.setVenipunture(true);
        rate.setApplyTypePayer(true);
        rate.setClaimCode("123");
        rate.setEligibility(true);
        rate.setInterchangeSender("");
        rate.setInterchangeQualifier("");
        rate.setApplicationSendCode("");
        rate.setLabSubmitter("");
        rate.setIdentificationPayer("");
        rate.setFormatMemberId("");
        rate.setReceiver(0);
        rate.setConsecutive("");
        rate.setOutputFileName("");
        rate.setClaimType(2);
        rate.setTransactionType(2);
        rate.setSupplierSignature(true);
        rate.setAssingBenefits(true);
        rate.setElectronicClaim(true);
        rate.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/rates")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_19_updateRateUSA() throws Exception
    {
        Rate rate = new Rate();
        rate.setId(2);
        rate.setCode("ISS");
        rate.setName("ISS");
        rate.setAddress("Cll 63");
        rate.setAddAddress("CRA 24");
        rate.setCity("San Francisco");
        rate.setDepartment("California");
        rate.setPhone("74589554");
        rate.setEmail("a@b.c");
        rate.setPostalCode("123");
        rate.setWebPage("www.a.com");
        rate.setCheckPaid(true);
        rate.setTypePayer(1);
        rate.setAssingAllAccounts(true);
        rate.setApplyDiagnostics(true);
        rate.setCheckCPTRelation(true);
        rate.setHomebound(true);
        rate.setVenipunture(true);
        rate.setApplyTypePayer(true);
        rate.setClaimCode("123");
        rate.setEligibility(true);
        rate.setInterchangeSender("123A");
        rate.setInterchangeQualifier("123A");
        rate.setApplicationSendCode("123A");
        rate.setLabSubmitter("123A");
        rate.setIdentificationPayer("123A");
        rate.setFormatMemberId("123A");
        rate.setReceiver(0);
        rate.setConsecutive("");
        rate.setOutputFileName("");
        rate.setClaimType(2);
        rate.setTransactionType(2);
        rate.setSupplierSignature(true);
        rate.setAssingBenefits(true);
        rate.setElectronicClaim(true);
        rate.setState(true);
        rate.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(put("/api/rates")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS")))
                .andExpect(jsonPath("$.name", is("ISS")))
                .andExpect(jsonPath("$.address", is("Cll 63")))
                .andExpect(jsonPath("$.addAddress", is("CRA 24")))
                .andExpect(jsonPath("$.city", is("San Francisco")))
                .andExpect(jsonPath("$.department", is("California")))
                .andExpect(jsonPath("$.phone", is("74589554")))
                .andExpect(jsonPath("$.postalCode", is("123")))
                .andExpect(jsonPath("$.webPage", is("www.a.com")))
                .andExpect(jsonPath("$.typePayer", is(1)))
                .andExpect(jsonPath("$.claimCode", is("123")))
                .andExpect(jsonPath("$.receiver", is(0)))
                .andExpect(jsonPath("$.claimType", is(2)))
                .andExpect(jsonPath("$.interchangeSender", is("123A")))
                .andExpect(jsonPath("$.interchangeQualifier", is("123A")))
                .andExpect(jsonPath("$.applicationSendCode", is("123A")))
                .andExpect(jsonPath("$.labSubmitter", is("123A")))
                .andExpect(jsonPath("$.identificationPayer", is("123A")))
                .andExpect(jsonPath("$.formatMemberId", is("123A")))
                .andExpect(jsonPath("$.transactionType", is(2)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_20_updateRateErrorUSA() throws Exception
    {
        Rate rate = new Rate();
        rate.setId(2);
        rate.setCode("ISS 2005");
        rate.setName("ISS 2005");
        rate.setAddress("Cll 63");
        rate.setAddAddress("CRA 24");
        rate.setCity("San Francisco");
        rate.setDepartment("California");
        rate.setPhone("74589554");
        rate.setEmail("a@b.c");
        rate.setPostalCode("123");
        rate.setWebPage("www.a.com");
        rate.setCheckPaid(true);
        rate.setTypePayer(1);
        rate.setAssingAllAccounts(true);
        rate.setApplyDiagnostics(true);
        rate.setCheckCPTRelation(true);
        rate.setHomebound(true);
        rate.setVenipunture(true);
        rate.setApplyTypePayer(true);
        rate.setClaimCode("123");
        rate.setEligibility(true);
        rate.setInterchangeSender("123A");
        rate.setInterchangeQualifier("123A");
        rate.setApplicationSendCode("123A");
        rate.setLabSubmitter("123A");
        rate.setIdentificationPayer("123A");
        rate.setFormatMemberId("123A");
        rate.setReceiver(0);
        rate.setConsecutive("");
        rate.setOutputFileName("");
        rate.setClaimType(2);
        rate.setTransactionType(2);
        rate.setSupplierSignature(true);
        rate.setAssingBenefits(true);
        rate.setElectronicClaim(true);
        rate.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(put("/api/rates")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_21_getRateByNameUpdateUSA() throws Exception
    {
        mockMvc.perform(get("/api/rates/filter/name/" + "ISS").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("ISS")))
                .andExpect(jsonPath("$.name", is("ISS")))
                .andExpect(jsonPath("$.address", is("Cll 63")))
                .andExpect(jsonPath("$.addAddress", is("CRA 24")))
                .andExpect(jsonPath("$.city", is("San Francisco")))
                .andExpect(jsonPath("$.department", is("California")))
                .andExpect(jsonPath("$.phone", is("74589554")))
                .andExpect(jsonPath("$.postalCode", is("123")))
                .andExpect(jsonPath("$.webPage", is("www.a.com")))
                .andExpect(jsonPath("$.typePayer", is(1)))
                .andExpect(jsonPath("$.claimCode", is("123")))
                .andExpect(jsonPath("$.receiver", is(0)))
                .andExpect(jsonPath("$.claimType", is(2)))
                .andExpect(jsonPath("$.interchangeSender", is("123A")))
                .andExpect(jsonPath("$.interchangeQualifier", is("123A")))
                .andExpect(jsonPath("$.applicationSendCode", is("123A")))
                .andExpect(jsonPath("$.labSubmitter", is("123A")))
                .andExpect(jsonPath("$.identificationPayer", is("123A")))
                .andExpect(jsonPath("$.formatMemberId", is("123A")))
                .andExpect(jsonPath("$.transactionType", is(2)))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_22_getPayersContent() throws Exception
    {
        mockMvc.perform(get("/api/rates/payers").header("Authorization", token))
                .andExpect(status().isOk());
    }
}
