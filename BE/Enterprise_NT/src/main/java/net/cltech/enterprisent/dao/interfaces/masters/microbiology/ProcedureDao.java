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
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.microbiology.TestProcedure;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * Procedimiento.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/08/2017
 * @see Creación
 */
public interface ProcedureDao
{

    /**
     * Lista los procedimientos desde la base de datos.
     *
     * @return Lista de procedimientos.
     * @throws Exception Error en la base de datos.
     */
    default List<Procedure> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab156c1, lab156c2, lab156c3, lab156c4, lab156.lab04c1, lab04c2, lab04c3, lab04c4, lab156.lab07c1 "
                    + "FROM lab156 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab156.lab04c1", (ResultSet rs, int i) ->
            {
                Procedure procedure = new Procedure();
                procedure.setId(rs.getInt("lab156c1"));
                procedure.setCode(rs.getString("lab156c2"));
                procedure.setName(rs.getString("lab156c3"));
                /*Usuario*/
                procedure.getUser().setId(rs.getInt("lab04c1"));
                procedure.getUser().setName(rs.getString("lab04c2"));
                procedure.getUser().setLastName(rs.getString("lab04c3"));
                procedure.getUser().setUserName(rs.getString("lab04c4"));

                procedure.setLastTransaction(rs.getTimestamp("lab156c4"));
                procedure.setState(rs.getInt("lab07c1") == 1);

                return procedure;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo procedimiento en la base de datos.
     *
     * @param procedure Instancia con los datos del procedimiento.
     *
     * @return Instancia con las datos de la procedimiento.
     * @throws Exception Error en la base de datos.
     */
    default Procedure create(Procedure procedure) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab156")
                .usingGeneratedKeyColumns("lab156c1");

        HashMap parameters = new HashMap();
        parameters.put("lab156c2", procedure.getCode().trim());
        parameters.put("lab156c3", procedure.getName().trim());
        parameters.put("lab156c4", timestamp);
        parameters.put("lab04c1", procedure.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        procedure.setId(key.intValue());
        procedure.setLastTransaction(timestamp);

        return procedure;
    }

    /**
     * Obtener información de un procedimiento por un campo especifico.
     *
     * @param id ID del procedimiento a ser consultado.
     * @param code Codigo del procedimiento a ser consultado.
     * @param name Nombre del procedimiento a ser consultado.
     *
     * @return Instancia con las datos de la procedimiento.
     * @throws Exception Error en la base de datos.
     */
    default Procedure get(Integer id, String code, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab156c1, lab156c2, lab156c3, lab156c4, lab156.lab04c1, lab04c2, lab04c3, lab04c4, lab156.lab07c1 "
                    + "FROM lab156 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab156.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab156c1 = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(lab156c2) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab156c3) = ? ";
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
                Procedure procedure = new Procedure();
                procedure.setId(rs.getInt("lab156c1"));
                procedure.setCode(rs.getString("lab156c2"));
                procedure.setName(rs.getString("lab156c3"));
                /*Usuario*/
                procedure.getUser().setId(rs.getInt("lab04c1"));
                procedure.getUser().setName(rs.getString("lab04c2"));
                procedure.getUser().setLastName(rs.getString("lab04c3"));
                procedure.getUser().setUserName(rs.getString("lab04c4"));

                procedure.setLastTransaction(rs.getTimestamp("lab156c4"));
                procedure.setState(rs.getInt("lab07c1") == 1);

                return procedure;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un procedimiento en la base de datos.
     *
     * @param procedure Instancia con los datos del procedimiento.
     *
     * @return Objeto del procedimiento modificada.
     * @throws Exception Error en la base de datos.
     */
    default Procedure update(Procedure procedure) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab156 SET lab156c2 = ?, lab156c3 = ?, lab156c4 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab156c1 = ?",
                procedure.getCode().trim(), procedure.getName().trim(), timestamp, procedure.getUser().getId(), procedure.isState() ? 1 : 0, procedure.getId());

        procedure.setLastTransaction(timestamp);

        return procedure;
    }

