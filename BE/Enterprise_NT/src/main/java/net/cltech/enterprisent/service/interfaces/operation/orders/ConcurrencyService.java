package net.cltech.enterprisent.service.interfaces.operation.orders;

/**
 * Servicios sobre la concurrencia de ordenes y pacientes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/11/2017
 * @see Creacion
 */
public interface ConcurrencyService
{

    /**
     * Elimina toda la concurrencia
     *
     *
     * @return Registros afectados
     * @throws Exception Error en el servicio
     */
    public int deleteAll() throws Exception;

    /**
     * Elimina concurrencia de una orden
     *
     *
     * @param order número de orden
     *
     * @return Registros afectados
     * @throws Exception Error en el servicio
     */
    public int deleteOrder(Long order) throws Exception;

    /**
     * Elimina concurrencia de la historia
     *
     *
     * @param type   tipo de documento
     * @param record historia
     *
     * @return Registros afectados
     * @throws Exception Error en el servicio
     */
    public int deleteRecord(Integer type, String record) throws Exception;

}
