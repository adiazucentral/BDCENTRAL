package net.cltech.enterprisent.dao.impl.postgresql.masters.microbiology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.AntibioticDao;
import net.cltech.enterprisent.domain.masters.microbiology.Antibiotic;
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
 * @since 07/06/2017
 * @see Creación
 */
@Repository
public class AntibioticDaoPostgreSQL implements AntibioticDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Antibiotic> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + " SELECT lab79c1, lab79c2, lab79c3, lab79.lab07c1"
                    + " ,lab79.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab79 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab79.lab04c1", (ResultSet rs, int i) ->
            {
                Antibiotic bean = new Antibiotic();
                bean.setId(rs.getInt("lab79c1"));
                bean.setName(rs.getString("lab79c2"));
                bean.setLastTransaction(rs.getTimestamp("lab79c3"));
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
    public Antibiotic create(Antibiotic newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab79")
                .usingGeneratedKeyColumns("lab79c1");

        HashMap parameters = new HashMap();
        parameters.put("lab79c2", newBean.getName().trim());
        parameters.put("lab79c3", timestamp);
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);
        newBean.setState(true);

        return newBean;
    }

    @Override
    public Antibiotic update(Antibiotic update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab79 SET lab79c2 = ?, lab79c3 = ?,  lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab79c1 = ?",
                update.getName().trim(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

}
