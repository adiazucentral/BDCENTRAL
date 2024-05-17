/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.TissueProcessorDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.operation.pathology.TissueProcessor;
import net.cltech.enterprisent.service.interfaces.operation.pathology.TissueProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del procesador de los casetes de las muestras de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 19/07/2021
 * @see Creaciòn
 */
@Service
public class TissueProcessorServiceEnterpriseNT implements TissueProcessorService
{
    @Autowired
    private TissueProcessorDao dao;
    
    @Override
    public List<TissueProcessor> create(List<TissueProcessor> tissueProcessor) throws Exception 
    {

        List<String> errors = validateFields(tissueProcessor);
        if (errors.isEmpty()) {
            dao.create(tissueProcessor);   
            return tissueProcessor;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }
    
    private List<String> validateFields(List<TissueProcessor> list) throws Exception 
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        
        if (list.size() > 0)
        {
            int row = 0;
            for (TissueProcessor tissueProcessor : list)
            {
                if(tissueProcessor.getCasete() == null || tissueProcessor.getCasete() == 0) {
                    errors.add("0|casete|" + row);
                }
                
                if(tissueProcessor.getHours() == null || tissueProcessor.getHours() == 0) {
                    errors.add("0|hours|" + row);
                }
                
                if(tissueProcessor.getTime().getId() == null || tissueProcessor.getTime().getId() == 0) {
                    errors.add("0|time|" + row);
                }
                
                row++;
            }
        } else
        {
            errors.add("0|casetes");
        }
        return errors;
    }
    
}
