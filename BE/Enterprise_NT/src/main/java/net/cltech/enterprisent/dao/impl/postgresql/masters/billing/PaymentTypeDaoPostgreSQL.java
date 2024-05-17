package net.cltech.enterprisent.dao.impl.postgresql.masters.billing;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.PaymentTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 30/08/2017
 * @see Creación
 */
@Repository
public class PaymentTypeDaoPostgreSQL implements PaymentTypeDao
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
