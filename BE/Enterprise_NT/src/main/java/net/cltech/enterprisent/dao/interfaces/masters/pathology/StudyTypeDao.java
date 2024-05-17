/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.pathology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de el maestro de tipo de estudio de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 26/10/2020
 * @see Creaciòn
 */
public interface StudyTypeDao 
{
    
    /**
    * Nuevo registro en la base de datos.
    *
    * @param studyType Instancia con los datos del tipo de estudio.
    *
    * @return {@link net.cltech.enterprisent.domain.masters.pathology.StudyType}
    * @throws Exception Error en la base de datos.
    */
    default StudyType create(StudyType studyType) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcPatTemplate())
                .withTableName("pat11")
                .usingColumns("pat11c2", "pat11c3", "pat11c4", "pat11c5", "lab04c1a")
                .usingGeneratedKeyColumns("pat11c1");

        Date createdAt = new Date();
        HashMap params = new HashMap();
        params.put("pat11c2", studyType.getCode().trim());
        params.put("pat11c3", studyType.getName().trim());
        params.put("pat11c4", studyType.getStatus());
        params.put("pat11c5", new Timestamp(createdAt.getTime()));
        params.put("lab04c1a", studyType.getUserCreated().getId());

        Number key = insert.executeAndReturnKey(params);

        studyType.setId(key.intValue());
        studyType.setCreatedAt(createdAt);
        createStudyTypeStudies(studyType);

        return studyType;
    }
    
    /**
    * Lista los tipos de estudios desde la base de datos.
    *
    * @return Lista de tipos de estudio.
    * @throws Exception Error en la base de datos.
    */
    default List<StudyType> list() throws Exception
    {
        try
        {
            String sql = "SELECT pat11c1, pat11c2, pat11c3, pat11c4, pat11c5, lab04c1a, pat11c6, lab04c1b "
                    + " FROM pat11 ";
            return getJdbcPatTemplate().query(sql, (ResultSet rs, int i) ->
            {
                StudyType studyType = new StudyType();
                
                studyType.setId(rs.getInt("pat11c1"));
                studyType.setCode(rs.getString("pat11c2"));
                studyType.setName(rs.getString("pat11c3"));
                studyType.setStatus(rs.getInt("pat11c4"));
                studyType.setStudies(listStudyTypeStudies(rs.getInt("pat11c1")));
                studyType.getUserCreated().setId(rs.getInt("lab04c1a"));
                studyType.getUserUpdated().setId(rs.getInt("lab04c1b"));
                studyType.setCreatedAt(rs.getTimestamp("pat11c5"));
                if (rs.getTimestamp("pat11c6") != null) {
                    studyType.setUpdatedAt(rs.getTimestamp("pat11c6"));
                }
                return studyType;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
    
    
    /**
    * Obtiene el estudio por id
    * 
    * @param study Id del estudio.
    *
    * @return Estudio.
    * @throws Exception Error en la base de datos.
    */
    default Study findStudyById(Integer study, Integer studyType) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat12c1, pat11c1, lab39c1 "
                    + "FROM pat12 WHERE lab39c1 = ? ";
            
            /*Where*/
            if (studyType != null)
            {
                query = query + "AND pat11c1 <> ?";
            }

            List parameters = new ArrayList(0);
            
            if (study != null)
            {
                parameters.add(study);
            }
            
            if (studyType != null)
            {
                parameters.add(studyType);
            }
            
            return getJdbcPatTemplate().queryForObject(query,
                    parameters.toArray(), (ResultSet rs, int i) ->
            {
                
                Study found = new Study();
                found.setId(rs.getInt("lab39c1"));
  
                return found;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
    * Actualiza la información del requisito en la base de datos.
    *
    *
    * @param studyType Instancia con los datos del tipo de estudio.
    *
    * @return tipo de estudio actualizado
    *
    * @throws Exception Error en la base de datos.
    */
    default StudyType update(StudyType studyType) throws Exception
    {
        Date modificationDate = new Date();
        getJdbcPatTemplate().update(" UPDATE pat11 \n"
                + " SET pat11c2 = ?, pat11c3 = ?, pat11c4 = ?, pat11c6 = ?, lab04c1b = ? \n"
                + " WHERE pat11c1 = ? ",
                studyType.getCode().trim(), studyType.getName().trim(), studyType.getStatus(), new Timestamp(modificationDate.getTime()),
                studyType.getUserUpdated().getId(), studyType.getId());
        studyType.setUpdatedAt(modificationDate);
        createStudyTypeStudies(studyType);
        return studyType;
    }
    
    /**
     * Nuevo registro de estudios por tipo de estudios en la base de datos.
     *
     * @param studyType Instancia con los datos del tipo de estudio.
     *
     * @return {@link net.cltech.enterprisent.domain.masters.pathology.StudyType}
     * @throws Exception Error en la base de datos.
     */
    default int createStudyTypeStudies(StudyType studyType) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();

        deleteStudyTypeStudies(studyType.getId());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcPatTemplate())
                .usingGeneratedKeyColumns("pat12c1")
                .withTableName("pat12");
        for (Study study : studyType.getStudies())
        {
            HashMap parameters = new HashMap();
            parameters.put("pat11c1", studyType.getId());
            parameters.put("lab39c1", study.getId());
            batchArray.add(parameters);
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[studyType.getStudies().size()]));

        return inserted.length;

    }
    
    
    /**
    * Obtiene estudios asociados al tipo de estudio.
    *
    * @param id id del tipo de estudio
    *
    * @return Lista de estudios
    */
    default List<Study> listStudyTypeStudies(Integer id)
    {
        try
        {
            String query = "SELECT pat12c1, pat11c1, lab39c1 "
                    + " FROM pat12 "
                    + " WHERE pat11c1 = ? ";

            return getJdbcPatTemplate().query(query, (ResultSet rs, int i) ->
            {
                /*Estudios*/
                Study study = new Study();

                String queryStudy = "SELECT lab39c1, lab39c2, lab39c3, lab39c4, lab39.lab24c1, lab24c2 "
                        + "FROM lab39 "
                        + "LEFT JOIN lab24 on lab39.lab24c1 = lab24.lab24c1 "
                        + "WHERE lab39c1 = ? ";

                study = getJdbcTemplate().queryForObject(queryStudy,
                        new Object[]{
                            rs.getInt("lab39c1")
                        }, (ResultSet rp, int j)
                        -> {
                            
                    Study studyData = new Study();
                    studyData.setId(rp.getInt("lab39c1"));
                    studyData.setCode(rp.getString("lab39c2"));
                    studyData.setName(rp.getString("lab39c4"));
                    studyData.setAbbr(rp.getString("lab39c3"));
                    studyData.setSample(rp.getInt("lab24c1"));
                    studyData.setSampleName(rp.getString("lab24c2"));

                    return studyData;
                });

                return study;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
    
    /**
    * Obtiene el tipo de estudio por estudio
    * 
    * @param study Id del estudio.
    *
    * @return Estudio.
    * @throws Exception Error en la base de datos.
    */
    default StudyType findstudyTypeByStudy(Integer study) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat11.pat11c1, pat11c2, pat11c3, pat11c4, pat11c5, lab04c1a, pat11c6, lab04c1b "
                    + " FROM pat11 "
                    + " JOIN pat12 ON pat12.pat11c1 = pat11.pat11c1 "
                    + " WHERE pat12.lab39c1 = ? ";

            List parameters = new ArrayList(0);
            
            if (study != null)
            {
                parameters.add(study);
            }
            
            return getJdbcPatTemplate().queryForObject(query,
                    parameters.toArray(), (ResultSet rs, int i) ->
            {
                StudyType studyType = new StudyType();
                
                studyType.setId(rs.getInt("pat11c1"));
                studyType.setCode(rs.getString("pat11c2"));
                studyType.setName(rs.getString("pat11c3"));
                studyType.setStatus(rs.getInt("pat11c4"));
                studyType.setStudies(listStudyTypeStudies(rs.getInt("pat11c1")));
                studyType.getUserCreated().setId(rs.getInt("lab04c1a"));
                studyType.getUserUpdated().setId(rs.getInt("lab04c1b"));
                studyType.setCreatedAt(rs.getTimestamp("pat11c5"));
                if (rs.getTimestamp("pat11c6") != null) {
                    studyType.setUpdatedAt(rs.getTimestamp("pat11c6"));
                }
                return studyType;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
     * Elimina asignación de estudios de un tipo de estudio
     *
     * @param idStudyType id del tipo de estudio
     *
     * @return número de registros afectados
     * @throws Exception Error BD
     */
    default int deleteStudyTypeStudies(Integer idStudyType) throws Exception
    {
        String query = " DELETE FROM pat12 WHERE pat11c1 = ?";

        return getJdbcPatTemplate().update(query, idStudyType);
    }
    
    /**
    * Obtiene la conexion JDBC de patologia
    *
    * @return conexion a base de datos
    */
    public JdbcTemplate getJdbcPatTemplate();
    
    /**
    * Obtiene la conexion JDBC
    *
    * @return conexion a base de datos
    */
    public JdbcTemplate getJdbcTemplate();
}
