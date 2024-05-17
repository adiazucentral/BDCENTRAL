package net.cltech.enterprisent.service.impl.enterprisent.operation.orders;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.BillingTestDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.CashBoxDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.external.billing.CashBoxExternalBillingApi;
import net.cltech.enterprisent.domain.integration.external.billing.PaymentExternalBillingApi;
import net.cltech.enterprisent.domain.integration.external.billing.TestExternalBillingApi;
import net.cltech.enterprisent.domain.masters.billing.PaymentType;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.billing.integration.CashBoxBilling;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBox;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader;
import net.cltech.enterprisent.domain.operation.orders.billing.FullPayment;
import net.cltech.enterprisent.domain.operation.orders.billing.Payment;
import net.cltech.enterprisent.service.interfaces.integration.BillingIntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.billing.PaymentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AccountService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.operation.billing.integration.OrderBillingService;
import net.cltech.enterprisent.service.interfaces.operation.orders.CashBoxService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.integration.ExternalBillingLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Agregar una descripcion de la clase
 *
 * @version 1.0.0
 * @author dcortes
 * @since 2/05/2018
 * @see Para cuando se crea una clase incluir
 */
@Service
public class CashBoxServiceEnterpriseNT implements CashBoxService
{

    @Autowired
    private CashBoxDao cashboxDao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private BillingIntegrationService billingIntegrationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private IntegrationIngresoDao integrationIngresoDao;
    @Autowired
    private BillingTestDao billingTestDao;
    @Autowired
    private UserService userService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private OrderBillingService orderBillingService;

    @Override
    public CashBox get(long order) throws Exception
    {
        CashBoxHeader header = cashboxDao.get(order);
        List<Payment> payments = cashboxDao.getPayments(order);
        CashBox cashbox = new CashBox();
        cashbox.setOrder(order);
        cashbox.setHeader(header);
        cashbox.setPayments(payments);

        return cashbox;
    }

