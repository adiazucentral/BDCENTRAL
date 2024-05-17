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
import net.cltech.enterprisent.dao.interfaces.masters.pathology.ColorationDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Coloration;
import net.cltech.enterprisent.service.interfaces.masters.pathology.ColorationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de el maestro de coloraciones de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 07/04/2021
 * @see Creaciòn
 */
@Service
public class ColorationServiceEnterpriseNT implements ColorationService
{
    @Autowired
    private ColorationDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public List<Coloration> list() throws Exception {
        List<Coloration> colorations = dao.list();
        colorations.forEach( coloration -> {
            try
            {
                coloration.setUserCreated(trackingPathologyDao.get(coloration.getUserCreated().getId()));
                coloration.setUserUpdated(trackingPathologyDao.get(coloration.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return colorations;
    }
    
    @Override
    public Coloration create(Coloration coloration) throws Exception {

        List<String> errors = validateFields(false, coloration);
        if (errors.isEmpty()) {
            Coloration created = dao.create(coloration);
            trackingService.registerConfigurationTracking(null, created, Coloration.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Coloration get(Integer id, String name, String code) throws Exception
    {
        Coloration coloration = dao.get(id, name, code);
        try
        {
            coloration.setUserCreated(trackingPathologyDao.get(coloration.getUserCreated().getId()));
            coloration.setUserUpdated(trackingPathologyDao.get(coloration.getUserUpdated().getId()));
        } catch (Exception e) {}
        return coloration;
    }

    @Override
    public Coloration update(Coloration coloration) throws Exception 
    {

        List<String> errors = validateFields(true, coloration);
        if (errors.isEmpty()) 
        {            
            Coloration colorationC = dao.get(coloration.getId(), null, null);
            Coloration modifited = dao.update(coloration);
            trackingService.registerConfigurationTracking(colorationC, modifited, Coloration.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Coloration> list(int state) throws Exception
    {
        List<Coloration> colorations = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Coloration) o).isState()== state));
        colorations.forEach( coloration -> {
            try
            {
                coloration.setUserCreated(trackingPathologyDao.get(coloration.getUserCreated().getId()));
                coloration.setUserUpdated(trackingPathologyDao.get(coloration.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return colorations;
    }

    private List<String> validateFields(boolean isEdit, Coloration coloration) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (coloration.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(coloration.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (coloration.getUserUpdated().getId() == null || coloration.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (coloration.getUserCreated().getId() == null || coloration.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (coloration.getCode()!= null && !coloration.getCode().isEmpty())
        {
            Coloration colorationC = dao.get(null, null, coloration.getCode());
            if (colorationC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(coloration.getId(), colorationC.getId()))
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

        if (coloration.getName() != null && !coloration.getName().isEmpty())
        {
            Coloration colorationC = dao.get(null, coloration.getName(), null);
            if (colorationC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(coloration.getId(), colorationC.getId()))
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
        
        if(coloration.getStatus() == null) 
        {
           errors.add("0|status");
        }
        return errors;
    }
}
