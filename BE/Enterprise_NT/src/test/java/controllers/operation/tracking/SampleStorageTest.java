package controllers.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.tracking.SampleStore;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import static org.hamcrest.Matchers.is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
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
 * Prueba unitaria sobre el almacenamiento de la muestra
 *
 * @version 1.0.0
 * @author eacuna
 * @since 13/08/2018
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
public class SampleStorageTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;
    List<Question> questions;

    public SampleStorageTest()
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
        user.setBranch(1);
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_rackCreation() throws Exception
    {
        TestScript.createInitialOrders();
        TestScript.execTestUpdateScript("TRUNCATE TABLE lab16 RESTART IDENTITY;", null);
        TestScript.createInitialBranch(new ArrayList<>(Arrays.asList("1", "2")));
        Rack general = new Rack();
        Rack pending = new Rack();
        Rack special = new Rack();

        general.setColumn(3);
        general.setRow(3);
        general.setName("Gral");
        general.setType(LISEnum.RackType.GENERAL.getValue());
        //Crea gradilla general
        String response = TestTools.getResponseString(mockMvc.perform(post("/api/racks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(general))));

        general = Tools.jsonObject(response, Rack.class);
        Assert.assertNotNull(general.getId());
        //Crea gradilla pendientes
        general.setName("Pendiente");
        general.setType(LISEnum.RackType.PENDING.getValue());
        response = TestTools.getResponseString(mockMvc.perform(post("/api/racks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(general))));
        pending = Tools.jsonObject(response, Rack.class);
        Assert.assertNotNull(pending.getId());
        //Crea gradilla especiales
        general.setName("Especial");
        general.setType(LISEnum.RackType.CONFIDENTIAL.getValue());
        response = TestTools.getResponseString(mockMvc.perform(post("/api/racks")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(general))));
        special = Tools.jsonObject(response, Rack.class);
        Assert.assertNotNull(special.getId());

    }

    @org.junit.Test
    public void test_02_sampleStorage() throws Exception
    {
        List<Test> tests = new ArrayList<>();
        tests.add(new Test(1));
        tests.add(new Test(2));
        tests.add(new Test(3));
        tests = tests.stream().map(t -> t.setTestType((short) 0)).collect(Collectors.toList());

        Order order = TestTools.createNewOrder(mockMvc, token, tests);
        order.getBranch().setId(2);
        System.out.println("Orden:" + order.getOrderNumber());
        Assert.assertNotNull(order.getOrderNumber());

        SampleStore store = new SampleStore();
        store.setOrder(order.getOrderNumber());
        store.setSample("1");
        store.setRacks(Arrays.asList(1, 2, 3));

        test_04_sampleVerify(order.getOrderNumber(), "1");
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(store);

        mockMvc.perform(post("/api/racks/store")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position", is("A1")))
                .andExpect(jsonPath("$.rack.id", is(2)))
                .andExpect(jsonPath("$.insert", is(true)))
                .andExpect(jsonPath("$.order", is(order.getOrderNumber())));
        test_03_sampleRemoveFromPending();

        String response = TestTools.getResponseString(mockMvc.perform(get("/api/audits/samplestorage/order/" + order.getOrderNumber() + "/sample/1")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(store))));
        List<AuditEvent> events = Tools.jsonList(response, AuditEvent.class);
        Assert.assertTrue(events.size() > 0);
    }

    public void test_03_sampleRemoveFromPending() throws Exception
    {
        SampleStore store = new SampleStore();
        store.setPosition("A1");
        store.setRackId(2);

        mockMvc.perform(put("/api/racks/remove")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(store)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

    }

    public void test_04_sampleVerify(long order, String codeSample) throws Exception
    {
        mockMvc.perform(post("/api/sampletrackings/verify/order/" + order + "/sample/" + codeSample)
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

}
