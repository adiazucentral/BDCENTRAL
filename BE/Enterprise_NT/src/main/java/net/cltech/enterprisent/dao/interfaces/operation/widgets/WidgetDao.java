/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.widgets;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.tracking.SampleTracking;
import net.cltech.enterprisent.domain.operation.widgets.WidgetSample;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * widgets.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/12/2017
 * @see Creación
 */
public interface WidgetDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Identificar si ya se encuentra creado el registro de valores para el dia
     * actual.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    default WidgetSample getWidgetValueToday(int date, int idBranch) throws Exception
    {
        try
        {
            return getConnection().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab300c1, lab300c2, lab300c3, lab300c4, lab300c5, lab300c6, lab300c7 "
                    + "FROM lab300 "
                    + "WHERE lab300c1 = ? AND lab05c1 = ?",
                    new Object[]
                    {
                        date, idBranch
                    }, (ResultSet rs, int i) ->
            {
                WidgetSample widgetSample = new WidgetSample();
                widgetSample.setDateNumber(rs.getInt("lab300c1"));
                widgetSample.setSampleOrdered(rs.getInt("lab300c2"));
                widgetSample.setSampleVerified(rs.getInt("lab300c3"));
                widgetSample.setSampleRejected(rs.getInt("lab300c4"));
                widgetSample.setSampleRetake(rs.getInt("lab300c5"));
                widgetSample.setSampleDelayed(rs.getInt("lab300c6"));
                widgetSample.setSampleExpired(rs.getInt("lab300c7"));
                return widgetSample;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new WidgetSample(0, 0, 0, 0, 0, 0, 0);
        }
    }

    /**
     * Identificar si ya se encuentra creado el registro de valores para el dia
     * actual.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    default boolean validateWidgetValueToday(int date, int idBranch) throws Exception
    {
        try
        {
            return getConnection().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab300c1 "
                    + "FROM lab300 "
                    + "WHERE lab300c1 = ? AND lab05c1 = ?",
                    new Object[]
                    {
                        date, idBranch
                    }, (ResultSet rs, int i) -> true);
        } catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Identificar si ya se encuentra creado el registro de valores para el dia
     * actual.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    default boolean createWidgetValueToday(int date, int idBranch) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab300");

        HashMap parameters = new HashMap();
        parameters.put("lab300c1", date);
        parameters.put("lab05c1", idBranch);
        parameters.put("lab300c2", 0);
        parameters.put("lab300c3", 0);
        parameters.put("lab300c4", 0);
        parameters.put("lab300c5", 0);
        parameters.put("lab300c6", 0);
        parameters.put("lab300c7", 0);

        insert.execute(parameters);
        return true;
    }

    /**
     * Incrementar muestras ingresadas en widgets.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @throws Exception Error en la base de datos.
     */
    default void addOrderedWidget(int date, int idBranch) throws Exception
    {
        getConnection().update("UPDATE lab300 SET lab300c2 = lab300c2 + 1 "
                + "WHERE lab300c1 = ? AND lab05c1 = ?",
                date, idBranch);
    }

    /**
     * Incrementar muestras verificadas en widgets.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @throws Exception Error en la base de datos.
     */
    default void addVerifiedWidget(int date, int idBranch) throws Exception
    {
        getConnection().update("UPDATE lab300 SET lab300c3 = lab300c3 + 1 "
                + "WHERE lab300c1 = ? AND lab05c1 = ?",
                date, idBranch);
    }

    /**
     * Incrementar muestras rechazadas en widgets.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @throws Exception Error en la base de datos.
     */
    default void addRejectedWidget(int date, int idBranch) throws Exception
    {
        getConnection().update("UPDATE lab300 SET lab300c4 = lab300c4 + 1 "
                + "WHERE lab300c1 = ? AND lab05c1 = ?",
                date, idBranch);
    }

    /**
     * Incrementar muestras retomadas en widgets.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @throws Exception Error en la base de datos.
     */
    default void addRetakedWidget(int date, int idBranch) throws Exception
    {
        getConnection().update("UPDATE lab300 SET lab300c5 = lab300c5 + 1 "
                + "WHERE lab300c1 = ? AND lab05c1 = ?",
                date, idBranch);
    }

    /**
     * Incrementar muestras vencidas en widgets.
     *
     * @param quantity Cantidad de muestras vencidas.
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @throws Exception Error en la base de datos.
     */
    default void updateExpiredWidget(int quantity, int date, int idBranch) throws Exception
    {
        getConnection().update("UPDATE lab300 SET lab300c7 = ? "
                + "WHERE lab300c1 = ? AND lab05c1 = ?",
                quantity, date, idBranch);
    }

    /**
     * Consulta la muestras que se tomaron el dia actual.
     *
     * @return Retorna la trazabilidad de la muestra.
     */
    default List<SampleTracking> getSampleTakeToday() throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab144.lab22c1, lab24.lab24c1, lab24.lab24c12, lab144.lab30c1, lab144.lab05c1, lab144.lab04c1, lab144.lab144c1, lab144.lab144c2, lab144.lab144c3, lab144.lab144c4 "
                    + "FROM lab144 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab144.lab24c1 "
                    + "WHERE lab144.lab144c3 = 3 AND DATE(lab144c2) = DATE(?) ";

            return getConnection().query(query,
                    new Object[]
                    {
                        timestamp
                    }, (ResultSet rs, int i) ->
            {
                SampleTracking sampleTracking = new SampleTracking();
                sampleTracking.setOrder(rs.getLong("lab22c1"));
                sampleTracking.setId(rs.getInt("lab144c1"));
                sampleTracking.setSample(rs.getInt("lab24c1"));
                sampleTracking.setQualityTime(rs.getString("lab24c12") == null ? null : rs.getLong("lab24c12"));

                sampleTracking.getBranch().setId(rs.getInt("lab05c1"));

                sampleTracking.getUser().setId(rs.getInt("lab04c1"));

                sampleTracking.getMotive().setId(rs.getInt("lab30c1"));

                sampleTracking.setDate(rs.getTimestamp("lab144c2"));
                sampleTracking.setState(rs.getInt("lab144c3"));
                sampleTracking.setComment(rs.getString("lab144c4"));

                sampleTracking.setResultTests(listResultTest(sampleTracking.getOrder(), sampleTracking.getSample()));

                return sampleTracking;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista de exámenes para el procesamiento de los resultados.
     *
     * @param orderId Id de ordenes.
     * @param sampleId Id de la muestra.
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     */
    default List<ResultTest> listResultTest(long orderId, Integer sampleId)
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT   lab57.lab22c1 ");
            select.append(" , lab43.lab43c1 "); //area id
            select.append(" , lab43.lab43c2 "); //area codigo
            select.append(" , lab43.lab43c3 "); //area abreviatura
            select.append(" , lab43.lab43c4 "); //area nombre
            select.append(" , lab57.lab39c1 "); //examen
            select.append(" , lab39c2 "); //código
            select.append(" , lab39c3 "); //Abrv
            select.append(" , lab39c4 "); //nombre
            select.append(" , lab57c1 "); //resultado
            select.append(" , lab57c2 "); //fecha resultado
            select.append(" , lab57c8 "); //estado
            select.append(" , lab57c14 "); //estado
            select.append(" , lab57c16 "); //estado muestra
            select.append(" , lab39c11 "); //tipo resultado
            select.append(" , lab57c9 "); //patología
            select.append(" , lab57.lab48c12 "); //referecnia mínima
            select.append(" , lab57.lab48c13 "); //referecnia máxima
            select.append(" , lab57.lab48c5 "); //panico minimo
            select.append(" , lab57.lab48c6 "); //panico máximo
            select.append(" , lab57.lab48c14 "); //reportable minimo
            select.append(" , lab57.lab48c15 "); //reportable máximo
            select.append(" , lab48c16 "); //pánico crítico
            select.append(" , lab50n.lab50c2 as lab50c2n "); //normal literal
            select.append(" , lab50p.lab50c2 as lab50c2p "); //pánico literal
            select.append(" , lab45c2 "); //unidades
            select.append(" , lab39c3 "); //abreviatura
            select.append(" , lab39c12 "); //decimales
            select.append(" , lab39c27 "); //confidencial
            select.append(" , lab57c32 "); //tiene comentario
            select.append(" , lab57c33 "); //numero de repeticiones
            select.append(" , lab57c24 "); //numero modificacion
            select.append(" , lab95c1 "); //comentario
            select.append(" , lab95c2 "); //fecha comentario
            select.append(" , lab95c3 "); //patología comentario
            select.append(" , lab95.lab04c1 "); //usuario comentario
            select.append(" , lab57.lab57c27 "); //delta mínimo
            select.append(" , lab57.lab57c28 "); //delta máximmo
            select.append(" , lab57.lab57c6 "); //último resultado
            select.append(" , lab57.lab57c7 "); //fecha último resultado
            select.append(" , lab57.lab57c30 "); //penúltimo resultado
            select.append(" , lab57.lab57c31 "); //fecha penúltimo resultado
            select.append(" , lab57.lab57c18 "); //fecha validacion
            select.append(" , lab57.lab57c4 "); //fecha ingreso
            StringBuilder from = new StringBuilder();
            from.append(" FROM    lab57 ");
            from.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            from.append(" INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
            from.append(" LEFT  JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 ");
            from.append(" LEFT  JOIN lab50 lab50n ON lab50n.lab50c1 = lab48.lab50c1_3 ");
            from.append(" LEFT  JOIN lab50 lab50p ON lab50p.lab50c1 = lab48.lab50c1_1 ");
            from.append(" LEFT  JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ");
            StringBuilder where = new StringBuilder();
            where.append(" WHERE ");
            where.append(" lab39c37 = 0 ");
            where.append(" AND lab57.lab22c1 = ? ");
            List<Object> params = new ArrayList<>();
            params.add(orderId);
            if (sampleId != null)
            {
                where.append(" AND lab39.lab24c1 = ? ");
                params.add(sampleId);
            }
            RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setOrder(rs.getLong("lab22c1"));
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setTestCode(rs.getString("lab39c2"));
                bean.setTestName(rs.getString("lab39c4"));
                bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                bean.setResultDate(rs.getTimestamp("lab57c2"));
                bean.setValidationDate(rs.getTimestamp("lab57c18"));
                bean.setEntryDate(rs.getTimestamp("lab57c4"));
                bean.setState(rs.getInt("lab57c8"));
                bean.setSampleState(rs.getInt("lab57c16"));
                bean.setAreaId(rs.getInt("lab43c1"));
                bean.setAreaCode(rs.getString("lab43c2"));
                bean.setAreaAbbr(rs.getString("lab43c3"));
                bean.setAreaName(rs.getString("lab43c4"));
                bean.setResultType(rs.getShort("lab39c11"));
                bean.setPathology(rs.getInt("lab57c9"));
                bean.setRefLiteral(rs.getString("lab50c2n"));
                bean.setPanicLiteral(rs.getString("lab50c2p"));
                bean.setConfidential(rs.getInt("lab39c27") == 1);
                bean.setRepeatAmmount(rs.getInt("lab57c33"));
                bean.setModificationAmmount(rs.getInt("lab57c24"));
                bean.setEntry(rs.getString("lab57c14") == null);
                BigDecimal refMin = rs.getBigDecimal("lab48c12");
                BigDecimal refMax = rs.getBigDecimal("lab48c13");
                if (!rs.wasNull())
                {
                    bean.setRefMin(refMin);
                    bean.setRefMax(refMax);
                    
                    String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                    String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                    bean.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                } else
                {
                    bean.setRefMin(BigDecimal.ZERO);
                    bean.setRefMax(BigDecimal.ZERO);
                    bean.setRefInterval(bean.getRefLiteral());
                }

                BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                BigDecimal panicMax = rs.getBigDecimal("lab48c6");
                if (!rs.wasNull())
                {
                    bean.setPanicMin(panicMin);
                    bean.setPanicMax(panicMax);
                    String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                    String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                    bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                } else
                {
                    bean.setPanicMin(BigDecimal.ZERO);
                    bean.setPanicMax(BigDecimal.ZERO);
                    bean.setPanicInterval(bean.getPanicLiteral());
                }

                BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                BigDecimal reportedMax = rs.getBigDecimal("lab48c15");
                if (!rs.wasNull())
                {
                    bean.setReportedMin(reportedMin);
                    bean.setReportedMax(reportedMax);
                    String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                    String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                    bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                } else
                {
                    bean.setReportedMin(BigDecimal.ZERO);
                    bean.setReportedMax(BigDecimal.ZERO);
                }

                BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                BigDecimal deltaMax = rs.getBigDecimal("lab57c28");
                if (!rs.wasNull())
                {
                    bean.setDeltaMin(deltaMin);
                    bean.setDeltaMax(deltaMax);
                    bean.setDeltaInterval(deltaMin.toString() + " - " + deltaMax.toString());
                } else
                {
                    bean.setDeltaMin(BigDecimal.ZERO);
                    bean.setDeltaMax(BigDecimal.ZERO);
                    bean.setDeltaInterval("");
                }

                bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));
                bean.setLastResultDate(rs.getDate("lab57c7"));
                bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                bean.setSecondLastResultDate(rs.getDate("lab57c31"));

                bean.setCritic(rs.getShort("lab48c16"));
                bean.setUnit(rs.getString("lab45c2"));
                bean.setAbbreviation(rs.getString("lab39c3"));
                bean.setDigits(rs.getShort("lab39c12"));
                bean.setHasComment(rs.getShort("lab57c32") == 1);

                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setTestId(rs.getInt("lab39c1"));
                comment.setComment(rs.getString("lab95c1"));
                comment.setCommentDate(rs.getDate("lab95c2"));
                comment.setPathology(rs.getShort("lab95c3"));
                comment.setUserId(rs.getInt("lab04c1"));
                comment.setCommentChanged(false);
                bean.setResultComment(comment);

                return bean;
            };
            return getConnection().query(select.toString() + from.toString() + where.toString(), params.toArray(), mapper);
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

}
