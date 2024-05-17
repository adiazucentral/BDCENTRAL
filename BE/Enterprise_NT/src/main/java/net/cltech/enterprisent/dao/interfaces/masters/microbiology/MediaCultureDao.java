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
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCultureTest;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Medio de cultivo.
 *
 * @version 1.0.0
 * @author enavas
 * @since 10/08/2017
 * @see Creación
 */
public interface MediaCultureDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista los medios de cultivo desde la base de datos.
     *
     * @return Lista de medios de cultivo.
     * @throws Exception Error en la base de datos.
     */
    default List<MediaCulture> list() throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab155.lab155c1"
                    + " , lab155.lab155c2"
                    + " , lab155.lab155c3"
                    + " , lab155.lab07c1"
                    + " , lab155.lab155c4"
                    + " , lab155.lab04c1"
                    + " FROM lab155"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab155.lab04c1";
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                MediaCulture mediaCulture = new MediaCulture();

                mediaCulture.setId(rs.getInt("lab155c1"));
                mediaCulture.setCode(rs.getString("lab155c2"));
                mediaCulture.setName(rs.getString("lab155c3"));
                mediaCulture.setState(rs.getInt("lab07c1") == 1);
                /*Usuario*/
                mediaCulture.getUser().setId(rs.getInt("lab04c1"));
                mediaCulture.setLastTransaction(rs.getTimestamp("lab155c4"));
                return mediaCulture;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener información de un medio de cultivo por un campo especifico.
     *
     * @param id ID del medio de cultivo a ser consultada.
     * @param code Codigo del medio de cultivo a ser consultada.
     * @param name Nombre del medio de cultivo a ser consultada.
     *
     *
     * @return Instancia con las datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    default MediaCulture get(Integer id, String code, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = ""
                    + " SELECT lab155.lab155c1"
                    + " , lab155.lab155c2"
                    + " , lab155.lab155c3"
                    + " , lab155.lab07c1"
                    + " , lab155.lab155c4"
                    + " , lab155.lab04c1"
                    + " , lab04.lab04c4"
                    + " FROM lab155"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab155.lab04c1";
            /*Where*/
            if (id != null)
            {
                query = query + " WHERE lab155.lab155c1 = ? ";
            }
            if (code != null)
            {
                query = query + " WHERE UPPER(lab155.lab155c2) = ? ";
            }
            if (name != null)
            {
                query = query + " WHERE UPPER(lab155.lab155c3) = ? ";
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

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                MediaCulture mediaCulture = new MediaCulture();
                mediaCulture.setId(rs.getInt("lab155c1"));
                mediaCulture.setCode(rs.getString("lab155c2"));
                mediaCulture.setName(rs.getString("lab155c3"));
                mediaCulture.setState(rs.getInt("lab07c1") == 1);
                /*Usuario*/
                mediaCulture.getUser().setId(rs.getInt("lab04c1"));
                mediaCulture.setLastTransaction(rs.getTimestamp("lab155c4"));
                mediaCulture.getUser().setUserName(rs.getString("lab04c4"));
                return mediaCulture;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Registra una nuevo medio de cultivo en la base de datos.
     *
     * @param mediaCulture Instancia con los datos del medio de cultivo.
     *
     * @return Instancia con las datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    default MediaCulture create(MediaCulture mediaCulture) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab155")
                .usingGeneratedKeyColumns("lab155c1");

        HashMap parameters = new HashMap();
        parameters.put("lab155c2", mediaCulture.getCode().trim());
        parameters.put("lab155c3", mediaCulture.getName());
        parameters.put("lab07c1", 1);
        parameters.put("lab155c4", timestamp);
        parameters.put("lab04c1", mediaCulture.getUser().getId());

        Number key = insert.executeAndReturnKey(parameters);
        mediaCulture.setId(key.intValue());
        mediaCulture.setLastTransaction(timestamp);

        return mediaCulture;
    }

    /**
     * Actualiza la información de un medio de cultivo en la base de datos.
     *
     * @param mediaCulture Instancia con los datos del medio de cultivo.
     *
     * @return Instancia con las datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    default MediaCulture update(MediaCulture mediaCulture) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        String query = ""
                + " UPDATE lab155"
                + " SET  lab155c2=?"
                + " , lab155c3=?"
                + " , lab07c1=?"
                + " , lab155c4=?"
                + " , lab04c1=?"
                + " WHERE lab155c1=?";

        getJdbcTemplate().update(query,
                mediaCulture.getCode(),
                mediaCulture.getName(),
                mediaCulture.isState() ? 1 : 0,
                timestamp,
                mediaCulture.getUser().getId(),
                mediaCulture.getId());
        mediaCulture.setLastTransaction(timestamp);

        return mediaCulture;
    }

    /**
     * Lista los medios de cultivo por prueba desde la base de datos.
     *
     * @param id Id del examen
     * @return Lista de medios de cultivo por prueba.
     * @throws Exception Error en la base de datos.
     */
    default MediaCultureTest listMediacultureTest(Integer id) throws Exception
    {
        try
        {
            MediaCultureTest mediaCultureTest = new MediaCultureTest();
            String query = ""
                    + " SELECT  lab155.lab155c1"
                    + " , lab155.lab155c2"
                    + " , lab155.lab155c3"
                    + " , lab155.lab155c4"
                    + " , lab164.lab164c1"
                    + " , lab155.lab04c1"
                    + " FROM lab155"
                    + " LEFT JOIN lab164 ON lab164.lab155c1 = lab155.lab155c1 AND lab164.lab39c1 = ? "
                    + " WHERE lab155.lab07c1 = 1";
            mediaCultureTest.setMediaCultures(getJdbcTemplate().query(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                MediaCulture mediaCulture = new MediaCulture();

                /*Medio de cultivo*/
                mediaCulture.setId(rs.getInt("lab155c1"));
                mediaCulture.setCode(rs.getString("lab155c2"));
                mediaCulture.setName(rs.getString("lab155c3"));
                mediaCulture.setState(true);
                mediaCulture.setSelect(rs.getString("lab164c1") != null);
                mediaCulture.setDefectValue(rs.getInt("lab164c1") == 1);
                /*Usuario*/
                mediaCulture.getUser().setId(rs.getInt("lab04c1"));
                mediaCulture.setLastTransaction(rs.getTimestamp("lab155c4"));
                return mediaCulture;
            }));
            mediaCultureTest.setTestId(id);
            return mediaCultureTest;
        } catch (EmptyResultDataAccessException ex)
        {
            return new MediaCultureTest();
        }
    }

    /**
     * Eliminar los medios de cultivo asociadas a una prueba.
     *
     * @param idTest Id de la prueba.
     *
     * @throws java.lang.Exception
     */
    default void deleteMediaCulture(Integer idTest) throws Exception
    {
        getJdbcTemplate().execute("DELETE FROM lab164 WHERE lab39c1 = " + idTest);
    }

    /**
     * Asociar los medios de cultivos a una prueba.
     *
     * @param mediaCultureTest Instancia con los datos de la prueba y medios de
     * cultivo.
     * @return cantidad de registros insertados
     * @throws java.lang.Exception
     */
    default int insertMediaCulture(MediaCultureTest mediaCultureTest) throws Exception
    {
        deleteMediaCulture(mediaCultureTest.getTestId());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab164");
        for (MediaCulture mediaCulture : mediaCultureTest.getMediaCultures())
        {
            HashMap parameters = new HashMap();
            parameters.put("lab155c1", mediaCulture.getId());
            parameters.put("lab39c1", mediaCultureTest.getTestId());
            parameters.put("lab164c1", mediaCulture.isDefectValue() ? 1 : 0);
            batchArray.add(parameters);
        }
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[mediaCultureTest.getMediaCultures().size()]));
        return inserted.length;
    }

    /**
     * Obtiene el examen que este asociado a la muestra.
     *
     * @param test Id del examen.
     * @return Examen asociado al medio de cultivo.
     * @throws java.lang.Exception
     */
    default TestBasic getMediaCultureTest(int test) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT DISTINCT lab39.lab39c1, "
                    + "         lab39.lab39c2, "
                    + "         lab39.lab39c3, "
                    + "         lab39.lab39c4 "
                    + "FROM lab39 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "INNER JOIN lab164 ON lab164.lab39c1 = lab39.lab39c1 "
                    + "WHERE lab24.lab24c1 = (SELECT lab24c1 FROM lab39 WHERE lab39.lab39c1 = ?) ";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        test
                    }, (ResultSet rs, int i) ->
            {
                TestBasic test1 = new TestBasic();
                test1.setId(rs.getInt("lab39c1"));
                test1.setCode(rs.getString("lab39c2"));
                test1.setAbbr(rs.getString("lab39c3"));
                test1.setName(rs.getString("lab39c4"));
                return test1;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
}
