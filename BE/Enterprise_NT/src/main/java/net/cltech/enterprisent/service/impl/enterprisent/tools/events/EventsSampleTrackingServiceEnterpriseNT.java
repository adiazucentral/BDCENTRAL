package net.cltech.enterprisent.service.impl.enterprisent.tools.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.tools.events.EventsSampleTrackingService;
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
 * Implementa los servicios de informacion eventos de muestra para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @see 11/01/2019
 * @see Creación
 */
@Service
public class EventsSampleTrackingServiceEnterpriseNT implements EventsSampleTrackingService
{

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void sampleTracking(long idOrder, String codeSample)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/sampletrackings/take/order/" + idOrder + "/sample/" + codeSample;
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
    public void sampleVerify(long idOrder, String codeSample)
    {
        try
        {
            final String urlEvents = configurationService.getValue("UrlEventos") + "/api/sampletrackings/verify/order/" + idOrder + "/sample/" + codeSample;
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
