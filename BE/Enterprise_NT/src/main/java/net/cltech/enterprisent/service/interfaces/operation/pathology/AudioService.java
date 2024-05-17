/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import net.cltech.enterprisent.domain.operation.pathology.Audio;

/**
 * Interface de servicios para la información de los audios de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/06/2021
 * @see Creacion
*/
public interface AudioService 
{
    /**
    * Registra un nuevo audio en la base de datos.
    *
    * @param audio Instancia con los datos del audio.
    * @return Instancia con los datos del audio.
    * @throws Exception Error en la base de datos.
    */
    public Audio create(Audio audio) throws Exception;
    
    /**
    * Actualiza un audio en la base de datos.
    *
    * @param audio Instancia con los datos del audio.
    * @return la instancia del objeto audio.
    * @throws Exception Error en la base de datos.
    */
    public Audio update(Audio audio) throws Exception;
}
