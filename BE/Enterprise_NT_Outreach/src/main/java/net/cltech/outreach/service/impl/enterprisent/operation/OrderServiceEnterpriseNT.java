package net.cltech.outreach.service.impl.enterprisent.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.outreach.dao.interfaces.operation.OrderDao;
import net.cltech.outreach.dao.interfaces.security.AuthenticationDao;
import net.cltech.outreach.domain.common.AuthenticationUser;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.common.JWTToken;
import net.cltech.outreach.domain.demographic.Demographic;
import net.cltech.outreach.domain.demographic.QueryDemographic;
import net.cltech.outreach.domain.exception.EnterpriseNTException;
import net.cltech.outreach.domain.exception.WebException;
import net.cltech.outreach.domain.masters.configuration.UserType;
import net.cltech.outreach.domain.operation.Filter;
import net.cltech.outreach.domain.operation.HistoryFilter;
import net.cltech.outreach.domain.operation.OrderSearch;
import net.cltech.outreach.domain.operation.ReportFilter;
import net.cltech.outreach.domain.operation.ResultTest;
import net.cltech.outreach.domain.operation.ResultTestHistory;
import net.cltech.outreach.domain.operation.TestHistory;
import net.cltech.outreach.service.interfaces.demographic.DemographicsService;
import net.cltech.outreach.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.outreach.service.interfaces.masters.configuration.UserTypeService;
import net.cltech.outreach.service.interfaces.operation.OrderService;
import net.cltech.outreach.tools.Constants;
import net.cltech.outreach.tools.JWT;
import net.cltech.outreach.tools.Log;
import net.cltech.outreach.tools.Tools;
import net.cltech.outreach.tools.enums.LISEnum;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementacion de servicios de ordenes para Consulta WEB
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/05/2018
 * @see Creación
 */
@Service
public class OrderServiceEnterpriseNT implements OrderService
{

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String urlBase;

    @Autowired(required = true)
    private HttpServletRequest request;
    @Autowired
    private OrderDao dao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private UserTypeService userTypeService;
    @Autowired
    private AuthenticationDao authenticationDao;
     @Autowired   
    private DemographicsService demographicService;
     
    
    @Override
    public List<OrderSearch> listOrders(Filter filter) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        if (filter.getOrder() == null || filter.getOrder() == 0)
        {
            filter.setOrder(null);
        }

        if (filter.getDateNumber() == null || filter.getDateNumber() == 0)
        {
            filter.setDateNumber(null);
        }
        
        if (filter.getDateNumberInit() == null || filter.getDateNumberInit() == 0)
        {
            filter.setDateNumberInit(null);
            filter.setDateNumberEnd(null);
        }

        if (filter.getPatientId() != null && !filter.getPatientId().isEmpty())
        {
            filter.setPatientId(Tools.encrypt(filter.getPatientId()));
        } else
        {
            filter.setPatientId(null);
        }

        if (filter.getDocumentType() == null || filter.getDocumentType() == 0)
        {
            filter.setDocumentType(null);
        }

        if (filter.getLastName() != null && !filter.getLastName().isEmpty())
        {
            filter.setLastName(Tools.encrypt(filter.getLastName()));
        } else
        {
            filter.setLastName(null);
        }

        if (filter.getSurName() != null && !filter.getSurName().isEmpty())
        {
            filter.setSurName(Tools.encrypt(filter.getSurName()));
        } else
        {
            filter.setSurName(null);
        }

        if (filter.getName1() != null && !filter.getName1().isEmpty())
        {
            filter.setName1(Tools.encrypt(filter.getName1()));
        } else
        {
            filter.setName1(null);
        }

        if (filter.getName2() != null && !filter.getName2().isEmpty())
        {
            filter.setName2(Tools.encrypt(filter.getName2()));
        } else
        {
            filter.setName2(null);
        }

        if (filter.getYear() == null || filter.getYear() == 0)
        {
            filter.setYear(null);
        }

