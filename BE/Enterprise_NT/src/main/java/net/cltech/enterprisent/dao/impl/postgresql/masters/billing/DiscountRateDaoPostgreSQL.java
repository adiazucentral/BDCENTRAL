package net.cltech.enterprisent.dao.impl.postgresql.masters.billing;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.DiscountRateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * realiza la implementacion de los métodos de acceso a base de datos 
 * para la información de los tipos de descuentos para PostgreSQL
 *
 * @version 1.0.0
 * @author javila
 * @since 23/03/2021
 * @see Creación
 */
@Repository
public class DiscountRateDaoPostgreSQL implements DiscountRateDao
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
