package net.cltech.enterprisent.dao.impl.postgresql.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.RequirementDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementacion de el acceso a datos para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/04/2017
 * @see Creación
 */
@Repository
public class RequirementDaoPostgreSQL implements RequirementDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public Requirement findById(Integer id) throws Exception
    {
        try
        {

            String sql = "SELECT lab41c1,lab41c2,lab41c3,lab41c4,lab41.lab07c1,lab04.lab04c1,lab04c2,lab04c3,lab04c4 FROM lab41 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab41.lab04c1 "
                    + " WHERE lab41c1 = ?";
            return jdbc.queryForObject(sql, new Object[]
            {
                id
            }, (ResultSet rs, int i) ->
            {
                Requirement result = new Requirement();

                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                result.setId(rs.getInt("lab41c1"));
                result.setCode(rs.getString("lab41c2"));
                result.setRequirement(rs.getString("lab41c3"));
                result.setState(rs.getInt("lab07c1") == 1);
                result.setLastTransaction(rs.getTimestamp("lab41c4"));
                result.setUser(user);
                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }

    }

    @Override
    public Requirement create(Requirement requirement) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab41")
                .usingColumns("lab41c2", "lab41c3", "lab41c4", "lab04c1", "lab07c1")
                .usingGeneratedKeyColumns("lab41c1");

        Date modificationDate = new Date();
        HashMap params = new HashMap();
        params.put("lab41c2", requirement.getCode().trim());
        params.put("lab41c3", requirement.getRequirement().trim());
        params.put("lab41c4", new Timestamp(modificationDate.getTime()));
        params.put("lab04c1", requirement.getUser().getId());
        params.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(params);

        requirement.setId(key.intValue());
        requirement.setLastTransaction(modificationDate);

        return requirement;
    }

    @Override
    public Requirement findByCode(String code) throws Exception
    {
        try
        {
            String sql = "SELECT lab41c1,lab41c2,lab41c3,lab41c4,lab41.lab07c1,lab04.lab04c1,lab04c2,lab04c3,lab04c4 FROM lab41 "
                    + " INNER JOIN lab04 ON lab04.lab04c1 = lab41.lab04c1  "
                    + " WHERE lower(lab41c2) = ?";
            return jdbc.queryForObject(sql, new Object[]
            {
                code.toLowerCase()
            }, (ResultSet rs, int i) ->
            {
                Requirement result = new Requirement();

                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                result.setId(rs.getInt("lab41c1"));
                result.setCode(rs.getString("lab41c2"));
                result.setRequirement(rs.getString("lab41c3"));
                result.setState(rs.getInt("lab07c1") == 1);
                result.setLastTransaction(rs.getTimestamp("lab41c4"));
                result.setUser(user);
                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Requirement update(Requirement requirement) throws Exception
    {
        Date modificationDate = new Date();

        jdbc.update(" UPDATE lab41 \n"
                + " SET lab41c2 = ?, lab41c3 = ?, lab41c4 = ?, lab07c1 = ?, lab04c1 = ? \n"
                + " WHERE lab41c1 = ? ",
                requirement.getCode().trim(), requirement.getRequirement().trim(), new Timestamp(modificationDate.getTime()), requirement.isState() ? 1 : 0,
                requirement.getUser().getId(), requirement.getId());

        requirement.setLastTransaction(modificationDate);
        return requirement;
    }

    @Override
    public List<Requirement> list() throws Exception
    {
        try
        {
            String sql = "SELECT lab41c1,lab41c2,lab41c3,lab41c4,lab41.lab07c1, lab04.lab04c1,lab04c2,lab04c3,lab04c4 "
                    + " FROM lab41 "
                    + " INNER JOIN lab04 ON lab04.lab04c1 = lab41.lab04c1";
            return jdbc.query(sql, (ResultSet rs, int i) ->
            {
                Requirement result = new Requirement();
                AuthorizedUser user = new AuthorizedUser();

                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                result.setId(rs.getInt("lab41c1"));
                result.setCode(rs.getString("lab41c2"));
                result.setRequirement(rs.getString("lab41c3"));
                result.setState(rs.getInt("lab07c1") == 1);
                result.setLastTransaction(rs.getTimestamp("lab41c4"));
                result.setUser(user);

                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

}
