/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.hamcrest.Matchers;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/serviceprint
 *
 * @version 1.0.0
 * @author equijano
 * @since 25/06/2019
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
public class ServicePrintTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ServicePrintTest()
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
        user.setId(-1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_deleteAll() throws Exception
    {
        mockMvc.perform(delete("/api/serviceprint/deleteall")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_02_listNoContent() throws Exception
    {
        mockMvc.perform(get("/api/serviceprint")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_03_Create() throws Exception
    {
        SerialPrint serialPrint = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(post("/api/reports/serial")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())), SerialPrint.class);
        Branch branch = new Branch();
        branch.setId(1);
        serialPrint.setBranch(branch);
        ServiceLaboratory service = new ServiceLaboratory();
        service.setId(1);
        serialPrint.setService(service);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(serialPrint);

        mockMvc.perform(post("/api/serviceprint")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_04_list() throws Exception
    {
        mockMvc.perform(get("/api/serviceprint")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(1)));
    }

    @org.junit.Test
    public void test_05_CreateAll() throws Exception
    {
        List<SerialPrint> list = new ArrayList<>();
        SerialPrint serialPrint = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(post("/api/reports/serial")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())), SerialPrint.class);
        Branch branch = new Branch();
        branch.setId(1);
        serialPrint.setBranch(branch);
        ServiceLaboratory service = new ServiceLaboratory();
        service.setId(2);
        serialPrint.setService(service);
        list.add(serialPrint);
        serialPrint = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(post("/api/reports/serial")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())), SerialPrint.class);
        branch = new Branch();
        branch.setId(2);
        serialPrint.setBranch(branch);
        service = new ServiceLaboratory();
        service.setId(2);
        serialPrint.setService(service);

        list.add(serialPrint);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(list);

        mockMvc.perform(post("/api/serviceprint/createall")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_06_list() throws Exception
    {
        mockMvc.perform(get("/api/serviceprint")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(3)));
    }

    @org.junit.Test
    public void test_07_Delete() throws Exception
    {
        SerialPrint serialPrint = Tools.jsonList(TestTools.getResponseString(mockMvc.perform(get("/api/serviceprint")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())), SerialPrint.class).get(0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(serialPrint);

        mockMvc.perform(delete("/api/serviceprint")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_08_list() throws Exception
    {
        mockMvc.perform(get("/api/serviceprint")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", Matchers.hasSize(2)));
    }

    @org.junit.Test
    public void test_09_getByService() throws Exception
    {
        mockMvc.perform(get("/api/serviceprint/1/2")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_10_deleteAll() throws Exception
    {
        mockMvc.perform(delete("/api/serviceprint/deleteall")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_11_listNoContent() throws Exception
    {
        mockMvc.perform(get("/api/serviceprint")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

}
