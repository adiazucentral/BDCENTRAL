package security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestAppContext;
import java.util.Date;
import java.util.List;
import net.cltech.outreach.domain.common.AuthenticationUser;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.common.JWTToken;
import net.cltech.outreach.domain.masters.configuration.UserPassword;
import net.cltech.outreach.tools.Constants;
import net.cltech.outreach.tools.DateTools;
import net.cltech.outreach.tools.JWT;
import net.cltech.outreach.tools.Tools;
import static org.hamcrest.Matchers.is;
import org.junit.Assert;
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
 * Prueba unitaria sobre el api rest /api/authentication
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/05/2018
 * @see Creacion
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthenticationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public AuthenticationTest()
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
        user.setId(-1);
        user.setUserName("administrator");
        user.setName("administrator");
        user.setAdministrator(true);
        user.setType(Constants.USERLIS);
        token = JWT.generate(user, 1);
    }

   

    @Test
    public void test_02_login_administrator() throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("administrator");
        user.setPassword("cltech" + DateTools.dateToNumber(new Date()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .header("Authorization", "")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_03_login_physician() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab19 WHERE lab19c17='a@b.c'", null);
        TestScript.execTestUpdateScript("INSERT INTO lab19(\n"
                + "            lab19c2, lab19c3, lab19c4, lab19c5, lab19c6, lab19c7, \n"
                + "            lab19c8, lab19c9, lab19c10, lab19c11, lab19c12, lab19c13, lab19c14, \n"
                + "            lab19c15, lab19c16, lab19c17, lab19c18, lab19c19, lab19c20, lab19c21, \n"
                + "            lab09c1, lab04c1, lab07c1, lab19c22, lab19c23)\n"
                + "    VALUES ('Edison', 'Quijano', '123456789', '1', 'd', 'd', \n"
                + "            'Bogota', 'C1', '', '', '', '', '', \n"
                + "            1, 1, 'a@b.c', 'deLN3a9Z7Xs=', '1', '2018-10-02 16:21:50.469', '1', \n"
                + "            '1', '1', '1', '1', '1');", null);
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("a@b.c");
        user.setPassword("123456");
        user.setType(Constants.PHYSICIAN);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .header("Authorization", "")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_login_account() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab14 WHERE lab14c24='ab@b.c'", null);
        TestScript.execTestUpdateScript("INSERT INTO lab14(\n"
                + "            lab14c2, lab14c3, lab14c4, lab14c5, lab14c6, lab14c7, \n"
                + "            lab14c8, lab14c9, lab14c10, lab14c11, lab14c12, lab14c13, lab14c14, \n"
                + "            lab14c15, lab14c16, lab14c17, lab14c18, lab14c19, lab14c20, lab14c21, \n"
                + "            lab14c22, lab14c23, lab14c24, lab14c25, lab14c26, lab04c1, lab07c1, \n"
                + "            lab14c28, lab14c29, lab14c30, lab14c31)\n"
                + "    VALUES ('12345', 'lab', '123', '', '', 15200, \n"
                + "            2560.99, 75.69, 0, '', 0, '003', 'dir', \n"
                + "            '', '', '123', 0, 1, 0, 'ab@b.c', \n"
                + "            0, 0, 'ab@b.c', 'deLN3a9Z7Xs=', '2018-12-16 15:03:21.808', 8, 1, \n"
                + "            '003', '1001', 'lab', 0);", null);

        AuthenticationUser user = new AuthenticationUser();
        user.setUser("ab@b.c");
        user.setPassword("123456");
        user.setType(Constants.ACCOUNT);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .header("Authorization", "")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_05_login_userLIS() throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("lismanager");
        user.setPassword("cltechmanager");
        user.setType(Constants.USERLIS);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/authentication")
                .header("Authorization", "")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_06_passwordRecovery_error() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab14 set lab14c21=null", null);

        mockMvc.perform(get("/api/authentication/passwordrecovery/cltech/" + Constants.USERLIS))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("1|wrong user type")));
        mockMvc.perform(get("/api/authentication/passwordrecovery/cltech/" + Constants.USERLIS))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("1|wrong user type")));

        mockMvc.perform(get("/api/authentication/passwordrecovery/cltech/" + Constants.ACCOUNT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("2|user not found")));

        mockMvc.perform(get("/api/authentication/passwordrecovery/ab@b.c/" + Constants.ACCOUNT))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("3|mail not found")));
    }

    @Test
    public void test_07_passwordrecovery_account_ok() throws Exception
    {
        TestScript.execTestUpdateScript("UPDATE lab14 set lab14c21='ab@b.c'", null);

        String jsonResponse = TestTools.getResponseString(mockMvc.perform(get("/api/authentication/passwordrecovery/ab@b.c/" + Constants.ACCOUNT)));
        List<JWTToken> token = Tools.jsonList(jsonResponse, JWTToken.class);

        Assert.assertNotNull(token.get(0).getToken());
        AuthenticationUser user = new AuthenticationUser();
        user.setPassword("123456");

        mockMvc.perform(put("/api/authentication/passwordreset")
                .header("Authorization", token.get(0).getToken())
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void test_08_patient_ok() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab21 WHERE lab21c1 = 1", null);
        TestScript.execTestUpdateScript("INSERT INTO lab21 (lab21c1,lab21c2,lab21c3,lab21c4,lab21c5,lab21c6,lab80c1,lab21c7,lab21c8,lab21c9,lab21c10,lab21c11,lab21c12,lab04c1,lab08c1,lab54c1,lab21c18,lab21c19) VALUES (1, '10IUEsRP64sIZgspdLsKYA==', 'NSjDTJu014Q=', '+5j1V2yNc8Q=', 'SdbsT2RnFj4=', 'K5/kmGNsFgs=', 7, '1970-04-26 18:37:00.051', 'a@b.c', '10', 50, NULL, '2017-09-12 16:38:58.075', 1, 1, 1, 'cltech', 'deLN3a9Z7Xs=')", null);

        String jsonResponse = TestTools.getResponseString(mockMvc.perform(get("/api/authentication/passwordrecovery/a@b.c/" + Constants.PATIENT)));
        List<JWTToken> token = Tools.jsonList(jsonResponse, JWTToken.class);

        Assert.assertNotNull(token.get(0).getToken());
        AuthenticationUser user = new AuthenticationUser();
        user.setPassword("123456");

        mockMvc.perform(put("/api/authentication/passwordreset")
                .header("Authorization", token.get(0).getToken())
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

    }

    @Test
    public void test_09_passwordRecovery_physician_ok() throws Exception
    {

        String jsonResponse = TestTools.getResponseString(mockMvc.perform(get("/api/authentication/passwordrecovery/a@b.c/" + Constants.PHYSICIAN)));
        List<JWTToken> token = Tools.jsonList(jsonResponse, JWTToken.class);

        Assert.assertNotNull(token.get(0).getToken());
        AuthenticationUser user = new AuthenticationUser();
        user.setPassword("123456");

        mockMvc.perform(put("/api/authentication/passwordreset")
                .header("Authorization", token.get(0).getToken())
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

    }

    @Test
    public void test_10_passworupDateok() throws Exception
    {

        UserPassword userPassword = new UserPassword();

        userPassword.setIdUser(142);
        userPassword.setUserName("Tdiaz");
        userPassword.setPasswordOld("123456");
        userPassword.setPasswordNew("funciona");
        userPassword.setType(5);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.writeValueAsString(userPassword);

        mockMvc.perform(put("/api/authentication/updatepassword")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(userPassword)))
                .andExpect(status().isOk());

    }

}
