package net.cltech.enterprisent.dao.interfaces.operation.orders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.integration.homebound.BillingTestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ContainerHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.SampleHomeBound;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Metodo de acceso a base de datos para lo examenes de una orden que son
 * cobrados
 *
 * @version 1.0.0
 * @author dcortes
 * @since 10/04/2018
 * @see Creación
 */
public interface BillingTestDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene los examenes cobrados para una orden
     *
     * @param order Numero de orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.BillingTest}
     * @throws Exception Error en base de datos
     */
    default List<BillingTest> get(long order) throws Exception
    {
        
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
        
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab900c1 "
                + " , lab900.lab21c1 "
                + " , lab900.lab22c1 "
                + " , lab900.lab39c1 "
                + " , lab39.lab39c2 "
                + " , lab39.lab39c4 "
                + " , lab900c2 "
                + " , lab900c3 "
                + " , lab900c4 "
                + " , lab901c1p "
                + " , lab901c1i "
                + " , lab904.lab904c1 "
                + " , lab904.lab904c2 "
                + " , lab904.lab904c3 "
                + " , lab14.lab14c1 "
                + " , lab14.lab14c2 "
                + " , lab14.lab14c3 "
                + " , lab05.lab05c1 "
                + " , lab05.lab05c2 "
                + " , lab05.lab05c10 "
                + " , lab05.lab05c4 "
                + " , lab900.lab900c5 "
                + " , lab900.lab900c6 "
                + "FROM      "
                + lab900
                + " as lab900 "
                + "         INNER JOIN lab39 ON lab900.lab39c1 = lab39.lab39c1 "
                + "         INNER JOIN lab904 ON lab900.lab904c1 = lab904.lab904c1 "
                + "         LEFT JOIN lab14 ON lab900.lab14c1 = lab14.lab14c1 "
                + "         LEFT JOIN lab05 ON lab900.lab05c1 = lab05.lab05c1 "
                + "WHERE    lab900.lab22c1 = ? ", (ResultSet rs, int numRow) ->
        {
            BillingTest record = new BillingTest();
            record.setId(rs.getInt("lab900c1"));

            Patient patient = new Patient();
            patient.setId(rs.getInt("lab21c1"));
            record.setPatient(patient);

            Order orderB = new Order();
            orderB.setOrderNumber(rs.getLong("lab22c1"));
            record.setOrder(orderB);

            Test test = new Test();
            test.setId(rs.getInt("Lab39C1"));
            test.setCode(rs.getString("Lab39C2"));
            test.setName(rs.getString("Lab39C4"));
            record.setTest(test);

            record.setServicePrice(rs.getBigDecimal("lab900c2"));
            record.setPatientPrice(rs.getBigDecimal("lab900c3"));
            record.setInsurancePrice(rs.getBigDecimal("lab900c4"));
            record.setTax(rs.getString("lab900c5") == null ? null : rs.getBigDecimal("lab900c5"));
            record.setDiscount(rs.getBigDecimal("lab900c6"));

            record.setPatientInvoice(rs.getString("lab901c1p") == null ? null : rs.getInt("lab901c1p"));
            record.setInsuranceInvoice(rs.getString("lab901c1i") == null ? null : rs.getInt("lab901c1i"));

            Rate rate = new Rate();
            rate.setId(rs.getInt("lab904c1"));
            rate.setCode(rs.getString("lab904c2"));
            rate.setName(rs.getString("lab904c3"));
            record.setRate(rate);

            if (rs.getString("lab14c1") != null)
            {
                Account account = new Account();
                account.setId(rs.getInt("lab14c1"));
                account.setNit(rs.getString("lab14c2"));
                account.setName(rs.getString("lab14c3"));
                record.setAccount(account);
            }

            if (rs.getString("lab05c1") != null)
            {
                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setCode(rs.getString("lab05c10"));
                branch.setAbbreviation(rs.getString("lab05c2"));
                branch.setName(rs.getString("lab05c4"));
                record.setBranch(branch);
            }
            return record;
        }, order);
    }

    /**
     * Obtiene informacion basica de los examenes cobrados para una orden
     *
     * @param order Numero de orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.BillingTest}
     * @throws Exception Error en base de datos
     */
    default List<BillingTest> getTestCashbox(long order) throws Exception
    {
        return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab900c1 "
                + " , lab900.lab21c1 "
                + " , lab900.lab22c1 "
                + " , lab900.lab39c1 "
                + " , lab39c2 "
                + " , lab39c3 "
                + " , lab39c4 "
                + " , lab900c2 "
                + " , lab900c3 "
                + " , lab900c4 "
                + " , lab901c1p "
                + " , lab901c1i "
                + " , lab904.lab904c1 "
                + " , lab904.lab904c2 "
                + " , lab904.lab904c3 "
                + " , lab39.lab39c49 "
                + "FROM     lab900 "
                + "         INNER JOIN lab39 ON lab900.lab39c1 = lab39.lab39c1 "
                + "         INNER JOIN lab904 ON lab900.lab904c1 = lab904.lab904c1 "
                + "WHERE    lab900.lab22c1 = ? ", (ResultSet rs, int numRow) ->
        {
            BillingTest record = new BillingTest();
            record.setId(rs.getInt("lab900c1"));

            Patient patient = new Patient();
            patient.setId(rs.getInt("lab21c1"));
            record.setPatient(patient);

            Order orderB = new Order();
            orderB.setOrderNumber(rs.getLong("lab22c1"));
            record.setOrder(orderB);

            Test test = new Test();
            test.setId(rs.getInt("Lab39C1"));
            test.setCode(rs.getString("Lab39C2"));
            test.setAbbr(rs.getString("Lab39C3"));
            test.setName(rs.getString("Lab39C4"));
            record.setTest(test);

            record.setServicePrice(rs.getBigDecimal("lab900c2"));
            record.setPatientPrice(rs.getBigDecimal("lab900c3"));
            record.setInsurancePrice(rs.getBigDecimal("lab900c4"));
            record.setTax(rs.getString("lab39c49") == null ? null : rs.getBigDecimal("lab39c49"));

            record.setPatientInvoice(rs.getString("lab901c1p") == null ? null : rs.getInt("lab901c1p"));
            record.setInsuranceInvoice(rs.getString("lab901c1i") == null ? null : rs.getInt("lab901c1i"));

            Rate rate = new Rate();
            rate.setId(rs.getInt("lab904c1"));
            rate.setCode(rs.getString("lab904c2"));
            rate.setName(rs.getString("lab904c3"));
            record.setRate(rate);
            return record;
        }, order);
    }

    /**
     * Inserta los precios de los examenes
     *
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.BillingTest}
     * @return Arreglo con los ids afectados
     * @throws Exception Error en base de datos
     */
    default int[] insert(List<BillingTest> tests) throws Exception
    {
        
        Integer year = Tools.YearOfOrder(String.valueOf(tests.get(0).getOrder().getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

        return getJdbcTemplate().batchUpdate(""
                + "insert into "+ lab900 +"(lab21c1"
                + ", lab22c1"
                + ", lab39c1"
                + ", lab900c2"
                + ", lab900c3"
                + ", lab900c4"
                + ", lab901c1p"
                + ", lab901c1i"
                + ", lab904c1"
                + ", lab14c1"
                + ", lab05c1"
                + ", lab900c5"
                + ", lab900c6) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                BillingTest test = tests.get(i);
                ps.setInt(1, test.getPatient().getId());
                ps.setLong(2, test.getOrder().getOrderNumber());
                ps.setInt(3, test.getTest().getId());
                ps.setBigDecimal(4, test.getServicePrice());
                ps.setBigDecimal(5, test.getPatientPrice());
                ps.setBigDecimal(6, test.getInsurancePrice());

                if (test.getPatientInvoice() != null)
                {
                    ps.setInt(7, test.getPatientInvoice());
                }
                else
                {
                    ps.setNull(7, java.sql.Types.INTEGER);
                }

                if (test.getInsuranceInvoice() != null)
                {
                    ps.setInt(8, test.getInsuranceInvoice());
                }
                else
                {
                    ps.setNull(8, java.sql.Types.INTEGER);
                }
                ps.setInt(9, test.getRate().getId());
                if (test.getAccount() != null && test.getAccount().getId() != null)
                {
                    ps.setInt(10, test.getAccount().getId());
                }
                else
                {
                    ps.setNull(10, java.sql.Types.INTEGER);
                }

                if (test.getBranch() != null)
                {
                    ps.setInt(11, test.getBranch().getId());
                }
                else
                {
                    ps.setNull(11, java.sql.Types.INTEGER);
                }

                ps.setBigDecimal(12, test.getTax());
                ps.setBigDecimal(13, test.getDiscount());
            }

            @Override
            public int getBatchSize()
            {
                return tests.size();
            }
        });
    }

    /**
     * Elimina los examenes con precio de una orden
     *
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.BillingTest}
     * @return Arreglo con los ids afectados
     * @throws Exception Error en base de datos
     */
    default int[] delete(List<BillingTest> tests) throws Exception
    {
        return getJdbcTemplate().batchUpdate(""
                + "DELETE   "
                + "FROM     lab900 "
                + "WHERE    lab22c1 = ? "
                + "         AND lab39c1 = ? "
                + "", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                BillingTest test = tests.get(i);
                ps.setLong(1, test.getOrder().getOrderNumber());
                ps.setInt(2, test.getTest().getId());
            }

            @Override
            public int getBatchSize()
            {
                return tests.size();
            }
        });
    }
    
     /**
     * Elimina los demograficos por id demografico y sede
     *
     * @param order
    
     *
     * @throws Exception Error en la base de datos.
     */
    default void deletePriceTestOrder(Long order) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
            
        String query = " DELETE FROM " + lab900 + " WHERE lab22c1 = ? ";
        getJdbcTemplate().update(query, order);
    }

    /**
     * Actualiza los examenes con precio de una orden parcialmente actualizando
     * solo: precio de servicio, tarifa, cuenta y sede
     *
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.BillingTest}
     * @return Arreglo con los ids afectados
     * @throws Exception Error en base de datos
     */
    default int[] patch(List<BillingTest> tests) throws Exception
    {
        return getJdbcTemplate().batchUpdate(""
                + "UPDATE lab900 "
                + "SET lab900c2 = ?"
                + " , lab900c3 = ?"
                + " , lab900c4 = ?"
                + " , lab904c1 = ?"
                + " , lab14c1 = ?"
                + " , lab05c1 = ?"
                + " , lab900c5 = ?"
                + " , lab900c6 = ?"
                + " WHERE lab22c1 = ? "
                + " AND lab39c1 = ? ",
                new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                BillingTest test = tests.get(i);
                ps.setBigDecimal(1, test.getServicePrice());
                ps.setBigDecimal(1, test.getPatientPrice());
                ps.setBigDecimal(1, test.getInsurancePrice());
                ps.setInt(2, test.getRate().getId());
                if (test.getAccount() != null && test.getAccount().getId() != null)
                {
                    ps.setInt(3, test.getAccount().getId());
                }
                else
                {
                    ps.setNull(3, java.sql.Types.INTEGER);
                }

                if (test.getBranch() != null && test.getBranch().getId() != null)
                {
                    ps.setInt(4, test.getBranch().getId());
                }
                else
                {
                    ps.setNull(4, java.sql.Types.INTEGER);
                }

                ps.setBigDecimal(5, test.getTax());
                ps.setBigDecimal(6, test.getDiscount());
                ps.setLong(7, test.getOrder().getOrderNumber());
                ps.setInt(8, test.getTest().getId());
            }

            @Override
            public int getBatchSize()
            {
                return tests.size();
            }
        });
    }

    /**
     * Obtiene un objeto de la muestra verificada o tambien no verificada en el
     * destino, solo si esa muestra pertenece al mismo
     *
     * @param rate
     * @param test
     * @return
     * @throws Exception Error al obtener las muestras verificadas
     */
    default BillingTestHomeBound getRatetByIdHomeBound(Integer rate, Integer test) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab39.lab39c1 AS idTest, lab39.lab39c2 AS codeTest, ")
                    .append("lab39.lab39c3 AS abbrTest, lab39c4 AS nameTest, lab24.lab24c1 AS idSample, ")
                    .append("lab24.lab24c9 AS codeSample, lab24.lab24c2 AS nameSample, lab24.lab04c1 AS userId, ")
                    .append("lab24.lab24c8 AS takenDate, lab24.lab24c3 AS print, lab24.lab24c4 AS count, lab24.lab24c6 AS info, ")
                    .append("lab56.lab56c1 AS idContainer, lab56.lab56c2 AS nameContainer, lab56.lab56c3 AS photo, ")
                    .append("FROM 39 ")
                    .append("INNER JOIN lab39 ON lab900.lab39c1 = lab39.lab39c1 ")
                    .append("LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 ")
                    .append("LEFT JOIN lab56 ON lab24.lab56c1 = lab56.lab56c1 ")
                    .append(" AND lab900.lab39c1 = ").append(test);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                BillingTestHomeBound bean = new BillingTestHomeBound();
                bean.setId(rs.getInt("idTest"));
                bean.setCode(rs.getString("codeTest"));
                bean.setAbbr(rs.getString("abbrTest"));
                bean.setName(rs.getString("nameTest"));               

                SampleHomeBound sampleHomeBound = new SampleHomeBound();
                sampleHomeBound.setId(rs.getInt("idSample"));
                sampleHomeBound.setCode(rs.getString("codeSample"));
                sampleHomeBound.setName(rs.getString("nameSample"));

                ContainerHomeBound container = new ContainerHomeBound();

                container.setId(rs.getInt("idContainer"));
                container.setName(rs.getString("nameContainer"));
                /*photo*/
                String photo64 = "";
                byte[] photoBytes = rs.getBytes("photo");
                if (photoBytes != null)
                {
                    photo64 = Base64.getEncoder().encodeToString(photoBytes);
                }
                container.setPhotoBase64(photo64);
                sampleHomeBound.setContainer(container);
                sampleHomeBound.setTakenUser(rs.getInt("userId"));
                sampleHomeBound.setTakenDate(rs.getDate("takenDate"));
                sampleHomeBound.setPrint(rs.getBoolean("print"));
                sampleHomeBound.setCount(rs.getInt("count"));
                sampleHomeBound.setInfo(rs.getString("info"));
                bean.setSample(sampleHomeBound);

                return bean;
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Consulta el impuesto de un examen por el id del mismo
     *
     * @param testId Id del examen
     * @return -1
     * @throws Exception Error al obtener las muestras verificadas
     */
    default Double getTestTaxByTestId(Integer testId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab39c49")
                    .append(" FROM lab39 ")
                    .append("WHERE lab39.lab39c1 = ").append(testId);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getDouble("lab39c49");
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
}
