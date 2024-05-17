/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.dao.impl.postgresql.exception;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.securitynt.dao.interfaces.exception.ExceptionDao;
import net.cltech.securitynt.domain.exception.WebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Implementa los metodos de acceso a datos para el manejo de errores para
 * PostgreSQL
 *
 * @version 1.0.0
 * @author dcortes
 * @since 25/04/2017
 * @see Creacion
 */
@Repository
public class ExceptionDaoPostgreSQL implements ExceptionDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource ds)
    {
        jdbc = new JdbcTemplate(ds);
    }

    @Override
    public WebException insert(WebException exception) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab151")
                .usingGeneratedKeyColumns("lab151c1")
                .usingColumns("lab151c2", "lab151c3", "lab151c4", "lab151c5", "lab151c6", "lab151c7", "lab04c1", "lab04c5", "lab151c8");
        HashMap<String, Object> fields = new HashMap<>(0);
        fields.put("lab151c2", new Timestamp(exception.getDate().getTime()));
        fields.put("lab151c3", exception.getCode());
        fields.put("lab151c4", exception.getHost());
        fields.put("lab151c5", exception.getUrl());
        fields.put("lab151c6", exception.getMessage() != null ? exception.getMessage() : "");
        fields.put("lab151c7", exception.getDetail());
        fields.put("lab04c1", exception.getIdUser());
        fields.put("lab04c5", exception.getUser());
        fields.put("lab151c8", exception.getType());
        Number key = insert.executeAndReturnKey(fields);
        exception.setId(key.intValue());
        return exception;
    }

    @Override
    public List<WebException> get(Timestamp initialDate, Timestamp finalDate) throws Exception
    {
        return jdbc.query(""
                + "SELECT   lab151.lab151c1 "
                + " , lab151.lab151c2 "
                + " , lab151.lab151c3 "
                + " , lab151.lab151c4 "
                + " , lab151.lab151c5 "
                + " , lab151.lab151c6 "
                + " , lab151.lab151c7 "
                + " , lab151.lab04c1 "
                + " , lab151.lab04c5 "
                + " , lab151.lab151c8 "
                + "FROM     lab151 "
                + "WHERE    lab151c2 BETWEEN ? AND ? ",
                new Object[]
                {
                    initialDate, finalDate
                },
                new int[]
                {
                    java.sql.Types.TIMESTAMP, java.sql.Types.TIMESTAMP
                }, (ResultSet rs, int i) ->
        {
            WebException exception = new WebException();
            exception.setId(rs.getInt("lab151c1"));
            exception.setDate(rs.getTimestamp("lab151c2"));
            exception.setCode(rs.getInt("lab151c3"));
            exception.setHost(rs.getString("lab151c4"));
            exception.setUrl(rs.getString("lab151c5"));
            exception.setMessage(rs.getString("lab151c6"));
            exception.setDetail(rs.getString("lab151c7"));
            exception.setIdUser(rs.getString("lab04c1") == null ? null : rs.getInt("lab04c1"));
            exception.setUser(rs.getString("lab04c5"));
            exception.setType(rs.getInt("lab151c8"));
            return exception;
        });
    }

    @Override
    public List<WebException> get(Timestamp initialDate, Timestamp finalDate, int type) throws Exception
    {
        return jdbc.query(""
                + "SELECT   lab151.lab151c1 "
                + " , lab151.lab151c2 "
                + " , lab151.lab151c3 "
                + " , lab151.lab151c4 "
                + " , lab151.lab151c5 "
                + " , lab151.lab151c6 "
                + " , lab151.lab151c7 "
                + " , lab151.lab04c1 "
                + " , lab151.lab04c5 "
                + " , lab151.lab151c8 "
                + "FROM     lab151 "
                + "WHERE    lab151c2 BETWEEN ? AND ? "
                + "         AND lab151c8 = ? ",
                new Object[]
                {
                    initialDate, finalDate, type
                },
                new int[]
                {
                    java.sql.Types.TIMESTAMP, java.sql.Types.TIMESTAMP, java.sql.Types.INTEGER
                }, (ResultSet rs, int i) ->
        {
            WebException exception = new WebException();
            exception.setId(rs.getInt("lab151c1"));
            exception.setDate(rs.getTimestamp("lab151c2"));
            exception.setCode(rs.getInt("lab151c3"));
            exception.setHost(rs.getString("lab151c4"));
            exception.setUrl(rs.getString("lab151c5"));
            exception.setMessage(rs.getString("lab151c6"));
            exception.setDetail(rs.getString("lab151c7"));
            exception.setIdUser(rs.getString("lab04c1") == null ? null : rs.getInt("lab04c1"));
            exception.setUser(rs.getString("lab04c5"));
            exception.setType(rs.getInt("lab151c8"));
            return exception;
        });
    }
}
