package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.PhysicianDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.PhysicianService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a medicos
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 24/05/2017
 * @see Creación
 */
@Service
public class PhysicianServiceEnterpriseNT implements PhysicianService
{

    @Autowired
    private PhysicianDao dao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Physician create(Physician create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Physician newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Physician.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Physician> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Physician filterById(Integer id) throws Exception
    {
        return dao.filterById(id);
    }

    @Override
    public Physician filterByIdentification(String identification) throws Exception
    {
        return dao.filterByIdentification(identification);
    }

    @Override
    public Physician update(Physician update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Physician old = dao.filterById(update.getId());
            Physician updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Physician.class);
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
     * @param state estado
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    @Override
    public List<Physician> filterByState(boolean state) throws Exception
    {
        List<Physician> filter = dao.list().stream()
                .filter(bean -> bean.isActive() == state)
                .collect(Collectors.toList());
        return filter;
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     * @param isEdit
     *
     * @return lista de campos que son requeridos y no estan establecidos
     * @throws java.lang.Exception
     */
    public List<String> validateFields(Physician validate, boolean isEdit) throws Exception
    {
        boolean isBillingUsa = Configuration.BILLING_USA.equals(configurationService.getValue(Configuration.KEY_BILLING));

        List<String> errors = new ArrayList<>();
        List<Physician> allRecords = dao.list();
        if (validate.getIdentification() == null || validate.getIdentification().trim().isEmpty())
        {
            errors.add("0|identification");
        } else
        {
            allRecords.stream()
                    .filter(physician -> validate.getIdentification().equals(physician.getIdentification()) && (!isEdit || (isEdit && !validate.getId().equals(physician.getId()))))
                    .findFirst()
                    .ifPresent(physician -> errors.add("1|identification"));
        }

        if (validate.getEmail() == null || validate.getEmail().trim().isEmpty())
        {
            errors.add("0|email");
        } 

        if (validate.getCode() == null || validate.getCode().trim().isEmpty())
        {
            errors.add("0|code");
        } else if (validate.getCode() != null)
        {
            allRecords.stream()
                    .filter(physician -> validate.getCode().equals(physician.getCode()) && (!isEdit || (isEdit && !validate.getId().equals(physician.getId()))))
                    .findFirst()
                    .ifPresent(physician -> errors.add("1|code"));
        }

        if (validate.getName() == null || validate.getName().trim().isEmpty())
        {
            errors.add("0|name");
        }

        if (isBillingUsa)
        {
            if (validate.getLicense() == null || validate.getLicense().trim().isEmpty())
            {
                errors.add("0|license");
            } else
            {
                allRecords.stream()
                        .filter(physician -> validate.getLicense().equals(physician.getLicense()) && (!isEdit || (isEdit && !validate.getId().equals(physician.getId()))))
                        .findFirst()
                        .ifPresent(physician -> errors.add("1|license"));
            }

        }
        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }

        return errors;
    }

}
