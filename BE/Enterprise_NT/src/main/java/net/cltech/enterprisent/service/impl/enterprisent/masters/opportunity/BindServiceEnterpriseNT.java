package net.cltech.enterprisent.service.impl.enterprisent.masters.opportunity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.opportunity.BindDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;
import net.cltech.enterprisent.service.interfaces.masters.opportunity.BindService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Sede para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 15/02/2018
 * @see Creaciòn
 */
@Service
public class BindServiceEnterpriseNT implements BindService
{

    @Autowired
    private BindDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Bind> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Bind create(Bind bind) throws Exception
    {
        List<String> errors = validateFields(false, bind);
        if (errors.isEmpty())
        {
            Bind created = dao.create(bind);
            trackingService.registerConfigurationTracking(null, created, Bind.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Bind get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public Bind update(Bind bind) throws Exception
    {
        List<String> errors = validateFields(true, bind);
        if (errors.isEmpty())
        {
            Bind bindC = dao.get(bind.getId(), null);
            Bind modifited = dao.update(bind);
            trackingService.registerConfigurationTracking(bindC, modifited, Bind.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Bind> list(boolean state) throws Exception
    {
        return dao.list().stream()
                .filter(bind -> bind.isState() == state)
                .collect(Collectors.toList());
    }

    private List<String> validateFields(boolean isEdit, Bind bind) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (bind.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(bind.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (bind.getName() != null && !bind.getName().isEmpty())
        {
            Bind bindC = dao.get(null, bind.getName());
            if (bindC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(bind.getId(), bindC.getId()))
                    {
                        errors.add("1|name");
                    }
                } else
                {
                    errors.add("1|name");
                }
            }
        } else
        {
            errors.add("0|name");
        }

        if (bind.getUser().getId() == null || bind.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

}
