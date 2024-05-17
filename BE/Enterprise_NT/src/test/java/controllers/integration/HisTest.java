package controllers.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.his.AuthorizedBranch;
import net.cltech.enterprisent.domain.integration.his.UserHis;
import net.cltech.enterprisent.domain.integration.his.UserStatus;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/his
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/02/2021
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
public class HisTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public HisTest()
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
    public void test_01_insertUser() throws Exception
    {
        UserHis user = new UserHis();
        List<AuthorizedBranch> branches = new ArrayList<>();
        AuthorizedBranch branch = new AuthorizedBranch("SDBQ1", "Sede De Bogotá Barrio Quiroga 2");
        branches.add(branch);
        user.setSedes(branches);
        user.setNombres("Carlos");
        user.setApellidos("Martin");
        user.setUsuario("cmartin");
        user.setContraseña("12345");
        user.setCorreo("a@b.c");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);

        mockMvc.perform(post("/api/his/usuario")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_02_changestatususer() throws Exception
    {
        UserStatus user = new UserStatus();
        user.setUsuario("cmartin");
        user.setEstado(1);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(user);
                
        mockMvc.perform(put("/api/his/users/changestate")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
                
    }
    
    @Test
    public void test_03_createBranch() throws Exception
    {
        Branch branch = new Branch();
        branch.setCode("SDF1");
        branch.setName("Santo día festivo");
        branch.setAbbreviation("SDF");
        branch.setAddress("Calle falsisima 1699");
        branch.setPhone("3368875");
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(branch);
                
        mockMvc.perform(post("/api/his/sede")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_04_updateBranch() throws Exception
    {
        Branch branch = new Branch();
        branch.setCode("SDF1");
        branch.setName("Santo día festivo 23");
        branch.setAbbreviation("SDF3");
        branch.setAddress("Calle falsisima 16991");
        branch.setPhone("3368875#");
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(branch);
                
        mockMvc.perform(put("/api/his/sede")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}
