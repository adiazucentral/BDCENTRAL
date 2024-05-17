package net.cltech.enterprisent.dao.impl.sqlserver.masters.test;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.ResultRelationshipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Relación de resultado para SQLServer
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/07/2017
 * @see Creación
 */
@Repository
public class ResultRelationshipSQLServer implements ResultRelationshipDao
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
