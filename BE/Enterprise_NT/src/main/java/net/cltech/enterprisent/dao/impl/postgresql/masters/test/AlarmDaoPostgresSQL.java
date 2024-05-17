package net.cltech.enterprisent.dao.impl.postgresql.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.test.AlarmDao;
import net.cltech.enterprisent.domain.masters.test.Alarm;
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
 * @since 06/06/2017
 * @see Creación
 */
@Repository
public class AlarmDaoPostgresSQL implements AlarmDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Alarm> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + " SELECT lab73c1, lab73c2, lab73c3, lab73c4, lab73.lab07c1"
                    + " ,lab73.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab73 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab73.lab04c1", (ResultSet rs, int i) ->
            {
                Alarm bean = new Alarm();
                bean.setId(rs.getInt("lab73c1"));
                bean.setName(rs.getString("lab73c2"));
                bean.setDescription(rs.getString("lab73c3"));
                bean.setLastTransaction(rs.getTimestamp("lab73c4"));
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
    public Alarm create(Alarm newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab73")
                .usingGeneratedKeyColumns("lab73c1");

        HashMap parameters = new HashMap();
        parameters.put("lab73c2", newBean.getName().trim());
        parameters.put("lab73c3", newBean.getDescription().trim());
        parameters.put("lab73c4", timestamp);
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);
        newBean.setState(true);

        return newBean;
    }

    @Override
    public Alarm update(Alarm update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab73 SET lab73c2 = ?, lab73c3 = ?, lab73c4 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab73c1 = ?",
                update.getName().trim(), update.getDescription().trim(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

}
