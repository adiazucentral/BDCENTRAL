package net.cltech.enterprisent.dao.impl.sqlserver.operation.statistic;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.OpportunityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de estadisticas
 * para SQLServer
 *
 * @version 1.0.0
 * @author eacuna
 * @since 11/12/2017
 * @see Creación
 */
@Repository
public class StatisticOpportunityDaoSQLServer implements OpportunityDao
{

    private JdbcTemplate jdbc;
    private JdbcTemplate jdbcStat;
    @Autowired
    private ToolsDao toolsDao;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setDataSourceStat(@Qualifier("dataSourceStat") DataSource dataSource)
    {
        jdbcStat = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getConnection()
    {
        return jdbc;
    }

    @Override
    public JdbcTemplate getConnectionStat()
    {
        return jdbcStat;
    }

    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }

}
