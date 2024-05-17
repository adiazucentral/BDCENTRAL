package net.cltech.enterprisent.dao.impl.sqlserver.operation.orders;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.orders.ConcurrencyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa los metodos de acceso a datos para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/11/2017
 * @see Creacion
 */
@Repository
public class ConcurrencyDaoSQLServer implements ConcurrencyDao
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
