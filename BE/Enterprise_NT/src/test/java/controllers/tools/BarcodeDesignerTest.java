package controllers.tools;

import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.tools.BarcodeDesigner;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/barcode/designer
 *
 * @version 1.0.0
 * @author eacuna
 * @since 07/12/2018
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
@Transactional(transactionManager = "transactionManager")
public class BarcodeDesignerTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public BarcodeDesignerTest()
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
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab105 RESTART IDENTITY;", null);
    }

    @Test
    public void test_01_empty() throws Exception
    {
        mockMvc.perform(get("/api/barcode/designer").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_create() throws Exception
    {
        BarcodeDesigner design = new BarcodeDesigner();
        design.setType(BarcodeDesigner.SAMPLE);
        design.setTemplate("Plantilla Muestras");
        design.setCommand("ComandoEPL");
        design.setType(1);
        design.setVersion(1);

        mockMvc.perform(post("/api/barcode/designer")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(design)))
                .andExpect(status().isOk());
    }

//    @Test
//    public void test_03_getById() throws Exception
//    {
//        mockMvc.perform(get("/api/barcode/designer/filter/id/" + "1").header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.template", is("Plantilla Muestras")))
//                .andExpect(jsonPath("$.command", is("ComandoEPL")))
//                .andExpect(jsonPath("$.type", is(BarcodeDesigner.SAMPLE)))
//                .andExpect(jsonPath("$.active", is(true)));
//    }
//
//    @Test
//    public void test_04_update() throws Exception
//    {
//        BarcodeDesigner design = new BarcodeDesigner();
//        design.setId(1);
//        design.setType(BarcodeDesigner.SAMPLE);
//        design.setTemplate("Plantilla Muestras 1");
//        design.setCommand("ComandoEPL Mod");
//        design.setActive(false);
//
//        mockMvc.perform(put("/api/barcode/designer")
//                .header("Authorization", token)
//                .contentType(TestTools.APPLICATION_JSON_UTF8)
//                .content(Tools.jsonObject(design)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void test_05_getAll() throws Exception
//    {
//        mockMvc.perform(get("/api/barcode/designer").header("Authorization", token))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].template", is("Plantilla Muestras 1")))
//                .andExpect(jsonPath("$[0].command", is("ComandoEPL Mod")))
//                .andExpect(jsonPath("$[0].active", is(false)))
//                .andExpect(jsonPath("$[0].type", is(BarcodeDesigner.SAMPLE)));
//    }
}
