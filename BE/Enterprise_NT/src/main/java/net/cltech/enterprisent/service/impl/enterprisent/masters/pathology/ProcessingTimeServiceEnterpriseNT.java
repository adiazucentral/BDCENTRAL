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
import net.cltech.enterprisent.dao.interfaces.masters.pathology.ProcessingTimeDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.ProcessingTime;
import net.cltech.enterprisent.service.interfaces.masters.pathology.ProcessingTimeService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios del maestro de las horas de procesamiento de las muestras de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 13/07/2021
 * @see Creaciòn
*/

@Service
public class ProcessingTimeServiceEnterpriseNT implements ProcessingTimeService
{
    @Autowired
    private ProcessingTimeDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public List<ProcessingTime> list() throws Exception {
        List<ProcessingTime> list = dao.list();
        list.forEach( processingTime -> {
            try
            {
                processingTime.setUserCreated(trackingPathologyDao.get(processingTime.getUserCreated().getId()));
                processingTime.setUserUpdated(trackingPathologyDao.get(processingTime.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return list;
    }
    
    @Override
    public ProcessingTime create(ProcessingTime processingTime) throws Exception {

        List<String> errors = validateFields(false, processingTime);
        if (errors.isEmpty()) {
            ProcessingTime created = dao.create(processingTime);
            trackingService.registerConfigurationTracking(null, created, ProcessingTime.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public ProcessingTime get(Integer id, String time) throws Exception
    {
        ProcessingTime processingTime = dao.get(id, time);
        try
        {
            processingTime.setUserCreated(trackingPathologyDao.get(processingTime.getUserCreated().getId()));
            processingTime.setUserUpdated(trackingPathologyDao.get(processingTime.getUserUpdated().getId()));
        } catch (Exception e) {}
        return processingTime;
    }

    @Override
    public ProcessingTime update(ProcessingTime processingTime) throws Exception 
    {

        List<String> errors = validateFields(true, processingTime);
        if (errors.isEmpty()) 
        {            
            ProcessingTime processingTimeC = dao.get(processingTime.getId(), null);
            ProcessingTime modifited = dao.update(processingTime);
            trackingService.registerConfigurationTracking(processingTimeC, modifited, ProcessingTime.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<ProcessingTime> list(int state) throws Exception
    {
        List<ProcessingTime> list = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((ProcessingTime) o).isState()== state));
        list.forEach( processingTime -> {
            try
            {
                processingTime.setUserCreated(trackingPathologyDao.get(processingTime.getUserCreated().getId()));
                processingTime.setUserUpdated(trackingPathologyDao.get(processingTime.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return list;
    }

    private List<String> validateFields(boolean isEdit, ProcessingTime processingTime) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (processingTime.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(processingTime.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (processingTime.getUserUpdated().getId() == null || processingTime.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (processingTime.getUserCreated().getId() == null || processingTime.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (processingTime.getTime() != null || !processingTime.getTime().isEmpty())
        {
            ProcessingTime processingTimeC = dao.get(null, processingTime.getTime());
            if (processingTimeC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(processingTime.getId(), processingTimeC.getId()))
                    {
                        errors.add("1|time");
                    }
                } else
                {
                    errors.add("1|time");
                }
            }
        } else
        {
            errors.add("0|time");
        }

        if(processingTime.getStatus() == null) 
        {
           errors.add("0|status");
        }
        return errors;
    }
}
