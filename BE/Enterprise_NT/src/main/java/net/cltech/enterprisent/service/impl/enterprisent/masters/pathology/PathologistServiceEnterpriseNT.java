/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.pathology;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.PathologistDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.OrganDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Pathologist;
import net.cltech.enterprisent.domain.masters.pathology.Organ;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.service.interfaces.masters.pathology.ScheduleService;
import net.cltech.enterprisent.service.interfaces.masters.pathology.PathologistService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de los patologos
 *
 * @version 1.0.0
 * @author omendez
 * @see 16/04/2021
 * @see Creaciòn
 */
@Service
public class PathologistServiceEnterpriseNT implements PathologistService
{
 
    @Autowired
    private PathologistDao dao;
    @Autowired
    private OrganDao organDao;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;   
    
    @Override
    public List<Pathologist> list() throws Exception 
    {
        List<User> users = dao.listPathologists();
        List<Pathologist> list = new ArrayList<>();
        Pathologist pathologist;
        for(int i = 0 ; i < users.size() ; i++ ) {
            pathologist = new Pathologist();
            pathologist.setPathologist( users.get(i) );
            list.add(pathologist);
        }
        return list;
    }
    
    @Override
    public List<Pathologist> list(FilterPathology filter) throws Exception 
    {
        List<User> users = dao.listPathologists();
        List<Pathologist> list = new ArrayList<>();
        Pathologist pathologist;
        for(int i = 0 ; i < users.size() ; i++ ) {
            pathologist = new Pathologist();
            pathologist.setPathologist( users.get(i) );
            pathologist.setOrgans( organsByPathologist( users.get(i).getId() ) );
            pathologist.setSchedule(scheduleService.getByPathologist( users.get(i).getId() , filter));
            pathologist.setQuantity(dao.getAssignedCases(users.get(i).getId()));
            list.add(pathologist);
        }
        return list;
    }
    
    @Override
    public Pathologist get(Integer id) throws Exception
    {
        List<User> users = dao.listPathologists();
        
        User userPathologist = users.stream().filter( user -> user.getId().equals(id)).findAny().orElse(null);
        
        if(userPathologist != null) {
            Pathologist pathologist = new Pathologist();
            pathologist.setPathologist(userPathologist);
            pathologist.setOrgans(organs(id));
            return pathologist;
        }
        return null; 
    }
    
    @Override
    public int assignOrgans(Pathologist pathologist) throws Exception
    {
        List<String> errors = validateFields(pathologist);
        if (errors.isEmpty())
        {
            Pathologist before = new Pathologist(pathologist.getId());
            
            before.setOrgans(organs(pathologist.getPathologist().getId()).stream()
                    .filter(selected -> selected.isSelected())
                    .collect(Collectors.toList()));

            trackingService.registerConfigurationTracking(before, pathologist, Pathologist.class);
            dao.deleteOrgans(pathologist.getPathologist().getId());
            return dao.insertOrgans(pathologist);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Organ> organs(int pathologist) throws Exception
    {
        List<Organ> organs = organDao.list();
        List<Organ> organsByPathologist = dao.listOrgansByPathologists(pathologist);
        
        organs.forEach( organ -> {
            try
            {
                Organ found = organsByPathologist.stream().filter( org -> org.getId().equals(organ.getId())).findFirst().orElse(null);
                if(found != null) {
                    organ.setSelected(true);
                    organ.setCreatedAt(found.getCreatedAt());
                    organ.setUserCreated(trackingPathologyDao.get(found.getUserCreated().getId()));
                }
            } catch (Exception e) {}
        });
        
        return organs;
    }
    
    
    public List<Organ> organsByPathologist(int pathologist) throws Exception
    {
        List<Organ> organs = organDao.list();
        List<Organ> organsByPathologist = dao.listOrgansByPathologists(pathologist);
   
        return organs.stream().filter( organ -> 
                organsByPathologist.stream().filter( o -> o.getId().equals(organ.getId())).findFirst().isPresent()
        ).collect(Collectors.toList());
    }
    
    /**
     * Validación de campos para la asignación de organos
     *
     * @param pathologist bean con informacion de la relación
     *
     * @return lista con errores
     * @throws Exception
    */
    private List<String> validateFields(Pathologist pathologist) throws Exception
    {
        List<String> errors = new ArrayList<>();
        
        if (pathologist.getPathologist().getId() == 0 || pathologist.getPathologist().getId() == null)
        {
            errors.add("0|id");
        }
        return errors;
    }
}
