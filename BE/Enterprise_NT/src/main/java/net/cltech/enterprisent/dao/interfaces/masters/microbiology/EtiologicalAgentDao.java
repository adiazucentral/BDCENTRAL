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
import net.cltech.enterprisent.domain.masters.microbiology.EtiologicalAgent;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro de agentes etiologicos.
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/06/2022
 * @see Creación
 */
public interface EtiologicalAgentDao {
    
    /**
     * Lista agentes etiologicos desde la base de datos.
     *
     * @return Lista de agentes etiologicos.
     * @throws Exception Error en la base de datos.
     */
    default List<EtiologicalAgent> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + " SELECT lab213c1, lab213c2, lab213c3, lab213c4, lab213c5, lab213c6 "
                    + " ,lab213.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab213 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab213.lab04c1", (ResultSet rs, int i) ->
            {
                EtiologicalAgent bean = new EtiologicalAgent();
                bean.setId(rs.getInt("lab213c1"));
                bean.setSearchBy(rs.getInt("lab213c2"));
                bean.setMicroorganism(rs.getString("lab213c3"));
                bean.setCode(rs.getString("lab213c4"));
                bean.setClasification(rs.getInt("lab213c5"));
                bean.setLastTransaction(rs.getTimestamp("lab213c6"));
                
                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un agente etiologico en la base de datos.
     *
     * @param create Instancia con los datos del agente etiologico.
     *
     * @return Instancia con los datos del agente etiologico.
     * @throws Exception Error en la base de datos.
     */
    default EtiologicalAgent create(EtiologicalAgent create) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab213")
                .usingGeneratedKeyColumns("lab213c1");

        HashMap parameters = new HashMap();
        parameters.put("lab213c2", create.getSearchBy());
        parameters.put("lab213c3", create.getMicroorganism().trim());
        parameters.put("lab213c4", create.getCode().trim());
        parameters.put("lab213c5", create.getClasification());
        parameters.put("lab213c6", timestamp);
        parameters.put("lab04c1", create.getUser().getId());

        Number key = insert.executeAndReturnKey(parameters);
        create.setId(key.intValue());
        create.setLastTransaction(timestamp);

        return create;
    }

    /**
     * Actualiza un agente etiologico en la base de datos.
     *
     * @param update Instancia con los datos del agente etiologico.
     *
     * @return Objeto agente etiologico.
     * @throws Exception Error en la base de datos.
     */
    default EtiologicalAgent update(EtiologicalAgent update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab213 SET lab213c2 = ?, lab213c3 = ?, lab213c4 = ?, lab213c5 = ?, lab213c6 = ?, lab04c1 = ?"
                + "WHERE lab213c1 = ?",
                update.getSearchBy(), update.getMicroorganism().trim(), update.getCode().trim(), update.getClasification(), timestamp, update.getUser().getId(), update.getId());

        update.setLastTransaction(timestamp);
        return update;
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
    
}
