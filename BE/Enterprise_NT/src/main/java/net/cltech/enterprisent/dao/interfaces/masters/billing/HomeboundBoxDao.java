package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.HomeboundBox;
import net.cltech.enterprisent.domain.masters.billing.HomeboundPayment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author jeibarbosa
 */
public interface HomeboundBoxDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene la caja homebound
     *
     * @param order Número de orden
     * @param userId
     * @return Lista de registros en caja para una orden
     * @throws Exception Error en base de datos
     */
    default List<HomeboundBox> get(long order, int userId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab902.lab902c1")
                    .append(", lab902.lab22c1")
                    .append(", lab902.lab902c2")
                    .append(", lab902.lab902c3")
                    .append(", lab902.lab902c4")
                    .append(", lab902.lab902c5")
                    .append(", lab902.lab902c6")
                    .append(", lab902.lab902c7")
                    .append(", lab902.lab902c8")
                    .append(", lab04.lab04c14")
                    .append(" FROM lab902 ")
                    .append("JOIN lab04 ON lab04.lab04c1 = ").append(userId)
                    .append(" WHERE lab902.lab22c1 = ").append(order);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int numRow) ->
            {
                HomeboundBox record = new HomeboundBox();
                record.setId(rs.getInt("lab902c1"));
                record.setOrder(rs.getLong("lab22c1"));
                record.setSubtotal(rs.getDouble("lab902c2"));
                record.setTax(rs.getDouble("lab902c5"));
                record.setTotalpayment(rs.getDouble("lab902c8"));
                record.setDiscount(rs.getDouble("lab902c3"));
                record.setDiscountPercent(rs.getDouble("lab902c4"));
                // Validación para el tipo de descuento:
                if (record.getDiscount() == null || record.getDiscount() == 0)
                {
                    // 0 - Porcentaje
                    record.setDiscountType(0);
                }
                else if (record.getDiscountPercent() == null || record.getDiscountPercent() == 0)
                {
                    // 1 - Valor
                    record.setDiscountType(1);
                }
                record.setCopay(rs.getDouble("lab902c6"));
                record.setUser(userId);
                record.setUserDiscount(rs.getDouble("lab04c14"));
                record.setPatientResponsability(rs.getDouble("lab902c7"));

                // Cargamos la lista del detalle de la caja
                try
                {
                    record.setPayments(getBoxDetailForHomebound(record.getOrder()));
                }
                catch (Exception e)
                {
                    record.setPayments(new ArrayList<>());
                }
                return record;
            });
        }
        catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el detalle de la caja para homebound
     *
     * @param order Número de orden
     *
     * @return Lista de registros del detalle de la caja para una orden
     * @throws Exception Error en base de datos
     */
    default List<HomeboundPayment> getBoxDetailForHomebound(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab903c1 AS id")
                    .append(", lab903c2 AS documentNo")
                    .append(", lab903c3 AS price")
                    .append(", lab01c1 AS paymentId")
                    .append(", lab59c1 AS cardId")
                    .append(", lab110c1 AS bankId")
                    .append(" FROM lab903 ")
                    .append("WHERE lab22c1 = ").append(order);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int numRow) ->
            {
                HomeboundPayment record = new HomeboundPayment();
                record.setId(rs.getInt("id"));
                record.setDocumentNo(rs.getString("DocumentNo"));
                record.setPrice(rs.getDouble("price"));
                record.setPaymentId(rs.getInt("paymentId"));
                record.setCardId(rs.getInt("cardId"));
                record.setBankId(rs.getInt("bankId"));
                return record;
            });
        }
        catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Registra una nueva caja.
     *
     * @param homeboundBox
     * @param rateId
     *
     * @return Instancia con los datos del registro en caja
     * @throws Exception Error en la base de datos.
     */
    default HomeboundBox create(HomeboundBox homeboundBox, int rateId) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab902")
                    .usingGeneratedKeyColumns("lab902c1");

            HashMap parameters = new HashMap();
            parameters.put("lab22c1", homeboundBox.getOrder());
            parameters.put("lab904c1", rateId);
            parameters.put("lab902c2", homeboundBox.getSubtotal());
            parameters.put("lab902c3", homeboundBox.getDiscount());
            parameters.put("lab902c4", homeboundBox.getDiscountPercent());
            parameters.put("lab902c5", homeboundBox.getTax());
            parameters.put("lab902c6", homeboundBox.getCopay());
            parameters.put("lab902c7", 0);
            parameters.put("lab902c8", homeboundBox.getTotalpayment());
            parameters.put("lab902c9", 0);
            parameters.put("lab04c1_i", homeboundBox.getUser());
            parameters.put("lab902c10", timestamp);

            Number key = insert.executeAndReturnKey(parameters);
            homeboundBox.setId(key.intValue());

            if (homeboundBox.getPayments() != null && !homeboundBox.getPayments().isEmpty())
            {
                createBoxDetails(homeboundBox.getPayments(), homeboundBox);
                insertTotalPayments(homeboundBox);
            }
            return homeboundBox;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Registra los detalles de la caja
     *
     * @param homeboundPayments
     * @param homeboundBox
     * @throws Exception Error en la base de datos.
     */
    default void createBoxDetails(List<HomeboundPayment> homeboundPayments, HomeboundBox homeboundBox) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab903")
                    .usingGeneratedKeyColumns("lab903c1");

            List<HashMap> records = new ArrayList<>();
            for (HomeboundPayment payment : homeboundPayments)
            {
                HashMap parameters = new HashMap();
                parameters.put("lab22c1", homeboundBox.getOrder());
                parameters.put("lab01c1", payment.getPaymentId());
                parameters.put("lab903c2", payment.getDocumentNo());
                parameters.put("lab110c1", payment.getBankId());
                parameters.put("lab59c1", payment.getCardId());
                parameters.put("lab903c3", payment.getPrice());
                parameters.put("lab07c1", 1);
                parameters.put("lab04c1_i", homeboundBox.getUser());
                parameters.put("lab903c4", timestamp);
                records.add(parameters);
            }

            insert.executeBatch(records.toArray(new HashMap[homeboundPayments.size()]));
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }

    /**
     * Actualiza un registra en caja.
     *
     * @param homeboundBox
     *
     * @return Instancia con los datos del registro en caja
     * @throws Exception Error en la base de datos.
     */
    default HomeboundBox updateBox(HomeboundBox homeboundBox) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            getJdbcTemplate().update("UPDATE lab902 "
                    + "SET      lab902c2 = ?"
                    + " , lab902c3 = ?"
                    + " , lab902c4 = ?"
                    + " , lab902c5 = ?"
                    + " , lab902c6 = ?"
                    + " , lab902c7 = ?"
                    + " , lab902c8 = ?"
                    + " , lab902c9 = ?"
                    + " , lab04c1_u = ?"
                    + " , lab902c11 = ?"
                    + " WHERE lab902c1 = ?",
                    homeboundBox.getSubtotal(),
                    homeboundBox.getDiscount(),
                    homeboundBox.getDiscountPercent(),
                    homeboundBox.getTax(),
                    homeboundBox.getCopay(),
                    0,
                    homeboundBox.getTotalpayment(),
                    0,
                    homeboundBox.getUser(),
                    timestamp,
                    homeboundBox.getId());
            if (homeboundBox.getPayments() != null && !homeboundBox.getPayments().isEmpty())
            {
                deleteBoxDetails(homeboundBox.getOrder());
                createBoxDetails(homeboundBox.getPayments(), homeboundBox);
                insertTotalPayments(homeboundBox);
            }
            return homeboundBox;
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
    
    
    /**
     * Elimina el detalle de una caja
     *
     * @param idOrder id de la orden asociada a la caja
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteBoxDetails(long idOrder) throws Exception {
        String query;
        query = ""
                + " DELETE FROM lab903 WHERE lab22c1 = " + idOrder;
        getJdbcTemplate().execute(query);
    }
    
    /**
     * Crea un registro del total a pagar por una caja
     *
     * @param 
     * @return {@link net.cltech.enterprisent.domain.operation.orders.billing.FullPayment} con su respectivo id
     * @throws Exception Error en base de datos
     */
    default HomeboundBox insertTotalPayments(HomeboundBox homeboundBox) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab908");
        HashMap<String, Object> parameters = new HashMap<>(0);
        parameters.put("lab22c1", homeboundBox.getOrder());
        parameters.put("lab908c1", homeboundBox.getTotalpayment());
        parameters.put("lab908c2", homeboundBox.getSubtotal() - homeboundBox.getDiscount());
        parameters.put("lab908c3", homeboundBox.getTotalpayment() - (homeboundBox.getSubtotal() - homeboundBox.getDiscount()));
        parameters.put("lab908c5", homeboundBox.getDiscount());
        parameters.put("lab908c6", homeboundBox.getDiscountPercent());
        parameters.put("lab908c4", timestamp);
        parameters.put("lab908c7", homeboundBox.getCopay());
        parameters.put("lab908c8", homeboundBox.getPatientResponsability());
        parameters.put("lab04c1", homeboundBox.getUser());
        int rows = insert.execute(parameters);
        return rows > 0 ? homeboundBox : null;
    }
}
