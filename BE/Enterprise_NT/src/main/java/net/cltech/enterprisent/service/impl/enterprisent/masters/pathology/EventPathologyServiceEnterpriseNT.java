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
import net.cltech.enterprisent.dao.interfaces.masters.pathology.EventPathologyDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.EventPathology;
import net.cltech.enterprisent.service.interfaces.masters.pathology.EventPathologyService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios del maestro de eventos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 18/05/2021
 * @see Creaciòn
*/

@Service
public class EventPathologyServiceEnterpriseNT implements EventPathologyService
{
    @Autowired
    private EventPathologyDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public List<EventPathology> list() throws Exception {
        List<EventPathology> events = dao.list();
        events.forEach( event -> {
            try
            {
                event.setUserCreated(trackingPathologyDao.get(event.getUserCreated().getId()));
                event.setUserUpdated(trackingPathologyDao.get(event.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return events;
    }
    
    @Override
    public EventPathology create(EventPathology event) throws Exception {

        List<String> errors = validateFields(false, event);
        if (errors.isEmpty()) {
            EventPathology created = dao.create(event);
            trackingService.registerConfigurationTracking(null, created, EventPathology.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public EventPathology get(Integer id, String code) throws Exception
    {
        EventPathology event = dao.get(id, code);
        try
        {
            event.setUserCreated(trackingPathologyDao.get(event.getUserCreated().getId()));
            event.setUserUpdated(trackingPathologyDao.get(event.getUserUpdated().getId()));
        } catch (Exception e) {}
        return event;
    }

    @Override
    public EventPathology update(EventPathology event) throws Exception 
    {

        List<String> errors = validateFields(true, event);
        if (errors.isEmpty()) 
        {            
            EventPathology eventC = dao.get(event.getId(), null);
            EventPathology modifited = dao.update(event);
            trackingService.registerConfigurationTracking(eventC, modifited, EventPathology.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<EventPathology> list(int state) throws Exception
    {
        List<EventPathology> events = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((EventPathology) o).isState()== state));
        events.forEach( event -> {
            try
            {
                event.setUserCreated(trackingPathologyDao.get(event.getUserCreated().getId()));
                event.setUserUpdated(trackingPathologyDao.get(event.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return events;
    }

    private List<String> validateFields(boolean isEdit, EventPathology event) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (event.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(event.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (event.getUserUpdated().getId() == null || event.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (event.getUserCreated().getId() == null || event.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (event.getCode()!= null && !event.getCode().isEmpty())
        {
            EventPathology eventC = dao.get(null, event.getCode());
            if (eventC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(event.getId(), eventC.getId()))
                    {
                        errors.add("1|code");
                    }
                } else
                {
                    errors.add("1|code");
                }
            }
        } else
        {
            errors.add("0|code");
        }

        if(event.getName().isEmpty()) 
        {
           errors.add("0|name");
        }
        
        if(event.getColour().isEmpty()) 
        {
           errors.add("0|colour");
        }
        
        if(event.getStatus() == null) 
        {
           errors.add("0|status");
        }
        return errors;
    }
}
