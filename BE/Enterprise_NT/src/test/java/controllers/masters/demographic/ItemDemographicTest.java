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
import net.cltech.enterprisent.domain.masters.demographic.BranchDemographic;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFather;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DemographicRequired;
import net.cltech.enterprisent.domain.masters.demographic.ItemDemographicSon;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
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
 * Prueba unitaria sobre el api rest /api/demographicitems
 *
 * @version 1.0.0
 * @author enavas
 * @since 09/05/2017
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
public class ItemDemographicTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String token;

    public ItemDemographicTest()
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
    public void test_01_getnotallDemographicitems() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_02_addDemographicitem() throws Exception
    {

        DemographicItem demographicitem = new DemographicItem();
        AuthorizedUser usu = new AuthorizedUser();

        demographicitem.setId(0);
        demographicitem.setCode("TRAUNI");
        demographicitem.setName("TARIFICA UNICA");
        demographicitem.setDescription("DESCRIPCION DE LA TARIFA");
        demographicitem.setDemographic(1);
        demographicitem.setDefaultItem(true);
        usu.setId(1);
        demographicitem.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicitem);

        mockMvc.perform(post("/api/demographicitems")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(demographicitem.getName())));
    }

    @Test
    public void test_03_getallDemographicitems() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_04_erroraddDemographicitem() throws Exception
    {

        DemographicItem demographicitem = new DemographicItem();
        AuthorizedUser usu = new AuthorizedUser();

        demographicitem.setId(0);
        demographicitem.setCode("TRAUNI");
        demographicitem.setName("prueba");
        demographicitem.setDescription("Descripcion prueba");
        demographicitem.setDemographic(1);
        usu.setId(1);
        demographicitem.setUser(usu);
        demographicitem.setDefaultItem(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicitem);

        mockMvc.perform(post("/api/demographicitems")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_05_modifyDemographicitem() throws Exception
    {

        DemographicItem demographicitem = new DemographicItem();
        AuthorizedUser usu = new AuthorizedUser();

        demographicitem.setId(1);
        demographicitem.setCode("PRU");
        demographicitem.setName("PRUEBA");
        demographicitem.setDescription("DESCRIPCION DE LA PRUEBA");
        demographicitem.setState(true);
        demographicitem.setDemographic(1);
        demographicitem.setDefaultItem(true);
        usu.setId(1);
        demographicitem.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicitem);

        mockMvc.perform(put("/api/demographicitems")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(demographicitem.getName())));
    }

    @Test
    public void test_06_errormodifyDemographicitem() throws Exception
    {

        DemographicItem demographicitem = new DemographicItem();
        AuthorizedUser usu = new AuthorizedUser();

        demographicitem.setId(-1);
        demographicitem.setCode("pru2");
        demographicitem.setName("prueba2");
        demographicitem.setState(true);
        demographicitem.setDescription("Descripcion prueba");
        demographicitem.setDemographic(1);
        demographicitem.setDefaultItem(false);
        usu.setId(1);
        demographicitem.setUser(usu);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicitem);

        mockMvc.perform(put("/api/demographicitems")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_07_getidDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/id/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_08_errorgetidDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/id/" + "-1").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_09_getnameDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/name/" + "PRUEBA").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_10_errorgetnameDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/name/" + "prueba2017").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_11_getcodeDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/code/" + "PRU").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_12_errorgetcodeDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/code/" + "pru2017").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_13_getdemographicDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/demographic/" + "1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_14_errorgetdemographicDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/demographic/" + "100155").header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_15_getstateDemographicitem() throws Exception
    {
        mockMvc.perform(get("/api/demographicitems/filter/state/" + "true").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_16_getAllDemographicsByBranch() throws Exception
    {
        mockMvc.perform(get("/api/branchitem/itemsdemographic/branch/1/demographic/-1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_17_saveDemographicSave() throws Exception
    {
        List<BranchDemographic> listDemo = new ArrayList<>();

        Branch branch = new Branch();
        branch.setId(1);
        Demographic demographic = new Demographic();
        demographic.setId(1);
        DemographicItem demographicItem = new DemographicItem();
        demographicItem.setId(1);
        BranchDemographic newObject = new BranchDemographic();
        newObject.setId(1);
        newObject.setDemographic(demographic);
        newObject.setDemographicItem(demographicItem);
        // Guardo ese objeto en la lista
        listDemo.add(newObject);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(listDemo);

        mockMvc.perform(post("/api/branchitem/itemsdemographic/saves")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_18_getAllDemographicsByBranch() throws Exception
    {
        mockMvc.perform(get("/api/branchitem/branch/1")
                .header("Authorization", token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_19_saveDemographicSave() throws Exception
    {
        List<BranchDemographic> listDemo = new ArrayList<>();

        Branch branch = new Branch();
        branch.setId(1);
        Demographic demographic = new Demographic();
        demographic.setId(1);
        DemographicItem demographicItem = new DemographicItem();
        demographicItem.setId(1);
        BranchDemographic newObject = new BranchDemographic();
        newObject.setId(1);
        newObject.setDemographic(demographic);
        newObject.setDemographicItem(demographicItem);
        // Guardo ese objeto en la lista
        listDemo.add(newObject);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(listDemo);

        mockMvc.perform(post("/api/branchitem/saves")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void test_20_valuerequired() throws Exception
    {

        DemographicRequired demographic = new DemographicRequired();
        AuthorizedUser usu = new AuthorizedUser();

        List<DemographicRequired> listDemo = new ArrayList<>();

        demographic.setDefaultValueRequired("Test1");
        demographic.setIdDemographic(1);

        // Guardo ese objeto en la lista
        listDemo.add(demographic);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(listDemo);

        mockMvc.perform(put("/api/branchitem/demographics/valuerequired")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());

    }

    @Test
    public void test_21_getListSons() throws Exception
    {
        mockMvc.perform(get("/api/demographicsitemssons/idfather/1/idfatheritem/1/idson/47").header("Authorization", token))
                .andExpect(status().isNoContent());

    }

    @Test
    public void test_22_updateDemographicFather() throws Exception
    {
        DemographicFather demographicFather = new DemographicFather();
        ItemDemographicSon son1 = new ItemDemographicSon();
        ItemDemographicSon son2 = new ItemDemographicSon();
        List<ItemDemographicSon> demographics = new ArrayList<>();

        son1.setId(54);
        son1.setCode("PN84");
        son1.setName("BUENAS84");
        son1.setSelected(true);
        demographics.add(son1);
        son2.setId(57);
        son2.setCode("79152223");
        son2.setName("GUTIERREZ APARICIO ANDRES");
        son2.setSelected(true);
        demographics.add(son2);

        demographicFather.setIdDemographicFather(1);
        demographicFather.setIdDemographicFatherItem(1);
        demographicFather.setIdDemographicSon(47);
        demographicFather.setDemographicSonItems(demographics);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographicFather);

        mockMvc.perform(post("/api/demographicsitemssons")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk());

    }

    @Test
    public void test_23_listSon() throws Exception
    {
        mockMvc.perform(get("/api/demographicsitemssons/idfather/1/idfatheritem/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_24_getIdDemographicSon() throws Exception
    {
        mockMvc.perform(get("/api/demographicsitemssons/idfather/1")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

}
