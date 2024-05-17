/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * tipos de documento.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 29/08/2017
 * @see Creación
 */
public interface DocumentTypeDao
{
    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();
    
    /**
     * Lista los tipos de documento desde la base de datos.
     *
     * @return Lista de tipos de documento.
     * @throws Exception Error en la base de datos.
     */
    default List<DocumentType> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab54c1, "
                    + "lab54c2, "
                    + "lab54c3, "
                    + "lab54c4, "
                    + "lab54.lab04c1, "
                    + "lab04c2, "
                    + "lab04c3, "
                    + "lab54.lab54c5 AS codeSiga, "
                    + "lab04c4, "
                    + "lab54c6, "
                    + "lab54.lab07c1 "
                    + "FROM lab54 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab54.lab04c1 "
                    + "WHERE lab54c1 != 1", (ResultSet rs, int i) ->
            {
                DocumentType documentType = new DocumentType();
                documentType.setId(rs.getInt("lab54c1"));
                documentType.setAbbr(rs.getString("lab54c2"));
                documentType.setName(rs.getString("lab54c3"));
                documentType.setCodeSiga(rs.getInt("codeSiga"));
                documentType.setEmail(rs.getString("lab54c6"));
                /*Usuario*/
                documentType.getUser().setId(rs.getInt("lab04c1"));
                documentType.getUser().setName(rs.getString("lab04c2"));
                documentType.getUser().setLastName(rs.getString("lab04c3"));
                documentType.getUser().setUserName(rs.getString("lab04c4"));

                documentType.setLastTransaction(rs.getTimestamp("lab54c4"));
                documentType.setState(rs.getInt("lab07c1") == 1);

                return documentType;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra uno nuevo tipo de documento en la base de datos.
     *
     * @param documentType Instancia con los datos del tipo de documento.
     *
     * @return Instancia con los datos del tipo de documento.
     * @throws Exception Error en la base de datos.
     */
    default DocumentType create(DocumentType documentType) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab54")
                .usingColumns("lab54c2", "lab54c3", "lab54c4", "lab04c1", "lab07c1", "lab54c5", "lab54c6")
                .usingGeneratedKeyColumns("lab54c1");

        HashMap parameters = new HashMap();
        parameters.put("lab54c2", documentType.getAbbr().trim());
        parameters.put("lab54c3", documentType.getName().trim());
        parameters.put("lab54c4", timestamp);
        parameters.put("lab04c1", documentType.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab54c5", documentType.getCodeSiga());
        parameters.put("lab54c6", documentType.getEmail());

        Number key = insert.executeAndReturnKey(parameters);
        documentType.setId(key.intValue());
        documentType.setLastTransaction(timestamp);

        return documentType;
    }

    /**
     * Obtener información de un tipo de documento por una campo especifico.
     *
     * @param id ID del tipo de documento a ser consultado.
     * @param abrr Descripcion del tipo de documento a ser consultado.
     * @param name Descripcion del tipo de documento a ser consultado.
     *
     * @return Instancia con los datos del tipo de documento.
     * @throws Exception Error en la base de datos.
     */
    default DocumentType get(Integer id, String abrr, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab54c1, "
                    + "lab54c2, "
                    + "lab54c3, "
                    + "lab54c4, "
                    + "lab54.lab04c1, "
                    + "lab04c2, "
                    + "lab04c3, "
                    + "lab04c4, "
                    + "lab54c6, "
                    + "lab54.lab54c5 AS codeSiga, "
                    + "lab54.lab07c1 "
                    + "FROM lab54 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab54.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab54c1 = ? ";
            }
            if (abrr != null)
            {
                query = query + "WHERE UPPER(lab54c2) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab54c3) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (abrr != null)
            {
                object = abrr.toUpperCase();
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
                DocumentType documentType = new DocumentType();
                documentType.setId(rs.getInt("lab54c1"));
                documentType.setAbbr(rs.getString("lab54c2"));
                documentType.setName(rs.getString("lab54c3"));
                documentType.setEmail(rs.getString("lab54c6"));
                documentType.setCodeSiga(rs.getInt("codeSiga"));
                /*Usuario*/
                documentType.getUser().setId(rs.getInt("lab04c1"));
                documentType.getUser().setName(rs.getString("lab04c2"));
                documentType.getUser().setLastName(rs.getString("lab04c3"));
                documentType.getUser().setUserName(rs.getString("lab04c4"));

                documentType.setLastTransaction(rs.getTimestamp("lab54c4"));
                documentType.setState(rs.getInt("lab07c1") == 1);

                return documentType;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una tipo de documento en la base de datos.
     *
     * @param documentType Instancia con los datos de la tipo de documento.
     *
     * @return Objeto de la tipo de documento modificado.
     * @throws Exception Error en la base de datos.
     */
    default DocumentType update(DocumentType documentType) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab54 SET lab54c2 = ?, "
                + "lab54c3 = ?, "
                + "lab54c4 = ?, "
                + "lab04c1 = ?, "
                + "lab54c5 = ?, "
                + "lab54c6 = ?, "
                + "lab07c1 = ? "
                + "WHERE lab54c1 = ?",
                documentType.getAbbr(),
                documentType.getName(),
                timestamp,
                documentType.getUser().getId(),
                documentType.getCodeSiga(),
                documentType.getEmail(),
                documentType.isState() ? 1 : 0,
                documentType.getId());

        documentType.setLastTransaction(timestamp);

        return documentType;
    }

    /**
     *
     * Elimina un tipo de documento de la base de datos.
     *
     * @param id ID del tipo de documento.
     *
     * @throws Exception Error en base de datos.
     */
    default void delete(Integer id) throws Exception
    {
    }
}
