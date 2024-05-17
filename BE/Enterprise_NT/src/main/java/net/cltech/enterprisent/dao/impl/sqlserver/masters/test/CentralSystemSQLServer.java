package net.cltech.enterprisent.dao.impl.sqlserver.masters.test;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.CentralSystemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Sistema Central para SQLServer
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/06/2017
 * @see Creación
 */
@Repository
public class CentralSystemSQLServer implements CentralSystemDao
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
