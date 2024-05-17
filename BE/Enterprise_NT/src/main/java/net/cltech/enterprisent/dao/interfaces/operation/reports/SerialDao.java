/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.reports;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * seriales a imprimir.
 *
 *
 * @version 1.0.0
 * @author equijano
 * @since 04/03/2019
 * @see Creación
 */
public interface SerialDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Lista los seriales registrados en el sistema
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<String> list() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab106c1 FROM lab106");
            RowMapper mapper = (RowMapper<String>) (ResultSet rs, int i) ->
            {
                return rs.getString("lab106c1");
            };
            return getConnection().query(query.toString(), mapper);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Valida si existe serial en el sistema
     *
     * @param serial
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default boolean isSerial(String serial) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab106c1 FROM lab106 WHERE lab106c1 = ? ");
            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return true;
            }, serial);
        } catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Registra un nuevo serial en la base de datos.
     *
     *
     * @param serial
     * @param ip
     * @return serial.
     * @throws Exception Error en la base de datos.
     */
    default SerialPrint create(String serial, String ip) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab106");

        HashMap parameters = new HashMap();
        parameters.put("lab106c1", serial);
        parameters.put("lab106c2", ip);
        parameters.put("lab106c3", timestamp);

        insert.execute(parameters);
        SerialPrint serialPrint = new SerialPrint();
        serialPrint.setSerial(serial);
        serialPrint.setIp(ip);
        serialPrint.setDate(timestamp);
        return serialPrint;
    }

}
