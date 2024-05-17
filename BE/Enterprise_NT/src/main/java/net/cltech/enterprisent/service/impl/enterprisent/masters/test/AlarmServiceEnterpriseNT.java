package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.test.AlarmDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Alarm;
import net.cltech.enterprisent.service.interfaces.masters.test.AlarmService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a alarma
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 06/06/2017
 * @see Creación
 */
@Service
public class AlarmServiceEnterpriseNT implements AlarmService
{

    @Autowired
    private AlarmDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Alarm create(Alarm create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Alarm newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Alarm.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Alarm> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Alarm filterById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public Alarm filterByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.equalsIgnoreCase(a.getName()));
    }

    @Override
    public Alarm update(Alarm update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Alarm old = filterById(update.getId());
            Alarm updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Alarm.class);
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
    private List<String> validateFields(Alarm validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();
        List<Alarm> all = dao.list();
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
            Alarm found = findBy(all, a -> a.getName().equalsIgnoreCase(validate.getName()));

            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|name");//duplicado
                }
            }
        }
        if (validate.getDescription() == null || validate.getDescription().trim().isEmpty())
        {
            errors.add("0|description");
        }

        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }

        return errors;

    }

    @Override
    public List<Alarm> filterByState(boolean state) throws Exception
    {
        List<Alarm> filter = dao.list().stream()
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
    public Alarm findBy(List<Alarm> alarmList, Predicate<Alarm> predicate)
    {
        return alarmList.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

}
