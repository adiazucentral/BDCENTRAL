/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Schedule;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;

/**
 * interface de servicios para la configuracion de la agenda de los patologos
 *
 * @version 1.0.0
 * @author omendez
 * @since 22/04/2021
 * @see Creacion
 */
public interface ScheduleService 
{
    
    /**
    * Obtener información de una agenda por un campo especifico.
    *
    * @param id ID de la agenda a ser consultada.
    * @return Instancia con los datos del organo.
    * @throws Exception Error en la base de datos.
    */
    public Schedule get(Integer id) throws Exception;
    
    /**
    * Obtener información de la agenda por un campo especifico.
    *
    * @param idPathologist ID del patologo
    * @return Instancia con los datos de la agenda.
    * @throws Exception Error en la base de datos.
    */
    public List<Schedule> getByPathologist(Integer idPathologist) throws Exception;
    
    /**
    * Obtener información de la agenda por un campo especifico.
    *
    * @param idPathologist ID del patologo
    * @param filter Filtros en fechas
    * @return Instancia con los datos de la agenda.
    * @throws Exception Error en la base de datos.
    */
    public List<Schedule> getByPathologist(Integer idPathologist, FilterPathology filter) throws Exception;
    
    /**
    * Registra una nueva configuracion de agenda en la base de datos.
    *
    * @param schedule Instancia con los datos de la agenda.
    * @return Instancia con los datos de la agenda.
    * @throws Exception Error en la base de datos.
    */
    public Schedule create(Schedule schedule) throws Exception;
    
    /**
    * Actualiza una configuracion de agenda de la base de datos.
    *
    * @param schedule Instancia con los datos de la agenda.
    * @return la instancia del objeto agenda.
    * @throws Exception Error en la base de datos.
    */
    public Schedule update(Schedule schedule) throws Exception;
    
    /**
    * Elimina una configuracion de agenda de la base de datos
    *
    * @param id
    * @throws Exception Error en la base de datos.
    */
    public void delete(int id) throws Exception;
}
