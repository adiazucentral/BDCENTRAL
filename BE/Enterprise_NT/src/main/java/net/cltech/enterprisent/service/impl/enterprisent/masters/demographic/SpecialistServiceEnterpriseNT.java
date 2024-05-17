package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.SpecialistDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.Specialist;
import net.cltech.enterprisent.service.interfaces.masters.demographic.SpecialistService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a especialista
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 11/05/2017
 * @see Creación
 */
@Service
public class SpecialistServiceEnterpriseNT implements SpecialistService
{

    @Autowired
    private SpecialistDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Specialist create(Specialist create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Specialist newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Specialist.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Specialist> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Specialist filterById(Integer id) throws Exception
    {
        return dao.filterById(id);
    }

    @Override
    public Specialist filterByName(String name) throws Exception
    {
        return dao.filterByName(name);
    }

    @Override
    public Specialist update(Specialist update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Specialist old = dao.filterById(update.getId());
            Specialist updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Specialist.class);
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
    private List<String> validateFields(Specialist validate, boolean isEdit) throws Exception
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
            Specialist found = dao.filterByName(validate.getName());
            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|name");//duplicado
                }
            }
        }
        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }

        return errors;

    }

    @Override
    public List<Specialist> filterByState(boolean state) throws Exception
    {
        List<Specialist> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Specialist) o).isState() == state));
        return filter;
    }

}
