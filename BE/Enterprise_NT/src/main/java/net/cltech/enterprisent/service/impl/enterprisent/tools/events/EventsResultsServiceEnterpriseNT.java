package net.cltech.enterprisent.service.impl.enterprisent.tools.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.tools.events.EventsResultsService;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.events.EventsLog;
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
 * Implementa los servicios de informacion eventos de resultados de examenes
 * para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @see 11/01/2019
 * @see Creación
 */
@Service
public class EventsResultsServiceEnterpriseNT implements EventsResultsService
{

    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private OrderService orderService;

    @Override
    public void create(ResultTest test)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/results";
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<ResultTest> httpEntity = new HttpEntity<>(test, headers);
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
    public void update(ResultTest test)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/results";
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<ResultTest> httpEntity = new HttpEntity<>(test, headers);
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
    public void rerun(ResultTest test)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/results/rerun";
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<ResultTest> httpEntity = new HttpEntity<>(test, headers);
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
    public void validateOrden(long order)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/results/order/validate";
            try
            {
                Order orderCompleted = orderService.get(order);
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<Order> httpEntity = new HttpEntity<>(orderCompleted, headers);
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
    public void validatedTest(Long orderId, int testId
    )
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/results/validate/order/" + orderId + "/test/" + testId;
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(urlEvents, HttpMethod.GET, httpEntity, String.class);
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
    public void printedTest(Long orderId, int testId)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/results/test/printTest/order/" + orderId + "/test/" + testId;
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(urlEvents, HttpMethod.GET, httpEntity, String.class);
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
    public void invalidate(int testId)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/results/test/invalidate/" + testId;
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(urlEvents, HttpMethod.GET, httpEntity, String.class);
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

}
