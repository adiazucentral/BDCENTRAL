/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.statistic;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.AgileStatisticDao;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de estadisticas
 * rapidas para SQLServer
 *
 * @version 1.0.0
 * @author cmartin
 * @since 04/04/2018
 * @see Creación
 */
@Repository
public class AgileStatisticDaoSQLServer implements AgileStatisticDao
{

    private JdbcTemplate jdbcStat;

    @Autowired
    public void setDataSourceStat(@Qualifier("dataSourceStat") DataSource dataSource)
    {
        jdbcStat = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getConnectionStat()
    {
        return jdbcStat;
    }

    @Override
    public List<String> getExistingYears()
    {
        try
        {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT TABLE_NAME "
                    + "FROM INFORMATION_SCHEMA.TABLES "
                    + "WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_NAME LIKE '%sta5%'";

            RowMapper mapper = (RowMapper<String>) (ResultSet rs, int i) -> rs.getString(1);
            return jdbcStat.query(query, mapper);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
}
