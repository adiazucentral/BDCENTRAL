/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.EventPathology;

/**
 * Interface de servicios para el maestro de eventos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 18/05/2021
 * @see Creacion
*/
public interface EventPathologyService 
{
    /**
    * lista de eventos de patologia.
    *
    *
    * @return la lista de eventos.
    * @throws Exception Error en la base de datos.
    */
    public List<EventPathology> list() throws Exception;

    /**
    * Registra un nuevo eventos en la base de datos.
    *
    * @param event Instancia con los datos del evento.
    * @return Instancia con los datos del evento.
    * @throws Exception Error en la base de datos.
    */
    public EventPathology create(EventPathology event) throws Exception;

    /**
    * Obtener información de un evento por un campo especifico.
    *
    * @param id ID del evento a ser consultado.
    * @param code Codigo del evento a ser consultado.
    * @return Instancia con los datos del evento.
    * @throws Exception Error en la base de datos.
    */
    public EventPathology get(Integer id, String code) throws Exception;

    /**
    * Actualiza un evento de la base de datos.
    *
    * @param event Instancia con los datos del evento.
    * @return la instancia del objeto evento.
    * @throws Exception Error en la base de datos.
    */
    public EventPathology update(EventPathology event) throws Exception;
    
    /**
    * Obtener información de un evento por estado.
    *
    * @param state Estado de los eventos a ser consultados
    * @return Instancia con los datos de los eventos.
    * @throws Exception Error en la base de datos.
    */
    public List<EventPathology> list(int state) throws Exception;
}
