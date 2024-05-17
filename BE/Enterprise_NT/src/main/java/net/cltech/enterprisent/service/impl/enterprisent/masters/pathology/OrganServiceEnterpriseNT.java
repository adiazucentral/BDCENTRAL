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
import net.cltech.enterprisent.dao.interfaces.masters.pathology.OrganDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Organ;
import net.cltech.enterprisent.service.interfaces.masters.pathology.OrganService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de el maestro de organos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 20/10/2020
 * @see Creaciòn
 */
@Service
public class OrganServiceEnterpriseNT implements OrganService 
{
    @Autowired
    private OrganDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public Organ create(Organ organ) throws Exception {

        List<String> errors = validateFields(false, organ);
        if (errors.isEmpty()) {
            Organ created = dao.create(organ);
            trackingService.registerConfigurationTracking(null, created, Organ.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Organ> list() throws Exception {
        List<Organ> organs = dao.list();
        organs.forEach( organ -> {
            try
            {
                organ.setUserCreated(trackingPathologyDao.get(organ.getUserCreated().getId()));
                organ.setUserUpdated(trackingPathologyDao.get(organ.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return dao.list();
    }

    @Override
    public Organ get(Integer id, String name, String code) throws Exception
    {
        Organ organ = dao.get(id, name, code);
        try
        {
            organ.setUserCreated(trackingPathologyDao.get(organ.getUserCreated().getId()));
            organ.setUserUpdated(trackingPathologyDao.get(organ.getUserUpdated().getId()));
        } catch (Exception e) {}
        return organ;
    }

    @Override
    public Organ update(Organ organ) throws Exception {

        List<String> errors = validateFields(true, organ);
        if (errors.isEmpty()) 
        {            
            Organ organC = dao.get(organ.getId(), null, null);
            Organ modifited = dao.update(organ);
            trackingService.registerConfigurationTracking(organC, modifited, Organ.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Organ> list(int state) throws Exception
    {
        List<Organ> organs = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Organ) o).isState()== state));
        organs.forEach( organ -> {
            try
            {
                organ.setUserCreated(trackingPathologyDao.get(organ.getUserCreated().getId()));
                organ.setUserUpdated(trackingPathologyDao.get(organ.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return organs;
    }

    private List<String> validateFields(boolean isEdit, Organ organ) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (organ.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(organ.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (organ.getUserUpdated().getId() == null || organ.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (organ.getUserCreated().getId() == null || organ.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (organ.getCode()!= null && !organ.getCode().isEmpty())
        {
            Organ organC = dao.get(null, null, organ.getCode());
            if (organC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(organ.getId(), organC.getId()))
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

        if (organ.getName() != null && !organ.getName().isEmpty())
        {
            Organ organC = dao.get(null, organ.getName(), null);
            if (organC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(organ.getId(), organC.getId()))
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
        
        if(organ.getStatus() == null) 
        {
           errors.add("0|status");
        }
        
        return errors;
    }
}
