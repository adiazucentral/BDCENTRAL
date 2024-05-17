package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.MicroorganismDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.MicroorganismAntibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MicroorganismService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a microorganismo
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 21/06/2017
 * @see Creación
 */
@Service
public class MicroorganismServiceEnterpriseNT implements MicroorganismService
{

    @Autowired
    private MicroorganismDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Microorganism create(Microorganism create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Microorganism newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, Microorganism.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public int createAll(List<Microorganism> importList) throws Exception
    {
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        List<Microorganism> saved = dao.list();
        time_end = System.currentTimeMillis();
        System.err.println("Consultando en " + (time_end - time_start) + " milliseconds");
        time_start = System.currentTimeMillis();
        List<Microorganism> toInsert = importList.stream()
                .distinct()
                .filter(m -> (m != null && m.getName() != null && !m.getName().trim().isEmpty()))
                .filter(m1 -> !alreadyExists(m1, saved))
                .collect(Collectors.toList());
        time_end = System.currentTimeMillis();
        System.err.println("Filtros en " + (time_end - time_start) + " milliseconds");
        time_start = System.currentTimeMillis();
        dao.createAll(toInsert);
        time_end = System.currentTimeMillis();
        System.err.println("Insercion " + toInsert.size() + " datos en " + (time_end - time_start) + " milliseconds");
        return toInsert.size();

    }

    @Override
    public List<Microorganism> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Microorganism findById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public Microorganism findByName(String name) throws Exception
    {
        return findBy(dao.list(), a -> name.trim().equalsIgnoreCase(a.getName()));
    }

    @Override
    public Microorganism update(Microorganism update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Microorganism old = findById(update.getId());
            Microorganism updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Microorganism.class);
            return updated;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Microorganism> filterByState(boolean state) throws Exception
    {
        List<Microorganism> filter = dao.list().stream()
                .filter(bean -> bean.isState() == state)
                .collect(Collectors.toList());
        return filter;
    }

    @Override
    public Sensitivity getSensitivity(Integer idMicroorganism, Integer idTest) throws Exception
    {
        return dao.getSensitivity(idMicroorganism, idTest);
    }

    @Override
    public List<Microorganism> filterByMicroorganism(Integer id) throws Exception
    {
        return dao.filterByMicroorganism(id);
    }

    @Override
    public Integer sensitivityUpdate(List<Microorganism> update) throws Exception
    {
        trackingService.registerConfigurationTracking(null, update, Microorganism.class);
        return dao.updateBatchSensitivity(update);
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(Microorganism validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();
        List<Microorganism> all = dao.list();
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
            Microorganism found = findBy(all, a -> a.getName().equalsIgnoreCase(validate.getName()));

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
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(MicroorganismAntibiotic validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (validate.getMicroorganism() == null || validate.getMicroorganism().getId() == null)
            {
                errors.add("0|microorganism");
            } else
            {
                if (validate.getAntibiotic() == null || validate.getAntibiotic().getId() == null)
                {
                    errors.add("0|antibiotic");
                } else
                {
                    if (validate.getMethod() == null || validate.getMethod() == null)
                    {
                        errors.add("0|method");
                    } else
                    {
                        if (validate.getInterpretation() == null || validate.getInterpretation() == null)
                        {
                            errors.add("0|interpretation");
                        } else
                        {
                            if (dao.getMicroorganismAntibiotic(validate.getMicroorganism().getId(), validate.getAntibiotic().getId(), validate.getMethod(), validate.getInterpretation()) == null)
                            {
                                errors.add("2|record not exists");//No existe el registro
                            }
                        }
                    }
                }
            }
        }

        if (validate.getMicroorganism() == null || validate.getMicroorganism().getId() == null)
        {
            errors.add("0|microorganism");
        }

        if (validate.getAntibiotic() == null || validate.getAntibiotic().getId() == null)
        {
            errors.add("0|antibiotic");
        }

        if (validate.getMethod() == null || validate.getMethod() == null)
        {
            errors.add("0|method");
        }

        if (validate.getInterpretation() == null || validate.getInterpretation() == null)
        {
            errors.add("0|interpretation");
        }

        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }

        if (errors.isEmpty())
        {
            MicroorganismAntibiotic antibiotic = dao.getMicroorganismAntibiotic(validate.getMicroorganism().getId(), validate.getAntibiotic().getId(), validate.getMethod(), validate.getInterpretation());
            if (antibiotic != null)
            {
                if (!isEdit)
                {
                    errors.add("1|record already exists");
                }
            }
        }

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
    public Microorganism findBy(List<Microorganism> alarmList, Predicate<Microorganism> predicate)
    {
        return alarmList.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

    /**
     * Metodo para verificar si el nombre del microorganismo existe en una lista
     *
     * @param microorganism
     * @param searchList lista de busqueda
     *
     * @return si existe en la lista
     */
    public boolean alreadyExists(Microorganism microorganism, List<Microorganism> searchList)
    {
        return searchList.stream()
                .anyMatch(m -> m.getName().trim().equalsIgnoreCase(microorganism.getName().trim()));
    }

    @Override
    public List<MicroorganismAntibiotic> listMicroorganimAntibiotic() throws Exception
    {
        return dao.listMicroorganismAntibiotic();
    }

    @Override
    public List<MicroorganismAntibiotic> listMicroorganimAntibiotic(int idMicroorganism) throws Exception
    {
        return dao.listMicroorganismAntibiotic().stream()
                .filter(antibiotic -> antibiotic.getMicroorganism().getId().equals(idMicroorganism))
                .collect(Collectors.toList());
    }

    @Override
    public MicroorganismAntibiotic getMicroorganismAntibiotic(int idMicroorganism, int idAntibiotic, short method, short interpretation) throws Exception
    {
        return dao.getMicroorganismAntibiotic(idMicroorganism, idAntibiotic, method, interpretation);
    }

    @Override
    public MicroorganismAntibiotic create(MicroorganismAntibiotic microAntibiotic) throws Exception
    {
        List<String> errors = validateFields(microAntibiotic, false);
        if (errors.isEmpty())
        {
            MicroorganismAntibiotic newBean = dao.create(microAntibiotic);
            trackingService.registerConfigurationTracking(null, newBean, MicroorganismAntibiotic.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public MicroorganismAntibiotic update(MicroorganismAntibiotic microAntibiotic) throws Exception
    {
        List<String> errors = validateFields(microAntibiotic, true);
        if (errors.isEmpty())
        {
            MicroorganismAntibiotic old = getMicroorganismAntibiotic(microAntibiotic.getMicroorganism().getId(), microAntibiotic.getAntibiotic().getId(), microAntibiotic.getMethod(), microAntibiotic.getInterpretation());
            MicroorganismAntibiotic updated = dao.update(microAntibiotic);
            trackingService.registerConfigurationTracking(old, updated, MicroorganismAntibiotic.class);
            return microAntibiotic;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public void delete(int idMicroorganism, int idAntibiotic, short method, short interpretation) throws Exception
    {
        MicroorganismAntibiotic old = getMicroorganismAntibiotic(idMicroorganism, idAntibiotic, method, interpretation);
        dao.delete(idMicroorganism, idAntibiotic, method, interpretation);
        trackingService.registerConfigurationTracking(old, null, MicroorganismAntibiotic.class);
    }

}
