package net.cltech.enterprisent.service.interfaces.start.jobs;

/**
 * Interfaz de servicios de reenvío de órdenes al Middleware
 *
 * @version 1.0.0
 * @author oarango
 * @since 27/08/2020
 * @see Creación
 */
public interface ControlTableOrdersService
{

    /**
     * Crea el hilo de ejecucion para una tarea programada
     *
     *
     * @return Arreglo entero de filas afectadas
     * @throws org.quartz.SchedulerException
     * @throws java.io.IOException
     */
    public int[] sendOrders() throws Exception;
}
