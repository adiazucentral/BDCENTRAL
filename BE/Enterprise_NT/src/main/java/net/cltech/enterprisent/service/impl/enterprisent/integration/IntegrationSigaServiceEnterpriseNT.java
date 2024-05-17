package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.siga.SigaBranch;
import net.cltech.enterprisent.domain.integration.siga.SigaFilterOrders;
import net.cltech.enterprisent.domain.integration.siga.SigaLogUser;
import net.cltech.enterprisent.domain.integration.siga.SigaMainLogUser;
import net.cltech.enterprisent.domain.integration.siga.SigaPointOfCare;
import net.cltech.enterprisent.domain.integration.siga.SigaReason;
import net.cltech.enterprisent.domain.integration.siga.SigaRequestLog;
import net.cltech.enterprisent.domain.integration.siga.SigaService;
import net.cltech.enterprisent.domain.integration.siga.SigaTransferByServices;
import net.cltech.enterprisent.domain.integration.siga.SigaTransferData;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnCall;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnGrid;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnMovement;
import net.cltech.enterprisent.domain.integration.siga.SigaUrlBranch;
import net.cltech.enterprisent.domain.integration.siga.SigaUser;
import net.cltech.enterprisent.domain.operation.widgets.TurnWidgetInfo;
import net.cltech.enterprisent.domain.operation.widgets.TurnsRatingWidgetInfo;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationSigaService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.integration.SigaLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Implementacion de integración Siga para Enterprise NT.
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/10/2018
 * @see Creacion
 */