        int showResult = Integer.valueOf(configurationService.getValue("MostrarResultado"));
        int yearsQuery = Integer.valueOf(configurationService.get("AniosConsultas").getValue());
       
        boolean isAuxPhysicians = "True".equals(configurationService.get("MedicosAuxiliares").getValue());
        
        List<Long> orderAuxPhysician = new ArrayList<>(); 
       
        //Se consultan las ordenes del medico auxiliar dependiendo de los filtros enviados
        if (isAuxPhysicians)
        {
            orderAuxPhysician = dao.ordersByAuxPhysician(filter, user, yearsQuery);       
        }
        
        Demographic demographic = null;
        QueryDemographic queryDemographic = null;
                
        if(user.getType() != null && user.getType() == Constants.DEMOGRAPHIC) {
            demographic = demographicService.queryDemographic();
            queryDemographic = demographicService.getQueryDemographic(user.getId());
        }
                
        List<OrderSearch> list = dao.listOrders(filter, showResult, user, yearsQuery, orderAuxPhysician, isAuxPhysicians, demographic, queryDemographic);

        UserType userType = userTypeService.list().stream().filter(type -> Objects.equals(type.getType(), user.getType())).findFirst().orElse(null);
        if (userType != null && userType.getQuantityOrder() > 0)
        {
            return list.stream().sorted(Comparator.comparing(OrderSearch::getOrder).reversed()).limit(userType.getQuantityOrder()).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<ResultTest> listResults(long idOrder, int area) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        AuthorizedUser confidentialLis = new AuthorizedUser();
        
        String textPending = configurationService.get("ComentarioResultadoPendiente").getValue();
        String textConfidential = configurationService.get("ComentarioRegistroRestringido").getValue();
        String textBlockTest = configurationService.get("ComentarioRegistroBloqueado").getValue();
        UserType userType = userTypeService.list().stream().filter(type -> Objects.equals(type.getType(), user.getType())).findFirst().orElse(null);
        
        List<ResultTest> results = dao.listResults(idOrder, area);
        
        if (userType != null) {
            if(userType.getType() == Constants.USERLIS) {
                confidentialLis = authenticationDao.authenticationUserLIS(user.getUserName());
            }
        }
            
        for (ResultTest result : results)
        {
            if (result.getState() < LISEnum.ResultTestState.VALIDATED.getValue())
            {
                result.setResult(textPending);
            } else {
                if( userType.getType() == Constants.USERLIS  && (!userType.isConfidential() || !confidentialLis.isConfidential()) && result.getConfidential()) {
                    result.setResult(textConfidential);
                } else if((!userType.isConfidential()) && result.getConfidential() && result.getConfidential()) {
                    result.setResult(textConfidential);
                }
            }
            
            if(result.getBlockTest() == 1){
                result.setResult(textBlockTest);
            }
        }
        return results;
    }

    @Override
    public String listTestHistory(HistoryFilter filter) throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("system");
        user.setPassword("cltechmanager");

