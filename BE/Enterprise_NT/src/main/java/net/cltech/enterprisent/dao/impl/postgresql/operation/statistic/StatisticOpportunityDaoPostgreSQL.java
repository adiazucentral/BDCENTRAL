package net.cltech.enterprisent.dao.impl.postgresql.operation.statistic;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.OpportunityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de estadisticas
 * para Postgres
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/02/2018
 * @see Creación
 */
@Repository
public class StatisticOpportunityDaoPostgreSQL implements OpportunityDao
{

    @Autowired
    private ToolsDao toolsDao;

    private JdbcTemplate jdbc;
    private JdbcTemplate jdbcStat;

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
