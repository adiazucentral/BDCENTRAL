/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.orders;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Recalled;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Interfaz de acceso a datos para todo lo relacionado con rellamado de ordenes
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/08/2019
 * @see Creación
 */
public interface RecalledDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Registra un rellamado de orden
     *
     * @param recalled
     * {@link net.cltech.enterprisent.domain.operation.orders.Recalled}
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Recalled}
     * @throws Exception Error en base de datos
     */
    default Recalled create(Recalled recalled) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab221");
        HashMap<String, Object> parameters = new HashMap<>(0);
        parameters.put("lab22c1_1", recalled.getFatherOrder().getOrderNumber());
        parameters.put("lab22c1_2", recalled.getDaughterOrder().getOrderNumber());
        if (insert.execute(parameters) > 0)
        {
            return recalled;
        } else
        {
            return null;
        }
    }

    /**
     * Obtiene los rellamados de ordenes
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Recalled}, vacio
     * en caso de no tener ordenes
     * @throws Exception Error en base de datos
     */
    default List<Recalled> list() throws Exception
    {
        try
        {
            final StringBuilder select = new StringBuilder();
            final StringBuilder from = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   lab22c1_1 , lab22c1_2 ");
            from.append(" FROM     lab221 ");
            return getJdbcTemplate().query(select.toString() + from.toString(), (RowMapper<Recalled>) (ResultSet rs, int i) ->
            {
                Recalled recalled = new Recalled();
                recalled.setFatherOrder(new Order());
                recalled.getFatherOrder().setOrderNumber(rs.getLong("lab22c1_1"));
                recalled.setDaughterOrder(new Order());
                recalled.getDaughterOrder().setOrderNumber(rs.getLong("lab22c1_2"));
                return recalled;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

}
