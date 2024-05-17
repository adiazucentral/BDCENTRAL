package net.cltech.enterprisent.dao.impl.sqlserver.masters.configuration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa los metodos de base de datos de Configuracion General para SQL
 * Server
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creacion
 */
@Repository
public class ConfigurationDaoSQLServer implements ConfigurationDao
{

    private JdbcTemplate jdbc;
    private JdbcTemplate jdbcStat;

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    @Autowired
    public void setDataSourceStat(@Qualifier("dataSourceStat") DataSource dataSource)
    {
        jdbcStat = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Configuration> get() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT   lab98c1 "
                    + " , lab98c2 "
                    + "FROM     lab98", (ResultSet rs, int i) ->
            {
                Configuration configuration = new Configuration();
                configuration.setKey(rs.getString("lab98c1"));
                configuration.setValue(rs.getString("lab98c2"));
                return configuration;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Configuration get(String key) throws Exception
    {
        try
        {
            return jdbc.queryForObject(""
                    + "SELECT   lab98c1 "
                    + " , lab98c2 "
                    + "FROM     lab98 "
                    + "WHERE    lab98c1 = ?",
                    new Object[]
                    {
                        key
                    }, (ResultSet rs, int i) ->
            {
                Configuration configuration = new Configuration();
                configuration.setKey(rs.getString("lab98c1"));
                configuration.setValue(rs.getString("lab98c2"));
                return configuration;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public void update(Configuration configuration) throws Exception
    {
        jdbc.update("UPDATE lab98 SET lab98c2 = ? WHERE lab98c1 = ?", configuration.getValue(), configuration.getKey());
    }
    
    @Override
    public void renameOperationTablesByYear(Integer year)
    {
        jdbc.execute("EXEC RENAME_NEW_YEAR @param1 = '"+ year +"';");
    }
    
    @Override
    public void renameOperationTablesStat(Integer year)
    {
        jdbcStat.execute("EXEC RENAME_NEW_YEAR_STA @param1 = '"+ year +"';");
    }
}