@Service
public class IntegrationSigaServiceEnterpriseNT implements IntegrationSigaService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private OrdersDao ordersDao;

    @Override
    public List<SigaBranch> listBranches() throws Exception
    {
        SigaLog.info("Lista de sedes");
        List<SigaBranch> list = new ArrayList<>();
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/branch/getAll";
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaBranch[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, SigaBranch[].class);
            if (responseEntity.getBody() != null)
            {
                list = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return list;
    }

    @Override
    public List<SigaService> listService(int idbranch) throws Exception
    {
        SigaLog.info("Lista de servicios por sede");
        List<SigaService> listService = new ArrayList<>();
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/service/getByBranch/" + idbranch;
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaService[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, SigaService[].class);
            if (responseEntity.getBody() != null)
            {
                listService = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return listService;

    }

    @Override
    public List<SigaPointOfCare> getByBranchService(int branch, int service) throws Exception
    {
        SigaLog.info("Lista de taquillas por sede y servicio");
        List<SigaPointOfCare> list = new ArrayList<>();
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/pointOfCare/getByBranchService/" + branch + "/" + service;
            SigaLog.info("Url: " + urlLIS);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaPointOfCare[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, null, SigaPointOfCare[].class);
            if (responseEntity.getBody() != null)
            {
                list = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return list;
    }

    @Override
    public SigaLogUser changeStateWork(SigaRequestLog requestLog) throws Exception
    {
        try
        {
            SigaLog.info("Cambio de estado de un turno");
            //Obtiene la sesión de la peticion
            AuthorizedUser user = JWT.decode(request);
            SigaUser sigauser = new SigaUser();
            sigauser.setId(user.getId());
            sigauser.setUser(user.getUserName());
            sigauser.setLastName(user.getLastName());
            sigauser.setName(user.getName());
            requestLog.setUser(sigauser);
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/log";
            SigaLog.info("Url: " + urlLIS);
            SigaLog.info("Data: " + Tools.jsonObject(requestLog));
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<SigaRequestLog> entity = new HttpEntity<SigaRequestLog>(requestLog, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(urlLIS, entity, SigaLogUser.class);
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    @Override
    public List<SigaBranch> test(String url) throws Exception
    {
        List<SigaBranch> list = new ArrayList<>();
        try
        {
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaBranch[]> responseEntity = restTemplate.exchange(url + "/api/branch/getAll", HttpMethod.GET, httpEntity, SigaBranch[].class);
            if (responseEntity.getBody() != null)
            {
                list = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return list;
    }

    @Override
    public List<SigaService> listServices(SigaUrlBranch url) throws Exception
    {
        List<SigaService> listService = new ArrayList<>();
        try
        {

            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaService[]> responseEntity = restTemplate.exchange(url.getUrl() + "/api/service/getByBranch/" + url.getId(), HttpMethod.GET, httpEntity, SigaService[].class);
            if (responseEntity.getBody() != null)
            {
                listService = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return listService;
    }

    @Override
    public List<SigaTurnGrid> listActiveTurns(int branch, int service, int point) throws Exception
    {
        SigaLog.info("Lista de turnos activos por sede y servicio");
        List<SigaTurnGrid> list = new ArrayList<>();
        try
        {
            //Obtiene la sesión de la peticion
            AuthorizedUser user = JWT.decode(request);
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/turns/daily/" + branch + "/" + service + "/" + user.getLastName() + "/" + user.getName() + "/" + user.getUserName() + "/" + point;
            //final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/turns/daily/2/" + service + "/4/1";
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaTurnGrid[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, SigaTurnGrid[].class);
            if (responseEntity.getBody() != null)
            {
                list = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return list;
    }

    @Override
    public SigaTurnGrid turnAutomatic(int branch, int service, int point) throws Exception
    {
        SigaLog.info("Obtener turno de forma automatica por sede, servicio y taquilla");
        SigaTurnGrid response = new SigaTurnGrid();
        try
        {
            //Obtiene la sesión de la peticion
            AuthorizedUser user = JWT.decode(request);
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/turns/automatic/" + branch + "/" + service + "/" + point + "/" + user.getLastName() + "/" + user.getName() + "/" + user.getUserName();
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaTurnGrid> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, SigaTurnGrid.class);
            if (responseEntity.getBody() != null)
            {
                response = (SigaTurnGrid) responseEntity.getBody();
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return response;
    }

    @Override
    public SigaTurnGrid turnmanual(SigaTurnCall turn) throws Exception
    {
        SigaLog.info("Obtener turno de forma manual");
        SigaTurnGrid response = new SigaTurnGrid();
        try
        {
            //Obtiene la sesión de la peticion
            AuthorizedUser user = JWT.decode(request);
            SigaUser sigauser = new SigaUser();
            sigauser.setId(4);
            sigauser.setUser(user.getUserName());
            sigauser.setLastName(user.getLastName());
            sigauser.setName(user.getName());
            turn.getPoint().setUser(sigauser);
            //Obtiene la sesión de la peticion
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/turns/call";
            SigaLog.info("Url: " + urlLIS);
            SigaLog.info("Data: " + Tools.jsonObject(turn));
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<SigaTurnCall> entity = new HttpEntity<>(turn, headers);
            RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.postForObject(urlLIS, entity, SigaTurnGrid.class);
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return response;
    }

    @Override
    public Boolean turncall(int turn, int service) throws Exception
    {
        SigaLog.info("Llamado de turno por turno y servicio");
        boolean response = false;
        try
        {
            //Obtiene la sesión de la peticion
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/turns/call/" + turn + "/" + service;
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, Boolean.class);
            if (responseEntity.getBody() != null)
            {
                response = (Boolean) responseEntity.getBody();
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return response;
    }

    @Override
    public List<SigaReason> listReason() throws Exception
    {
        SigaLog.info("Listado de razones");
        List<SigaReason> list = new ArrayList<>();
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/reasons/postponement";
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaReason[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, SigaReason[].class);
            if (responseEntity.getBody() != null)
            {
                list = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return list;
    }

    @Override
    public List<SigaReason> listReasonBreak() throws Exception
    {
        SigaLog.info("Listado de razones de descanso");
        List<SigaReason> list = new ArrayList<>();
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/reasons/break";
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaReason[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, SigaReason[].class);
            if (responseEntity.getBody() != null)
            {
                list = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return list;
    }

    @Override
    public List<SigaReason> listReasonCancel() throws Exception
    {
        SigaLog.info("Listado de razones para cancelar");
        List<SigaReason> list = new ArrayList<>();
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/reasons/cancel";
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaReason[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, SigaReason[].class);
            if (responseEntity.getBody() != null)
            {
                list = Arrays.asList(responseEntity.getBody());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
        return list;
    }

    @Override
    public int changeStateTurn(SigaTurnMovement turnmovement) throws Exception
    {
        SigaLog.info("Cambiar estado del turno");
        try
        {
            //Obtiene la sesión de la peticion
            AuthorizedUser user = JWT.decode(request);
            SigaUser sigauser = new SigaUser();
            sigauser.setId(user.getId());
            sigauser.setUser(user.getUserName());
            sigauser.setLastName(user.getLastName());
            sigauser.setName(user.getName());
            turnmovement.setUser(sigauser);
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/turns/insertMovement";
            SigaLog.info("Url: " + urlLIS);
            SigaLog.info("Data: " + Tools.jsonObject(turnmovement));
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<SigaTurnMovement> entity = new HttpEntity<SigaTurnMovement>(turnmovement, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(urlLIS, entity, Integer.class);
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    @Override
    public List<SigaTransferByServices> transferServicies(int branch, int service, int turn) throws Exception
    {
        SigaLog.info("Listado de servicios de transferencia");
        try
        {
            List<SigaTransferByServices> list = new ArrayList<>();
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/transfers/services/" + branch + "/" + service + "/" + turn;
            SigaLog.info("Url: " + urlLIS);
            final HttpHeaders headers = new HttpHeaders();
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SigaTransferByServices[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, SigaTransferByServices[].class);
            if (responseEntity.getBody() != null)
            {
                list = Arrays.asList(responseEntity.getBody());
            }
            return list;
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    @Override
    public int transfers(SigaTransferData data) throws Exception
    {
        SigaLog.info("Transferencia de un turno");
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/transfers";
            SigaLog.info("Url: " + urlLIS);
            //Obtiene la sesión de la peticion
            AuthorizedUser user = JWT.decode(request);
            SigaUser sigauser = new SigaUser();
            sigauser.setId(user.getId());
            sigauser.setUser(user.getUserName());
            sigauser.setLastName(user.getLastName());
            sigauser.setName(user.getName());
            data.setUser(sigauser);
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<SigaTransferData> entity = new HttpEntity<SigaTransferData>(data, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(urlLIS, entity, Integer.class);
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    @Override
    public int serviceRating(SigaTurnMovement data) throws Exception
    {
        SigaLog.info("Validar si un servicio es calificable");
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/turns/rating";
            SigaLog.info("Url: " + urlLIS);
            SigaLog.info("Data: " + Tools.jsonObject(data));
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<SigaTurnMovement> entity = new HttpEntity<>(data, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(urlLIS, entity, Integer.class);
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    @Override
    public TurnWidgetInfo informationTurns(int date, int idBranch, int idUser) throws Exception
    {
        SigaLog.info("Obtener informacion de los turnos");
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/widgetInformation/getTurnsWidgetInformation";
            SigaLog.info("Url: " + urlLIS);
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlLIS)
                    .queryParam("dateInteger", date)
                    .queryParam("idBranch", idBranch)
                    .queryParam("idUser", idUser)
                    .queryParam("idService", configurationService.getValue("OrdenesSIGA"));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            HttpEntity<TurnWidgetInfo> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    TurnWidgetInfo.class);
            return response.getBody();
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    @Override
    public List<TurnsRatingWidgetInfo> turnsQualification(int date, int idBranch) throws Exception
    {
        SigaLog.info("Obtener calificacion de los turnos");
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/widgetInformation/getTurnsQualificationWidgetInformation";
            SigaLog.info("Url: " + urlLIS);
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlLIS)
                    .queryParam("dateInteger", date)
                    .queryParam("idBranch", idBranch)
                    .queryParam("idService", configurationService.getValue("OrdenesSIGA"));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<TurnsRatingWidgetInfo>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<TurnsRatingWidgetInfo>>()
            {
            });
            return response.getBody();
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    /**
     * Obtiene el historial del usuario en taquilla y turno
     *
     * @param userName
     * @return Historial del usuario
     * @throws Exception Error en la base de datos.
     */
    @Override
    public SigaMainLogUser getUserHistory(String userName) throws Exception
    {
        SigaLog.info("Obtener historial del usuario en taquilla y turno");
        SigaMainLogUser logUser = new SigaMainLogUser();
        try
        {
            final String urlSiga = configurationService.getValue("UrlSIGA") + "/api/log/validateTurnInPoint";
            SigaLog.info("Url: " + urlSiga);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlSiga)
                    .queryParam("username", userName);

            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));

            ResponseEntity<SigaMainLogUser> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, SigaMainLogUser.class);
            if (response.getBody() != null)
            {
                logUser = response.getBody();
            }
        } catch (Exception e)
        {
            SigaLog.error(e);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }

        return logUser;
    }

    /**
     * Obtiene todos los puntos de atención
     *
     * @return Lista de puntos de atención
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<SigaPointOfCare> getAllPointsOfCare() throws Exception
    {
        SigaLog.info("Obtener todos los puntos de atención");
        List<SigaPointOfCare> points = new ArrayList<>();
        try
        {
            final String urlSiga = configurationService.getValue("UrlSIGA") + "/pointsOfCare/";
            SigaLog.info("Url: " + urlSiga);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> httpEntity;
            RestTemplate restTemplate = new RestTemplate();

            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", request.getHeader("Authorization"));
            httpEntity = new HttpEntity<>(headers);

            ResponseEntity<SigaPointOfCare[]> response = restTemplate.exchange(urlSiga, HttpMethod.GET, httpEntity, SigaPointOfCare[].class);
            if (response.getBody() != null)
            {
                points = Arrays.asList(response.getBody());
            }
        } catch (Exception e)
        {
            SigaLog.error(e);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }

        return points;
    }

    @Override
    public List<Long> getOrdersByDate(SigaFilterOrders filter) throws Exception
    {
        try
        {
            List<Long> listIdOrders = ordersDao.getOrdersByDate(filter);
            for (Long iddOrder : listIdOrders)
            {
                ordersDao.updateOrderTurn(iddOrder, filter.getShift());
            }
            return listIdOrders;
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public int serviceTurnOrder(SigaTurnMovement data) throws Exception
    {
        try
        {
            final String urlLIS = configurationService.getValue("UrlSIGA") + "/api/orders";
            SigaLog.info("Url: " + urlLIS);
            SigaLog.info("Data: " + Tools.jsonObject(data));
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<SigaTurnMovement> entity = new HttpEntity<>(data, headers);
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(urlLIS, entity, Integer.class);
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            SigaLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }
}
