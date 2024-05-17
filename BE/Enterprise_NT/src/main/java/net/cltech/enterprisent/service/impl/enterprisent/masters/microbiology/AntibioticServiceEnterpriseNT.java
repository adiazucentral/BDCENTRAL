package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.AntibioticDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.Antibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.AntibioticBySensitivity;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.AntibioticService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a alarma
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 07/06/2017
 * @see Creación
 */
@Service
public class AntibioticServiceEnterpriseNT implements AntibioticService
{

    @Autowired
    private AntibioticDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Antibiotic create(Antibiotic create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Antibiotic newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Antibiotic.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Antibiotic> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Antibiotic findById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public Antibiotic findByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.equalsIgnoreCase(a.getName()));
    }

    @Override
    public Antibiotic update(Antibiotic update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Antibiotic old = findById(update.getId());
            Antibiotic updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Antibiotic.class);
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
    private List<String> validateFields(Antibiotic validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();
        List<Antibiotic> all = dao.list();
        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (findById(validate.getId()) == null)
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
            Antibiotic found = findBy(all, a -> a.getName().equalsIgnoreCase(validate.getName()));

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
    public List<Antibiotic> filterByState(boolean state) throws Exception
    {
        List<Antibiotic> filter = dao.list().stream()
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
    public Antibiotic findBy(List<Antibiotic> alarmList, Predicate<Antibiotic> predicate)
    {
        return alarmList.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

    @Override
    public List<AntibioticBySensitivity> filterBySensitivity(int id) throws Exception
    {
        return dao.filterBySensitivity(id);
    }

}
