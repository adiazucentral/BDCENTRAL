/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.Macroscopy;

/**
 * Interface de servicios para la información de las descripciones macroscopicas de los casos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/06/2021
 * @see Creacion
*/
public interface MacrocopyService 
{
    /**
    * Registra una nueva descripcion en la base de datos.
    *
    * @param macroscopy Instancia con los datos de la descripcion.
    * @return Instancia con los datos del descripcion.
    * @throws Exception Error en la base de datos.
    */
    public Macroscopy create(Macroscopy macroscopy) throws Exception;
    
    /**
    * Busca la descripcion macroscopica de un caso
    *
    * @param idCase Id del caso 
    * @return Instancia con los datos de la descripcion
    * @throws Exception Error en el servicio
    */
    public Macroscopy getByCase(int idCase) throws Exception;
    
    /**
    * Actualiza una descripcion en la base de datos
    *
    * @param macroscopy Instancia con los datos de la descripcion.
    *
    * @return Instancia con los datos del descripcion.
    * @throws Exception Error presentado en el servicio
    */
    public Macroscopy update(Macroscopy macroscopy) throws Exception;
    
    /**
    * Busca la descripciones macroscopicas pendientes de transcripcion
    *
    * @return Instancia con los datos de las descripciones
    * @throws Exception Error en el servicio
    */
    public List<Macroscopy> getPendingTranscripts() throws Exception;
    
    /**
    * Inserta una trancripcion en la base de datos
    *
    * @param macroscopy Instancia con los datos de la descripcion.
    *
    * @return Instancia con los datos del descripcion.
    * @throws Exception Error presentado en el servicio
    */
    public Macroscopy transcript(Macroscopy macroscopy) throws Exception;
    
    /**
    * Inserta una autorizacion en la base de datos
    *
    * @param macroscopy Instancia con los datos de la descripcion.
    *
    * @return Instancia con los datos del descripcion.
    * @throws Exception Error presentado en el servicio
    */
    public Macroscopy authorization(Macroscopy macroscopy) throws Exception;
    
    /**
    * Busca la descripciones macroscopicas pendientes de autorizacion por usuario
    *
     * @param idUser
    * @return Instancia con los datos de las descripciones
    * @throws Exception Error en el servicio
    */
    public List<Macroscopy> getPendingAuthorizations(Integer idUser) throws Exception;
}