    /**
     *
     * Elimina un procedimiento de la base de datos.
     *
     * @param id ID del procedimiento.
     *
     * @throws Exception Error en base de datos.
     */
    default void delete(Integer id) throws Exception
    {

    }

    /**
     * Eliminar los procedimientos asociadas a una prueba.
     *
     * @param idtest id del examen
     *
     * @return registros eliminados
     */
    default int deleteTestProcedure(Integer idtest)
    {
        String deleteSql = "DELETE FROM lab157 WHERE lab39c1 = " + idtest;
        return getJdbcTemplate().update(deleteSql);
    }

    /**
     * Asocia los procedimientos a una prueba.
     *
     * @param testProcedures lista de examenes con procedimientos
     *
     * @return registros asociados
     */
    default int insertTestProcedure(List<TestProcedure> testProcedures)
    {
        deleteTestProcedure(testProcedures.get(0).getTest().getId());
        List<HashMap> batchArray = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab157");
        for (TestProcedure test : testProcedures)
        {
            if (test.getProcedure().getId() != null && test.getProcedure().isSelected())
            {
                HashMap parameters = new HashMap();
                parameters.put("lab39c1", test.getTest().getId());
                parameters.put("lab156c1", test.getProcedure().getId());
                parameters.put("lab157c1", test.getProcedure().isDefaultvalue() ? 1 : 0);
                parameters.put("lab157c2", test.getProcedure().getConfirmatorytest());
                parameters.put("lab157c3", timestamp);
                parameters.put("lab04c1", test.getUser().getId());
                batchArray.add(parameters);
            }
        }

        return !batchArray.isEmpty() ? insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()])).length : 0;
    }

    /**
     * lista examenes relacionados al procedimientos
     *
     * @param idtest id del examen
     *
     * @return lista de examenes relacionados con procedimientos
     */
    default List<TestProcedure> listTestProcedure(Integer idtest)
    {
        try
        {
            String query = "SELECT lab156.lab156c1, lab156c2, lab156c3, lab39.lab39c1, lab39.lab39c2, lab39.lab39c4, lab157c1, lab157c2, lab157.lab39c1 AS exa, lab39_2.lab39c4 AS exaName "
                    + ", lab157.lab04c1, lab04c2, lab04c3, lab04c4, lab157c3 "
                    + " FROM lab156"
                    + " LEFT JOIN  lab157 ON lab157.lab156c1 = lab156.lab156c1 AND lab157.lab39c1 = ? "
                    + " LEFT JOIN  lab39 ON lab39.lab39c1 = lab157.lab157c2"
                    + " LEFT JOIN  lab39 AS lab39_2 ON lab39_2.lab39c1 = lab157.lab39c1"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab157.lab04c1 "
                    + " WHERE lab156.lab07c1 = 1";
            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idtest
                    }, (ResultSet rs, int i) ->
            {
                TestProcedure procedure = new TestProcedure();
                /*Procedimiento*/
                procedure.getProcedure().setConfirmatorytest(rs.getInt("lab157c2"));
                procedure.getProcedure().setConfirmatorytestcode(rs.getString("lab39c2"));
                procedure.getProcedure().setConfirmatorytestname(rs.getString("lab39c4"));
                procedure.getProcedure().setDefaultvalue(rs.getInt("lab157c1") == 1);
                procedure.getProcedure().setSelected(rs.getString("lab157c1") != null);
                procedure.getProcedure().setName(rs.getString("lab156c3"));
                procedure.getProcedure().setCode(rs.getString("lab156c2"));
                procedure.getProcedure().setId(rs.getInt("lab156c1"));

                procedure.getTest().setId(rs.getInt("exa"));
                procedure.getTest().setName(rs.getString("exaName"));
                
                procedure.setLastTransaction(rs.getTimestamp("lab157c3"));
                /*Usuario*/
                procedure.getUser().setId(rs.getInt("lab04c1"));
                procedure.getUser().setName(rs.getString("lab04c2"));
                procedure.getUser().setLastName(rs.getString("lab04c3"));
                procedure.getUser().setUserName(rs.getString("lab04c4"));
                
                return procedure;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
