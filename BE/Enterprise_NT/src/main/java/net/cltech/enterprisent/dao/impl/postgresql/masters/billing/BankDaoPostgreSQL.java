package net.cltech.enterprisent.dao.impl.postgresql.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.BankDao;
import net.cltech.enterprisent.domain.masters.billing.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Bancos para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creación
 */
@Repository
public class BankDaoPostgreSQL implements BankDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Bank> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab110c1, lab110c2, lab110c3, lab110.lab04c1, lab04c2, lab04c3, lab04c4, lab110.lab07c1 "
                    + "FROM lab110 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab110.lab04c1", (ResultSet rs, int i) ->
            {
                Bank bank = new Bank();
                bank.setId(rs.getInt("lab110c1"));
                bank.setName(rs.getString("lab110c2"));
                /*Usuario*/
                bank.getUser().setId(rs.getInt("lab04c1"));
                bank.getUser().setName(rs.getString("lab04c2"));
                bank.getUser().setLastName(rs.getString("lab04c3"));
                bank.getUser().setUserName(rs.getString("lab04c4"));

                bank.setLastTransaction(rs.getTimestamp("lab110c3"));
                bank.setState(rs.getInt("lab07c1") == 1);

                return bank;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Bank create(Bank bank) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab110")
                .usingGeneratedKeyColumns("lab110c1");

        HashMap parameters = new HashMap();
        parameters.put("lab110c2", bank.getName().trim());
        parameters.put("lab110c3", timestamp);
        parameters.put("lab04c1", bank.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        bank.setId(key.intValue());
        bank.setLastTransaction(timestamp);

        return bank;
    }

    @Override
    public Bank get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab110c1, lab110c2, lab110c3, lab110.lab04c1, lab04c2, lab04c3, lab04c4, lab110.lab07c1 "
                    + "FROM lab110 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab110.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab110c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab110c2) = ? ";
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

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Bank bank = new Bank();
                bank.setId(rs.getInt("lab110c1"));
                bank.setName(rs.getString("lab110c2"));
                /*Usuario*/
                bank.getUser().setId(rs.getInt("lab04c1"));
                bank.getUser().setName(rs.getString("lab04c2"));
                bank.getUser().setLastName(rs.getString("lab04c3"));
                bank.getUser().setUserName(rs.getString("lab04c4"));

                bank.setLastTransaction(rs.getTimestamp("lab110c3"));
                bank.setState(rs.getInt("lab07c1") == 1);

                return bank;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Bank update(Bank bank) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab110 SET lab110c2 = ?, lab110c3 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab110c1 = ?",
                bank.getName(), timestamp, bank.getUser().getId(), bank.isState() ? 1 : 0, bank.getId());

        bank.setLastTransaction(timestamp);

        return bank;
    }

    @Override
    public void delete(Integer id) throws Exception
    {

    }

}
