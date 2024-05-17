/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.pathology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Area;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.cltech.enterprisent.service.interfaces.masters.pathology.AreasService;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.AreasDao;
import net.sf.cglib.core.CollectionUtils;

/**
 * Implementa los servicios de el maestro de areas de patología
 *
 * @version 1.0.0
 * @author etoro
 * @see 08/10/2020
 * @see Creaciòn
 */
@Service
public class AreasServiceEnterpriseNT implements AreasService {

    @Autowired
    private AreasDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public List<Area> list() throws Exception {
        List<Area> areas = dao.list();
        areas.forEach( area -> {
            try
            {
                area.setUserCreated(trackingPathologyDao.get(area.getUserCreated().getId()));
                area.setUserUpdated(trackingPathologyDao.get(area.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return areas;
    }
    
    @Override
    public Area create(Area area) throws Exception {

        List<String> errors = validateFields(false, area);
        if (errors.isEmpty()) {
            Area created = dao.create(area);
            trackingService.registerConfigurationTracking(null, created, Area.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Area get(Integer id, String name, String code) throws Exception
    {
        Area area = dao.get(id, name, code);
        try
        {
            area.setUserCreated(trackingPathologyDao.get(area.getUserCreated().getId()));
            area.setUserUpdated(trackingPathologyDao.get(area.getUserUpdated().getId()));
        } catch (Exception e) {}
        return area;
    }

    @Override
    public Area update(Area area) throws Exception 
    {

        List<String> errors = validateFields(true, area);
        if (errors.isEmpty()) 
        {            
            Area areaC = dao.get(area.getId(), null, null);
            Area modifited = dao.update(area);
            trackingService.registerConfigurationTracking(areaC, modifited, Area.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Area> list(int state) throws Exception
    {
        List<Area> areas = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Area) o).isState()== state));
        areas.forEach( area -> {
            try
            {
                area.setUserCreated(trackingPathologyDao.get(area.getUserCreated().getId()));
                area.setUserUpdated(trackingPathologyDao.get(area.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return areas;
    }

    private List<String> validateFields(boolean isEdit, Area area) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (area.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(area.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (area.getUserUpdated().getId() == null || area.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (area.getUserCreated().getId() == null || area.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (area.getCode()!= null && !area.getCode().isEmpty())
        {
            Area areaC = dao.get(null, null, area.getCode());
            if (areaC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(area.getId(), areaC.getId()))
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

        if (area.getName() != null && !area.getName().isEmpty())
        {
            Area areaC = dao.get(null, area.getName(), null);
            if (areaC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(area.getId(), areaC.getId()))
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
        
        if(area.getStatus() == null) 
        {
           errors.add("0|status");
        }


        return errors;
    }

}
