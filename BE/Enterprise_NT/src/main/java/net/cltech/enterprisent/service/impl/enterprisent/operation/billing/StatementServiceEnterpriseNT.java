package net.cltech.enterprisent.service.impl.enterprisent.operation.billing;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.billing.StatementDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.billing.statement.InvoiceFilterByCustomer;
import net.cltech.enterprisent.domain.operation.billing.statement.InvoiceReport;
import net.cltech.enterprisent.service.interfaces.operation.billing.StatementService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de los servicios para el estado de cuenta
 *
 * @version 1.0.0
 * @author Julian
 * @since 13/08/2021
 * @see Creación
 */
@Service
public class StatementServiceEnterpriseNT implements StatementService
{
    @Autowired
    private StatementDao statementDao;
    @Autowired
    private HttpServletRequest request;
    
    @Override
    public InvoiceReport getCustomerInvoices(InvoiceFilterByCustomer filters)throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            Date dateOFPrinting = new Date();
            InvoiceReport report = new InvoiceReport();
            report.setStartDate(DateTools.dateFormatddMMyyyy(filters.getStartDate()));
            report.setEndDate(DateTools.dateFormatddMMyyyy(filters.getEndDate()));
            report.setDateOfPrinting(dateOFPrinting);
            report.setUserName(user.getUserName());
            report.setDetails(statementDao.getCustomerInvoices(filters, -1));
            return report;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public InvoiceReport getExpiredInvoicesByCustomer(InvoiceFilterByCustomer filters)throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            Date dateOFPrinting = new Date();
            InvoiceReport report = new InvoiceReport();
            report.setStartDate(DateTools.dateFormatddMMyyyy(filters.getStartDate()));
            report.setEndDate(DateTools.dateFormatddMMyyyy(filters.getEndDate()));
            report.setDateOfPrinting(dateOFPrinting);
            report.setUserName(user.getUserName());
            report.setDetails(statementDao.getCustomerInvoices(filters, 0));
            report.setUnbilledOrders(statementDao.getOrdersPendingInvoicing(filters));
            return report;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
