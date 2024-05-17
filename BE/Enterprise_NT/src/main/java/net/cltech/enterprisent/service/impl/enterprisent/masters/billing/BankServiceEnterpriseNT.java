package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.billing.BankDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.Bank;
import net.cltech.enterprisent.service.interfaces.masters.billing.BankService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Bancos para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 07/06/2017
 * @see Creación
 */
@Service
public class BankServiceEnterpriseNT implements BankService
{

    @Autowired
    private BankDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Bank> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Bank create(Bank bank) throws Exception
    {
        List<String> errors = validateFields(false, bank);
        if (errors.isEmpty())
        {
            Bank created = dao.create(bank);
            trackingService.registerConfigurationTracking(null, created, Bank.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Bank get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public Bank update(Bank bank) throws Exception
    {
        List<String> errors = validateFields(true, bank);
        if (errors.isEmpty())
        {
            Bank bankC = dao.get(bank.getId(), null);
            Bank modifited = dao.update(bank);
            trackingService.registerConfigurationTracking(bankC, modifited, Bank.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        dao.delete(id);
    }

    @Override
    public List<Bank> list(boolean state) throws Exception
    {
        List<Bank> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Bank) o).isState() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, Bank bank) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (bank.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(bank.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (bank.getName() != null && !bank.getName().isEmpty())
        {
            Bank bankC = dao.get(null, bank.getName());
            if (bankC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(bank.getId(), bankC.getId()))
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

        if (bank.getUser().getId() == null || bank.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
}
