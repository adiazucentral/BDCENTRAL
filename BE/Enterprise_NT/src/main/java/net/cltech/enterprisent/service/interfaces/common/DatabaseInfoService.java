/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.common;

import net.cltech.enterprisent.domain.info.DatabaseInfo;

/**
 * Interfaz de servicios a la informacion de la base de datos
 *
 * @version 1.0.0
 * @author dcortes
 * @since 06/04/2017
 * @see Creación
 */
public interface DatabaseInfoService
{

    /**
     * Obtiene informacion de la base de datos conectada
     *
     * @return {@link net.cltech.enterprisent.domain.info.DatabaseInfo}
     * @throws Exception Error presentado en el servicio
     */
    public DatabaseInfo getInfo() throws Exception;
}
