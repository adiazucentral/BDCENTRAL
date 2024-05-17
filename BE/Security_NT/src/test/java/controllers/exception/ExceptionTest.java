/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.exception;

import config.MongoTestAppContext;
import config.TestAppContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.cltech.securitynt.domain.common.AuthorizedUser;
import net.cltech.securitynt.domain.masters.configuration.Configuration;
import net.cltech.securitynt.tools.Constants;
import net.cltech.securitynt.tools.JWT;
import net.cltech.securitynt.tools.Tools;
import static org.hamcrest.Matchers.hasSize;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Pruebas unitarias sobre el registro de errores
 *
 * @version 1.0.0
 * @author dcortes
 * @since 25/04/2017
 * @see Para cuando se crea una clase incluir
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    MongoTestAppContext.class,
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExceptionTest
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
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()), Constants.TOKEN_AUTH_USER);
    }

    @Test
    public void test_01_Insert_SqlException() throws Exception
    {
        TestScript.deleteData("lab151");
        mockMvc.perform(post("/api/exceptions/test/sql/exception").header("Authorization", token))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void test_02_Insert_GeneralException() throws Exception
    {
        mockMvc.perform(post("/api/exceptions/test/not_control/exception").header("Authorization", token))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void test_03_NoContentException() throws Exception
    {
        mockMvc.perform(get("/api/exceptions/filter/date/" + 20100101 + "/" + 20100102).header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_04_GetExceptions() throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int initialDate = Integer.parseInt(sdf.format(new Date()));
        mockMvc.perform(get("/api/exceptions/filter/date/" + initialDate + "/" + initialDate).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void test_05_GetSqlExceptions() throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int initialDate = Integer.parseInt(sdf.format(new Date()));
        mockMvc.perform(get("/api/exceptions/filter/date/" + initialDate + "/" + initialDate + "/type/" + 0).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
