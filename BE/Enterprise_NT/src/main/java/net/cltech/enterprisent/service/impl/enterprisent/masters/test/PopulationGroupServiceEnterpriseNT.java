package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.test.PopulationGroupDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.PopulationGroup;
import net.cltech.enterprisent.service.interfaces.masters.test.PopulationGroupService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a grupo poblacional
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 22/05/2017
 * @see Creación
 */
@Service
public class PopulationGroupServiceEnterpriseNT implements PopulationGroupService
{

    @Autowired
    private PopulationGroupDao dao;

    @Autowired
    private TrackingService trackingService;

    @Override
    public PopulationGroup create(PopulationGroup create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            PopulationGroup newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, PopulationGroup.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<PopulationGroup> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public PopulationGroup filterById(Integer id) throws Exception
    {
        return dao.filterById(id);
    }

    @Override
    public PopulationGroup filterByName(String name) throws Exception
    {
        return dao.filterByName(name);
    }

    @Override
    public PopulationGroup update(PopulationGroup update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            PopulationGroup old = dao.filterById(update.getId());
            PopulationGroup updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, PopulationGroup.class);
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
    private List<String> validateFields(PopulationGroup validate, boolean isEdit) throws Exception
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
            PopulationGroup found = dao.filterByName(validate.getName());

            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|name");//duplicado
                }
            }
        }
        if (validate.getSex() == null)
        {
            errors.add("0|sex");
        } else
        {

            if (!validate.getSex().equals(7) && !validate.getSex().equals(8) && !validate.getSex().equals(9) && !validate.getSex().equals(42))
            {
                errors.add("3|sex");
            }
        }
        if (validate.getUnit() == null)
        {
            errors.add("0|unit");
        } else
        {

            if (!validate.getUnit().equals(1) && !validate.getUnit().equals(2))
            {
                errors.add("3|unit");
            }
        }
        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }
        if (validate.getInitialRange() == null)
        {
            errors.add("0|initialRange");
        }
        if (validate.getFinalRange() == null)
        {
            errors.add("0|finalRange");
        }

        return errors;

    }

    @Override
    public List<PopulationGroup> filterByState(boolean state) throws Exception
    {
        List<PopulationGroup> filter = dao.list().stream()
                .filter(bean -> bean.isState() == state)
                .collect(Collectors.toList());
        return filter;
    }

}
