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
import net.cltech.enterprisent.dao.interfaces.masters.test.AreaDao;
import net.cltech.enterprisent.domain.masters.test.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro Áreas
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/04/2017
 * @see Creación
 */
@Repository
public class AreaDaoPostgreSQL implements AreaDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Area> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab43c1, lab43c2, lab43c3, lab43c4, lab43c5, lab43c7, lab43c8, lab43c9, lab43.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab43.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab43 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab43.lab43c6 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab43.lab04c1", (ResultSet rs, int i) ->
            {
                Area area = new Area();
                area.setId(rs.getInt("lab43c1"));
                area.setOrdering(rs.getShort("lab43c2"));
                area.setAbbreviation(rs.getString("lab43c3"));
                area.setName(rs.getString("lab43c4"));
                area.setNameEnglish(rs.getString("lab43c9"));
                area.setColor(rs.getString("lab43c5"));

                /*Tipo*/
                area.getType().setId(rs.getInt("lab80c1"));
                area.getType().setIdParent(rs.getInt("lab80c2"));
                area.getType().setCode(rs.getString("lab80c3"));
                area.getType().setEsCo(rs.getString("lab80c4"));
                area.getType().setEnUsa(rs.getString("lab80c5"));

                area.setLastTransaction(rs.getTimestamp("lab43c7"));
                area.setPartialValidation(rs.getInt("lab43c8") == 1);
                /*Usuario*/
                area.getUser().setId(rs.getInt("lab04c1"));
                area.getUser().setName(rs.getString("lab04c2"));
                area.getUser().setLastName(rs.getString("lab04c3"));
                area.getUser().setUserName(rs.getString("lab04c4"));

                area.setState(rs.getInt("lab07c1") == 1);

                return area;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Area create(Area area) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab43")
                .usingColumns("lab43c2", "lab43c3", "lab43c4", "lab43c5", "lab43c6", "lab43c7", "lab43c8", "lab04c1", "lab07c1", "lab43c9")
                .usingGeneratedKeyColumns("lab43c1");

        HashMap parameters = new HashMap();
        parameters.put("lab43c2", area.getOrdering());
        parameters.put("lab43c3", area.getAbbreviation().trim());
        parameters.put("lab43c4", area.getName().trim());
        parameters.put("lab43c5", area.getColor());
        parameters.put("lab43c6", area.getType().getId());
        parameters.put("lab43c7", timestamp);
        parameters.put("lab43c8", area.isPartialValidation() ? 1 : 0);
        parameters.put("lab04c1", area.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab43c9", area.getNameEnglish().trim());

        Number key = insert.executeAndReturnKey(parameters);
        area.setId(key.intValue());
        area.setLastTransaction(timestamp);

        return area;
    }

    @Override
    public Area get(Integer id, Integer ordering, String name, String abbr) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab43c1, lab43c2, lab43c3, lab43c4, lab43c5, lab43c7, lab43c8, lab43c9, lab43.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab43.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab43 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab43.lab43c6 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab43.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab43c1 = ? ";
            }
            if (ordering != null)
            {
                query = query + "WHERE lab43c2 = ? ";
            }
            if (abbr != null)
            {
                query = query + "WHERE UPPER(lab43c3) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab43c4) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (ordering != null)
            {
                object = ordering;
            }
            if (abbr != null)
            {
                object = abbr.toUpperCase();
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
                Area area = new Area();
                area.setId(rs.getInt("lab43c1"));
                area.setOrdering(rs.getShort("lab43c2"));
                area.setAbbreviation(rs.getString("lab43c3"));
                area.setName(rs.getString("lab43c4"));
                area.setNameEnglish(rs.getString("lab43c9"));
                area.setColor(rs.getString("lab43c5"));

                /*Tipo*/
                area.getType().setId(rs.getInt("lab80c1"));
                area.getType().setIdParent(rs.getInt("lab80c2"));
                area.getType().setCode(rs.getString("lab80c3"));
                area.getType().setEsCo(rs.getString("lab80c4"));
                area.getType().setEnUsa(rs.getString("lab80c5"));

                area.setLastTransaction(rs.getTimestamp("lab43c7"));
                area.setPartialValidation(rs.getInt("lab43c8") == 1);
                /*Usuario*/
                area.getUser().setId(rs.getInt("lab04c1"));
                area.getUser().setName(rs.getString("lab04c2"));
                area.getUser().setLastName(rs.getString("lab04c3"));
                area.getUser().setUserName(rs.getString("lab04c4"));

                area.setState(rs.getInt("lab07c1") == 1);

                return area;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Area update(Area area) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab43 SET lab43c2 = ?, lab43c3 = ?, lab43c4 = ?, lab43c5 = ?, lab43c6 = ?, lab43c7 = ?, lab43c8 = ?, lab04c1 = ?, lab07c1 = ?, lab43c9 = ? "
                + "WHERE lab43c1 = ?",
                area.getOrdering(), area.getAbbreviation(), area.getName(), area.getColor(), area.getType().getId(), timestamp, area.isPartialValidation() ? 1 : 0, area.getUser().getId(), area.isState() ? 1 : 0, area.getNameEnglish(), area.getId());

        area.setLastTransaction(timestamp);

        return area;
    }

    @Override
    public void delete(Integer id) throws Exception
    {

    }
}
