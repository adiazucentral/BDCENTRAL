package net.cltech.enterprisent.dao.impl.postgresql.operation.orders;

import java.sql.ResultSet;
import java.util.Date;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.orders.LastOrderPatient;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Implementa los metodos de acceso a datos para PostgreSQL
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creacion
 */
@Repository
public class OrdersDaoPostgreSQL implements OrdersDao
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
    public void addDemographicToPatient(Demographic demographic) throws Exception
    {
        try
        {
            jdbc.queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   1 "
                    + "FROM     information_schema.columns "
                    + "WHERE    table_name='lab21' AND column_name='lab_demo_" + demographic.getId() + "' ", Integer.class);
        } catch (EmptyResultDataAccessException ex)
        {
            jdbc.execute("ALTER TABLE lab21 ADD COLUMN lab_demo_" + demographic.getId() + " " + (demographic.isEncoded() ? "INTEGER" : "VARCHAR(256)"));
        }
    }

    @Override
    public void addDemographicToOrder(Demographic demographic, int yearsQuery) throws Exception
    {
        try
        {
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                try
                {
                    jdbc.queryForObject("" + ISOLATION_READ_UNCOMMITTED
                            + "SELECT   1 "
                            + "FROM     information_schema.columns "
                            + "WHERE    table_name='"+ lab22 +"' AND column_name='lab_demo_" + demographic.getId() + "' ", Integer.class);
                } catch (EmptyResultDataAccessException ex)
                {
                    jdbc.execute("ALTER TABLE "+ lab22 +" ADD COLUMN lab_demo_" + demographic.getId() + " " + (demographic.isEncoded() ? "INTEGER" : "VARCHAR(256)"));
                }
            }
        } catch (DataAccessException e)
        {
            OrderCreationLog.error(e.getMessage());
        }
    }

    //@Override
    public LastOrderPatient getLastOrder(int date, int branch, int patient) throws Exception
    {
        try
        {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(date));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1"
                    + ", lab21.lab21c1"
                    + ", lab54.lab54c1"
                    + ", lab54.lab54c3"
                    + ", lab21.lab21c2"
                    + ", lab21.lab21c3"
                    + ", lab21.lab21c4"
                    + ", lab21.lab21c5"
                    + ", lab21.lab21c6"
                    + ", lab80.lab80c3"
                    + ", lab21.lab21c7"
                    + " FROM  " + lab22 + " as lab22 "
                    + "INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                    + "INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                    + "WHERE lab22.lab07c1 = 1 "
                    + "AND lab22.lab21c1 = ? AND (lab22c19 = 0 or lab22c19 is null) "
                    + "AND lab22.lab22c2 >= ? "
                    + (branch != -1 ? " AND lab22.lab05c1 = " + branch : "")
                    + " ORDER BY lab22c1 DESC LIMIT 1";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        patient, date
                    }, (ResultSet rs, int i) ->
            {
                LastOrderPatient orderR = new LastOrderPatient();
                orderR.setOrderNumber(rs.getLong("lab22c1"));
                orderR.setPatientIdDB(rs.getInt("lab21c1"));
                orderR.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                orderR.setDocumentTypeId(rs.getInt("lab54c1"));
                orderR.setDocumentType(rs.getString("lab54c3"));
                orderR.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                orderR.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                orderR.setName1(Tools.decrypt(rs.getString("lab21c3")));
                orderR.setName2(Tools.decrypt(rs.getString("lab21c4")));
                orderR.setSex(rs.getInt("lab80c3"));
                orderR.setBirthday(rs.getTimestamp("lab21c7"));
                return orderR;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }

}
