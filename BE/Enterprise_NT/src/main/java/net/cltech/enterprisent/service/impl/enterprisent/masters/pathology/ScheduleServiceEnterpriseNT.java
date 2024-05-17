/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.pathology;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.ScheduleDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Schedule;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.service.interfaces.masters.pathology.ScheduleService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de la configuracion de agenda de patologos 
 *
 * @version 1.0.0
 * @author omendez
 * @see 22/04/2021
 * @see Creaciòn
 */
@Service
public class ScheduleServiceEnterpriseNT implements ScheduleService 
{
    @Autowired
    private ScheduleDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public Schedule create(Schedule schedule) throws Exception {

        List<String> errors = validateFields(false, schedule);
        if (errors.isEmpty()) {
            Schedule created = dao.create(schedule);
            trackingService.registerConfigurationTracking(null, created, Schedule.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public Schedule get(Integer id) throws Exception
    {
        Schedule schedule = dao.get(id);
        try
        {
            schedule.setUserCreated(trackingPathologyDao.get(schedule.getUserCreated().getId()));
            schedule.setUserUpdated(trackingPathologyDao.get(schedule.getUserUpdated().getId()));
        } catch (Exception e) {}
        return schedule;
    }
    
    @Override
    public List<Schedule> getByPathologist(Integer idPathologist) throws Exception {
        List<Schedule> schedules = dao.getByPathologist(idPathologist, null);
        schedules.forEach( schedule -> {
            try
            {
                schedule.setUserCreated(trackingPathologyDao.get(schedule.getUserCreated().getId()));
                schedule.setUserUpdated(trackingPathologyDao.get(schedule.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return schedules;
    }
    
    @Override
    public List<Schedule> getByPathologist(Integer idPathologist, FilterPathology filter) throws Exception {
        List<Schedule> schedules = dao.getByPathologist(idPathologist, filter);
        schedules.forEach( schedule -> {
            try
            {
                schedule.setUserCreated(trackingPathologyDao.get(schedule.getUserCreated().getId()));
                schedule.setUserUpdated(trackingPathologyDao.get(schedule.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return schedules;
    }
    
    @Override
    public Schedule update(Schedule schedule) throws Exception 
    {

        List<String> errors = validateFields(true, schedule);
        if (errors.isEmpty()) 
        {            
            Schedule scheduleC = dao.get(schedule.getId());
            Schedule modifited = dao.update(schedule);
            trackingService.registerConfigurationTracking(scheduleC, modifited, Schedule.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public void delete(int id) throws Exception
    {
        dao.delete(id);
    }
    
    private List<String> validateFields(boolean isEdit, Schedule schedule) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (schedule.getUserUpdated().getId() == null || schedule.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (schedule.getUserCreated().getId() == null || schedule.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }
        
        if(schedule.getPathologist() == null || schedule.getPathologist() == 0) {
            errors.add("0|pathologist");
        }
        
        if(schedule.getInit() == null) {
            errors.add("0|init");
        }
        
        if(schedule.getEnd()== null) {
            errors.add("0|end");
        }
        
        return errors;
    }
    
}