    @Override
    public List<CashBoxHeader> getCashBoxHeader(long order) throws Exception
    {
        try
        {
            List<CashBoxHeader> payers = cashboxDao.getBasicCashBox(order);
            return payers;
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public List<CashBoxHeader> getCashBoxFound(long order, int idTest) throws Exception
    {
        try
        {
            List<CashBoxHeader> payers = cashboxDao.getCashBoxFound(order, idTest);
            return payers;
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean cashBoxExists(long order) throws Exception
    {
        try
        {
            boolean existsCash = cashboxDao.cashBoxExists(order);
            return existsCash;
        } catch (SQLException e)
        {
            return false;
        }
    }

    @Override
    public CashBox get(long order, int payer) throws Exception
    {
        CashBoxHeader payerO = cashboxDao.get(order, payer);
        if (payerO != null)
        {

            List<Payment> payments = cashboxDao.getPayments(order);
            CashBox cashbox = new CashBox();
            cashbox.setOrder(order);
            cashbox.setHeader(payerO);
            cashbox.setPayments(payments);

            return cashbox;
        } else
        {
            return null;
        }
    }

    @Override
    public List<Payment> getPayments(long order) throws Exception
    {
        return cashboxDao.getPayments(order);
    }

    @Transactional(transactionManager = "transactionManager", isolation = Isolation.READ_COMMITTED)
    @Override
    public CashBox save(CashBox cashbox) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);

        //Revisamos si ya existen datos de caja para la orden
        CashBoxHeader header = cashbox.getHeader();

        CashBox cashBoxDB = get(cashbox.getOrder());
        if (cashBoxDB.getHeader() != null && cashBoxDB.getHeader().getId() != null && cashBoxDB.getHeader().getId() > 0)
        {
            header.setId(cashBoxDB.getHeader().getId());
            header.setUpdateDate(new Date());
            header.setUpdateUser(new User());
            header.getUpdateUser().setId(user.getId());
            cashboxDao.update(header);

            //Revisa los pagos
            List<Payment> payments = cashbox.getPayments();
            //Obtiene los pagos que son nuevos
            List<Payment> newPayments = payments.stream()
                    .filter(payment -> payment.getId() == null)
                    .collect(Collectors.toList());
            newPayments.forEach((t) ->
            {
                t.setEntryDate(new Date());
                t.setEntryUser(new User());
                t.getEntryUser().setId(user.getId());
            });
            //Obtiene los pagos que son actualizaciones
            List<Payment> updatePayments = payments.stream()
                    .filter(payment -> payment.getId() != null)
                    .collect(Collectors.toList());
            updatePayments.forEach((t) ->
            {
                t.setUpdateDate(new Date());
                t.setUpdateUser(new User());
                t.getUpdateUser().setId(user.getId());
            });
            //Inserta y actualiza en base de datos
            cashboxDao.insertPayments(newPayments);
            cashboxDao.updatePayments(updatePayments);

            AuditOperation audit = new AuditOperation(AuditOperation.ACTION_UPDATE, Tools.jsonObject(cashbox), cashbox.getOrder(), AuditOperation.EXECUTION_TYPE_CB);
            trackingService.registerInvoiceAudit(audit);
        } else
        {
            header.setEntryDate(new Date());
            header.setEntryUser(new User());
            header.getEntryUser().setId(user.getId());
            cashboxDao.insert(header);

            //Revisa si se enviaron pagos
            if (cashbox.getPayments() != null && !cashbox.getPayments().isEmpty())
            {
                //Se enviaron pagos entonces inserta los pagos
                List<Payment> payments = cashbox.getPayments();
                payments.forEach((t) ->
                {
                    t.setEntryDate(new Date());
                    t.setEntryUser(new User());
                    t.getEntryUser().setId(user.getId());
                });
                cashboxDao.insertPayments(payments);
            }
            AuditOperation audit = new AuditOperation(AuditOperation.ACTION_INSERT, Tools.jsonObject(cashbox), cashbox.getOrder(), AuditOperation.EXECUTION_TYPE_CB);
            trackingService.registerInvoiceAudit(audit);
        }
        
        //Modificar los datos a enviar a kbits
        if( header.getComboBills() == 1 || header.getTypeCredit() > 0 ) {
            cashboxDao.deleteCashboxDetail(cashbox.getOrder());
        }
        int status = header.getComboBills() == 0 ? Constants.SEND : Constants.UNBILLED;
        orderBillingService.updateStatusOrderTest(cashbox.getOrder(), 0, status);
        
        return get(cashbox.getOrder());
    }

    @Override
    public FullPayment insertTotalPayments(FullPayment fullpayment) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        fullpayment.setUpdateDate(new Date());
        fullpayment.setUpdateUser(Long.parseLong(user.getId().toString()));
        FullPayment result = cashboxDao.insertTotalPayments(fullpayment);
        if (result != null)
        {
            AuditOperation audit = new AuditOperation(AuditOperation.ACTION_INSERT, Tools.jsonObject(result), fullpayment.getOrder(), AuditOperation.EXECUTION_TYPE_PT);
            trackingService.registerInvoiceAudit(audit);
        }
        return result;
    }

    @Override
    public FullPayment updateTotalPayments(FullPayment fullpayment) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        fullpayment.setUpdateDate(new Date());
        fullpayment.setUpdateUser(Long.parseLong(user.getId().toString()));
        FullPayment result = cashboxDao.updateTotalPayments(fullpayment);
        if (result != null)
        {
            AuditOperation audit = new AuditOperation(AuditOperation.ACTION_UPDATE, Tools.jsonObject(result), fullpayment.getOrder(), AuditOperation.EXECUTION_TYPE_PT);
            trackingService.registerInvoiceAudit(audit);
        }
        return result;
    }

    @Override
    public List<FullPayment> getLisFullpayment(long order) throws Exception
    {
        return cashboxDao.getLisFullpayment(order);
    }

    @Override
    public int deleteCashbox(Long id) throws Exception
    {
        CashBox cashBoxDB = get(id);
        AuditOperation audit = new AuditOperation(AuditOperation.ACTION_DELETE, Tools.jsonObject(cashBoxDB), id, AuditOperation.EXECUTION_TYPE_CB);
        trackingService.registerInvoiceAudit(audit);
        cashboxDao.deleteCashboxDetail(id);
        cashboxDao.deleteCashboxHeader(id);
        return cashboxDao.deleteTotalPayments(id);
    }

