package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFather;
import net.cltech.enterprisent.domain.masters.demographic.ItemDemographicSon;
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
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/branches
 *
 * @version 1.0.0
 * @author cmartin
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
public class BranchTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public BranchTest()
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
    public void test_01_getAllBranchesNoContent() throws Exception
    {
        mockMvc.perform(get("/api/branches").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_insertBranch() throws Exception
    {
        Branch branch = new Branch();
        branch.setAbbreviation("S1");
        branch.setName("SEDE 1");
        branch.getUser().setId(1);
        branch.setCode("1");
        branch.setResponsable("AAA");
        branch.setEmail("a@b.c");
        branch.setMinimum(2);
        branch.setMaximum(11);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(branch);

        mockMvc.perform(post("/api/branches")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_03_insertBranchError() throws Exception
    {
        Branch branch = new Branch();
        branch.setAbbreviation("S1");
        branch.setName("SEDE 1");
        branch.getUser().setId(1);
        branch.setCode("1");
        branch.setResponsable("AAA");
        branch.setEmail("a@b.c");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(branch);

        mockMvc.perform(post("/api/branches")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_04_updateBranch() throws Exception
    {

        Branch branch = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/branches/filter/abbr/" + "S1").header("Authorization", token))), Branch.class);
        branch.setAbbreviation("S");
        branch.setName("SEDE");
        branch.getUser().setId(1);
        branch.setCode("1");
        branch.setResponsable("AAA");
        branch.setEmail("b@c.com");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(branch);

        mockMvc.perform(put("/api/branches")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.abbreviation", is("S")))
                .andExpect(jsonPath("$.lastTransaction", Matchers.notNullValue()));
    }

    @Test
    public void test_05_updateBranchError() throws Exception
    {
        Branch branch = new Branch();
        branch.setId(-1);
        branch.setAbbreviation("S");
        branch.setName("SEDE");
        branch.getUser().setId(1);
        branch.setCode("1");
        branch.setResponsable("AAA");
        branch.setEmail("a@b.c");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(branch);

        mockMvc.perform(put("/api/branches")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_06_getAllBranches() throws Exception
    {
        mockMvc.perform(get("/api/branches").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_07_getBranchById() throws Exception
    {
        Branch branch = Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/branches/filter/abbr/" + "S").header("Authorization", token))), Branch.class);
        mockMvc.perform(get("/api/branches/" + branch.getId()).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_08_getBranchByName() throws Exception
    {
        mockMvc.perform(get("/api/branches/filter/name/" + "SEDE").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_08_getBranchByAbbr() throws Exception
    {
        mockMvc.perform(get("/api/branches/filter/abbr/" + "S").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_09_getBranchByIdNoContent() throws Exception
    {
        mockMvc.perform(get("/api/branches/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_10_getBranchByNameNoContent() throws Exception
    {
        mockMvc.perform(get("/api/branches/filter/name/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_getBranchByAbbrNoContent() throws Exception
    {
        mockMvc.perform(get("/api/branches/filter/abbr/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_12_getAllBranchesDashBoard() throws Exception
    {
        mockMvc.perform(get("/api/dashboard/Get_Branches").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

}
