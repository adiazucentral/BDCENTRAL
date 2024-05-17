/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.configuration;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.OrderGrouping;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la
 * Agrupacion de ordenes
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/03/2019
 * @see Creación
 */
public interface OrderGroupingDao
{

    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene todas las agrupaciones de la orden por servicio
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.configuration.OrderGrouping}
     * @throws Exception Error en base de datos
     */
    default List<OrderGrouping> listByService() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT ");
            query.append("lab801c1, lab801c3, lab801c4, lab801.lab04c1, lab10c1, lab10c2, lab10c7, lab10c5 ");
            query.append("FROM lab801 ");
            query.append("INNER JOIN lab10 ON lab801.lab801c2 = lab10.lab10c1 ");
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                OrderGrouping orderGrouping = new OrderGrouping();
                orderGrouping.setColumn(rs.getInt("lab801c1"));
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                orderGrouping.setUser(user);
                orderGrouping.setDate(rs.getTimestamp("lab801c3"));
                ServiceLaboratory serviceLaboratory = new ServiceLaboratory();
                serviceLaboratory.setId(rs.getInt("lab10c1"));
                serviceLaboratory.setCode(rs.getString("lab10c7").trim());
                serviceLaboratory.setName(rs.getString("lab10c2"));
                serviceLaboratory.setExternal(rs.getInt("lab10c5") == 1);
                orderGrouping.setService(serviceLaboratory);
                orderGrouping.setColumnName(rs.getString("lab801c4"));
                return orderGrouping;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene todas las agrupaciones de la orden por tipo de orden
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.configuration.OrderGrouping}
     * @throws Exception Error en base de datos
     */
    default List<OrderGrouping> listByOrderType() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT ");
            query.append("lab801c1, lab801c3, lab801c4, lab801.lab04c1, lab103c1, lab103c2, lab103c3, lab103c4 ");
            query.append("FROM lab801 ");
            query.append("INNER JOIN lab103 ON lab801.lab801c2 = lab103.lab103c1 ");
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                OrderGrouping orderGrouping = new OrderGrouping();
                orderGrouping.setColumn(rs.getInt("lab801c1"));
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                orderGrouping.setUser(user);
                orderGrouping.setDate(rs.getTimestamp("lab801c3"));
                OrderType orderType = new OrderType();
                orderType.setId(rs.getInt("lab103c1"));
                orderType.setCode(rs.getString("lab103c2"));
                orderType.setName(rs.getString("lab103c3"));
                orderType.setColor(rs.getString("lab103c4") == null ? null : rs.getString("lab103c4"));
                orderGrouping.setOrderType(orderType);
                orderGrouping.setColumnName(rs.getString("lab801c4"));
                return orderGrouping;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta las agrupaciones de la orden
     *
     * @param list
     * @param groupType
     * @return Numero de registros
     * @throws Exception Error en base de datos
     */
    default int insertGroups(List<OrderGrouping> list, String groupType) throws Exception
    {
        final List<HashMap> batchArray = new ArrayList<>(0);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab801");
        list.stream().forEachOrdered((orderGrouping) ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab801c1", orderGrouping.getColumn());
            if (groupType.equals(Constants.ORDER_GROUPING_SERVICE))
            {
                parameters.put("lab801c2", orderGrouping.getService().getId());
            } else
            {
                parameters.put("lab801c2", orderGrouping.getOrderType().getId());
            }
            parameters.put("lab801c3", timestamp);
            parameters.put("lab801c4", orderGrouping.getColumnName());
            parameters.put("lab04c1", orderGrouping.getUser().getId());
            batchArray.add(parameters);
        });
        return insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()])).length;
    }

    /**
     * Elimina todas las agrupaciones registradas
     *
     * @throws Exception Error en base de datos
     */
    default void deleteGroups() throws Exception
    {
        getJdbcTemplate().update("DELETE FROM lab801");
    }

}
