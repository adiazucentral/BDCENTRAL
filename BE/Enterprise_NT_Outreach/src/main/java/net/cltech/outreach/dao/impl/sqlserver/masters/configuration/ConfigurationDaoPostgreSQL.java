package net.cltech.outreach.dao.impl.sqlserver.masters.configuration;

import javax.sql.DataSource;
import net.cltech.outreach.dao.interfaces.masters.configuration.ConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa los metodos de base de datos de Configuracion General para
 * Postgres
 *
 * @version 1.0.0
 * @author eacuna
 * @since 23/04/2018
 * @see Creacion
 */
@Repository
public class ConfigurationDaoPostgreSQL implements ConfigurationDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
}
