/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.File;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para obtener los documentos asociados a los casos.
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/03/2021
 * @see Creación
 */
public interface FileDao 
{
    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplatePat();
    
    /**
    * Lista los documentos de un caso
    *
    * @param casePat Caso.
    *
    * @return Lista de documentos.
    * @throws Exception Error en la base de datos.
    */
    default List<File> list(Integer casePat) throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat20c1, pat01c1, pat20c2, pat20c3, pat20c4, pat20c5, lab04c1a, pat20c6, lab04c1b, pat20c7 "
                    + "FROM pat20 "
                    + "WHERE pat01c1 = ?",
                    new Object[]
                    {
                        casePat
                    }, (ResultSet rs, int i) ->
            {
                File file = new File();
                file.setIdCase(rs.getInt("pat01c1"));
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("pat20c2");
                if (ImaBytes != null)
                {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                file.setFile(Imabas64);
                file.setName(rs.getString("pat20c3"));
                file.setFileType(rs.getString("pat20c4"));
                file.setExtension(rs.getString("pat20c5"));
                file.getUserCreated().setId(rs.getInt("lab04c1a"));
                file.setCreatedAt(rs.getTimestamp("pat20c6"));
                file.getUserUpdated().setId(rs.getInt("lab04c1b"));
                if(rs.getTimestamp("pat20c7") != null) {
                    file.setUpdatedAt(rs.getTimestamp("pat20c7"));
                }
                return file;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Registra un nuevo documento del caso en la base de datos
     *
     * @param file Instancia con los datos del documento.
     *
     * @return Instancia con los datos del documento.
     * @throws Exception Error en base de datos
     */
    default File saveCaseDocument(File file) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat20")
                .usingGeneratedKeyColumns("pat20c1");

        HashMap parameters = new HashMap();
        parameters.put("pat01c1", file.getIdCase());
        byte[] ImaBytes = Base64.getDecoder().decode(file.getFile());
        parameters.put("pat20c2", ImaBytes);
        parameters.put("pat20c3", file.getName());
        parameters.put("pat20c4", file.getFileType());
        parameters.put("pat20c5", file.getExtension());
        parameters.put("lab04c1a", file.getUserCreated().getId());
        parameters.put("pat20c6", timestamp);
        insert.execute(parameters);
        file.setCreatedAt(timestamp);
        return file;
    }
    
    /**
     * Registra un nuevo documento del caso en la base de datos
     *
     * @param file Instancia con los datos del documento.
     *
     * @return Instancia con los datos del documento.
     * @throws Exception Error en base de datos
     */
    default File updateCaseDocument(File file) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        byte[] ImaBytes = Base64.getDecoder().decode(file.getFile());

        String query;
        query = ""
                + "UPDATE pat20 SET "
                + "pat20c2 = ?, "
                + "pat20c7 = ?, "
                + "lab04c1b = ? "
                + "WHERE pat01c1 = ? AND LOWER(pat20c3) = ?";
        getJdbcTemplatePat().update(query, ImaBytes, timestamp, file.getUserUpdated().getId(), file.getIdCase(), file.getName().toLowerCase());
        return file;
    }
    
    /**
     * Elimina un documento del caso en la base de datos
     *
     * @param file Instancia con los datos del documento.
     *
     * @throws Exception Error en base de datos
     */
    default void delete(File file) throws Exception
    {
        String query;
        query = ""
                + "DELETE FROM pat20 "
                + "WHERE pat01c1 = ? AND LOWER(pat20c3) = ?";
        getJdbcTemplatePat().update(query, file.getIdCase(), file.getName().toLowerCase());
    }
}
