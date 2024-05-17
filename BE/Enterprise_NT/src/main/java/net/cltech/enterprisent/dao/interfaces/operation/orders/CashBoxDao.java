package net.cltech.enterprisent.dao.interfaces.operation.orders;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Bank;
import net.cltech.enterprisent.domain.masters.billing.Card;
import net.cltech.enterprisent.domain.masters.billing.PaymentType;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.billing.integration.CashBoxBilling;
import net.cltech.enterprisent.domain.operation.billing.integration.PaymentBilling;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader;
import net.cltech.enterprisent.domain.operation.orders.billing.FullPayment;
import net.cltech.enterprisent.domain.operation.orders.billing.Invoice;
import net.cltech.enterprisent.domain.operation.orders.billing.Payment;
import net.cltech.enterprisent.tools.Constants;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Metodos de acceso a base de datos para caja
 *
 * @version 1.0.0
 * @author dcortes
 * @since 27/04/2018
 * @see Creaciòn
 */
public interface CashBoxDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Inserta los valores de caja
     *
     * @param header Referencia a {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader}
     * @return Arreglo con los registros afectados
     * @throws Exception Error en base de datos
     */
    default CashBoxHeader insert(CashBoxHeader header) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(
                getJdbcTemplate()).withTableName("lab902").usingGeneratedKeyColumns("lab902c1");
        
        HashMap<String, Object> parameters = new HashMap<>(0);
        parameters.put("lab22c1", header.getOrder());
        parameters.put("lab904c1", header.getRate().getId());
        parameters.put("lab902c2", header.getSubTotal());
        parameters.put("lab902c3", header.getDiscountValue());
        parameters.put("lab902c4", header.getDiscountPercent());
        parameters.put("lab902c5", header.getTaxValue());
        parameters.put("lab902c6", header.getCopay());
        parameters.put("lab902c7", header.getFee());
        parameters.put("lab902c8", header.getTotalPaid());
        parameters.put("lab902c9", header.getBalance());
        parameters.put("lab04c1_i", header.getEntryUser().getId());
        parameters.put("lab902c12", header.getDiscountValueRate());
        parameters.put("lab902c13", header.getDiscountPercentRate());
        parameters.put("lab902c10", new Timestamp(header.getEntryDate().getTime()));
        parameters.put("lab902c14", header.getCopayType());
        parameters.put("lab902c15", header.getCharge());
        parameters.put("lab902c16", header.getBilled());
        parameters.put("lab902c17", header.getRuc());
        parameters.put("lab902c18", header.getTypeCredit());
        parameters.put("lab902c19", header.getComboBills() == 0 ? Constants.SEND : Constants.UNBILLED);
        parameters.put("lab902c20", header.getComboBills());
        parameters.put("lab902c21", header.getPhone());
        parameters.put("lab902c22", header.getBalanceCustomer());

        int rows = insert.execute(parameters);
        return rows > 0 ? header : null;
    }

    /**
     * Inserta los valores de caja
     *
     * @param header Referencia de {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader}
     * @return Cabecera de la caja
     * @throws Exception Error en base de datos
     */
    default CashBoxHeader update(CashBoxHeader header) throws Exception
    {
        getJdbcTemplate().update(
                "UPDATE lab902 "
                        + "SET lab902c2 = ?, "
                        + "lab902c3 = ?, "
                        + "lab902c4 = ?, "
                        + "lab902c5 = ?, "
                        + "lab902c6 = ?, "
                        + "lab902c7 = ?, "
                        + "lab902c8 = ?, "
                        + "lab902c9 = ?, "
                        + "lab04c1_u = ?, "
                        + "lab902c11 = ?, "
                        + "lab902c12 = ?, "
                        + "lab902c13 = ?, "
                        + "lab902c14 = ?, "
                        + "lab902c15 = ?, "
                        + "lab902c16 = ?, "
                        + "lab902c17 = ?, "
                        + "lab902c21 = ?, "
                        + "lab902c18 = ?, "
                        + "lab902c19 = ?, "                      
                        + "lab902c20 = ?, "
                        + "lab902c22 = ? "
                + "WHERE lab902c1 = ?",
                header.getSubTotal(), 
                header.getDiscountValue(), 
                header.getDiscountPercent(), 
                header.getTaxValue(), 
                header.getCopay(), 
                header.getFee(),
                header.getTotalPaid(), 
                header.getBalance(), 
                header.getUpdateUser().getId(),
                new Timestamp(header.getUpdateDate().getTime()),
                header.getDiscountValueRate(),
                header.getDiscountPercentRate(),
                header.getCopayType(),
                header.getCharge(),
                header.getBilled(),
                header.getRuc(),
                header.getPhone(),
                header.getTypeCredit(),
                header.getComboBills() == 0 ? Constants.SEND : Constants.UNBILLED,
                header.getComboBills(),
                header.getBalanceCustomer(),
                header.getId());

        return header;
    }

    /**
     * Inserta los pagos de una orden
     *
     * @param payments Lista de {@link net.cltech.enterprisent.domain.operation.orders.billing.Payment}
     * @return Arreglo de los registros afectados
     * @throws Exception Error en base de datos
     */
    default int[] insertPayments(List<Payment> payments) throws Exception
    {
        return getJdbcTemplate().batchUpdate(""
                + "INSERT INTO lab903(lab22c1, lab01c1, lab903c2, lab110c1, lab59c1, lab903c3, lab07c1, lab04c1_i, lab903c4) "
                + "VALUES(?,?,?,?,?,?,?,?,?) ", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                ps.setLong(1, payments.get(i).getOrder());
                ps.setInt(2, payments.get(i).getPaymentType().getId());
                ps.setString(3, payments.get(i).getNumber());
                if (payments.get(i).getBank() == null)
                {
                    ps.setNull(4, java.sql.Types.INTEGER);
                }
                else
                {
                    ps.setInt(4, payments.get(i).getBank().getId());
                }
                if (payments.get(i).getCard() == null)
                {
                    ps.setNull(5, java.sql.Types.INTEGER);
                }
                else
                {
                    ps.setInt(5, payments.get(i).getCard().getId());
                }
                ps.setBigDecimal(6, payments.get(i).getPayment());
                ps.setInt(7, payments.get(i).isActive() ? 1 : 0);
                ps.setInt(8, payments.get(i).getEntryUser().getId());
                ps.setTimestamp(9, new Timestamp(payments.get(i).getEntryDate().getTime()));
            }

            @Override
            public int getBatchSize()
            {
                return payments. size();
            }
        });
    }

    /**
     * Actualiza los pagos de una orden
     *
     * @param payments Lista de {@link net.cltech.enterprisent.domain.operation.orders.billing.Payment}
     * @return Arreglo de los registros afectados
     * @throws Exception Error en base de datos
     */
    default int[] updatePayments(List<Payment> payments) throws Exception
    {
        return getJdbcTemplate().batchUpdate(""
                + "UPDATE   lab903 "
                + "SET      lab01c1 = ? "
                + " , lab903c2 = ? "
                + " , lab110c1 = ? "
                + " , lab59c1 = ? "
                + " , lab903c3 = ? "
                + " , lab07c1 = ? "
                + " , lab04c1_u = ? "
                + " , lab903c5 = ? "
                + "WHERE    lab903c1 = ? ", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                ps.setInt(1, payments.get(i).getPaymentType().getId());
                ps.setString(2, payments.get(i).getNumber());
                if (payments.get(i).getBank() == null)
                {
                    ps.setNull(3, java.sql.Types.INTEGER);
                }
                else
                {
                    ps.setInt(3, payments.get(i).getBank().getId());
                }
                if (payments.get(i).getCard() == null)
                {
                    ps.setNull(4, java.sql.Types.INTEGER);
                }
                else
                {
                    ps.setInt(4, payments.get(i).getCard().getId());
                }
                ps.setBigDecimal(5, payments.get(i).getPayment());
                ps.setInt(6, payments.get(i).isActive() ? 1 : 0);
                ps.setInt(7, payments.get(i).getUpdateUser().getId());
                ps.setTimestamp(8, new Timestamp(payments.get(i).getUpdateDate().getTime()));
                ps.setInt(9, payments.get(i).getId());
            }

            @Override
            public int getBatchSize()
            {
                return payments.size();
            }
        });
    }

    /**
     * Obtiene la cabecera de la caja
     *
     * @param order Numero de orden
     * @return Lista de {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader}, lista vacia en caso de no tener registros
     * @throws Exception Error en base de datos
     */
    default CashBoxHeader get(long order) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT     lab902.lab902c1 "
                    + " , lab902.lab22c1 "
                    + " , lab904.lab904c1 "
                    + " , lab904.lab904c2 "
                    + " , lab904.lab904c3 "
                    + " , lab902.lab902c2 "
                    + " , lab902.lab902c3 "
                    + " , lab902.lab902c4 "
                    + " , lab902.lab902c5 "
                    + " , lab902.lab902c6 "
                    + " , lab902.lab902c7 "
                    + " , lab902.lab902c8 "
                    + " , lab902.lab902c9 "
                    + " , lab04_i.lab04c1 as lab04c1_i "
                    + " , lab04_i.lab04c2 as lab04c2_i "
                    + " , lab04_i.lab04c3 as lab04c3_i "
                    + " , lab04_i.lab04c4 as lab04c4_i "
                    + " , lab902.lab902c10 "
                    + " , lab04_u.lab04c1 as lab04c1_u "
                    + " , lab04_u.lab04c2 as lab04c2_u "
                    + " , lab04_u.lab04c3 as lab04c3_u "
                    + " , lab04_u.lab04c4 as lab04c4_u "
                    + " , lab902.lab902c11 "
                    + " , lab902.lab902c12 "
                    + " , lab902.lab902c13 "
                    + " , lab902.lab902c14 "
                    + " , lab902.lab902c15 "
                    + " , lab902.lab902c16 "
                    + " , lab902.lab902c17 "
                    + " , lab902.lab902c18 "
                    + " , lab902.lab902c20 "
                    + " , lab902.lab902c21 " 
                    + " , lab902.lab902c22 "
                    + "FROM     lab902 "
                    + "         INNER JOIN lab904 ON lab902.lab904c1 = lab904.lab904c1 "
                    + "         INNER JOIN lab04 lab04_i ON lab04_i.lab04c1 = lab902.lab04c1_i "
                    + "         LEFT JOIN lab04 lab04_u ON lab04_u.lab04c1 = lab902.lab04c1_u "
                    + "WHERE    lab902.lab22c1 = ? ",
                    (ResultSet rs, int numRow) ->
            {
                CashBoxHeader record = new CashBoxHeader();
                record.setId(rs.getInt("lab902c1"));
                record.setOrder(rs.getLong("lab22c1"));

                Rate rate = new Rate();
                rate.setId(rs.getInt("lab904c1"));
                rate.setCode(rs.getString("lab904c2"));
                rate.setName(rs.getString("lab904c3"));
                record.setRate(rate);

                record.setSubTotal(rs.getBigDecimal("lab902c2"));
                record.setDiscountValue(rs.getBigDecimal("lab902c3"));
                record.setDiscountPercent(rs.getBigDecimal("lab902c4"));
                record.setTaxValue(rs.getBigDecimal("lab902c5"));
                record.setCopay(rs.getBigDecimal("lab902c6"));
                record.setFee(rs.getBigDecimal("lab902c7"));
                record.setTotalPaid(rs.getBigDecimal("lab902c8"));
                record.setBalance(rs.getBigDecimal("lab902c9"));
                record.setDiscountValueRate(rs.getBigDecimal("lab902c12"));
                record.setDiscountPercentRate(rs.getBigDecimal("lab902c13"));
                record.setCopayType(rs.getInt("lab902c14"));
                record.setCharge(rs.getBigDecimal("lab902c15"));
                record.setBilled(rs.getString("lab902c16"));
                record.setRuc(rs.getString("lab902c17"));
                record.setPhone(rs.getString("lab902c21"));
                record.setTypeCredit(rs.getInt("lab902c18"));
                record.setComboBills(rs.getInt("lab902c20"));
                record.setBalanceCustomer(rs.getBigDecimal("lab902c22"));
                
                User userI = new User();
                userI.setId(rs.getInt("lab04c1_i"));
                userI.setLastName(rs.getString("lab04c2_i"));
                userI.setName(rs.getString("lab04c3_i"));
                userI.setUserName(rs.getString("lab04c4_i"));
                record.setEntryUser(userI);
                record.setEntryDate(rs.getTimestamp("lab902c10"));
                
                if (rs.getString("lab04c1_u") != null)
                {
                    User userU = new User();
                    userU.setId(rs.getInt("lab04c1_u"));
                    userU.setLastName(rs.getString("lab04c2_u"));
                    userU.setName(rs.getString("lab04c3_u"));
                    userU.setUserName(rs.getString("lab04c4_u"));
                    record.setUpdateUser(userU);
                    record.setUpdateDate(rs.getTimestamp("lab902c11"));
                }
                return record;
            }, order);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
     /**
     * Obtiene la cabecera de la caja
     *
     * @param order Numero de orden
     * @return Lista de {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader}, lista vacia en caso de no tener registros
     * @throws Exception Error en base de datos
     */
    default List<CashBoxHeader> getBasicCashBox(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("" + ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT  lab902.lab902c1")
                    .append(", lab902.lab22c1")
                    .append(" , lab902.lab902c2 ")
                    .append(" , lab902.lab902c3 ")
                    .append(" , lab902.lab902c4 ")
                    .append(" , lab902.lab902c5 ")
                    .append(" , lab902.lab902c6 ")
                    .append(" , lab902.lab902c7 ")
                    .append(" , lab902.lab902c8 ")
                    .append(" , lab902.lab902c9 ")
                    .append(" , lab902.lab902c12 ")
                    .append(" , lab902.lab902c13 ")
                    .append(" , lab902.lab902c14 ")
                    .append(" , lab902.lab902c15 ")
                    .append(" , lab902.lab902c16 ")
                    .append(" , lab902.lab902c17 ")
                    .append(" , lab902.lab902c18 ")
                    .append(" , lab902.lab902c20 ")
                    .append(" , lab902.lab902c22 ")
                    .append(" FROM lab902 ")
                    .append("WHERE lab902.lab22c1 = ").append(order);
            
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int numRow) ->
            {
                CashBoxHeader record = new CashBoxHeader();
                record.setId(rs.getInt("lab902c1"));
                record.setOrder(rs.getLong("lab22c1"));

                record.setSubTotal(rs.getBigDecimal("lab902c2"));
                record.setDiscountValue(rs.getBigDecimal("lab902c3"));
                record.setDiscountPercent(rs.getBigDecimal("lab902c4"));
                record.setTaxValue(rs.getBigDecimal("lab902c5"));
                record.setCopay(rs.getBigDecimal("lab902c6"));
                record.setFee(rs.getBigDecimal("lab902c7"));
                record.setTotalPaid(rs.getBigDecimal("lab902c8"));
                record.setBalance(rs.getBigDecimal("lab902c9"));
                record.setDiscountValueRate(rs.getBigDecimal("lab902c12"));
                record.setDiscountPercentRate(rs.getBigDecimal("lab902c13"));
                record.setCopayType(rs.getInt("lab902c14"));
                record.setCharge(rs.getBigDecimal("lab902c15"));
                record.setBilled(rs.getString("lab902c16"));
                record.setRuc(rs.getString("lab902c17"));
                record.setTypeCredit(rs.getInt("lab902c18"));
                record.setComboBills(rs.getInt("lab902c20"));
                record.setBalanceCustomer(rs.getBigDecimal("lab902c22"));
                return record;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
     /**
     * Obtiene los datos de una orden sin caja
     *
     * @param order Numero de orden
     * @param idTest
     * @return Lista de {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader}, lista vacia en caso de no tener registros
     * @throws Exception Error en base de datos
     */
    default List<CashBoxHeader> getCashBoxFound(long order, int idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
            
            StringBuilder query = new StringBuilder();
            query.append("" + ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT  lab900c1")
                    .append(" , lab22c1 ")
                    .append(" , lab900c3 ")
                    .append(" , lab900c4 ")
                    .append(" , lab900c5 ")
                    .append(" , lab900c6 ")
                    .append(" FROM  ").append(lab900).append(" as lab900 ")
                    .append("WHERE lab22c1 = ").append(order)
                    .append(" AND lab39c1 = ").append(idTest);
            
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int numRow) ->
            {
                CashBoxHeader record = new CashBoxHeader();
                record.setId(rs.getInt("lab900c1"));
                record.setOrder(rs.getLong("lab22c1"));

                record.setSubTotal(new BigDecimal(0));
                record.setDiscountValue(new BigDecimal(0));
                record.setDiscountPercent(rs.getBigDecimal("lab900c6"));
                record.setTaxValue(rs.getBigDecimal("lab900c5"));
                record.setCopay(new BigDecimal(0));
                record.setFee(new BigDecimal(0));
                record.setTotalPaid(new BigDecimal(0));
                record.setBalance(new BigDecimal(0));
                record.setDiscountValueRate(new BigDecimal(0));
                record.setDiscountPercentRate(new BigDecimal(0));
                return record;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
     /**
    * Valida si una orden tiene caja
    * 
     * @param order
     * @return  
    * @throws SQLException Error en base de datos
    */
    default boolean cashBoxExists(long order) throws SQLException 
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("select count(*) as count FROM lab902 where lab902.lab22c1 = ").append(order);
           
            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("count") != 0;
            });
        }
        catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Obtiene la cabecera de la caja
     *
     * @param order Numero de orden
     * @param rate Id Tarifa
     * @return Lista de {@link net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader}, lista vacia en caso de no tener registros
     * @throws Exception Error en base de datos
     */
    default CashBoxHeader get(long order, int rate) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT     lab902.lab902c1 "
                    + " , lab902.lab22c1 "
                    + " , lab904.lab904c1 "
                    + " , lab904.lab904c2 "
                    + " , lab904.lab904c3 "
                    + " , lab902.lab902c2 "
                    + " , lab902.lab902c3 "
                    + " , lab902.lab902c4 "
                    + " , lab902.lab902c5 "
                    + " , lab902.lab902c6 "
                    + " , lab902.lab902c7 "
                    + " , lab902.lab902c8 "
                    + " , lab902.lab902c9 "
                    + " , lab04_i.lab04c1 as lab04c1_i "
                    + " , lab04_i.lab04c2 as lab04c2_i "
                    + " , lab04_i.lab04c3 as lab04c3_i "
                    + " , lab04_i.lab04c4 as lab04c4_i "
                    + " , lab902.lab902c10 "
                    + " , lab04_u.lab04c1 as lab04c1_u "
                    + " , lab04_u.lab04c2 as lab04c2_u "
                    + " , lab04_u.lab04c3 as lab04c3_u "
                    + " , lab04_u.lab04c4 as lab04c4_u "
                    + " , lab902.lab902c11 "
                    + " , lab902.lab902c12 "
                    + " , lab902.lab902c13 "
                    + " , lab902.lab902c14 "
                    + " , lab902.lab902c15 " 
                    + " , lab902.lab902c16 " 
                    + " , lab902.lab902c17 " 
                    + " , lab902.lab902c18 "
                    + " , lab902.lab902c20 "
                    + " , lab902.lab902c22 "
                    + "FROM     lab902 "
                    + "         INNER JOIN lab904 ON lab902.lab904c1 = lab904.lab904c1 "
                    + "         INNER JOIN lab04 lab04_i ON lab04_i.lab04c1 = lab902.lab04c1_i "
                    + "         LEFT JOIN lab04 lab04_u ON lab04_u.lab04c1 = lab902.lab04c1_u "
                    + "WHERE    lab902.lab22c1 = ? "
                    + "         AND lab902.lab904c1 = ? ",
                    (ResultSet rs, int numRow) ->
            {
                CashBoxHeader record = new CashBoxHeader();
                record.setId(rs.getInt("lab902c1"));
                record.setOrder(rs.getLong("lab22c1"));

                Rate rateI = new Rate();
                rateI.setId(rs.getInt("lab904c1"));
                rateI.setCode(rs.getString("lab904c2"));
                rateI.setName(rs.getString("lab904c3"));
                record.setRate(rateI);

                record.setSubTotal(rs.getBigDecimal("lab902c2"));
                record.setDiscountValue(rs.getBigDecimal("lab902c3"));
                record.setDiscountPercent(rs.getBigDecimal("lab902c4"));
                record.setTaxValue(rs.getBigDecimal("lab902c5"));
                record.setCopay(rs.getBigDecimal("lab902c6"));
                record.setFee(rs.getBigDecimal("lab902c7"));
                record.setTotalPaid(rs.getBigDecimal("lab902c8"));
                record.setBalance(rs.getBigDecimal("lab902c9"));
                record.setDiscountValueRate(rs.getBigDecimal("lab902c12"));
                record.setDiscountPercentRate(rs.getBigDecimal("lab902c13"));
                record.setCopayType(rs.getInt("lab902c14"));
                record.setCharge(rs.getBigDecimal("lab902c15"));
                record.setBilled(rs.getString("lab902c16"));
                record.setRuc(rs.getString("lab902c17"));
                record.setTypeCredit(rs.getInt("lab902c18"));
                record.setComboBills(rs.getInt("lab902c20"));
                record.setBalanceCustomer(rs.getBigDecimal("lab902c22"));
                
                User userI = new User();
                userI.setId(rs.getInt("lab04c1_i"));
                userI.setLastName(rs.getString("lab04c2_i"));
                userI.setName(rs.getString("lab04c3_i"));
                userI.setUserName(rs.getString("lab04c4_i"));
                record.setEntryUser(userI);
                record.setEntryDate(rs.getTimestamp("lab902c10"));
                if (rs.getString("lab04c1_u") != null)
                {
                    User userU = new User();
                    userU.setId(rs.getInt("lab04c1_u"));
                    userU.setLastName(rs.getString("lab04c2_u"));
                    userU.setName(rs.getString("lab04c3_u"));
                    userU.setUserName(rs.getString("lab04c4_u"));
                    record.setUpdateUser(userU);
                    record.setUpdateDate(rs.getTimestamp("lab902c11"));
                }
                return record;
            }, order, rate);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene los pagos asociados a una orden
     *
     * @param order Numero de Orden
     * @return Lista de {@link net.cltech.enterprisent.domain.operation.orders.billing.Payment}
     * @throws Exception Error en base de datos
     */
    default List<Payment> getPayments(long order) throws Exception
    {
        try
        {
            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab903.lab903c1 "
                    + " , lab903.lab22c1 "
                    + " , lab01.lab01c1 "
                    + " , lab01.lab01c2 "
                    + " , lab01.lab01c3 "
                    + " , lab01.lab01c4 "
                    + " , lab01.lab01c5 "
                    + " , lab01.lab01c6 "
                    + " , lab903.lab903c2 "
                    + " , lab110.lab110c1 "
                    + " , lab110.lab110c2 "
                    + " , lab59.lab59c1 "
                    + " , lab59.lab59c2 "
                    + " , lab903.lab903c3 "
                    + " , lab903.lab07c1 "
                    + " , lab04_i.lab04c1 as lab04c1_i "
                    + " , lab04_i.lab04c2 as lab04c2_i "
                    + " , lab04_i.lab04c3 as lab04c3_i "
                    + " , lab04_i.lab04c4 as lab04c4_i "
                    + " , lab903.lab903c4 "
                    + " , lab04_u.lab04c1 as lab04c1_u "
                    + " , lab04_u.lab04c2 as lab04c2_u "
                    + " , lab04_u.lab04c3 as lab04c3_u "
                    + " , lab04_u.lab04c4 as lab04c4_u "
                    + " , lab903.lab903c5 "
                    + "FROM     lab903 "
                    + "         INNER JOIN lab01 ON lab903.lab01c1 = lab01.lab01c1 "
                    + "         LEFT JOIN lab110 ON lab903.lab110c1 = lab110.lab110c1 "
                    + "         LEFT JOIN lab59 ON lab903.lab59c1 = lab59.lab59c1 "
                    + "         INNER JOIN lab04 lab04_i ON lab04_i.lab04c1 = lab903.lab04c1_i "
                    + "         LEFT JOIN lab04 lab04_u ON lab04_u.lab04c1 = lab903.lab04c1_u "
                    + "WHERE    lab903.lab22c1 = ? "
                    + "", (ResultSet rs, int numRow) ->
            {
                Payment record = new Payment();
                record.setId(rs.getInt("lab903c1"));
                record.setOrder(rs.getLong("lab22c1"));

                PaymentType type = new PaymentType();
                type.setId(rs.getInt("lab01c1"));
                type.setName(rs.getString("lab01c2"));
                type.setBank(rs.getInt("lab01c3") == 1);
                type.setCard(rs.getInt("lab01c4") == 1);
                type.setNumber(rs.getInt("lab01c5") == 1);
                type.setAdjustment(rs.getInt("lab01c6") == 1);
                record.setPaymentType(type);

                record.setNumber(rs.getString("lab903c2"));

                if (rs.getString("lab110c1") != null)
                {
                    Bank bank = new Bank();
                    bank.setId(rs.getInt("lab110c1"));
                    bank.setName(rs.getString("lab110c2"));
                    record.setBank(bank);
                }

                if (rs.getString("lab59c1") != null)
                {
                    Card card = new Card();
                    card.setId(rs.getInt("lab59c1"));
                    card.setName(rs.getString("lab59c2"));
                    record.setCard(card);
                }
                record.setPayment(rs.getBigDecimal("lab903c3"));
                record.setActive(rs.getInt("lab07c1") == 1);
                User userI = new User();
                userI.setId(rs.getInt("lab04c1_i"));
                userI.setLastName(rs.getString("lab04c2_i"));
                userI.setName(rs.getString("lab04c3_i"));
                userI.setUserName(rs.getString("lab04c4_i"));
                record.setEntryUser(userI);
                record.setEntryDate(rs.getTimestamp("lab903c4"));
                if (rs.getString("lab04c1_u") != null)
                {
                    User userU = new User();
                    userU.setId(rs.getInt("lab04c1_u"));
                    userU.setLastName(rs.getString("lab04c2_u"));
                    userU.setName(rs.getString("lab04c3_u"));
                    userU.setUserName(rs.getString("lab04c4_u"));
                    record.setUpdateUser(userU);
                    record.setUpdateDate(rs.getTimestamp("lab903c5"));
                }
                return record;
            }, order);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Crea un registro del total a pagar por una caja
     *
     * @param fullpayment
     * @return {@link net.cltech.enterprisent.domain.operation.orders.billing.FullPayment} con su respectivo id
     * @throws Exception Error en base de datos
     */
    default FullPayment insertTotalPayments(FullPayment fullpayment) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab908");
        HashMap<String, Object> parameters = new HashMap<>(0);
        parameters.put("lab22c1", fullpayment.getOrder());
        parameters.put("lab908c1", fullpayment.getPayment());
        parameters.put("lab908c2", fullpayment.getAllpayments());
        parameters.put("lab908c3", fullpayment.getTotalbalance());
        parameters.put("lab908c5", fullpayment.getDiscountValue());
        parameters.put("lab908c6", fullpayment.getDiscountPercent());
        parameters.put("lab908c4", new Timestamp(fullpayment.getUpdateDate().getTime()));
        parameters.put("lab908c7", fullpayment.getCopay());
        parameters.put("lab908c8", fullpayment.getFee());
        parameters.put("lab908c9", fullpayment.getCharge());
        parameters.put("lab908c10", fullpayment.getBalanceCustomer());
        parameters.put("lab04c1", fullpayment.getUpdateUser());
        int rows = insert.execute(parameters);
        return rows > 0 ? fullpayment : null;
    }

    /**
     * Actualiza un registro del total a pagar por una caja
     *
     *
     * @param fullpayment
     * @return {@link net.cltech.enterprisent.domain.operation.orders.billing.FullPayment} con su respectivo id
     * @throws Exception Error en base de datos
     */
    default FullPayment updateTotalPayments(FullPayment fullpayment) throws Exception
    {
        getJdbcTemplate().update("UPDATE lab908 SET lab908c1 = ?, lab908c2 = ?, lab908c3 = ?, lab908c4 = ?, lab04c1 = ?, lab908c5 = ?, lab908c6 = ?, lab908c7 = ?, lab908c8 = ?, lab908c9 = ?, lab908c10 = ? "
                + "WHERE lab22c1 = ?",
                fullpayment.getPayment(), fullpayment.getAllpayments(), fullpayment.getTotalbalance(), new Timestamp(fullpayment.getUpdateDate().getTime()), fullpayment.getUpdateUser(), fullpayment.getDiscountValue(), fullpayment.getDiscountPercent(), fullpayment.getCopay(), fullpayment.getFee(), 
                fullpayment.getCharge(), fullpayment.getBalanceCustomer(), fullpayment.getOrder());

        return fullpayment;
    }

    /**
     * Obtiene el total de pago de una caja
     *
     * @param order Numero de Orden
     * @return Lista de pago totales de una caja. {@link net.cltech.enterprisent.domain.operation.orders.billing.Payment}
     * @throws Exception Error en base de datos
     */
    default List<FullPayment> getLisFullpayment(long order) throws Exception
    {
        try
        {
              return getJdbcTemplate().query(""
                    + "SELECT lab908c1, lab22c1, lab908c2, lab908c3, lab908c4, lab04c1, lab908c5, lab908c6, lab908c7, lab908c8, lab908c9, lab908c10 "
                    + "FROM lab908 "
                    + "WHERE lab22c1 = ? ",
                    (ResultSet rs, int numRow) ->
            {
                FullPayment fullpayment = new FullPayment();
                fullpayment.setOrder(rs.getLong("lab22c1"));
                fullpayment.setPayment(rs.getBigDecimal("lab908c1"));
                fullpayment.setAllpayments(rs.getBigDecimal("lab908c2"));
                fullpayment.setTotalbalance(rs.getBigDecimal("lab908c3"));
                fullpayment.setUpdateDate(rs.getTimestamp("lab908c4"));
                fullpayment.setDiscountValue(rs.getBigDecimal("lab908c5"));
                fullpayment.setDiscountPercent(rs.getBigDecimal("lab908c6"));
                fullpayment.setCopay(rs.getBigDecimal("lab908c7"));
                fullpayment.setFee(rs.getBigDecimal("lab908c8"));
                fullpayment.setCharge(rs.getBigDecimal("lab908c9"));
                fullpayment.setBalanceCustomer(rs.getBigDecimal("lab908c10"));
                //Usuario
                fullpayment.setUpdateUser(rs.getLong("lab04c1"));
                return fullpayment;
            }, order);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Elimina el detalle de la caja por medio de la orden
     *
     * @param id Numero de la orden
     *
     * @return registros eliminados
     */
    default int deleteCashboxDetail(Long id)
    {
        String deleteSql = "DELETE FROM lab903 WHERE lab22c1 = ? ";
        return getJdbcTemplate().update(deleteSql, id);
    }

    /**
     * Elimina la cabecera de la caja por medio de la orden
     *
     * @param id Numero de la orden
     *
     * @return registros eliminados
     */
    default int deleteCashboxHeader(Long id)
    {
        String deleteSql = "DELETE FROM lab902 WHERE lab22c1 = ? ";
        return getJdbcTemplate().update(deleteSql, id);
    }

    /**
     * Elimina el total de pagos de la caja por medio de la orden
     *
     * @param id Numero de la orden
     *
     * @return registros eliminados
     */
    default int deleteTotalPayments(Long id)
    {
        String deleteSql = "DELETE FROM lab908 WHERE lab22c1 = ? ";
        return getJdbcTemplate().update(deleteSql, id);
    }

   

    /**
     * Crea un registro de la factura
     *
     * @param invoice
     * @return Filas insertadas
     * @throws Exception Error en base de datos
     */
    default int insertInvoice(Invoice invoice) throws Exception
    {
        try
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab909");
            HashMap<String, Object> parameters = new HashMap<>(0);
            parameters.put("lab909c1", invoice.getInvoiceId());
            parameters.put("lab07c1", invoice.isState() == true ? 1 : 0);
            int rows = insert.execute(parameters);
            return rows > 0 ? rows : 0;
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Actualiza un registro de la factura
     *
     * @param invoice
     * @return Filas insertadas
     * @throws Exception Error en base de datos
     */
    default int updateInvoice(Invoice invoice) throws Exception
    {
        try
        {
            return getJdbcTemplate().update("UPDATE lab909 SET lab07c1 = ? "
                    + "WHERE lab909c1 = ?",
                    invoice.isState() == true ? 1 : 0,
                    invoice.getInvoiceId());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Consulta el id de la factura
     *
     * @param invoiceId
     * @return Filas insertadas
     * @throws Exception Error en base de datos
     */
    default Invoice getInvoiceById(Long invoiceId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab909c1, lab07c1 ")
                    .append("FROM lab909 ")
                    .append("WHERE lab909c1 = ").append(invoiceId);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getLong("lab909c1"));
                invoice.setState(rs.getBoolean("lab07c1"));
                return invoice;
            });
        }
        catch (Exception e)
        {
            return new Invoice();
        }
    }
    
    /**
     * Consulta el primer sistema central que tiene la propiedad de CUPS Activa
     * 
     * @return
     * @throws Exception 
     */
    public int getCentralSystemToExternalBilling() throws Exception;
    
    /**
     * Consulta el id de la factura por el id de una orden
     *
     * @param orderId
     * @return Id de la factura -> si esta ya pertenece a alguna, sino retorna cero
     * @throws Exception Error en base de datos
     */
    default long getInvoiceIdByOrderId(Long orderId) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
            
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT DISTINCT lab901c1p")
                    .append(", lab901c1i")
                    .append(" FROM  ").append(lab900).append(" as lab900 ")
                    .append("WHERE lab22c1 = ").append(orderId);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                if(rs.getLong("lab901c1p") > 0)
                {
                    return rs.getLong("lab901c1p");
                }
                else
                {
                    return rs.getLong("lab901c1i");
                }
            });
        }
        catch (DataAccessException e)
        {
            return -1;
        }
    }
    
    /**
     * Obtiene el id de la cabecera de la caja
     *
     * @param order Numero de orden
     * @return Id de la cabecera de la caja
     * @throws Exception Error en base de datos
     */
    default int getCashboxIdByOrderId(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("" + ISOLATION_READ_UNCOMMITTED)
                    .append("SELECT lab902c1")
                    .append(" FROM lab902 ")
                    .append("WHERE lab902.lab22c1 = ").append(order);
            
            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int numRow) ->
            {
                return rs.getInt("lab902c1");
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return 0;
        }
    }
    
    /**
     * Obtiene la lista de cajas de un rango de ordenes
     *
     * @param startDate Fecha de inicio
     * @param endDate Fecha final
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<CashBoxBilling> getListCashboxBilling(String startDate, String endDate) throws Exception
    {
        try
        {
            HashMap<Long, CashBoxBilling> list = new HashMap<>();

            // Query
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);

            query.append("SELECT lab903.lab01c1, lab01c2, lab01c8, lab110c2, lab59c2, lab903c3, lab902.lab22c1, lab04.lab04c4, ");
            query.append("lab902c2, lab902c3, lab902c4, lab902c14, lab902c6, lab902c9, lab902c15, lab902c16, lab902c17, lab902c18, lab902c22");

            StringBuilder from = new StringBuilder();
            from.append(" FROM ").append(" lab902 ");
            from.append("LEFT JOIN lab903 ON lab902.lab22c1 = lab903.lab22c1  ");
            from.append("LEFT JOIN lab01 ON lab01.lab01c1 = lab903.lab01c1 ");
            from.append("LEFT JOIN lab110 ON lab110.lab110c1 = lab903.lab110c1  ");
            from.append("LEFT JOIN lab59 ON lab59.lab59c1 = lab903.lab59c1  ");
            from.append("LEFT JOIN lab04 ON lab902.lab04c1_i = lab04.lab04c1  ");
            
            from.append(" WHERE lab902.lab22c1 BETWEEN ").append(startDate).append(" AND ").append(endDate);
                        
            from.append(" AND (lab902.lab902c19 = ").append(Constants.SEND);
            from.append(" OR lab902.lab902c19 = ").append(Constants.BILLED).append(") ");

            RowMapper mapper = (RowMapper<CashBoxBilling>) (ResultSet rs, int i) ->
            {
                CashBoxBilling cash = new CashBoxBilling();
                if (!list.containsKey(rs.getLong("lab22c1")))
                {
                    MathContext m = new MathContext(2);
                    cash.setOrder(rs.getLong("lab22c1"));
                    cash.setDiscountValue(rs.getBigDecimal("lab902c3"));
                    cash.setDiscountPercent(rs.getBigDecimal("lab902c4"));
                    cash.setCopayType(rs.getInt("lab902c14"));
                    cash.setCopay(rs.getBigDecimal("lab902c6"));
                    cash.setBalancePatient(rs.getBigDecimal("lab902c9"));
                    cash.setBilled(rs.getString("lab902c16"));
                    cash.setRuc(rs.getString("lab902c17"));
                    cash.setTypeCredit(rs.getInt("lab902c18") == 0 ? 1 : 2);
                    cash.setChargedPatient(rs.getBigDecimal("lab902c15"));
                    cash.setBalanceCustomer(rs.getBigDecimal("lab902c22"));
                    cash.setUserCreated(rs.getString("lab04c4"));
                    
                    if( cash.getCopay().compareTo(BigDecimal.ZERO) > 0) {
                        cash.setCopay(cash.getCopay().round(m));
                    }
                    
                    if( cash.getBalancePatient().compareTo(BigDecimal.ZERO) > 0 ) {
                        cash.setBalancePatient(cash.getBalancePatient().round(m));
                    }
                    
                    if( cash.getChargedPatient() != null && cash.getChargedPatient().compareTo(BigDecimal.ZERO) > 0 ) {
                        cash.setChargedPatient(cash.getChargedPatient().round(m));
                    }
                    
                    if( cash.getBalanceCustomer() != null && cash.getBalanceCustomer().compareTo(BigDecimal.ZERO) > 0 ) {
                        cash.setBalanceCustomer(cash.getBalanceCustomer().round(m));
                    }
                    
                    if(cash.getCopayType() == 1 && cash.getCopay().compareTo(BigDecimal.ZERO) > 0) {
                        Double subtotal = rs.getDouble("lab902c2") - cash.getDiscountValue().doubleValue();
                        Double copayPercent = ( cash.getCopay().doubleValue() / subtotal ) * 100;
                        cash.setCopayPercent(copayPercent);
                    } else {
                        cash.setCopayPercent(0.0);
                    } 
                    list.put(cash.getOrder(), cash);

                } else
                {
                    cash = list.get(rs.getLong("lab22c1"));
                }
                
                PaymentBilling payment = new PaymentBilling();
                
                payment.setMethodOfPayment(rs.getString("lab01c2"));
                payment.setCodeMethodOfPayment(rs.getString("lab01c8"));
                payment.setBank(rs.getString("lab110c2"));
                payment.setCard(rs.getString("lab59c2"));
                payment.setPayment(rs.getBigDecimal("lab903c3"));

                if (!list.get(cash.getOrder()).getPayMentBillings().contains(payment))
                {
                    list.get(cash.getOrder()).getPayMentBillings().add(payment);
                }

                return cash;
            };
            getJdbcTemplate().query(query.toString() + " " + from.toString(), mapper);
            return new ArrayList<>(list.values());
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error("Error cashbox: " + ex);
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Actauliza el estado de la caja para integracion con kbits
     *
     * @param numberOrden Numero de orden
     * @param status Estado. 0-> No enviar, 1-> Enviar a Kbits, 2-> Combo no facturada, 3-> Combo facturada, 4 -> Enviado
     * @return Long
     * @throws Exception Error presentado en base de datos
     */
    default boolean updateCashBoxStatus(long numberOrden, int status) throws Exception
    {     
        try
        {
            StringBuilder select = new StringBuilder();    
            select.append(" UPDATE lab902 SET lab902c19 = ? WHERE lab22c1 = ? ");
            getJdbcTemplate().update(select.toString(), status, numberOrden); 
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
