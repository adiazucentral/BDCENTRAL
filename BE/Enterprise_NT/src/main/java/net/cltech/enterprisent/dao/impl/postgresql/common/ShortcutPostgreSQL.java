package net.cltech.enterprisent.dao.impl.postgresql.common;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ShortcutDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/09/2017
 * @see Creación
 */
@Repository
public class ShortcutPostgreSQL implements ShortcutDao
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
