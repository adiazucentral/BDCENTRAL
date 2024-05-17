package net.cltech.enterprisent.dao.impl.postgresql.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.PopulationGroupDao;
import net.cltech.enterprisent.domain.masters.test.PopulationGroup;
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
public class PopulationGroupDaoPostgreSQL implements PopulationGroupDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<PopulationGroup> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab67c1, lab67c2, lab67c3, lab67c4, lab67c5, lab67c6,"
                    + "lab67c7, lab67.lab07c1, "
                    + "lab67.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab67 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab67.lab04c1", (ResultSet rs, int i) ->
            {
                PopulationGroup bean = new PopulationGroup();
                bean.setId(rs.getInt("lab67c1"));
                bean.setName(rs.getString("lab67c2"));
                bean.setSex(rs.getInt("lab67c3"));
                bean.setUnit(rs.getInt("lab67c4"));
                bean.setInitialRange(rs.getInt("lab67c5"));
                bean.setFinalRange(rs.getInt("lab67c6"));
                bean.setLastTransaction(rs.getTimestamp("lab67c7"));
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
    public PopulationGroup create(PopulationGroup newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab67")
                .usingColumns("lab67c2", "lab67c3", "lab67c4", "lab67c5", "lab67c6", "lab67c7", "lab04c1", "lab07c1")
                .usingGeneratedKeyColumns("lab67c1");

        HashMap parameters = new HashMap();
        parameters.put("lab67c2", newBean.getName().trim());
        parameters.put("lab67c3", newBean.getSex());
        parameters.put("lab67c4", newBean.getUnit());
        parameters.put("lab67c5", newBean.getInitialRange());
        parameters.put("lab67c6", newBean.getFinalRange());
        parameters.put("lab67c7", timestamp);
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);
        newBean.setState(true);

        return newBean;
    }

    @Override
    public PopulationGroup filterById(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab67c1, lab67c2, lab67c3, lab67c4, lab67c5, lab67c6 "
                    + ",lab67c7, lab67.lab07c1 "
                    + ",lab67.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab67 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab67.lab04c1 "
                    + "WHERE lab67c1 = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                PopulationGroup bean = new PopulationGroup();

                bean.setId(rs.getInt("lab67c1"));
                bean.setName(rs.getString("lab67c2"));
                bean.setSex(rs.getInt("lab67c3"));
                bean.setUnit(rs.getInt("lab67c4"));
                bean.setInitialRange(rs.getInt("lab67c5"));
                bean.setFinalRange(rs.getInt("lab67c6"));
                bean.setLastTransaction(rs.getTimestamp("lab67c7"));
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
            return null;
        }
    }

    @Override
    public PopulationGroup filterByName(String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab67c1, lab67c2, lab67c3, lab67c4, lab67c5, lab67c6 "
                    + ",lab67c7, lab67.lab07c1 "
                    + ",lab67.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab67 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab67.lab04c1 "
                    + "WHERE lower(lab67c2) = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        name.toLowerCase().trim()
                    }, (ResultSet rs, int i) ->
            {
                PopulationGroup bean = new PopulationGroup();
                bean.setId(rs.getInt("lab67c1"));
                bean.setName(rs.getString("lab67c2"));
                bean.setSex(rs.getInt("lab67c3"));
                bean.setUnit(rs.getInt("lab67c4"));
                bean.setInitialRange(rs.getInt("lab67c5"));
                bean.setFinalRange(rs.getInt("lab67c6"));
                bean.setLastTransaction(rs.getTimestamp("lab67c7"));
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
            return null;
        }
    }

    @Override
    public PopulationGroup update(PopulationGroup update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab67 SET lab67c2 = ?, lab67c3 = ?, lab67c4 = ?, lab67c5 = ?,lab67c6 = ?,lab67c7 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab67c1 = ?",
                update.getName().trim(), update.getSex(), update.getUnit(), update.getInitialRange(), update.getFinalRange(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

}
