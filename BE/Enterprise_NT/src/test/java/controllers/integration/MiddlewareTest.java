package controllers.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.middleware.MiddlewareMessage;
import net.cltech.enterprisent.domain.integration.middleware.MiddlewareUrl;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Objeto que representa las pruebas unitarias de la integracion con middleware
 *
 * @version 1.0.0
 * @author equijano
 * @since 29/11/2018
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
public class MiddlewareTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

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
    }

    @Test
    public void test_01_testUrl() throws Exception
    {
        MiddlewareUrl url = new MiddlewareUrl();
        url.setUrl("http://interfaces:8080/MiddlewareManager");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(url);

        mockMvc.perform(post("/api/integration/middleware/verification/url")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void samples_02_ok() throws Exception
    {
        mockMvc.perform(get("/api/middleware/samples")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_03_ok() throws Exception
    {
        mockMvc.perform(get("/api/middleware/test").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void antibiotics_04_noContent() throws Exception
    {
        mockMvc.perform(get("/api/middleware/antibiotics").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void demographics_05_isOk() throws Exception
    {
        mockMvc.perform(get("/api/middleware/demographics").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void anatomic_sites_06_noContent() throws Exception
    {
        mockMvc.perform(get("/api/middleware/anatomic_sites").header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void microorganisms_07_noContent() throws Exception
    {
        mockMvc.perform(get("/api/middleware/microorganisms").header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void resend_08_ok() throws Exception
    {
        TestScript.createInitialOrders();
        ResultFilter resultFilter = new ResultFilter();
        resultFilter.setFirstOrder(Long.parseLong("201710040001"));
        resultFilter.setLastOrder(Long.parseLong("201710040009"));
        resultFilter.setFilterId(1);
        Laboratory laboratory = new Laboratory();
        laboratory.setId(1);
        resultFilter.setLaboratorys(Arrays.asList(laboratory));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(resultFilter);

        mockMvc.perform(post("/api/integration/middleware/resend")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void demographic_09_ok() throws Exception
    {
        mockMvc.perform(get("/api/middleware/demographics/items/" + "-1").header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_deltacheck() throws Exception
    {
        mockMvc.perform(get("/api/middleware/deltacheck")
                .header(token, "Authorization"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_insertResult() throws Exception
    {
        MiddlewareMessage msg = new MiddlewareMessage();
        List<MiddlewareMessage> listMessages = new ArrayList<>();

        // Cargamos el objeto
        msg.setDate("1588012112567");
        msg.setMessage("H|\\\\^&|||Middleware^CLTECH^MIDDLEWARE^1.0.0|||||||P||20200427132832\\r\\nP|1|004270006-2|1070592123||LEONARDO ANDRES DIAZ OSPINA^||19900214|M||||||||"
                + "|||||||201710040007|||||||||\\r\\nO|1|201710040007-2|0|^^^1|R|20200427132832|||||A||||1||||||||||F||||||\\r\\nR|1|^^^102|***.**|||||F|20200427132832||||lismanager|1\\r\\nL|1|N\\r\\n");

        listMessages.add(msg);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(listMessages);

        mockMvc.perform(post("/api/middleware/results")
                .header(token, "Authorization")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void test_12_detailautomate() throws Exception
    {

        TestScript.execTestUpdateScript("INSERT INTO lab05 (lab05c1, lab05c10, lab05c4, lab05c9, lab04c1, lab07c1) VALUES (18, '83', 'Fundacion Santafe de Bogota', '2020-06-15 22:37:05.988', 21, 1);", null);
        TestScript.execTestUpdateScript(" UPDATE lab22 set lab05c1 = 18 where lab22c1 = 201710040001", null);

        mockMvc.perform(get("/api/middleware/detailautomate/order/201710040001/samplecode/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
