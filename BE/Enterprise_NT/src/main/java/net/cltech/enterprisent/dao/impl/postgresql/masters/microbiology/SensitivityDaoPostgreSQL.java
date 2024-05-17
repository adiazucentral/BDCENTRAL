package net.cltech.enterprisent.dao.impl.postgresql.masters.microbiology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.SensitivityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos para
 * PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 27/06/2017
 * @see Creación
 */
@Repository
public class SensitivityDaoPostgreSQL implements SensitivityDao
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
