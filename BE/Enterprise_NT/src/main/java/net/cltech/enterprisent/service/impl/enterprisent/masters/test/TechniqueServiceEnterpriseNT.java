/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.test.TechniqueDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Technique;
import net.cltech.enterprisent.service.interfaces.masters.test.TechniqueService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro técnica para Enterprise NT
 * 
 * @version 1.0.0
 * @author MPerdomo
 * @since 18/04/2017
 * @see Creacion
 */
@Service
public class TechniqueServiceEnterpriseNT implements TechniqueService
{
    @Autowired
    private TechniqueDao techniqueDao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Technique create(Technique technique) throws Exception
    {
        List<String> errors = validateFields(technique, false);
        if (errors.isEmpty())
        {
            Technique newTechnique = techniqueDao.create(technique);
            trackingService.registerConfigurationTracking(null, newTechnique, Technique.class);
            return newTechnique;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Technique> list() throws Exception
    {
        return techniqueDao.list();
    }

    @Override
    public Technique findById(Integer id) throws Exception
    {
        return techniqueDao.findById(id);
    }
    
    @Override
    public Technique findByCode(String code) throws Exception
    {
        return techniqueDao.findByCode(code);
    }

    @Override
    public Technique findByName(String name) throws Exception
    {
        return techniqueDao.findByName(name);
    }

    @Override
    public Technique update(Technique technique) throws Exception
    {
        List<String> errors = validateFields(technique, true);
        if (errors.isEmpty())
        {
            Technique oldTechnique = techniqueDao.findById(technique.getId());
            Technique modifiedTechnique = techniqueDao.update(technique);
            trackingService.registerConfigurationTracking(oldTechnique, technique, Technique.class);
            return modifiedTechnique;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        techniqueDao.delete(id);
    }
    
    @Override
    public List<Technique> list(boolean state) throws Exception
    {
        List<Technique> filter = new ArrayList<>(CollectionUtils.filter(techniqueDao.list(), (Object o) -> ((Technique) o).isState() == state));
        return filter;
    }
    
    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param unit entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(Technique technique, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (isEdit)
        {
            if (technique.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (techniqueDao.findById(technique.getId()) == null)//Si existe
                {
                    errors.add("2|id");
                }
            }
        }
        if (technique.getCode() == null)
        {
            errors.add("0|code");
        } else
        {
            Technique techniqueFromDB = techniqueDao.findByCode(technique.getCode());
            if (techniqueFromDB != null)
            {
                if (!isEdit || (isEdit && !technique.getId().equals(techniqueFromDB.getId())))
                {
                    errors.add("1|code");
                }
            }
        }
        if (technique.getName() == null)
        {
            errors.add("0|name");
        } else
        {
            Technique unitFromDB = techniqueDao.findByName(technique.getName());
            if (unitFromDB != null)
            {
                if (!isEdit || (isEdit && !technique.getId().equals(unitFromDB.getId())))
                {
                    errors.add("1|name");
                }
            }
        }
        if (technique.getUser().getId() == null)
        {
            errors.add("0|userId");
        }
        return errors;

    }

}
