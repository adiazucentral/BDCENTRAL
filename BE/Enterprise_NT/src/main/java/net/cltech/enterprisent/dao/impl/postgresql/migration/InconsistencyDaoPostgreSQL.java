package net.cltech.enterprisent.dao.impl.postgresql.migration;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.migration.InconsistencyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de
 * inconsistencias para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/09/2017
 * @see Creación
 */
@Repository
public class InconsistencyDaoPostgreSQL implements InconsistencyDao
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
