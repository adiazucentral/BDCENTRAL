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
import net.cltech.enterprisent.dao.interfaces.masters.test.DiagnosticDao;
import net.cltech.enterprisent.domain.masters.test.Diagnostic;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro de
 * Diagnostico para PostgreSQL
 *
 * @version 1.0.0
 * @author enavas
 * @since 21/06/2017
 * @see Creación
 */
@Repository
public class DiagnosticDaoPostgreSQL implements DiagnosticDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Diagnostic> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab20.lab20c1"
                    + ", lab20.lab20c2"
                    + ", lab20.lab20c3"
                    + ", lab20.lab20c4"
                    + ", lab20.lab20c5"
                    + ", lab20.lab04c1"
                    + ", lab20.lab07c1"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab80.lab80c1"
                    + ", lab80.lab80c2"
                    + ", lab80.lab80c3"
                    + ", lab80.lab80c4"
                    + ", lab80.lab80c5"
                    + "  FROM lab20"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1=lab20.lab04c1"
                    + "  LEFT JOIN lab80 ON lab80.lab80c1 = lab20.lab20c5", (ResultSet rs, int i) ->
            {
                Diagnostic diagnostic = new Diagnostic();

                diagnostic.setId(rs.getInt("lab20c1"));
                diagnostic.setCode(rs.getString("lab20c2"));
                diagnostic.setName(rs.getString("lab20c3"));
                diagnostic.setLastTransaction(rs.getTimestamp("lab20c4"));
                diagnostic.getType().setId(rs.getInt("lab20c5"));
                diagnostic.setState(rs.getInt("lab07c1") == 1);
                /*Usuario*/
                diagnostic.getUser().setId(rs.getInt("lab04c1"));
                diagnostic.getUser().setName(rs.getString("lab04c2"));
                diagnostic.getUser().setLastName(rs.getString("lab04c3"));
                diagnostic.getUser().setUserName(rs.getString("lab04c4"));
                /*TIPO */
                diagnostic.getType().setId(rs.getInt("lab80c1"));
                diagnostic.getType().setIdParent(rs.getInt("lab80c2"));
                diagnostic.getType().setCode(rs.getString("lab80c3"));
                diagnostic.getType().setEsCo(rs.getString("lab80c4"));
                diagnostic.getType().setEnUsa(rs.getString("lab80c5"));

                return diagnostic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public List<Diagnostic> get(Integer id, String code, String name, Integer type, Boolean state) throws Exception
    {
        try
        {

            String query;

            query = ""
                    + "SELECT lab20.lab20c1"
                    + ", lab20.lab20c2"
                    + ", lab20.lab20c3"
                    + ", lab20.lab20c4"
                    + ", lab20.lab20c5"
                    + ", lab20.lab04c1"
                    + ", lab20.lab07c1"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab80.lab80c1"
                    + ", lab80.lab80c2"
                    + ", lab80.lab80c3"
                    + ", lab80.lab80c4"
                    + ", lab80.lab80c5"
                    + "  FROM lab20"
                    + "  LEFT JOIN lab04 ON lab04.lab04c1=lab20.lab04c1"
                    + "  LEFT JOIN lab80 ON lab80.lab80c1 = lab20.lab20c5";
            //where

            if (id != null)
            {
                query += " WHERE lab20.lab20c1 = ? ";
            }

            if (code != null)
            {
                query += " WHERE UPPER(lab20.lab20c2) = ? ";
            }

            if (name != null)
            {
                query += " WHERE UPPER(lab20.lab20c3) = ? ";
            }

            if (type != null)
            {
                query += " WHERE lab20.lab20c5 = ? ";
            }

            if (state != null)
            {
                query += " WHERE lab20.lab07c1 = ? ";
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

            if (name != null)
            {
                object = name.toUpperCase();
            }

            if (type != null)
            {
                object = type;
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
                Diagnostic diagnostic = new Diagnostic();

                diagnostic.setId(rs.getInt("lab20c1"));
                diagnostic.setCode(rs.getString("lab20c2"));
                diagnostic.setName(rs.getString("lab20c3"));
                diagnostic.setLastTransaction(rs.getTimestamp("lab20c4"));
                diagnostic.getType().setId(rs.getInt("lab20c5"));
                diagnostic.setState(rs.getInt("lab07c1") == 1);
                /*Usuario*/
                diagnostic.getUser().setId(rs.getInt("lab04c1"));
                diagnostic.getUser().setName(rs.getString("lab04c2"));
                diagnostic.getUser().setLastName(rs.getString("lab04c3"));
                diagnostic.getUser().setUserName(rs.getString("lab04c4"));
                /*TIPO */
                diagnostic.getType().setId(rs.getInt("lab80c1"));
                diagnostic.getType().setIdParent(rs.getInt("lab80c2"));
                diagnostic.getType().setCode(rs.getString("lab80c3"));
                diagnostic.getType().setEsCo(rs.getString("lab80c4"));
                diagnostic.getType().setEnUsa(rs.getString("lab80c5"));

                return diagnostic;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Diagnostic create(Diagnostic diagnostic) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab20")
                .usingColumns("lab20c2", "lab20c3", "lab20c4", "lab20c5", "lab04c1", "lab07c1")
                .usingGeneratedKeyColumns("lab20c1");

        HashMap parameters = new HashMap();

        parameters.put("lab20c2", diagnostic.getCode());
        parameters.put("lab20c3", diagnostic.getName());
        parameters.put("lab20c4", timestamp);
        parameters.put("lab20c5", diagnostic.getType().getId());
        parameters.put("lab04c1", diagnostic.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);

        diagnostic.setId(key.intValue());
        diagnostic.setLastTransaction(timestamp);
        return diagnostic;
    }

    @Override
    public Diagnostic update(Diagnostic diagnostic) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query;
        query = ""
                + " UPDATE lab20 SET"
                + "      lab20c2=?"
                + "    , lab20c3=?"
                + "    , lab20c4=?"
                + "    , lab20c5=?"
                + "    , lab04c1=?"
                + "    , lab07c1=?"
                + " WHERE lab20c1 = ?";

        jdbc.update(query, diagnostic.getCode(),
                diagnostic.getName(),
                timestamp,
                diagnostic.getType().getId(),
                diagnostic.getUser().getId(),
                diagnostic.isState() ? 1 : 0,
                diagnostic.getId()
        );
        diagnostic.setLastTransaction(timestamp);
        return diagnostic;
    }

    @Override
    public int createAll(List<Diagnostic> diagnostics) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab20")
                .usingGeneratedKeyColumns("lab20c1");
        for (Diagnostic diagnostic : diagnostics)
        {
            HashMap parameters = new HashMap();

            parameters.put("lab20c2", diagnostic.getCode());
            parameters.put("lab20c3", diagnostic.getName());
            parameters.put("lab20c4", timestamp);
            parameters.put("lab20c5", diagnostic.getType().getId());
            parameters.put("lab04c1", diagnostic.getId());
            parameters.put("lab07c1", 1);
            batchArray.add(parameters);
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[diagnostics.size()]));

        return inserted.length;
    }

    @Override
    public int createAllByOrder(Order order) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc).withTableName("lab180");
        List<Diagnostic> diagnostics = order.getListDiagnostic();
        for (Diagnostic diagnostic : diagnostics)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab22c1", order.getOrderNumber());
            parameters.put("lab20c1", diagnostic.getId());
            batchArray.add(parameters);
        }
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[diagnostics.size()]));
        return inserted.length;
    }

    @Override
    public int updateAllByOrder(Order order) throws Exception
    {
        String query = "DELETE FROM lab180 WHERE lab22c1 = " + order.getOrderNumber();
        jdbc.execute(query);
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc).withTableName("lab180");
        List<Diagnostic> diagnostics = order.getListDiagnostic();
        for (Diagnostic diagnostic : diagnostics)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab22c1", order.getOrderNumber());
            parameters.put("lab20c1", diagnostic.getId());
            batchArray.add(parameters);
        }
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[diagnostics.size()]));
        return inserted.length;
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

}
