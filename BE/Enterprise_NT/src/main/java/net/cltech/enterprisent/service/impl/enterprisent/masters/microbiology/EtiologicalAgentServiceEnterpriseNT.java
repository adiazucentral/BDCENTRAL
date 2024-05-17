/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.EtiologicalAgentDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.EtiologicalAgent;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.EtiologicalAgentService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso al agente etiologico
 *
 * @author omendez
 * @version 1.0.0
 * @since 02/06/2022
 * @see Creación
 */
@Service
public class EtiologicalAgentServiceEnterpriseNT implements EtiologicalAgentService {
    
    @Autowired
    private EtiologicalAgentDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public EtiologicalAgent create(EtiologicalAgent create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            EtiologicalAgent newBean = dao.create(create);
            trackingService.registerConfigurationTracking(null, newBean, EtiologicalAgent.class);
            return newBean;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<EtiologicalAgent> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public EtiologicalAgent findById(Integer id) throws Exception
    {
        return findBy(dao.list(), a -> id.equals(a.getId()));
    }

    @Override
    public EtiologicalAgent update(EtiologicalAgent update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            EtiologicalAgent old = findById(update.getId());
            EtiologicalAgent updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, EtiologicalAgent.class);
            return updated;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(EtiologicalAgent validate, boolean isEdit) throws Exception
    {

        List<String> errors = new ArrayList<>();
        
        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (findById(validate.getId()) == null)
                {
                    errors.add("2|id");//No existe id
                }
            }
        }
        
        if (validate.getSearchBy() == null || validate.getSearchBy() == 0 )
        {
            errors.add("0|searchby");
        }
        
        if (validate.getMicroorganism() == null || validate.getMicroorganism().trim().isEmpty())
        {
            errors.add("0|microorganism");
        }
        
        if (validate.getCode() == null || validate.getCode().trim().isEmpty())
        {
            errors.add("0|code");
        }
        
        if (validate.getClasification() == null || validate.getClasification() == 0 )
        {
            errors.add("0|clasification");
        }
        
        if (validate.getUser().getId() == null)
        {
            errors.add("0|userId");
        }

        return errors;

    }
    /**
     * Busqueda por una expresion
     *
     * @param list listado en el que se realiza la busqueda
     * @param predicate criterio de busqueda
     *
     * @return Objeto encontrado, null si no se encuentra
     */
    public EtiologicalAgent findBy(List<EtiologicalAgent> list, Predicate<EtiologicalAgent> predicate)
    {
        return list.stream().filter(predicate)
                .findAny()
                .orElse(null);
    }

}
