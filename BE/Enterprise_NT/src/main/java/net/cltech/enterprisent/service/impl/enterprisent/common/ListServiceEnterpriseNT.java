package net.cltech.enterprisent.service.impl.enterprisent.common;

import java.util.List;
import net.cltech.enterprisent.dao.interfaces.common.ListDao;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.service.interfaces.common.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa los servicios de lista de items para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 19/04/2017
 * @see Creaciòn
 */
@Service
public class ListServiceEnterpriseNT implements ListService
{

    @Autowired
    private ListDao dao;
    
    @Transactional(readOnly = false, isolation=Isolation.READ_UNCOMMITTED)
    @Override
    public List<Item> list(int id) throws Exception
    {
        return dao.list(id);
    }

    @Override
    public Item update(Item item) throws Exception
    {
        return dao.update(item);
    }

}
