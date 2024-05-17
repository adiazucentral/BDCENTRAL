/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.tools;

import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.HashMap;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/encode/
 *
 * @version 1.0.0
 * @author omendez
 * @since 13/12/2021
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
@Transactional(transactionManager = "transactionManager")
public class EncodeTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public EncodeTest()
    {
    }

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }
    
    @Test
    public void test_01_encrypt() throws Exception
    {
        
        HashMap<String, String> map = new HashMap<>();
        map.put("Valor 1", "Valor 1");
        map.put("Valor 2", "Valor 2");
        map.put("Valor 3", "Valor 3");
        
        mockMvc.perform(patch("/api/encode/encrypt")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(map)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void test_02_decrypt() throws Exception
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("WUp0oIgwVPM=", "WUp0oIgwVPM=");
        map.put("6SnA79uS82o=", "6SnA79uS82o=");
        map.put("6SnA79uS82o=", "6SnA79uS82o=");
        
        mockMvc.perform(patch("/api/encode/decrypt")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(Tools.jsonObject(map)))
                .andExpect(status().isOk());
    }
}
