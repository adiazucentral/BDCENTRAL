package net.cltech.enterprisent.dao.impl.sqlserver.operation.results;

import java.sql.ResultSet;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultOrderDao;
import net.cltech.enterprisent.domain.operation.results.UpdateResult;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a datos para las órdenes en el módulo de registro de
 * resultados en SQLServer
 *
 * @version 1.0.0
 * @author jblanco
 * @since 03/07/2017
 * @see Creación
 */
@Repository
public class ResultOrderDaoSQLServer implements ResultOrderDao
{

    private JdbcTemplate jdbc;

    @Autowired
    private ConfigurationDao configDao;
    
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
    public ConfigurationDao getConfigDao()
    {
        return configDao;
    }
    
    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }

    @Override
    public void updateReference(UpdateResult updateResult, int valueSample) throws Exception
    {

        String query = ISOLATION_READ_UNCOMMITTED + "SELECT  lab39.lab24c1 FROM lab39 WHERE lab39.lab39c1 = ? ";

        Integer SampleValueIdExamNew = jdbc.queryForObject(query,
                (ResultSet rs, int i) ->
        {
            return rs.getInt("lab24c1");
        }, updateResult.getNewExamIdentifier());

        if (SampleValueIdExamNew == valueSample)
        {

            jdbc.update("UPDATE lab57 SET lab39c1 = ? "
                    + "WHERE lab22c1 = ? AND lab39C1 = ? ",
                    updateResult.getNewExamIdentifier(), updateResult.getNumberOrder(), updateResult.getOldExamIdentifier());

        } else if (SampleValueIdExamNew != valueSample)
        {

            jdbc.update("UPDATE lab57 SET lab39c1 = ?, lab24c1 = ?  "
                    + "WHERE lab22c1 = ? AND lab39C1 = ? ",
                    updateResult.getNewExamIdentifier(), SampleValueIdExamNew, updateResult.getNumberOrder(), updateResult.getOldExamIdentifier());

            jdbc.update("UPDATE lab159 SET lab24c1 = ?"
                    + "WHERE lab22c1 = ? AND lab24C1 = ? ",
                    SampleValueIdExamNew, updateResult.getNumberOrder(), valueSample);

        }

    }

}
