/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.TissueProcessor;

/**
 * Interfaz de servicios a la informacion del procesador de tejidos de los casetes
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/07/2021
 * @see Creación
 */
public interface TissueProcessorService 
{
    /**
    * Registra los datos del procesador de tejidos de una lista de casetes en la base de datos.
    *
    * @param tissueProcessor Instancia con los datos del procesador.
    * @return Instancia con los datos del procesador.
    * @throws Exception Error en la base de datos.
    */
    public List<TissueProcessor> create(List<TissueProcessor> tissueProcessor) throws Exception;
    
}
