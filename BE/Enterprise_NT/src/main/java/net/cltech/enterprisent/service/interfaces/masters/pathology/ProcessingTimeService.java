/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.ProcessingTime;

/**
 * Interface de servicios para el maestro de horas de procesamiento de muestras de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 13/07/2021
 * @see Creacion
*/
public interface ProcessingTimeService 
{
    /**
    * lista las horas de procesamiento de muestras de patologia.
    *
    *
    * @return la lista de horas de procesamiento.
    * @throws Exception Error en la base de datos.
    */
    public List<ProcessingTime> list() throws Exception;

    /**
    * Registra una nueva hora de procesamiento en la base de datos.
    *
    * @param processingTime Instancia con los datos de la hora de procesamiento.
    * @return Instancia con los datos de la hora de procesamiento.
    * @throws Exception Error en la base de datos.
    */
    public ProcessingTime create(ProcessingTime processingTime) throws Exception;

    /**
    * Obtener información de una hora de procesamiento por un campo especifico.
    *
    * @param id ID de la hora de procesamiento a ser consultada.
    * @param time Hora de la hora de procesamiento a ser consultada.
    * @return Instancia con los datos de la hora de procesamiento.
    * @throws Exception Error en la base de datos.
    */
    public ProcessingTime get(Integer id, String time) throws Exception;

    /**
    * Actualiza una hora de procesamiento en la base de datos.
    *
    * @param processingTime Instancia con los datos de la hora de procesamiento.
    * @return la instancia con los datos de la hora de procesamiento.
    * @throws Exception Error en la base de datos.
    */
    public ProcessingTime update(ProcessingTime processingTime) throws Exception;
    
    /**
    * Obtener información de las horas de procesamiento por estado.
    *
    * @param state Estado de las horas de procesamiento a ser consultadas
    * @return Instancia con los datos de las horas de procesamiento.
    * @throws Exception Error en la base de datos.
    */
    public List<ProcessingTime> list(int state) throws Exception;
}
