package net.cltech.enterprisent.dao.impl.sqlserver.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.SpecialistDao;
import net.cltech.enterprisent.domain.masters.demographic.Specialist;
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
 * @since 11/05/2017
 * @see Creación
 */
@Repository
public class SpecialistDaoSQLServer implements SpecialistDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Specialist> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab09c1, lab09c2, lab09c3,  lab09.lab07c1, lab09.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab09 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab09.lab04c1", (ResultSet rs, int i) ->
            {
                Specialist bean = new Specialist();
                bean.setId(rs.getInt("lab09c1"));
                bean.setName(rs.getString("lab09c2"));
                bean.setLastTransaction(rs.getTimestamp("lab09c3"));
                bean.setState(rs.getInt("lab07c1") == 1);

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
    public Specialist create(Specialist newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab09")
                .usingColumns("lab09c2", "lab09c3", "lab04c1", "lab07c1")
                .usingGeneratedKeyColumns("lab09c1");

        HashMap parameters = new HashMap();
        parameters.put("lab09c2", newBean.getName().trim());
        parameters.put("lab09c3", timestamp);
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);

        return newBean;
    }

    @Override
    public Specialist filterById(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab09c1, lab09c2, lab09c3, lab09.lab07c1, lab09.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab09 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab09.lab04c1 "
                    + "WHERE lab09c1 = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                Specialist found = new Specialist();
                found.setId(rs.getInt("lab09c1"));
                found.setName(rs.getString("lab09c2"));
                found.setLastTransaction(rs.getTimestamp("lab09c3"));
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
    public Specialist filterByName(String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab09c1, lab09c2, lab09c3, lab09.lab07c1, lab09.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab09 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab09.lab04c1 "
                    + "WHERE lower(lab09c2) = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        name.toLowerCase()
                    }, (ResultSet rs, int i) ->
            {
                Specialist found = new Specialist();
                found.setId(rs.getInt("lab09c1"));
                found.setName(rs.getString("lab09c2"));
                found.setLastTransaction(rs.getTimestamp("lab09c3"));
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
    public Specialist update(Specialist update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab09 SET lab09c2 = ?, lab09c3 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab09c1 = ?",
                update.getName(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

}
