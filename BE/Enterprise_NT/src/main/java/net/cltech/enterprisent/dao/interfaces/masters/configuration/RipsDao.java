/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.RIPS;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la
 * configuración RIPS
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/01/2021
 * @see Creación
 */
public interface RipsDao 
{
    /**
    * Obtiene todas las llaves de configuracion rips
    *
    * @return Lista de
    * {@link net.cltech.enterprisent.domain.masters.configuration.RIPS}
    * @throws Exception Error en base de datos
    */
    public List<RIPS> get() throws Exception;

    /**
    * Obtiene una llave de configuración RIPS
    *
    * @param key Llave de configuración RIPS
    * @return
    * {@link net.cltech.enterprisent.domain.masters.configuration.RIPS},
    * null en caso de que no se encuentre la llave
    * @throws Exception Error en base de datos
    */
    public RIPS get(String key) throws Exception;

    /**
     * Actualiza una llave de configuración RIPS si existe
     *
     * @param configuration
     * {@link net.cltech.enterprisent.domain.masters.configuration.RIPS}
     * @throws Exception Error en base de datos
     */
    public void update(RIPS configuration) throws Exception;
    
    /**
    * Obtiene todas las llaves de configuracion rips asociadas a los demograficos
    *
    * @return Lista de
    * {@link net.cltech.enterprisent.domain.masters.configuration.RIPS}
    * @throws Exception Error en base de datos
    */
    public List<RIPS> getDemographic() throws Exception;
}
