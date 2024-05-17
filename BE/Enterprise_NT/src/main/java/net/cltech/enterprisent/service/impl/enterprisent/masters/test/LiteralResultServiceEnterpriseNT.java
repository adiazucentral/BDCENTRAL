package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.test.LiteralResultDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.LiteralByTest;
import net.cltech.enterprisent.domain.masters.test.LiteralResult;
import net.cltech.enterprisent.service.interfaces.masters.test.LiteralResultService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a resultado litereal
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 22/05/2017
 * @see Creación
 */
@Service
public class LiteralResultServiceEnterpriseNT implements LiteralResultService
{

    @Autowired
    private LiteralResultDao dao;

    @Autowired
    private TrackingService trackingService;

    @Override
    public LiteralResult create(LiteralResult create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            LiteralResult newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, LiteralResult.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<LiteralResult> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public LiteralResult filterById(Integer id) throws Exception
    {
        return dao.filterById(id);
    }

    @Override
    public LiteralResult filterByName(String name) throws Exception
    {
        return dao.filterByName(name);
    }

    @Override
    public LiteralResult update(LiteralResult update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            LiteralResult old = dao.filterById(update.getId());
            LiteralResult updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, LiteralResult.class);
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
    private List<String> validateFields(LiteralResult validate, boolean isEdit) throws Exception
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
            LiteralResult found = dao.filterByName(validate.getName());

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
    public List<LiteralResult> filterByState(boolean state) throws Exception
    {
        List<LiteralResult> filter = dao.list().stream()
                .filter(bean -> bean.isState() == state)
                .collect(Collectors.toList());
        return filter;
    }

    @Override
    public List<LiteralByTest> filterByTest(Integer id) throws Exception
    {
        return dao.filterByTest(id);
    }

    @Override
    public int assignResults(List<LiteralByTest> results) throws Exception
    {
        List<LiteralByTest> listPrevious = filterByTest(results.get(0).getId());
        dao.deleteResultAssign(results.get(0).getId());
        Integer insert = dao.insertLiteralResults(results);
        trackingService.registerConfigurationTracking(listPrevious, results, LiteralByTest.class);
        return insert;
    }

    @Override
    public List<LiteralResult> listWithTestId() throws Exception
    {
        return dao.listWithTestId();
    }

    @Override
    public List<LiteralResult> listByOrder(long order) throws Exception
    {
        return dao.listByOrder(order);
    }
}
