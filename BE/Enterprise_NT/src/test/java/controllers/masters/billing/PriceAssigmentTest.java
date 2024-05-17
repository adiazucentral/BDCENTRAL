package controllers.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.math.BigDecimal;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.billing.FormulaOption;
import net.cltech.enterprisent.domain.masters.billing.PriceAssigment;
import net.cltech.enterprisent.domain.masters.billing.TestPrice;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
 * Prueba unitaria sobre el api rest /api/priceassigments
 *
 * @version 1.0.0
 * @author eacuna
 * @since 03/08/2017
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
public class PriceAssigmentTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public PriceAssigmentTest()
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
        TestScript.execTestUpdateScript("DELETE FROM lab39", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (1, 1, '1001', 'HMG', 'HEMOGLOBINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (2, 1, '1002', 'GLU', 'GLUCOSA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 0, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (3, 1, '1003', 'COL', 'COLESTEROL', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (4, 1, '1004', 'TRI', 'TRIGLICERIDOS', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (5, NULL, '101', 'CH', 'CUADRO HEMATICO', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 0, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 1, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (6, 1, '1005', 'GLI', 'GLICEMIA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 1, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 0, 0, 0, 0, NULL, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 VALUES (7, NULL, '102', 'CH', 'PARCIAL ORINA', NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, 2, 0, 1, NULL, NULL, 0, 0, 2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, now(), 1, 1, 1, 0, 0, 0, NULL, 1, NULL)", null);

        TestScript.execTestUpdateScript("DELETE FROM lab904", null);
        TestScript.execTestUpdateScript("INSERT INTO lab904 VALUES (1, N'S', N'SOAT', N'', N'', N'', N'', N'', N'', N'', N'', 0, 0, 0, 0, 0, 0, 0, 0, 0, N'', 0, N'', N'', N'', N'', N'', N'', NULL, N'0', N'', 0, 0, 0, 0, 0, now(), 1, 1, NULL)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab904 VALUES (2, N'I', N'ISS 1%', N'', N'', N'', N'', N'', N'', N'', N'', 0, 0, 0, 0, 0, 0, 0, 0, 0, N'', 0, N'', N'', N'', N'', N'', N'', NULL, N'0', N'', 0, 0, 0, 0, 0, now(), 1, 1, NULL)", null);

        TestScript.execTestUpdateScript("DELETE FROM lab116", null);

        mockMvc.perform(get("/api/priceassigments/{valid}/{rate}/{area}".replace("{valid}", "1").replace("{rate}", "1").replace("{area}", "5")).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_content() throws Exception
    {

        TestScript.execTestUpdateScript("UPDATE lab39 set lab39c20 = 1 where lab39c1 in (1,2,4,5)", null);
        mockMvc.perform(get("/api/priceassigments/{valid}/{rate}/{area}".replace("{valid}", "1").replace("{rate}", "1").replace("{area}", "1")).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 4, 6)))
                .andExpect(jsonPath("$[*].price", containsInAnyOrder(0, 0, 0)));
    }

    @Test
    public void test_03_updatePriceErrorEmpty() throws Exception
    {

        PriceAssigment rate = new PriceAssigment();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/priceassigments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("0|feeschedule", "0|rate", "0|test")));
    }

    @Test
    public void test_04_updatePriceErrorInvalid() throws Exception
    {

        TestPrice test = new TestPrice(50);
        test.setPrice(new BigDecimal(150));

        PriceAssigment rate = new PriceAssigment();
        rate.setIdValid(1);
        rate.setId(50);
        rate.setTest(test);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        System.out.println(jsonContent);

        mockMvc.perform(post("/api/priceassigments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("3|feeschedule", "3|rate", "3|test")));
    }

    @Test
    public void test_05_updatePrice() throws Exception
    {

        TestScript.execTestUpdateScript("INSERT INTO lab116 VALUES (1, N'2015', now(), now(), 1, 1, now(),1)", null);
        TestPrice test = new TestPrice(1);
        test.setPrice(new BigDecimal(150.50));

        PriceAssigment rate = new PriceAssigment();
        rate.setIdValid(1);
        rate.setId(1);
        rate.setTest(test);
        rate.setCentralSystem(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/priceassigments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void test_06_contentWithPrice() throws Exception
    {

        mockMvc.perform(get("/api/priceassigments/{valid}/{rate}/{area}".replace("{valid}", "1").replace("{rate}", "1").replace("{area}", "1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 4, 6)))
                .andExpect(jsonPath("$[?(@.id==1)].price", contains(150.50)))
                .andExpect(jsonPath("$[?(@.id==4)].price", contains(0)));
    }

    @Test
    public void test_07_importError() throws Exception
    {

        TestPrice test = new TestPrice();
        test.setCode("a");
        test.setPrice(new BigDecimal(150.50));
        TestPrice test1 = new TestPrice();
        test1.setCode("b");
        test1.setPrice(new BigDecimal(200));

        PriceAssigment rate = new PriceAssigment();
        rate.setIdValid(50);
        rate.setId(50);
        rate.setCentralSystem(0);
        rate.getImportTests().add(test);
        rate.getImportTests().add(test1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/priceassigments/import").header("Authorization", token)
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("3|feeschedule", "3|rate", "3|code|0", "3|code|1")));
    }

    @Test
    public void test_08_importOk() throws Exception
    {

        TestPrice test = new TestPrice();
        test.setCode("1001");
        test.setPrice(new BigDecimal(150.50));
        TestPrice test1 = new TestPrice();
        test1.setCode("1004");
        test1.setPrice(new BigDecimal(200));

        PriceAssigment rate = new PriceAssigment();
        rate.setIdValid(1);
        rate.setId(1);
        rate.setCentralSystem(0);
        rate.getImportTests().add(test);
        rate.getImportTests().add(test1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/priceassigments/import").header("Authorization", token)
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void test_09_contentImport() throws Exception
    {

        mockMvc.perform(get("/api/priceassigments/{valid}/{rate}/{area}".replace("{valid}", "1").replace("{rate}", "1").replace("{area}", "1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 4, 6)))
                .andExpect(jsonPath("$[?(@.id==1)].price", contains(150.50)))
                .andExpect(jsonPath("$[?(@.id==4)].price", contains(200)));
    }

    @Test
    public void test_10_importSystemError() throws Exception
    {

        TestPrice test = new TestPrice();
        test.setCode("a");
        test.setPrice(new BigDecimal(150.50));
        TestPrice test1 = new TestPrice();
        test1.setCode("b");
        test1.setPrice(new BigDecimal(200));

        PriceAssigment rate = new PriceAssigment();
        rate.setIdValid(50);
        rate.setId(50);
        rate.setCentralSystem(1);
        rate.getImportTests().add(test);
        rate.getImportTests().add(test1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/priceassigments/import").header("Authorization", token)
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("3|feeschedule", "3|rate", "3|code|0", "3|code|1")));
    }

    @Test
    public void test_11_importSystemOk() throws Exception
    {
        TestScript.execTestUpdateScript("INSERT INTO lab61 VALUES (1, 1, 'b')", null);
        TestScript.execTestUpdateScript("INSERT INTO lab61 VALUES (1, 4, 'a')", null);

        TestPrice test = new TestPrice();
        test.setCode("a");
        test.setPrice(new BigDecimal(150.50));
        TestPrice test1 = new TestPrice();
        test1.setCode("b");
        test1.setPrice(new BigDecimal(200));

        PriceAssigment rate = new PriceAssigment();
        rate.setIdValid(1);
        rate.setId(1);
        rate.setCentralSystem(1);
        rate.getImportTests().add(test);
        rate.getImportTests().add(test1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/priceassigments/import").header("Authorization", token)
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void test_12_contentSystemImportOk() throws Exception
    {

        mockMvc.perform(get("/api/priceassigments/{valid}/{rate}/{area}".replace("{valid}", "1").replace("{rate}", "1").replace("{area}", "1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 4, 6)))
                .andExpect(jsonPath("$[?(@.id==4)].price", contains(150.50)))
                .andExpect(jsonPath("$[?(@.id==1)].price", contains(200)));
    }

    @Test
    public void test_13_formulaUpdateError() throws Exception
    {
        FormulaOption formula = new FormulaOption();
        formula.setFrom(4);
        formula.setTo(5);
        formula.setOperator("a");
        formula.setRoundMode(3);
        formula.setWriteType(3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(formula);

        mockMvc.perform(put("/api/priceassigments/formula")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", containsInAnyOrder("3|from", "3|to", "3|operator", "3|value", "3|roundMode", "3|writeType", "3|feeschedule")));
    }

    @Test
    public void test_14_formulaTotalApplyOk() throws Exception
    {
        FormulaOption formula = new FormulaOption();
        formula.setFrom(1);
        formula.setTo(2);
        formula.setOperator("+");
        formula.setValue(new BigDecimal(2.5));
        formula.setRoundMode(0);
        formula.setWriteType(FormulaOption.WRITE_TOTAL);
        formula.setFeeSchedule(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(formula);

        mockMvc.perform(put("/api/priceassigments/formula")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void test_15_contentApplyOk() throws Exception
    {

        mockMvc.perform(get("/api/priceassigments/{valid}/{rate}/{area}".replace("{valid}", "1").replace("{rate}", "2").replace("{area}", "-1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 4, 5, 6, 7)))
                .andExpect(jsonPath("$[?(@.id==4)].price", contains(153)))
                .andExpect(jsonPath("$[?(@.id==1)].price", contains(202)))
                .andExpect(jsonPath("$[?(@.id==5)].price", contains(2)))
                .andExpect(jsonPath("$[?(@.id==6)].price", contains(2)))
                .andExpect(jsonPath("$[?(@.id==7)].price", contains(2)));
    }

    @Test
    public void test_16_formulaPartialApplyOk() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab120 WHERE lab39c1 = 4 AND lab904c1 = 2 ", null);
        TestScript.execTestUpdateScript("UPDATE lab98 set lab98c2 = 'True' WHERE lab98c1 = 'ManejoCentavos'", null);
        FormulaOption formula = new FormulaOption();
        formula.setFrom(1);
        formula.setTo(2);
        formula.setOperator("/");
        formula.setValue(new BigDecimal(0.44));
        formula.setRoundMode(1);
        formula.setWriteType(FormulaOption.WRITE_PARTIAL);
        formula.setFeeSchedule(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(formula);

        mockMvc.perform(put("/api/priceassigments/formula")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void test_17_contentApplyPartialOk() throws Exception
    {

        mockMvc.perform(get("/api/priceassigments/{valid}/{rate}/{area}".replace("{valid}", "1").replace("{rate}", "2").replace("{area}", "-1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 4, 5, 6, 7)))
                .andExpect(jsonPath("$[?(@.id==4)].price", contains(342.05)))
                .andExpect(jsonPath("$[?(@.id==6)].price", contains(2)))
                .andExpect(jsonPath("$[?(@.id==7)].price", contains(2)))
                .andExpect(jsonPath("$[?(@.id==5)].price", contains(2)))
                .andExpect(jsonPath("$[?(@.id==1)].price", contains(202)));
    }

    @Test
    public void test_18_contentApplyFeescheduleError() throws Exception
    {
        mockMvc.perform(post("/api/priceassigments/apply")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content("5"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorFields", contains("3|feeschedule")));
    }

    @Test
    public void test_19_contentApplyFeescheduleOk() throws Exception
    {
        mockMvc.perform(post("/api/priceassigments/apply")
                .header("Authorization", token).contentType(TestTools.APPLICATION_JSON_UTF8)
                .content("1"))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));
    }

    @Test
    public void test_20_updatePrice() throws Exception
    {

        TestPrice test = new TestPrice(1);
        test.setPrice(new BigDecimal(225.50));

        PriceAssigment rate = new PriceAssigment();
        rate.setIdValid(1);
        rate.setId(1);
        rate.setTest(test);
        rate.setCentralSystem(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rate);

        mockMvc.perform(post("/api/priceassigments")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void test_21_contentWithPrice() throws Exception
    {

        mockMvc.perform(get("/api/priceassigments/{valid}/{rate}/{area}".replace("{valid}", "1").replace("{rate}", "1").replace("{area}", "1"))
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 4, 6)))
                .andExpect(jsonPath("$[?(@.id==1)].price", contains(225.50)))
                .andExpect(jsonPath("$[?(@.id==4)].price", contains(150.5)));
    }

}
