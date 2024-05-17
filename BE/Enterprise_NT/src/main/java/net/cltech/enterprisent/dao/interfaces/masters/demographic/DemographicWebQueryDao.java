package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.domain.masters.demographic.DemographicWebQuery;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la informacion del
 * demografico con gestion de la consulta web
 *
 * @version 1.0.0
 * @author javila
 * @since 23/01/2020
 * @see Creación
 */
public interface DemographicWebQueryDao
{

    /**
     * Consulta los demograficos con consulta web
     *
     * @return
     * @throws java.lang.Exception
     */
    default List<DemographicWebQuery> list() throws Exception
    {
        try
        {
            String query = "SELECT lab181c1 "
                    + ", lab181c2"
                    + ", lab181c3"
                    + ", lab181c4"
                    + ", lab181c5"
                    + ", lab181c6"
                    + ", lab181c7"
                    + ", lab181c8"
                    + ", lab181c9"
                    + ", lab181c13"
                    + " FROM lab181";
            return getJdbcTemplate().query(query,
                    (ResultSet rs, int i)
                    ->
            {
                DemographicWebQuery demographicWebQuery = new DemographicWebQuery();
                demographicWebQuery.setId(rs.getInt("lab181c1"));
                demographicWebQuery.setUser(rs.getString("lab181c2"));
                demographicWebQuery.setPassword(rs.getString("lab181c3"));
                demographicWebQuery.setPasswordExpirationDate(rs.getDate("lab181c4"));
                demographicWebQuery.setDateOfLastEntry(rs.getDate("lab181c5"));
                demographicWebQuery.setNumberFailedAttempts(rs.getByte("lab181c6"));
                demographicWebQuery.setDemographic(rs.getInt("lab181c7"));
                demographicWebQuery.setIdDemographicItem(rs.getInt("lab181c8"));                 
                demographicWebQuery.setState(rs.getBoolean("lab181c9"));
                demographicWebQuery.setEmail(rs.getString("lab181c13"));
                return demographicWebQuery;
            });
        } catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    /**
     * Crear registro en demograficos consulta web
     *
     * @param demographicWebQuery
     * @return
     * @throws java.lang.Exception
     */
    default DemographicWebQuery create(DemographicWebQuery demographicWebQuery) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab181")
                    .usingColumns("lab181c2", "lab181c3", "lab181c4", "lab181c5", "lab181c6", "lab181c7", "lab181c8", "lab181c9", "lab181c13", "lab04c1", "lab181c15")
                    .usingGeneratedKeyColumns("lab181c1");

            HashMap parameters = new HashMap();
            parameters.put("lab181c2", demographicWebQuery.getUser());
            parameters.put("lab181c3", Tools.encrypt(demographicWebQuery.getPassword().trim()));
            parameters.put("lab181c4", timestamp);
            parameters.put("lab181c5", null);
            parameters.put("lab181c6", 0);
            parameters.put("lab181c7", demographicWebQuery.getDemographic());
            parameters.put("lab181c8", demographicWebQuery.getIdDemographicItem());
            parameters.put("lab181c9", demographicWebQuery.isState());
            parameters.put("lab181c13", demographicWebQuery.getEmail());
            parameters.put("lab04c1", demographicWebQuery.getUserLastTransaction().getId());
            parameters.put("lab181c15", timestamp);

            Number key = insert.executeAndReturnKey(parameters);
            demographicWebQuery.setId(key.intValue());
            demographicWebQuery.setPassword(null);

            return demographicWebQuery;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Modificar o Actualizar datos de demograficos consulta web
     *
     * @param demographicWebQuery
     * @return Demografico consulta web
     * @throws Exception Error en la base de datos
     */
    default DemographicWebQuery update(DemographicWebQuery demographicWebQuery) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            
            if (demographicWebQuery.getPassword() != null)
            {
                getJdbcTemplate().update("UPDATE lab181 SET lab181c2 = ?"
                        + ", lab181c3 = ?"
                        + ", lab181c4 = ?"
                        + ", lab181c9 = ?"
                        + ", lab181c10 = ?"
                        + ", lab181c11 = ?"
                        + ", lab181c13 = ?"
                        + ", lab04c1 = ?"
                        + ", lab181c15 = ?"
                        + " WHERE lab181c1 = ?",
                        demographicWebQuery.getUser(),
                        demographicWebQuery.getPassword(),
                        demographicWebQuery.getPasswordExpirationDate(),
                        demographicWebQuery.isState() ? 1 : 0,
                        demographicWebQuery.getPenultimatePassword(), 
                        demographicWebQuery.getAntepenultimatePassword(),
                        demographicWebQuery.getEmail(),
                        demographicWebQuery.getUserLastTransaction().getId(),
                        timestamp,
                        demographicWebQuery.getId()
                        );
            } else if (demographicWebQuery.getPassword() == null || demographicWebQuery.getPassword().isEmpty())
            {
                getJdbcTemplate().update("UPDATE lab181 SET lab181c2 = ?"
                        + ", lab181c9 = ?"
                        + ", lab181c13 = ?"
                        + ", lab04c1 = ?"
                        + ", lab181c15 = ?"
                        + " WHERE lab181c1 = ?",
                        demographicWebQuery.getUser(),
                        demographicWebQuery.isState() ? 1 : 0,
                        demographicWebQuery.getEmail(),
                        demographicWebQuery.getUserLastTransaction().getId(),
                        timestamp,
                        demographicWebQuery.getId());
                
            }
            demographicWebQuery.setPassword(null);
            return demographicWebQuery;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Consulta de nombre de usuario a la tabla demografico consulta web
     *
     * @param isEdit
     * @param userName
     * @return El nombre de usuario existente, o nulo si por el contrario no
     * existe
     * @throws Exception Error en la base de datos
     */
    default String validateUsername(boolean isEdit, String userName) throws Exception
    {
        try
        {
            if (isEdit)
            {

            } else
            {
                String sql = "SELECT lab181c2 FROM lab181 WHERE LOWER(lab181c2) = ?";
                return getJdbcTemplate().queryForObject(sql, new Object[]
                {
                    userName.toLowerCase()
                },
                        (ResultSet rs, int i)
                        ->
                {
                    return rs.getString("lab181c2");
                });
            }
        } catch (EmptyResultDataAccessException ex)
        {
            return "";
        }
        return "";
    }

    /**
     * Obtener resultados del nombre de usuario en cuestion
     *
     * @param userName
     * @return
     * @throws Exception Error de base de datos
     */
    default DemographicWebQuery get(String userName) throws Exception
    {
        try
        {
            String query = "SELECT lab181c1"
                    + ", lab181c2"
                    + ", lab181c3"
                    + ", lab181c4"
                    + ", lab181c5"
                    + ", lab181c6"
                    + ", lab181c7"
                    + ", lab181c8"
                    + ", lab181c9 "
                    + ", lab181c10 "
                    + ", lab181c11 "
                    + "FROM lab181 "
                    + "WHERE LOWER(lab181c2) = LOWER(?)";
            return getJdbcTemplate().queryForObject(query, new Object[]
            {
                userName.toLowerCase()
            },
                    (ResultSet rs, int i)
                    ->
            {
                DemographicWebQuery demographicWebQuery = new DemographicWebQuery();
                demographicWebQuery.setId(rs.getInt("lab181c1"));
                demographicWebQuery.setUser(rs.getString("lab181c2"));
                demographicWebQuery.setPassword(rs.getString("lab181c3"));
                demographicWebQuery.setPasswordExpirationDate(rs.getDate("lab181c4"));
                demographicWebQuery.setDateOfLastEntry(rs.getDate("lab181c5"));
                demographicWebQuery.setNumberFailedAttempts(rs.getInt("lab181c6"));
                demographicWebQuery.setDemographic(rs.getInt("lab181c7"));
                demographicWebQuery.setIdDemographicItem(rs.getInt("lab181c8"));
                demographicWebQuery.setPenultimatePassword(rs.getString("lab181c10")); 
                demographicWebQuery.setAntepenultimatePassword(rs.getString("lab181c11"));
                demographicWebQuery.setState(rs.getBoolean("lab181c9"));
                
                return demographicWebQuery;
            });
        } catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }
    
    /**
     * Obtener resultados del nombre de usuario en cuestion
     *
     * @param id
     * @return
     * @throws Exception Error de base de datos
     */
    default DemographicWebQuery getById(int id) throws Exception
    {
        try
        {
            String query = "SELECT lab181c1"
                    + ", lab181c2"
                    + ", lab181c3"
                    + ", lab181c4"
                    + ", lab181c5"
                    + ", lab181c6"
                    + ", lab181c7"
                    + ", lab181c8"
                    + ", lab181c9 "
                    + ", lab181c10 "
                    + ", lab181c11 "
                    + ", lab181c13 "
                    + "FROM lab181 "
                    + "WHERE lab181c1 = ?";
            return getJdbcTemplate().queryForObject(query, new Object[]
            {
                id
            },
                    (ResultSet rs, int i)
                    ->
            {
                DemographicWebQuery demographicWebQuery = new DemographicWebQuery();
                demographicWebQuery.setId(rs.getInt("lab181c1"));
                demographicWebQuery.setUser(rs.getString("lab181c2"));
                demographicWebQuery.setPassword(rs.getString("lab181c3"));
                demographicWebQuery.setPasswordExpirationDate(rs.getDate("lab181c4"));
                demographicWebQuery.setDateOfLastEntry(rs.getDate("lab181c5"));
                demographicWebQuery.setNumberFailedAttempts(rs.getInt("lab181c6"));
                demographicWebQuery.setDemographic(rs.getInt("lab181c7"));
                demographicWebQuery.setIdDemographicItem(rs.getInt("lab181c8"));
                demographicWebQuery.setState(rs.getBoolean("lab181c9"));
                demographicWebQuery.setPenultimatePassword(rs.getString("lab181c10")); 
                demographicWebQuery.setAntepenultimatePassword(rs.getString("lab181c11"));
                demographicWebQuery.setEmail(rs.getString("lab181c13"));           
                
                return demographicWebQuery;
            });
        } catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene una lista de demograficos consulta web según sea su id
     * demografico y su demografico
     *
     * @param idDemographicWebQuery
     * @param demographicWebQuery
     * @return
     * @throws Exception Error de base de datos
     */
    default DemographicWebQuery filterByIdDemographic(int idDemographicWebQuery, int demographicWebQuery) throws Exception
    {
        try
        {
            String query = "SELECT lab181c1"
                    + ", lab181c2"
                    + ", lab181c3"
                    + ", lab181c4"
                    + ", lab181c5"
                    + ", lab181c6"
                    + ", lab181c7"
                    + ", lab181c8"
                    + ", lab181c9"
                    + ", lab181c13"
                    + ", lab181.lab04c1"
                    + ", lab04c2"
                    + ", lab04c3"
                    + ", lab04c4"
                    + ", lab181c15"               
                    + " FROM lab181"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab181.lab04c1 "
                    + " WHERE lab181c7 = ? AND lab181c8 = ?";
            return getJdbcTemplate().queryForObject(query, new Object[]
            {
                idDemographicWebQuery,
                demographicWebQuery
            }, (ResultSet rs, int i) ->
            {
                DemographicWebQuery demographicWebQuery1 = new DemographicWebQuery();
                demographicWebQuery1.setId(rs.getInt("lab181c1"));
                demographicWebQuery1.setUser(rs.getString("lab181c2"));
                demographicWebQuery1.setPasswordExpirationDate(rs.getDate("lab181c4"));
                demographicWebQuery1.setDemographic(rs.getInt("lab181c7"));
                demographicWebQuery1.setIdDemographicItem(rs.getInt("lab181c8"));
                demographicWebQuery1.setState(rs.getBoolean("lab181c9"));
                demographicWebQuery1.setEmail(rs.getString("lab181c13"));
                
                demographicWebQuery1.setLastTransaction(rs.getTimestamp("lab181c15"));
                /*Usuario*/
                demographicWebQuery1.getUserLastTransaction().setId(rs.getInt("lab04c1"));
                demographicWebQuery1.getUserLastTransaction().setName(rs.getString("lab04c2"));
                demographicWebQuery1.getUserLastTransaction().setLastName(rs.getString("lab04c3"));
                demographicWebQuery1.getUserLastTransaction().setUserName(rs.getString("lab04c4"));
                
                return demographicWebQuery1;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }
    
     /**
     * Lista de usuarios con id y fecha de ultimo ingreso al sistema, para
     * verifiacion de estado.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    default List<DemographicWebQuery> listDeactivate() throws Exception
    {
        try
        {
            String sql = "SELECT lab181c1, lab181c5 "
                    + " FROM lab181 "
                    + " WHERE lab181c9 = 1";
            return getJdbcTemplate().query(sql, (ResultSet rs, int i) ->
            {
                DemographicWebQuery demographicWebQuery = new DemographicWebQuery();
                demographicWebQuery.setId(rs.getInt("lab181c1"));
                demographicWebQuery.setDateOfLastEntry(rs.getTimestamp("lab181c5"));
                return demographicWebQuery;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
     * Cambia de estado un usuario de consulta web 
     *
     * @param userDemographicWebQuery {@link net.cltech.enterprisent.domain.masters.demographic.demographicwebquery}
     */
    default void changeStateUserDemographicWebQuery(DemographicWebQuery userDemographicWebQuery)
    {
        getJdbcTemplate().update("UPDATE lab04 SET lab07c1 = ? WHERE lab04c1 = ? ", userDemographicWebQuery.isState() ? 1 : 0, userDemographicWebQuery.getId());
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
