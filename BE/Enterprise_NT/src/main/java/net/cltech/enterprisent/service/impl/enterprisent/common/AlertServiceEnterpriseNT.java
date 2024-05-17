package net.cltech.enterprisent.service.impl.enterprisent.common;

import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.AlertDao;
import net.cltech.enterprisent.domain.common.Alert;
import net.cltech.enterprisent.service.interfaces.common.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios alert para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 19/09/2017
 * @see Creaciòn
 */
@Service
public class AlertServiceEnterpriseNT implements AlertService
{

    @Autowired
    private AlertDao dao;

    @Override
    public List<Alert> list(String form) throws Exception
    {
        return dao.list()
                .stream()
                .filter(filter -> form.equals("0") || form.equals(filter.getForm()))
                .collect(Collectors.toList());
    }

    @Override
    public int add(Alert alert) throws Exception
    {
        return dao.create(alert);
    }

    @Override
    public int delete(Alert alert) throws Exception
    {
        return dao.delete(alert);
    }

}
