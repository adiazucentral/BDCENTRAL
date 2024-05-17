/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.microbiology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.CollectionMethod;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author cmartin
 */
public interface CollectionMethodDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista los metodos de recolección desde la base de datos.
     *
     * @return Lista de metodos de recolección.
     * @throws Exception Error en la base de datos.
     */
    default List<CollectionMethod> list() throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab201.lab201c1, lab201.lab201c2, lab201.lab201c3, lab201.lab07c1 , lab04.lab04c1 , lab04c2, lab04c3, lab04c4 "
                    + "FROM lab201 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab201.lab04c1";
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                CollectionMethod collectionMethod = new CollectionMethod();

                collectionMethod.setId(rs.getInt("lab201c1"));
                collectionMethod.setName(rs.getString("lab201c2"));
                collectionMethod.setState(rs.getInt("lab07c1") == 1);
                /*Usuario*/
                collectionMethod.getUser().setId(rs.getInt("lab04c1"));
                collectionMethod.getUser().setName(rs.getString("lab04c2"));
                collectionMethod.getUser().setLastName(rs.getString("lab04c3"));
                collectionMethod.getUser().setUserName(rs.getString("lab04c4"));

                collectionMethod.setLastTransaction(rs.getTimestamp("lab201c3"));
                return collectionMethod;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener información de un metodo de recolección por un campo especifico.
     *
     * @param id ID del metodo de recolección a ser consultada.
     * @param name Nombre del metodo de recolección a ser consultada.
     *
     *
     * @return Instancia con las datos del metodo de recolección.
     * @throws Exception Error en la base de datos.
     */
    default CollectionMethod get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = ""
                    + "SELECT lab201.lab201c1, lab201.lab201c2, lab201.lab201c3, lab201.lab07c1 , lab04.lab04c1 , lab04c2, lab04c3, lab04c4 "
                    + "FROM lab201 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab201.lab04c1";
            /*Where*/
            if (id != null)
            {
                query = query + " WHERE lab201.lab201c1 = ? ";
            }
            if (name != null)
            {
                query = query + " WHERE UPPER(lab201.lab201c2) = ? ";
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

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                CollectionMethod collectionMethod = new CollectionMethod();

                collectionMethod.setId(rs.getInt("lab201c1"));
                collectionMethod.setName(rs.getString("lab201c2"));
                collectionMethod.setState(rs.getInt("lab07c1") == 1);
                /*Usuario*/
                collectionMethod.getUser().setId(rs.getInt("lab04c1"));
                collectionMethod.getUser().setName(rs.getString("lab04c2"));
                collectionMethod.getUser().setLastName(rs.getString("lab04c3"));
                collectionMethod.getUser().setUserName(rs.getString("lab04c4"));

                collectionMethod.setLastTransaction(rs.getTimestamp("lab201c3"));

                return collectionMethod;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Registra una nuevo metodo de recolección en la base de datos.
     *
     * @param collectionMethod Instancia con los datos del metodo de
     * recolección.
     *
     * @return Instancia con las datos del metodo de recolección.
     * @throws Exception Error en la base de datos.
     */
    default CollectionMethod create(CollectionMethod collectionMethod) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab201")
                .usingGeneratedKeyColumns("lab201c1");

        HashMap parameters = new HashMap();
        parameters.put("lab201c2", collectionMethod.getName());
        parameters.put("lab07c1", 1);
        parameters.put("lab201c3", timestamp);
        parameters.put("lab04c1", collectionMethod.getUser().getId());

        Number key = insert.executeAndReturnKey(parameters);
        collectionMethod.setId(key.intValue());
        collectionMethod.setLastTransaction(timestamp);

        return collectionMethod;
    }

    /**
     * Actualiza la información de un metodo de recolección en la base de datos.
     *
     * @param collectionMethod Instancia con los datos del metodo de
     * recolección.
     *
     * @return Instancia con las datos del metodo de recolección.
     * @throws Exception Error en la base de datos.
     */
    default CollectionMethod update(CollectionMethod collectionMethod) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        String query = ""
                + " UPDATE lab201 SET lab201c2 = ?, lab07c1 = ?, lab201c3 = ?, lab04c1 = ?"
                + " WHERE lab201c1 = ?";

        getJdbcTemplate().update(query,
                collectionMethod.getName(),
                collectionMethod.isState() ? 1 : 0,
                timestamp,
                collectionMethod.getUser().getId(),
                collectionMethod.getId());
        collectionMethod.setLastTransaction(timestamp);

        return collectionMethod;
    }
}
