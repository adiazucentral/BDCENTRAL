/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.tools;

import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.BranchDao;
import net.cltech.enterprisent.dao.interfaces.tools.BarcodeDao;
import net.cltech.enterprisent.domain.tools.PrintLog;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.tools.PrintLogService;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hpoveda
 */
@Service
public class PrintLogServiceEnterpriseNT implements PrintLogService
{

    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private BarcodeDao dao;
    @Autowired
    private BranchDao branchDao;

    @Override
    public Boolean InsertPrint(PrintLog printLog) throws Exception
    {
        printLog.setBranchName(branchDao.get(printLog.getBranch(), null, null, null).getName());
        return !Objects.equals(dao.createLogPrintClient(printLog), null);
    }

    @Override
    public List<PrintLog> list() throws Exception
    {
        int orderDigits = configurationService.getIntValue("DigitosOrden");
        String initDate = String.valueOf(Tools.buildInitialOrder((int) 10, orderDigits));
        String finalDate = String.valueOf(Tools.buildFinalOrder(orderDigits));

        return dao.listLogPrintClient(finalDate, initDate);
    }

}
