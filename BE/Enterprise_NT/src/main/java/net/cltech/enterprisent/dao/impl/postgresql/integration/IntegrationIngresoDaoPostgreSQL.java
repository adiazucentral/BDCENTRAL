package net.cltech.enterprisent.dao.impl.postgresql.integration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoDao;

/**
 * Implementa el acceso a datos para examenes y demograficos en PostgreSQL
 *
 * @version 1.0.0
 * @author javila
 * @since 05/02/2020
 * @see Creación
 */
@Repository
public class IntegrationIngresoDaoPostgreSQL implements IntegrationIngresoDao
{
    private JdbcTemplate jdbc;
    private JdbcTemplate jdbcCont;
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    @Autowired
    public void setDataSourceCont(@Qualifier("dataSourceCont") DataSource dataSource)
    {
        jdbcCont = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
    
    @Override
    public JdbcTemplate getConnectionCont()
    {
        return jdbcCont;
    }
}
