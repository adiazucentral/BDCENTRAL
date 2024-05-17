package net.cltech.enterprisent.service.impl.enterprisent.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.external.billing.CashBoxExternalBillingApi;
import net.cltech.enterprisent.domain.integration.siigo.AccountSiigo;
import net.cltech.enterprisent.domain.integration.siigo.invoice.CreditNoteSiigo;
import net.cltech.enterprisent.domain.integration.siigo.invoice.InvoiceSiigo;
import net.cltech.enterprisent.service.interfaces.integration.BillingIntegrationService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.tools.log.integration.ExternalBillingLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

/**
* Implementación de los servicios para el envió de información a la API de Facturación
*
* @version 1.0.0
* @author Julian
* @since 19/04/2021
* @see Creación
*/
@Service
public class BillingIntegrationServiceEnterpriseNT implements BillingIntegrationService
{
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationService integrationService;
    
    @Override
    public Boolean sendToCreateAThird(AccountSiigo accountSiigo) throws Exception
    {
        try
        {
            //Obtiene la url del API de facturación
            final String urlSiigo = configurationService.getValue("UrlApiFacturacion") + "/api/siigo/createAThirdParty";
            // Data (Json)
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(accountSiigo);
            Boolean created = integrationService.post(json, Boolean.class, urlSiigo);
            return created;
        }
        catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }
    
    @Override
    public Boolean sendToCreateInvoice(InvoiceSiigo invoiceSiigo) throws Exception
    {
        try
        {
            //Obtiene la url del API de facturación
            final String urlSiigo = configurationService.getValue("UrlApiFacturacion") + "/api/siigo/createInvoice";
            // Data (Json)
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(invoiceSiigo);
            Boolean created = integrationService.post(json, Boolean.class, urlSiigo);
            return created;
        }
        catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }
    
    @Override
    public Boolean sendToCreateCreditNote(CreditNoteSiigo creditNote) throws Exception
    {
        try
        {
            //Obtiene la url del API de facturación
            final String urlSiigo = configurationService.getValue("UrlApiFacturacion") + "/api/siigo/createCreditNote";
            // Data (Json)
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(creditNote);
            Boolean created = integrationService.post(json, Boolean.class, urlSiigo);
            return created;
        }
        catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }
    
    @Override
    public Boolean sendToCreateBoxInExternalBilling(CashBoxExternalBillingApi cashBox) throws Exception
    {
        try
        {
            // Data (Json)
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(cashBox);
      
            //Obtiene la url del API de facturación
            final String urlExternalBilling = configurationService.getValue("UrlApiFacturacion") + "/api/externalBilling/creationOfTheBox";
            ExternalBillingLog.info("////////////////////////////////////////////////////////");
            ExternalBillingLog.info("Url del API de Facturación: " + urlExternalBilling);
            ExternalBillingLog.info("Json enviado al API de Facturación : " + json);
            ExternalBillingLog.info("////////////////////////////////////////////////////////");
            Boolean created = integrationService.post(json, Boolean.class, urlExternalBilling);
            
            return created;
        }
        catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            ExternalBillingLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }
}