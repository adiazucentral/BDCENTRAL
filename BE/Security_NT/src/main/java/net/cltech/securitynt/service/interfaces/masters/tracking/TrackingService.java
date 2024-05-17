/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.service.interfaces.masters.tracking;

import net.cltech.securitynt.domain.common.AuthorizedUser;

/**
 * Interfaz de servicios a la informacion de la auditoria
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creacion
 *
 * @version 1.0.0
 * @author enavas
 * @since 28/04/2017
 * @see Creacion de los Servicios de la auditoria con base de datos relacionales
 */
public interface TrackingService
{

    /**
     * Registra un objeto de configuracion en la auditoria
     *
     * @param oldObject Antiguo objeto
     * @param newObject Nuevo objeto
     * @param type Tipo de objeto
     */
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type);

    /**
     * Registra un objeto de configuracion en la auditoria
     *
     * @param oldObject Antiguo objeto
     * @param newObject Nuevo objeto
     * @param type Tipo de objeto
     * @param comment
     */
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type, String comment);

    /**
     * Obtener informacion del usuario obtenida del token
     *
     * @return Instancia con los datos de la autenticacion del usuario.
     * @throws Exception Error en base de datos
     */
    public AuthorizedUser getRequestUser() throws Exception;

}
