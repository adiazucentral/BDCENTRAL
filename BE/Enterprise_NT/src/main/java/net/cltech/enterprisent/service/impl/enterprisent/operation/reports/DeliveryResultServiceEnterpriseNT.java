/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.reports.DeliveryResultDao;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.reports.DeliveryOrder;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.operation.reports.DeliveryResultService;
import net.cltech.enterprisent.service.interfaces.operation.results.CheckResultService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de entrega de resultados para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/11/2017
 * @see Creacion
 */
@Service
public class DeliveryResultServiceEnterpriseNT implements DeliveryResultService
{

    @Autowired
    private DeliveryResultDao dao;
    private CheckResultService checkResultService;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;
    @Autowired
    private HttpServletRequest request;


    @Override
    public List<DeliveryOrder> listFilters(Filter search) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        return dao.listDeliveryResult(search, laboratorys, idbranch);
    }

    @Override
    public List<OrderList> listDeliveryResultPending(Filter search) throws Exception
    {
        search.setFilterState(Arrays.asList(LISEnum.ResultTestCommonState.VALIDATED.getValue()));
        List<OrderList> orders = checkResultService.list(search);

        if (orders.isEmpty())
        {
            return new ArrayList<>();
        }
        return orders;
    }
}
