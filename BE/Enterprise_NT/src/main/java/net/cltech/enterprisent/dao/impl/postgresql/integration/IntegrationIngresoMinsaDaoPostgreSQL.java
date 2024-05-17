package net.cltech.enterprisent.dao.impl.postgresql.integration;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationMinsaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementacion de Integracion de ingreso para Enterprise NT
 *
 * @version 1.0.0
 * @author BValero
 * @since 23/04/2020
 * @see Creacion
 */
@Repository
public class IntegrationIngresoMinsaDaoPostgreSQL implements IntegrationMinsaDao
{

    private JdbcTemplate jdbcStat;
    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSourceStat(@Qualifier("dataSourceStat") DataSource dataSource)
    {
        jdbcStat = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getConnection()
    {
        return jdbc;
    }

    @Override
    public JdbcTemplate getConnectionStat()
    {
        return jdbcStat;
    }

}
