package net.cltech.enterprisent.service.impl.enterprisent.operation.billing;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.billing.ContractDao;
import net.cltech.enterprisent.dao.interfaces.masters.billing.RateDao;
import net.cltech.enterprisent.dao.interfaces.masters.billing.ResolutionDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.AccountDao;
import net.cltech.enterprisent.dao.interfaces.operation.billing.InvoiceDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.BillingTestDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.siigo.ContactSiigo;
import net.cltech.enterprisent.domain.integration.siigo.invoice.CreditNoteSiigo;
import net.cltech.enterprisent.domain.integration.siigo.invoice.InvoiceSiigo;
import net.cltech.enterprisent.domain.integration.siigo.invoice.PaymentsSiigo;
import net.cltech.enterprisent.domain.integration.siigo.invoice.ProductSiigo;
import net.cltech.enterprisent.domain.masters.billing.Contract;
import net.cltech.enterprisent.domain.masters.billing.Provider;
import net.cltech.enterprisent.domain.masters.billing.Resolution;
import net.cltech.enterprisent.domain.operation.billing.CashReport;
import net.cltech.enterprisent.domain.operation.billing.CashReportDetail;
import net.cltech.enterprisent.domain.operation.billing.CashReportFilter;
import net.cltech.enterprisent.domain.operation.billing.ComboInvoice;
import net.cltech.enterprisent.domain.operation.billing.CreditNote;
import net.cltech.enterprisent.domain.operation.billing.CreditNoteCombo;
import net.cltech.enterprisent.domain.operation.billing.DetailToRecalculate;
import net.cltech.enterprisent.domain.operation.billing.DetailedCashReport;
import net.cltech.enterprisent.domain.operation.billing.FilterSimpleInvoice;
import net.cltech.enterprisent.domain.operation.billing.Invoice;
import net.cltech.enterprisent.domain.operation.billing.InvoicePayment;
import net.cltech.enterprisent.domain.operation.billing.OrderParticular;
import net.cltech.enterprisent.domain.operation.billing.PreInvoiceHeader;
import net.cltech.enterprisent.domain.operation.billing.PreInvoiceOrder;
import net.cltech.enterprisent.domain.operation.billing.PreInvoiceTest;
import net.cltech.enterprisent.domain.operation.billing.RecalculateRate;
import net.cltech.enterprisent.domain.operation.billing.RecalculatedDetail;
import net.cltech.enterprisent.domain.operation.billing.SimpleInvoice;
import net.cltech.enterprisent.domain.operation.billing.Tax;
import net.cltech.enterprisent.domain.operation.billing.TaxInvoice;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingFilter;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBox;
import net.cltech.enterprisent.domain.operation.orders.billing.PriceChange;
import net.cltech.enterprisent.service.interfaces.integration.BillingIntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.billing.PaymentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.billing.ProviderService;
import net.cltech.enterprisent.service.interfaces.masters.billing.ResolutionService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.billing.InvoiceService;
import net.cltech.enterprisent.service.interfaces.operation.billing.integration.OrderBillingService;
import net.cltech.enterprisent.service.interfaces.operation.orders.CashBoxService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.events.EventsLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementaci�n de los servicios para la factura
 *
 * @version 1.0.0
 * @author Julian
 * @since 13/04/2021
 * @see Creaci�n
 */
@Service
public class InvoiceServiceEnterpriseNT implements InvoiceService
{

    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ResolutionDao resolutionDao;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private CashBoxService cashBoxService;
    @Autowired
    private ResolutionService resolutionService;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private RateDao rateDao;
    @Autowired
    private BillingIntegrationService billingIntegrationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private BillingTestDao billingTestDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private DemographicService serviceDemographic;
    
