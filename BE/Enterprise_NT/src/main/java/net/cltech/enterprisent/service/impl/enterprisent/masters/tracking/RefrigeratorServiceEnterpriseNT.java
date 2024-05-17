package net.cltech.enterprisent.service.impl.enterprisent.masters.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.tracking.RefrigeratorDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.tracking.Refrigerator;
import net.cltech.enterprisent.service.interfaces.masters.tracking.RefrigeratorService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a nevera
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 08/06/2017
 * @see Creación
 */
@Service
public class RefrigeratorServiceEnterpriseNT implements RefrigeratorService
{

    @Autowired
    private RefrigeratorDao dao;

    @Autowired
    private TrackingService trackingService;

    @Override
    public Refrigerator create(Refrigerator create) throws Exception
    {
        create.setUser(trackingService.getRequestUser());
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Refrigerator newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Refrigerator.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Refrigerator> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Refrigerator filterById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public Refrigerator filterByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.trim().equalsIgnoreCase(a.getName()));
    }

    @Override
    public Refrigerator update(Refrigerator update) throws Exception
    {
        update.setUser(trackingService.getRequestUser());
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Refrigerator old = filterById(update.getId());
            Refrigerator updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Refrigerator.class);
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
    private List<String> validateFields(Refrigerator validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();
        List<Refrigerator> all = dao.list();
        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (filterById(validate.getId()) == null)
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
            Refrigerator found = findBy(all, a -> a.getName().equalsIgnoreCase(validate.getName()));

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

        if (validate.getBranch() == null)
        {
            errors.add("0|branch");
        }

        return errors;

    }

    @Override
    public List<Refrigerator> filterByState(boolean state) throws Exception
    {
        List<Refrigerator> filter = dao.list().stream()
                .filter(bean -> bean.isState() == state)
                .collect(Collectors.toList());
        return filter;
    }

    /**
     * Busqueda por una expresion
     *
     * @param alarmList listado en el que se realiza la busqueda
     * @param predicate criterio de busqueda
     *
     * @return Objeto encontrado, null si no se encuentra
     */
    public Refrigerator findBy(List<Refrigerator> alarmList, Predicate<Refrigerator> predicate)
    {
        return alarmList.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

}
