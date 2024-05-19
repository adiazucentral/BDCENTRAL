/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.document;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.document.Document;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos documentos para obtener los
 * documentos asociados a las ordenes o resultados.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/12/2017
 * @see Creación
 */
public interface DocumentDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Lista los documentos de una orden.
     *
     * @param idOrder Orden.
     *
     * @return Lista de documentos.
     * @throws Exception Error en la base de datos.
     */
    default List<Document> list(long idOrder) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab22c1, doc01c1, doc01c2, doc01c3, doc01c4, doc01c5, doc01c6, lab04c1, doc01c7 "
                    + "FROM doc01 "
                    + "WHERE lab22c1 = ? ",
                    new Object[]
                    {
                        idOrder
                    }, (ResultSet rs, int i) ->
            {
                Document document = new Document();
                document.setIdOrder(rs.getLong("lab22c1"));
                document.setName(rs.getString("doc01c1"));
                
                document.setDate(rs.getTimestamp("doc01c3"));
                document.setFileType(rs.getString("doc01c4"));
                document.setExtension(rs.getString("doc01c5"));
                document.setViewresul(rs.getInt("doc01c6") == 1);
                document.getUser().setId(rs.getInt("lab04c1"));
                document.setPath(rs.getString("doc01c7"));
                return document;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los documentos de una orden.
     *
     * @param idOrder Orden.
     *
     * @return Lista de documentos que se encuentran activo para ver en reporte.
     * @throws Exception Error en la base de datos.
     */
    default List<Document> listattament(long idOrder) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab22c1, doc01c1, doc01c2, doc01c3, doc01c4, doc01c5, doc01c6, lab04c1, doc01c7 "
                    + "FROM doc01 "
                    + "WHERE lab22c1 = ? AND doc01c6= 1",
                    new Object[]
                    {
                        idOrder
                    }, (ResultSet rs, int i) ->
            {
                Document document = new Document();
                document.setIdOrder(rs.getLong("lab22c1"));
                document.setName(rs.getString("doc01c1"));
               
                document.setDate(rs.getTimestamp("doc01c3"));
                document.setFileType(rs.getString("doc01c4"));
                document.setExtension(rs.getString("doc01c5"));
                document.setViewresul(rs.getInt("doc01c6") == 1);
                document.getUser().setId(rs.getInt("lab04c1"));
                document.setPath(rs.getString("doc01c7"));
                return document;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los documentos de un resultado.
     *
     * @param idOrder Orden.
     * @param idTest Prueba.
     *
     * @return Lista de documentos que se encuentran activo para ver en reporte.
     * @throws Exception Error en la base de datos.
     */
    default List<Document> listResultattament(long idOrder, int idTest) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab22c1, lab39c1, doc02c1,  doc02c3, doc02c4, doc02c5, doc02c6, lab04c1, doc02c7 "
                    + "FROM doc02 "
                    + "WHERE lab22c1 = ? AND lab39c1 = ? AND doc02c6 = 1",
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) ->
            {
                Document document = new Document();
                document.setIdOrder(rs.getLong("lab22c1"));
                document.setIdTest(rs.getInt("lab39c1"));
                document.setName(rs.getString("doc02c1"));
               
                document.setDate(rs.getTimestamp("doc02c3"));
                document.setFileType(rs.getString("doc02c4"));
                document.setExtension(rs.getString("doc02c5"));
                document.setViewresul(rs.getInt("doc02c6") == 1);
                document.getUser().setId(rs.getInt("lab04c1"));
                document.setPath(rs.getString("doc02c7"));
                return document;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los documentos de una orden.
     *
     * @param idOrder Orden.
     *
     * @return Lista de documentos que se encuentran activo para ver en reporte.
     * @throws Exception Error en la base de datos.
     */
    default List<Document> listResultattament(long idOrder) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab22c1, lab39c1, doc02c1, doc02c3, doc02c4, doc02c5, doc02c6, lab04c1, doc02c7 "
                    + "FROM doc02 "
                    + "WHERE lab22c1 = ? AND doc02c6 =1",
                    new Object[]
                    {
                        idOrder
                    }, (ResultSet rs, int i) ->
            {
                Document document = new Document();
                document.setIdOrder(rs.getLong("lab22c1"));
                document.setIdTest(rs.getInt("lab39c1"));
                document.setName(rs.getString("doc02c1"));
               
                document.setDate(rs.getTimestamp("doc02c3"));
                document.setFileType(rs.getString("doc02c4"));
                document.setExtension(rs.getString("doc02c5"));
                document.setViewresul(rs.getInt("doc02c6") == 1);
                document.getUser().setId(rs.getInt("lab04c1"));
                document.setPath(rs.getString("doc02c7"));
                return document;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los documentos de un resultado.
     *
     * @param idOrder Orden.
     * @param idTest Prueba.
     *
     * @return Lista de documentos.
     * @throws Exception Error en la base de datos.
     */
    default List<Document> listResultDocument(long idOrder, int idTest) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab22c1, lab39c1, doc02c1, doc02c3, doc02c4, doc02c5, doc02c6, lab04c1, doc02c7 "
                    + "FROM doc02 "
                    + "WHERE lab22c1 = ? AND lab39c1 = ?",
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) ->
            {
                Document document = new Document();
                document.setIdOrder(rs.getLong("lab22c1"));
                document.setIdTest(rs.getInt("lab39c1"));
                document.setName(rs.getString("doc02c1"));
                
                document.setDate(rs.getTimestamp("doc02c3"));
                document.setFileType(rs.getString("doc02c4"));
                document.setExtension(rs.getString("doc02c5"));
                document.setViewresul(rs.getInt("doc02c6") == 1);
                document.getUser().setId(rs.getInt("lab04c1"));
                document.setPath(rs.getString("doc02c7"));
                return document;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los documentos de una orden.
     *
     * @param idOrder Orden.
     *
     * @return Lista de documentos.
     * @throws Exception Error en la base de datos.
     */
    default List<Document> listResultDocument(long idOrder) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab22c1, lab39c1, doc02c1, doc02c3, doc02c4, doc02c5, lab04c1, doc02c7 "
                    + "FROM doc02 "
                    + "WHERE lab22c1 = ?",
                    new Object[]
                    {
                        idOrder
                    }, (ResultSet rs, int i) ->
            {
                Document document = new Document();
                document.setIdOrder(rs.getLong("lab22c1"));
                document.setIdTest(rs.getInt("lab39c1"));
                document.setName(rs.getString("doc02c1"));
               
                document.setDate(rs.getTimestamp("doc02c3"));
                document.setFileType(rs.getString("doc02c4"));
                document.setExtension(rs.getString("doc02c5"));
                document.getUser().setId(rs.getInt("lab04c1"));
                document.setPath(rs.getString("doc02c7"));
                return document;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo documento de la orden en la base de datos
     *
     * @param document Instancia con los datos del recipiente.
     *
     * @return Instancia con los datos del recipiente.
     * @throws Exception Error en base de datos
     */
    default Document saveOrderDocument(Document document) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("doc01");

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", document.getIdOrder());
        parameters.put("doc01c1", document.getName());
        
        parameters.put("doc01c3", timestamp);
        parameters.put("doc01c4", document.getFileType());
        parameters.put("doc01c5", document.getExtension());
        parameters.put("doc01c6", document.isViewresul() ? 1 : 0);
        parameters.put("lab04c1", document.getUser().getId());
        parameters.put("doc01c7", document.getPath());
        insert.execute(parameters);
        document.setDate(timestamp);

        return document;
    }

    /**
     * Registra un nuevo documento del resultado en la base de datos
     *
     * @param document Instancia con los datos del recipiente.
     *
     * @return Instancia con los datos del recipiente.
     * @throws Exception Error en base de datos
     */
    default Document saveResultDocument(Document document) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("doc02");

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", document.getIdOrder());
        parameters.put("lab39c1", document.getIdTest());
        parameters.put("doc02c1", document.getName());
       
        parameters.put("doc02c3", timestamp);
        parameters.put("doc02c4", document.getFileType());
        parameters.put("doc02c5", document.getExtension());
        parameters.put("doc02c6", document.isViewresul() ? 1 : 0);
        parameters.put("lab04c1", document.getUser().getId());
        parameters.put("doc02c7", document.getPath());
        
        insert.execute(parameters);
        document.setDate(timestamp);

        return document;
    }

    /**
     * Registra un nuevo documento de la orden en la base de datos
     *
     * @param document Instancia con los datos del recipiente.
     *
     * @return Instancia con los datos del recipiente.
     * @throws Exception Error en base de datos
     */
    default Document updateOrderDocument(Document document) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
       
        String query;
        query = ""
                + "UPDATE doc01 SET "
                + "doc01c3 = ?, "
                + "doc01c6 = ?, "
                + "lab04c1 = ?, "
                + "doc01c7 = ? "
                + "WHERE lab22c1 = ? AND LOWER(doc01c1) = ?";
        getConnection().update(query, timestamp, document.isViewresul() ? 1 : 0, document.getUser().getId(), document.getPath(), document.getIdOrder(), document.getName().toLowerCase());
        document.setDate(timestamp);

        return document;
    }

    /**
     * Registra un nuevo documento del resultado en la base de datos
     *
     * @param document Instancia con los datos del recipiente.
     *
     * @return Instancia con los datos del recipiente.
     * @throws Exception Error en base de datos
     */
    default Document updateResultDocument(Document document) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
       
        String query;
        query = ""
                + "UPDATE doc02 SET "
                + "doc02c3 = ?, "
                + "doc02c6 = ?, "
                + "lab04c1 = ?, "
                + "doc02c7 = ? "
                + "WHERE lab22c1 = ? AND lab39c1 = ? AND LOWER(doc02c1) = ?";

        getConnection().update(query, timestamp, document.isViewresul() ? 1 : 0, document.getUser().getId(), document.getPath(), document.getIdOrder(), document.getIdTest(), document.getName().toLowerCase());
        document.setDate(timestamp);

        return document;
    }

    /**
     * Elimina nuevo documento de la orden en la base de datos
     *
     * @param document Instancia con los datos del recipiente.
     *
     * @throws Exception Error en base de datos
     */
    default void deleteOrderDocument(Document document) throws Exception
    {
        String query;
        query = ""
                + "DELETE FROM doc01 "
                + "WHERE lab22c1 = ? AND LOWER(doc01c1) = ?";
        getConnection().update(query, document.getIdOrder(), document.getName().toLowerCase());
    }

    /**
     * Elimina nuevo documento del resultado en la base de datos
     *
     * @param document Instancia con los datos del recipiente.
     *
     * @throws Exception Error en base de datos
     */
    default void deleteResultDocument(Document document) throws Exception
    {
        String query;
        query = ""
                + "DELETE FROM doc02  "
                + "WHERE lab22c1 = ? AND lab39c1 = ? AND LOWER(doc02c1) = ?";

        getConnection().update(query, document.getIdOrder(), document.getIdTest(), document.getName().toLowerCase());
    }
}
