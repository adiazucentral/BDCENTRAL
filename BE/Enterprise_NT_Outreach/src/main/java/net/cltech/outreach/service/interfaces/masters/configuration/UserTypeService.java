/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.service.interfaces.masters.configuration;

import java.util.List;
import net.cltech.outreach.domain.masters.configuration.UserType;

/**
 * Servicios de tipos de usuario
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/04/2017
 * @see Creacion
 */
public interface UserTypeService
{

    /**
     * Obtiene los tipos de usuario de la aplicación
     *
     * @return Lista de tipos de usuario
     * @throws Exception Error en el servicio
     */
    public List<UserType> list() throws Exception;

    /**
     * Actualiza los tipos de usuario de la aplicación
     *
     * @param userTypes Lista de tipos de usuario.
     * @return Cantidad de datos afectados
     * @throws Exception Error en el servicio
     */
    public int update(List<UserType> userTypes) throws Exception;
}
