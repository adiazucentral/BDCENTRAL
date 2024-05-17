/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.masters.tracking.SampleOportunity;
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
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/destinations
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/06/2017
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
public class DestinationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public DestinationTest()
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
    public void test_01_getAllDestinationNoContent() throws Exception
    {
        mockMvc.perform(get("/api/destinations").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertDestination() throws Exception
    {
        Destination destination = new Destination();
        destination.setCode("01");
        destination.setName("Microbiologia");
        destination.setDescription("Microbiologia");
        destination.setColor("#000000");
        destination.getType().setId(45);
        destination.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destination);

        mockMvc.perform(post("/api/destinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("01")))
                .andExpect(jsonPath("$.name", is("Microbiologia")))
                .andExpect(jsonPath("$.description", is("Microbiologia")))
                .andExpect(jsonPath("$.color", is("#000000")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_getAllDestination() throws Exception
    {
        mockMvc.perform(get("/api/destinations").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getDestinationById() throws Exception
    {
        mockMvc.perform(get("/api/destinations/" + "1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("01")))
                .andExpect(jsonPath("$.name", is("Microbiologia")))
                .andExpect(jsonPath("$.description", is("Microbiologia")))
                .andExpect(jsonPath("$.color", is("#000000")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_getDestinationByCode() throws Exception
    {
        mockMvc.perform(get("/api/destinations/filter/code/" + "01").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("01")))
                .andExpect(jsonPath("$.name", is("Microbiologia")))
                .andExpect(jsonPath("$.description", is("Microbiologia")))
                .andExpect(jsonPath("$.color", is("#000000")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_06_getDestinationByName() throws Exception
    {
        mockMvc.perform(get("/api/destinations/filter/name/" + "Microbiologia").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("01")))
                .andExpect(jsonPath("$.name", is("Microbiologia")))
                .andExpect(jsonPath("$.description", is("Microbiologia")))
                .andExpect(jsonPath("$.color", is("#000000")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_07_getDestinationByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/destinations/" + "0").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_08_getDestinationByCodeNoContent() throws Exception
    {
        mockMvc.perform(get("/api/destinations/filter/code/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getDestinationByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/destinations/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_insertDestinationError() throws Exception
    {
        Destination destination = new Destination();
        destination.setCode("01");
        destination.setName("Microbiologia");
        destination.setDescription("Microbiologia");
        destination.setColor("#000000");
        destination.getType().setId(7);
        destination.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destination);

        mockMvc.perform(post("/api/destinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_11_updateDestination() throws Exception
    {
        Destination destination = new Destination();
        destination.setId(1);
        destination.setCode("01");
        destination.setName("Quimica");
        destination.setDescription("Quimica");
        destination.setColor("#000000");
        destination.getType().setId(45);
        destination.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destination);

        mockMvc.perform(put("/api/destinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code", is("01")))
                .andExpect(jsonPath("$.name", is("Quimica")))
                .andExpect(jsonPath("$.description", is("Quimica")))
                .andExpect(jsonPath("$.color", is("#000000")))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_12_updateDestinationError() throws Exception
    {
        Destination destination = new Destination();
        destination.setId(-1);
        destination.setCode("01");
        destination.setName("Quimica");
        destination.setDescription("Quimica");
        destination.setColor("#000000");
        destination.getType().setId(7);
        destination.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destination);

        mockMvc.perform(put("/api/destinations")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_14_insertAssignmentDestination() throws Exception
    {
        AssignmentDestination assignment = new AssignmentDestination();
        assignment.getBranch().setId(1);
        assignment.getSample().setId(1);
        assignment.getOrderType().setId(1);
        assignment.getUser().setId(1);

        DestinationRoute destinationRoute = new DestinationRoute();
        destinationRoute.getDestination().setId(1);
        destinationRoute.setOrder(1);

        TestBasic test1 = new TestBasic();
        test1.setId(1);
        TestBasic test2 = new TestBasic();
        test2.setId(2);

        destinationRoute.getTests().add(test1);
        destinationRoute.getTests().add(test2);

        assignment.getDestinationRoutes().add(destinationRoute);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(assignment);

        mockMvc.perform(post("/api/destinations/assignment")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_15_insertSampleOportunityDestination() throws Exception
    {
        List<DestinationRoute> destinations = new ArrayList<>();

        DestinationRoute destinationRoute = new DestinationRoute();
        destinationRoute.setId(1);

        SampleOportunity oportunity1 = new SampleOportunity();
        oportunity1.getService().setId(1);
        oportunity1.setExpectedTime(10);
        oportunity1.setMaximumTime(15);

        SampleOportunity oportunity2 = new SampleOportunity();
        oportunity2.getService().setId(2);
        oportunity2.setExpectedTime(50);
        oportunity2.setMaximumTime(60);

        destinationRoute.getSampleOportunitys().add(oportunity1);
        destinationRoute.getSampleOportunitys().add(oportunity2);

        destinations.add(destinationRoute);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(destinations);

        mockMvc.perform(post("/api/destinations/oportunity")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @Test
    public void test_16_getSampleOportunityContent() throws Exception
    {
        mockMvc.perform(get("/api/destinations/oportunity/branch/1/sample/1/typeorder/1").header("Authorization", token))
                .andExpect(status().isOk());
    }

}
