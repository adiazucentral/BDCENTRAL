package net.cltech.enterprisent.service.impl.enterprisent.operation.orders;

import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.orders.RecalledDao;
import net.cltech.enterprisent.domain.operation.orders.Recalled;
import net.cltech.enterprisent.service.interfaces.operation.orders.RecalledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de servicios de rellamado de ordenes para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/08/2019
 * @see Creacion
 */
@Service
public class RecalledServiceEnterpriseNT implements RecalledService
{

    @Autowired
    private RecalledDao recalledDao;

    @Override
    public List<Recalled> list() throws Exception
    {
        return recalledDao.list();
    }

    @Override
    public Recalled create(Recalled recalled) throws Exception
    {
        return recalledDao.create(recalled);
    }

}
