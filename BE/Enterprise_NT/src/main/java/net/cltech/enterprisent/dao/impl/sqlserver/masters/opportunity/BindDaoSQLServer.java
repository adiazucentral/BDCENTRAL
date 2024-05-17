package net.cltech.enterprisent.dao.impl.sqlserver.masters.opportunity;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.opportunity.BindDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Clases para SQLServer
 *
 * @version 1.0.0
 * @author eacuna
 * @since 15/02/2018
 * @see Creación
 */
@Repository
public class BindDaoSQLServer implements BindDao
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
