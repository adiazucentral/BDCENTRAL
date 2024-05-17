package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.SensitivityDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.AntibioticBySensitivity;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import net.cltech.enterprisent.domain.masters.test.Unit;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.SensitivityService;
import net.cltech.enterprisent.service.interfaces.masters.test.UnitService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MicroorganismService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a antibiograma
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 27/06/2017
 * @see Creación
 */
@Service
public class SensitivityServiceEnterpriseNT implements SensitivityService
{

    @Autowired
    private SensitivityDao dao;
    @Autowired
    private UnitService unitService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private MicroorganismService microorganismService;

    @Override
    public Sensitivity create(Sensitivity create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Sensitivity newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Sensitivity.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Sensitivity> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Sensitivity findById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public Sensitivity findByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.trim().equalsIgnoreCase(a.getName()));
    }

    @Override
    public Sensitivity findByCode(String code) throws Exception
    {
        return findBy(dao.list(), a -> code.trim().equalsIgnoreCase(a.getCode()));
    }

    @Override
    public Sensitivity findByAbbr(String abbr) throws Exception
    {
        return findBy(dao.list(), a -> abbr.trim().equalsIgnoreCase(a.getAbbr()));
    }

    @Override
    public Sensitivity update(Sensitivity update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Sensitivity old = findById(update.getId());
            Sensitivity updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Sensitivity.class);
            return updated;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Sensitivity> filterByState(boolean state) throws Exception
    {
        List<Sensitivity> filter = dao.list().stream()
                .filter(bean -> bean.isState() == state)
                .collect(Collectors.toList());
        return filter;
    }

    @Override
    public int assignAntibiotics(List<AntibioticBySensitivity> update) throws Exception
    {
        List<String> errors = validateAntibiotics(update);
        if (errors.isEmpty())
        {
            dao.deleteAntibiotics(update.get(0).getId());
            trackingService.registerConfigurationTracking(null, update, AntibioticBySensitivity.class);
            return dao.insertAntibiotics(update);
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public int deleteAntibiotics(Integer id) throws Exception
    {
        return dao.deleteAntibiotics(id);
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(Sensitivity validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();
        List<Sensitivity> all = dao.list();
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
        if (validate.getCode() == null || validate.getCode().trim().isEmpty())
        {
            errors.add("0|code");
        } else
        {
            Sensitivity found = findBy(all, a -> a.getCode().equalsIgnoreCase(validate.getCode()));

            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|code");//duplicado
                }
            }
        }
        if (validate.getAbbr() == null || validate.getAbbr().trim().isEmpty())
        {
            errors.add("0|abbr");
        } else
        {
            Sensitivity found = findBy(all, a -> a.getAbbr().equalsIgnoreCase(validate.getAbbr()));

            if (found != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(found.getId())))
                {
                    errors.add("1|abbr");//duplicado
                }
            }
        }
        if (validate.getName() == null || validate.getName().trim().isEmpty())
        {
            errors.add("0|name");
        } else
        {
            Sensitivity found = findBy(all, a -> a.getName().equalsIgnoreCase(validate.getName()));

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

    /**
     * Validacion de los campos relacion antibiotico
     *
     * @param antibiotics Lista de antibioticos
     *
     * @return Lista de eroores encontrados
     * @throws Exception
     */
    public List<String> validateAntibiotics(List<AntibioticBySensitivity> antibiotics) throws Exception
    {
        List<String> errors = new ArrayList<>();
        final List<Unit> units = unitService.filterByState(true);

        if (antibiotics.isEmpty())
        {
            errors.add("0|antibiotics");
            return errors;
        }

        errors.addAll(
                antibiotics.stream()
                        .filter(a -> a.getAntibiotic().getId() == null)
                        .map(a2 -> "0|antibioticId")
                        .collect(Collectors.toList())
        );

        errors.addAll(
                antibiotics.stream()
                        .filter(a -> a.getUnit() != null)
                        .filter(a1 -> !units.contains(new Unit(a1.getUnit())))
                        .map(a2 -> "3|unit|" + a2.getAntibiotic().getId())
                        .collect(Collectors.toList())
        );
        errors.addAll(
                antibiotics.stream()
                        .filter(a -> a.getLine() != 0)
                        .filter(a2 -> a2.getLine() < 1 || a2.getLine() > 3)
                        .map(map -> "3|line|" + map.getAntibiotic().getId())
                        .collect(Collectors.toList())
        );
        return errors;

    }

    /**
     * Busqueda por una expresion
     *
     * @param alarmList listado en el que se realiza la busqueda
     * @param predicate criterio de busqueda
     *
     * @return Objeto encontrado, null si no se encuentra
     */
    public Sensitivity findBy(List<Sensitivity> alarmList, Predicate<Sensitivity> predicate)
    {
        return alarmList.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }
    
    /**
     * Obtiene los antibiogramas relacionados a una orden y a un examen
     *
     * @param idOrder
     * @param idTest
     * @return numero de registros eliminados
     * @throws Exception
     */
    @Override
    public List<Sensitivity> getAntibiogramByOrderIdByTestId(long idOrder, int idTest) throws Exception
    {
        return dao.getAntibiogramByOrderIdByTestId(idOrder, idTest);
    }
    
    @Override
    public int generalSensitivity(Sensitivity sensitivity) throws Exception
    {
        List<Microorganism> microorganisms = microorganismService.list();
        return dao.generalSensitivity(microorganisms, sensitivity.getId());
    }
}
