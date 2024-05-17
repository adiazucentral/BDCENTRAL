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
import net.cltech.enterprisent.dao.interfaces.masters.pathology.FixativeDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Fixative;
import net.cltech.enterprisent.service.interfaces.masters.pathology.FixativeService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios del maestro de fijadores de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 07/04/2021
 * @see Creaciòn
 */
@Service
public class FixativeServiceEnterpriseNT implements FixativeService
{
    @Autowired
    private FixativeDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public List<Fixative> list() throws Exception {
        List<Fixative> fixatives = dao.list();
        fixatives.forEach( fixative -> {
            try
            {
                fixative.setUserCreated(trackingPathologyDao.get(fixative.getUserCreated().getId()));
                fixative.setUserUpdated(trackingPathologyDao.get(fixative.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return fixatives;
    }
    
    @Override
    public Fixative create(Fixative fixative) throws Exception {

        List<String> errors = validateFields(false, fixative);
        if (errors.isEmpty()) {
            Fixative created = dao.create(fixative);
            trackingService.registerConfigurationTracking(null, created, Fixative.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Fixative get(Integer id, String name, String code) throws Exception
    {
        Fixative fixative = dao.get(id, name, code);
        try
        {
            fixative.setUserCreated(trackingPathologyDao.get(fixative.getUserCreated().getId()));
            fixative.setUserUpdated(trackingPathologyDao.get(fixative.getUserUpdated().getId()));
        } catch (Exception e) {}
        return fixative;
    }

    @Override
    public Fixative update(Fixative fixative) throws Exception 
    {

        List<String> errors = validateFields(true, fixative);
        if (errors.isEmpty()) 
        {            
            Fixative fixativeC = dao.get(fixative.getId(), null, null);
            Fixative modifited = dao.update(fixative);
            trackingService.registerConfigurationTracking(fixativeC, modifited, Fixative.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Fixative> list(int state) throws Exception
    {
        List<Fixative> fixatives = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Fixative) o).isState()== state));
        fixatives.forEach( fixative -> {
            try
            {
                fixative.setUserCreated(trackingPathologyDao.get(fixative.getUserCreated().getId()));
                fixative.setUserUpdated(trackingPathologyDao.get(fixative.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return fixatives;
    }

    private List<String> validateFields(boolean isEdit, Fixative fixative) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (fixative.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(fixative.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (fixative.getUserUpdated().getId() == null || fixative.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (fixative.getUserCreated().getId() == null || fixative.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (fixative.getCode()!= null && !fixative.getCode().isEmpty())
        {
            Fixative fixativeC = dao.get(null, null, fixative.getCode());
            if (fixativeC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(fixative.getId(), fixativeC.getId()))
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

        if (fixative.getName() != null && !fixative.getName().isEmpty())
        {
            Fixative fixativeC = dao.get(null, fixative.getName(), null);
            if (fixativeC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(fixative.getId(), fixativeC.getId()))
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
        
        if(fixative.getStatus() == null) 
        {
           errors.add("0|status");
        }
        return errors;
    }
}
