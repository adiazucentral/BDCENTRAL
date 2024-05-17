/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.tools;

import java.util.List;
import net.cltech.enterprisent.dao.interfaces.tools.BarcodeDao;
import net.cltech.enterprisent.domain.tools.OrderTestPatientHistory;
import net.cltech.enterprisent.service.interfaces.tools.UpdateDateHistoricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Actualiza las ordenes que tengan resultado de histórico sin fecha
 *
 * @author hpoveda
 */
@Service
public class UpdateDateHistoricsServiceEnterpriseNT implements UpdateDateHistoricsService
{

    @Autowired
    //  private UpdateDateHistoricsDao dao;
    private BarcodeDao dao;

    @Override
    public String updateHistorics() throws Exception
    {
        StringBuilder response = new StringBuilder();
        List<OrderTestPatientHistory> orderTestPatients = dao.listOrderTestPatien();

        orderTestPatients.forEach(order ->
        {
            try
            {
                order.setNumberOrderLastValidate(dao.lastOrderValidate(order) == 0L
                        ? dao.lastOrderValidate1(order) == 0L
                        ? dao.lastOrderValidate2(order) == 0L
                        ? dao.lastOrderValidate3(order) : dao.lastOrderValidate2(order) : dao.lastOrderValidate1(order) : dao.lastOrderValidate(order));
                dao.updateHistory(order);
                dao.updateHistory1(order);
                dao.updateHistory2(order);

            } catch (Exception ex)
            {
                response.append(" ERROR " + ex);
            }
        });
        //actualiza la lab17
        List<OrderTestPatientHistory> orderTestPatientsla17 = dao.listOrderTestPatienLab17();
        orderTestPatientsla17.forEach(order ->
        {
            try
            {
                dao.updateHistory17(order);
                dao.updateHistory1722(order);
            } catch (Exception e)
            {
                response.append(" ERROR " + e);
            }
        });
        return response.toString();
    }

}
