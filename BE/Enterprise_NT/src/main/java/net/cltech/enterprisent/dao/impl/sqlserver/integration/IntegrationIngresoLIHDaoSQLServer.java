package net.cltech.enterprisent.dao.impl.sqlserver.integration;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoLIHDao;
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
public class IntegrationIngresoLIHDaoSQLServer implements IntegrationIngresoLIHDao{
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
