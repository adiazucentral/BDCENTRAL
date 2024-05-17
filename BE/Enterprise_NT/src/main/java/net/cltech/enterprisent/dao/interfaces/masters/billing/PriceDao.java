package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.masters.billing.PriceAssigment;
import net.cltech.enterprisent.domain.masters.billing.PriceAssignmentBatch;
import net.cltech.enterprisent.domain.masters.billing.TestPrice;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * Precios .
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/08/2017
 * @see Creación
 */
public interface PriceDao
{

    /**
     * Actualiza el precio de un examen
     *
     * @param rate Información de la tarifa
     *
     * @return registros actualizados
     * @throws Exception Error
     */
    default int priceUpdate(PriceAssigment rate) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return getJdbcTemplate().update("UPDATE lab120 SET lab120c1 = ? "
                + ", lab120c2 = ?"
                + ", lab120c3 = ?"
                + ", lab04c1 = ?"
                + " WHERE lab39c1 = ? "
                + "AND lab904c1 = ? "
                + "AND lab116c1 = ?",
                rate.getTest().getPrice(), 
                rate.getTest().getPatientPercentage() == null ? 0 : rate.getTest().getPatientPercentage(), 
                timestamp, 
                rate.getUser().getId(), 
                rate.getTest().getId(), 
                rate.getId(), 
                rate.getIdValid());
    }

    /**
     * Inserta el precio de un examen
     *
     * @param rate Información de la tarifa
     *
     * @return registros creados
     * @throws Exception Error
     */
    default int priceCreate(PriceAssigment rate) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab120");
        HashMap parameters = new HashMap();
        parameters.put("lab120c1", rate.getTest().getPrice());
        parameters.put("lab39c1", rate.getTest().getId());
        parameters.put("lab904c1", rate.getId());
        parameters.put("lab116c1", rate.getIdValid());
        parameters.put("lab120c2", rate.getTest().getPatientPercentage() == null ? 0 : rate.getTest().getPatientPercentage());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        parameters.put("lab120c3", timestamp);
        parameters.put("lab04c1", rate.getUser().getId());

        return insert.execute(parameters);
    }

    /**
     * Verifica si existe el registro en la base de datos
     *
     * @param rateId id tarifa
     * @param testId id examen
     * @param validId id vigencia
     *
     * @return si existe en base de datos
     * @throws Exception error
     */
    default boolean priceExists(int rateId, int testId, int validId) throws Exception
    {
        try
        {
            String query = "SELECT lab120c1 FROM lab120 "
                    + "WHERE lab904c1 = ? AND lab39c1 = ? AND lab116c1 = ? ";

            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int i) -> true, rateId, testId, validId);
        } catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Trae informacion de un precio por servicio
     *
     * @param rateId id tarifa
     * @param testId id examen
     * @param validId id vigencia
     *
     * @return si existe en base de datos devuelve objeto
     * @throws Exception error
     */
    default PriceAssigment get(int rateId, int testId, int validId) throws Exception
    {
        try
        {
            String query = "SELECT lab120c1"
                    + ", lab39.lab39c1"
                    + ", lab120.lab904c1"
                    + ", lab120.lab116c1"
                    + ", lab39.lab39c4"
                    + ", lab116c2"
                    + ", lab904.lab904c3"
                    + ", lab120.lab120c2 AS patientPercentage"
                    + ", lab120.lab120c3 AS lastTransaction"
                    + ", lab04.lab04c1 AS lab04c1"
                    + ", lab04.lab04c2 AS lab04c2"
                    + ", lab04.lab04c3 AS lab04c3"
                    + ", lab04.lab04c4 AS lab04c4"
                    + " FROM lab120 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab120. lab39c1 "
                    + "INNER JOIN lab116 ON lab116.lab116c1 = lab120.lab116c1 "
                    + "INNER JOIN lab904 ON lab904.lab904c1 = lab120.lab904c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab120.lab04c1 "
                    + "WHERE lab120.lab904c1 = ? AND lab120.lab39c1 = ? AND lab120.lab116c1 = ? ";

            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int i) ->
            {
                PriceAssigment price = new PriceAssigment();
                price.setId(rs.getInt("lab904c1"));
                price.setIdValid(rs.getInt("lab116c1"));
                price.setNameValid(rs.getString("lab116c2"));
                price.setNameRate(rs.getString("lab904c3"));
                price.setLastTransaction(rs.getTimestamp("lastTransaction"));
                price.getUser().setId(rs.getInt("lab04c1"));
                price.getUser().setName(rs.getString("lab04c2"));
                price.getUser().setLastName(rs.getString("lab04c3"));
                price.getUser().setUserName(rs.getString("lab04c4"));
                TestPrice test = new TestPrice();
                test.setId(rs.getInt("lab39c1"));
                test.setPrice(rs.getBigDecimal("lab120c1"));
                test.setName(rs.getString("lab39c4"));
                price.setTest(test);
                price.setPatientPercentage(rs.getDouble("patientPercentage"));
                return price;
            }, rateId, testId, validId);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Inserta precios en bloque
     *
     * @param rate datos a ser importados
     *
     * @return registros afectados
     * @throws Exception Error
     */
    default int priceBatchCreate(PriceAssigment rate) throws Exception
    {

        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab120");

        for (TestPrice test : rate.getImportTests())
        {
            HashMap parameters = new HashMap();
            parameters.put("lab120c1", test.getPrice());
            parameters.put("lab39c1", test.getId());
            parameters.put("lab904c1", rate.getId());
            parameters.put("lab116c1", rate.getIdValid());
            parameters.put("lab120c2", test.getPatientPercentage() == null ? rate.getTest().getPatientPercentage() == null ? 0 : rate.getPatientPercentage(): test.getPatientPercentage());
            Timestamp timestamp = new Timestamp(new Date().getTime());
            parameters.put("lab120c3", timestamp);
            parameters.put("lab04c1", rate.getUser().getId());
            batchArray.add(parameters);
        }

        return insert.executeBatch(batchArray.toArray(new HashMap[rate.getImportTests().size()])).length;
    }

    /**
     * Elimina los precios de una tarifa para la vigencia
     *
     * @param idRate id tarifa
     * @param idValid id vigencía
     *
     * @return registros afectados
     */
    default int deletePrices(int idRate, int idValid)
    {
        String deleteSql = "DELETE FROM lab120 WHERE lab116c1 = ? AND lab904c1 = ?";
        return getJdbcTemplate().update(deleteSql, idValid, idRate);
    }

    /**
     * Lista examenes configurados de facturacion con sus preciós
     *
     * @param idValid id de la vigencia
     * @param idRate id de la tarifa
     *
     * @return lista de examenes con precios
     * @throws Exception Error
     */
    default List<TestPrice> listTestPrices(int idValid, int idRate) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab39.lab39c1"
                    + ", lab39c2"
                    + ", lab39c3"
                    + ", lab39c3"
                    + ", lab39c4"
                    + ", lab39.lab43c1"
                    + ", lab120c1"
                    + ", lab120.lab120c2 AS patientPercentage"
                    + ", lab120.lab120c3 AS lastTransaction"
                    + ", lab04.lab04c1 AS lab04c1"
                    + ", lab04.lab04c2 AS lab04c2"
                    + ", lab04.lab04c3 AS lab04c3"
                    + ", lab04.lab04c4 AS lab04c4"
                    + " FROM lab39 "
                    + "LEFT JOIN lab120 ON lab39.lab39c1 = lab120.lab39c1 AND lab116c1 = ?  AND lab904c1 = ? "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab120.lab04c1 "
                    + "WHERE lab39.lab07c1 = 1 AND lab39c20 = 1 ", (ResultSet rs, int i) ->
            {
                TestPrice test = new TestPrice();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.getArea().setId(rs.getInt("lab43c1"));
                test.setPrice(rs.getString("lab120c1") == null ? BigDecimal.ZERO : rs.getBigDecimal("lab120c1"));
                test.setPatientPercentage(rs.getDouble("patientPercentage"));
                test.setLastTransaction(rs.getTimestamp("lastTransaction"));
                test.getUser().setId(rs.getInt("lab04c1"));
                test.getUser().setName(rs.getString("lab04c2"));
                test.getUser().setLastName(rs.getString("lab04c3"));
                test.getUser().setUserName(rs.getString("lab04c4"));
                return test;
            }, idValid, idRate);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Elimina los precios de la vigencia activa
     *
     * @return registros afectados
     * @throws Exception
     */
    default int deleteActivePrices() throws Exception
    {
        String deleteSql = "DELETE FROM lab55";
        return getJdbcTemplate().update(deleteSql);
    }

    /**
     * Activa la vigencia y copia precios a vigencia activa
     *
     * @param idValid id de la vigencia
     *
     * @return registros afectados
     * @throws java.lang.Exception Error SQL
     */
    default int activeFeeschedule(int idValid) throws Exception
    {
        getJdbcTemplate().update("UPDATE lab116 SET lab07c1 = 0");
        getJdbcTemplate().update("UPDATE lab116 SET lab07c1 = 1 WHERE lab116c1 = ?", idValid);
        String sql = "INSERT INTO lab55(lab39c1,lab904c1,lab55c1, lab55c2)"
                + " SELECT lab39c1,lab904c1,lab120c1, lab120c2 FROM lab120 WHERE lab116c1 = ?";
        return getJdbcTemplate().update(sql, idValid);
    }
    
    /**
     * Elimina los precios de una lista de tarifas 
     *
     * @param rates Lista de ids de las tarifas
     * @param idValid Id de la vigencia
     *
     */
    default void deleteListPrices(List<Integer> rates, Integer idValid)
    {
        StringBuilder query = new StringBuilder();
        query.append(" DELETE FROM lab120 WHERE lab116c1 = ").append(idValid)
            .append(" AND lab904c1 in (").append(rates.stream().map(rate -> rate.toString()).collect(Collectors.joining(","))).append(")");         
        getJdbcTemplate().update(query.toString());

    }
    
    /**
     * Inserta precios en bloque
     *
     * @param list datos a ser importados
     *
     * @return registros afectados
     * @throws Exception Error
     */
    public int priceBatchCreate(List<PriceAssignmentBatch> list) throws Exception;
    
    /**
     * Inserta precios en bloque
     *
     * @param list datos a ser importados
     *
     * @return registros afectados
     * @throws Exception Error
     */
//    default int priceBatchCreate(List<PriceAssignmentBatch> list) throws Exception
//    {
//
//        List<HashMap> batchArray = new ArrayList<>();
//        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
//                .withTableName("lab120");
//
//        list.stream().map((test) -> {
//            HashMap parameters = new HashMap();
//            parameters.put("lab120c1", test.getPrice());
//            parameters.put("lab39c1", test.getIdTest());
//            parameters.put("lab904c1", test.getIdRate());
//            parameters.put("lab116c1", test.getIdValid());
//            parameters.put("lab120c2", test.getPatientPercentage() == null ? 0 : test.getPatientPercentage());
//            Timestamp timestamp = new Timestamp(new Date().getTime());
//            parameters.put("lab120c3", timestamp);
//            parameters.put("lab04c1", test.getUser().getId());
//            return parameters;
//        }).forEachOrdered((parameters) -> {
//            batchArray.add(parameters);
//        });
//
//        insert.executeBatch(batchArray.toArray(new HashMap[list.size()]));
//        return 1;
//    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
