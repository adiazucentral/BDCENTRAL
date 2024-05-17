package net.cltech.enterprisent.dao.interfaces.operation.billing;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.operation.billing.statement.InvoiceFilterByCustomer;
import net.cltech.enterprisent.domain.operation.billing.statement.InvoiceReportDetail;
import net.cltech.enterprisent.domain.operation.billing.statement.UnbilledOrder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los metodos de acceso para los datos del estado de la cuenta
 *
 * @version 1.0.0
 * @author Julian
 * @since 13/08/2021
 * @see Creación
 */
public interface StatementDao
{

    /**
     * Metodo de conexión hacia cualquier motor de base de datos
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Consultamos la lista de facturas asociadas a un cliente y una sede
     *
     * @param filters
     * @param status Estado de la factura 0-> Pendiente de pago, 1->Pagada
     * @return Lista de facturas por cliente
     * @throws java.lang.Exception Error de base de datos
     */
    default List<InvoiceReportDetail> getCustomerInvoices(InvoiceFilterByCustomer filters, int status) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab901.lab901c2 AS invoiceNumber")
                    .append(", lab901.lab901c4 AS total")
                    .append(", lab901.lab901c5 AS invoiceDate")
                    .append(", lab901.lab901c24 AS expirationDate")
                    .append(", lab901.lab901c30 AS paymentDate")
                    .append(" FROM lab901 ")
                    .append("JOIN lab900 ON lab900.lab901c1i = lab901.lab901c1 OR lab900.lab901c1p = lab901.lab901c1 ")
                    .append("JOIN lab22 ON lab22.lab22c1 = lab900.lab22c1 ")
                    .append("WHERE lab901.lab14c1 = ").append(filters.getCustomerId())
                    .append(" AND lab901.lab901c5 BETWEEN '").append(filters.getStartDate()).append("'")
                    .append(" AND '").append(filters.getEndDate()).append("'");
            
            if(status >= 0) {
                query.append(" AND lab901.lab901c31 = ").append(status);
            }
                  
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date today = new Date();
                InvoiceReportDetail report = new InvoiceReportDetail();
                report.setInvoiceNumber(rs.getString("invoiceNumber"));
                report.setTotalValue(rs.getDouble("total"));
                String date = format.format(rs.getDate("invoiceDate"));
                try
                {
                    report.setInvoiceDate(format.parse(date));
                    date = format.format(rs.getDate("expirationDate"));
                    report.setExpirationDate(format.parse(date));
                }
                catch (ParseException ex)
                {
                    report.setInvoiceDate(null);
                    report.setExpirationDate(null);
                }
                report.setPaymentDate(rs.getDate("paymentDate"));
                if(report.getPaymentDate() == null) {
                    if(today.after(report.getExpirationDate()))
                    {
                        report.setPaymentStatus("Vencida");
                    }
                    else
                    {
                        report.setPaymentStatus("Pendiente");
                    }
                } else {
                    report.setPaymentStatus("Pagada");
                }
                return report;
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
    
    /**
     * Consultamos la lista de ordenes pendientes por facturar junto con su valor total
     *
     * @param filters
     * @return Lista de ordenes pendientes por facturar
     * @throws java.lang.Exception Error de base de datos
     */
    default List<UnbilledOrder> getOrdersPendingInvoicing(InvoiceFilterByCustomer filters) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab900.lab22c1 AS orderNumber")
                    .append(", SUM(lab900.lab900c4) AS totalPriceInsurance")
                    .append(" FROM lab900 ")
                    .append("JOIN lab22 ON lab22.lab22c1 = lab900.lab22c1 ")
                    .append("WHERE lab900.lab14c1 = ").append(filters.getCustomerId())
                    .append(" AND lab22.lab22c3 BETWEEN '").append(filters.getStartDate()).append("'")
                    .append(" AND '").append(filters.getEndDate()).append("'")
                    .append(" AND lab900.lab901c1i IS NULL")
                    .append(" AND lab900.lab901c1p IS NULL")
                    .append(" GROUP BY orderNumber");

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                UnbilledOrder unbilledOrder = new UnbilledOrder();
                unbilledOrder.setOrderNumber(rs.getLong("orderNumber"));
                unbilledOrder.setValue(rs.getDouble("totalPriceInsurance"));
                return unbilledOrder;
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
}
