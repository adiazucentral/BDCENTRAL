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
import net.cltech.enterprisent.dao.interfaces.masters.pathology.FieldDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.service.interfaces.masters.pathology.FieldService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de el maestro de campos para las plantillas de macroscopia
 *
 * @version 1.0.0
 * @author omendez
 * @see 09/06/2021
 * @see Creaciòn
 */
@Service
public class FieldServiceEnterpriseNT implements FieldService
{
    @Autowired
    private FieldDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    
    @Override
    public List<Field> list() throws Exception {
        List<Field> fields = dao.list();
        fields.forEach( field -> {
            try
            {
                field.setUserCreated(trackingPathologyDao.get(field.getUserCreated().getId()));
                field.setUserUpdated(trackingPathologyDao.get(field.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return fields;
    }
    
    @Override
    public Field create(Field field) throws Exception {

        List<String> errors = validateFields(false, field);
        if (errors.isEmpty()) {
            Field created = dao.create(field);
            trackingService.registerConfigurationTracking(null, created, Field.class);
            return created;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Field get(Integer id, String name) throws Exception
    {
        Field field = dao.get(id, name);
        try
        {
            field.setUserCreated(trackingPathologyDao.get(field.getUserCreated().getId()));
            field.setUserUpdated(trackingPathologyDao.get(field.getUserUpdated().getId()));
        } catch (Exception e) {}
        return field;
    }

    @Override
    public Field update(Field field) throws Exception 
    {

        List<String> errors = validateFields(true, field);
        if (errors.isEmpty()) 
        {            
            Field fieldC = dao.get(field.getId(), null);
            Field modifited = dao.update(field);
            trackingService.registerConfigurationTracking(fieldC, modifited, Field.class);
            return modifited;
        } else 
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    @Override
    public List<Field> list(int state) throws Exception
    {
        List<Field> fields = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Field) o).isState()== state));
        fields.forEach( field -> {
            try
            {
                field.setUserCreated(trackingPathologyDao.get(field.getUserCreated().getId()));
                field.setUserUpdated(trackingPathologyDao.get(field.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return fields;
    }

    private List<String> validateFields(boolean isEdit, Field field) throws Exception {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (field.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(field.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
            
            if (field.getUserUpdated().getId() == null || field.getUserUpdated().getId() == 0) {
                errors.add("0|userUpdated");
            }
        } else {
            if (field.getUserCreated().getId() == null || field.getUserCreated().getId() == 0) {
                errors.add("0|userCreated");
            }
        }

        if (field.getName() != null && !field.getName().isEmpty())
        {
            Field fieldC = dao.get(null, field.getName());
            if (fieldC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(field.getId(), fieldC.getId()))
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
        
        if(field.getType()== null) 
        {
           errors.add("0|type");
        }
        
        if(field.getGrid()== null) 
        {
           errors.add("0|grid");
        }
        
        if(field.getRequired()== null) 
        {
           errors.add("0|required");
        }
        
        if(field.getStatus() == null) 
        {
           errors.add("0|status");
        }
        return errors;
    }
}
