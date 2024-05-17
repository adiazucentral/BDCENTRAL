package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 16/05/2017
 * @see Creación
 */
public interface OrderTypeDao
{
    
    /**
     * Obtiene la conexion con la base de datos
     * 
     * @return Jdbc Template de sprint para el acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();
    
    /**
     * Lista tipos de orden desde la base de datos.
     *
     * @return Lista de tipos de orden.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderType> list() throws Exception;

    /**
     * Registra tipo de orden en la base de datos.
     *
     * @param create Instancia con los datos de tipo de orden.
     *
     * @return Instancia con los datos de tipo de orden.
     * @throws Exception Error en la base de datos.
     */
    public OrderType create(OrderType create) throws Exception;

    /**
     * Obtener información de tipo de orden por nombre.
     *
     * @param name Nombre de tipo de orden a ser consultada.
     *
     * @return Instancia con los datos de tipo de orden.
     * @throws Exception Error en la base de datos.
     */
    public OrderType filterByName(String name) throws Exception;

    /**
     * Obtener información de tipo de orden por id.
     *
     * @param id id de tipo de orden.
     *
     * @return Instancia con los datos de tipo de orden.
     * @throws Exception Error en la base de datos.
     */
    public OrderType filterById(Integer id) throws Exception;

    /**
     * Actualiza la información de tipo de orden en la base de datos.
     *
     * @param update Instancia con los datos de tipo de orden.
     *
     * @return Objeto de tipo de orden modificada.
     * @throws Exception Error en la base de datos.
     */
    public OrderType update(OrderType update) throws Exception;
    

}
