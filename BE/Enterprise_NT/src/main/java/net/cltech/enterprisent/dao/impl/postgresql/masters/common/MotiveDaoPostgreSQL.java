package net.cltech.enterprisent.dao.impl.postgresql.masters.common;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.common.MotiveDao;
import net.cltech.enterprisent.domain.masters.common.Motive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Motivos para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/06/2017
 * @see Creación
 */
@Repository
public class MotiveDaoPostgreSQL implements MotiveDao
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
    public List<Motive> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab30c1, lab30c2, lab30c3, lab30c5, lab30.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab30.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab30 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab30.lab30c4 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab30.lab04c1", (ResultSet rs, int i) ->
            {
                Motive motive = new Motive();
                motive.setId(rs.getInt("lab30c1"));
                motive.setName(rs.getString("lab30c2"));
                motive.setDescription(rs.getString("lab30c3"));

                /*Tipo*/
                motive.getType().setId(rs.getInt("lab80c1"));
                motive.getType().setIdParent(rs.getInt("lab80c2"));
                motive.getType().setCode(rs.getString("lab80c3"));
                motive.getType().setEsCo(rs.getString("lab80c4"));
                motive.getType().setEnUsa(rs.getString("lab80c5"));

                motive.setLastTransaction(rs.getTimestamp("lab30c5"));
                /*Usuario*/
                motive.getUser().setId(rs.getInt("lab04c1"));
                motive.getUser().setName(rs.getString("lab04c2"));
                motive.getUser().setLastName(rs.getString("lab04c3"));
                motive.getUser().setUserName(rs.getString("lab04c4"));

                motive.setState(rs.getInt("lab07c1") == 1);

                return motive;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Motive create(Motive motive) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab30")
                .usingColumns("lab30c2", "lab30c3", "lab30c4", "lab30c5", "lab04c1", "lab07c1")
                .usingGeneratedKeyColumns("lab30c1");

        HashMap parameters = new HashMap();
        parameters.put("lab30c2", motive.getName().trim());
        parameters.put("lab30c3", motive.getDescription().trim());
        parameters.put("lab30c4", motive.getType().getId());
        parameters.put("lab30c5", timestamp);
        parameters.put("lab04c1", motive.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        motive.setId(key.intValue());
        motive.setLastTransaction(timestamp);

        return motive;
    }

    @Override
    public Motive get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab30c1, lab30c2, lab30c3, lab30c5, lab30.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab30.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab30 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab30.lab30c4 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab30.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab30c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab30c2) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Motive motive = new Motive();
                motive.setId(rs.getInt("lab30c1"));
                motive.setName(rs.getString("lab30c2"));
                motive.setDescription(rs.getString("lab30c3"));

                /*Tipo*/
                motive.getType().setId(rs.getInt("lab80c1"));
                motive.getType().setIdParent(rs.getInt("lab80c2"));
                motive.getType().setCode(rs.getString("lab80c3"));
                motive.getType().setEsCo(rs.getString("lab80c4"));
                motive.getType().setEnUsa(rs.getString("lab80c5"));

                motive.setLastTransaction(rs.getTimestamp("lab30c5"));
                /*Usuario*/
                motive.getUser().setId(rs.getInt("lab04c1"));
                motive.getUser().setName(rs.getString("lab04c2"));
                motive.getUser().setLastName(rs.getString("lab04c3"));
                motive.getUser().setUserName(rs.getString("lab04c4"));

                motive.setState(rs.getInt("lab07c1") == 1);

                return motive;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Motive update(Motive motive) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab30 SET lab30c2 = ?, lab30c3 = ?, lab30c4 = ?, lab30c5 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab30c1 = ?",
                motive.getName(), motive.getDescription(), motive.getType().getId(), timestamp, motive.getUser().getId(), motive.isState() ? 1 : 0, motive.getId());

        motive.setLastTransaction(timestamp);

        return motive;
    }

    @Override
    public void delete(Integer id) throws Exception
    {

    }
    
    @Override
    public List<Motive> listMotivePathology() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab30c1, lab30c2, lab30c3, lab30c5, lab30.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab30.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab30 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab30.lab30c4 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab30.lab04c1 "
                    + "WHERE lab80c1 = 63 ", (ResultSet rs, int i) ->
            {
                Motive motive = new Motive();
                motive.setId(rs.getInt("lab30c1"));
                motive.setName(rs.getString("lab30c2"));
                motive.setDescription(rs.getString("lab30c3"));

                /*Tipo*/
                motive.getType().setId(rs.getInt("lab80c1"));
                motive.getType().setIdParent(rs.getInt("lab80c2"));
                motive.getType().setCode(rs.getString("lab80c3"));
                motive.getType().setEsCo(rs.getString("lab80c4"));
                motive.getType().setEnUsa(rs.getString("lab80c5"));

                motive.setLastTransaction(rs.getTimestamp("lab30c5"));
                /*Usuario*/
                motive.getUser().setId(rs.getInt("lab04c1"));
                motive.getUser().setName(rs.getString("lab04c2"));
                motive.getUser().setLastName(rs.getString("lab04c3"));
                motive.getUser().setUserName(rs.getString("lab04c4"));

                motive.setState(rs.getInt("lab07c1") == 1);

                return motive;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
}
