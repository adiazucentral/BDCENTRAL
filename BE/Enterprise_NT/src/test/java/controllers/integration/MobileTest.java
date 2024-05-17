package controllers.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import net.cltech.enterprisent.controllers.tools.JWTPatient;
import net.cltech.enterprisent.domain.integration.mobile.AuthenticationData;
import net.cltech.enterprisent.domain.integration.mobile.AuthorizedPatient;
import net.cltech.enterprisent.domain.integration.mobile.ChangePassword;
import net.cltech.enterprisent.domain.integration.mobile.LisPatient;
import net.cltech.enterprisent.domain.integration.mobile.PatientEmailUpdate;
import net.cltech.enterprisent.tools.Constants;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import tools.TestScript;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/test
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 20/02/2020
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
public class MobileTest
{

    public MobileTest()
    {
    }

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
        AuthorizedPatient user = new AuthorizedPatient();
        user.setId(27);
        user.setPatientId("13173393");
        user.setLastName("PEÑA");
        user.setSurName("QUINTERO");
        user.setEmail("jduarte@cltech.net");
        user.setName1("ROXANNE");
        user.setId(27);

        token = JWTPatient.generate(user, Constants.TOKEN_AUTH_USER);

        //token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJFbnRlcnByaXNlIE5UIiwic3ViIjoiTWpBdyIsImxhc3ROYW1lIjoiUEXDkUEiLCJpc3MiOiJDTFRlY2giLCJuYW1lIjoiUk9YQU5ORSIsImlkIjoyMDAsInRva2VuVHlwZSI6MSwiaWF0IjoxNTk4MzYwNzA3LCJ1c2VyIjoiamR1YXJ0ZUBjbHRlY2gubmV0In0.yYm1rK-1f-OcnJRZ5csvpxtFcATtYMwCuvjF1f9W7a4";
    }

    @Test
    public void mobile_01_authenticationsPatient() throws Exception
    {
        TestScript.execTestUpdateScript("INSERT INTO lab21 (lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab80c1, lab21c7, lab21c8, lab21c12, lab04c1, lab54c1, lab21c19, lab21c18)"
                + " VALUES (27, 'zjCefTyTLhfT91p806+vrQ==', 'BMUgg+wpBQs=', 'TBZ6ewnaWF8=', 'm0JR2TStpXw=', 'f9iecHVkW/ZF3FCklmXmPQ==', 8, '1998-03-06 00:00:00', 'jduarte@cltech.net','2020-06-03 09:01:57.664', 45, 1, 'zjCefTyTLhfT91p806+vrQ==', '9hwNWVbYHoE/4yjPGP0PrA==')");

        AuthenticationData autentication = new AuthenticationData();

        autentication.setUser("jduarte@cltech.net");
        autentication.setPassword("13173393");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(autentication);

        mockMvc.perform(post("/api/mobile/authentications/patient")
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());

    }

    @Test
    public void mobile_02_updatePasswordPatient() throws Exception
    {
        ChangePassword changePassword = new ChangePassword();

        changePassword.setPassword("13173393");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(changePassword);

        mockMvc.perform(put("/api/mobile/patients").header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void mobile_03_getListOrdersPatient() throws Exception
    {
        mockMvc.perform(get("/api/mobile/orders/filter/patient/1").header("Authorization", token))
                .andExpect(status().isNoContent());

    }

    @Test
    public void mobile_04_restorePasswordPatient() throws Exception
    {
        LisPatient lisPatient = new LisPatient();

        lisPatient.setId(27);
        lisPatient.setDocumentType("CEDULA DE CIUDADANIA");
        lisPatient.setPatientId("13173393");
        lisPatient.setSecondLastname("QUINTERO");
        lisPatient.setFirstName("ROXANNE");
        lisPatient.setEmail("jduarte@cltech.net");
        lisPatient.setFirsLastname("PEÑA");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(lisPatient);

        mockMvc.perform(put("/api/mobile/patients/restorePassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void mobile_05_getResultPDFPatient() throws Exception
    {
        mockMvc.perform(get("/api/mobile/reports/202001170014").header("Authorization", token))
                .andExpect(status().isNoContent());

    }

    @Test
    public void mobile_06_getEtTest() throws Exception
    {
        mockMvc.perform(get("/api/mobile/tests/filter/viewInOrder").header("Authorization", token))
                .andExpect(status().isNoContent());

    }

    @Test
    public void mobile_07_getPatientByPatientId() throws Exception
    {
        mockMvc.perform(get("/api/mobile/patients/filter/patientId/13173393")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
    
    @Test
    public void mobile_08_patientEmailUpdate() throws Exception
    {
        PatientEmailUpdate patient = new PatientEmailUpdate();
        patient.setId(1);
        patient.setEmail("jduarte@cltech.net");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(patient);

        mockMvc.perform(put("/api/mobile/patientEmailUpdate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isOk());
    }
}
