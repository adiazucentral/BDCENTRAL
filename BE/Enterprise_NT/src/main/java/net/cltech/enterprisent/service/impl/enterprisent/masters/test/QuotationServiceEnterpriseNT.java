/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.test.QuotationDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.test.QuotationHeader;
import net.cltech.enterprisent.service.interfaces.masters.test.QuotationService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a cotizaciones
 *
 * @author Jrodriguez
 * @version 1.0.0
 * @since 01/11/2018
 * @see Creación
 */
@Service
public class QuotationServiceEnterpriseNT implements QuotationService
{

    @Autowired
    private QuotationDao dao;
    @Autowired
    private HttpServletRequest request;

    @Override
    public int insertQuotations(QuotationHeader quotationHeader) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        quotationHeader.setDate(new Date());
        quotationHeader.setUser(Long.parseLong(user.getId().toString()));

        quotationHeader.setId(dao.insertQuotations(quotationHeader));
        dao.insertQuotationDetail(quotationHeader);
        return quotationHeader.getId();

    }

    @Override
    public List<QuotationHeader> getPatientBy(String name) throws Exception
    {
        return dao.getPatientBy(name);

    }

    @Override
    public List<QuotationHeader> listQuotationHeader(Date init, Date end)
    {
        return dao.listQuotationHeader(DateTools.getInitialDate(init), DateTools.getFinalDate(end));
    }

}