        JWTToken token = authenticate(user);
        return listTestHistoryLIS(token.getToken(), filter);
    }

    @Override
    public String listReport(ReportFilter filter) throws Exception
    {
        AuthenticationUser user = new AuthenticationUser();
        user.setUser("system");
        user.setPassword("cltechmanager");

        JWTToken token = authenticate(user);
        return listReportLIS(token.getToken(), filter);
    }

    /**
     * Realiza la autenticacion del usuario laboratorio llamando servicio del
     * LIS (/api/authentication)
     *
     * @param user informacion usuario
     * @return Token de seguridad
     * @throws IOException
     * @throws EnterpriseNTException
     * @throws Exception
     */
    public JWTToken authenticate(AuthenticationUser user) throws IOException, EnterpriseNTException, Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        urlBase = configurationService.getValue("ServiciosLISUrl");

        String jsonContent = mapper.writeValueAsString(user);
        RequestBody body = RequestBody.create(JSON, jsonContent);
        Request request = new Request.Builder()
                .url(urlBase + "/api/authentication")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(response.body().string(), JWTToken.class);
        } else
        {
            processResponseError(response);
            return null;
        }
    }

    /**
     * Consulta el historico de resultados (llamado al servicio del LIS
     * /api/results/history)
     *
     * @param token token de authenticacion
     * @param filter filtro del historico
     * @return json
     * @throws Exception
     */
    public String listTestHistoryLIS(String token, HistoryFilter filter) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        urlBase = configurationService.getValue("ServiciosLISUrl");

        String jsonContent = mapper.writeValueAsString(filter);
        RequestBody body = RequestBody.create(JSON, jsonContent);

        Request request = new Request.Builder()
                .url(urlBase + "/api/results/history")
                .patch(body)
                .addHeader("Authorization", token)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            return response.body().string();
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return "";
        } else
        {
            processResponseError(response);
        }
        return null;
    }

    /**
     * Laamado al servicio para obtener reporte de resultados (/api/reports)
     *
     * @param token token de seguridad
     * @param filter Filtro del reporte
     * @return json con la informacion del reporte
     * @throws Exception Error en el servicio
     */
    public String listReportLIS(String token, ReportFilter filter) throws Exception
    {

        Log.info(String.class, "el filter " + filter.toString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        urlBase = configurationService.getValue("ServiciosLISUrl");
        Log.info(String.class, "urlBase " + configurationService.getValue("ServiciosLISUrl"));
        String jsonContent = mapper.writeValueAsString(filter);
        RequestBody body = RequestBody.create(JSON, jsonContent);
        Log.info(String.class, "el JSON " + body);
        
        

          Request request = new Request.Builder()
                .url(urlBase + "/api/reports")
                .patch(body)
                .addHeader("Authorization", token)
                .build();

        Log.info(String.class, "request " + request);
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            return response.body().string();

        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return "";
        } else
        {
            Log.info(String.class, "(response.code() : " + response.code());
            Log.info(String.class, "(response : " + response);
            //processResponseError(response);
        }
        return null;
    }

    /**
     * Metodo para darle tratamiento a los errores encontrados al llamar a los
     * servicios del LIS
     *
     * @param response
     * @throws IOException
     * @throws EnterpriseNTException
     */
    public void processResponseError(Response response) throws IOException, EnterpriseNTException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String responseAsString = response.body().string();
        System.out.println("" + responseAsString);
        try
        {
            if (response.code() == HttpStatus.NOT_FOUND.value())
            {
                throw new EnterpriseNTException("0|url not found");
            } else
            {
                WebException ex = mapper.readValue(responseAsString, WebException.class);
                throw new EnterpriseNTException(ex.getErrorFields());
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
            throw new EnterpriseNTException(responseAsString);
        }
    }
    
    @Override
    public List<TestHistory> listHistory(HistoryFilter filter) throws Exception
    {
        int yearsQuery = Integer.valueOf(configurationService.get("AniosConsultas").getValue());
        List<TestHistory> results = new ArrayList<>();
        
        for (Integer testId : filter.getTestId())
        {
            TestHistory item = new TestHistory();
            List<ResultTestHistory> list = new ArrayList<>();

            list.addAll(dao.listTestHistory(filter.getId(), testId, yearsQuery).stream().limit(20).collect(Collectors.toList()));
            list.stream().filter((itemList) -> (itemList.getResultType() == 1 && isNumeric(itemList.getResult()))).forEachOrdered((itemList)
                    ->
            {
                itemList.setResultNumber(new BigDecimal(itemList.getResult()));
            });

            item.setTestId(testId);
            item.setHistory(list);
            if (!list.isEmpty())
            {
                item.setTestCode(list.get(0).getTestCode());
                item.setTestName(list.get(0).getTestName());
                item.setAbbr(list.get(0).getAbbr());
                item.setResultType(list.get(0).getResultType());
            }

            results.add(item);
        }
        return results;

    }
    
    private boolean isNumeric(String value)
    {
        try
        {
            if (value != null)
            {
                new BigDecimal(value);
                return true;
            } else
            {
                return false;
            }
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

}
