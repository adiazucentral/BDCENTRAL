package controllers.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicItemDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.StandardizationDemographic;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba requirementaria sobre el api rest
 * /api/centralsystems/standardization/demographics
 *
 * @version 1.0.0
 * @author cmartin
 * @since 03/08/2017
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
public class StandardizationDemographicTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DemographicDao demographicDao;
    @Autowired
    private DemographicItemDao demographicItemDao;
    private MockMvc mockMvc;
    private String token;

    public StandardizationDemographicTest()
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
    public void test_01_getAllDemographics() throws Exception
    {
        mockMvc.perform(get("/api/demographics/all").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_02_getAllDemographicsItemsByCentralSystem() throws Exception
    {
        Demographic demographic = new Demographic();
        demographic.setName("TARIFA");
        demographic.setOrigin("O");
        demographic.setEncoded(false);
        demographic.setObligatory(Short.parseShort("0"));
        demographic.setOrdering(Short.parseShort("1"));
        demographic.setStatistics(true);
        demographic.setLastOrder(true);
        demographic.setCanCreateItemInOrder(true);
        demographic.getUser().setId(1);
        demographic.setModify(true);
        demographic.setDemographicItem(1);

        demographicDao.create(demographic);

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

        demographicItemDao.create(demographicitem);
    }

    @Test
    public void test_03_addStandardizationDemographic() throws Exception
    {
        StandardizationDemographic demographic = new StandardizationDemographic();
        demographic.setId(1);
        demographic.getDemographic().setId(1);
        demographic.getDemographicItem().setId(1);
        demographic.getCentralCode().add("COD1");
        demographic.getCentralCode().add("COD2");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographic);

        mockMvc.perform(post("/api/centralsystems/standardization/demographics")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }

    @Test
    public void test_04_getAllDemographicsItemsByCentralSystem() throws Exception
    {
        mockMvc.perform(get("/api/centralsystems/standardization/demographics/system/1/demographic/1").header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void test_05_getCodeExistsTrue() throws Exception
    {
        mockMvc.perform(get("/api/centralsystems/standardization/demographics/exists/1/1/3/COD1")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void test_06_getCodeExistsFalse() throws Exception
    {
        mockMvc.perform(get("/api/centralsystems/standardization/demographics/exists/2/1/2/COD1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void test_06_getImport() throws Exception
    {
        List<StandardizationDemographic> demographics = new ArrayList<>();
        StandardizationDemographic demographic = new StandardizationDemographic();
        demographic.setId(1);
        demographic.getDemographic().setId(1);
        demographic.getDemographicItem().setId(2);
        demographic.getCentralCode().add("COD11");

        StandardizationDemographic demographic2 = new StandardizationDemographic();
        demographic2.setId(1);
        demographic2.getDemographic().setId(1);
        demographic2.getDemographicItem().setId(2);
        demographic2.getCentralCode().add("COD12");

        StandardizationDemographic demographic3 = new StandardizationDemographic();
        demographic3.setId(1);
        demographic3.getDemographic().setId(1);
        demographic3.getDemographicItem().setId(2);
        demographic3.getCentralCode().add("COD13");

        StandardizationDemographic demographic4 = new StandardizationDemographic();
        demographic4.setId(1);
        demographic4.getDemographic().setId(1);
        demographic4.getDemographicItem().setId(3);
        demographic4.getCentralCode().add("COD9");

        demographics.add(demographic);
        demographics.add(demographic2);
        demographics.add(demographic3);
        demographics.add(demographic4);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonContent = mapper.writeValueAsString(demographics);

        mockMvc.perform(post("/api/centralsystems/standardization/demographics/import")
                .header("Authorization", token)
                .contentType(TestTools.APPLICATION_JSON_UTF8)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestTools.APPLICATION_JSON_UTF8));
    }
}
