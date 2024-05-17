/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.dao.interfaces.common;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.securitynt.dao.impl.postgresql.common.TrackingDaoPostgreSQL;
import net.cltech.securitynt.domain.common.Tracking;
import net.cltech.securitynt.domain.common.TrackingDetail;
import net.cltech.securitynt.domain.operation.common.AuditOperation;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * Auditoria.
 *
 * @author enavas
 * @since 19/04/2017
 * @see Creación
 */
public interface TrackingDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Registra un nuevo registro de auditoria en la base de datos
     *
     * @param tracking Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default void create(Tracking tracking) throws Exception
    {
        if (!tracking.getFields().isEmpty())
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab34")
                    .usingColumns("lab34c2", "lab04c1", "lab34c3", "lab34c4", "lab34c5", "lab34c6", "lab34c7", "lab34c8")
                    .usingGeneratedKeyColumns("lab34c1");

            HashMap parameters = new HashMap();
            parameters.put("lab34c2", tracking.getDate());
            parameters.put("lab04c1", tracking.getUserId());
            parameters.put("lab34c3", tracking.getUser());
            parameters.put("lab34c4", tracking.getUserName());
            parameters.put("lab34c5", tracking.getUrl());
            parameters.put("lab34c6", tracking.getHost());
            parameters.put("lab34c7", tracking.getModule());
            parameters.put("lab34c8", tracking.getState());
            Number key = insert.executeAndReturnKey(parameters);
            parameters.clear();
            createFileds(tracking.getFields(), key.intValue());
        }
    }

    default void createFileds(List<TrackingDetail> list, int key) throws Exception
    {
        final List<HashMap> batchArray = new ArrayList<>(0);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab36")
                .usingColumns("lab34c1", "lab36c2", "lab36c3", "lab36c4", "lab36c5")
                .usingGeneratedKeyColumns("lab36c1");
        list.stream()
                .forEachOrdered((TrackingDetail tradet) ->
                {
                    HashMap inserts = new HashMap();
                    inserts.put("lab34c1", key);
                    inserts.put("lab36c2", tradet.getField() == null ? "" : tradet.getField());
                    inserts.put("lab36c3", tradet.getOldValue());
                    inserts.put("lab36c4", tradet.getNewValue());
                    inserts.put("lab36c5", tradet.getFieldList());
                    batchArray.add(inserts);
                });
        insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()]));
    }

    /**
     * Obtener informacion de la auditoria en la base de datos
     *
     * @param initialDate Fecha inicial de la busqueda.
     * @param finalDate Fecha final de la busqueda.
     * @param module Nombre del modulo consultado.
     * @param iduser Id del usuario consultado.
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<Tracking> get(Timestamp initialDate, Timestamp finalDate, String module, Integer iduser) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab34.lab34c1"
                    + " ,lab34.lab34c2"
                    + " ,lab34.lab04c1"
                    + " ,lab34.lab34c3"
                    + " ,lab34.lab34c4"
                    + " ,lab34.lab34c5"
                    + " ,lab34.lab34c6"
                    + " ,lab34.lab34c7"
                    + " ,lab34.lab34c8"
                    + " FROM lab34"
                    + " WHERE lab34.lab34c2 BETWEEN ? AND ? ";
            //where
            if (module != null)
            {
                query += " AND UPPER(lab34.lab34c7) = ? ";
            }

            if (iduser != null)
            {
                query += " AND lab34.lab04c1 = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (module != null)
            {
                object = module.toUpperCase();
            }

            if (iduser != null)
            {
                object = iduser;
            }
            query += " ORDER BY lab34c2 ASC ";
            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        initialDate, finalDate, object
                    }, (ResultSet rs, int i) ->
            {
                Tracking tracking = new Tracking();
                Integer aud = null;
                aud = rs.getInt("lab34c1");
                tracking.setId(aud);
                tracking.setDate(rs.getTimestamp("lab34c2"));
                tracking.setUserId(rs.getInt("lab04c1"));
                tracking.setUser(rs.getString("lab34c3"));
                tracking.setUserName(rs.getString("lab34c4"));
                tracking.setUrl(rs.getString("lab34c5"));
                tracking.setHost(rs.getString("lab34c6"));
                tracking.setModule(rs.getString("lab34c7"));
                tracking.setState(rs.getString("lab34c8"));
                tracking.setType(AuditOperation.TYPE_MASTER);
                try
                {
                    tracking.setFields(getTrackingDetail(aud));
                } catch (Exception ex)
                {
                    Logger.getLogger(TrackingDaoPostgreSQL.class.getName()).log(Level.SEVERE, null, ex);
                }
                return tracking;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }

    }

    /**
     * Obtener informacion de la auditoria en la base de datos
     *
     * @param initialDate Fecha inicial de la busqueda.
     * @param finalDate Fecha final de la busqueda.
     * @param user
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<Tracking> get(Timestamp initialDate, Timestamp finalDate, Integer user) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab34.lab34c1"
                    + " ,lab34.lab34c2"
                    + " ,lab34.lab04c1"
                    + " ,lab34.lab34c3"
                    + " ,lab34.lab34c4"
                    + " ,lab34.lab34c5"
                    + " ,lab34.lab34c6"
                    + " ,lab34.lab34c7"
                    + " ,lab34.lab34c8"
                    + " FROM lab34"
                    + " WHERE  lab34.lab34c2 BETWEEN ? AND ? ";
            if (user != null)
            {
                query += " AND lab34.lab04c1 = " + user + " ";
            }
            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        initialDate, finalDate
                    }, (ResultSet rs, int i) ->
            {
                Tracking tracking = new Tracking();
                Integer aud = null;
                aud = rs.getInt("lab34c1");
                tracking.setId(aud);
                tracking.setDate(rs.getTimestamp("lab34c2"));
                tracking.setUserId(rs.getInt("lab04c1"));
                tracking.setUser(rs.getString("lab34c3"));
                tracking.setUserName(rs.getString("lab34c4"));
                tracking.setUrl(rs.getString("lab34c5"));
                tracking.setHost(rs.getString("lab34c6"));
                tracking.setModule(rs.getString("lab34c7"));
                tracking.setState(rs.getString("lab34c8"));
                try
                {
                    tracking.setFields(getTrackingDetail(aud));
                } catch (Exception ex)
                {
                    Logger.getLogger(TrackingDaoPostgreSQL.class.getName()).log(Level.SEVERE, null, ex);
                }
                return tracking;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener el detalle de la auditoria en la base de datos
     *
     * @param idAud Id de la auditoria.
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default List<TrackingDetail> getTrackingDetail(Integer idAud) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab36.lab34c1"
                    + " ,lab36.lab36c1"
                    + " ,lab36.lab36c2"
                    + " ,lab36.lab36c3"
                    + " ,lab36.lab36c4"
                    + " ,lab36.lab36c5"
                    + " FROM lab36"
                    + " WHERE  lab36.lab34c1 = ? ";
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";
            Object object = null;
            if (idAud != null)
            {
                object = idAud;
            }
            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                TrackingDetail trackingDetail = new TrackingDetail();
                trackingDetail.setId(rs.getInt("lab36c1"));
                trackingDetail.setIdAud(rs.getInt("lab34c1"));
                trackingDetail.setField(rs.getString("lab36c2"));
                trackingDetail.setOldValue(rs.getString("lab36c3"));
                trackingDetail.setNewValue(rs.getString("lab36c4"));
                trackingDetail.setFieldList(rs.getString("lab36c5"));
                return trackingDetail;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo registro de trazabilidad de operacion en la base de
     * datos
     *
     * @param audits Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    default void insertOperationTracking(List<AuditOperation> audits) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        final List<HashMap> batchArray = new ArrayList<>(0);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab03");
        audits.stream().forEach((AuditOperation audit) ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab22c1", audit.getOrder());
            parameters.put("lab03c1", audit.getId());
            parameters.put("lab03c2", audit.getAction());
            parameters.put("lab03c3", audit.getFieldType());
            parameters.put("lab03c4", audit.getInformation());
            parameters.put("lab03c5", timestamp);
            parameters.put("lab04c1", audit.getUser());
            parameters.put("lab30c1", audit.getMotive());
            parameters.put("lab03c6", audit.getComment());
            batchArray.add(parameters);
        });
        insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()]));
    }

}
