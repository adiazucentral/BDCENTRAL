package net.cltech.enterprisent.service.impl.enterprisent.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.tools.BarcodeDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.tools.BarcodeDesigner;
import net.cltech.enterprisent.service.interfaces.tools.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion codigo de barras para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 10/12/2018
 * @see Creación
 */
@Service
public class BarcodeServiceEnterpriseNT implements BarcodeService
{

    @Autowired
    private BarcodeDao dao;

    @Override
    public List<BarcodeDesigner> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public BarcodeDesigner create(BarcodeDesigner create) throws Exception
    {
        List<String> errors = validateFields(false, create);
        if (errors.isEmpty())
        {
            return dao.create(create);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public BarcodeDesigner update(BarcodeDesigner barcode) throws Exception
    {
        List<String> errors = validateFields(true, barcode);
        if (errors.isEmpty())
        {
            BarcodeDesigner updated = dao.update(barcode);
            return updated;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<BarcodeDesigner> list(boolean state) throws Exception
    {
        return list().stream()
                .filter(barcode -> barcode.isActive() == state)
                .collect(Collectors.toList());
    }

    private List<String> validateFields(boolean isEdit, BarcodeDesigner bean) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (bean.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (getById(bean.getId()) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (bean.getTemplate() == null || bean.getTemplate().isEmpty())
        {
            errors.add("0|template");
        }
        if (bean.getCommand() == null || bean.getCommand().trim().isEmpty())
        {
            errors.add("0|command");
        }

        return errors;
    }

    @Override
    public BarcodeDesigner getById(Integer id) throws Exception
    {
        return list().stream()
                .filter(barcode -> Objects.equals(barcode.getId(), id))
                .findFirst()
                .orElse(null);
    }

}
