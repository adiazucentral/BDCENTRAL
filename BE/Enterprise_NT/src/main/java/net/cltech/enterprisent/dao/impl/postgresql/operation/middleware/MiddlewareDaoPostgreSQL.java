package net.cltech.enterprisent.dao.impl.postgresql.operation.middleware;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.MiddlewareDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a datos para middleware en PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/04/2017
 * @see Creación
 */
@Repository
public class MiddlewareDaoPostgreSQL implements MiddlewareDao
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
    public JdbcTemplate getJdbcTemplateCont()
    {
        return jdbcCont;
    }
}
