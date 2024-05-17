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
import net.cltech.enterprisent.domain.masters.test.CommentCoded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import net.cltech.enterprisent.dao.interfaces.masters.test.CommentCodedDao;

/**
 * Realiza la implementación del acceso a datos de información de los
 * comentarios para PostgreSQL
 *
 * @version 1.0.0
 * @author enavas
 * @since 15/05/2017
 * @see Creación
 */
@Repository
public class CommentCodedDaoPostgreSQL implements CommentCodedDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<CommentCoded> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab68.lab68c1"
                    + ", lab68.lab68c2"
                    + ", lab68.lab68c3"
                    + ", lab68.lab68c4"
                    + ", lab68.lab07c1"
                    + ", lab68.lab68c5"
                    + ", lab68.lab68c6"
                    + ", lab68.lab04c1"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab68.lab68c7"
                    + "  FROM lab68"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1=lab68.lab04c1", (ResultSet rs, int i) ->
            {
                CommentCoded comment = new CommentCoded();
                comment.setId(rs.getInt("lab68c1"));
                comment.setCode(rs.getString("lab68c2"));
                comment.setMessage(rs.getString("lab68c3"));
                comment.setLastTransaction(rs.getTimestamp("lab68c4"));
                comment.setState(rs.getBoolean("lab07c1"));
                comment.setApply(rs.getInt("lab68c5"));
                comment.setDiagnostic(rs.getInt("lab68c6"));
                comment.setMessageEnglish(rs.getString("lab68c7"));
                /*Usuario*/
                comment.getUser().setId(rs.getInt("lab04c1"));
                comment.getUser().setName(rs.getString("lab04c2"));
                comment.getUser().setLastName(rs.getString("lab04c3"));
                comment.getUser().setUserName(rs.getString("lab04c4"));
                return comment;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public CommentCoded create(CommentCoded comment) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab68")
                .usingColumns("lab68c2", "lab68c3", "lab68c4", "lab07c1", "lab68c5", "lab04c1", "lab68c6","lab68c7" )
                .usingGeneratedKeyColumns("lab68c1");

        HashMap parameters = new HashMap();

        parameters.put("lab68c2", comment.getCode());
        parameters.put("lab68c3", comment.getMessage());
        parameters.put("lab68c4", timestamp);
        parameters.put("lab07c1", 1);
        parameters.put("lab68c5", comment.getApply());
        parameters.put("lab04c1", comment.getUser().getId());
        parameters.put("lab68c6", comment.getDiagnostic());
        parameters.put("lab68c7", comment.getMessageEnglish());
        
        Number key = insert.executeAndReturnKey(parameters);
        comment.setId(key.intValue());
        comment.setLastTransaction(timestamp);
        return comment;

    }

        @Override
    public List<CommentCoded> get(Integer id, String code, Integer apply, Boolean state) throws Exception
    {
        try
        {
            String query;
            query = ""
                    + "SELECT lab68.lab68c1"
                    + ", lab68.lab68c2"
                    + ", lab68.lab68c3"
                    + ", lab68.lab68c4"
                    + ", lab68.lab07c1"
                    + ", lab68.lab68c5"
                    + ", lab68.lab68c6"
                    + ", lab68.lab04c1"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab68.lab68c7"
                    + "  FROM lab68"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1=lab68.lab04c1";
            //where
            if (id != null)
            {
                query += " WHERE lab68.lab68c1 = ? ";
            }

            if (code != null)
            {
                query += " WHERE UPPER(lab68.lab68c2) = ? ";
            }

            if (apply != null)
            {
                query += " WHERE lab68.lab68c5 = ? ";
            }

            if (state != null)
            {
                query += " WHERE lab68.lab07c1 = ? ";
            }


            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;

            if (id != null)
            {
                object = id;
            }
            if (code != null)
            {
                object = code.toUpperCase();
            }
            if (apply != null)
            {
                object = apply;
            }

            if (state != null)
            {
                object = state ? 1 : 0;
            }
            return jdbc.query(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                CommentCoded comment = new CommentCoded();
                comment.setId(rs.getInt("lab68c1"));
                comment.setCode(rs.getString("lab68c2"));
                comment.setMessage(rs.getString("lab68c3"));
                comment.setLastTransaction(rs.getTimestamp("lab68c4"));
                comment.setState(rs.getBoolean("lab07c1"));
                comment.setApply(rs.getInt("lab68c5"));
                comment.setDiagnostic(rs.getInt("lab68c6"));
                comment.setMessageEnglish(rs.getString("lab68c7"));
                /*Usuario*/
                comment.getUser().setId(rs.getInt("lab04c1"));
                comment.getUser().setName(rs.getString("lab04c2"));
                comment.getUser().setLastName(rs.getString("lab04c3"));
                comment.getUser().setUserName(rs.getString("lab04c4"));
                return comment;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public CommentCoded update(CommentCoded comment) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query;
        query = ""
                + " UPDATE lab68 SET"
                + " lab68c2 = ?"
                + " ,lab68c3 = ?"
                + " ,lab68c4 = ?"
                + " ,lab07c1 = ?"
                + " ,lab68c5 = ?"
                + " ,lab04c1 = ?"
                + " ,lab68c6 = ?"
                + " ,lab68c7 = ?"
                + "  WHERE lab68c1 = ?";
        jdbc.update(query, comment.getCode(),
                comment.getMessage(),
                timestamp,
                comment.isState() ? 1 : 0,
                comment.getApply(),
                comment.getUser().getId(),
                comment.getDiagnostic(),
                comment.getMessageEnglish(),
                comment.getId()
        );
        comment.setLastTransaction(timestamp);
        return comment;
    }

}
