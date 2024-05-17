package controllers.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.ingreso.RequestCentralCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestDemographicAuto;
import net.cltech.enterprisent.domain.integration.ingreso.RequestDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestHomologationDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestHomologationTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestItemCentralCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTestIngreso;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.test.CentralSystem;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Objeto que representa las pruebas unitarias de la integracion de la interfaz
 * de ingreso
 *
 * @version 1.0.0
 * @author bvalero
 * @since 24/01/2020
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
public class IngresoTest
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
    public void test_01_HomologacionDemograficos() throws Exception
    {
        RequestDemographicIngreso obj = new RequestDemographicIngreso(-10, "PN");
        List<RequestDemographicIngreso> list = new ArrayList<>();
        list.add(obj);
        RequestHomologationDemographicIngreso obj1 = new RequestHomologationDemographicIngreso(7, list);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(obj1);
        mockMvc.perform(post("/api/integration/ingreso/DemographicsHomologationSystem")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_02_HomologationTest() throws Exception
    {
        TestScript.execTestUpdateScript("INSERT INTO lab61 VALUES (1, 1877, 'PN01')", null);
        RequestTestIngreso item = new RequestTestIngreso("PN01", "buenas");
        List<RequestTestIngreso> list = new ArrayList<>();
        list.add(item);
        RequestHomologationTestIngreso obj = new RequestHomologationTestIngreso(1, list);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(obj);

        mockMvc.perform(post("/api/integration/ingreso/TestHomologationSystem")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_03_AutomaticDemographicCreation() throws Exception
    {
        //Insertando el sistema central en la base de datos:
        Timestamp timestamp = Timestamp.valueOf("2020-02-04 10:46:43.95");
        CentralSystem central = new CentralSystem();
        central.setName("Médico tratante");
        central.setEHR(true);
        central.setLastTransaction(timestamp);
        central.setRepeatCode(true);
        central.setState(true);
        central.getUser().setId(1);

        ObjectMapper mapperOne = new ObjectMapper();
        mapperOne.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentCentralSystem = mapperOne.writeValueAsString(central);

        mockMvc.perform(post("/api/centralsystems").header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentCentralSystem))
                .andExpect(status().isOk());
        
        // Crear el demografico en la base de datos:
        Demographic demo = new Demographic();
        demo.setName("NIVEL DE IQ");
        demo.setOrigin("O");
        demo.setEncoded(true);
        demo.setObligatory(Short.parseShort("0"));
        demo.setOrdering(Short.parseShort("2"));
        demo.setStatistics(true);
        demo.setLastOrder(true);
        demo.setCanCreateItemInOrder(true);
        demo.getUser().setId(1);
        demo.setModify(true);
        demo.setDemographicItem(1);

        ObjectMapper mapperDemo = new ObjectMapper();
        mapperDemo.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentDemographic = mapperDemo.writeValueAsString(demo);

        mockMvc.perform(post("/api/demographics").header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentDemographic))
                .andExpect(status().isOk());

        // Despues de la creacion de estos dos objetos en la base de datos
        // es posible hacer la auto creacion del demografico:
        // Auto creación item demografico:
        RequestDemographicAuto demoAut = new RequestDemographicAuto(1, 1, "PN01", "CARLOS MENDIETA");   
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demoAut);
        mockMvc.perform(post("/api/integration/ingreso/autoCreationDemographic").header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_04_updateCentralCode() throws Exception
    { 
        // Crear Registro inicial en la tabla de resultados -> lab57
        TestScript.createInitialOrders();
        mockMvc.perform(get("/api/comments/order/201709080001")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
        
        RequestCentralCode rCentralCode = new RequestCentralCode();
        RequestItemCentralCode rItemCentralCode = new RequestItemCentralCode();
        List<RequestItemCentralCode> listCentralCode = new ArrayList<>();
        
        // Se carga el item
        rItemCentralCode.setCentralCode("Yulius is here");
        rItemCentralCode.setIdTest(9);
        
        // Se añade dicho item a la lista del tipo del item
        listCentralCode.add(rItemCentralCode);
        
        /**
         * Y por ultimo se carga el objeto mas grande con la lista 
         * y el otro valor solicitado
         */
        rCentralCode.setIdOrder(Long.parseLong("201709080001"));
        rCentralCode.setItemOrder(listCentralCode);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(rCentralCode);
        
        mockMvc.perform(put("/api/integration/ingreso/updateCentralCode").header(token, "Authorization")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_05_getSampleByIdOrderAndIdTest()throws Exception
    {
        
    }
}
