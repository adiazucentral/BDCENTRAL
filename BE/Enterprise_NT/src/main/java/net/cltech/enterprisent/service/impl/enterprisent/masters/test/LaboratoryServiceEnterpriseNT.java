package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.test.LaboratoryDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratoryService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a laboratorio
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 19/05/2017
 * @see Creación
 */
@Service
public class LaboratoryServiceEnterpriseNT implements LaboratoryService
{

    @Autowired
    private LaboratoryDao dao;

    @Autowired
    private TrackingService trackingService;

    @Override
    public Laboratory create(Laboratory create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Laboratory newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Laboratory.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Laboratory> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Laboratory filterById(Integer id) throws Exception
    {
        return dao.filterById(id);
    }

    @Override
    public Laboratory filterByName(String name) throws Exception
    {
        return dao.filterByName(name);
    }

    @Override
    public Laboratory update(Laboratory update, Integer type) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Laboratory old = dao.filterById(update.getId());
            Laboratory updated = dao.update(update);
            if (type == Constants.ORIGINLABORATORY)
            {
                trackingService.registerConfigurationTracking(old, update, Laboratory.class);
            } else
            {
                trackingService.registerConfigurationTracking(old, update, Laboratory.class, "net.cltech.enterprisent.domain.masters.test.integrationMiddleware");
            }
            return updated;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(Laboratory validate, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (dao.filterById(validate.getId()) == null)
                {
                    errors.add("2|id");//No existe id
                }
            }
        }
        if (validate.getName() == null || validate.getName().trim().isEmpty())
        {
            errors.add("0|name");
        } else
        {
            Laboratory found = dao.filterByName(validate.getName());
            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|name");//duplicado
                }
            }
        }
        if (validate.getCode() == null)
        {
            errors.add("0|code");
        } else
        {
            Laboratory found = filterByCode(validate.getCode());
            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|code");
                }
            }
        }
        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }
        if (validate.getType() != (short) 1 && validate.getType() != (short) 2)
        {
            errors.add("3|type");
        }

        return errors;

    }

    @Override
    public List<Laboratory> filterByState(boolean state) throws Exception
    {
        List<Laboratory> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Laboratory) o).isState() == state));
        return filter;
    }

    @Override
    public Laboratory filterByCode(Integer code) throws Exception
    {
        Laboratory filter = dao.list().stream()
                .filter(laboratory -> laboratory.getCode().equals(code))
                .findFirst()
                .orElse(null);
        return filter;
    }

    @Override
    public List<Laboratory> listLaboratorysProcessing() throws Exception
    {
        return dao.listLaboratorysProcessing();
    }

}
