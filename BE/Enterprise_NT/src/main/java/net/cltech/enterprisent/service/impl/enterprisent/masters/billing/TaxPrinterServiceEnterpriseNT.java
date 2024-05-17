package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.billing.BankDao;
import net.cltech.enterprisent.dao.interfaces.masters.billing.TaxPrinterDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.Bank;
import net.cltech.enterprisent.domain.masters.billing.TaxPrinter;
import net.cltech.enterprisent.service.interfaces.masters.billing.TaxPrinterService;
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
public class TaxPrinterServiceEnterpriseNT implements TaxPrinterService
{

    @Autowired
    private TaxPrinterDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<TaxPrinter> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public TaxPrinter create(TaxPrinter taxPrinter) throws Exception
    {
        List<String> errors = validateFields(false, taxPrinter);
        if (errors.isEmpty())
        {
            TaxPrinter created = dao.create(taxPrinter);
            trackingService.registerConfigurationTracking(null, created, Bank.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public TaxPrinter get(Integer id, String name,String code) throws Exception
    {
        return dao.get(id, name,code);
    }

    @Override
    public TaxPrinter update(TaxPrinter taxPrinter) throws Exception
    {
        List<String> errors = validateFields(true, taxPrinter);
        if (errors.isEmpty())
        {
            TaxPrinter taxPrinterC = dao.get(taxPrinter.getId(), null, null);
            TaxPrinter modifited = dao.update(taxPrinter);
            trackingService.registerConfigurationTracking(taxPrinterC, modifited, Bank.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<TaxPrinter> list(boolean state) throws Exception
    {
        List<TaxPrinter> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((TaxPrinter) o).isState() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, TaxPrinter taxPrinter) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (taxPrinter.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(taxPrinter.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (taxPrinter.getName() != null && !taxPrinter.getName().isEmpty())
        {
            TaxPrinter taxPrinterC = dao.get(null, taxPrinter.getName(), null);
            if (taxPrinterC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(taxPrinter.getId(), taxPrinterC.getId()))
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
        
        if (taxPrinter.getCode() != null && !taxPrinter.getCode().isEmpty())
        {
            TaxPrinter taxPrinterC = dao.get(null, null, taxPrinter.getCode());
            if (taxPrinterC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(taxPrinter.getId(), taxPrinterC.getId()))
                    {
                        errors.add("1|code");
                    }
                } else
                {
                    errors.add("1|code");
                }
            }
        } else
        {
            errors.add("0|code");
        }

        if (taxPrinter.getUser().getId() == null || taxPrinter.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
}
