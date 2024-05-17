package net.cltech.enterprisent.dao.impl.sqlserver.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.ServiceDao;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
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
public class ServiceDaoSQLServer implements ServiceDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<ServiceLaboratory> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab10c1, lab10c2, lab10c3, lab10c4, lab10c5, lab10c6, lab10c7, lab10.lab07c1, lab10c10, lab10.lab04c1, lab04c2, lab04c3, lab04c4, lab10c8, lab10c9 "
                    + "FROM lab10 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab10.lab04c1", (ResultSet rs, int i) ->
            {
                ServiceLaboratory bean = new ServiceLaboratory();
                bean.setId(rs.getInt("lab10c1"));
                bean.setCode(rs.getString("lab10c7").trim());
                bean.setName(rs.getString("lab10c2"));
                bean.setMin(rs.getInt("lab10c3"));
                bean.setMax(rs.getInt("lab10c4"));
                bean.setExternal(rs.getInt("lab10c5") == 1);
                bean.setLastTransaction(rs.getTimestamp("lab10c6"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setEmail(rs.getString("lab10c10"));
                bean.setHospitalSampling(rs.getInt("lab10c8") == 1);
                bean.setPriorityAlarm(rs.getInt("lab10c9") == 1);

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
    public ServiceLaboratory create(ServiceLaboratory newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab10")
                .usingGeneratedKeyColumns("lab10c1");

        HashMap parameters = new HashMap();
        parameters.put("lab10c2", newBean.getName().trim());
        parameters.put("lab10c3", newBean.getMin());
        parameters.put("lab10c4", newBean.getMax());
        parameters.put("lab10c5", newBean.isExternal() ? 1 : 0);
        parameters.put("lab10c6", timestamp);
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab10c7", newBean.getCode().trim());
        parameters.put("lab10c8", newBean.getHospitalSampling() ? 1 : 0);
        parameters.put("lab10c9", newBean.getPriorityAlarm() ? 1 : 0);
        parameters.put("lab10c10", newBean.getEmail());

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);

        return newBean;
    }

    @Override
    public ServiceLaboratory filterById(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab10c1, lab10c2, lab10c3, lab10c4, lab10c5, lab10c6, lab10c7, lab10.lab07c1, lab10.lab04c1, lab04c2, lab04c3, lab04c4, lab10c8, lab10c9 , lab10c10 "
                    + "FROM lab10 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab10.lab04c1 "
                    + "WHERE lab10c1 = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                ServiceLaboratory found = new ServiceLaboratory();
                found.setId(rs.getInt("lab10c1"));
                found.setCode(rs.getString("lab10c7").trim());
                found.setName(rs.getString("lab10c2"));
                found.setMin(rs.getInt("lab10c3"));
                found.setMax(rs.getInt("lab10c4"));
                found.setExternal(rs.getInt("lab10c5") == 1);
                found.setLastTransaction(rs.getTimestamp("lab10c6"));
                found.setState(rs.getInt("lab07c1") == 1);
                found.setHospitalSampling(rs.getInt("lab10c8") == 1);
                found.setPriorityAlarm(rs.getInt("lab10c9") == 1);
                found.setEmail(rs.getString("lab10c10"));

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
    public ServiceLaboratory filterByName(String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab10c1, lab10c2, lab10c3, lab10c4, lab10c5, lab10c6, lab10c7, lab10.lab07c1, lab10.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab10 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab10.lab04c1 "
                    + "WHERE lower(lab10c2) = ? ";

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        name.toLowerCase()
                    }, (ResultSet rs, int i) ->
            {
                ServiceLaboratory found = new ServiceLaboratory();
                found.setId(rs.getInt("lab10c1"));
                found.setCode(rs.getString("lab10c7"));
                found.setName(rs.getString("lab10c2"));
                found.setMin(rs.getInt("lab10c3"));
                found.setMax(rs.getInt("lab10c4"));
                found.setExternal(rs.getInt("lab10c5") == 1);
                found.setLastTransaction(rs.getTimestamp("lab10c6"));
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
    public ServiceLaboratory update(ServiceLaboratory update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab10 SET lab10c2 = ?, lab10c3 = ?, lab10c4 = ?, lab10c5 = ?,lab10c6 = ?, lab10c7 = ?, lab04c1 = ?, lab07c1 = ?, lab10c8 = ?, lab10c9 = ? , lab10c10 = ? "
                + "WHERE lab10c1 = ?",
                update.getName(), update.getMin(), update.getMax(), update.isExternal() ? 1 : 0, timestamp, update.getCode().trim(), update.getUser().getId(), update.isState() ? 1 : 0, update.getHospitalSampling() ? 1 : 0, update.getPriorityAlarm() ? 1 : 0, update.getEmail(), update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
}
