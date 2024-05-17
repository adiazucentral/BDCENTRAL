/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.service.interfaces.start.jobs;

import java.io.IOException;
import org.quartz.SchedulerException;

/**
 * Interfaz de servicios de inicio de la aplicacion
 *
 * @version 1.0.0
 * @author equijano
 * @since 04/12/2018
 * @see Creacion
 */
public interface SchedulerService
{

    /**
     * Crea el hilo de ejecucion para la tarea de reinicio de numeracion de
     * ordenes
     *
     *
     * @param hour
     * @throws org.quartz.SchedulerException
     * @throws java.io.IOException
     */
    public void createThread(String hour) throws SchedulerException, IOException;

    /**
     * Cerrar todos los hilos activos
     *
     *
     */
    public void shutdown();
}
