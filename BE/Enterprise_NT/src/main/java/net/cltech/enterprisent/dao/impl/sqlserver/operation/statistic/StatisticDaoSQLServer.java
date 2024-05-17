package net.cltech.enterprisent.dao.impl.sqlserver.operation.statistic;

import java.util.Date;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.StatisticDao;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.service.interfaces.operation.orders.CashBoxService;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.dao.DataAccessException;

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
public class StatisticDaoSQLServer implements StatisticDao
{

    private JdbcTemplate jdbc;
    private JdbcTemplate jdbcStat;
    
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private CashBoxService cashDao;

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
    public void addDemographicToPatient(Demographic demographic) throws Exception
    {
        try
        {
            jdbcStat.queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   1 "
                    + "FROM     sys.syscolumns "
                    + "WHERE    id = object_id(N'[sta1]') and sys.syscolumns.name = 'sta_demo_" + demographic.getId() + "'", Integer.class);
        } catch (EmptyResultDataAccessException ex)
        {
            if (demographic.isEncoded())
            {
                jdbcStat.execute("ALTER TABLE sta1 ADD sta_demo_" + demographic.getId() + " INTEGER");
                jdbcStat.execute("ALTER TABLE sta1 ADD sta_demo_" + demographic.getId() + "_code VARCHAR(32)");
                jdbcStat.execute("ALTER TABLE sta1 ADD sta_demo_" + demographic.getId() + "_name VARCHAR(256)");
            } else
            {

                jdbcStat.execute("ALTER TABLE sta1 ADD sta_demo_" + demographic.getId() + " VARCHAR(256)");

            }
        }
    }

    @Override
    public void addDemographicToOrder(Demographic demographic, int yearsQuery) throws Exception
    {
        try
        {
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String sta2;
            for (Integer year : years)
            {
                sta2 = year.equals(currentYear) ? "sta2" : "sta2_" + year;
                try
                {
                    jdbcStat.queryForObject("" + ISOLATION_READ_UNCOMMITTED
                            + "SELECT   1 "
                            + "FROM     sys.syscolumns "
                            + "WHERE    id = object_id(N'["+ sta2 +"]') and sys.syscolumns.name = 'sta_demo_" + demographic.getId() + "'", Integer.class);
                } catch (EmptyResultDataAccessException ex)
                {
                    if (demographic.isEncoded())
                    {
                        jdbcStat.execute("ALTER TABLE "+ sta2 +" ADD sta_demo_" + demographic.getId() + " INTEGER");
                        jdbcStat.execute("ALTER TABLE "+ sta2 +" ADD sta_demo_" + demographic.getId() + "_code VARCHAR(32)");
                        jdbcStat.execute("ALTER TABLE "+ sta2 +" ADD sta_demo_" + demographic.getId() + "_name VARCHAR(256)");
                    } else
                    {
                        jdbcStat.execute("ALTER TABLE "+ sta2 +" ADD sta_demo_" + demographic.getId() + " VARCHAR(256)");
                    }
                }
            }
        } catch (DataAccessException e)
        {
            OrderCreationLog.error(e.getMessage());
        }
    }

    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }

    @Override
    public CashBoxService getcashBoxService()
    {
        return cashDao;
    }
}
