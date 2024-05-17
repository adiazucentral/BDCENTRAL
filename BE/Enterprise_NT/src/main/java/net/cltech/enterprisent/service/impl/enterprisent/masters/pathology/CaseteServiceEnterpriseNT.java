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
import net.cltech.enterprisent.dao.interfaces.masters.pathology.CaseteDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Casete;
import net.cltech.enterprisent.service.interfaces.masters.pathology.CaseteService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios del maestro de casetes de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 07/04/2021
 * @see Creaciòn
*/

@Service
public class CaseteServiceEnterpriseNT implements CaseteService
{
    @Autowired
    private CaseteDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public List<Casete> list() throws Exception {
        List<Casete> casetes = dao.list();
        casetes.forEach( casete -> {
            try
            {
                casete.setUserCreated(trackingPathologyDao.get(casete.getUserCreated().getId()));
                casete.setUserUpdated(trackingPathologyDao.get(casete.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return casetes;
    }
    
    @Override
    public Casete create(Casete casete) throws Exception {

        List<String> errors = validateFields(false, casete);
        if (errors.isEmpty()) {
            Casete created = dao.create(casete);
            trackingService.registerConfigurationTracking(null, created, Casete.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Casete get(Integer id, String name, String code) throws Exception
    {
        Casete casete = dao.get(id, name, code);
        try
        {
            casete.setUserCreated(trackingPathologyDao.get(casete.getUserCreated().getId()));
            casete.setUserUpdated(trackingPathologyDao.get(casete.getUserUpdated().getId()));
        } catch (Exception e) {}
        return casete;
    }

    @Override
    public Casete update(Casete casete) throws Exception 
    {

        List<String> errors = validateFields(true, casete);
        if (errors.isEmpty()) 
        {            
            Casete caseteC = dao.get(casete.getId(), null, null);
            Casete modifited = dao.update(casete);
            trackingService.registerConfigurationTracking(caseteC, modifited, Casete.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Casete> list(int state) throws Exception
    {
        List<Casete> casetes = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Casete) o).isState()== state));
        casetes.forEach( casete -> {
            try
            {
                casete.setUserCreated(trackingPathologyDao.get(casete.getUserCreated().getId()));
                casete.setUserUpdated(trackingPathologyDao.get(casete.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return casetes;
    }

    private List<String> validateFields(boolean isEdit, Casete casete) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (casete.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(casete.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (casete.getUserUpdated().getId() == null || casete.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (casete.getUserCreated().getId() == null || casete.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (casete.getCode()!= null && !casete.getCode().isEmpty())
        {
            Casete caseteC = dao.get(null, null, casete.getCode());
            if (caseteC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(casete.getId(), caseteC.getId()))
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

        if (casete.getName() != null && !casete.getName().isEmpty())
        {
            Casete caseteC = dao.get(null, casete.getName(), null);
            if (caseteC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(casete.getId(), caseteC.getId()))
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
        
        if(casete.getStatus() == null) 
        {
           errors.add("0|status");
        }
        return errors;
    }
}
