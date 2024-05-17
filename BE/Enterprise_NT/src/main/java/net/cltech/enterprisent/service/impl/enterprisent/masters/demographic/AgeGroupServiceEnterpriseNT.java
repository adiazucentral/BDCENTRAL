package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.AgeGroupDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.AgeGroup;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AgeGroupService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.enums.ListEnum;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a los grupos etarios de la orden
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 31/01/2018
 * @see Creación
 */
@Service
public class AgeGroupServiceEnterpriseNT implements AgeGroupService
{

    @Autowired
    private AgeGroupDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public AgeGroup create(AgeGroup create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            AgeGroup newAgeGroup = dao.create(create);
            trackingService.registerConfigurationTracking(null, newAgeGroup, AgeGroup.class);
            return newAgeGroup;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<AgeGroup> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public AgeGroup filterById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public AgeGroup filterByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.trim().equalsIgnoreCase(a.getName()));
    }

    @Override
    public AgeGroup filterByCode(String code) throws Exception
    {
        return findBy(dao.list(), a -> code.trim().equalsIgnoreCase(a.getCode()));
    }

    @Override
    public AgeGroup update(AgeGroup update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            AgeGroup oldAgeGroup = filterById(update.getId());
            AgeGroup updated = dao.update(update);
            trackingService.registerConfigurationTracking(oldAgeGroup, update, AgeGroup.class);
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
    private List<String> validateFields(AgeGroup validate, boolean isEdit) throws Exception
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
            AgeGroup found = filterByName(validate.getName());
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
            AgeGroup found = filterByCode(validate.getCode());
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

        if (validate.getGender() == null || !ListEnum.Gender.BOTH.isValid(validate.getGender().getId()))
        {
            errors.add("0|gender");
        }
        if (validate.getUnitAge() == null || (validate.getUnitAge() != 1 && validate.getUnitAge() != 2 && validate.getUnitAge() != 3))
        {
            errors.add("0|unitAge");
        }

        return errors;

    }

    @Override
    public List<AgeGroup> filterByState(boolean state) throws Exception
    {
        List<AgeGroup> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((AgeGroup) o).isState() == state));
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
    public AgeGroup findBy(List<AgeGroup> alarmList, Predicate<AgeGroup> predicate)
    {
        return alarmList.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

}
