package net.cltech.enterprisent.dao.impl.sqlserver.integration;

import java.sql.ResultSet;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationBandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información 
 * para la BD de SQLServer
 * 
 * @version 1.0.0
 * @author Julian
 * @since 22/05/2020
 * @see Creación
 */
@Repository
public class IntegrationBandDaoSQLServer implements IntegrationBandDao
{
    private JdbcTemplate jdbc;
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource")DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
    
    /**
     * Obtiene un listado con las ordenes registradas el dia de hoy
     * 
     * @return 
     * @throws Exception Error al obtener las muestras verificadas
     */
    @Override
    public List<Long> ordersOfTheDay() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab22c1 ")
                    .append("FROM lab22 ")
                    .append("WHERE lab22.lab07c1 = 1 AND (lab22c19 = 0 or lab22c19 is null) AND CONVERT(DATE, lab22c3) = CONVERT(DATE, GETDATE())");
            
            return jdbc.query(query.toString(), 
                    (ResultSet rs, int i)->
            {
                return rs.getLong("lab22c1");
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
