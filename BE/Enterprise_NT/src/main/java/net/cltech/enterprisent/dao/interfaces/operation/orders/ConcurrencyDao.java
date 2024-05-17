package net.cltech.enterprisent.dao.interfaces.operation.orders;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Interfaz de acceso a datos para todo lo relacionado con concurrencia
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/11/2017
 * @see Creación
 */
public interface ConcurrencyDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Elimina todos los registros de la concurrencia
     *
     * @return
     * @throws Exception
     */
    public default int deleteAll() throws Exception
    {
        String update = "DELETE FROM lab113";

        int affectedRows = getJdbcTemplate().update(update);
        return affectedRows;

    }

    /**
     * Elimina orden de la concurrencia
     *
     * @param order numero de la orden
     *
     * @return registros afectados
     * @throws Exception
     */
    public default int deleteOrder(Long order) throws Exception
    {
        String update = "DELETE FROM lab113 where lab113c1 = 0 AND lab113c3 = ?";

        int affectedRows = getJdbcTemplate().update(update, order.toString());
        return affectedRows;

    }

    /**
     * Elimina orden de la concurrencia
     *
     * @param type   tipo de documento
     * @param record numero de la historia
     *
     * @return registros afectados
     * @throws Exception
     */
    public default int deleteRecord(Integer type, String record) throws Exception
    {
        String update = "DELETE FROM lab113 where lab113c1 = 1 AND lab113c3 = ? ";
        if (type != null)
        {
            update = update + " AND lab113c2 = ?";
            return getJdbcTemplate().update(update, record, type);
        }

        return getJdbcTemplate().update(update, record);
    }

}
