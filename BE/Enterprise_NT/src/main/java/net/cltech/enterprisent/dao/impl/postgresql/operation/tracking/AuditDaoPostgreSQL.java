package net.cltech.enterprisent.dao.impl.postgresql.operation.tracking;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.AuditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de listado de
 * ordenes para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 30/10/2017
 * @see Creación
 */
@Repository
public class AuditDaoPostgreSQL implements AuditDao
{

    private JdbcTemplate jdbc;
    @Autowired
    private ToolsDao toolsDao;

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

    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }
}
