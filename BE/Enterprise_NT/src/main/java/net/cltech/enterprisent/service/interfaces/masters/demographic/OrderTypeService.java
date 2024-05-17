package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;

/**
 * Interfaz de tipo ordens a la informacion del maestro Tipo orden
 *
 * @version 1.0.0
 * @author eacuna
 * @since 16/05/2017
 * @see Creación
 */
public interface OrderTypeService
{

    /**
     * Lista tipo orden desde la base de datos.
     *
     * @return Lista de tipo orden.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderType> list() throws Exception;

    /**
     * Registra una nueva tipo orden en la base de datos.
     *
     * @param create Instancia con los datos del tipo orden.
     *
     * @return Instancia con los datos del tipo orden.
     * @throws Exception Error en la base de datos.
     */
    public OrderType create(OrderType create) throws Exception;

    /**
     * Obtener información de tipo orden por un campo especifico.
     *
     * @param id ID de tipo orden a consultar.
     *
     * @return Instancia con los datos del tipo orden.
     * @throws Exception Error en la base de datos.
     */
    public OrderType filterById(Integer id) throws Exception;

    /**
     * Obtener información de tipo orden por un campo especifico.
     *
     * @param name name de tipo orden a consultar.
     *
     * @return Instancia con los datos del tipo orden.
     * @throws Exception Error en la base de datos.
     */
    public OrderType filterByName(String name) throws Exception;

    /**
     * Obtener información de tipo orden por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del tipo orden.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderType> filterByState(boolean state) throws Exception;

    /**
     * Obtener información de tipo orden por un campo especifico.
     *
     * @param code Codigo .
     *
     * @return Instancia con los datos del tipo orden.
     * @throws Exception Error en la base de datos.
     */
    public OrderType filterByCode(String code) throws Exception;

    /**
     * Actualiza la información de un tipo orden en la base de datos.
     *
     * @param update Instancia con los datos del tipo orden.
     *
     * @return Objeto de tipo orden modificada.
     * @throws Exception Error en la base de datos.
     */
    public OrderType update(OrderType update) throws Exception;
    
    
}
