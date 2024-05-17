package net.cltech.enterprisent.dao.impl.postgresql.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.LiteralResultDao;
import net.cltech.enterprisent.domain.masters.test.LiteralResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro para
 * PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 22/05/2017
 * @see Creación
 */
@Repository
public class LiteralResultDaoPostgreSQL implements LiteralResultDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<LiteralResult> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab50c1, lab50c2, lab50c3, lab50.lab07c1 "
                    + ", lab50.lab04c1, lab04c2, lab04c3, lab04c4, lab50c4 "
                    + "FROM lab50 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab50.lab04c1", (ResultSet rs, int i) ->
            {
                LiteralResult bean = new LiteralResult();
                bean.setId(rs.getInt("lab50c1"));
                bean.setName(rs.getString("lab50c2"));
                bean.setLastTransaction(rs.getTimestamp("lab50c3"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setNameEnglish(rs.getString("lab50c4"));

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
    public LiteralResult create(LiteralResult newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab50")
                .usingColumns("lab50c2", "lab50c3", "lab04c1", "lab07c1", "lab50c4")
                .usingGeneratedKeyColumns("lab50c1");

        HashMap parameters = new HashMap();
        parameters.put("lab50c2", newBean.getName().trim());
        parameters.put("lab50c3", timestamp);
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab50c4", newBean.getNameEnglish().trim());

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);
        newBean.setState(true);

        return newBean;
    }

    @Override
    public LiteralResult filterById(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab50c1, lab50c2, lab50c3, lab50.lab07c1 "
                    + ",lab50.lab04c1, lab04c2, lab04c3, lab04c4, lab50c4 "
                    + "FROM lab50 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab50.lab04c1 "
                    + "WHERE lab50c1 = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                LiteralResult bean = new LiteralResult();

                bean.setId(rs.getInt("lab50c1"));
                bean.setName(rs.getString("lab50c2"));
                bean.setLastTransaction(rs.getTimestamp("lab50c3"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setNameEnglish(rs.getString("lab50c4"));

                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public LiteralResult filterByName(String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab50c1, lab50c2, lab50c3, lab50.lab07c1 "
                    + ",lab50.lab04c1, lab04c2, lab04c3, lab04c4, lab50c4 "
                    + "FROM lab50 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab50.lab04c1 "
                    + "WHERE lower(lab50c2) = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        name.toLowerCase().trim()
                    }, (ResultSet rs, int i) ->
            {
                LiteralResult bean = new LiteralResult();
                bean.setId(rs.getInt("lab50c1"));
                bean.setName(rs.getString("lab50c2"));
                bean.setLastTransaction(rs.getTimestamp("lab50c3"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setNameEnglish(rs.getString("lab50c4"));

                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public LiteralResult update(LiteralResult update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab50 SET lab50c2 = ?, lab50c3 = ?, lab04c1 = ?, lab07c1 = ?, lab50c4 = ? "
                + "WHERE lab50c1 = ?",
                update.getName().trim(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getNameEnglish(), update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

}
