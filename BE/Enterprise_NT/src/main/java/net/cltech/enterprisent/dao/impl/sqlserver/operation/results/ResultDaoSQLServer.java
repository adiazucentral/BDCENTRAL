package net.cltech.enterprisent.dao.impl.sqlserver.operation.results;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultDao;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de
 * resultados PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/10/2017
 * @see Creación
 */
@Repository
public class ResultDaoSQLServer implements ResultDao
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
    
    /**
     * Lista los id de las ordenes por rango de fechas una fecha de inicio y otra de finalización
     * solo ordenes ya validadas dentro de esas fechas
     *
     * @param startDate
     * @param endDate
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<Long> rangeOrders(String startDate, String endDate) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab22c1 ")
                    .append("FROM lab57 ")
                    .append("WHERE CONVERT(DATE, lab57c2) BETWEEN CONVERT(DATE, ?)")
                    .append(" AND CONVERT(DATE, ?)")
                    .append(" AND lab57c8 = 4");

            return jdbc.query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getLong("lab22c1");
            }, startDate, endDate);
        }
        catch (Exception ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }
}
