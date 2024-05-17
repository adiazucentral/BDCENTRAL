package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.billing.ProviderDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.Provider;
import net.cltech.enterprisent.service.interfaces.masters.billing.ProviderService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Emisor para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 02/05/2018
 * @see Creaciòn
 */
@Service
public class ProviderServiceEnterpriseNT implements ProviderService
{

    @Autowired
    private ProviderDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Provider> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Provider create(Provider account) throws Exception
    {
        List<String> errors = validateFields(false, account);
        if (errors.isEmpty())
        {
            Provider created = dao.create(account);
            trackingService.registerConfigurationTracking(null, created, Provider.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Provider get(Integer id, String name, String nit, String codeEps) throws Exception
    {
        return dao.get(id, name, nit, codeEps);
    }

    @Override
    public Provider update(Provider account) throws Exception
    {
        List<String> errors = validateFields(true, account);
        if (errors.isEmpty())
        {
            Provider current = dao.get(account.getId(), null, null, null);
            Provider modified = dao.update(account);
            trackingService.registerConfigurationTracking(current, modified, Provider.class);
            return modified;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Provider> list(boolean state) throws Exception
    {
        List<Provider> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Provider) o).isActive() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, Provider account) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (account.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(account.getId(), null, null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (account.getNit() != null)
        {
            Provider accountC = dao.get(null, null, account.getNit(), null);
            if (accountC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(account.getId(), accountC.getId()))
                    {
                        errors.add("1|nit");
                    }
                } else
                {
                    errors.add("1|nit");
                }
            }
        }

        if (account.getCode() != null && !account.getCode().isEmpty())
        {
            Provider accountC = dao.get(null, null, null, account.getCode());
            if (accountC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(account.getId(), accountC.getId()))
                    {
                        errors.add("1|eps code");
                    }
                } else
                {
                    errors.add("1|eps code");
                }
            }
        }

        if (account.getName() != null && !account.getName().isEmpty())
        {
            Provider accountC = dao.get(null, account.getName(), null, null);
            if (accountC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(account.getId(), accountC.getId()))
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

        if (account.getUser().getId() == null || account.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
    
    @Override
    public Provider getProviderParticular() throws Exception
    {
        return dao.getProviderParticular();
    }

}
