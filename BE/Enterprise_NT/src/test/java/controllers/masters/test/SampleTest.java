/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.SampleByService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
 * Prueba unitaria sobre el api rest /api/samples
 *
 * @version 1.0.0
 * @author enavas
 * @since 02/05/2017
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
public class SampleTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public SampleTest()
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
    public void test_01_getnotallSamples() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab24", null);
        mockMvc.perform(get("/api/samples").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_addSample() throws Exception
    {

        Sample sample = new Sample();
        AuthorizedUser usu = new AuthorizedUser();
        sample.setId(0);
        sample.setName("Prueba");
        sample.setPrintable(true);
        sample.setCanstiker(1);
        sample.setCheck(true);
        sample.setManagementsample("");
        sample.setDaysstored(1);
        sample.getContainer().setId(1);
        sample.setCodesample("PRU");
        sample.setLaboratorytype("2");
        sample.setTypebarcode(true);

        usu.setId(1);
        sample.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sample);

        mockMvc.perform(post("/api/samples")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(sample.getName())));
    }

    @Test
    public void test_03_getAllSample() throws Exception
    {
        mockMvc.perform(get("/api/samples").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_erroraddSample() throws Exception
    {

        Sample sample = new Sample();
        AuthorizedUser usu = new AuthorizedUser();
        sample.setId(0);
        sample.setName("Prueba");
        sample.setPrintable(true);
        sample.setCanstiker(1);
        sample.setCheck(true);
        sample.setManagementsample("");
        sample.setDaysstored(1);
        sample.getContainer().setId(1);
        sample.setCodesample("PRU");
        sample.setLaboratorytype("2");
        sample.setTypebarcode(true);

        usu.setId(1);
        sample.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sample);

        mockMvc.perform(post("/api/containers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void test_05_modifySample() throws Exception
    {

        Sample sample = new Sample();
        AuthorizedUser usu = new AuthorizedUser();
        sample.setId(1);
        sample.setName("Prueba2");
        sample.setPrintable(true);
        sample.setCanstiker(1);
        sample.setCheck(true);
        sample.setManagementsample("");
        sample.setDaysstored(1);
        sample.getContainer().setId(1);
        sample.setCodesample("PRU");
        sample.setLaboratorytype("2");
        sample.setTypebarcode(true);
        usu.setId(1);
        sample.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sample);

        mockMvc.perform(put("/api/samples")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(sample.getName())));
    }

    @Test
    public void test_06_errormodifySample() throws Exception
    {

        Sample sample = new Sample();
        AuthorizedUser usu = new AuthorizedUser();
        sample.setId(1);
        sample.setName("Prueba2");
        sample.setPrintable(true);
        sample.setCanstiker(1);
        sample.setCheck(true);
        sample.setManagementsample("");
        sample.setDaysstored(1);
        sample.getContainer().setId(1);
        sample.setCodesample("PRU");
        sample.setLaboratorytype("2");
        sample.setTypebarcode(true);
        usu.setId(1);
        sample.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(sample);

        mockMvc.perform(put("/api/containers")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_07_getidSample() throws Exception
    {
        mockMvc.perform(get("/api/samples/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_08_errorgetidSample() throws Exception
    {
        mockMvc.perform(get("/api/samples/filter/id/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getnameSample() throws Exception
    {
        mockMvc.perform(get("/api/samples/filter/name/" + "prueba2").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_10_errorgetnameSample() throws Exception
    {
        mockMvc.perform(get("/api/samples/filter/name/" + "prueba").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_getcodeSample() throws Exception
    {
        mockMvc.perform(get("/api/samples/filter/code/" + "pru").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_12_errorgetcodeSample() throws Exception
    {
        mockMvc.perform(get("/api/samples/filter/code/" + "pra").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_13_getcontainerSample() throws Exception
    {
        mockMvc.perform(get("/api/samples/filter/container/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_14_errorgetcontainerSample() throws Exception
    {
        mockMvc.perform(get("/api/samples/filter/container/" + "2").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    public void test_15_insertSamplesByservice() throws Exception
    {
        SampleByService sample = new SampleByService();
        sample.getService().setId(1);
        sample.getSample().setId(1);
        sample.setExpectedTime(10);

        ObjectMapper mapperT = new ObjectMapper();
        mapperT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContentT = mapperT.writeValueAsString(sample);

        mockMvc.perform(post("/api/samples/services")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContentT))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void test_16_getAllAutomaticTest() throws Exception
    {
        mockMvc.perform(get("/api/samples/services/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

}
