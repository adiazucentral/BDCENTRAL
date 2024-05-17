/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.masters.configuration.UserType;
import net.cltech.outreach.tools.JWT;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/configuration
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/04/2018
 * @see Creacion
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =
{
    TestAppContext.class
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTypeTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public UserTypeTest()
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
        token = JWT.generate(user, 1);
    }

    @Test
    public void test_01_getUserTypes() throws Exception
    {
        TestScript.execTestUpdateScript("DELETE FROM lab88", null);
        TestScript.execTestUpdateScript("INSERT INTO lab88 (lab88c1, lab88c2, lab88c3, lab88c4, lab88c5) VALUES "
                + "            (1, '', 0, '', 0), "
                + "            (2, '', 0, '', 0), "
                + "            (3, '', 0, '', 0), "
                + "            (4, '', 0, '', 0);"
                + "", null);
        mockMvc.perform(get("/api/usertypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(1, 2, 3, 4)))
                .andExpect(jsonPath("$[*].quantityOrder", containsInAnyOrder(0, 0, 0, 0)));
    }

    @Test
    public void test_02_updateUserTypes() throws Exception
    {
        List<UserType> userTypes = new ArrayList<>();
        UserType userType1 = new UserType();
        userType1.setType(1);
        userType1.setMessage("Medico");
        userType1.setQuantityOrder(10);
        userType1.setImage("");
        userType1.setVisible(true);

        UserType userType2 = new UserType();
        userType2.setType(2);
        userType2.setMessage("Paciente");
        userType2.setQuantityOrder(15);
        userType2.setImage("");
        userType2.setVisible(true);

        UserType userType3 = new UserType();
        userType3.setType(3);
        userType3.setMessage("Cliente");
        userType3.setQuantityOrder(5);
        userType3.setImage("");
        userType3.setVisible(true);

        UserType userType4 = new UserType();
        userType4.setType(4);
        userType4.setMessage("Usuario");
        userType4.setQuantityOrder(20);
        userType4.setImage("");
        userType4.setVisible(true);

        userTypes.add(userType1);
        userTypes.add(userType2);
        userTypes.add(userType3);
        userTypes.add(userType4);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(userTypes);

        mockMvc.perform(put("/api/usertypes")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(content().string("4"));
    }

    @Test
    public void test_03_getUserTypesUpdated() throws Exception
    {
        mockMvc.perform(get("/api/usertypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(1, 2, 3, 4)))
                .andExpect(jsonPath("$[*].message", containsInAnyOrder("Medico", "Paciente", "Cliente", "Usuario")))
                .andExpect(jsonPath("$[*].quantityOrder", containsInAnyOrder(10, 15, 5, 20)));
    }
}
