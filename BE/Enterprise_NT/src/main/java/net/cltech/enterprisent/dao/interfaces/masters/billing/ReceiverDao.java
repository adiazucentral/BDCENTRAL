/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Receiver;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Receptores.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 04/07/2017
 * @see Creación
 */
public interface ReceiverDao
{

    /**
     * Lista los receptores desde la base de datos.
     *
     * @return Lista de receptores.
     * @throws Exception Error en la base de datos.
     */
    default List<Receiver> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab905c1, lab905c2, lab905c3, lab905c4, lab905c5, lab905c6, lab905.lab04c1, lab04c2, lab04c3, lab04c4, lab905.lab07c1 "
                    + "FROM lab905 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab905.lab04c1", (ResultSet rs, int i) ->
            {
                Receiver receiver = new Receiver();
                receiver.setId(rs.getInt("lab905c1"));
                receiver.setName(rs.getString("lab905c2"));
                receiver.setApplicationReceiverCode(rs.getString("lab905c3"));
                receiver.setReceiverID(rs.getString("lab905c4"));
                receiver.setInterchangeReceiver(rs.getString("lab905c5"));
                /*Usuario*/
                receiver.getUser().setId(rs.getInt("lab04c1"));
                receiver.getUser().setName(rs.getString("lab04c2"));
                receiver.getUser().setLastName(rs.getString("lab04c3"));
                receiver.getUser().setUserName(rs.getString("lab04c4"));

                receiver.setLastTransaction(rs.getTimestamp("lab905c6"));
                receiver.setState(rs.getInt("lab07c1") == 1);

                return receiver;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo receptor en la base de datos.
     *
     * @param receiver Instancia con los datos del receptor.
     *
     * @return Instancia con los datos del receptor.
     * @throws Exception Error en la base de datos.
     */
    default Receiver create(Receiver receiver) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab905")
                .usingGeneratedKeyColumns("lab905c1");

        HashMap parameters = new HashMap();
        parameters.put("lab905c2", receiver.getName().trim());
        parameters.put("lab905c3", receiver.getApplicationReceiverCode().trim());
        parameters.put("lab905c4", receiver.getReceiverID().trim());
        parameters.put("lab905c5", receiver.getInterchangeReceiver().trim());
        parameters.put("lab905c6", timestamp);
        parameters.put("lab04c1", receiver.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        receiver.setId(key.intValue());
        receiver.setLastTransaction(timestamp);

        return receiver;
    }

    /**
     * Obtener información de un receptor por una campo especifico.
     *
     * @param id ID del receptor a ser consultada.
     * @param name Nombre del receptor a ser consultada.
     *
     * @return Instancia con los datos del receptor.
     * @throws Exception Error en la base de datos.
     */
    default Receiver get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab905c1, lab905c2, lab905c3, lab905c4, lab905c5, lab905c6, lab905.lab04c1, lab04c2, lab04c3, lab04c4, lab905.lab07c1 "
                    + "FROM lab905 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab905.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab905c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab905c2) = ? ";
            }
            /*Order By, Group By y demas complementos del consulta*/
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

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Receiver receiver = new Receiver();
                receiver.setId(rs.getInt("lab905c1"));
                receiver.setName(rs.getString("lab905c2"));
                receiver.setApplicationReceiverCode(rs.getString("lab905c3"));
                receiver.setReceiverID(rs.getString("lab905c4"));
                receiver.setInterchangeReceiver(rs.getString("lab905c5"));
                /*Usuario*/
                receiver.getUser().setId(rs.getInt("lab04c1"));
                receiver.getUser().setName(rs.getString("lab04c2"));
                receiver.getUser().setLastName(rs.getString("lab04c3"));
                receiver.getUser().setUserName(rs.getString("lab04c4"));

                receiver.setLastTransaction(rs.getTimestamp("lab905c6"));
                receiver.setState(rs.getInt("lab07c1") == 1);

                return receiver;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una receptor en la base de datos.
     *
     * @param receiver Instancia con los datos del receptor.
     *
     * @return Objeto del receptor modificada.
     * @throws Exception Error en la base de datos.
     */
    default Receiver update(Receiver receiver) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab905 SET lab905c2 = ?, lab905c3 = ?, lab905c4 = ?, lab905c5 = ?, lab905c6 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab905c1 = ?",
                receiver.getName(), receiver.getApplicationReceiverCode(), receiver.getReceiverID(), receiver.getInterchangeReceiver(), timestamp, receiver.getUser().getId(), receiver.isState() ? 1 : 0, receiver.getId());

        receiver.setLastTransaction(timestamp);

        return receiver;
    }

    /**
     *
     * Elimina un receptor del base de datos.
     *
     * @param id ID del receptor.
     *
     * @throws Exception Error en base de datos.
     */
    default void delete(Integer id) throws Exception
    {

    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();
}