    @Override
    public void createBoxInExternalBilling(final CashBox cashBox, final int status, final long orderId)
    {
        try
        {
            ExternalBillingLog.info("[JSON DE ENVIÓ A CREACIÓN]:");
            final AuthorizedUser userAux = JWT.decode(request);
            if (configurationService.get("IntegracionFacturacionExterna").getValue().equalsIgnoreCase("True"))
            {
                CompletableFuture.runAsync(() ->
                {
                    try
                    {
                        AuthorizedUser userToken = userAux;
                        // Antes de iniciar con todo el proceso se debe validar el estado
                        CashBoxExternalBillingApi cashBoxExternal = new CashBoxExternalBillingApi();
                        // Si el estado es menor a 2 es decir entre 0 y 1
                        // no tendremos nada que validar
                        if (status < 2)
                        {
                            cashBoxExternal.setEstado(status);
                        } else
                        {
                            // Validamos si esa orden ya tiene asociada una factura
                            // Porque de ser así, esta orden no podra ser anulada y debera escribirse un error en el log
                            long invoiceId = cashboxDao.getInvoiceIdByOrderId(orderId);
                            if (invoiceId == 0)
                            {
                                cashBoxExternal.setEstado(status);
                            } else
                            {
                                // Escribimos un error en algun log
                                throw new EnterpriseNTException("");
                            }
                        }
                        // Para terminar de llenar datos de la caja es necesario obtener la orden:
                        Order order = orderService.get(orderId);
                        Account account = accountService.get(order.getAccount().getId(), null, null, null, null);
                        List<TestExternalBillingApi> externalTests = new ArrayList<>();
                        List<PaymentExternalBillingApi> externalPayments = new ArrayList<>();
                        // Sistema central del que obtendremos los codigos de homologación de los examenes:
                        int centralSystem = cashboxDao.getCentralSystemToExternalBilling();

                        cashBoxExternal.setOrden(orderId);
                        // fecha de entrada:
                        Date dt = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        String fechaMensaje = sdf.format(dt);
                        cashBoxExternal.setFechaIngreso(sdf.format(order.getCreatedDate()));
                        cashBoxExternal.setUsuarioIngreso(order.getCreateUser().getUserName());
                        cashBoxExternal.setFechaMensaje(fechaMensaje);
                        cashBoxExternal.setUsuarioMensaje(userToken.getUserName());
                        cashBoxExternal.setTarifa(order.getRate().getCode());

                        // Calculamos el valor del descuento
                        if (cashBoxExternal.getDescuento() != null && cashBoxExternal.getDescuento().compareTo(BigDecimal.ZERO) > 0)
                        {
                            cashBoxExternal.setDescuento(cashBoxExternal.getTotalOrden().multiply(cashBoxExternal.getDescuento()));
                        }
                        // Cargamos la lista de pagos
                        cashBox.getPayments().stream().map((payment) ->
                        {
                            PaymentType paymentAux;
                            User userCashbox;
                            try
                            {
                                paymentAux = paymentTypeService.findById(payment.getPaymentType().getId());
                                userCashbox = userService.get(payment.getEntryUser() == null ? userToken.getId() : payment.getEntryUser().getId(), null, null, null);
                            } catch (Exception e)
                            {
                                paymentAux = new PaymentType();
                                userCashbox = new User();
                            }
                            PaymentExternalBillingApi paymentExt = new PaymentExternalBillingApi();
                            paymentExt.setFormaPago(paymentAux.getName());
                            paymentExt.setValor(payment.getPayment());
                            paymentExt.setFechaPago(sdf.format(payment.getEntryDate() == null ? new Date() : payment.getEntryDate()));
                            paymentExt.setUsuarioPago(userCashbox.getUserName());
                            return paymentExt;
                        }).forEachOrdered((paymentExt) ->
                        {
                            externalPayments.add(paymentExt);
                        });
                        cashBoxExternal.setPagos(externalPayments);

                        // Cargamos el paciente de la orden:
                        cashBoxExternal.getPaciente().setHistoria(order.getPatient().getPatientId());
                        cashBoxExternal.getPaciente().setApellidoPaterno(order.getPatient().getLastName());
                        cashBoxExternal.getPaciente().setApellidoMaterno(order.getPatient().getSurName());
                        String fullName = order.getPatient().getName1();
                        if (order.getPatient().getName2() == null || order.getPatient().getName2().isEmpty())
                        {
                            fullName += "";
                        } else
                        {
                            fullName += " " + order.getPatient().getName2();
                        }
                        cashBoxExternal.getPaciente().setNombres(fullName);
                        SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String bdFormat = birthDayFormat.format(order.getPatient().getBirthday());
                        cashBoxExternal.getPaciente().setFechaNacimiento(bdFormat);
                        cashBoxExternal.getPaciente().setCURP("");

                        // Validación de género
                        // Masculino - H (Hombre)
                        // Femenino - M (Mujer)
                        if (order.getPatient().getSex().getEsCo().contains("M") || order.getPatient().getSex().getEsCo().contains("m"))
                        {
                            cashBoxExternal.getPaciente().setGenero("H");
                        } else
                        {
                            cashBoxExternal.getPaciente().setGenero("M");
                        }

                        // Cargamos la sede
                        cashBoxExternal.getSede().setCodigoSede(order.getBranch().getCode());
                        cashBoxExternal.getSede().setDescripcionSede(order.getBranch().getName());
                        // Cargamos al cliente
                        cashBoxExternal.getCliente().setRFC(account.getNit());
                        cashBoxExternal.getCliente().setRegimenFiscal(account.getUsoRegimenFiscal() == null ? "" : account.getUsoRegimenFiscal());
                        cashBoxExternal.getCliente().setRazonSocial(account.getName());
                        cashBoxExternal.getCliente().setCorreo(account.getEmail());
                        cashBoxExternal.getCliente().setFacturar(account.isInvoice() ? "SI" : "NO");
                        cashBoxExternal.getCliente().setConvenio(account.isAgreement() ? "SI" : "NO");
                        cashBoxExternal.getCliente().setUsoCFDI(account.getUsoCfdi() == null ? "" : account.getUsoCfdi());
                        cashBoxExternal.getCliente().setPostalCode(account.getPostalCode() == null ? "" : account.getPostalCode());
                        cashBoxExternal.getCliente().setPhone(account.getPhone() == null ? "" : account.getPhone());
                        cashBoxExternal.getCliente().setAddress(account.getAddress() == null ? "" : account.getAddress());
                        cashBoxExternal.getCliente().setAdditionalAddress(account.getAdditionalAddress() == null ? "" : account.getAdditionalAddress());
                        // Cargamos la lista de estudios
                        List<BillingTest> orderTests = billingTestDao.get(orderId);
                        for (BillingTest test : orderTests)
                        {
                            List<String> homologationCodes = integrationIngresoDao.homologationCodesForIdTest(String.valueOf(test.getTest().getId()), centralSystem);
                            TestExternalBillingApi externalTest = new TestExternalBillingApi();
                            externalTest.setCodigo(test.getTest().getCode());
                            externalTest.setDescripcion(test.getTest().getName());
                            if (homologationCodes != null && !homologationCodes.isEmpty())
                            {
                                externalTest.setClaveProdServSAT(homologationCodes.get(0));
                                if (homologationCodes.size() > 1)
                                {
                                    externalTest.setClaveUnidadSAT(homologationCodes.get(1));
                                } else
                                {
                                    externalTest.setClaveUnidadSAT("");
                                }
                            } else
                            {
                                externalTest.setClaveProdServSAT("");
                                externalTest.setClaveUnidadSAT("");
                            }
                            externalTest.setCantidad(1);
                            externalTest.setPrecio(test.getServicePrice());
                            externalTest.setSubtotal(test.getServicePrice());
                            externalTest.setDescuentoPorcentaje(test.getDiscount() == null ? BigDecimal.ZERO : test.getDiscount());
                            externalTest.setDescuentoValor(externalTest.getPrecio().multiply(externalTest.getDescuentoPorcentaje()).divide(new BigDecimal(100)));
                            externalTest.setBaseImpuesto(externalTest.getSubtotal().subtract(externalTest.getDescuentoValor()));
                            if (test.getTax() != null)
                            {
                                externalTest.setImpuesto(externalTest.getBaseImpuesto().multiply(test.getTax()).divide(BigDecimal.valueOf(100)));
                                externalTest.setImpuestoPorcentaje(test.getTax());
                            } else
                            {
                                externalTest.setImpuesto(new BigDecimal(0));
                                externalTest.setImpuestoPorcentaje(new BigDecimal(0));
                            }
                            externalTest.setTotal(externalTest.getBaseImpuesto().add(externalTest.getImpuesto()));
                            externalTests.add(externalTest);
                        }
                        cashBoxExternal.setPruebas(externalTests);
                        ExternalBillingLog.info("[JSON DE ENVIÓ A CREACIÓN DE FACTURACIÓN EXTERNA]:");
                        ExternalBillingLog.info(Tools.jsonObject(cashBoxExternal));
                        ExternalBillingLog.info("--------------------------------------------------");
                        billingIntegrationService.sendToCreateBoxInExternalBilling(cashBoxExternal);
                    } catch (Exception e)
                    {
                        ExternalBillingLog.error(e);
                    }
                });
            } else
            {
                ExternalBillingLog.info("La llave de configuración IntegracionFacturacionExterna no esta debidamente configurada pues su valor es: " + configurationService.get("IntegracionFacturacionExterna").getValue());
            }
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }
    
    @Override
    public List<CashBoxBilling> getListCashboxBilling(String startDate, String endDate) throws Exception
    {
        return cashboxDao.getListCashboxBilling(startDate, endDate);
    }
    
    @Override
    public boolean updateCashBoxStatus(long order, int status) throws Exception
    {
        try
        {
            cashboxDao.updateCashBoxStatus(order, status);
            return true;
        } catch (SQLException e)
        {
            return false;
        }
    }
}
