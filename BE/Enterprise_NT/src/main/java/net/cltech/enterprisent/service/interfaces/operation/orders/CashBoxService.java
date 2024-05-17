package net.cltech.enterprisent.service.interfaces.operation.orders;

import java.util.List;
import net.cltech.enterprisent.domain.operation.billing.integration.CashBoxBilling;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBox;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader;
import net.cltech.enterprisent.domain.operation.orders.billing.FullPayment;
import net.cltech.enterprisent.domain.operation.orders.billing.Payment;

/**
 * Servicios para caja de ordenes
 *
 * @version 1.0.0
 * @author dcortes
 * @since 2/05/2018
 * @see Creaciòn
 */
public interface CashBoxService
{

    /**
     * Obtiene la caja de una orden incluyendo cabecera y pagos
     *
     * @param order Numero de Orden
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public CashBox get(long order) throws Exception;
    
    
    /**
     * Obtiene la caja de una orden incluyendo cabecera y pagos
     *
     * @param order Numero de Orden
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public List<CashBoxHeader> getCashBoxHeader(long order) throws Exception;
    
    /**
     * Obtiene la informacion de una orden , sin tener caja
     *
     * @param order Numero de Orden
     * @param idTest
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public List<CashBoxHeader> getCashBoxFound(long order, int idTest) throws Exception;
    
    /**
     * Obtiene la caja de una orden incluyendo cabecera y pagos
     *
     * @param order Numero de Orden
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public boolean cashBoxExists(long order) throws Exception;

    /**
     * Obtiene la caja de una orden y un pagador en especifico con los pagos
     * realizados
     *
     * @param order Numero de orden
     * @param payer Id Pagador
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public CashBox get(long order, int payer) throws Exception;

    /**
     * Obtiene los pagos asociados a una orden
     *
     * @param order Numero de Orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.Payment}
     * @throws Exception Error presentado en el servicio
     */
    public List<Payment> getPayments(long order) throws Exception;

    /**
     * Inserta o actualiza los registros de caja de una orden
     *
     * @param cashbox
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public CashBox save(CashBox cashbox) throws Exception;

    /**
     * Inserta los registros de pagos totales de una caja
     *
     * @param fullpayment
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public FullPayment insertTotalPayments(FullPayment fullpayment) throws Exception;

    /**
     * Actualiza los registros de pagos totales de una caja
     *
     * @param fullpayment
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public FullPayment updateTotalPayments(FullPayment fullpayment) throws Exception;

    /**
     * Obtiene el total de pago de una caja
     *
     * @param order Numero de Orden
     * @return Lista de total de pago de una caja
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.FullPayment}
     * @throws Exception Error presentado en el servicio
     */
    public List<FullPayment> getLisFullpayment(long order) throws Exception;

    /**
     * Elimina la caja por medio de una orden
     *
     * @param id numero de la orden
     *
     * @return registros eliminados
     * @throws Exception Error en la base de datos.
     */
    public int deleteCashbox(Long id) throws Exception;
    
    /**
     * Envia la petición al API de Facturación Externa
     *
     * @param cashBox 
     * @param status 
     * @param orderId 
     */
    public void createBoxInExternalBilling(final CashBox cashBox, final int status, final long orderId);
    
    /**
     * Obtiene la lista de cajas de un rango de ordenes
     *
     * @param startDate Fecha de inicio
     * @param endDate Fecha fin
     * @return Lista de total de pago de una caja
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.FullPayment}
     * @throws Exception Error presentado en el servicio
     */
    public List<CashBoxBilling> getListCashboxBilling(String startDate, String endDate) throws Exception;
    
    /**
     * Obtiene la caja de una orden incluyendo cabecera y pagos
     *
     * @param order Numero de Orden
     * @param status Estado
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBox}
     * @throws Exception Error presentado en el servicio
     */
    public boolean updateCashBoxStatus(long order, int status) throws Exception;
    
}
