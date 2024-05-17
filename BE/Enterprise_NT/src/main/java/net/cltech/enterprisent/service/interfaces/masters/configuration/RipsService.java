/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.RIPS;

/**
 * Servicios de configuracion RIPS
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/01/2021
 * @see Creacion
 */
public interface RipsService 
{
    
    /**
    * Obtiene todas las llaves de configuracion rips
    *
    * @return 
    * {@link net.cltech.enterprisent.domain.masters.configuration.RIPS}
    * @throws Exception Error en el servicio
    */
    public List<RIPS> get() throws Exception;

    /**
    * Obtiene una llave de configuración rips
    *
    * @param key Llave de configuración rips
    *
    * @return
    * {@link net.cltech.enterprisent.domain.masters.configuration.RIPS},
    * null en caso de que no se encuentre la llave
    * @throws Exception Error en el servicio
    */
    public RIPS get(String key) throws Exception;

    /**
    * Obtiene el valor de configuración rips
    *
    * @param key Llave de configuración rips
    *
    * @return LLave de configuración null en caso de que no se encuentre la
    * llave
    * @throws Exception Error en el servicio
    */
    public String getValue(String key) throws Exception;

    /**
    * Actualiza una llave de configuración rips si existe
    *
    * @param configuration Lista de
    * {@link net.cltech.enterprisent.domain.masters.configuration.RIPS}
    *
    * @throws Exception Error en el servicio
    */
    public void update(List<RIPS> configuration) throws Exception;
    
    /**
    * Obtiene todas las llaves de configuracion rips asociadas a los demograficos
    *
    * @return Lista de
    * {@link net.cltech.enterprisent.domain.masters.configuration.RIPS}
    * @throws Exception Error en base de datos
    */
    public List<RIPS> getDemographic() throws Exception;
}
