/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.billing;

import java.util.List;
import net.cltech.enterprisent.domain.operation.billing.RipsFilter;
import net.cltech.enterprisent.domain.operation.billing.InformationRips;

/**
 * Interfaz de servicios a la informacion rips
 *
 * @version 1.0.0
 * @author omendez
 * @since 21/01/2021
 * @see Creación
 */
public interface InformationRipsService {
    
    /**
    * Lista la informacion rips a partir de los filtros 
    *
    * @param filter filtros de la busqueda
    * @return Lista de informacion rips
    * @throws java.lang.Exception
    */
    public List<InformationRips> information(RipsFilter filter) throws Exception;
    
}
