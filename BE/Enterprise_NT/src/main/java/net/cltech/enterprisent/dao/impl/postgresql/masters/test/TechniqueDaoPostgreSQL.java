/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.TechniqueDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.test.Technique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * tecnica para PostgreSQL
 *
 * @version 1.0.0
 * @author MPerdomo
 * @since 18/04/2017
 * @see
 */
@Repository
public class TechniqueDaoPostgreSQL implements TechniqueDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public Technique create(Technique technique) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab64")
                .usingColumns("lab64c2", "lab64c3", "lab64c4", "lab04c1", "lab07c1")
                .usingGeneratedKeyColumns("lab64c1");

        HashMap parameters = new HashMap();
        parameters.put("lab64c2", technique.getCode().trim());
        parameters.put("lab64c3", technique.getName().trim());
        parameters.put("lab64c4", timestamp);
        parameters.put("lab04c1", technique.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        technique.setId(key.intValue());
        technique.setLastTransaction(timestamp);

        return technique;
    }

    @Override
    public List<Technique> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab64c1,lab64c2,lab64c3,lab64c4,lab64.lab04c1,lab04.lab04c2,lab04.lab04c3,lab04.lab04c4,lab64.lab07c1 "
                    + "FROM lab64 "
                    + "INNER JOIN lab04 ON lab64.lab04c1 = lab04.lab04c1 ", (ResultSet rs, int i) ->
            {
                AuthorizedUser user = new AuthorizedUser();

                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                Technique technique = new Technique();
                technique.setId(rs.getInt("lab64c1"));
                technique.setCode(rs.getString("lab64c2"));
                technique.setName(rs.getString("lab64c3"));
                technique.setLastTransaction(rs.getTimestamp("lab64c4"));
                /*Usuario*/
                technique.setUser(user);

                technique.setState(rs.getInt("lab07c1") == 1);

                return technique;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public Technique findById(Integer id) throws Exception
    {
        try
        {
            String sql = "SELECT lab64c1,lab64c2,lab64c3,lab64c4,lab64.lab04c1,lab04.lab04c2,lab04.lab04c3,lab04.lab04c4,lab64.lab07c1 "
                    + "FROM lab64 "
                    + "INNER JOIN lab04 ON lab64.lab04c1 = lab04.lab04c1 "
                    + "WHERE lab64c1 = ?";
            return jdbc.queryForObject(sql, new Object[]
            {
                id
            }, (ResultSet rs, int i) ->
            {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                Technique result = new Technique();
                result.setId(rs.getInt("lab64c1"));
                result.setCode(rs.getString("lab64c2"));
                result.setName(rs.getString("lab64c3"));
                result.setLastTransaction(rs.getTimestamp("lab64c4"));
                result.setUser(user);
                result.setState(rs.getInt("lab07c1") == 1);
                return result;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Technique findByCode(String code) throws Exception
    {
        try
        {
            String sql = "SELECT lab64c1,lab64c2,lab64c3,lab64c4,lab64.lab04c1,lab04.lab04c2,lab04.lab04c3,lab04.lab04c4,lab64.lab07c1 "
                    + "FROM lab64 "
                    + "INNER JOIN lab04 ON lab64.lab04c1 = lab04.lab04c1 "
                    + "WHERE lab64c2 = ?";
            return jdbc.queryForObject(sql, new Object[]
            {
                code
            }, (ResultSet rs, int i) ->
            {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                Technique result = new Technique();
                result.setId(rs.getInt("lab64c1"));
                result.setCode(rs.getString("lab64c2"));
                result.setName(rs.getString("lab64c3"));
                result.setLastTransaction(rs.getTimestamp("lab64c4"));
                result.setUser(user);
                result.setState(rs.getInt("lab07c1") == 1);
                return result;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Technique findByName(String name) throws Exception
    {
        try
        {
            String sql = "SELECT lab64c1,lab64c2,lab64c3,lab64c4,lab64.lab04c1,lab04.lab04c2,lab04.lab04c3,lab04.lab04c4,lab64.lab07c1 "
                    + "FROM lab64 "
                    + "INNER JOIN lab04 ON lab64.lab04c1 = lab04.lab04c1 "
                    + "WHERE lab64c3 = ?";
            return jdbc.queryForObject(sql, new Object[]
            {
                name
            }, (ResultSet rs, int i) ->
            {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                Technique result = new Technique();
                result.setId(rs.getInt("lab64c1"));
                result.setCode(rs.getString("lab64c2"));
                result.setName(rs.getString("lab64c3"));
                result.setLastTransaction(rs.getTimestamp("lab64c4"));
                result.setUser(user);
                result.setState(rs.getInt("lab07c1") == 1);
                return result;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Technique update(Technique technique) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab64 SET lab64c2 = ?, lab64c3 = ?, lab64c4 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab64c1 = ?",
                technique.getCode(), technique.getName(), timestamp, technique.getUser().getId(), technique.isState() ? 1 : 0, technique.getId());

        technique.setLastTransaction(timestamp);

        return technique;
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
