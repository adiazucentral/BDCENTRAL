package controllers.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.securitynt.domain.common.AuthenticationUser;
import net.cltech.securitynt.domain.common.AuthorizedUser;
import net.cltech.securitynt.domain.common.JWTToken;
import net.cltech.securitynt.domain.masters.configuration.Configuration;
import net.cltech.securitynt.tools.JWT;
import net.cltech.securitynt.tools.Tools;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/license
 *
 * @version 1.0.0
 * @author bvalero
 * @since 07/04/2020
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
public class LicenseTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public LicenseTest() {
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
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()), 1);
    }
    
    private String getToken() throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("lismanager");
        user.setPassword("cltechmanager");
        user.setBranch(1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        token = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(post("/api/authentication")
                .header("Authorization", "")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))), JWTToken.class).getToken();

        return token;
    }
    
    @org.junit.Test
    public void test_01_licencesByInterface() throws Exception
    {
       String keyCode = "0001";

        mockMvc.perform(get("/api/license/keyCode/"+keyCode)
                .contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
}
