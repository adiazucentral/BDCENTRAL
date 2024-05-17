/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.CaseteTrackingDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.operation.pathology.CaseteTracking;
import net.cltech.enterprisent.service.interfaces.operation.pathology.CaseteTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion de la trazabilidad de los casetes
 *
 * @version 1.0.0
 * @author omendez
 * @see 26/07/2021
 * @see Creaciòn
 */
@Service
public class CaseteTrackingServiceEnterpriseNT implements CaseteTrackingService
{
    @Autowired
    private CaseteTrackingDao dao;
    
    @Override
    public CaseteTracking create(CaseteTracking tracking) throws Exception {

        List<String> errors = validateFields(tracking);
        if (errors.isEmpty()) {
            return dao.create(tracking);
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
   
    private List<String> validateFields(CaseteTracking tracking) throws Exception 
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        
        List<String> errors = new ArrayList<>();

        if(tracking.getCasete() == null || tracking.getCasete() == 0) {
           errors.add("0|casete");
        }

        if(tracking.getStatus() == null || tracking.getStatus() == 0) {
            errors.add("0|status");
        }

        if(tracking.getCauser().getId() == null || tracking.getCauser().getId() == 0) {
            errors.add("0|causer");
        }
        return errors;
    }
}
