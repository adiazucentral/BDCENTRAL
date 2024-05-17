/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Task;

/**
 * Interfaz de servicios a la informacion del maestro Tareas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/06/2017
 * @see Creación
 */
public interface TaskService
{
    /**
     * Lista las tareas desde la base de datos.
     *
     * @return Lista de tareas.
     * @throws Exception Error en la base de datos.
     */
    public List<Task> list() throws Exception;

    /**
     * Registra una nueva tarea en la base de datos.
     *
     * @param task Instancia con los datos de la tarea.
     * @return Instancia con los datos de la tarea.
     * @throws Exception Error en la base de datos.
     */
    public Task create(Task task) throws Exception;
    
    /**
     * Obtener información de una tarea por un campo especifico.
     *
     * @param id ID de la tarea a ser consultada.
     * @param description Descripción de la tarea a ser consultada.
     * @return Instancia con los datos de la tarea.
     * @throws Exception Error en la base de datos.
     */
    public Task get(Integer id, String description) throws Exception;

    /**
     * Actualiza la información de una tarea en la base de datos.
     *
     * @param task Instancia con los datos de la tarea.
     * @return Objeto de la tarea modificada.
     * @throws Exception Error en la base de datos.
     */
    public Task update(Task task) throws Exception;

    /**
     *
     * Elimina una tarea de la base de datos.
     *
     * @param id ID de la tarea.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
     * Obtener información de una tarea por estado.
     *
     * @param state Estado de los tareas a ser consultadas
     * @return Instancia con los datos de los tareas.
     * @throws Exception Error en la base de datos.
     */
    public List<Task> list(boolean state) throws Exception;
}
