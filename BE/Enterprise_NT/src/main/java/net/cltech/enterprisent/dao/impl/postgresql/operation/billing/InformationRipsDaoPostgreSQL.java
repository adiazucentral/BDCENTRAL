package net.cltech.enterprisent.dao.impl.postgresql.operation.billing;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.billing.InformationRipsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información rips
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author omendez
 * @since 21/01/2021
 * @see Creación
 */
@Repository
public class InformationRipsDaoPostgreSQL implements InformationRipsDao
{
    private JdbcTemplate jdbc;
    
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
}
