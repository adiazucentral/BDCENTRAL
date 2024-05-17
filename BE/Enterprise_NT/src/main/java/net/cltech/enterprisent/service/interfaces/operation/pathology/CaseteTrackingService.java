/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import net.cltech.enterprisent.domain.operation.pathology.CaseteTracking;

/**
 * Interface de servicios para la información de la trazabilidad de los casetes
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/07/2021
 * @see Creacion
*/
public interface CaseteTrackingService 
{
    /**
    * Registra un registro de actividad sobre un casete en la base de datos.
    *
    * @param tracking Instancia con los datos de la trazabilidad.
    * @return Instancia con los datos de la trazabilidad.
    * @throws Exception Error en la base de datos.
    */
    public CaseteTracking create(CaseteTracking tracking) throws Exception;
}
