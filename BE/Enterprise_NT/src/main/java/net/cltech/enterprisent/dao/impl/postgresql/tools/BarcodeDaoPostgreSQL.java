package net.cltech.enterprisent.dao.impl.postgresql.tools;

import java.sql.ResultSet;
import java.util.Date;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.tools.BarcodeDao;
import net.cltech.enterprisent.domain.tools.OrderTestPatientHistory;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Tarifas para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 10/12/2018
 * @see Creación
 */
@Repository
public class BarcodeDaoPostgreSQL implements BarcodeDao
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

    ///--------------------------actualiza capo de fecha historicos
    /**
     * Obtiene la ultima orden donde se valido el examen cuando se crea la orden
     *
     * @param order.
     * @throws Exception Error en la base de datos.
     */
    @Override
    public Long lastOrderValidate(OrderTestPatientHistory order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order.getNumberOrderSinDate()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT  lab22.lab22c1  AS lab22c1 "
                    + "FROM  " + lab57 + " as lab57 "
                    + "inner join " + lab22 + " as lab22  on lab22.lab22c1 =lab57.lab22c1  "
                    + "WHERE  lab22.lab22c1  <  ")
                    .append(order.getNumberOrderSinDate())
                    .append(" and lab39c1 =  ").append(order.getIdTest())
                    .append(" and lab22.lab21c1=  ").append(order.getIdPatient())
                    .append(" and lab57c18 is not null ")
                    .append(" UNION ")
                    .append("SELECT  lab22.lab22c1  AS lab22c1 "
                            + "FROM " + lab57 + " AS lab57 "
                            + "inner join " + lab22 + " as lab22 on lab22.lab22c1 = lab57.lab22c1  "
                            + "WHERE  lab22.lab22c1  <  ")
                    .append(order.getNumberOrderSinDate())
                    .append(" and lab39c1 =  ").append(order.getIdTest())
                    .append(" and lab22.lab21c1=  ").append(order.getIdPatient())
                    .append(" and lab57c18 is not null ")
                    .append(" LIMIT 1 ");
            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getLong("lab22c1");
            });
        } catch (DataAccessException e)
        {
            return 0L;
        }
    }

    /**
     * Obtiene la ultima fecha donde se valido el examen cuando se crea la orden
     *
     * @param order .
     *
     * @throws Exception Error en la base de datos.
     */
    @Override
    public Date lastDateTestValidate(OrderTestPatientHistory order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order.getNumberOrderSinDate()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT  lab57c18 FROM " + lab57 + " as lab57 WHERE lab22c1 = ")
                    .append(order.getNumberOrderLastValidate())
                    .append(" and lab39c1 =  ").append(order.getIdTest())
                    .append(" UNION ")
                    .append(" SELECT  lab57c18 FROM " + lab57 + " WHERE lab22c1 = ")
                    .append(order.getNumberOrderLastValidate())
                    .append(" and lab39c1 =  ").append(order.getIdTest());
            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getTimestamp("lab57c18");
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }
}
