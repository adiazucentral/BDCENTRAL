/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.CollectionMethodDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.CollectionMethod;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.CollectionMethodService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Metodo de Recolección
 * para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 18/01/2018
 * @see Creaciòn
 */
@Service
public class CollectionMethodServiceEnterpriseNT implements CollectionMethodService
{

    @Autowired
    private CollectionMethodDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<CollectionMethod> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public CollectionMethod create(CollectionMethod collectionMethod) throws Exception
    {
        List<String> errors = validateFields(false, collectionMethod);
        if (errors.isEmpty())
        {
            CollectionMethod created = dao.create(collectionMethod);
            trackingService.registerConfigurationTracking(null, created, CollectionMethod.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public CollectionMethod get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public CollectionMethod update(CollectionMethod collectionMethod) throws Exception
    {
        List<String> errors = validateFields(true, collectionMethod);
        if (errors.isEmpty())
        {
            CollectionMethod collectionMethodC = dao.get(collectionMethod.getId(), null);
            CollectionMethod modifited = dao.update(collectionMethod);
            trackingService.registerConfigurationTracking(collectionMethodC, modifited, CollectionMethod.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        
    }

    @Override
    public List<CollectionMethod> list(boolean state) throws Exception
    {
        List<CollectionMethod> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((CollectionMethod) o).isState() == state));
        return filter;
    }
    
    private List<String> validateFields(boolean isEdit, CollectionMethod collectionMethod) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (collectionMethod.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(collectionMethod.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (collectionMethod.getName() != null && !collectionMethod.getName().isEmpty())
        {
            CollectionMethod collectionMethodC = dao.get(null, collectionMethod.getName());
            if (collectionMethodC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(collectionMethod.getId(), collectionMethodC.getId()))
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

        if (collectionMethod.getUser().getId() == null || collectionMethod.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

}
