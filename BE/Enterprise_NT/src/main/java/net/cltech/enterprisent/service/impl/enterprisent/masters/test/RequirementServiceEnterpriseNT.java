package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.test.RequirementDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.service.interfaces.masters.test.RequirementService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de servicios requisito
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 28/04/2017
 * @see Creación
 */
@Service
public class RequirementServiceEnterpriseNT implements RequirementService
{

    @Autowired
    private RequirementDao requirementDao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Requirement create(Requirement create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Requirement created = requirementDao.create(create);
            trackingService.registerConfigurationTracking(null, created, Requirement.class);
            return created;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Requirement> list() throws Exception
    {
        return requirementDao.list();
    }

    @Override
    public Requirement filterById(Integer id) throws Exception
    {
        return requirementDao.findById(id);
    }

    @Override
    public Requirement filterByCode(String code) throws Exception
    {
        return requirementDao.findByCode(code);
    }

    @Override
    public void update(Requirement update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Requirement old = requirementDao.findById(update.getId());
            requirementDao.update(update);
            trackingService.registerConfigurationTracking(old, update, Requirement.class);
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
    private List<String> validateFields(Requirement validate, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (requirementDao.findById(validate.getId()) == null)//Si existe
                {
                    errors.add("2|id");
                }
            }
        }
        if (validate.getCode() == null || validate.getCode().trim().isEmpty())
        {
            errors.add("0|code");
        } else
        {
            Requirement unitFromDB = requirementDao.findByCode(validate.getCode());
            if (unitFromDB != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(unitFromDB.getId())))
                {
                    errors.add("1|code");
                }
            }
        }

        if (validate.getRequirement() == null || validate.getRequirement().trim().isEmpty())
        {
            errors.add("0|requirement");
        }

        if (validate.getUser().getId() == null || validate.getUser().getId().equals(0))
        {
            errors.add("0|userId");
        }
        return errors;

    }

    @Override
    public List<Requirement> filterByState(boolean state) throws Exception
    {
        List<Requirement> filter = new ArrayList<>(CollectionUtils.filter(requirementDao.list(), (Object o) -> ((Requirement) o).isState() == state));
        return filter;
    }

}
