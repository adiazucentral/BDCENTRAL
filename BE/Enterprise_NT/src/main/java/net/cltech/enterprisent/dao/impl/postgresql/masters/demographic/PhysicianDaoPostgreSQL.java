package net.cltech.enterprisent.dao.impl.postgresql.masters.demographic;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.PhysicianDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro para
 * PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 22/05/2017
 * @see Creación
 */
@Repository
public class PhysicianDaoPostgreSQL implements PhysicianDao
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
