package net.cltech.enterprisent.dao.impl.postgresql.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.OrderTypeDao;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 16/05/2017
 * @see Creación
 */
@Repository
public class OrderTypeDaoPostgreSQL implements OrderTypeDao
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

    @Override
    public List<OrderType> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103c5, lab103.lab07c1, lab103.lab103c6, lab103.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab103 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab103.lab04c1", (ResultSet rs, int i) ->
            {
                OrderType bean = new OrderType();
                bean.setId(rs.getInt("lab103c1"));
                bean.setCode(rs.getString("lab103c2"));
                bean.setName(rs.getString("lab103c3"));
                bean.setColor(rs.getString("lab103c4") == null ? null : rs.getString("lab103c4"));
                bean.setLastTransaction(rs.getTimestamp("lab103c5"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setEmail(rs.getString("lab103c6"));

                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public OrderType create(OrderType newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab103")
                .usingColumns("lab103c2", "lab103c3", "lab103c4", "lab103c5", "lab04c1", "lab07c1", "lab103c6")
                .usingGeneratedKeyColumns("lab103c1");

        HashMap parameters = new HashMap();
        parameters.put("lab103c2", newBean.getCode().trim());
        parameters.put("lab103c3", newBean.getName().trim());
        parameters.put("lab103c4", newBean.getColor());
        parameters.put("lab103c5", timestamp);
        parameters.put("lab103c6", newBean.getEmail());
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);
        newBean.setState(true);

        return newBean;
    }

    @Override
    public OrderType filterById(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103c5, lab103.lab07c1, lab103.lab04c1, lab103.lab103c6, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab103 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab103.lab04c1 "
                    + "WHERE lab103.lab103c1 = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                OrderType found = new OrderType();
                found.setId(rs.getInt("lab103c1"));
                found.setCode(rs.getString("lab103c2"));
                found.setName(rs.getString("lab103c3"));
                found.setColor(rs.getString("lab103c4"));
                found.setLastTransaction(rs.getTimestamp("lab103c5"));
                found.setState(rs.getInt("lab07c1") == 1);
                found.setEmail(rs.getString("lab103c6"));

                /*Usuario*/
                found.getUser().setId(rs.getInt("lab04c1"));
                found.getUser().setName(rs.getString("lab04c2"));
                found.getUser().setLastName(rs.getString("lab04c3"));
                found.getUser().setUserName(rs.getString("lab04c4"));

                return found;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public OrderType filterByName(String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103c5, lab103.lab07c1, lab103.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab103 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab103.lab04c1 "
                    + "WHERE lower(lab103c3) = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        name.toLowerCase()
                    }, (ResultSet rs, int i) ->
            {
                OrderType found = new OrderType();
                found.setId(rs.getInt("lab103c1"));
                found.setCode(rs.getString("lab103c2"));
                found.setName(rs.getString("lab103c3"));
                found.setColor(rs.getString("lab103c4"));
                found.setLastTransaction(rs.getTimestamp("lab103c5"));
                found.setState(rs.getInt("lab07c1") == 1);

                /*Usuario*/
                found.getUser().setId(rs.getInt("lab04c1"));
                found.getUser().setName(rs.getString("lab04c2"));
                found.getUser().setLastName(rs.getString("lab04c3"));
                found.getUser().setUserName(rs.getString("lab04c4"));

                return found;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public OrderType update(OrderType update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab103 SET lab103c2 = ?, lab103c3 = ?, lab103c4 = ?, lab103c5 = ?, lab04c1 = ?, lab07c1 = ? , lab103c6 = ? "
                + "WHERE lab103c1 = ?",
                update.getCode().trim(), update.getName().trim(), update.getColor(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getEmail(), update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

}
