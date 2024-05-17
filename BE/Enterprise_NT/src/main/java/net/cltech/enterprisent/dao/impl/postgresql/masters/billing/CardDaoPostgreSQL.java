/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.CardDao;
import net.cltech.enterprisent.domain.masters.billing.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Tarjetas de Credito para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creación
 */
@Repository
public class CardDaoPostgreSQL implements CardDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Card> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab59c1, lab59c2, lab59c3, lab59.lab04c1, lab04c2, lab04c3, lab04c4, lab59.lab07c1 "
                    + "FROM lab59 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab59.lab04c1", (ResultSet rs, int i) ->
            {
                Card card = new Card();
                card.setId(rs.getInt("lab59c1"));
                card.setName(rs.getString("lab59c2"));
                /*Usuario*/
                card.getUser().setId(rs.getInt("lab04c1"));
                card.getUser().setName(rs.getString("lab04c2"));
                card.getUser().setLastName(rs.getString("lab04c3"));
                card.getUser().setUserName(rs.getString("lab04c4"));

                card.setLastTransaction(rs.getTimestamp("lab59c3"));
                card.setState(rs.getInt("lab07c1") == 1);

                return card;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Card create(Card card) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab59")
                .usingGeneratedKeyColumns("lab59c1");

        HashMap parameters = new HashMap();
        parameters.put("lab59c2", card.getName().trim());
        parameters.put("lab59c3", timestamp);
        parameters.put("lab04c1", card.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        card.setId(key.intValue());
        card.setLastTransaction(timestamp);

        return card;
    }

    @Override
    public Card get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab59c1, lab59c2, lab59c3, lab59.lab04c1, lab04c2, lab04c3, lab04c4, lab59.lab07c1 "
                    + "FROM lab59 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab59.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab59c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab59c2) = ? ";
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
                Card card = new Card();
                card.setId(rs.getInt("lab59c1"));
                card.setName(rs.getString("lab59c2"));
                /*Usuario*/
                card.getUser().setId(rs.getInt("lab04c1"));
                card.getUser().setName(rs.getString("lab04c2"));
                card.getUser().setLastName(rs.getString("lab04c3"));
                card.getUser().setUserName(rs.getString("lab04c4"));

                card.setLastTransaction(rs.getTimestamp("lab59c3"));
                card.setState(rs.getInt("lab07c1") == 1);

                return card;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public Card update(Card card) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        jdbc.update("UPDATE lab59 SET lab59c2 = ?, lab59c3 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab59c1 = ?",
                card.getName(), timestamp, card.getUser().getId(), card.isState() ? 1 : 0, card.getId());

        card.setLastTransaction(timestamp);

        return card;
    }

    @Override
    public void delete(Integer id) throws Exception
    {

    }

}
