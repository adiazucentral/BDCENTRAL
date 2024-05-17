package net.cltech.enterprisent.dao.impl.postgresql.operation.result;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.WorklistDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de
 * hojas de trabajo PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/10/2017
 * @see Creación
 */
@Repository
public class WorklistDaoPostgreSQL implements WorklistDao
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
    public JdbcTemplate getConnection()
    {
        return jdbc;
    }

   @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }
}
