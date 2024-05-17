package net.cltech.enterprisent.service.impl.enterprisent.tools.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.tools.EventsResponse;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.tools.events.EventsOrderService;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.events.EventsLog;
import net.cltech.enterprisent.tools.log.integration.MiddlewareLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementa los servicios de informacion eventos de ordenes para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @see 11/01/2019
 * @see Creación
 */
@Service
public class EventsOrderServiceEnterpriseNT implements EventsOrderService
{

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void create(Order order)
    {
        try
        {

            MiddlewareLog.info("entro por create");
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/orders";
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<Order> httpEntity = new HttpEntity<>(order, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(urlEvents, HttpMethod.POST, httpEntity, String.class);
                if (responseEntity.getBody() != null)
                {
                    System.out.println(Tools.jsonObject(responseEntity.getBody()));
                }
            } catch (IllegalArgumentException | HttpClientErrorException ex)
            {
                EventsLog.error("URL not found - Url: " + urlEvents + " - Method: POST");
            } catch (JsonProcessingException | RestClientException ex)
            {
                EventsLog.error(ex);
            }
        } catch (Exception ex)
        {
            EventsLog.error(ex);
        }
    }

    @Override
    public void update(Order order)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/orders";
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<Order> httpEntity = new HttpEntity<>(order, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(urlEvents, HttpMethod.PUT, httpEntity, String.class);
                if (responseEntity.getBody() != null)
                {
                    System.out.println(Tools.jsonObject(responseEntity.getBody()));
                }
            } catch (IllegalArgumentException | HttpClientErrorException ex)
            {
                EventsLog.error("URL not found - Url: " + urlEvents + " - Method: PUT");
            } catch (JsonProcessingException | RestClientException ex)
            {
                EventsLog.error(ex);
            }
        } catch (Exception ex)
        {
            EventsLog.error(ex);
        }
    }

    @Override
    public void cancel(long order)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/orders/cancel/" + order;
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<EventsResponse> responseEntity = restTemplate.exchange(urlEvents, HttpMethod.GET, httpEntity, EventsResponse.class);
                if (responseEntity.getBody() != null)
                {
                    System.out.println(Tools.jsonObject(responseEntity.getBody()));
                }
            } catch (IllegalArgumentException | HttpClientErrorException ex)
            {
                EventsLog.error("URL not found - Url: " + urlEvents + " - Method: GET");
            } catch (JsonProcessingException | RestClientException ex)
            {
                EventsLog.error(ex);
            }
        } catch (Exception ex)
        {
            EventsLog.error(ex);
        }
    }

    @Override
    public EventsResponse validateUrl(EventsResponse url) throws EnterpriseNTException, Exception
    {
        final String urlEvents = url.getUrl() + "/api/tests/validateUrl";
        try
        {
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<EventsResponse> responseEntity = restTemplate.exchange(urlEvents, HttpMethod.GET, httpEntity, EventsResponse.class);
            if (responseEntity.getBody() != null)
            {
                url.setResponse(responseEntity.getBody().getResponse());
                System.out.println(url.getResponse());
            }
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            EventsLog.error("URL not found - Url: " + urlEvents + " - Method: GET");
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        } catch (RestClientException ex)
        {
            EventsLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList(ex.getMessage()));
        }
        return url;
    }

}
