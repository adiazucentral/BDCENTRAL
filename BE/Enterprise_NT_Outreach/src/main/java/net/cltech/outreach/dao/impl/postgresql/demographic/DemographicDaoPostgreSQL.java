package net.cltech.outreach.dao.impl.postgresql.demographic;

import javax.sql.DataSource;
import net.cltech.outreach.dao.interfaces.demographic.DemographicsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa los metodos de acceso a datos para el manejo de errores para
 * PostgreSQL
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 29/01/2020
 * @see Creacion
 */
@Repository
public class DemographicDaoPostgreSQL implements DemographicsDao
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
