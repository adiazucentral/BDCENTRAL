package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.billing.PaymentTypeDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.PaymentType;
import net.cltech.enterprisent.service.interfaces.masters.billing.PaymentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Tipo de pago para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 30/08/2017
 * @see Creación
 */
@Service
public class PaymentTypeServiceEnterpriseNT implements PaymentTypeService
{

    @Autowired
    private PaymentTypeDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<PaymentType> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public PaymentType create(PaymentType type) throws Exception
    {
        List<String> errors = validateFields(false, type);
        if (errors.isEmpty())
        {
            PaymentType created = dao.create(type);
            trackingService.registerConfigurationTracking(null, created, PaymentType.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public PaymentType update(PaymentType type) throws Exception
    {
        List<String> errors = validateFields(true, type);
        if (errors.isEmpty())
        {
            PaymentType before = findById(type.getId());
            PaymentType newRecord = dao.update(type);
            trackingService.registerConfigurationTracking(before, newRecord, PaymentType.class);
            return newRecord;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<PaymentType> list(boolean state) throws Exception
    {
        return dao.list().stream()
                .filter(type -> type.isState() == state)
                .collect(Collectors.toList());
    }

    /**
     * Valida errores de campos
     *
     * @param isEdit
     * @param type 0 -> Datos vacios<br>
     * 1 -> Esta duplicado<br>
     * 2 -> Id no existe solo aplica para modificar
     *
     * @return
     * @throws Exception
     */
    private List<String> validateFields(boolean isEdit, PaymentType type) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (type.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (findById(type.getId()) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (type.getName() != null && !type.getName().isEmpty())
        {
            PaymentType typeC = findByName(type.getName());
            if (typeC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(type.getId(), typeC.getId()))
                    {
                        errors.add("1|name");
                    }
                } else
                {
                    errors.add("1|name");
                }
            }
        } 
        if (type.getCode() != null && !type.getCode().isEmpty())
        {
            PaymentType typeC = findByCode(type.getCode());
            if (typeC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(type.getId(), typeC.getId()))
                    {
                        errors.add("1|code");
                    }
                } else
                {
                    errors.add("1|code");
                }
            }
        }
        else
        {
            errors.add("0|code");
        }

        if (type.getUser().getId() == null || type.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    @Override
    public PaymentType findById(Integer id) throws Exception
    {
        return dao.list().stream()
                .filter(type -> type.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public PaymentType findByName(String name) throws Exception
    {
        return dao.list().stream()
                .filter(type -> type.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public PaymentType findByCode(String code) throws Exception
    {
        return dao.list().stream()
                .filter(type -> type.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

}
