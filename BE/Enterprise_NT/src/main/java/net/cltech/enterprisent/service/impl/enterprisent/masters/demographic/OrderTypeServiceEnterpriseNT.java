package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.OrderTypeDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.service.interfaces.masters.demographic.OrderTypeService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a tipo orden
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 16/05/2017
 * @see Creación
 */
@Service
public class OrderTypeServiceEnterpriseNT implements OrderTypeService
{

    @Autowired
    private OrderTypeDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public OrderType create(OrderType create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            OrderType newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, OrderType.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<OrderType> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public OrderType filterById(Integer id) throws Exception
    {
        return dao.filterById(id);
    }

    @Override
    public OrderType filterByName(String name) throws Exception
    {
        return dao.filterByName(name);
    }

    @Override
    public OrderType update(OrderType update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            OrderType old = dao.filterById(update.getId());
            if (isRutineOrEmergency(update))
            {
                update.setState(old.isState());
            }
            if (isSystemType(update))
            {
                update.setCode(old.getCode());
            }

            OrderType updated = dao.update(update);

            trackingService.registerConfigurationTracking(old, update, OrderType.class);
            return updated;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    /**
     * Identifica si es un tipo de orden por defecto del sistema
     *
     * @param update
     *
     * @return
     */
    private static boolean isSystemType(OrderType update)
    {
        return update.getId() >= 1 && update.getId() <= 5;
    }

    /**
     * Identifica si un tipo de orden obligatorio del sistema
     *
     * @param update
     *
     * @return
     */
    private static boolean isRutineOrEmergency(OrderType update)
    {
        return update.getId() == 1 || update.getId() == 2;
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(OrderType validate, boolean isEdit) throws Exception
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
            OrderType found = dao.filterByName(validate.getName());
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
            OrderType found = filterByCode(validate.getCode());
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
    public List<OrderType> filterByState(boolean state) throws Exception
    {
        List<OrderType> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((OrderType) o).isState() == state));
        return filter;
    }

    @Override
    public OrderType filterByCode(String code) throws Exception
    {
        return dao.list().stream()
                .filter(e -> e.getCode().equals(code) || e.getName().equals(code))
                .findFirst()
                .orElse(null);
    }

    
}
