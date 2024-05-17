package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.RaceDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.service.interfaces.masters.demographic.RaceService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a las unidades de examenes
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 17/04/2017
 * @see Creación
 */
@Service
public class RaceServiceEnterpriseNT implements RaceService
{

    @Autowired
    private RaceDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Race create(Race create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Race newRace = dao.create(create);
            trackingService.registerConfigurationTracking(null, newRace, Race.class);
            return newRace;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Race> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Race filterById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public Race filterByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.trim().equalsIgnoreCase(a.getName()));
    }

    @Override
    public Race filterByCode(String code) throws Exception
    {
        return findBy(dao.list(), a -> code.trim().equalsIgnoreCase(a.getCode()));
    }

    @Override
    public Race update(Race update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Race oldRace = filterById(update.getId());
            Race updated = dao.update(update);
            trackingService.registerConfigurationTracking(oldRace, update, Race.class);
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
    private List<String> validateFields(Race validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();

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
            Race found = filterByName(validate.getName());
            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|name");//duplicado
                }
            }
        }

        if (validate.getCode() == null || validate.getCode().trim().isEmpty())
        {
            errors.add("0|code");
        } else
        {
            Race found = filterByCode(validate.getCode());
            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|code");//duplicado
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
    public List<Race> filterByState(boolean state) throws Exception
    {
        List<Race> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Race) o).isState() == state));
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
    public Race findBy(List<Race> alarmList, Predicate<Race> predicate)
    {
        return alarmList.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

}
