package net.cltech.enterprisent.dao.impl.sqlserver.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.LaboratoryDao;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro para
 * SQLServer
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/05/2017
 * @see Creación
 */
@Repository
public class LaboratoryDaoSQLServer implements LaboratoryDao
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
    public List<Laboratory> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab40c1, lab40c2, lab40c3, lab40c4, lab40c5, lab40c6,"
                    + "lab40c7, lab40c8, lab40c9, lab40c10, lab40c11, lab40c12, lab40c13, lab40.lab07c1, "
                    + "lab40.lab04c1, lab04c2, lab04c3, lab04c4, "
                    + "lab40.lab05c1 AS winery "
                    + "FROM lab40 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab40.lab04c1", (ResultSet rs, int i) ->
            {
                Laboratory bean = new Laboratory();
                bean.setId(rs.getInt("lab40c1"));
                bean.setCode(rs.getInt("lab40c2"));
                bean.setName(rs.getString("lab40c3"));
                bean.setAddress(rs.getString("lab40c4"));
                bean.setPhone(rs.getString("lab40c5"));
                bean.setContact(rs.getString("lab40c6"));
                bean.setType(rs.getShort("lab40c7"));
                bean.setPath(rs.getString("lab40c8"));
                bean.setLastTransaction(rs.getTimestamp("lab40c9"));
                bean.setUrl(rs.getString("lab40c10") == null ? "" : rs.getString("lab40c10"));
                bean.setEntry(rs.getBoolean("lab40c11"));
                bean.setCheck(rs.getBoolean("lab40c12"));
                OrderCreationLog.info("bean is check: " + bean.isCheck());

                bean.setMiddleware(rs.getBoolean("lab40c13"));
                bean.setState(rs.getInt("lab07c1") == 1);
                OrderCreationLog.info("bean is State : " + bean.isState());
                bean.setWinery(rs.getInt("winery"));

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
    public Laboratory create(Laboratory newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab40")
                .usingColumns("lab40c2", "lab40c3", "lab40c4", "lab40c5", "lab40c6", "lab40c7", "lab40c8", "lab40c9", "lab40c10", "lab40c11", "lab40c12", "lab40c13", "lab04c1", "lab07c1", "lab05c1")
                .usingGeneratedKeyColumns("lab40c1");

        HashMap parameters = new HashMap();
        parameters.put("lab40c2", newBean.getCode());
        parameters.put("lab40c3", newBean.getName().trim());
        parameters.put("lab40c4", newBean.getAddress());
        parameters.put("lab40c5", newBean.getPhone());
        parameters.put("lab40c6", newBean.getContact());
        parameters.put("lab40c7", newBean.getType());
        parameters.put("lab40c8", newBean.getPath());
        parameters.put("lab40c9", timestamp);
        parameters.put("lab40c10", newBean.getUrl());
        parameters.put("lab40c11", newBean.isEntry() ? 1 : 0);
        parameters.put("lab40c12", newBean.isCheck() ? 1 : 0);
        parameters.put("lab40c13", newBean.isMiddleware() ? 1 : 0);
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab05c1", newBean.getWinery());

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);

        return newBean;
    }

    @Override
    public Laboratory filterById(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab40c1, lab40c2, lab40c3, lab40c4, lab40c5, lab40c6 "
                    + ",lab40c7, lab40c8, lab40c9, lab40c10, lab40c11, lab40c12, lab40c13, lab40.lab07c1 "
                    + ",lab40.lab04c1, lab04c2, lab04c3, lab04c4,"
                    + "lab40.lab05c1 AS winery "
                    + "FROM lab40 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab40.lab04c1 "
                    + "WHERE lab40c1 = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                Laboratory bean = new Laboratory();
                bean.setId(rs.getInt("lab40c1"));
                bean.setCode(rs.getInt("lab40c2"));
                bean.setName(rs.getString("lab40c3"));
                bean.setAddress(rs.getString("lab40c4"));
                bean.setPhone(rs.getString("lab40c5"));
                bean.setContact(rs.getString("lab40c6"));
                bean.setType(rs.getShort("lab40c7"));
                bean.setPath(rs.getString("lab40c8"));
                bean.setLastTransaction(rs.getTimestamp("lab40c9"));
                bean.setUrl(rs.getString("lab40c10") == null ? "" : rs.getString("lab40c10"));
                bean.setEntry(rs.getBoolean("lab40c11"));
                bean.setCheck(rs.getBoolean("lab40c12"));
                bean.setMiddleware(rs.getBoolean("lab40c13"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setWinery(rs.getInt("winery"));

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
    public Laboratory filterByName(String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab40c1, lab40c2, lab40c3, lab40c4, lab40c5, lab40c6 "
                    + ",lab40c7, lab40c8, lab40c9, lab40c10, lab40c11, lab40c12, lab40c13, lab40.lab07c1 "
                    + ",lab40.lab04c1, lab04c2, lab04c3, lab04c4, "
                    + "lab40.lab05c1 AS winery "
                    + "FROM lab40 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab40.lab04c1 "
                    + "WHERE lower(lab40c3) = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        name.toLowerCase().trim()
                    }, (ResultSet rs, int i) ->
            {
                Laboratory bean = new Laboratory();
                bean.setId(rs.getInt("lab40c1"));
                bean.setCode(rs.getInt("lab40c2"));
                bean.setName(rs.getString("lab40c3"));
                bean.setAddress(rs.getString("lab40c4"));
                bean.setPhone(rs.getString("lab40c5"));
                bean.setContact(rs.getString("lab40c6"));
                bean.setType(rs.getShort("lab40c7"));
                bean.setPath(rs.getString("lab40c8"));
                bean.setLastTransaction(rs.getTimestamp("lab40c9"));
                bean.setUrl(rs.getString("lab40c10") == null ? "" : rs.getString("lab40c10"));
                bean.setEntry(rs.getBoolean("lab40c11"));
                bean.setCheck(rs.getBoolean("lab40c12"));
                bean.setMiddleware(rs.getBoolean("lab40c13"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setWinery(rs.getInt("winery"));

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
    public Laboratory update(Laboratory update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab40 SET lab40c2 = ?, "
                + "lab40c3 = ?, "
                + "lab40c4 = ?, "
                + "lab40c5 = ?, "
                + "lab40c6 = ?, "
                + "lab40c7 = ?, "
                + "lab40c8 = ?, "
                + "lab40c9 = ?, "
                + "lab40c10 = ?, "
                + "lab40c11 = ?, "
                + "lab40c12 = ?, "
                + "lab40c13 = ?, "
                + "lab04c1 = ?, "
                + "lab07c1 = ?, "
                + "lab05c1 = ? "
                + "WHERE lab40c1 = ?",
                update.getCode(),
                update.getName().trim(),
                update.getAddress(),
                update.getPhone(),
                update.getContact(),
                update.getType(),
                update.getPath(),
                timestamp,
                update.getUrl(),
                update.isEntry() ? 1 : 0,
                update.isCheck() ? 1 : 0,
                update.isMiddleware() ? 1 : 0,
                update.getUser().getId(),
                update.isState() ? 1 : 0,
                update.getWinery(),
                update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

}
