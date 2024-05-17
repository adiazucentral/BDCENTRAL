package net.cltech.enterprisent.dao.impl.postgresql.masters.test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.UnitDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.test.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementacion de el acceso a datos de informacion de la base de
 * datos para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/04/2017
 * @see Creación
 */
@Repository
public class UnitDaoPostgreSQL implements UnitDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public Unit create(Unit unit) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab45")
                .usingColumns("lab45c2", "lab45c3", "lab45c4", "lab45c5", "lab04c1", "lab07c1")
                .usingGeneratedKeyColumns("lab45c1");

        Date modificationDate = new Date();
        HashMap params = new HashMap();
        params.put("lab45c2", unit.getName().trim());
        params.put("lab45c3", unit.getInternational() == null ? null : unit.getInternational().trim());
        params.put("lab45c4", unit.getConversionFactor());
        params.put("lab45c5", new Timestamp(modificationDate.getTime()));
        params.put("lab04c1", unit.getUser().getId());
        params.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(params);

        unit.setId(key.intValue());
        unit.setLastTransaction(modificationDate);

        return unit;

    }

    @Override
    public List<Unit> list() throws Exception
    {
        try
        {
            String sql = "SELECT lab45c1,lab45c2,lab45c3,lab45c4,lab45c5,lab45.lab07c1, lab04.lab04c1,lab04c2,lab04c3,lab04c4 FROM lab45 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab45.lab04c1";
            return jdbc.query(sql, (ResultSet rs, int i) ->
            {
                Unit result = new Unit();
                AuthorizedUser user = new AuthorizedUser();

                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                result.setId(rs.getInt("lab45c1"));
                result.setName(rs.getString("lab45c2"));
                result.setInternational(rs.getString("lab45c3"));
                result.setConversionFactor(rs.getString("lab45c4") == null ? null : rs.getBigDecimal("lab45c4"));
                result.setState(rs.getInt("lab07c1") == 1);
                result.setLastTransaction(rs.getTimestamp("lab45c5"));
                result.setUser(user);

                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }

    }

    @Override
    public Unit findById(Integer id) throws Exception
    {
        try
        {

            String sql = "SELECT lab45c1,lab45c2,lab45c3,lab45c4,lab45c5,lab45.lab07c1,lab04.lab04c1,lab04c2,lab04c3,lab04c4 FROM lab45 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab45.lab04c1 "
                    + " WHERE lab45c1 = ?";
            return jdbc.queryForObject(sql, new Object[]
            {
                id
            }, (ResultSet rs, int i) ->
            {
                Unit result = new Unit();

                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                result.setId(rs.getInt("lab45c1"));
                result.setName(rs.getString("lab45c2"));
                result.setInternational(rs.getString("lab45c3"));
                result.setConversionFactor(rs.getBigDecimal("lab45c4"));
                result.setState(rs.getInt("lab07c1") == 1);
                result.setLastTransaction(rs.getTimestamp("lab45c5"));
                result.setUser(user);
                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }

    }

    @Override
    public Unit findByName(String name) throws Exception
    {

        try
        {
            String sql = "SELECT lab45c1,lab45c2,lab45c3,lab45c4,lab45c5,lab45.lab07c1,lab04.lab04c1,lab04c2,lab04c3,lab04c4 FROM lab45 "
                    + " INNER JOIN lab04 ON lab04.lab04c1 = lab45.lab04c1  "
                    + " WHERE lower(lab45c2) = ?";
            return jdbc.queryForObject(sql, new Object[]
            {
                name.toLowerCase()
            }, (ResultSet rs, int i) ->
            {
                Unit result = new Unit();

                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                result.setId(rs.getInt("lab45c1"));
                result.setName(rs.getString("lab45c2"));
                result.setInternational(rs.getString("lab45c3"));
                result.setConversionFactor(rs.getBigDecimal("lab45c4"));
                result.setState(rs.getInt("lab07c1") == 1);
                result.setLastTransaction(rs.getTimestamp("lab45c5"));
                result.setUser(user);
                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }

    }

    @Override
    public Unit update(Unit update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        jdbc.update(" UPDATE lab45 \n"
                + " SET lab45c2 = ?, lab45c3 = ?, lab45c4 = ?, lab45c5 = ?, lab07c1 = ?, lab04c1 = ? \n"
                + " WHERE lab45c1 = ? ",
                update.getName().trim(), update.getInternational() == null ? null : update.getInternational().trim(), update.getConversionFactor(), timestamp, update.isState() ? 1 : 0,
                update.getUser().getId(), update.getId());
        update.setLastTransaction(timestamp);
        return update;

    }

    @Override
    public void delete(Integer id) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
