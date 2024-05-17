package net.cltech.enterprisent.dao.impl.postgresql.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Demografico para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/05/2017
 * @see Creación
 */
@Repository
public class DemographicDaoPostgreSQL implements DemographicDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public Demographic create(Demographic demographic) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab62")
                .usingColumns("lab62c2", "lab62c3", "lab62c4", "lab62c5", "lab62c6", "lab62c7", "lab62c8", "lab62c9", "lab62c10", "lab62c11", "lab62c12", "lab04c1", "lab07c1", "lab62c13", "lab62c14", "lab62c15", "lab63c1")
                .usingGeneratedKeyColumns("lab62c1");

        HashMap parameters = new HashMap();
        parameters.put("lab62c2", demographic.getName());
        parameters.put("lab62c3", demographic.getOrigin());
        parameters.put("lab62c4", demographic.isEncoded() ? 1 : 0);
        parameters.put("lab62c5", demographic.getObligatory());
        parameters.put("lab62c6", demographic.getOrdering());
        parameters.put("lab62c7", demographic.getFormat());
        parameters.put("lab62c8", demographic.getDefaultValue());
        parameters.put("lab62c9", demographic.isStatistics() ? 1 : 0);
        parameters.put("lab62c10", demographic.isLastOrder() ? 1 : 0);
        parameters.put("lab62c11", demographic.isCanCreateItemInOrder() ? 1 : 0);
        parameters.put("lab62c12", timestamp);
        parameters.put("lab04c1", demographic.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab62c13", demographic.isModify() ? 1 : 0);
        parameters.put("lab62c14", demographic.getDefaultValueRequired());
        parameters.put("lab62c15", demographic.isPromiseTime()? 1 : 0);
        parameters.put("lab63c1", null);

        Number key = insert.executeAndReturnKey(parameters);
        demographic.setId(key.intValue());
        demographic.setLastTransaction(timestamp);

        return demographic;
    }

    @Override
    public Demographic get(Integer id, String name, Integer sort) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab62c1, lab62c2, lab62c3, lab62c4, lab62c5, lab62c6, lab62c7, lab62c8, lab62c9, lab62c10, lab62c11, lab62c12, lab62.lab07c1, lab62.lab04c1, lab04c2, lab04c3, lab04c4, lab62c13, lab62c14, lab62c15, lab63c1 "
                    + "FROM lab62 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab62.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab62c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab62c2) = ? ";
            }
            if (sort != null)
            {
                query = query + "WHERE lab62c6 = ? ";
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
            if (sort != null)
            {
                object = sort;
            }

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setName(rs.getString("lab62c2"));
                demographic.setOrigin(rs.getString("lab62c3"));
                demographic.setEncoded(rs.getShort("lab62c4") == 1);
                demographic.setObligatory(rs.getShort("lab62c5"));
                demographic.setOrdering(rs.getShort("lab62c6"));
                demographic.setFormat(rs.getString("lab62c7"));
                demographic.setDefaultValue(rs.getString("lab62c8"));
                demographic.setStatistics(rs.getShort("lab62c9") == 1);
                demographic.setLastOrder(rs.getShort("lab62c10") == 1);
                demographic.setCanCreateItemInOrder(rs.getShort("lab62c11") == 1);

                demographic.setLastTransaction(rs.getTimestamp("lab62c12"));
                /*Usuario*/
                demographic.getUser().setId(rs.getInt("lab04c1"));
                demographic.getUser().setName(rs.getString("lab04c2"));
                demographic.getUser().setLastName(rs.getString("lab04c3"));
                demographic.getUser().setUserName(rs.getString("lab04c4"));
                
                demographic.setPromiseTime(rs.getShort("lab62c15") == 1);                        
                demographic.setState(rs.getInt("lab07c1") == 1);

                demographic.setState(rs.getInt("lab07c1") == 1);
                demographic.setModify(rs.getInt("lab62c13") == 1);
                demographic.setDemographicItem(rs.getInt("lab63c1"));

                return demographic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Demographic getOrdening(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab62c1, lab195c1, lab195.lab04c1, lab195c2, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab195 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab195.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab62c1 = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Demographic demographic = new Demographic();
                demographic.setId(rs.getInt("lab62c1"));
                demographic.setOrderingDemo(rs.getInt("lab195c1"));
                demographic.setLastTransaction(rs.getTimestamp("lab195c2"));
                /*Usuario*/
                demographic.getUser().setId(rs.getInt("lab04c1"));
                demographic.getUser().setName(rs.getString("lab04c2"));
                demographic.getUser().setLastName(rs.getString("lab04c3"));
                demographic.getUser().setUserName(rs.getString("lab04c4"));

                return demographic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Demographic update(Demographic demographic) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab62 SET lab62c2 = ?, lab62c3 = ?, lab62c4 = ?, lab62c5 = ?, lab62c6 = ?, lab62c7 = ?, lab62c8 = ?, lab62c9 = ?, lab62c10 = ?, lab62c11 = ?, lab62c12 = ?, lab04c1 = ?, lab07c1 = ?, lab62c13 = ?, lab62c14 = ?, lab62c15 = ?, lab63c1 = ? "
                + "WHERE lab62c1 = ?",
                demographic.getName(), demographic.getOrigin(), demographic.isEncoded() ? 1 : 0, demographic.getObligatory(), demographic.getOrdering(), demographic.getFormat(), demographic.getDefaultValue(), demographic.isStatistics() ? 1 : 0, demographic.isLastOrder() ? 1 : 0, demographic.isCanCreateItemInOrder() ? 1 : 0, timestamp, demographic.getUser().getId(), demographic.isState() ? 1 : 0, demographic.isModify() ? 1 : 0, demographic.getDefaultValueRequired(), demographic.isPromiseTime()? 1 : 0, demographic.getDemographicItem(), demographic.getId());

        demographic.setLastTransaction(timestamp);
        return demographic;
    }

    @Override
    public void delete(Integer id) throws Exception
    {

    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

}
