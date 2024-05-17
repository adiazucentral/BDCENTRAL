package net.cltech.enterprisent.service.impl.enterprisent.operation.orders;

import net.cltech.enterprisent.dao.interfaces.operation.orders.ConcurrencyDao;
import net.cltech.enterprisent.service.interfaces.operation.orders.ConcurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de servicios de concurrencia para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/11/2017
 * @see Creacion
 */
@Service
public class ConcurrencyServiceEnterpriseNT implements ConcurrencyService
{

    @Autowired
    private ConcurrencyDao dao;

    @Override
    public int deleteAll() throws Exception
    {
        return dao.deleteAll();
    }

    @Override
    public int deleteOrder(Long order) throws Exception
    {
        return dao.deleteOrder(order);
    }

    @Override
    public int deleteRecord(Integer type, String record) throws Exception
    {
        return dao.deleteRecord(type == 0 ? null : type, record);
    }

}
