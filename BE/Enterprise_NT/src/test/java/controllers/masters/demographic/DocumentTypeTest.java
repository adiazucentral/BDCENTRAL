/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
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
 * Prueba unitaria sobre el api rest /api/documenttypes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 29/08/2017
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
public class DocumentTypeTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public DocumentTypeTest()
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
    public void test_01_getAllDocumentTypesNoContent() throws Exception
    {
        TestScript.deleteData("lab54");
        TestScript.execTestUpdateScript("INSERT INTO lab54(lab54c1, lab54c2, lab54c3, lab54c4, lab04c1, lab07c1) VALUES (1, '00', 'Predeterminado', now(), 1, 1);", null);
        TestScript.execTestUpdateScript("ALTER SEQUENCE lab54_lab54c1_seq RESTART WITH 2");
        mockMvc.perform(get("/api/documenttypes").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertDocumentType() throws Exception
    {
        DocumentType documentType = new DocumentType();
        documentType.setAbbr("TI");
        documentType.setName("Tarjeta de Identidad");
        documentType.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(documentType);

        mockMvc.perform(post("/api/documenttypes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_insertDocumentTypeError() throws Exception
    {
        DocumentType documentType = new DocumentType();
        documentType.setAbbr("TI");
        documentType.setName("");
        documentType.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(documentType);

        mockMvc.perform(post("/api/documenttypes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_04_updateDocumentType() throws Exception
    {
        DocumentType documentType = new DocumentType();
        documentType.setId(2);
        documentType.setAbbr("CC");
        documentType.setName("Cedula de Ciudadania");
        documentType.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(documentType);

        mockMvc.perform(put("/api/documenttypes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.abbr", is("CC")))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_updateDocumentTypeError() throws Exception
    {
        DocumentType documentType = new DocumentType();
        documentType.setId(-1);
        documentType.setAbbr("");
        documentType.setName("Cedula de Ciudadania");
        documentType.getUser().setId(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(documentType);

        mockMvc.perform(put("/api/documenttypes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_06_getAllDocumentTypees() throws Exception
    {
        mockMvc.perform(get("/api/documenttypes").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_07_getDocumentTypeById() throws Exception
    {
        mockMvc.perform(get("/api/documenttypes/" + "2").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_08_getDocumentTypeByName() throws Exception
    {
        mockMvc.perform(get("/api/documenttypes/filter/name/" + "Cedula de Ciudadania").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_08_getDocumentTypeByAbbr() throws Exception
    {
        mockMvc.perform(get("/api/documenttypes/filter/abbr/" + "CC").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_09_getDocumentTypeByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/documenttypes/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_getDocumentTypeByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/documenttypes/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_getDocumentTypeByAbbrNoContent() throws Exception
    {
        mockMvc.perform(get("/api/documenttypes/filter/abbr/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
