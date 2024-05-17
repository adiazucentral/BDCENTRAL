package controllers.masters.configuration;

import config.TestAppContext;
import net.cltech.outreach.domain.common.AuthorizedUser;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Prueba unitaria al api rest /api/demographicwebquery
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 29/01/2020
 * @see Creación
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes
        = {
            TestAppContext.class
        })
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DemographicControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
   

    public DemographicControllerTest() {
    }

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        AuthorizedUser user = new AuthorizedUser();
        user.setId(-1);
        user.setUserName("tests");
        user.setLastName("Pruebas");
        user.setName("CLTech");
        user.setAdministrator(true);
    }

 
    @Test
    public void test_01_demographicQuery() throws Exception {
        mockMvc.perform(get("/api/demographic/webquery"))
                .andExpect(status().isNoContent());
        
        
    }
    
      @Test
    public void test_01_demographicQueryff() throws Exception {
        mockMvc.perform(get("/api/results/updateResultForEx"))
                .andExpect(status().isOk());
        
        
        
    }
    
    

}
