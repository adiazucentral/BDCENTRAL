package net.cltech.enterprisent.service.interfaces.integration;

import net.cltech.enterprisent.domain.integration.external.billing.CashBoxExternalBillingApi;
import net.cltech.enterprisent.domain.integration.siigo.AccountSiigo;
import net.cltech.enterprisent.domain.integration.siigo.invoice.CreditNoteSiigo;
import net.cltech.enterprisent.domain.integration.siigo.invoice.InvoiceSiigo;

/**
 * Servicios para el envió de información a la API de Facturación
 * 
 * @version 1.0.0
 * @author Julian
 * @since 19/04/2021
 * @see Creación
 */
public interface BillingIntegrationService
{
    /**
     * Se enviá la información a la API de facturación para que esta sea procesada desde allí
     * 
     * @param accountSiigo
     * @return 
     * @throws java.lang.Exception
     */
    public Boolean sendToCreateAThird(AccountSiigo accountSiigo) throws Exception;
    
    /**
     * Se enviá la información a la API de facturación para que esta sea procesada desde allí
     * y se encargue de la creación de una factura
     * 
     * @param invoiceSiigo 
     * @return 
     * @throws java.lang.Exception
     */
    public Boolean sendToCreateInvoice(InvoiceSiigo invoiceSiigo) throws Exception;
    
    /**
     * Se enviá la información a la API de facturación para que esta sea procesada desde allí
     * y se encargue de la creación de una nota de credito en Siigo
     * 
     * @param creditNote 
     * @return 
     * @throws java.lang.Exception
     */
    public Boolean sendToCreateCreditNote(CreditNoteSiigo creditNote) throws Exception;
    
    /**
     * Se enviá la información a la API de facturación para que esta sea procesada desde allí
     * y se encargue de la creación o actualización de una Caja en el API de México
     * 
     * @param cashBox
     * @return 
     * @throws java.lang.Exception
     */
    public Boolean sendToCreateBoxInExternalBilling(CashBoxExternalBillingApi cashBox) throws Exception;
}
