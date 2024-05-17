package controllers.operation.statistics;

import config.MongoTestAppContext;
import config.TestAppContext;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;
import net.cltech.enterprisent.domain.operation.statistic.Histogram;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.service.impl.enterprisent.operation.statistic.OpportunityServiceEnterpriseNT;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.TestTools;

/**
 * Prueba unitaria sobre el api rest /api/opportunity Oportunidad
 *
 * @version 1.0.0
 * @author eacuna
 * @since 20/02/2018
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
public class OpportunityTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private String token;
    private List<StatisticOrder> orders;
    private List<Bind> binds;

    public OpportunityTest()
    {
    }

    @Before
    public void setUp() throws Exception
    {

        String ordersString = "[{\n"
                + "  \"orderNumber\": 201802200001,\n"
                + "  \"service\": 1,\n"
                + "  \"serviceCode\": \"S1\",\n"
                + "  \"serviceName\": \"Service1\",\n"
                + "  \"results\": [{\"id\": 1,\"code\": \"01\",\"name\": \"Test1\",\"testState\": 6,\"orderNumber\": 201802200001,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200001,\"entryElapsedTime\": 0,\"printElapsedTime\": 10,\"resultElapsedTime\": 30,\"verifyElapsedTime\": 5,\"validElapsedTime\": 15}\n"
                + "},\n"
                + "{\"id\": 2,\"code\": \"02\",\"name\": \"Test2\",\"testState\": 6,\"orderNumber\": 201802200001,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200001,\"entryElapsedTime\": 0,\"printElapsedTime\": 40,\"resultElapsedTime\": 100,\"verifyElapsedTime\": 0,\"validElapsedTime\": 150}\n"
                + "},\n"
                + "{\"id\": 3,\"code\": \"03\",\"name\": \"Test3\",\"testState\": 6,\"orderNumber\": 201802200001,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200001,\"entryElapsedTime\": 0,\"printElapsedTime\": 150,\"resultElapsedTime\": 10,\"verifyElapsedTime\": 0,\"validElapsedTime\": 122}\n"
                + "},\n"
                + "{\"id\": 4,\"code\": \"04\",\"name\": \"Test4\",\"testState\": 6,\"orderNumber\": 201802200001,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200001,\"entryElapsedTime\": 0,\"printElapsedTime\": 5,\"resultElapsedTime\": 10,\"verifyElapsedTime\": 0,\"validElapsedTime\": 7}\n"
                + "}]\n"
                + "},\n"
                + "{\n"
                + "  \"orderNumber\": 201802200002,\n"
                + "  \"service\": 1,\n"
                + "  \"serviceCode\": \"S1\",\n"
                + "  \"serviceName\": \"Service1\",\n"
                + "  \"results\": [{\"id\": 1,\"code\": \"01\",\"name\": \"Test1\",\"testState\": 6,\"orderNumber\": 201802200002,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200002,\"entryElapsedTime\": 0,\"printElapsedTime\": 15,\"resultElapsedTime\": 500,\"verifyElapsedTime\": 0,\"validElapsedTime\": 50}\n"
                + "},\n"
                + "{\"id\": 2,\"code\": \"02\",\"name\": \"Test2\",\"testState\": 6,\"orderNumber\": 201802200002,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200002,\"entryElapsedTime\": 0,\"printElapsedTime\": 40,\"resultElapsedTime\": 100,\"verifyElapsedTime\": 0,\"validElapsedTime\": 1000}\n"
                + "},\n"
                + "{\"id\": 3,\"code\": \"03\",\"name\": \"Test3\",\"testState\": 6,\"orderNumber\": 201802200002,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200002,\"entryElapsedTime\": 0,\"printElapsedTime\": 150,\"resultElapsedTime\": 10,\"verifyElapsedTime\": 0,\"validElapsedTime\": 60}\n"
                + "},\n"
                + "{\"id\": 4,\"code\": \"04\",\"name\": \"Test4\",\"testState\": 6,\"orderNumber\": 201802200002,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200002,\"entryElapsedTime\": 0,\"printElapsedTime\": 5,\"resultElapsedTime\": 10,\"verifyElapsedTime\": 0,\"validElapsedTime\": 1110}\n"
                + "}]\n"
                + "},\n"
                + "{\n"
                + "  \"orderNumber\": 201802200003,\n"
                + "  \"service\": 1,\n"
                + "  \"serviceCode\": \"S1\",\n"
                + "  \"serviceName\": \"Service1\",\n"
                + "  \"results\": [{\"id\": 1,\"code\": \"01\",\"name\": \"Test1\",\"testState\": 6,\"orderNumber\": 201802200003,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200003,\"entryElapsedTime\": 0,\"printElapsedTime\": 15,\"resultElapsedTime\": 500,\"verifyElapsedTime\": 0,\"validElapsedTime\": 400}\n"
                + "},\n"
                + "{\"id\": 2,\"code\": \"02\",\"name\": \"Test2\",\"testState\": 6,\"orderNumber\": 201802200003,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200003,\"entryElapsedTime\": 0,\"printElapsedTime\": 40,\"resultElapsedTime\": 100,\"verifyElapsedTime\": 0,\"validElapsedTime\": 200}\n"
                + "},\n"
                + "{\"id\": 3,\"code\": \"03\",\"name\": \"Test3\",\"testState\": 6,\"orderNumber\": 201802200003,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200003,\"entryElapsedTime\": 0,\"printElapsedTime\": 150,\"resultElapsedTime\": 10,\"verifyElapsedTime\": 0,\"validElapsedTime\": 300}\n"
                + "},\n"
                + "{\"id\": 4,\"code\": \"04\",\"name\": \"Test4\",\"testState\": 6,\"orderNumber\": 201802200003,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200003,\"entryElapsedTime\": 0,\"printElapsedTime\": 5,\"resultElapsedTime\": 10,\"verifyElapsedTime\": 0,\"validElapsedTime\": 1210}\n"
                + "}]\n"
                + "},\n"
                + "{\n"
                + "  \"orderNumber\": 201802200004,\n"
                + "  \"service\": 1,\n"
                + "  \"serviceCode\": \"S1\",\n"
                + "  \"serviceName\": \"Service1\",\n"
                + "  \"results\": [{\"id\": 1,\"code\": \"01\",\"name\": \"Test1\",\"testState\": 6,\"orderNumber\": 201802200004,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200004,\"entryElapsedTime\": 0,\"printElapsedTime\": 15,\"resultElapsedTime\": 30,\"verifyElapsedTime\": 0,\"validElapsedTime\": 25}\n"
                + "},\n"
                + "{\"id\": 2,\"code\": \"02\",\"name\": \"Test2\",\"testState\": 6,\"orderNumber\": 201802200004,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200004,\"entryElapsedTime\": 0,\"printElapsedTime\": 15,\"resultElapsedTime\": 30,\"verifyElapsedTime\": 0,\"validElapsedTime\": 25}\n"
                + "},\n"
                + "{\"id\": 3,\"code\": \"03\",\"name\": \"Test3\",\"testState\": 6,\"orderNumber\": 201802200004,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200004,\"entryElapsedTime\": 0,\"printElapsedTime\": 15,\"resultElapsedTime\": 30,\"verifyElapsedTime\": 0,\"validElapsedTime\": 25}\n"
                + "},\n"
                + "{\"id\": 4,\"code\": \"04\",\"name\": \"Test4\",\"testState\": 6,\"orderNumber\": 201802200004,\n"
                + "\"opportunityTimes\": {\"id\": 1,\"orderNumber\": 201802200004,\"entryElapsedTime\": 0,\"printElapsedTime\": 5,\"resultElapsedTime\": 10,\"verifyElapsedTime\": 0,\"validElapsedTime\": 1210}\n"
                + "}]\n"
                + "}]\n"
                + "";
        String bindString = "[{\"id\": 1,\"name\": \"1\",\"minimum\": 0,\"maximum\": 100},{\"id\": 2,\"name\": \"2\",\"minimum\": 100,\"maximum\": 200},{\"id\": 3,\"name\": \"3\",\"minimum\": 200,\"maximum\": 400},{\"id\": 4,\"name\": \"4\",\"minimum\": 400,\"maximum\": 800},{\"id\": 5,\"name\": \"5\",\"minimum\": 800}]";
        orders = Tools.jsonList(ordersString, StatisticOrder.class);
        binds = Tools.jsonList(bindString, Bind.class);

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        AuthorizedUser user = new AuthorizedUser();
        user.setUserName("tests");
        user.setLastName("Pruebas");
        user.setName("CLTech");
        user.setBranch(1);
        user.setId(1);
        token = JWT.generate(user, Integer.parseInt(Tools.jsonObject(TestTools.getResponseString(mockMvc.perform(get("/api/configuration/tokenexpiration"))), Configuration.class).getValue()));
    }

    @org.junit.Test
    public void test_01_histogram() throws Exception
    {
        OpportunityServiceEnterpriseNT op = new OpportunityServiceEnterpriseNT();

        Histogram histogram = new Histogram();
        histogram.setBinds(binds);
        orders = orders.stream().map(order -> order.setResults(op.validTimeFilter(order.getResults(), 0, true, true)))
                .filter(order -> !order.getResults().isEmpty())
                .collect(Collectors.toList());
        histogram.setDetail(op.histogramData(orders, histogram, true));

        op.calculateStatisticsValues(histogram);

        Assert.assertEquals(462.75, histogram.getMean(), 0);
        Assert.assertEquals(275, histogram.getMedian(), 0);
        Assert.assertEquals(476, histogram.getStandardDeviation().longValue());
        Assert.assertEquals(55, histogram.getPercentile25(), 0);
        Assert.assertEquals(1050, histogram.getPercentile75(), 0);
        Assert.assertEquals(55, histogram.getMode()[0], 0);

    }

}
