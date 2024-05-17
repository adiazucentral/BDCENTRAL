/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicTestDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.DemographicTest;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicTestService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Examenes por laboratorio
 *
 * @version 1.0.0
 * @author omendez
 * @see 01/02/2022
 * @see Creaciòn
 */
@Service
public class DemographicTestServiceEnterpriseNT implements DemographicTestService {
    
    @Autowired
    private DemographicTestDao dao;
    @Autowired
    private TrackingService trackingService;
    
    @Override
    public List<DemographicTest> list() throws Exception
    {
        return dao.list();
    }
    
    @Override
    public DemographicTest create(DemographicTest demographic) throws Exception
    {
        List<String> errors = validateFields(demographic, false);
        if (errors.isEmpty())
        {
            DemographicTest created = dao.create(demographic);
            trackingService.registerConfigurationTracking(null, created, DemographicTest.class);
            return created;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }
    
    @Override
    public DemographicTest findById(Integer id) throws Exception
    {
        try
        {
            DemographicTest found = dao.list().stream()
                .filter(demo -> demo.getId().equals(id))
                .findFirst()
                .orElse(null);
            if (found != null)
            {
                found.setTests(dao.listTests(id));
            }
            return found;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public DemographicTest update(DemographicTest demographics) throws Exception
    {
        List<String> errors = validateFields(demographics, true);
        if (errors.isEmpty())
        {
            DemographicTest old = findById(demographics.getId());
            DemographicTest updated = dao.update(demographics);
            trackingService.registerConfigurationTracking(old, demographics, DemographicTest.class);
            return updated;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
     /**
     * Valida los campos enviados del maestro examenes por demograficos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(DemographicTest validate, boolean isEdit) throws Exception
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
        
        if(validate.getUser().getId() == null || validate.getUser().getId().equals(0)) {
            errors.add("0|user");
        }
        
        if(validate.getIdDemographic1() == null || validate.getIdDemographic1().equals(0)) {
            errors.add("0|demographic1");
        }
        
        if(validate.getValueDemographic1()== null || validate.getValueDemographic1().equals(0)) {
            errors.add("0|valueDemographic1");
        }

        if(validate.getTests().isEmpty()) {
            errors.add("0|tests");
        }
        
        return errors;
    }
    
}
