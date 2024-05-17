package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.PaymentType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Tipo de Pago.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 30/08/2017
 * @see Creación
 */
public interface PaymentTypeDao
{

    /**
     * Lista los tipos de pago desde la base de datos.
     *
     * @return Lista tipos de pago.
     * @throws Exception Error en la base de datos.
     */
    default List<PaymentType> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab01c1, lab01c2, lab01c3, lab01c4, lab01c5, lab01c6, lab01c7, lab01c8 "
                    + ", lab01.lab04c1, lab04c2, lab04c3, lab04c4, lab01.lab07c1 "
                    + "FROM lab01 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab01.lab04c1", (ResultSet rs, int i) ->
            {
                PaymentType rate = new PaymentType();
                rate.setId(rs.getInt("lab01c1"));
                rate.setName(rs.getString("lab01c2"));
                rate.setCode(rs.getString("lab01c8"));
                rate.setBank(rs.getInt("lab01c3") == 1);
                rate.setCard(rs.getInt("lab01c4") == 1);
                rate.setNumber(rs.getInt("lab01c5") == 1);
                rate.setAdjustment(rs.getInt("lab01c6") == 1);
                /*Usuario*/
                rate.getUser().setId(rs.getInt("lab04c1"));
                rate.getUser().setName(rs.getString("lab04c2"));
                rate.getUser().setLastName(rs.getString("lab04c3"));
                rate.getUser().setUserName(rs.getString("lab04c4"));

                rate.setLastTransaction(rs.getTimestamp("lab01c7"));
                rate.setState(rs.getInt("lab07c1") == 1);

                return rate;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra tipo de pago en la base de datos.
     *
     * @param type Instancia con los datos .
     *
     * @return Instancia con los datos .
     * @throws Exception Error en la base de datos.
     */
    default PaymentType create(PaymentType type) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab01")
                .usingGeneratedKeyColumns("lab01c1");

        HashMap parameters = new HashMap();
        parameters.put("lab01c2", type.getName());
        parameters.put("lab01c8", type.getCode());
        parameters.put("lab01c3", type.isBank() ? 1 : 0);
        parameters.put("lab01c4", type.isCard() ? 1 : 0);
        parameters.put("lab01c5", type.isNumber() ? 1 : 0);
        parameters.put("lab01c6", type.isAdjustment() ? 1 : 0);
        parameters.put("lab01c7", timestamp);
        parameters.put("lab04c1", type.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        type.setId(key.intValue());
        type.setLastTransaction(timestamp);

        return type;
    }

    /**
     * Actualiza la información en la base de datos.
     *
     * @param type Instancia con los datos .
     *
     * @return Objeto modificada.
     * @throws Exception Error en la base de datos.
     */
    default PaymentType update(PaymentType type) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab01 SET lab01c2 = ?, lab01c3 = ?, lab01c4 = ?, lab01c5 = ?, lab01c6 = ?, lab01c7 = ?, lab01c8 = ?, "
                + " lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab01c1 = ?",
                type.getName(), 
                type.isBank() ? 1 : 0, 
                type.isCard() ? 1 : 0, 
                type.isNumber() ? 1 : 0, 
                type.isAdjustment() ? 1 : 0, 
                timestamp, 
                type.getCode(), 
                type.getUser().getId(), 
                type.isState() ? 1 : 0, 
                type.getId());

        type.setLastTransaction(timestamp);

        return type;
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
