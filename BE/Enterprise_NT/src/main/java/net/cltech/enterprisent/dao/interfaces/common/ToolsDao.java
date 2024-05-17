package net.cltech.enterprisent.dao.interfaces.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Interfaz con metodos de utilidad de base de datos
 *
 * @version 1.0.0
 * @author dcortes
 * @since 24/10/2017
 * @see Creacion
 */
public interface ToolsDao
{

    /**
     * Crea una secuencia en base de datos validando que esta secuencia no
     * exista
     *
     * @param name Nombre de la secuencia
     * @param start Numero inicial de la secuencia
     * @param increment Incremento de la secuencia
     * @param maximum valor maximo
     * @throws Exception Error en base de datos
     */
    public void createSequence(String name, int start, int increment, int maximum) throws Exception;

    /**
     * Reinicia una secuencia al numero establecido
     *
     * @param name Nombre de secuencia
     * @param start Numero a reiniciar
     * @throws Exception Error en base de datos
     */
    public void resetSequence(String name, int start) throws Exception;

    /**
     * Reinicia una secuencia al numero establecido
     *
     * @param name Nombre de secuencia
     * @param start Numero a reiniciar
     * @param maximum Numero maximo
     * @throws Exception Error en base de datos
     */
    public void resetSequence(String name, int start, int maximum) throws Exception;

    /**
     * Validar si la secuencia ya existe
     *
     * @param name Nombre de secuencia
     * @return
     * @throws Exception Error en base de datos
     */
    public boolean validateSequence(String name) throws Exception;

    /**
     * Obtiene el siguiente valor de una secuencia
     *
     * @param name Nombre de la secuencia
     * @return Siguiente numero de la secuencia. -1 en caso de no encontrar la
     * secuencia
     * @throws Exception Error en base de datos
     */
    public int nextVal(String name) throws Exception;

    /**
     * Obtiene el siguiente valor de una secuencia
     *
     * @param name Nombre de la secuencia
     * @return Siguiente numero de la secuencia. -1 en caso de no encontrar la
     * secuencia
     * @throws Exception Error en base de datos
     */
    public int nextValQuery(String name) throws Exception;
    
     /**
     * Obtiene el siguiente valor de una secuencia sin incrementarla
     *
     * @param name Nombre de la secuencia
     * @return Siguiente numero de la secuencia. -1 en caso de no encontrar la
     * secuencia
     * @throws Exception Error en base de datos
     */
    public int getValQuery(String name) throws Exception;
    
    /**
    * Validar si la secuencia de patologia ya existe
    *
    * @param name Nombre de secuencia
    * @return
    * @throws Exception Error en base de datos
    */
    public boolean validateSequencePathology(String name) throws Exception;
    
    /**
    * Crea una secuencia en base de datos de patologia validando que esta secuencia no
    * exista
    *
    * @param name Nombre de la secuencia
    * @param start Numero inicial de la secuencia
    * @param increment Incremento de la secuencia
    * @param maximum valor maximo
    * @throws Exception Error en base de datos
    */
    public void createSequencePathology(String name, int start, int increment, int maximum) throws Exception;
    
    
    /**
     * Reinicia una secuencia de patologia al numero establecido
     *
     * @param name Nombre de secuencia
     * @param start Numero a reiniciar
     * @throws Exception Error en base de datos
     */
    public void resetSequencePathology(String name, int start) throws Exception;
    
    /**
    * Reinicia una secuencia de patologia al numero establecido
    *
    * @param name Nombre de secuencia
    * @param start Numero a reiniciar
    * @param maximum Numero maximo
    * @throws Exception Error en base de datos
    */
    public void resetSequencePathology(String name, int start, int maximum) throws Exception;
    
    /**
    * Obtiene el siguiente valor de una secuencia de patologia
    *
    * @param name Nombre de la secuencia
    * @return Siguiente numero de la secuencia. -1 en caso de no encontrar la
    * secuencia
    * @throws Exception Error en base de datos
    */
    public int nextValPathology(String name) throws Exception;
    
    /**
    * Valida si una tabla existe en la base de datos
    *
     * @param connection Conexion a la base de datos
    * @param name Nombre de la tabla 
     * @return  
    * @throws SQLException Error en base de datos
    */
    default boolean tableExists(JdbcTemplate connection, String name) throws SQLException 
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT count(*) as count FROM information_schema.tables WHERE table_name =  '").append(name).append("'");
           
            return connection.queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("count") != 0;
            });
        }
        catch (DataAccessException e)
        {
            return false;
        }
    }
}
