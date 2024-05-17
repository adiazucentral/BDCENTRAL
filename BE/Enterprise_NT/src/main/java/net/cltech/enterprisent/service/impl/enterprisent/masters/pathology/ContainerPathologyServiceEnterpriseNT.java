/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.pathology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.ContainerPathologyDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.ContainerPathology;
import net.cltech.enterprisent.service.interfaces.masters.pathology.ContainerPathologyService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de el maestro de contenedores de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 19/10/2020
 * @see Creaciòn
 */
@Service
public class ContainerPathologyServiceEnterpriseNT implements ContainerPathologyService 
{
    @Autowired
    private ContainerPathologyDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public List<ContainerPathology> list() throws Exception {
        List<ContainerPathology> containers = dao.list();
        containers.forEach( container -> {
            try
            {
                container.setUserCreated(trackingPathologyDao.get(container.getUserCreated().getId()));
                container.setUserUpdated(trackingPathologyDao.get(container.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return containers;
    }
    
    @Override
    public ContainerPathology create(ContainerPathology container) throws Exception {

        List<String> errors = validateFields(false, container);
        if (errors.isEmpty()) {
            ContainerPathology created = dao.create(container);
            trackingService.registerConfigurationTracking(null, created, ContainerPathology.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public ContainerPathology get(Integer id, String name) throws Exception
    {
        ContainerPathology container = dao.get(id, name);
        try
        {
            container.setUserCreated(trackingPathologyDao.get(container.getUserCreated().getId()));
            container.setUserUpdated(trackingPathologyDao.get(container.getUserUpdated().getId()));
        } catch (Exception e) {}
        return container;
    }

    @Override
    public ContainerPathology update(ContainerPathology container) throws Exception 
    {

        List<String> errors = validateFields(true, container);
        if (errors.isEmpty()) 
        {            
            ContainerPathology containerC = dao.get(container.getId(), null);
            ContainerPathology modifited = dao.update(container);
            trackingService.registerConfigurationTracking(containerC, modifited, ContainerPathology.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<ContainerPathology> list(int state) throws Exception
    {
        List<ContainerPathology> containers = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((ContainerPathology) o).isState()== state));
        containers.forEach( container -> {
            try
            {
                container.setUserCreated(trackingPathologyDao.get(container.getUserCreated().getId()));
                container.setUserUpdated(trackingPathologyDao.get(container.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return containers;
    }

    private List<String> validateFields(boolean isEdit, ContainerPathology container) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (container.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(container.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (container.getUserUpdated().getId() == null || container.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (container.getUserCreated().getId() == null || container.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (container.getName() != null && !container.getName().isEmpty())
        {
            ContainerPathology containerC = dao.get(null, container.getName());
            if (containerC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(container.getId(), containerC.getId()))
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
        
        if(container.getStatus() == null) 
        {
           errors.add("0|status");
        }
        
        if(container.getPrint() == null) 
        {
           errors.add("0|print");
        }

        return errors;
    }
}
