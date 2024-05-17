package net.cltech.enterprisent.service.impl.enterprisent.masters.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.common.HolidayDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.common.Holiday;
import net.cltech.enterprisent.service.interfaces.masters.common.HolidayService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.DateTools;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Festivos para Enterprise
 * NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 30/08/2017
 * @see Creaciòn
 */
@Service
public class HolidayServiceEnterpriseNT implements HolidayService
{

    @Autowired
    private HolidayDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Holiday> list() throws Exception
    {
        return dao.list();
    }
    
    @Override
    public List<String> listBasic() throws Exception
    {
        return dao.listBasic();
    }

    @Override
    public Holiday create(Holiday holiday) throws Exception
    {
        List<String> errors = validateFields(false, holiday);
        if (errors.isEmpty())
        {
            Holiday created = dao.create(holiday);
            trackingService.registerConfigurationTracking(null, created, Holiday.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Holiday get(Integer id, String name, Date date) throws Exception
    {
        if (date != null)
        {
            return getByDate(date);
        } else
        {
            return dao.get(id, name);
        }
    }

    @Override
    public Holiday update(Holiday holiday) throws Exception
    {
        List<String> errors = validateFields(true, holiday);
        if (errors.isEmpty())
        {
            Holiday holidayC = dao.get(holiday.getId(), null);
            Holiday modifited = dao.update(holiday);
            trackingService.registerConfigurationTracking(holidayC, modifited, Holiday.class);
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
    public List<Holiday> list(boolean state) throws Exception
    {
        List<Holiday> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Holiday) o).isState() == state));
        return filter;
    }

    /**
     * Validacion de campos
     *
     * @param isEdit  es edición
     * @param holiday campos a validar
     *
     * @return LIsta de errores
     *         0 -> Datos vacios
     *         1 -> Esta duplicado
     *         2 -> Id no existe solo aplica para modificar
     * @throws Exception Error
     */
    private List<String> validateFields(boolean isEdit, Holiday holiday) throws Exception
    {

        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (holiday.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(holiday.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (holiday.getName() != null && !holiday.getName().isEmpty())
        {
            Holiday holidayC = dao.get(null, holiday.getName());
            if (holidayC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(holiday.getId(), holidayC.getId()))
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

        if (holiday.getDate() != null)
        {
            Holiday holidayC = getByDate(holiday.getDate());
            if (holidayC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(holiday.getId(), holidayC.getId()))
                    {
                        errors.add("1|date");
                    }
                } else
                {
                    errors.add("1|date");
                }
            }
        } else
        {
            errors.add("0|date");
        }

        if (holiday.getUser().getId() == null || holiday.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    /**
     * Obtiene festivo por fecha
     *
     * @param date fecha
     *
     * @return Festivo
     * @throws Exception Error
     */
    private Holiday getByDate(Date date) throws Exception
    {
        return dao.list().stream()
                .filter(holiday -> DateTools.getDateWithoutTime(holiday.getDate()).equals(DateTools.getDateWithoutTime(date)))
                .findFirst()
                .orElse(null);
    }
}
