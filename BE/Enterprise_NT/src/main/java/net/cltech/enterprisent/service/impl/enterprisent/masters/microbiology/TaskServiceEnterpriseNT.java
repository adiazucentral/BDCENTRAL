/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.TaskDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.Task;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.TaskService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Tareas para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 09/06/2017
 * @see Creaciòn
 */
@Service
public class TaskServiceEnterpriseNT implements TaskService
{

    @Autowired
    private TaskDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Task> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Task create(Task task) throws Exception
    {
        List<String> errors = validateFields(false, task);
        if (errors.isEmpty())
        {
            Task created = dao.create(task);
            trackingService.registerConfigurationTracking(null, created, Task.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Task get(Integer id, String description) throws Exception
    {
        return dao.get(id, description);
    }

    @Override
    public Task update(Task task) throws Exception
    {
        List<String> errors = validateFields(true, task);
        if (errors.isEmpty())
        {
            Task taskC = dao.get(task.getId(), null);
            Task modifited = dao.update(task);
            trackingService.registerConfigurationTracking(taskC, modifited, Task.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        dao.delete(id);
    }

    @Override
    public List<Task> list(boolean state) throws Exception
    {
        List<Task> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Task) o).isState() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, Task task) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (task.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(task.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (task.getDescription() != null && !task.getDescription().isEmpty())
        {
            Task taskC = dao.get(null, task.getDescription());
            if (taskC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(task.getId(), taskC.getId()))
                    {
                        errors.add("1|description");
                    }
                } else
                {
                    errors.add("1|description");
                }
            }
        } else
        {
            errors.add("0|description");
        }

        if (task.getUser().getId() == null || task.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

}
