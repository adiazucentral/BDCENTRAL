package net.cltech.outreach.dao.interfaces.demographic;

import java.sql.ResultSet;
import net.cltech.outreach.domain.demographic.Demographic;
import net.cltech.outreach.domain.demographic.QueryDemographic;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Interfaz con los metodos de acceso a datos para Demografico
 * errores
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 29/01/2020
 * @see Creacion
 */
public interface DemographicsDao  {
    
     /**
     * Obtiene la conexión a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();
    
    
       /**
     * Obtiene una llave de configuraciòn
     *
     * @param value
     * @return
     * {@link net.cltech.outreach.domain.masters.configuration.Configuration},
     * null en caso de que no se encuentre la llave
     * @throws Exception Error en base de datos
     */
    default Demographic getDemographic(int value) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT   lab62c1 ,"
                    + "         lab62c2 ,"
                    + "         lab62c3  "
                    + "FROM     lab62 "
                    + "WHERE    lab62c1 = ?",
                    new Object[]
                    {
                        value
                    }, (ResultSet rs, int i) ->
                   
            {
                   Demographic demographic = new Demographic();                
                   demographic.setId(rs.getInt("lab62c1"));
                   demographic.setName(rs.getString("lab62c2"));
                   demographic.setType(rs.getString("lab62c3"));
                    
                    return demographic;
                
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
     * Obtiene la informacion del demografico consula web
     *
     * @param id Id del demografico consula web
     * @return
     * {@link net.cltech.outreach.domain.masters.configuration.Configuration},
     * null en caso de que no se encuentre la llave
     * @throws Exception Error en base de datos
     */
    default QueryDemographic getQueryDemographic(Integer id) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT   lab181c1 ,"
                    + "         lab181c8 "
                    + "FROM     lab181 "
                    + "WHERE    lab181c1 = ?",
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
                   
            {
                   QueryDemographic demographic = new QueryDemographic();                
                   demographic.setId(rs.getInt("lab181c1"));
                   demographic.setIdItem(rs.getInt("lab181c8"));
                    
                    return demographic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
}