    @Override
    public Invoice preInvoice(BillingFilter filter) throws Exception
    {
        List<String> errors = new ArrayList<>(0);
        try
        {
            Invoice invoice = new Invoice();

            List<Contract> contracts = contractDao.getContractByCustomers(filter.getCustomers(), filter.getRates());

            if (contracts.isEmpty())
            {
                errors.add("1|no valid contracts");
                throw new EnterpriseNTException(errors);
            }

            int priceInvoice = Integer.parseInt(configurationService.getValue("PrecioFactura"));
            
            List<PreInvoiceOrder> orders = invoiceDao.getOrders(
                filter.getStartDate(), 
                filter.getEndDate(), 
                filter.getBranchId(), 
                filter.getDemographics(), 
                filter.getTestFilterType(), 
                filter.getTests(),
                serviceDemographic.list(true),
                filter.getCustomers(),
                filter.getRates(),
                priceInvoice
            );

            if (orders.isEmpty())
            {
                return null;
            }
            
            List<Integer> customersIds = contracts.stream().map(c -> c.getIdclient()).collect(Collectors.toList());
            List<Integer> contractsIds = contracts.stream().map(c -> c.getId()).collect(Collectors.toList());

            List<PreInvoiceHeader> headers = invoiceDao.getPreInvoice(customersIds, contractsIds, filter);
            Boolean subtractTaxes = configurationService.getValue("RestarImpuesto").equalsIgnoreCase("True");

            headers.forEach( (header) -> {
                
                Contract contract = contracts.stream().filter( c -> Objects.equals(c.getId(), header.getContractId())).findAny().orElse(null);
                
                header.setOrders(
                    orders
                    .stream()
                    .filter(order -> order.getCustomerId().equals(header.getCustomerId()))
                    .filter(order -> order.getContractId().equals(header.getContractId()))
                    .filter(order ->
                    {
                        return contract.getRates().stream().anyMatch(rate -> rate.getRateId().equals(order.getRate()));
                    }).collect(Collectors.toList())
                );

                if (header.getOrders().size() > 0)
                {

                    // Obtenemos datos de la caja requeridos para la factura
                    Double co_payments = 0.0;
                    Double moderatorFees = 0.0;

                    if (header.getCopayment() == 1)
                    {
                        co_payments = header.getOrders().stream().mapToDouble(orderOne -> orderOne.getCopayment()).sum();
                    }

                    if (header.getModeratingFee() == 1)
                    {
                        moderatorFees = header.getOrders().stream().mapToDouble(orderOne -> orderOne.getModeratorFee()).sum();
                    }

                    header.setTotalCopayment(co_payments);
                    header.setTotalModeratingFee(moderatorFees);

                    invoice.setPayerId(filter.getPayerId());

                    // Obtenemos el total del precio de las ordenes
                    Double total = 0.0;
                    Double totalPayments = 0.0;

                    total = header.getOrders().stream().map((order) -> order.getTests().stream().mapToDouble(test -> test.getInsurancePrice()).sum()).reduce(total, (accumulator, _item) -> accumulator + _item);
                    totalPayments = header.getOrders().stream().mapToDouble(orderOne -> orderOne.getTotalPayments()).sum();

                    header.setTotalPayments(totalPayments);
                    
                    // Adicionamos datos requeridos por la factura
                    if (total > 0)
                    {
                        Double discount = (total * header.getDiscountPercentage()) / 100;
                        Double totalTaxes = 0.0;
                        try {
                            totalTaxes = (total * invoiceDao.getTotalTaxValue(header.getContractId())) / 100;
                        } catch (Exception ex) {
                            Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        header.setDiscount(discount);
                        header.setTax(totalTaxes);

                        header.setTotalPaidInCash(co_payments + moderatorFees);

                        //Calculamos el total con descuento e impuesto
                        if (subtractTaxes)
                        {
                            total = (total - header.getTotalPaidInCash()) - (header.getDiscount() + header.getTax()) - totalPayments;
                        } else
                        {
                            total = (total + header.getTax() - header.getTotalPaidInCash()) - header.getDiscount() - totalPayments;
                        }

                        // Se validan los montos del cliente:
                        Double current = header.getCurrentAmount() + total;
                        // Validamos que el monto actual no exceda el monto maximo
                        if (current > header.getMaximumAmount())
                        {
                            errors.add("1|The current amount exceeds the maximum amount");
                            try {
                                throw new EnterpriseNTException(errors);
                            } catch (EnterpriseNTException ex) {
                                Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else
                        {
                            header.setTotal(total);
                            header.setCurrentAmount(current);
                        }
                    } else
                    {
                        header.setDiscount(0.0);
                        header.setTax(0.0);
                        header.setTotalPaidInCash(0.0);
                        header.setTotal(0.0);
                    }

                    // Validamos que no se rebase el limite de ordenes por capitado
                    if (header.getCapitated() > 0)
                    {
                        // Cantidad de ordenes por capitado asociadas a un cliente en este mes:
                        // Fecha yyyyMM -> Mes actual:
                        String dateFormat = DateTools.dateToString(new Date());
                        Integer ordersPerCapita = 0;
                        try {
                            ordersPerCapita = invoiceDao.getTotalOrdersPerCapita(header.getContractId(), header.getCustomerId(), dateFormat);
                        } catch (Exception ex) {
                            Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ordersPerCapita += header.getOrders().size();
                        if (ordersPerCapita > header.getCapitated())
                        {
                            errors.add("1|Limit per capita");
                            try {
                                throw new EnterpriseNTException(errors);
                            } catch (EnterpriseNTException ex) {
                                Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else
                        {
                            invoice.setComment("||" + (header.getCapitated() - ordersPerCapita) + "||" + "<br>" + (invoice.getComment() == null ? "" : invoice.getComment()));
                        }
                    } else
                    {
                        invoice.setComment("||" + (header.getMaximumAmount() - header.getCurrentAmount()) + "||" + "<br>" + (invoice.getComment() == null ? "" : invoice.getComment()));
                    }

                    List<Tax> listaTax = new LinkedList<>();
                    try {
                        listaTax = invoiceDao.listTax(header.getContractId());
                    } catch (Exception ex) {
                        Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    List<TaxInvoice> listTaxInv = new LinkedList<>();
                    //creamos dao para insertar en la nueva tabla lab916 con el numero factura el id del impuesto y el valor.
                    for (Tax tax : listaTax)
                    {
                        TaxInvoice taxInvoice = new TaxInvoice();
                        taxInvoice.setTaxId(tax.getTaxId());
                        taxInvoice.setName(tax.getName());
                        taxInvoice.setValue(((total * tax.getValue()) / 100));
                        listTaxInv.add(taxInvoice);
                    }

                    //setear la propiedad de el objeto Invoice con la lista de impuestos. 
                    header.setTaxs(listTaxInv);
                }
                
            });
            
            headers = headers.stream().filter( h -> h.getOrders().size() > 0).collect(Collectors.toList());
            
            if (headers.size() > 0)
            {

                invoice.setInvoiceHeader(headers);

                // Validamos que una entidad tenga una resoluci�n asignada
                // Obtenemos el id de la resoluci�n por el id de la entidad (Proveedor)
                int idResolution = resolutionService.getResolutionIdByProvider(invoice.getPayerId());
                if (idResolution > 0)
                {
                    invoice.setResolutionId(idResolution);
                } else
                {
                    errors.add("1|The associated entity does not have an assigned resolution");
                    throw new EnterpriseNTException(errors);
                }

                return invoice;
            }

            return null;
        } catch (Exception e)
        {
            OrderCreationLog.error(e);
            throw new EnterpriseNTException(errors);
        }
    }

    public void idInVoice(List<String> errors, Invoice invoice) throws EnterpriseNTException, Exception
    {
        // Obtenemos el id de la resoluci�n por el id de la entidad (Proveedor)
        int idResolution = resolutionService.getResolutionIdByProvider(invoice.getPayerId());

        if (idResolution == -1)
        {
            errors.add("1|Resolution not fount.");
            throw new EnterpriseNTException(errors);
        }

        // Resoluci�n
        Resolution resolution = resolutionDao.get(idResolution);
        invoice.setResolutionId(idResolution);
        // Generamos el nombre de la secuencia de donde tomaremos el numero consecutivo para esa esa resolucion
        String nameSequence = Constants.RESOLUTION_SEQUENCE + idResolution;
        long nextValue = toolsDao.nextVal(nameSequence);
        if (nextValue == -1)
        {
            // Si el id de la factura ya existe se debe generar una excepci�n controlada
            errors.add("1|No sequence found to generate the invoice number with the resolution ");
            throw new EnterpriseNTException(errors);
        } else if (nextValue == -2)
        {
            errors.add("1|Invoice numbering has been exceeded");
            throw new EnterpriseNTException(errors);
        } else
        {
            invoice.setInvoiceNumber(resolution.getPrefix() + String.valueOf(nextValue));
        }

    }

    @Override
    public Invoice saveInvoice(BillingFilter filter) throws Exception
    {
        List<String> errors = new ArrayList<>(0);
        try
        {
            AuthorizedUser user = JWT.decode(request);
            Invoice invoice = new Invoice();

            List<Contract> contracts = contractDao.getContractByCustomers(filter.getCustomers(), filter.getRates());
            if (contracts.isEmpty())
            {
                errors.add("1|no valid contracts");
                throw new EnterpriseNTException(errors);
            }

            int priceInvoice = Integer.parseInt(configurationService.getValue("PrecioFactura"));
            
            List<PreInvoiceOrder> orders = invoiceDao.getOrders(
                   filter.getStartDate(), 
                            filter.getEndDate(), 
                            filter.getBranchId(), 
                            filter.getDemographics(), 
                            filter.getTestFilterType(), 
                            filter.getTests(),
                            serviceDemographic.list(true),
                            filter.getCustomers(),
                            filter.getRates(),
                            priceInvoice
            );

            if (orders.isEmpty())
            {
                return null;
            }

            List<Integer> customersIds = contracts.stream().map(c -> c.getIdclient()).collect(Collectors.toList());
            List<Integer> contractsIds = contracts.stream().map(c -> c.getId()).collect(Collectors.toList());

            List<PreInvoiceHeader> headers = invoiceDao.getPreInvoice(customersIds, contractsIds, filter);
            Boolean subtractTaxes = configurationService.getValue("RestarImpuesto").equalsIgnoreCase("True");
            
            headers.forEach((header) ->
            {
                try
                {
                    Contract contract = contracts.stream().filter( c -> Objects.equals(c.getId(), header.getContractId())).findAny().orElse(null);

                    header.setOrders(
                            orders
                            .stream()
                            .filter(order -> order.getCustomerId().equals(contract.getIdclient()))
                            .filter(order -> order.getContractId().equals(contract.getId()))
                            .filter(order ->
                            {
                                return contract.getRates().stream().anyMatch(rate -> rate.getRateId().equals(order.getRate()));
                            }).collect(Collectors.toList())
                    );

                    if (header.getOrders().size() > 0)
                    {

                        // Obtenemos datos de la caja requeridos para la factura
                        Double co_payments = 0.0;
                        Double moderatorFees = 0.0;

                        if (header.getCopayment() == 1)
                        {
                            co_payments = header.getOrders().stream().mapToDouble(orderOne -> orderOne.getCopayment()).sum();
                        }

                        if (header.getModeratingFee() == 1)
                        {
                            moderatorFees = header.getOrders().stream().mapToDouble(orderOne -> orderOne.getModeratorFee()).sum();
                        }

                        header.setTotalCopayment(co_payments);
                        header.setTotalModeratingFee(moderatorFees);

                        int size = header.getOrders().size() - 1;
                        header.setInitialOrder(header.getOrders().get(0).getOrderId());
                        header.setFinalOrder(header.getOrders().get(size).getOrderId());
                        
                        
                        Double totalPayments = 0.0;
                        // Obtenemos el total del precio de la factura:
                        Double total = 0.0;
                        total = header.getOrders().stream().map((order) -> order.getTests().stream().mapToDouble(test -> test.getInsurancePrice()).sum()).reduce(total, (accumulator, _item) -> accumulator + _item);

                        totalPayments = header.getOrders().stream().mapToDouble(orderOne -> orderOne.getTotalPayments()).sum();

                        header.setTotalPayments(totalPayments);
                        
                        // Adicionamos datos requeridos por la factura
                        if (total > 0)
                        {
                            Double discount = (total * header.getDiscountPercentage()) / 100;
                            Double totalTaxes = (total * invoiceDao.getTotalTaxValue(header.getContractId())) / 100;

                            //crear y consultar el dao de los impuestos asociados a un cliente y alamcenarlos en una variable. id del imouesto y el porcentaje
                            header.setDiscount(discount);
                            header.setTax(totalTaxes);
                            header.setTotalPaidInCash(co_payments + moderatorFees);
                            // Calculamos el total con descuento e impuesto
                            if (subtractTaxes)
                            {
                                total = (total - header.getTotalPaidInCash()) - (header.getDiscount() + header.getTax()) - totalPayments;
                            } else
                            {
                                total = (total + header.getTax() - header.getTotalPaidInCash()) - header.getDiscount() - totalPayments;
                            }
                            
                            // Se validan los montos del cliente:
                            Double current = header.getCurrentAmount() + total;
                            // Validamos que el monto actual no exceda el monto maximo
                            if (current > header.getMaximumAmount())
                            {
                                errors.add("1|The current amount exceeds the maximum amount");
                                throw new EnterpriseNTException(errors);
                            } else
                            {
                                header.setCurrentAmount(current);
                                header.setTotal(total);
                                int rowsAffected = invoiceDao.updateCurrentCustomerAmount(header.getCurrentAmount(), header.getContractId());
                                if (rowsAffected == -1)
                                {
                                    EventsLog.info("entro por 3");
                                    errors.add("1|The current amount could not be assigned to the client");
                                    throw new EnterpriseNTException(errors);
                                }
                            }
                        } else
                        {
                            EventsLog.info("entro por 4");
                            header.setDiscount(0.0);
                            header.setTax(0.0);
                            header.setTotalPaidInCash(0.0);
                            // Calculamos el total con descuento e impuesto
                            header.setTotal(0.0);
                        }

                        // Validacion de manejo por capitado
                        if (header.getCapitated() > 0)
                        {
                            // Cantidad de ordenes por capitado asociadas a un cliente en este mes:
                            // Fecha yyyyMM -> Mes actual:
                            String dateFormat = DateTools.dateToString(new Date());
                            Integer ordersPerCapita = invoiceDao.getTotalOrdersPerCapita(header.getContractId(), header.getCustomerId(), dateFormat);
                            ordersPerCapita += header.getOrders().size();
                            // Consultamos el valor mensual del contrato:
                            Double monthlyValue = invoiceDao.getMonthlyValueContractById(header.getContractId());
                            if (ordersPerCapita > header.getCapitated())
                            {
                                ordersPerCapita -= ordersPerCapita - header.getCapitated();
                            }
                            if (ordersPerCapita <= header.getCapitated())
                            {
                                invoice.setComment("||" + (header.getCapitated() - ordersPerCapita) + "||" + "<br>" + (invoice.getComment() == null ? "" : invoice.getComment()));
                                // Asignamos el valor mensual del contrato al total de la factura:                                    
                                header.setTotal(total + monthlyValue);
                            } else
                            {
                                EventsLog.info("entro por 5");
                                errors.add("1|Limit per capita");
                                throw new EnterpriseNTException(errors);
                            }
                        } else
                        {
                            invoice.setComment("||" + (header.getMaximumAmount() - header.getCurrentAmount()) + "||" + "<br>" + (invoice.getComment() == null ? "" : invoice.getComment()));

                        }

                        String dateFormat = DateTools.dateToString(new Date());
                        header.setDatePerCapita(dateFormat);
                    }

                } catch (Exception ex)
                {
                    Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            headers = headers.stream().filter( h -> h.getOrders().size() > 0).collect(Collectors.toList());
            
            if (headers.size() > 0)
            {
                invoice.setInvoiceHeader(headers);
                invoice.setPayerId(filter.getPayerId());
                invoice.setComment(filter.getComment());
                invoice.setCfdi(null);

                invoice.setDateOfInvoice(filter.getDateOfInvoice());
                // Periodo de facturaci�n
                invoice.setBillingPeriod(filter.getStartDate().toString() + " - " + filter.getEndDate().toString());
                invoice.setUserId(user.getId());
                invoice.setParticular(0);

                // Validamos que la llave configuraci�n indique que se debe generar de manera automatica el n�mero de la factura
                if (configurationService.getValue("facturacionAutomatica").equalsIgnoreCase("True"))
                {
                    idInVoice(errors, invoice);

                } else
                {

                    if (filter.getInvoiceNumber() == null)
                    {
                        idInVoice(errors, invoice);
                    }

                    if (!filter.getInvoiceNumber().isEmpty())
                    {
                        boolean invoiceDb = !invoiceDao.getInvoiceById(filter.getInvoiceNumber()).isEmpty();
                        if (invoiceDb)
                        {
                            // Si el id de la factura ya existe se debe generar una excepci�n controlada
                            errors.add("1|Duplicate invoice number");
                            throw new EnterpriseNTException(errors);
                        } else
                        {
                            invoice.setInvoiceNumber(filter.getInvoiceNumber());
                        }
                    } else
                    {
                        EventsLog.info("entro por 6.15");
                        // Si el id de la factura ya existe se debe generar una excepci�n controlada
                        errors.add("1|Invoice creation without invoice number");
                        throw new EnterpriseNTException(errors);
                    }
                }

                Double discount = invoice.getInvoiceHeader().stream().mapToDouble(header -> header.getDiscount()).sum();
                Double tax = invoice.getInvoiceHeader().stream().mapToDouble(header -> header.getTax()).sum();
                Double finalTotal = invoice.getInvoiceHeader().stream().mapToDouble(header -> header.getTotal()).sum();

                invoice.setDiscount(discount);
                invoice.setTotal(finalTotal);
                invoice.setTax(tax);
                invoice.setCustomerId(filter.getCustomers().get(0));

                if (filter.getFilterTpye() == 1)
                {
                    invoice.setDueDate(filter.getDueDate());
                    invoice.setFormOfPayment(filter.getFormOfPayment());

                } else
                {

                    invoice.setDueDate(invoice.getInvoiceHeader().get(0).getDateOfInvoice());
                    invoice.setFormOfPayment(invoice.getInvoiceHeader().get(0).getFormOfPayment());
                }

                Invoice created = invoiceDao.insertInvoice(invoice);

                // Actualizamos las ordenes que son facturadas
                invoiceDao.updateInvoicedOrders(invoice.getInvoiceHeader(), created.getInvoiceId());

                // Solo se permiten crear facturas en siigo que pertenezcan a un solo cliente con un unico contrato
                if (invoice.getInvoiceHeader().size() == 1)
                {
                    createInvoiceSiigo(invoice);
                }

                invoiceAudit(created, AuditOperation.ACTION_INSERT, AuditOperation.EXECUTION_TYPE_INV);
                return created;
            }

            return null;

        } catch (Exception e)
        {
            OrderCreationLog.error(e);
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Realiza la creaci�n de una nota cr�dito
     *
     * @param creditNote
     *
     * @return True - si fue cancelada totalmente, False - si no pudo terminar
     * de ser cancelada
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public Long createCreditNote(CreditNote creditNote) throws Exception
    {
        List<String> errors = new ArrayList<>(0);
        try
        {
            AuthorizedUser user = JWT.decode(request);
            creditNote.setUserId(user.getId());

            // Obtenemos el id de la factura:
            int priceInvoice = Integer.parseInt(configurationService.getValue("PrecioFactura"));
            int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
            Invoice invoice = invoiceDao.getInvoice(creditNote.getInvoiceNumber(), false, priceInvoice, yearsQuery);

            if (invoice != null)
            {

                creditNote.setInvoiceId(invoice.getInvoiceId());
                // Consultar notas credito ya asociadas a esta factura:
                Double totalValueCreditNotes = invoiceDao.totalValueCreditNotes(invoice.getInvoiceId());
                // Obtenemos el valor total de la factura
                Double totalValue = invoice.getTotal();

                if (totalValue > 0)
                {
                    totalValue = totalValue - totalValueCreditNotes;
                    // Si el valor de la nota excede el valor total de la factura se retorna un error
                    if (totalValue < creditNote.getValue())
                    {
                        errors.add("1|The value of the credit note exceeds the value of the invoice.");
                        throw new EnterpriseNTException(errors);
                    } else if (Objects.equals(totalValue, creditNote.getValue()))
                    {
                        // Liberaci�n de ordenes asociadas a una factura
                        invoiceDao.releaseOrdersFromAnInvoice(invoice.getInvoiceId(), creditNote.getType());
                    }

                    Long creditNoteId = invoiceDao.createCreditNote(creditNote);

                    if (creditNoteId != -1)
                    {
                        if (creditNote.getType() == 1)
                        {
                            if (invoice.getInvoiceHeader().size() == 1)
                            {
                                invoice.getInvoiceHeader().forEach(header ->
                                {
                                    try
                                    {
                                        Contract contract = contractDao.getContract(header.getContractId(), null, null);
                                        invoiceDao.updateCurrentCustomerAmount(contract.getCurrentAmount() - creditNote.getValue(), contract.getId());
                                    } catch (Exception ex)
                                    {
                                        Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
                            } else
                            {
                                invoice.getInvoiceHeader().forEach(header ->
                                {
                                    try
                                    {
                                        Contract contract = contractDao.getContract(header.getContractId(), null, null);
                                        invoiceDao.updateCurrentCustomerAmount(contract.getCurrentAmount() - header.getTotal(), contract.getId());
                                    } catch (Exception ex)
                                    {
                                        Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
                            }
                        }
                        createCreditNoteInSiigo(creditNote);
                    }

                    AuditOperation audit = new AuditOperation(AuditOperation.ACTION_INSERT, String.valueOf(creditNoteId), creditNote.getInvoiceId(), AuditOperation.EXECUTION_TYPE_CN);
                    trackingService.registerInvoiceAudit(audit);

                    return creditNoteId;
                } else
                {
                    errors.add("1|The value of the invice is 0");
                    throw new EnterpriseNTException(errors);
                }
            }
            return null;
        } catch (Exception e)
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Obtiene el detalle simple de una factura
     *
     * @param invoiceNumber
     *
     * @return Detalle de la factura simple
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public Invoice getInvoiceCreditNotes(String invoiceNumber) throws Exception
    {
        try
        {
            // Obtenemos el id de la factura:
            int priceInvoice = Integer.parseInt(configurationService.getValue("PrecioFactura"));
            int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
            Invoice invoice = invoiceDao.getInvoice(invoiceNumber, false, priceInvoice, yearsQuery);

            if (invoice != null)
            {
                //Consulta las tarifas de los contratos asociados a la factura
                invoice.getInvoiceHeader().forEach(header ->
                {
                    try
                    {
                        header.setRates(contractDao.allRatesPerCustomerAndContract(header.getCustomerId(), header.getContractId(), true));
                    } catch (Exception ex)
                    {
                        Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                //Consultar notas credito ya asociadas a esta factura:
                Double totalValueCreditNotes = invoiceDao.totalValueCreditNotes(invoice.getInvoiceId());
                invoice.setTotal(invoice.getTotal() - totalValueCreditNotes);
                return invoice;
            }
            return null;
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public Invoice getInvoiceParticular(Long order) throws Exception
    {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDao.getInvoicedParticularOrder(order).getInvoiceNumber());
        return invoice;
    }

    @Override
    public Invoice saveInvoiceParticular(OrderParticular order) throws Exception
    {
        List<String> errors = new ArrayList<>(0);
        try
        {
            Invoice invoice = new Invoice();
            AuthorizedUser user = JWT.decode(request);
            invoice.setUserId(user.getId());
            CashBox cashBox = cashBoxService.get(order.getOrder());
            if (cashBox == null)
            {
                errors.add("1|cashbox not fount.");
                throw new EnterpriseNTException(errors);
            }

            Provider provider = providerService.getProviderParticular();
            if (provider == null)
            {
                errors.add("2|provider not fount.");
                throw new EnterpriseNTException(errors);
            }
            invoice.setPayerId(provider.getId());
            // Validamos que la llave configuraci�n indique que se debe generar de manera automatica el n�mero de la factura
            if (configurationService.getValue("facturacionAutomatica").equalsIgnoreCase("True"))
            {
                if (provider == null)
                {
                    errors.add("1|provider not fount.");
                    throw new EnterpriseNTException(errors);
                }

                // Obtenemos el id de la resoluci�n por el id de la entidad (Proveedor)
                int idResolution = resolutionService.getResolutionIdByProvider(provider.getId());
                if (idResolution == -1)
                {
                    errors.add("1|Resolution not fount.");
                    throw new EnterpriseNTException(errors);
                }

                Resolution resolution = resolutionDao.get(idResolution);
                invoice.setResolutionId(idResolution);
                // Generamos el nombre de la secuencia de donde tomaremos el numero consecutivo para esa esa resolucion
                String nameSequence = Constants.RESOLUTION_SEQUENCE + idResolution;
                long nextValue = toolsDao.nextVal(nameSequence);
                if (nextValue == -1)
                {
                    // Si el id de la factura ya existe se debe generar una excepci�n controlada
                    errors.add("1|No sequence found to generate the invoice number with the resolution ");
                    throw new EnterpriseNTException(errors);
                } else if (nextValue == -2)
                {
                    errors.add("1|Invoice numbering has been exceeded");
                    throw new EnterpriseNTException(errors);
                } else
                {
                    invoice.setInvoiceNumber(resolution.getPrefix() + String.valueOf(nextValue));
                }
            } else
            {
                if (!order.getInvoiceNumber().isEmpty())
                {
                    boolean invoiceDb = !invoiceDao.getInvoiceById(order.getInvoiceNumber()).isEmpty();
                    if (invoiceDb)
                    {
                        // Si el id de la factura ya existe se debe generar una excepci�n controlada
                        errors.add("1|Duplicate invoice number");
                        throw new EnterpriseNTException(errors);
                    } else
                    {
                        invoice.setInvoiceNumber(order.getInvoiceNumber());
                    }
                } else
                {
                    errors.add("1|Invoice creation without invoice number");
                    throw new EnterpriseNTException(errors);
                }
            }

            Order orderCreated = orderService.get(order.getOrder());

            // Cargamos los respectivos datos del paciente a la factura
            Patient patient = orderCreated.getPatient();

            List<PreInvoiceHeader> headers = new LinkedList<>();

            PreInvoiceHeader header = new PreInvoiceHeader();
            // Los datos del paciente seran almacenados en los atributos donde se cargan los datos del cliente
            // A excepci�n del identificador del paciente de la base de datos
            invoice.setPatientId(patient.getId());
            header.setAccountNit(patient.getPatientId());
            String fullName = patient.getName1();
            fullName += patient.getName2().isEmpty() ? "" : " " + patient.getName2() + " ";
            fullName += patient.getLastName();
            fullName += patient.getSurName().isEmpty() ? "" : " " + patient.getSurName();
            header.setAccountName(fullName);
            header.setAccountPhone(patient.getPhone());
            header.setAccountAddress(patient.getAddress());
            header.setAccountCity(null);
            header.setDateOfInvoice(new Timestamp(new Date().getTime()));
            header.setFormOfPayment(Constants.FORM_OF_PAYMENT_CASH);
            header.setCapitated(0);
            header.setInitialOrder(order.getOrder());
            header.setFinalOrder(order.getOrder());
            header.setContractId(0);

            // Obtenemos datos de la caja requeridos para la factura

            Double co_payments = cashBox.getHeader().getCopay().doubleValue();
            Double moderatorFees = cashBox.getHeader().getFee().doubleValue();
                                               

            header.setTotalCopayment(co_payments);
            header.setTotalModeratingFee(moderatorFees);

            // Obtenemos el total de la orden
            Double total = 0.0;

            DecimalFormat df = new DecimalFormat("###");
            if (configurationService.getValue("ManejoCentavos").equalsIgnoreCase("True"))
            {
                df = new DecimalFormat("###.##");
                DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
                dfs.setDecimalSeparator('.');
                df.setDecimalFormatSymbols(dfs);
            }

            Double discount = 0.0;

            Double tax = cashBox.getHeader().getTaxValue().doubleValue();
            
            if (!cashBox.getHeader().getDiscountPercent().equals(BigDecimal.ZERO))
            {
                discount = ((cashBox.getHeader().getSubTotal().add(cashBox.getHeader().getTaxValue())).multiply(cashBox.getHeader().getDiscountPercent())).divide(new BigDecimal(100)).doubleValue();
            }
            else

            {
                discount = cashBox.getHeader().getDiscountValue().doubleValue();
            }
 
            total = Double.parseDouble(df.format(cashBox.getHeader().getSubTotal().doubleValue() - discount + header.getTotalCopayment() + header.getTotalModeratingFee() + tax).replace(',', '.') );

            header.setDiscount(discount);
            header.setTax(tax);

            header.setTotalPaidInCash(co_payments + moderatorFees);

            header.setTotal(total);

            String dateFormat = DateTools.dateToString(new Date());
            header.setDatePerCapita(dateFormat);

            // Obtenemos datos de la caja requeridos para la factura
            List<PreInvoiceOrder> orders = new LinkedList<>();

            PreInvoiceOrder orderInvoice = new PreInvoiceOrder();

            orderInvoice.setOrderId(orderCreated.getOrderNumber());

            List<PreInvoiceTest> listTests = new LinkedList<>();

            List<Integer> listTestIds = orderCreated.getResultTest().stream().map(testConvert -> testConvert.getTestId()).collect(Collectors.toList());

            listTestIds.forEach(test ->
            {
                PreInvoiceTest testInvoice = new PreInvoiceTest();
                testInvoice.setTestId(test);
                listTests.add(testInvoice);
            });

            orderInvoice.setTests(listTests);

            orders.add(orderInvoice);

            header.setOrders(orders);

            headers.add(header);

            invoice.setInvoiceHeader(headers);

            // fecha de vencimiento de la factura
            invoice.setDateOfInvoice(new Timestamp(new Date().getTime()));
            invoice.setComment(order.getComment());
            invoice.setTypeOfInvoice(null);
            invoice.setCfdi(null);
            invoice.setParticular(1);
            invoice.setDiscount(discount);
            invoice.setTax(tax);
            invoice.setTotal(total);
            invoice.setCustomerId(0);

            Invoice created = invoiceDao.insertInvoice(invoice);

            // Actualizamos las ordenes que son facturadas            
            invoiceDao.updateInvoicedOrdersToParticular(created.getInvoiceHeader(), created.getInvoiceId());

            List<BillingTest> tests = billingTestDao.getTestCashbox(order.getOrder());
            created.setBillingTest(tests);

            // Solo se permiten crear facturas en siigo que pertenezcan a un solo cliente con un unico contrato
            if (invoice.getInvoiceHeader().size() == 1)
            {
                createInvoiceSiigo(invoice);
            }
            invoiceAudit(created, AuditOperation.ACTION_INSERT, AuditOperation.EXECUTION_TYPE_INV);
            return created;
        } catch (Exception e)
        {
            OrderCreationLog.error(e);
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Realiza el recalculo de las tarifas
     *
     * @param recalculateRate
     *
     * @return Las tarifas recalculadas
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public RecalculatedDetail recalculateRates(RecalculateRate recalculateRate) throws Exception
    {
        try
        {
            List<DetailToRecalculate> listToRecalculates = invoiceDao.getOrdersWithoutInvoice(recalculateRate);
            List<DetailToRecalculate> differentPrices = new ArrayList<>();

            for (DetailToRecalculate toRecalculate : listToRecalculates)
            {
                // Se crea el objeto que cuenta con el recalculo
                DetailToRecalculate detailToUpdate = new DetailToRecalculate();
                detailToUpdate.setOrderId(toRecalculate.getOrderId());
                detailToUpdate.setTestId(toRecalculate.getTestId());

                BigDecimal servicePrice = ordersDao.getPriceTest(detailToUpdate.getTestId(), recalculateRate.getRateId());

                detailToUpdate.setServicePrice(servicePrice);
                BigDecimal patientPercentage = new BigDecimal(ordersDao.getPatientPercentageTest(detailToUpdate.getTestId(), recalculateRate.getRateId()));

                BigDecimal patientPrice = detailToUpdate.getServicePrice().multiply(patientPercentage).divide(new BigDecimal(100));

                detailToUpdate.setPatientPrice(patientPrice);
                BigDecimal insurancePrice = detailToUpdate.getServicePrice().subtract(patientPrice);
                detailToUpdate.setInsurancePrice(insurancePrice);

                if (!Objects.equals(servicePrice, toRecalculate.getServicePrice())
                        || !Objects.equals(patientPrice, toRecalculate.getPatientPrice())
                        || !Objects.equals(insurancePrice, toRecalculate.getInsurancePrice()))
                {

                    // Actualizamos los diferentes precios
                    invoiceDao.recalculateRates(detailToUpdate, recalculateRate);
                    differentPrices.add(detailToUpdate);
                }
            }
            // Cargamos el objeto con los detalles de las ordenes recalculadas
            RecalculatedDetail recalculated = new RecalculatedDetail();
            recalculated.setCustomerName(accountDao.getName(recalculateRate.getCustomerId()));
            recalculated.setRateName(rateDao.getName(recalculateRate.getRateId()));
            recalculated.setDetails(differentPrices);
            return recalculated;
        } catch (Exception e)
        {
            OrderCreationLog.info("entro" + e);
            return null;
        }
    }

    private void createInvoiceSiigo(final Invoice invoice) throws Exception
    {
        if (configurationService.get("IntegracionSiigo").getValue().equalsIgnoreCase("True"))
        {
            CompletableFuture.runAsync(() ->
            {
                try
                {
                    InvoiceSiigo invoiceSiigo = new InvoiceSiigo();
                    ContactSiigo contact = new ContactSiigo();
                    List<ProductSiigo> items = new ArrayList<>();
                    // Obtenemos la entidad legal del laboratorio:
                    Provider provider = providerService.get(invoice.getPayerId(), null, null, null);
                    int documentId = configurationService.get("SiigoCodigoComprobante").getValue().isEmpty() ? 0 : Integer.parseInt(configurationService.get("SiigoCodigoComprobante").getValue());

                    // Cargamos la identificacion del cliente
                    invoiceSiigo.setLisInvoiceId(invoice.getInvoiceId());
                    invoiceSiigo.getCustomer().setIdentification(invoice.getInvoiceHeader().get(0).getAccountNit());
                    invoiceSiigo.getDocument().setId(documentId);
                    // Si la factura automatica esta activa este no aplica
                    invoiceSiigo.setDate(DateTools.dateFormatyyyy_MM_dd(new Date()));
                    invoiceSiigo.setSeller(629); // El vendedor no tiene identificacion
                    // Envio solo la identificacion del cliente para que en el API de facturaci�n
                    // Mediante el servicio get pueda obtener este cliente

                    // Formas de pago:
                    List<PaymentsSiigo> payments = new ArrayList();
                    int idPayment;

                    if (invoice.getInvoiceHeader().get(0).getFormOfPayment() == Constants.FORM_OF_PAYMENT_CASH)
                    {
                        PaymentsSiigo payment = new PaymentsSiigo();
                        idPayment = configurationService.get("SiigoIdPagoContado").getValue().isEmpty() ? 0 : Integer.parseInt(configurationService.get("SiigoIdPagoContado").getValue());
                        payment.setId(idPayment);
                        payment.setValue(invoice.getTotal());
                        payments.add(payment);
                    }

                    if (invoice.getInvoiceHeader().get(0).getFormOfPayment() == Constants.FORM_OF_PAYMENT_CREDIT)
                    {
                        PaymentsSiigo payment = new PaymentsSiigo();
                        idPayment = configurationService.get("SiigoIdPagoCredito").getValue().isEmpty() ? 0 : Integer.parseInt(configurationService.get("SiigoIdPagoCredito").getValue());
                        payment.setId(idPayment);
                        payment.setValue(invoice.getTotal());
                        payment.setDue_date(DateTools.dateFormatyyyy_MM_dd(invoice.getDateOfInvoice()));
                        payments.add(payment);
                    }
                    invoiceSiigo.setPayments(payments);

                    // Cargamos la lista de productos:
                    ProductSiigo product = new ProductSiigo();
                    // Hace falta el codigo del producto que se adicionara a la factura
                    product.setCode("0548");
                    product.setDescription("Venta Online");
                    product.setQuantity(1);
                    product.setPrice(invoice.getTotal());
                    product.setDiscount(invoice.getDiscount());
                    items.add(product);
                    invoiceSiigo.setItems(items);

                    billingIntegrationService.sendToCreateInvoice(invoiceSiigo);
                } catch (Exception ex)
                {
                    Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    /**
     * Obtiene los datos requeridos para el reporte detallado de caja
     *
     * @param filter
     *
     * @return Reporte detallado de caja
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public CashReport detailedCashReport(CashReportFilter filter) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            CashReport report = new CashReport();

            String i = null, f = null;
            if (filter.getStartDate() != null)
            {
                i = String.valueOf(filter.getStartDate()) + " 00:00:00";
                filter.setStartDate(i);
            }

            // A�adimos un dia y ademas agregamso las horas minutos y segundos -> 23:59:59
            if (filter.getEndDate() != null)
            {
                f = String.valueOf(filter.getEndDate()) + " 23:59:59";
                filter.setEndDate(f);
            }

            report.setDateOfPrinting(new Timestamp(new Date().getTime()));
            report.setUserName(user.getUserName());
            
            if(filter.getBranchId() > 0) {
                report.setBranchName(branchService.get(filter.getBranchId(), null, null, null).getName());
            }
            
            report.setCashReportDetails(null);
            
            boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
            
            int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
                    
            List<DetailedCashReport> paid = invoiceDao.getDetailedCashReport(filter, account, yearsQuery);
                        
            List<DetailedCashReport> returns = invoiceDao.getsDetailedCashReturns(filter, account);
            report.getDetailedCashReport().addAll(paid);
            report.getDetailedCashReport().addAll(returns);
            return report;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene los datos requeridos para el reporte general de caja
     *
     * @param filter
     *
     * @return Reporte de caja
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public CashReport generalCashReport(CashReportFilter filter) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            CashReport report = new CashReport();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            // A�adimos un dia a la fecha enviada
            String i = null, f = null;
            if (filter.getStartDate() != null)
            {
                i = String.valueOf(filter.getStartDate()) + " 00:00:00";
                filter.setStartDate(i);
            }

            // A�adimos un dia y ademas agregamso las horas minutos y segundos -> 23:59:59
            if (filter.getEndDate() != null)
            {
                f = String.valueOf(filter.getEndDate()) + " 23:59:59";
                filter.setEndDate(f);
            }

            report.setDateOfPrinting(new Timestamp(new Date().getTime()));
            report.setUserName(user.getUserName());
            
            if(filter.getBranchId() > 0) {
                report.setBranchName(branchService.get(filter.getBranchId(), null, null, null).getName());
            }

            // Obtenemos las ordenes dentro de un rango de fechas:
            List<Integer> payments = paymentTypeService.list(true).stream()
                    .map(payment -> payment.getId()).collect(Collectors.toList());
            List<CashReportDetail> paid = invoiceDao.getCashReport(filter, payments);
            List<CashReportDetail> returns = invoiceDao.getsCashReturns(filter);
            report.getCashReportDetails().addAll(paid);
            report.getCashReportDetails().addAll(returns);
            return report;
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public Invoice getInvoice(String invoiceNumber) throws Exception
    {
        try
        {
            int priceInvoice = Integer.parseInt(configurationService.getValue("PrecioFactura"));
            int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
            Invoice invoice = invoiceDao.getInvoice(invoiceNumber, true, priceInvoice, yearsQuery);
            if (invoice != null)
            {
                invoiceAudit(invoice, AuditOperation.ACTION_REPORT, AuditOperation.EXECUTION_TYPE_INV);
            }
            return invoice;
        } catch (Exception e)
        {
            OrderCreationLog.error(e);
            return null;
        }
    }

    @Override
    public List<CreditNote> getCreditNote(String invoiceNumber) throws Exception
    {
        try
        {
            return invoiceDao.getOrdersFromACreditNote(invoiceNumber);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<SimpleInvoice> getSimpleInvoiceDetail(FilterSimpleInvoice filter) throws Exception
    {
        try
        {
            if (filter.getInitialInvoice() != null && !filter.getInitialInvoice().isEmpty())
            {
                long intInvoiceId = invoiceDao.getInvoiceId(filter.getInitialInvoice());
                filter.setIdInitialInvoice(intInvoiceId);
            }
            if (filter.getFinalInvoice() != null && !filter.getFinalInvoice().isEmpty())
            {
                long finInvoiceId = invoiceDao.getInvoiceId(filter.getFinalInvoice());
                filter.setIdFinalInvoice(finInvoiceId);
            }
            String i = null, f = null;
            if (filter.getInvoiceDate() != null)
            {
                i = String.valueOf(filter.getInvoiceDate()) + " 00:00:00";
                f = String.valueOf(filter.getInvoiceDate()) + " 23:59:59";
            }
            return invoiceDao.getListInvoices(filter, i, f);
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    private void createCreditNoteInSiigo(CreditNote creditNote) throws Exception
    {
        if (configurationService.get("IntegracionSiigo").getValue().equalsIgnoreCase("True"))
        {
            CompletableFuture.runAsync(() ->
            {
                try
                {
                    CreditNoteSiigo creditNoteS = new CreditNoteSiigo();
                    ProductSiigo product = new ProductSiigo();
                    List<ProductSiigo> items = new ArrayList<>();
                    List<PaymentsSiigo> payments = new ArrayList();
                    PaymentsSiigo payment = new PaymentsSiigo();
                    int documentId = configurationService.get("SiigoCodigoComprobanteNC").getValue().isEmpty() ? 0 : Integer.parseInt(configurationService.get("SiigoCodigoComprobanteNC").getValue());
                    String siigoInvoiceId = invoiceDao.getSiigoInvoiceId(creditNote.getInvoiceId());

                    creditNoteS.getDocument().setId(documentId);
                    creditNoteS.setDate(DateTools.dateFormatyyyy_MM_dd(new Date()));
                    creditNoteS.setInvoice(siigoInvoiceId);
                    creditNoteS.setNumber(creditNote.getInvoiceId());
                    // Productos
                    // Cargamos la lista de productos:
                    // Hace falta el codigo del producto que se adicionara a la factura
                    product.setCode("0548");
                    product.setDescription("Venta Online");
                    product.setQuantity(1);
                    product.setPrice(creditNote.getValue());
                    product.setDiscount(null);
                    items.add(product);
                    creditNoteS.setItems(items);

                    // Tipos de pagos Siigo
                    int idPayment = configurationService.get("SiigoIdPagoContado").getValue().isEmpty() ? 0 : Integer.parseInt(configurationService.get("SiigoIdPagoContado").getValue());
                    payment.setId(idPayment);
                    payment.setValue(creditNote.getValue());
                    payments.add(payment);
                    creditNoteS.setPayments(payments);

                    billingIntegrationService.sendToCreateCreditNote(creditNoteS);
                } catch (Exception ex)
                {
                    Logger.getLogger(InvoiceServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    @Override
    public boolean paymentOfInvoice(InvoicePayment invoicePayment) throws Exception
    {
        try
        {
            boolean updated = invoiceDao.updateInvoicePayment(invoicePayment);
            if (updated)
            {
                long invoiceId = invoiceDao.getInvoiceId(invoicePayment.getInvoiceNumber());
                AuditOperation audit = new AuditOperation(AuditOperation.ACTION_UPDATE, Tools.jsonObject(invoicePayment), invoiceId, AuditOperation.EXECUTION_TYPE_PI);
                trackingService.registerInvoiceAudit(audit);
            }
            return updated;
        } catch (Exception e)
        {
            return false;
        }
    }

    private void invoiceAudit(Invoice invoice, String action, String executionType)
    {
        if (invoice != null && invoice.getInvoiceId() > 0)
        {
            AuditOperation audit = new AuditOperation(action, invoice.getInvoiceNumber(), invoice.getInvoiceId(), executionType);
            try
            {
                trackingService.registerInvoiceAudit(audit);
            } catch (Exception e)
            {
                e.getMessage();
            }
        }
    }
    
    /**
     * Obtiene los datos requeridos para el reporte de saldos de caja
     *
     * @param filter
     *
     * @return Reporte de saldos de caja
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public CashReport cashBalances(CashReportFilter filter) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            CashReport report = new CashReport();
            // A�adimos un dia a la fecha enviada
            String i = null, f = null;
            if (filter.getStartDate() != null)
            {
                i = String.valueOf(filter.getStartDate()) + " 00:00:00";
                filter.setStartDate(i);
            }

            // A�adimos un dia y ademas agregamso las horas minutos y segundos -> 23:59:59
            if (filter.getEndDate() != null)
            {
                f = String.valueOf(filter.getEndDate()) + " 23:59:59";
                filter.setEndDate(f);
            }

            report.setDateOfPrinting(new Timestamp(new Date().getTime()));
            report.setUserName(user.getUserName());
            
            if(filter.getBranchId() > 0) {
                report.setBranchName(branchService.get(filter.getBranchId(), null, null, null).getName());
            }
            
            report.setDetailedCashReport(invoiceDao.cashBalances(filter));
            return report;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene las ordenes que no han sido facturadas
     *
     * @param filter
     *
     * @return Reporte de saldos de caja
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public CashReport unbilled(CashReportFilter filter) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            CashReport report = new CashReport();
            // A�adimos un dia a la fecha enviada
            String i = null, f = null;
            if (filter.getStartDate() != null)
            {
                i = String.valueOf(filter.getStartDate()) + " 00:00:00";
                 filter.setStartDate(i);
            }

            // A�adimos un dia y ademas agregamso las horas minutos y segundos -> 23:59:59
            if (filter.getEndDate() != null)
            {
                f = String.valueOf(filter.getEndDate()) + " 23:59:59";
                filter.setEndDate(f);
            }

            report.setDateOfPrinting(new Timestamp(new Date().getTime()));
            report.setUserName(user.getUserName());
            
            if(filter.getBranchId() > 0) {
                report.setBranchName(branchService.get(filter.getBranchId(), null, null, null).getName());
            }

            report.setDetailedCashReport(invoiceDao.unbilled(filter));
            return report;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
     /**
     * Realiza el cambio de precios de los examenes de una orden
     *
     * @param prices entidad con la lista de examenes
     *
     * @return Las tarifas recalculadas
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public PriceChange priceChange(PriceChange prices) throws Exception
    {
        try
        {           
            AuditOperation audit = new AuditOperation(AuditOperation.ACTION_INSERT, Tools.jsonObject(prices), prices.getOrder(), AuditOperation.EXECUTION_TYPE_PC);
            trackingService.registerInvoiceAudit(audit);
            //Actualizacion de datos
            invoiceDao.priceChange(prices);
            cashBoxService.save(prices.getCashbox());
            return prices;
        } catch (Exception e)
        {
            OrderCreationLog.info(e.toString());
            return null;
        }
    }
    
    /**
     * Obtiene los datos requeridos para el reporte consolidado de convenios //LABOPAT
     *
     * @param filter
     *
     * @return Reporte consolidado de convenios
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public CashReport consolidatedAccount(CashReportFilter filter) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            CashReport report = new CashReport();

            String i = null, f = null;
            if (filter.getStartDate() != null)
            {
                i = String.valueOf(filter.getStartDate()) + " 00:00:00";
                filter.setStartDate(i);
            }

            // A�adimos un dia y ademas agregamso las horas minutos y segundos -> 23:59:59
            if (filter.getEndDate() != null)
            {
                f = String.valueOf(filter.getEndDate()) + " 23:59:59";
                filter.setEndDate(f);
            }

            report.setDateOfPrinting(new Timestamp(new Date().getTime()));
            report.setUserName(user.getUserName());
            
            if(filter.getBranchId() > 0) {
                report.setBranchName(branchService.get(filter.getBranchId(), null, null, null).getName());
            }
                       
            boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
            int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
            
            List<DetailedCashReport> orders = invoiceDao.consolidatedAccountOrders(filter, account, yearsQuery);
                        
            List<DetailedCashReport> payments = invoiceDao.consolidatedAccountPayment(filter, account, yearsQuery);
            report.getDetailedCashReport().addAll(orders);
            report.getDetailedCashReport().addAll(payments);
            
            return report;
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public ComboInvoice getInvoiceCombo(Integer idInvoice) throws Exception
    {
        ComboInvoice invoice = invoiceDao.getInvoiceCombo(idInvoice);
        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        invoice.setOrders(invoiceDao.getOrdersByComboInvoice(invoice.getInvoiceId(), yearsQuery));
        return invoice;
    }
    
    @Override
    public ComboInvoice preInvoiceComboInvoice(BillingFilter filter) throws Exception
    {
        ComboInvoice invoice = new ComboInvoice();
        invoice.setOrders(invoiceDao.getOrdersComboInvoice(filter.getStartDate(), filter.getEndDate(), filter.getBranchId(), filter.getDemographics(), serviceDemographic.list(true)));
        return invoice;
    }
    
    @Override
    public ComboInvoice saveInvoiceCombo(BillingFilter filter) throws Exception
    {
        try
        {
            ComboInvoice invoice = new ComboInvoice();
            invoice.setOrders(invoiceDao.getOrdersComboInvoice(filter.getStartDate(), filter.getEndDate(), filter.getBranchId(), filter.getDemographics(), serviceDemographic.list(true)));
        
            if( invoice.getOrders().size() > 0 ) {
                AuthorizedUser user = JWT.decode(request);
                invoice.setUserId(user.getId());
                invoice.setComment(filter.getComment());

                invoice = invoiceDao.createInvoiceCombo(invoice);

                if(invoice.getInvoiceId() > 0) {

                    invoiceDao.updateOrdersInvoiceCombo(invoice.getInvoiceId(), filter.getStartDate(), filter.getEndDate(), filter.getBranchId(), filter.getDemographics(), serviceDemographic.list(true));
                    invoiceDao.updateOrdersTestsInvoiceCombo(invoice.getInvoiceId(), filter.getStartDate(), filter.getEndDate(), Constants.BILLED);
                    boolean update = invoiceDao.updateCashboxInvoiceCombo(invoice.getInvoiceId(), filter.getStartDate(), filter.getEndDate(), Constants.BILLED);                 
                    if(update) {
                        return getInvoiceCombo(invoice.getInvoiceId());
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            
            return invoice;
            
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public CreditNoteCombo getCreditNodeCombo(Long idCreditNote) throws Exception
    {
        return invoiceDao.getCreditNodeCombo(idCreditNote);
    }
    
    /**
     * Realiza la creacion de una nota credito combo
     *
     * @param creditNote
     *
     * @return True - si fue cancelada totalmente, False - si no pudo terminar
     * de ser cancelada
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public CreditNoteCombo creditNoteCombo(CreditNoteCombo creditNote) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            creditNote.setUserId(user.getId());
            
            Long idNote = invoiceDao.creditNoteCombo(creditNote);
            
            if(idNote > 0) {
                int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
                invoiceDao.updateStatusInvoiceCombo(creditNote.getIdInvoice());
                invoiceDao.updateOrdersTestsCreditNoteCombo(creditNote.getIdInvoice(), yearsQuery, Constants.UNBILLED);
                invoiceDao.updateCashboxCreditNoteCombo(creditNote.getIdInvoice(), yearsQuery, Constants.UNBILLED);
                boolean update = invoiceDao.updateOrdersCreditNoteCombo(creditNote.getIdInvoice(), yearsQuery);
                if(update) {
                    return getCreditNodeCombo(idNote);
                } else {
                    return null;
                }
            }
            
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
