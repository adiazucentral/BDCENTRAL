package net.cltech.enterprisent.dao.interfaces.masters.common;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Motive;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de los Motivos.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/06/2017
 * @see Creación
 */
public interface MotiveDao
{
    /**
     * Obtiene la conexión con la base de datos
     * 
     * @return 
     */
    public JdbcTemplate getJdbcTemplate();
    
    /**
     * Lista los motivos desde la base de datos.
     *
     * @return Lista de motivos.
     * @throws Exception Error en la base de datos.
     */
    public List<Motive> list() throws Exception;

    /**
     * Registra un nuevo motivo en la base de datos.
     *
     * @param motive Instancia con los datos del motivo.
     * @return Instancia con los datos del motivo.
     * @throws Exception Error en la base de datos.
     */
    public Motive create(Motive motive) throws Exception;
    
    /**
     * Obtener información de un motivo por un campo especifico.
     *
     * @param id ID del motivo a ser consultada.
     * @param name Nombre del motivo a ser consultada.
     * @return Instancia con los datos del motivo.
     * @throws Exception Error en la base de datos.
     */
    public Motive get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de un motivo en la base de datos.
     *
     * @param motive Instancia con los datos del motivo.
     * @return Objeto del motivo modificada.
     * @throws Exception Error en la base de datos.
     */
    public Motive update(Motive motive) throws Exception;

    /**
     *
     * Elimina un motivo de la base de datos.
     *
     * @param id ID del motivo.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
    * Lista los motivos de patologia desde la base de datos.
    *
    * @return Lista de motivos de patologia.
    * @throws Exception Error en la base de datos.
    */
    public List<Motive> listMotivePathology() throws Exception;
    
    /**
    * Lista los motivos por tipo
    *
    * @param type
    * @return Lista de motivos
    * @throws Exception Error en la base de datos.
    */
    default List<Motive> getListMotivesByType(Integer type) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab30c1, "
                    + "lab30c2, "
                    + "lab30c3, "
                    + "lab30c5, "
                    + "lab30.lab07c1, "
                    + "lab80c1, "
                    + "lab80c2, "
                    + "lab80c3, "
                    + "lab80c4, "
                    + "lab80c5, "
                    + "lab30.lab04c1, "
                    + "lab04c2, "
                    + "lab04c3, "
                    + "lab04c4 "
                    + "FROM lab30 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab30.lab30c4 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab30.lab04c1 "
                    + "WHERE lab30c4 = ? ";
            
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                Motive motive = new Motive();
                motive.setId(rs.getInt("lab30c1"));
                motive.setName(rs.getString("lab30c2"));
                motive.setDescription(rs.getString("lab30c3"));

                /*Tipo*/
                motive.getType().setId(rs.getInt("lab80c1"));
                motive.getType().setIdParent(rs.getInt("lab80c2"));
                motive.getType().setCode(rs.getString("lab80c3"));
                motive.getType().setEsCo(rs.getString("lab80c4"));
                motive.getType().setEnUsa(rs.getString("lab80c5"));

                motive.setLastTransaction(rs.getTimestamp("lab30c5"));
                /*Usuario*/
                motive.getUser().setId(rs.getInt("lab04c1"));
                motive.getUser().setName(rs.getString("lab04c2"));
                motive.getUser().setLastName(rs.getString("lab04c3"));
                motive.getUser().setUserName(rs.getString("lab04c4"));

                motive.setState(rs.getInt("lab07c1") == 1);

                return motive;
            }, type);
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }
}
