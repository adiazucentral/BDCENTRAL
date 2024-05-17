/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.masters.microbiology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.AnatomicalSiteDao;
import net.cltech.enterprisent.domain.masters.microbiology.AnatomicalSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro Sitio
 * Anatomico para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/06/2017
 * @see Creación
 */
@Repository
public class AnatomicalSiteDaoPostgreSQL implements AnatomicalSiteDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<AnatomicalSite> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab158c1, lab158c2, lab158c3, lab158c4, lab158.lab04c1, lab04c2, lab04c3, lab04c4, lab158.lab07c1 "
                    + "FROM lab158 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab158.lab04c1", (ResultSet rs, int i) ->
            {
                AnatomicalSite anatomicalSite = new AnatomicalSite();
                anatomicalSite.setId(rs.getInt("lab158c1"));
                anatomicalSite.setName(rs.getString("lab158c2"));
                anatomicalSite.setAbbr(rs.getString("lab158c3"));
                /*Usuario*/
                anatomicalSite.getUser().setId(rs.getInt("lab04c1"));
                anatomicalSite.getUser().setName(rs.getString("lab04c2"));
                anatomicalSite.getUser().setLastName(rs.getString("lab04c3"));
                anatomicalSite.getUser().setUserName(rs.getString("lab04c4"));

                anatomicalSite.setLastTransaction(rs.getTimestamp("lab158c4"));
                anatomicalSite.setState(rs.getInt("lab07c1") == 1);

                return anatomicalSite;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public AnatomicalSite create(AnatomicalSite anatomicalSite) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab158")
                .usingGeneratedKeyColumns("lab158c1");

        HashMap parameters = new HashMap();
        parameters.put("lab158c2", anatomicalSite.getName().trim());
        parameters.put("lab158c3", anatomicalSite.getAbbr().trim());
        parameters.put("lab158c4", timestamp);
        parameters.put("lab04c1", anatomicalSite.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        anatomicalSite.setId(key.intValue());
        anatomicalSite.setLastTransaction(timestamp);

        return anatomicalSite;
    }

    @Override
    public AnatomicalSite get(Integer id, String name, String abbr) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab158c1, lab158c2, lab158c3, lab158c4, lab158.lab04c1, lab04c2, lab04c3, lab04c4, lab158.lab07c1 "
                    + "FROM lab158 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab158.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab158c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab158c2) = ? ";
            }
            if (abbr != null)
            {
                query = query + "WHERE UPPER(lab158c3) = ? ";
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
            if (abbr != null)
            {
                object = abbr.toUpperCase();
            }

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                AnatomicalSite anatomicalSite = new AnatomicalSite();
                anatomicalSite.setId(rs.getInt("lab158c1"));
                anatomicalSite.setName(rs.getString("lab158c2"));
                anatomicalSite.setAbbr(rs.getString("lab158c3"));
                /*Usuario*/
                anatomicalSite.getUser().setId(rs.getInt("lab04c1"));
                anatomicalSite.getUser().setName(rs.getString("lab04c2"));
                anatomicalSite.getUser().setLastName(rs.getString("lab04c3"));
                anatomicalSite.getUser().setUserName(rs.getString("lab04c4"));

                anatomicalSite.setLastTransaction(rs.getTimestamp("lab158c4"));
                anatomicalSite.setState(rs.getInt("lab07c1") == 1);

                return anatomicalSite;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public AnatomicalSite update(AnatomicalSite anatomicalSite) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab158 SET lab158c2 = ?, lab158c3 = ?, lab158c4 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab158c1 = ?",
                anatomicalSite.getName(), anatomicalSite.getAbbr(), timestamp, anatomicalSite.getUser().getId(), anatomicalSite.isState() ? 1 : 0, anatomicalSite.getId());

        anatomicalSite.setLastTransaction(timestamp);

        return anatomicalSite;
    }

    @Override
    public void delete(Integer id) throws Exception
    {

    }

}
