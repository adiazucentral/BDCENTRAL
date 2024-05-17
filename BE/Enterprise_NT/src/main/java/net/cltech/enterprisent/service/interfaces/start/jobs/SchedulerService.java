package net.cltech.enterprisent.service.interfaces.start.jobs;

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
     * Crea el hilo de ejecucion para una tarea programada
     *
     *
     * @param hour
     * @throws org.quartz.SchedulerException
     * @throws java.io.IOException
     */
    public void createThread(String hour) throws SchedulerException, IOException;
    
    /**
     * Cierra todos los hilos activos
     */
    public void shutdown();
    
    /**
     * Crea el hilo de ejecución para la tarea programada
     * de la limpieza de la tabla de excepciones.
     *
     * @throws org.quartz.SchedulerException
     * @throws java.io.IOException
     */
    public void jobTableCleaning() throws SchedulerException, IOException;
    
    /**
     * Crea el hilo de ejecución, para la tarea programada
     * que se encargará de cada año de cambiar el nombre de las tablas de operación,
     * sus llaves primarias, y sus Indexes
     *
     * @throws org.quartz.SchedulerException
     * @throws java.io.IOException
     */
    public void jobRenameOperationTablesByYear() throws SchedulerException, IOException;
}
