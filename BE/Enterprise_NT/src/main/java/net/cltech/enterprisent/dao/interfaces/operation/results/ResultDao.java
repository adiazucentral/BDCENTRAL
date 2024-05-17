package net.cltech.enterprisent.dao.interfaces.operation.results;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.control.PatientResult;
import net.cltech.enterprisent.domain.integration.resultados.RequestUpdateSendResult;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.list.RemissionLaboratory;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.CentralSystemResults;
import net.cltech.enterprisent.domain.operation.results.FindShippedOrders;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * hojas de trabajo.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/10/2017
 * @see Creación
 */
public interface ResultDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista las pruebas de una orden.
     *
     * @param order Número de orden.
     * @param test id del examen
     * @param reference valores de referencia a actualizar
     * @param patology
     *
     * @return numero de registros afectados
     */
    default int updateReferenceValue(long order, int test, ReferenceValue reference, int patology)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        return getJdbcTemplate().update("UPDATE " + lab57 + " SET lab48c1 = ?, "
                + "lab48c5 = ?, "
                + "lab48c6 = ?, "
                + "lab48c12 = ?, "
                + "lab48c13 = ?, "
                + "lab50c1_1 = ?, "
                + "lab50c1_3 = ?, "
                + "lab48c14 = ?, "
                + "lab48c15 = ?, "
                + "lab57c9 = ? "
                + "WHERE lab22c1 = ? AND lab39c1 = ?",
                reference.getId(),
                reference.getPanicMin(),
                reference.getPanicMax(),
                reference.getNormalMin(),
                reference.getNormalMax(),
                reference.getPanic().getId(),
                reference.getNormal().getId(),
                reference.getReportableMin(),
                reference.getReportableMax(),
                patology, order, test);
    }

    /**
     * Obtiene todos los ids de los analitos asignados a una orden para la
     * asignacion de valores de referencia
     *
     * @param order Numero de orden
     * @return Lista de ids (Lab39C1)
     * @throws Exception Error en base de datos
     */
    default List<Integer> getTestsIdsReferenceValue(long order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab39c1 "
                    + "FROM      " + lab57 + " as lab57 "
                    + "WHERE    lab22c1 = ? and lab24c1 is not null ",
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("lab39c1");
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene todos los ids de los examenes asignados a una orden
     *
     * @param order Numero de orden
     * @return Lista de ids (Lab39C1)
     * @throws Exception Error en base de datos
     */
    default List<Integer> getTestsIds(long order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab39c1 "
                    + "FROM      " + lab57 + " as lab57 "
                    + "WHERE    lab22c1 = ? ",
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("lab39c1");
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            OrderCreationLog.info("error examenes lab57" + ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene el resultado de una orden y un examen
     *
     * @param order Numero de orden
     * @param test
     * @return Resultado
     * @throws Exception Error en base de datos
     */
    default String getTestResult(long order, int test) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab57c1 "
                    + "FROM      " + lab57 + " as lab57 "
                    + "WHERE    lab22c1 = ? and lab39c1 = ? ",
                    (ResultSet rs, int i)
                    ->
            {
                return Tools.decrypt(rs.getString("lab57c1"));

            }, order, test);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * obtiene el tipo de resultado de un examen
     *
     * @param order Numero de orden
     * @param test
     * @return Resultado
     * @throws Exception Error en base de datos
     */
    default Short getTestTypeResult(int test) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab39c11 "
                    + "FROM     lab39 "
                    + "WHERE    lab39c1 = ? ",
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getShort("lab39c11");

            }, test);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Inserta todos los examenes a una orden
     *
     * @param order Numero de Orden
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @param date Fecha de Creacion
     * @param user Usuario de Creacion
     * @throws Exception Error en base de datos
     */
    default void saveResult(long order, List<Test> tests, Date date, int user) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;

            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(lab57);
            List<HashMap<String, Object>> batch = new ArrayList<>(0);
            HashMap<String, Object> parameters = null;

            SimpleJdbcInsert insertCommet = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(lab95);
            List<HashMap<String, Object>> batchComment = new ArrayList<>(0);
            HashMap<String, Object> parametersComment = null;

            for (Test test : tests)
            {
                test.setFixedComment(test.getFixedComment() == null ? "" : test.getFixedComment());
                parameters = new HashMap<>(0);
                parameters.put("lab22c1", order); //Orden
                parameters.put("lab39c1", test.getId()); //Id Examen
                parameters.put("lab57c1", test.getAutomaticResult() == null ? null : Tools.encrypt(test.getAutomaticResult())); //Resultado
                parameters.put("lab57c2", test.getAutomaticResult() == null ? null : new Timestamp(date.getTime())); //Fecha del resultado
                parameters.put("lab57c3", test.getAutomaticResult() == null ? null : user); //usuario del resultado
                parameters.put("lab57c4", new Timestamp(date.getTime())); //Fecha Ingreso
                parameters.put("lab57c5", user); //Usuario Ingreso
                parameters.put("lab57c8", test.getTestState()); //Estado Examen
                parameters.put("lab57c9", 0); //Patologia
                parameters.put("lab57c10", 0); //Bloqueado
                parameters.put("lab57c14", test.getPanel() == null ? null : test.getPanel().getId()); //Id Perfil padre
                parameters.put("lab57c15", test.getPack() == null ? null : test.getPack().getId()); //Id Paquete padre
                parameters.put("lab57c16", test.getSampleState()); //Estado de la muestra
                parameters.put("lab45c2", test.getUnit() == null ? null : test.getUnit().getName()); //Nombre Unidad
                parameters.put("lab64c1", test.getTechnique() == null ? null : test.getTechnique().getId()); //Id Tecnica
                parameters.put("lab57c24", 0); //Numero Modificaciones
                parameters.put("lab57c25", test.getPrint()); //Si se imprime
                parameters.put("lab57c26", 0); //Tiene antibiograma
                parameters.put("lab40c1", test.getLaboratory().getId()); //Laboratorio de procesamiento
                parameters.put("lab57c29", test.isHistoricGraphic() ? 1 : 0); //Imprime grafica de historico en el reporte
                parameters.put("lab24c1", test.getSample().getId()); //Muestra
                parameters.put("lab57c41", 0); //Cantidad de Adjuntos
                parameters.put("lab57c43", 0); //Tiene Delta
                parameters.put("lab57c50", 0); //No se ha enviado al HIS
                parameters.put("lab57c53", 0); //Dilución
                parameters.put("lab57c32", !"".equals(test.getFixedComment().trim()) ? 1 : 0); //Dilución
                parameters.put("lab57c63", test.getPrintComment()); //Dilución
                parameters.put("lab57c61", 0); //Comentario interno
                parameters.put("lab57c71", test.getCommentResult()); //Comentario interno II
                parameters.put("lab57c49", test.getCodeCups()); //CODIGO HOMOLOGADO HIS

                if (test.getLastResult() != null)
                {
                    if (test.getLastResult().getResult() != null)
                    {
                        parameters.put("lab57c6", Tools.encrypt(test.getLastResult().getResult())); //Ultimo resultado
                        parameters.put("lab57c7", test.getLastResult().getDateResult()); //Fecha del ultimo resultado
                    }
                }

                if (test.getSecondLastResult() != null)
                {
                    if (test.getSecondLastResult().getResult() != null)
                    {
                        // Validamos que no este encriptado el penultimo resultado con el fin de saber si demos encriptarlo o no, porque ya viene asi 
                        parameters.put("lab57c30", Tools.encrypt(test.getSecondLastResult().getResult())); //Segundo Ultimo resultado
                        parameters.put("lab57c31", test.getSecondLastResult().getDateResult()); //Fecha del segundo ultimo resultado
                    }
                }
                batch.add(parameters);
                //paso de los comentarios fijos a el comentario del resultado
                if (!"".equals(test.getFixedComment().trim()))
                {
                    parametersComment = new HashMap<>(0);
                    parametersComment.put("lab22c1", order);
                    parametersComment.put("lab39c1", test.getId());
                    parametersComment.put("lab95c1", test.getFixedComment());
                    parametersComment.put("lab95c2", new Timestamp(date.getTime()));
                    parametersComment.put("lab95c3", 0);
                    parametersComment.put("lab95c4", 0);
                    parametersComment.put("lab04c1", user);

                    batchComment.add(parametersComment);
                }
            }
            insert.executeBatch(batch.toArray(new HashMap[0]));
            insertCommet.executeBatch(batchComment.toArray(new HashMap[0]));
        } catch (Exception e)
        {
            ResultsLog.error(e);
        }
    }

    /**
     * Inserta todos los examenes a una orden
     *
     * @param comment
     * @param user
     * @throws Exception Error en base de datos
     */
    default void insertCommentInternalResult(ResultTestComment comment, int user) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(comment.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab212 = year.equals(currentYear) ? "lab212" : "lab212_" + year;

            Date date = new Date();
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(lab212);

            HashMap<String, Object> parameters = null;
            StringBuilder query = new StringBuilder();

            parameters = new HashMap<>(0);
            parameters.put("lab22c1", comment.getOrder());
            parameters.put("lab39c1", comment.getTestId());
            parameters.put("lab212c1", comment.getComment());
            parameters.put("lab212c2", new Timestamp(date.getTime()));
            parameters.put("lab04c1", user);
            insert.execute(parameters);

            query.append("UPDATE LAB57 SET ")
                    .append("lab57c61 =").append("".equals(comment.getComment().trim()) ? 0 : 1)
                    .append(" WHERE ")
                    .append(" lab22c1 = ").append(comment.getOrder())
                    .append(" AND lab39c1 = ").append(comment.getTestId());

            getJdbcTemplate().update(query.toString());

        } catch (Exception e)
        {
            ResultsLog.error(e);
        }
    }

    /**
     * Inserta todos los examenes a una orden
     *
     * @param comment
     * @param user
     * @throws Exception Error en base de datos
     */
    default void updateObservacion(ResultTestComment comment, int user) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(comment.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            Date date = new Date();

            StringBuilder query = new StringBuilder();
            //paso de los comentarios fijos a el comentario del resultado

            query.append("UPDATE ").append(lab22).append(" SET ")
                    .append(" lab22c16 = '").append(comment.getComment()).append("'")
                    .append(" WHERE ")
                    .append(" lab22c1 = ").append(comment.getOrder());
            getJdbcTemplate().update(query.toString());            

        } catch (Exception e)
        {
            ResultsLog.error(e);
        }
    }
    /**
     * Inserta todos los examenes a una orden
     *
     * @param comment
     * @param user
     * @throws Exception Error en base de datos
     */
    default void updateCommentInternalResult(ResultTestComment comment, int user) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(comment.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab212 = year.equals(currentYear) ? "lab212" : "lab212_" + year;

            Date date = new Date();

            StringBuilder query = new StringBuilder();
            //paso de los comentarios fijos a el comentario del resultado

            query.append("UPDATE ").append(lab212).append(" SET ")
                    .append(" lab22c1 = ").append(comment.getOrder())
                    .append(", lab39c1 = ").append(comment.getTestId())
                    .append(", lab212c1 = '").append(comment.getComment()).append("'")
                    .append(", lab212c2 = '").append(new Timestamp(date.getTime())).append("'")
                    .append(", lab04c1 = ").append(user)
                    .append(" WHERE ")
                    .append(" lab22c1 = ").append(comment.getOrder())
                    .append(" AND lab39c1 = ").append(comment.getTestId());
            getJdbcTemplate().update(query.toString());

            query.append("UPDATE LAB57 SET ")
                    .append("lab57c61 =").append("".equals(comment.getComment().trim()) ? 0 : 1)
                    .append(" WHERE ")
                    .append(" lab22c1 = ").append(comment.getOrder())
                    .append(" AND lab39c1 = ").append(comment.getTestId());

            getJdbcTemplate().update(query.toString());

        } catch (Exception e)
        {
            ResultsLog.error(e);
        }
    }

    /**
     * Lista los destinos desdel base de datos.
     *
     * @param orderId
     * @param testId
     * @return Lista de destinos.
     * @throws Exception Error en la base de datos.
     */
    default ResultTestComment getCommentInternalResult(long orderId, int testId) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab212 = year.equals(currentYear) ? "lab212" : "lab212_" + year;

            StringBuilder query = new StringBuilder();
            query.append("SELECT  lab212.lab22c1, ");
            query.append("lab212.lab39c1, ");
            query.append("lab212.lab212c1 ");
            query.append("FROM ").append(lab212).append(" as lab212 ");

            StringBuilder from = new StringBuilder();
            from.append(" WHERE lab212.lab22c1 =  ? AND lab212.lab39c1 = ? ");

            Object[] params = null;
            params = new Object[]
            {
                orderId, testId
            };

            return getJdbcTemplate().queryForObject(query + " " + from, (ResultSet rs, int i) ->
            {
                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setComment(rs.getString("lab212c1"));
                return comment;
            }, params);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista los destinos desdel base de datos.
     *
     * @param orderId
     * @return Lista de destinos.
     * @throws Exception Error en la base de datos.
     */
    default ResultTestComment getObservations(long orderId) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            StringBuilder query = new StringBuilder();
            query.append("SELECT  lab22.lab22c1, ");
            query.append("lab22.lab22c16 ");
            query.append("FROM ").append(lab22).append(" as lab22 ");

            StringBuilder from = new StringBuilder();
            from.append(" WHERE lab22.lab22c1 =  ? ");

            Object[] params = null;
            params = new Object[]
            {
                orderId
            };

            return getJdbcTemplate().queryForObject(query + " " + from, (ResultSet rs, int i) ->
            {
                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setComment(rs.getString("lab22c16"));
                return comment;
            }, params);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * actualiza el historico del examen de una orden.
     *
     * @param order Numero de Orden
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en base de datos
     */
    default void updateHistoricalResult(long order, List<Test> tests) throws Exception
    {
        try
        {
            for (Test test : tests)
            {
                StringBuilder query = new StringBuilder();
                query.append("UPDATE LAB57 SET ")
                        .append("lab57c6 ='").append(Tools.encrypt(test.getLastResult().getResult())).append("'")
                        .append(" , lab57c7 ='").append(test.getLastResult().getDateResult()).append("'")
                        .append(" , lab57c30 ='").append(Tools.encrypt(test.getSecondLastResult().getResult())).append("'")
                        .append(" , lab57c31 ='").append(test.getSecondLastResult().getDateResult()).append("'")
                        .append(" WHERE ")
                        .append(" lab22c1 = ").append(order)
                        .append(" AND lab39c1 = ").append(test.getId());

                getJdbcTemplate().update(query.toString());
            }
        } catch (Exception e)
        {
            ResultsLog.error(e);
        }
    }

    /**
     * Obtiene el historico de resultados
     *
     * @param patientId Id Paciente
     * @param testId Id Prueba
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.HistoricalResult},
     * null si el paciente no tiene resultados historicos
     * @throws Exception Error en base de datosF
     */
    default HistoricalResult get(int patientId, int testId) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c1 "
                    + " , lab39c1 "
                    + " , lab17c1 "
                    + " , lab17c2 "
                    + " , lab04L.lab04c1 AS lab04c1L "
                    + " , lab04L.lab04c2 AS lab04c2L "
                    + " , lab04L.lab04c3 AS lab04c3L "
                    + " , lab04L.lab04c4 AS lab04c4L "
                    + " , lab04L.lab04c16 AS lab04c16L "
                    + " , lab17c3 "
                    + " , lab17c4 "
                    + " , lab04SL.lab04c1 AS lab04c1SL "
                    + " , lab04SL.lab04c2 AS lab04c2SL "
                    + " , lab04SL.lab04c3 AS lab04c3SL "
                    + " , lab04SL.lab04c4 AS lab04c4SL "
                    + " , lab04SL.lab04c16 AS lab04c16SL "
                    + " , lab17c5 "
                    + " , lab17c6 "
                    + " , lab04SLT.lab04c1 AS lab04c1SLT "
                    + " , lab04SLT.lab04c2 AS lab04c2SLT "
                    + " , lab04SLT.lab04c3 AS lab04c3SLT "
                    + " , lab04SLT.lab04c4 AS lab04c4SLT "
                    + " , lab04SLT.lab04c16 AS lab04c16SLT "
                    + "FROM     lab17 "
                    + "         LEFT JOIN lab04 lab04L ON lab04L.lab04c1 = lab17.lab04c1_1 "
                    + "         LEFT JOIN lab04 lab04SL ON lab04SL.lab04c1 = lab17.lab04c1_2 "
                    + "         LEFT JOIN lab04 lab04SLT ON lab04SLT.lab04c1 = lab17.lab04c1_3 "
                    + "WHERE    lab21c1 = ? "
                    + "         AND lab39c1 = ? ",
                    (ResultSet rs, int numRow)
                    ->
            {
                HistoricalResult result = new HistoricalResult();
                result.setPatientId(rs.getInt("lab21c1"));
                result.setTestId(rs.getInt("lab39c1"));
                result.setLastResult(rs.getString("lab17c1") != null ? Tools.decrypt(rs.getString("lab17c1")) : null);
                result.setLastResultDate(rs.getTimestamp("lab17c2"));
                User userL = new User();
                userL.setId(rs.getInt("lab04c1L"));
                userL.setName(rs.getString("lab04c2L"));
                userL.setLastName(rs.getString("lab04c3L"));
                userL.setUserName(rs.getString("lab04c4L"));
                userL.setPhoto(rs.getBytes("lab04c16L") != null ? Base64.getEncoder().encodeToString(rs.getBytes("lab04c16L")) : "");
                result.setLastResultUser(userL);
                if (rs.getString("lab17c3") != null)
                {
                    result.setSecondLastResult(Tools.decrypt(rs.getString("lab17c3")));
                    result.setSecondLastResultDate(rs.getTimestamp("lab17c4"));
                    User userSL = new User();
                    userSL.setId(rs.getInt("lab04c1SL"));
                    userSL.setName(rs.getString("lab04c2SL"));
                    userSL.setLastName(rs.getString("lab04c3SL"));
                    userSL.setUserName(rs.getString("lab04c4SL"));
                    userSL.setPhoto(rs.getBytes("lab04c16SL") != null ? Base64.getEncoder().encodeToString(rs.getBytes("lab04c16SL")) : "");
                    result.setSecondLastResultUser(userSL);
                }
                if (rs.getString("lab17c5") != null)
                {
                    result.setSecondLastResultTemp(Tools.decrypt(rs.getString("lab17c5")));
                    result.setSecondLastResultDateTemp(rs.getTimestamp("lab17c6"));
                    User userSL = new User();
                    userSL.setId(rs.getInt("lab04c1SLT"));
                    userSL.setName(rs.getString("lab04c2SLT"));
                    userSL.setLastName(rs.getString("lab04c3SLT"));
                    userSL.setUserName(rs.getString("lab04c4SLT"));
                    userSL.setPhoto(rs.getBytes("lab04c16SLT") != null ? Base64.getEncoder().encodeToString(rs.getBytes("lab04c16SLT")) : "");
                    result.setSecondLastResultUserTemp(userSL);
                }
                return result;
            }, patientId, testId);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene el historico de resultados
     *
     * @param patientId Id Paciente
     * @param testId Id Prueba
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.HistoricalResult},
     * null si el paciente no tiene resultados historicos
     * @throws Exception Error en base de datosF
     */
    default int getHistorical(int patientId, int testId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab17c1 FROM lab17 WHERE lab21c1 = ? AND lab39c1 = ? ");

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab17c1");
            });
        } catch (DataAccessException e)
        {
            return -1;
        }

    }

    /**
     * Obtiene el historico de resultados basico
     *
     * @param patientId Id Paciente
     * @param testId Id Prueba
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.HistoricalResult},
     * null si el paciente no tiene resultados historicos
     * @throws Exception Error en base de datosF
     */
    default HistoricalResult getBasic(int patientId, int testId) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c1 "
                    + " , lab39c1 "
                    + " , lab17c1 "
                    + " , lab17c2 "
                    + " , lab17c3 "
                    + " , lab17c4 "
                    + " , lab17c5 "
                    + " , lab17c6 "
                    + "FROM     lab17 "
                    + "WHERE    lab21c1 = ? "
                    + "         AND lab39c1 = ? ",
                    (ResultSet rs, int numRow)
                    ->
            {
                HistoricalResult result = new HistoricalResult();
                result.setPatientId(rs.getInt("lab21c1"));
                result.setTestId(rs.getInt("lab39c1"));

                result.setLastResult(rs.getString("lab17c1") != null ? Tools.decrypt(rs.getString("lab17c1")) : null);
                result.setLastResultDate(rs.getTimestamp("lab17c2"));

                if (rs.getString("lab17c3") != null)
                {
                    result.setSecondLastResult(Tools.decrypt(rs.getString("lab17c3")));
                    result.setSecondLastResultDate(rs.getTimestamp("lab17c4"));
                }
                if (rs.getString("lab17c5") != null)
                {
                    result.setSecondLastResultTemp(Tools.decrypt(rs.getString("lab17c5")));
                    result.setSecondLastResultDateTemp(rs.getTimestamp("lab17c6"));
                }
                return result;
            }, patientId, testId);
        } catch (EmptyResultDataAccessException ex)
        {
            OrderCreationLog.info("consulta de historico " + ex);
            return null;
        }
    }

    /**
     * Obtiene el historico de resultados de un paciente
     *
     * @param patientId Id Paciente
     * @param testsId Lista con los ids de los examenes
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.HistoricalResult}
     * @throws Exception Error en base de datos
     */
    default List<HistoricalResult> get(int patientId, List<Integer> testsId) throws Exception
    {
        try
        {
            String tests = "";
            tests = testsId.stream().map((i) -> "" + i + ",").reduce(tests, String::concat);
            tests = tests.contains(",") ? tests.substring(0, tests.lastIndexOf(",")) : tests;
            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab21c1 "
                    + " , lab17.lab39c1 "
                    + " , lab17c1 "
                    + " , lab17c2 "
                    + " , lab04L.lab04c1 AS lab04c1L"
                    + " , lab04L.lab04c2 AS lab04c2L "
                    + " , lab04L.lab04c3 AS lab04c3L "
                    + " , lab04L.lab04c4 AS lab04c4L "
                    + " , lab04L.lab04c16 AS lab04c16L "
                    + " , lab17c3 "
                    + " , lab17c4 "
                    + " , lab39c2 "       
                    + " , lab04SL.lab04c1 AS lab04c1SL "
                    + " , lab04SL.lab04c2 AS lab04c2SL "
                    + " , lab04SL.lab04c3 AS lab04c3SL "
                    + " , lab04SL.lab04c4 AS lab04c4SL "
                    + " , lab04SL.lab04c16 AS lab04c16SL "
                    + "FROM     lab17 "
                    + "         INNER JOIN lab39 ON lab39.lab39c1 = lab17.lab39c1 "
                    + "         LEFT JOIN lab04 lab04L ON lab04L.lab04c1 = lab17.lab04c1_1 "
                    + "         LEFT JOIN lab04 lab04SL ON lab04SL.lab04c1 = lab17.lab04c1_2 "
                    + "WHERE    lab21c1 = ? "
                    + "         AND lab17.lab39c1 IN (" + tests + ") ",
                    (ResultSet rs, int numRow)
                    ->
            {
                HistoricalResult result = new HistoricalResult();
                result.setPatientId(rs.getInt("lab21c1"));
                result.setTestId(rs.getInt("lab39c1"));
                result.setTestCode(rs.getString("lab39c2"));
                result.setLastResult(rs.getString("lab17c1") != null ? Tools.decrypt(rs.getString("lab17c1")) : null);
                result.setLastResultDate(rs.getTimestamp("lab17c2"));
                User userL = new User();
                userL.setId(rs.getInt("lab04c1L"));
                userL.setName(rs.getString("lab04c2L"));
                userL.setLastName(rs.getString("lab04c3L"));
                userL.setUserName(rs.getString("lab04c4L"));

                String photo64 = "";
                byte[] photoBytes = rs.getBytes("lab04c16L");
                if (photoBytes != null)
                {
                    photo64 = Base64.getEncoder().encodeToString(photoBytes);
                }
                userL.setPhoto(photo64);

                // userL.setPhoto(Base64.getEncoder().encodeToString(rs.getBytes("lab04c16L")));
                result.setLastResultUser(userL);
                if (rs.getString("lab17c3") != null)
                {
                    result.setSecondLastResult(Tools.decrypt(rs.getString("lab17c3")));
                    result.setSecondLastResultDate(rs.getTimestamp("lab17c4"));
                    User userSL = new User();
                    userSL.setId(rs.getInt("lab04c1SL"));
                    userSL.setName(rs.getString("lab04c2SL"));
                    userSL.setLastName(rs.getString("lab04c3SL"));
                    userSL.setUserName(rs.getString("lab04c4SL"));
                    userSL.setPhoto(Base64.getEncoder().encodeToString(rs.getBytes("lab04c16SL")));
                    result.setSecondLastResultUser(userSL);
                }
                return result;
            }, patientId);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Elimina los examenes asociados a una orden
     *
     * @param order Orden
     * @param deleteTest Lista de los ids de los examenes que seran eliminados
     * @return Arreglo con los registros actualizados de cada orden
     * @throws Exception Error en base de datos
     */
    default int[] deleteTestToOrder(long order, List<Test> deleteTest) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        return getJdbcTemplate().batchUpdate(""
                + "DELETE   "
                + "FROM      " + lab57
                + " WHERE    lab22c1 = ? "
                + "         AND lab39c1 = ? ", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                ps.setLong(1, order);
                ps.setInt(2, deleteTest.get(i).getId());
            }

            @Override
            public int getBatchSize()
            {
                return deleteTest.size();
            }
        });
    }

    /**
     * Obtiene los examenes de solicitar resultado en el ingreso de una orden
     *
     * @param order Numero de orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en base de datos
     */
    default List<Test> getOrderTestByResultInOrderEntry(long order) throws Exception
    {
        try
        {
            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab39.lab39c1 "
                    + " , lab39.lab39c2 "
                    + " , lab39.lab39c3 "
                    + " , lab39.lab39c4 "
                    + " , lab39.lab39c37 "
                    + " , lab39.lab39c11 "
                    + " , lab39.lab39c12 "
                    + " , lab50.lab50c2 "
                    + "FROM     lab57 "
                    + "         INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + "         LEFT JOIN lab49 ON lab39.lab39c1 = lab49.lab39c1 "
                    + "         LEFT JOIN lab50 ON lab49.lab50c1 = lab50.lab50c1 "
                    + "WHERE    lab22c1 = ? "
                    + "         AND lab39c28 = 1"
                    + "", (ResultSet rs, int numRow)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setTestType(rs.getShort("lab39c37"));
                test.setResultType(rs.getInt("lab39c11"));
                test.setDecimals(rs.getString("lab39c12") == null ? null : rs.getInt("lab39c12"));
                if (rs.getString("lab50c2") != null)
                {
                    test.getLiteralResults().add(rs.getString("lab50c2"));
                }
                return test;
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene los examenes de una orden por resultados de el resultado de
     * ingreso
     *
     * @param order Numero de orden
     * @return Lista de pruebas
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en base de datos
     */
    default List<Test> getTestToResultRegister(long order) throws Exception
    {
        try
        {
            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab39.lab39c1 "
                    + " , lab39.lab39c2 "
                    + " , lab39.lab39c3 "
                    + " , lab39.lab39c4 "
                    + " , lab39.lab39c37 "
                    + " , lab39.lab39c11 "
                    + " , lab39.lab39c12 "
                    + "FROM     lab57 "
                    + "         INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + "WHERE    lab22c1 = ? "
                    + "         AND lab39c28 = 1"
                    + "         AND lab57c8 < 2"
                    + "", (ResultSet rs, int numRow)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setTestType(rs.getShort("lab39c37"));
                test.setResultType(rs.getInt("lab39c11"));
                test.setDecimals(rs.getString("lab39c12") == null ? 0 : rs.getInt("lab39c12"));
                test.setLiteralResultsLiteral(new ArrayList<>());
                return test;
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista de los nombre de examenes por orden y muestra
     *
     * @param order
     * @param sample
     * @return toma de muestra
     */
    default List<TestBasic> getTestByOrderSample(long order, int sample)
    {
        try
        {
            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab57.lab22c1, lab57.lab39c1, lab39.lab39c1,  lab39.lab39c2, "
                    + "lab39.lab39c3, lab39.lab39c4 , lab39.lab24c1, "
                    + "lab57c14 , t.lab39c4 as nameprofil, t.lab39c3 as abbrprofil "
                    + "FROM lab57 "
                    + "INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1  "
                    + "LEFT JOIN lab39 t ON lab57.lab57c14  = t.lab39c1  "
                    + "WHERE lab39.lab39c24 = 1 AND lab57.lab22c1 = ? AND lab39.lab24c1 = ? ",
                    (ResultSet rs, int i)
                    ->
            {
                TestBasic testBasic = new TestBasic();

                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setProfile(rs.getInt("lab57c14"));
                testBasic.setAbbrprofile(rs.getString("abbrprofil"));
                testBasic.setNameprofile(rs.getString("nameprofil"));

                return testBasic;
            }, order, sample);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista de los nombre de areas por orden y muestra
     *
     * @param order
     * @param sample
     * @return toma de muestra
     */
    default List<Area> getAreasByOrderSample(long order, int sample)
    {
        try
        {
            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab57.lab22c1, lab57.lab39c1, lab39.lab39c1, lab39.lab39c4 , lab39.lab24c1, lab39.lab43c1, lab43c4 "
                    + "FROM lab57 "
                    + "INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1  "
                    + "INNER JOIN lab43 ON lab39.lab43c1 = lab43.lab43c1  "
                    + "WHERE  lab57.lab22c1 = ? AND lab39.lab24c1 = ?",
                    (ResultSet rs, int i)
                    ->
            {
                Area area = new Area();
                area.setName(rs.getString("lab43c4"));

                return area;
            }, order, sample);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista las muestras pendiente por toma de muestra
     *
     * @param order
     * @return toma de muestra
     */
    default List<Sample> getSamplesToTake(long order)
    {

        try
        {
            int ordered = LISEnum.ResultSampleState.ORDERED.getValue();
            int pending = LISEnum.ResultSampleState.PENDING.getValue();

            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab57.lab22c1, lab57.lab39c1, lab57c16, lab57.lab24c1"
                    + ", lab24.lab24c1"
                    + ", lab24.lab24c2"
                    + ", lab24.lab24c3"
                    + ", lab24.lab24c4"
                    + ", lab24.lab24c5"
                    + ", lab24.lab24c6"
                    + ", lab24.lab24c7"
                    + ", lab24.lab24c8"
                    + ", lab24.lab04c1"
                    + ", lab24.lab07c1"
                    + ", lab24.lab56c1"
                    + ", lab24.lab24c9"
                    + ", lab24.lab24c10"
                    + ", lab24.lab24c11"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab56.lab56c2"
                    + ", lab24.lab24c12"
                    + ", lab24.lab24c13"
                    + ", lab24.lab24c14"
                    + " FROM lab57 "
                    + "INNER JOIN lab24 ON lab57.lab24c1 = lab24.lab24c1  "
                    + "INNER JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab24.lab04c1 "
                    + "WHERE  lab57.lab22c1 = ? AND lab57.lab57c16 IN (" + ordered + "," + pending + ")",
                    (ResultSet rs, int i)
                    ->
            {
                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));

                return sample;
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista de los perfiles de una orden
     *
     * @param order
     * @return toma de muestra
     */
    default List<Integer> getProfiles(long order)
    {

        try
        {

            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1, lab57.lab39c1, lab39.lab39c1 AS idProfile, lab39.lab39c37"
                    + " FROM lab57 "
                    + "INNER JOIN lab39 on lab57.lab39c1 = lab39.lab39c1 "
                    + "WHERE  lab57.lab22c1 = ? AND lab39.lab39c37 = 1 ",
                    (ResultSet rs, int i)
                    ->
            {

                return rs.getInt("idProfile");

            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Verifica si los hijos de un perfil estan marcados como tomados
     *
     * @param order
     * @param idProfile
     * @return toma de muestra
     */
    default List<TestBasic> isAllChildTaked(long order, int idProfile)
    {
        try
        {
            int ordered = LISEnum.ResultSampleState.ORDERED.getValue();
            int pending = LISEnum.ResultSampleState.PENDING.getValue();
            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab57.lab22c1, lab57.lab39c1, lab57.lab24c1, lab39.lab39c2, "
                    + "lab39.lab39c3, lab39.lab39c4, lab57.lab57c16 "
                    + "FROM lab57 "
                    + "INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 "
                    + "WHERE lab57.lab22c1 = ? AND lab57.lab57c14 = ? AND"
                    + " lab57.lab57c16 IN (" + ordered + "," + pending + ")",
                    (ResultSet rs, int i)
                    ->
            {
                TestBasic testBasic = new TestBasic();

                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));

                return testBasic;
            }, order, idProfile);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(null);
        }
    }

    /**
     * Marca el perfil como tomado
     *
     * @param resulTest
     * @param order
     * @param profile
     * @return
     *
     */
    default ResultTest checkProfileAsTaked(ResultTest resulTest, long order, int profile)
    {

        int ordered = LISEnum.ResultSampleState.COLLECTED.getValue();
        int isProfile = 1;
        getJdbcTemplate().update("UPDATE lab57 SET lab57c16 = " + ordered + " "
                + "WHERE lab57.lab22c1 = ? AND lab57.lab57c14 = ? and lab57c16 <  " + ordered + "",
                order, profile);

        return resulTest;

    }

    /**
     * Obtiene la lista las muestras pendiente por toma de muestra
     *
     * @param order
     * @param codeSample
     * @return toma de muestra
     */
    default List<Sample> isSampleTaken(long order, String codeSample)
    {

        try
        {
            int taked = LISEnum.ResultSampleState.COLLECTED.getValue();

            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab57.lab22c1, lab57.lab39c1, lab57c16, lab57.lab24c1"
                    + ", lab24.lab24c1"
                    + ", lab24.lab24c2"
                    + ", lab24.lab24c3"
                    + ", lab24.lab24c4"
                    + ", lab24.lab24c5"
                    + ", lab24.lab24c6"
                    + ", lab24.lab24c7"
                    + ", lab24.lab24c8"
                    + ", lab24.lab04c1"
                    + ", lab24.lab07c1"
                    + ", lab24.lab56c1"
                    + ", lab24.lab24c9"
                    + ", lab24.lab24c10"
                    + ", lab24.lab24c11"
                    + ", lab04.lab04c2"
                    + ", lab04.lab04c3"
                    + ", lab04.lab04c4"
                    + ", lab56.lab56c2"
                    + ", lab24.lab24c12"
                    + ", lab24.lab24c13"
                    + ", lab24.lab24c14"
                    + " FROM lab57 "
                    + "INNER JOIN lab24 ON lab57.lab24c1 = lab24.lab24c1  "
                    + "INNER JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab24.lab04c1 "
                    + "WHERE  lab57.lab22c1 = ? AND lab24.lab24c9 = ? AND lab57.lab57c16 =  " + taked + "",
                    (ResultSet rs, int i)
                    ->
            {
                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));

                return sample;
            }, order, codeSample);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la lista las test que van a ser actualizados como tomados
     *
     * @param order
     * @return toma de muestra
     */
    default List<ResultTest> testToUpdate(long order, int profile)
    {

        try
        {
            int ordered = LISEnum.ResultSampleState.COLLECTED.getValue();

            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1"
                    + ", lab39c1"
                    + ", lab24c1"
                    + ", lab57c16"
                    + ", lab57c14"
                    + " FROM lab57 "
                    + "WHERE  lab22c1 = ? AND lab57c14 = ? AND lab57c16  < " + ordered + "",
                    (ResultSet rs, int i)
                    ->
            {
                ResultTest resultTest = new ResultTest();
                resultTest.setOrder(rs.getLong("lab22c1"));
                resultTest.setTestId(rs.getInt("lab39c1"));
                resultTest.setSampleId(rs.getInt("lab24c1"));
                resultTest.setState(rs.getInt("lab57c16"));

                return resultTest;
            }, order, profile);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los id de las ordenes por rango de fechas una fecha de inicio y
     * otra de finalización
     *
     * @param startDate
     * @param endDate
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Long> rangeOrders(String startDate, String endDate) throws Exception;

    /**
     * Actualizamos el registro de resultados en el campo lab57c50 el cual
     * indica que ese examen de esa orden ya fue enviado a un sistema central
     *
     * @param idOrder
     * @param idTest
     * @param idCentralSystem
     * @return Lista de ordenes que aun no se han enviado al sistema central
     * @throws Exception Error base de datos
     */
    default int updateSentCentralSystem(long idOrder, String idTest, Integer idCentralSystem) throws Exception
    {
        if (Objects.isNull(idCentralSystem))
        {
            try
            {
                StringBuilder query = new StringBuilder();
                query.append("")
                        .append(" UPDATE lab57 SET lab57c50 = 1 , ")
                        .append(" lab57c51 = ? ")
                        .append("WHERE lab22c1 =  ?  and ")
                        .append("lab39c1 IN ")
                        .append("(SELECT lab39c1 FROM  lab39 WHERE lab39c2 = ? ) ");
                return getJdbcTemplate().update(query.toString(),
                        new Timestamp(System.currentTimeMillis()), idOrder,
                        idTest);
            } catch (Exception e)
            {
                return 0;
            }

        } else
        {
            try
            {
                return getJdbcTemplate().update("UPDATE lab57 SET lab57c50 = ? "
                        + "WHERE lab22c1 = ? "
                        + "AND lab39c1 = ?",
                        idCentralSystem,
                        idOrder,
                        Integer.valueOf(idTest));
            } catch (Exception e)
            {
                return 0;
            }

        }

    }

    /**
     * Inserta la orden en la tabla correspondiente al envio de ordenes al
     * sistema central externo
     *
     * @param orderShipped
     * @return Número de filas insertadas
     * @throws Exception Error en base de datos
     */
    default int insertOrdersToTheExternalCentralSystem(CentralSystemResults orderShipped) throws Exception
    {
        try
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab191");
            HashMap<String, Object> parameters = new HashMap<>();
            parameters = new HashMap<>(0);
            parameters.put("lab118c1", orderShipped.getCentralSystem()); //Id del sistema central
            parameters.put("lab39c1", orderShipped.getIdTest()); //Id Examen
            parameters.put("lab191c1", orderShipped.getCodeProfile()); //Codigo del perfil
            parameters.put("lab22c1", orderShipped.getIdOrder()); //Id Orden
            parameters.put("lab191c2", orderShipped.getIndicatore()); //Indicador
            parameters.put("lab191c3", orderShipped.getDateOfDispatch()); //Fecha Ingreso
            return insert.execute(parameters);
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * restorna el el perfil por ORDEN y por TEST sistema central externo
     *
     * @param orderShipped
     * @return Número de filas insertadas
     * @throws Exception Error en base de datos
     */
    default List<Object> getProfileForOrderAndTest(long idOrder, int idTest) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(
                    "SELECT lab57c14 AS profile FROM lab57 WHERE lab22c1 = ? AND lab39c1 = ? ",
                    (ResultSet rs, int i)
                    ->
            {
                int profile = rs.getInt("profile");

                return profile;
            }, idOrder, idTest);

        } catch (Exception e)
        {
            return new ArrayList<Object>();
        }

    }

    /**
     * Inserta la orden en la tabla correspondiente al envio de ordenes al
     * sistema central externo
     *
     * @param orderShipped
     * @return Número de filas insertadas
     * @throws Exception Error en base de datos
     */
    default int insertOrdersToTheExternalCentralSystem(RequestUpdateSendResult orderShipped, Timestamp updateDate) throws Exception
    {
        try
        {

            List<Object> profileForOrder = getProfileForOrderAndTest(orderShipped.getOrder(), orderShipped.getTestId());

            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab191");
            HashMap<String, Object> parameters = new HashMap<>();
            parameters = new HashMap<>(0);
            parameters.put("lab118c1", orderShipped.getCentralCode()); //Id del sistema central
            parameters.put("lab39c1", orderShipped.getTestId()); //Id Examen
            parameters.put("lab191c1", profileForOrder.get(0)); //Codigo del perfil
            parameters.put("lab22c1", orderShipped.getOrder()); //Id Orden
            parameters.put("lab191c2", orderShipped.getState()); //Indicador

            parameters.put("lab191c3", updateDate); //Fecha Ingreso
            return insert.execute(parameters);
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Actualiza la orden en envio de ordenes al sistema central externo
     *
     * @param idOrder
     * @param idTest
     * @return Número de filas insertadas
     * @throws Exception Error en base de datos
     */
    default int updateOrdersToTheExternalCentralSystem(long idOrder, int idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab191 = year.equals(currentYear) ? "lab191" : "lab191_" + year;

            return getJdbcTemplate().update("UPDATE " + lab191 + " SET lab191c2 = 0, lab191c3 = NULL "
                    + "WHERE lab22c1 = ? "
                    + "AND lab39c1 = ?",
                    idOrder,
                    idTest);
        } catch (Exception e)
        {
            return 0;
        }
    }
    
    /**
     * Actualiza la orden en envio de ordenes a tableros
     *
     * @param idOrder
     * @param idTest
     * @return Número de filas insertadas
     * @throws Exception Error en base de datos
     */
    default int updateOrdersToTheDashboard(long idOrder, int idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            return getJdbcTemplate().update("UPDATE " + lab57 + " SET lab57c72 = 0 "
                    + "WHERE lab22c1 = ? "
                    + "AND lab39c1 = ?",
                    idOrder,
                    idTest);
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Actualiza la orden en envio de ordenes al sistema central externo
     *
     * @param idOrder
     * @param idTest
     * @return Número de filas insertadas
     * @throws Exception Error en base de datos
     */
    default int OrdersToTheExternalCentralSystem(long idOrder, int idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab191 = year.equals(currentYear) ? "lab191" : "lab191_" + year;

            return getJdbcTemplate().update("UPDATE " + lab191 + " SET lab191c2 = 0, lab191c3 = NULL "
                    + "WHERE lab22c1 = ? "
                    + "AND lab39c1 = ?",
                    idOrder,
                    idTest);
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Obtien estado del lab50c50
     *
     * @param idOrder
     * @param idTest
     * @return stado de la orden
     * @throws Exception Error en base de datos
     */
    default List<Integer> statusExternalCentralSystem(long idOrder, int idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab191 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String sql = "SELECT lab57c50 AS status FROM lab57 WHERE lab22c1 = ? AND lab39c1 = ?";

            return getJdbcTemplate().query(sql,
                    (ResultSet rs, int i)
                    ->
            {
                int status = rs.getInt("status");

                return status;
            }, idOrder, idTest);

        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene una orden de envio de ordenes al sistema central externo
     *
     * @param idOrder
     * @param idTest
     * @param idCentralSystem
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default CentralSystemResults getOrdersToTheExternalCentralSystem(long idOrder, int idTest, int idCentralSystem) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab118c1, ")
                    .append("lab39c1, ")
                    .append("lab191c1, ")
                    .append("lab22c1, ")
                    .append("lab191c2, ")
                    .append("lab191c3 ")
                    .append("FROM lab191 ")
                    .append("WHERE lab118c1 = ").append(idCentralSystem)
                    .append(" AND lab39c1 = ").append(idTest)
                    .append(" AND lab22c1 = ").append(idOrder);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                CentralSystemResults results = new CentralSystemResults();
                results.setCentralSystem(rs.getInt("lab118c1"));
                results.setIdTest(rs.getInt("lab39c1"));
                results.setCodeProfile(rs.getString("lab191c1"));
                results.setIdOrder(rs.getLong("lab22c1"));
                results.setIndicatore(rs.getInt("lab191c2"));
                results.setDateOfDispatch(rs.getTimestamp("lab191c3"));
                return results;
            });
        } catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Agregar los resultados para el paciente
     *
     * @param idTest
     * @return Lista de ordenes que han sido enviado a un sistema central
     * externo
     * @throws Exception Error base de datos
     */
    default List<FindShippedOrders> findShippedOrdersCentralSystem(int idTest) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab118.lab118c2 AS ExternalSystemName, ")
                    .append("lab191.lab191c3 AS dateDispatched ")
                    .append("FROM lab191 ")
                    .append("INNER JOIN lab118 ON lab118.lab118c1 = lab191.lab118c1 ")
                    .append("WHERE lab39c1 = ").append(idTest);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                FindShippedOrders order = new FindShippedOrders();
                order.setNameCentralsystem(rs.getString("ExternalSystemName"));
                order.setDispatchedDate(rs.getTimestamp("dateDispatched").getTime());
                return order;
            });

        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Eliminar los resultados para el paciente
     *
     * @param patientResults
     * @return Funcion para insertar los resultados de los pacientes
     * @throws Exception Error base de datos
     */
    default int insertPatientResults(PatientResult patientResults) throws Exception
    {
        try
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("cont01");
            HashMap<String, Object> parameters = new HashMap<>();
            parameters = new HashMap<>(0);
            parameters.put("cont01c1", patientResults.getIdOrden());
            parameters.put("cont01c2", patientResults.getDocumentType());
            parameters.put("cont01c3", patientResults.getPatientRecord());
            parameters.put("cont01c4", patientResults.getFirstName());
            parameters.put("cont01c5", patientResults.getSecondName());
            parameters.put("cont01c6", patientResults.getFirstLastName());
            parameters.put("cont01c7", patientResults.getSecondLastName());
            parameters.put("cont01c8", patientResults.getIdTest());
            parameters.put("cont01c9", patientResults.getResults());
            return insert.execute(parameters);
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Listar de las ordenes enviadas a un sistema central externo
     *
     * @param idOrden
     * @param idTest
     * @return Eliminar resultados de paciente
     * @throws Exception Error base de datos
     */
    default int deletePatientResults(long idOrden, int idTest) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("DELETE FROM cont01 ")
                    .append(" WHERE ")
                    .append(" cont01c1 = ").append(idOrden)
                    .append(" AND cont01c8 = ").append(idTest);

            return getJdbcTemplate().update(query.toString());
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Obtiene los examenes de una orden que aun no tienen resultados
     *
     * @param idOrder
     * @return Lista de ids de los examenes sin resultados
     * @throws Exception Error presentado en base de datos
     */
    default List<Integer> getTestsWithoutResult(long idOrder) throws Exception
    {
        try
        {

            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab39c1 ")
                    .append("FROM  ").append(lab57).append(" as lab57 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab57c8 < ").append(LISEnum.ResultTestState.VALIDATED.getValue());

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab39c1");
            });
        } catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Actualiza los examenes que van para remision
     *
     * @param orders Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders}
     * @param laboratory Laboratorio
     * @throws Exception Error en base de datos
     */
    default int remission(RemissionLaboratory orders) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>(0);
        List<Object[]> parametersprofile = new ArrayList<>(0);

        String query = "";
        String queryprofile = "";

        if (orders.getType() == 1)
        {
            query = "UPDATE lab57 SET lab57c16 = 2, lab57c54 = 1 , lab40c1 = ?, lab40c1a = ? WHERE lab22c1 = ? AND lab39c1 = ?";
            queryprofile = "UPDATE lab57 SET lab57c16 = 2, lab57c54 = 1 , lab40c1 = ?, lab40c1a = ? WHERE lab22c1 = ? AND (lab39c1 = ? OR lab57c14  = ?)";
        } else
        {
            query = "UPDATE lab57 SET lab57c54 = 1, lab40c1 = ?, lab40c1a = ? WHERE lab22c1 = ? AND lab39c1 = ?";
            queryprofile = "UPDATE lab57 SET lab57c54 = 1 , lab40c1 = ?, lab40c1a = ? WHERE lab22c1 = ? AND (lab39c1 = ? OR lab57c14  = ?)";
        }

        orders.getOrders().forEach((order) ->
        {
            order.getTests().forEach((test) ->
            {
                if (test.getTestType() == 0)
                {
                    parameters.add(new Object[]
                    {
                        orders.getLaboratory(),
                        test.getLaboratory().getId(),
                        order.getOrderNumber(),
                        test.getId()
                    });
                } else
                {
                    parametersprofile.add(new Object[]
                    {
                        orders.getLaboratory(),
                        test.getLaboratory().getId(),
                        order.getOrderNumber(),
                        test.getId(),
                        test.getId()
                    });
                }

            });
        });

        if (parameters.size() > 0)
        {
            getJdbcTemplate().batchUpdate(query, parameters);
        }
        if (parametersprofile.size() > 0)
        {
            getJdbcTemplate().batchUpdate(queryprofile, parametersprofile);
        }
        return 1;
    }

}
