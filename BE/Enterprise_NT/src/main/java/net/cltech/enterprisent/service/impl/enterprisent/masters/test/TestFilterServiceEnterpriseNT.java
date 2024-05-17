package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.test.TestFilterDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.TestFilter;
import net.cltech.enterprisent.service.interfaces.masters.test.TestFilterService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Grupos para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 25/10/2017
 * @see Creaciòn
 */
@Service
public class TestFilterServiceEnterpriseNT implements TestFilterService
{

    @Autowired
    private TestFilterDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<TestFilter> list() throws Exception
    {
        return dao.list();
    }
    
    @Override
    public List<TestFilter> listTestGroup() throws Exception
    {
        return dao.listTestGroup();
    }

    @Override
    public TestFilter findByName(String name) throws Exception
    {
        return list().stream()
                .filter(group -> name.equalsIgnoreCase(group.getName()))
                .findAny()
                .orElse(null);
    }

    @Override
    public TestFilter findById(Integer id) throws Exception
    {
        return list().stream()
                .filter(group -> Objects.equals(id, group.getId()))
                .findAny()
                .map(group -> group.setTests(dao.readTests(group.getId())))
                .orElse(null);
    }

    @Override
    public TestFilter create(TestFilter group) throws Exception
    {
        List<String> errors = validateFields(false, group);
        if (errors.isEmpty())
        {
            TestFilter created = dao.create(group);
            trackingService.registerConfigurationTracking(null, created, TestFilter.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public TestFilter update(TestFilter group) throws Exception
    {
        List<String> errors = validateFields(true, group);
        if (errors.isEmpty())
        {
            TestFilter groupC = findById(group.getId());
            TestFilter modifited = dao.update(group);
            trackingService.registerConfigurationTracking(groupC, modifited, TestFilter.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<TestFilter> list(boolean state) throws Exception
    {
        List<TestFilter> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((TestFilter) o).getState() == state));
        return filter;
    }

    /**
     * Valida campos obligatorios para la homologación
     *
     *
     *
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, TestFilter group) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (group.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (findById(group.getId()) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (group.getName() != null && !group.getName().isEmpty())
        {
            TestFilter groupC = findByName(group.getName().trim());
            if (groupC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(group.getId(), groupC.getId()))
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

        if (group.getTests() == null || group.getTests().isEmpty())
        {
            errors.add("0|tests");
        }

        if (group.getUser().getId() == null || group.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

}
