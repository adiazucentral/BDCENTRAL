package net.cltech.enterprisent.dao.impl.sqlserver.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.TaxPrinterDao;
import net.cltech.enterprisent.domain.masters.billing.Bank;
import net.cltech.enterprisent.domain.masters.billing.TaxPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Bancos para SQLServer
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creación
 */
@Repository
public class TaxPrinterSQLServer implements TaxPrinterDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<TaxPrinter> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab914c1, lab914c2, lab914c3, lab914c4, lab914c5,  lab914c6, lab04.lab04c1, lab04c2,lab04c3,lab04c4,lab914.lab07c1 "
                    + "FROM lab914 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab914.lab04c1", (ResultSet rs, int i) ->
            {
                TaxPrinter taxPrinter = new TaxPrinter();
                taxPrinter.setId(rs.getInt("lab914c1"));
                taxPrinter.setCode(rs.getString("lab914c6"));
                taxPrinter.setName(rs.getString("lab914c2"));
                taxPrinter.setClientIp(rs.getString("lab914c4"));
                taxPrinter.setPathXml(rs.getString("lab914c3"));
                /*Usuario*/
                taxPrinter.getUser().setId(rs.getInt("lab04c1"));
                taxPrinter.getUser().setName(rs.getString("lab04c2"));
                taxPrinter.getUser().setLastName(rs.getString("lab04c3"));
                taxPrinter.getUser().setUserName(rs.getString("lab04c4"));

                taxPrinter.setLastTransaction(rs.getTimestamp("lab914c5"));
                taxPrinter.setState(rs.getInt("lab07c1") == 1);

                return taxPrinter;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public TaxPrinter create(TaxPrinter taxPrinter) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab914")
                .usingGeneratedKeyColumns("lab914c1");

        HashMap parameters = new HashMap();
        parameters.put("lab914c2", taxPrinter.getName());
        parameters.put("lab914c6", taxPrinter.getCode());
        parameters.put("lab914c3", taxPrinter.getPathXml());
        parameters.put("lab914c4", taxPrinter.getClientIp());
        parameters.put("lab914c5", timestamp);
        parameters.put("lab04c1", taxPrinter.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        taxPrinter.setId(key.intValue());
        taxPrinter.setLastTransaction(timestamp);

        return taxPrinter;
    }

    @Override
    public TaxPrinter get(Integer id, String name, String code) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab914c1, lab914c6, lab914c2, lab914c3, lab04c3, lab914c4, lab04.lab04c1, lab04c2,lab04c3,lab04c4,lab914c5, lab914.lab07c1 "
                    + "FROM lab914 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab914.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab914c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab914c2) = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(lab914c6) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }
             if (code != null)
            {
                object = code.toUpperCase();
            }

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                TaxPrinter taxPrinter = new TaxPrinter();
                taxPrinter.setId(rs.getInt("lab914c1"));
                taxPrinter.setCode(rs.getString("lab914c6"));
                taxPrinter.setName(rs.getString("lab914c2"));
                taxPrinter.setClientIp(rs.getString("lab914c4"));
                taxPrinter.setPathXml(rs.getString("lab914c3"));
                /*Usuario*/
                taxPrinter.getUser().setId(rs.getInt("lab04c1"));
                taxPrinter.getUser().setName(rs.getString("lab04c2"));
                taxPrinter.getUser().setLastName(rs.getString("lab04c3"));
                taxPrinter.getUser().setUserName(rs.getString("lab04c4"));

                taxPrinter.setLastTransaction(rs.getTimestamp("lab914c5"));
                taxPrinter.setState(rs.getInt("lab07c1") == 1);

                return taxPrinter;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public TaxPrinter update(TaxPrinter taxPrinter) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab914 SET lab914c6 = ?, lab914c2 = ?, lab914c3 = ?, lab914c4 = ?, lab07c1 = ?, lab914c5 = ?, lab04c1 = ? "
                + "WHERE lab914c1 = ?",
                taxPrinter.getCode(),  taxPrinter.getName(), taxPrinter.getPathXml(),taxPrinter.getClientIp(), taxPrinter.isState() ? 1 : 0, timestamp, taxPrinter.getUser().getId(),  taxPrinter.getId());

        taxPrinter.setLastTransaction(timestamp);

        return taxPrinter;
    }

    
}
