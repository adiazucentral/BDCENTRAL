/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.tools;

import java.util.List;
import net.cltech.enterprisent.domain.tools.PrintLog;

/**
 *
 * @author hpoveda
 */
public interface PrintLogService
{

    public Boolean InsertPrint(PrintLog printLog) throws Exception;

    public List<PrintLog> list() throws Exception;

}
