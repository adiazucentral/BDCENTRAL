package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.skl.ContainerSkl;
import net.cltech.enterprisent.domain.integration.skl.InterviewInformedConsent;
import net.cltech.enterprisent.domain.integration.skl.ListTestOrderSkl;
import net.cltech.enterprisent.domain.integration.skl.OrderConsent;
import net.cltech.enterprisent.domain.integration.skl.OrderConsentBase64;
import net.cltech.enterprisent.domain.integration.skl.OrderInformedConsent;
import net.cltech.enterprisent.domain.integration.skl.RequestSampleDestination;
import net.cltech.enterprisent.domain.integration.skl.SklAnswer;
import net.cltech.enterprisent.domain.integration.skl.SklOrderAnswer;
import net.cltech.enterprisent.domain.integration.skl.SklSampleDestination;
import net.cltech.enterprisent.domain.integration.skl.TestConsent;
import net.cltech.enterprisent.domain.integration.skl.TestConsentBase64;
import net.cltech.enterprisent.domain.integration.skl.patientTestPending;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los Recipientes.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/05/2020
 * @see Creación
 */
public interface IntegrationSklDao
{

    /**
     * Se obtienen todos los recipientes asignados a una orden
     *
     * @param id
     * @return Lista de recipientes
     * @throws Exception
     */
    default ContainerSkl getRecipientOrderByTestList(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT Cont.lab56c1 AS lab56c1, ")
                    .append("Cont.lab56c2 AS lab56c2, ")
                    .append("Cont.lab56c3 AS lab56c3, ")
                    .append("Sam.lab24c1  as lab24c1, ")
                    .append("Sam.lab24c2  as lab24c2, ")
                    .append("Sam.lab24c4  as lab24c4 ")
                    .append("FROM lab39 Tes ")
                    .append("JOIN lab24 Sam ON (Tes.lab24c1 = Sam.lab24c1) ")
                    .append("JOIN lab56 Cont ON (Sam.lab56c1 = Cont.lab56c1) ")
                    .append("WHERE Tes.lab39c1 = ").append(id);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                ContainerSkl cont = new ContainerSkl();
                cont.setId(rs.getInt("lab56c1"));
                cont.setName(rs.getString("lab56c2"));
                cont.setCount(rs.getInt("lab24c4"));
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("lab56c3");
                if (ImaBytes != null)
                {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                cont.setImageBase64(Imabas64);
                cont.setIdSample(rs.getInt("lab24c1"));
                cont.setSampleName(rs.getString("lab24c2"));
                return cont;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Lista los recipientes desde la base de datos integracion SKL
     *
     * @return Lista de recipientes skl
     * @throws Exception Error en base de datos
     */
    default List<ContainerSkl> listContainer() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab24.lab24c1"
                    + " ,lab24.lab24c2"
                    + " ,lab24.lab24c4"
                    + " ,lab56.lab56c1"
                    + " ,lab56.lab56c2"
                    + " ,lab56.lab56c3"
                    + " FROM lab24"
                    + " LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1",
                    (ResultSet rs, int i)
                    ->
            {
                ContainerSkl container = new ContainerSkl();

                container.setId(rs.getInt("lab56c1"));
                container.setName(rs.getString("lab56c2"));
                container.setCount(rs.getInt("lab24c4"));
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("lab56c3");
                if (ImaBytes != null)
                {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                container.setImageBase64(Imabas64);
                container.setIdSample(rs.getInt("lab24c1"));
                container.setSampleName(rs.getString("lab24c2"));

                return container;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Se obtienen todos los recipientes asignados a una orden
     *
     * @param idOrder
     * @param pendingToTake // 0 - no filtro por muestras pendientes por tomar | 1 - si filtro por muestras por tomar
     * @return Lista de recipientes
     * @throws Exception
     */
    default List<ContainerSkl> getRecipients(long idOrder, int pendingToTake) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT Cont.lab56c1 AS lab56c1, ")
                    .append("Cont.lab56c2 AS lab56c2, ")
                    .append("Cont.lab56c4 AS lab56c4, ")
                    .append("Cont.lab04c1 AS lab04c1, ")
                    .append("Cont.lab56c5 AS lab56c5, ")
                    .append("Cont.lab07c1 AS lab07c1, ")
                    .append("Cont.lab45c1 AS lab45c1, ")
                    .append("Sam.lab24c1 AS lab24c1, ")
                    .append("Sam.lab24c2 AS lab24c2, ")
                    .append("Sam.lab24c4 AS lab24c4 ")
                    .append("FROM lab57 O ")
                    .append("JOIN lab24 Sam ON (O.lab24c1 = Sam.lab24c1) ")
                    .append("JOIN lab56 Cont ON (Sam.lab56c1 = Cont.lab56c1) ");

            if (pendingToTake == 0)
            {
                query.append("WHERE O.lab22c1 = ").append(idOrder);
            }
            if (pendingToTake == 1)
            {
                query.append("WHERE O.lab22c1 = ").append(idOrder).append(" AND (O.lab57c16 = -1 OR O.lab57c16 = 2) ");
            }
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                ContainerSkl cont = new ContainerSkl();
                cont.setId(rs.getInt("lab56c1"));
                cont.setName(rs.getString("lab56c2"));
                cont.setIdSample(rs.getInt("lab24c1"));
                cont.setSampleName(rs.getString("lab24c2"));
                cont.setCount(rs.getInt("lab24c4"));
                return cont;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene informacion basica del paciente por medio de la orden
     *
     * @param idOrder
     * @return idPaciente + Nombres del paciente
     * @throws Exception Error presentado en el servicio
     */
    default String getPatientBasicInfo(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Pat.lab21c1 AS lab21c1, ")
                    .append("Pat.lab21c3 AS lab21c3, ")
                    .append("Pat.lab21c4 AS lab21c4, ")
                    .append("Pat.lab21c5 AS lab21c5, ")
                    .append("Pat.lab21c6 AS lab21c6 ")
                    .append("FROM lab22 O ")
                    .append("JOIN lab21 Pat ON (O.lab21c1 = Pat.lab21c1) ")
                    .append("WHERE O.lab22c1 = ").append(idOrder);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                try
                {
                    String nameOne = "";
                    String nameTwo = "";
                    String lastNameOne = "";
                    String lastNameTwo = "";
                    String idPat = "";
                    String finalPatient = "";

                    idPat = String.valueOf(rs.getInt("lab21c1"));
                    nameOne = Tools.decrypt(rs.getString("lab21c3"));
                    nameTwo = Tools.decrypt(rs.getString("lab21c4"));
                    lastNameOne = Tools.decrypt(rs.getString("lab21c5"));
                    lastNameTwo = Tools.decrypt(rs.getString("lab21c6"));

                    if (nameTwo == null)
                    {
                        finalPatient = idPat + ", " + nameOne + " ";
                    }
                    else
                    {
                        finalPatient = idPat + ", " + nameOne + " " + nameTwo + " ";
                    }
                    if (lastNameTwo == null)
                    {
                        finalPatient += lastNameOne;
                    }
                    else
                    {
                        finalPatient += lastNameOne + " " + lastNameTwo;
                    }

                    return finalPatient;
                }
                catch (SQLException ex)
                {
                    return "";
                }
            });
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Obtiene el nombre del tipo de una orden con el id de esa orden
     *
     * @param idOrder
     * @return Nombre del tipo de orden
     * @throws Exception Error en la base de datos.
     */
    default String getOrderType(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT OT.lab103c3 AS lab103c3 ")
                    .append("FROM lab22 O ")
                    .append("JOIN lab103 OT ON (O.lab103c1 = OT.lab103c1) ")
                    .append("WHERE O.lab22c1 = ").append(idOrder);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getString("lab103c3");
            });
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /**
     * * Lista la abreviatura de los examenes.
     *
     * @param codeTest
     * @param resultListTest
     * @return toma de muestra
     */
    default String getTestByTestsIdSample(String codeTest, ListTestOrderSkl resultListTest)
    {
        try
        {

            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab39c3 "
                    + "FROM lab39 "
                    + "WHERE lab39.lab39c2 = ? AND lab39.lab24c1 = ? ",
                    (ResultSet rs, int i)
                    ->
            {

                return rs.getString("lab39c3");

            }, codeTest, resultListTest.getIdSample());
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Consulta la ruta del siguiente destino y retorna el id del mismo
     *
     * @param verifyDestination
     * @return Retorna id de la siguiente ruta de destino
     *
     * @throws Exception Error en la base de datos.
     */
    default int nextDestination(VerifyDestination verifyDestination) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Dest.lab53c1 AS lab53c1 ")
                    .append("FROM lab159 S ")
                    .append("JOIN lab24 Samp ON (S.lab24c1 = Samp.lab24c1) ")
                    .append("JOIN lab52 Assign ON (Samp.lab24c1 = Assign.lab24c1) ")
                    .append("JOIN lab42 Route ON (Assign.lab52c1 = Route.lab52c1) ")
                    .append("JOIN lab53 Dest ON (Route.lab53c1 = Dest.lab53c1) ")
                    .append("WHERE Samp.lab24c1 = ").append(verifyDestination.getIdSample())
                    .append(" AND Dest.lab53c1 = ").append(verifyDestination.getAssigmentDestination())
                    .append(" AND Assign.lab05c1 = ").append(verifyDestination.getBranch())
                    .append(" AND S.lab22c1 =  ").append(verifyDestination.getOrder());

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab53c1");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Lista los usuarios desde la base de datos.
     *
     * @param idPatient
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    public String listCommentPatient(int idPatient) throws Exception;

    /**
     * Obtiene los comentarios de una orden o el diagnostico permanente de un paciente.
     *
     * @param idOrder Numero de la Orden.
     * @return Lista de comentarios.
     * @throws java.lang.Exception
     */
    public String listCommentOrder(Long idOrder) throws Exception;

    /**
     * Obtiene todas las respuestas asociadas a una pregunta cerrada
     *
     * @param idQuestion
     * @return Lista de respuestas por pregunta
     * @throws java.lang.Exception Error en la base de datos.
     */
    default List<SklAnswer> listAnswersByIdQuestion(int idQuestion) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Q.lab70c1 AS idQuestion, ")
                    .append("Ans.lab90c1 AS idAnswer, ")
                    .append("Ans.lab90c2 AS nameAnswer ")
                    .append("FROM lab70 Q  ")
                    .append("JOIN lab91 QA ON (Q.lab70c1 = QA.lab70c1) ")
                    .append("JOIN lab90 Ans ON (Ans.lab90c1 = QA.lab90c1)  ")
                    .append("WHERE Q.lab70c1 = ").append(idQuestion);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                SklAnswer answer = new SklAnswer();
                answer.setIdQuestion(rs.getInt("idQuestion"));
                answer.setId(rs.getInt("idAnswer"));
                answer.setAnswer(rs.getString("nameAnswer"));
                return answer;
            });
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Obtiene todas las respuestas asociadas a una orden
     *
     * @param idOrder
     * @return Lista de respuestas por orden
     * @throws java.lang.Exception Error en la base de datos.
     */
    default List<SklAnswer> listAnswersByIdOrder(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT Q.lab70c1 AS idQuestion, ")
                    .append("Ans.lab90c1 AS idAnswer, ")
                    .append("Ans.lab90c2 AS nameAnswer, ")
                    .append("E.lab23c1 AS openAnswer ")
                    .append("FROM lab23 E ")
                    .append("JOIN lab70 Q ON (E.lab70c1 = Q.lab70c1) ")
                    .append("LEFT JOIN lab91 QA ON (Q.lab70c1 = QA.lab70c1) ")
                    .append("LEFT JOIN lab90 Ans ON (Ans.lab90c1 = QA.lab90c1) ")
                    .append("WHERE E.lab22c1 = ").append(idOrder);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                SklAnswer answer = new SklAnswer();
                answer.setIdQuestion(rs.getInt("idQuestion"));
                answer.setId(rs.getInt("idAnswer"));
                answer.setAnswer(rs.getString("nameAnswer") == null ? rs.getString("openAnswer") : rs.getString("nameAnswer"));
                return answer;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene el id de la pregunta asignada a una orden
     *
     * @param idOrder
     * @param idQuestion
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    default Integer getOrderQuestionId(long idOrder, int idQuestion) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab70c1 ")
                    .append("FROM lab23 ")
                    .append("WHERE lab70c1 = ").append(idQuestion).append(" AND lab22c1 = ").append(idOrder);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("lab70c1");
            });

        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Consulta la ruta de destinos de una muestra
     *
     * @param requestSampleDestination
     * @return Lista de destinos de una muestra
     *
     * @throws Exception Error en la base de datos.
     */
    default List<SklSampleDestination> getSampleDestinations(RequestSampleDestination requestSampleDestination) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Dest.lab53c1 AS lab53c1, ")
                    .append("Dest.lab53c2 AS lab53c2, ")
                    .append("Dest.lab53c3 AS lab53c3, ")
                    .append("Dest.lab53c4 AS lab53c4, ")
                    .append("Dest.lab53c5 AS lab53c5, ")
                    .append("Route.lab42c1 AS lab42c1, ")
                    .append("Samp.lab24c1 AS lab24c1 ")
                    .append("FROM lab159 S ")
                    .append("JOIN lab24 Samp ON (S.lab24c1 = Samp.lab24c1) ")
                    .append("JOIN lab52 Assign ON (Samp.lab24c1 = Assign.lab24c1) ")
                    .append("JOIN lab42 Route ON (Assign.lab52c1 = Route.lab52c1) ")
                    .append("JOIN lab53 Dest ON (Route.lab53c1 = Dest.lab53c1) ")
                    .append("JOIN lab22 O ON (O.lab22c1 = S.lab22c1) ")
                    .append("JOIN lab103 OT ON (O.lab103c1 = OT.lab103c1) ")
                    .append("WHERE Dest.lab07c1 = ").append(requestSampleDestination.getState())
                    .append(" AND Samp.lab24c1 = ").append(requestSampleDestination.getIdSample())
                    .append(" AND Assign.lab05c1 = ").append(requestSampleDestination.getIdBranch())
                    .append(" AND S.lab22c1 =  ").append(requestSampleDestination.getIdOrder())
                    .append(" AND OT.lab103c2 = ?");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                SklSampleDestination destination = new SklSampleDestination();
                destination.setIdDestination(rs.getInt("lab53c1"));
                destination.setDestinationCode(rs.getString("lab53c2"));
                destination.setName(rs.getString("lab53c3"));
                destination.setDestinationType(rs.getInt("lab53c5"));
                destination.setIdRoute(rs.getInt("lab42c1"));
                destination.setSampleId(rs.getInt("lab24c1"));
                return destination;
            }, requestSampleDestination.getOrderType());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Registra una nueva respuesta en la base de datos.
     *
     * @param answer Instancia con los datos de la respuesta.
     * @param typeQuestion
     *
     * @return Instancia con las datos de la respuesta.
     * @throws Exception Error en la base de datos.
     */
    default SklOrderAnswer createAnswer(SklOrderAnswer answer, int typeQuestion) throws Exception
    {

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab23");

        Timestamp timestamp = new Timestamp(new Date().getTime());
        HashMap parameters = new HashMap();
        parameters.put("lab22c1", answer.getOrder());
        parameters.put("lab04c1", answer.getIdPatient());
        parameters.put("lab70c1", answer.getIdQuestion());
        parameters.put("lab90c1", answer.getIdSelectAnswer());
        parameters.put("lab23c1", answer.getTextAnswer());
        parameters.put("lab23c2", timestamp);
        parameters.put("lab70c5", typeQuestion);

        insert.execute(parameters);
        return answer;
    }

    /**
     * Obtiene el tipode respuesta de una pregunta
     *
     * @param answer
     * @return Id del usuario analizador
     * @throws Exception Error en el servicio
     */
    default Integer typeQuestion(SklOrderAnswer answer) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab70c5 ")
                    .append("FROM lab70 ")
                    .append("WHERE lab70c1 = ").append(answer.getIdQuestion());

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getObject("lab70c5") == null ? 0 : rs.getInt("lab70c5");
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Actualiza la respuesta de una pregunta a una orden
     *
     * @param answer
     * @param user
     * @return + 1 - Actualizo registros, 0 - no encontro coincidencias, - 1 - Error al actualizar
     * @throws Exception Error en la base de datos.
     */
    default int updateAnswer(SklOrderAnswer answer, AuthorizedUser user) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            if (answer.getTextAnswer().isEmpty())
            {
                return getJdbcTemplate().update("UPDATE lab23 SET lab90c1 = ?, "
                        + "lab23c1 = null, "
                        + "lab23c2 = ?, "
                        + "lab04c1 = ? "
                        + "WHERE lab22c1 = ? "
                        + "AND lab70c1 = ?",
                        answer.getIdSelectAnswer(),
                        timestamp,
                        user.getId(),
                        answer.getOrder(),
                        answer.getIdQuestion());
            }
            else
            {
                return getJdbcTemplate().update("UPDATE lab23 SET lab90c1 = null, "
                        + "lab23c1 = ?, "
                        + "lab23c2 = ?, "
                        + "lab04c1 = ? "
                        + "WHERE lab22c1 = ?"
                        + " AND lab70c1 = ?",
                        answer.getTextAnswer(),
                        timestamp,
                        user.getId(),
                        answer.getOrder(),
                        answer.getIdQuestion());
            }
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Obtiene la fecha de verificacion de una muestra de una orden
     *
     * @param idAssignRout
     * @param idOrder
     * @return La fecha de verificacion de esa muestra en el destino asignado
     * @throws Exception Error en la base de datos.
     */
    default Date checkSample(int idAssignRout, long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab25c1 ")
                    .append("FROM lab25 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab42c1 = ").append(idAssignRout);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getDate("lab25c1");
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene las ordenes registradas con la fecha enviada
     *
     * @param date
     * @return Lista de ordenes
     * @throws Exception Error en la base de datos.
     */
    default List<Long> getOrdersToTake(int date) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab22c1 ")
                    .append("FROM lab22 ")
                    .append("WHERE lab22c2 = ").append(date)
                    .append(" AND (lab22c19 = 0 or lab22c19 is null)  ");
                    
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getLong("lab22c1");
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el estado de la muestra segun lab57
     *
     * @param idSample
     * @param idOrder
     * @return el estado de la muestra
     * @throws Exception Error en la base de datos.
     */
    default int getSampleStatus(int idSample, long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57c16 ")
                    .append("FROM lab57  ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab24c1 = ").append(idSample);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab57c16");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Obtiene las preguntas de la entrevista configurada para el consentimiento informado
     *
     * @return Lista de InterviewInformedConsent
     * @throws Exception Error en la base de datos.
     */
    default List<InterviewInformedConsent> listInterviewInformedConsent() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab92.lab70c1, lab70.lab70c3 ")
                    .append("FROM lab92 INNER JOIN lab44 ON lab92.lab44c1 = lab44.lab44c1 ")
                    .append("LEFT JOIN lab70 ON lab92.lab70c1 = lab70.lab70c1 ")
                    .append("WHERE lab44.lab44c6 = 1 ");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                InterviewInformedConsent interviewInformedConsent = new InterviewInformedConsent();

                interviewInformedConsent.setId(rs.getInt("lab70c1"));
                interviewInformedConsent.setQuestion(rs.getString("lab70c3"));

                return interviewInformedConsent;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene los examenes con consentimiento informado de una orden
     *
     * @param order
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default OrderConsent getOrderConsent(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab57.lab22c1 as orderLab57, lab22.lab22c1, lab22.lab21c1 as idHistory, lab21.lab21c1 as idPatient,  lab21.lab21c2 as nameHistory, ")
                    .append("lab21.lab21c3 as firstName, lab21.lab21c4 as secondName, lab21.lab21c5 as firstSubName, lab21.lab21c6 as secondSubName ")
                    .append("FROM lab57 ")
                    .append("INNER JOIN lab22 ON lab57.lab22c1 = lab22.lab22c1 ")
                    .append("INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ")
                    .append("  WHERE lab57.lab22c1 =").append(order);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {

                OrderConsent bean = new OrderConsent();
                bean.setOder(rs.getLong("orderLab57"));
                bean.setIdPatient(rs.getInt("idHistory"));
                bean.setHistory(Tools.decrypt(rs.getString("nameHistory")));
                bean.setNames(Tools.decrypt(rs.getString("firstName")) + " " + Tools.decrypt(rs.getString("secondName")));
                bean.setSubnames(Tools.decrypt(rs.getString("firstSubName")) + " " + Tools.decrypt(rs.getString("secondSubName")));

                return bean;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene la pruebas id de una orden que esta en consentimiento informado en uno
     *
     * @param order
     * @return Lista de InterviewInformedConsent
     * @throws Exception Error en la base de datos.
     */
    default List<TestConsent> listTestInformedConsent(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57.lab39c1 as idTestResult, lab39.lab39c1 as idTest, lab39c2, "
                    + "lab39c3, lab39c4, lab39.lab43c1 idAreaTest, lab43.lab43c1 idArea, lab43c4 as nameArea, lab39c52 ")
                    .append("FROM lab57 ")
                    .append("INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 ")
                    .append("INNER JOIN lab43 ON lab39.lab43c1 = lab43.lab43c1 ")
                    .append("WHERE lab57.lab22c1 =").append(order)
                    .append(" AND lab39c52 = 1");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                TestConsent testConsent = new TestConsent();

                testConsent.setId(rs.getInt("idTest"));
                testConsent.setCode(rs.getString("lab39c2"));
                testConsent.setAbbr(rs.getString("lab39c3"));
                testConsent.setName(rs.getString("lab39c4"));
                testConsent.setSection(rs.getString("nameArea"));

                return testConsent;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Registra una nueva registro de consentimiento.
     *
     * @param consent
     * @param userId
     * @return
     * @throws Exception Error en la base de datos.
     */
    default OrderInformedConsent createConsent(OrderInformedConsent consent, int userId) throws Exception
    {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab188")
                .usingColumns("lab22c1", "lab39c1", "lab188c1", "lab04c1", "lab188c2");

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", consent.getAnswer().getOrder());
        parameters.put("lab39c1", consent.getAnswer().getIdTest());
        if (consent.getDocument() != null)
        {
            byte[] documentByte = DatatypeConverter.parseBase64Binary(consent.getDocument());
            parameters.put("lab188c1", documentByte);
        }
        else
        {
            parameters.put("lab188c1", null);
        }
        parameters.put("lab04c1", userId);
        parameters.put("lab188c2", timestamp);

        insert.execute(parameters);

        return consent;
    }

    /**
     * Obtendra un objeto con consentimiento informado en uno
     *
     * @param order
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default OrderConsentBase64 getAllConsentInformed(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab22.lab22c1 as idOrder, lab22.lab21c1 as idHistory, lab21.lab21c1 as idPatient,  lab21.lab21c2 as nameHistory, ")
                    .append("lab21.lab21c3 as firstName, lab21.lab21c4 as secondName, lab21.lab21c5 as firstSubName, lab21.lab21c6 as secondSubName, lab21.lab21c14 ")
                    .append("FROM lab22 ")
                    .append("INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ")
                    .append("WHERE lab22.lab22c1 =").append(order);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {

                OrderConsentBase64 orderConsentBase64 = new OrderConsentBase64();
                orderConsentBase64.setOder(rs.getLong("idOrder"));
                orderConsentBase64.setIdPatient(rs.getInt("idHistory"));
                orderConsentBase64.setHistory(Tools.decrypt(rs.getString("nameHistory")));
                orderConsentBase64.setNames(Tools.decrypt(rs.getString("firstName")) + " " + Tools.decrypt(rs.getString("secondName")));
                orderConsentBase64.setSubnames(Tools.decrypt(rs.getString("firstSubName")) + " " + Tools.decrypt(rs.getString("secondSubName")));
                orderConsentBase64.setPhoto(rs.getString("lab21c14"));

                return orderConsentBase64;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene la pruebas id de una orden que esta en consentimiento informado en uno
     *
     * @param order
     * @return Lista de InterviewInformedConsent
     * @throws Exception Error en la base de datos.
     */
    default List<TestConsentBase64> listTestConsentbase64(long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab188.lab39c1 as idTestlab188, lab188.lab188c1 as document, lab188.lab04c1, "
                    + "lab04.lab04c1 as idUser, lab04.lab04c4, lab188.lab188c2 as dateRegistration, "
                    + "lab39.lab39c1 as idTest, lab39c2, lab39c3, lab39c4 ")
                    .append("FROM lab188 ")
                    .append("INNER JOIN lab39 ON lab188.lab39c1 = lab39.lab39c1 ")
                    .append("INNER JOIN lab04 ON lab188.lab04c1 = lab04.lab04c1 ")
                    .append("WHERE lab188.lab22c1 =").append(order);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                TestConsentBase64 testConsentBase64 = new TestConsentBase64();

                testConsentBase64.setId(rs.getInt("idTest"));
                testConsentBase64.setCode(rs.getString("lab39c2"));
                testConsentBase64.setAbbr(rs.getString("lab39c3"));
                testConsentBase64.setName(rs.getString("lab39c4"));
                testConsentBase64.setUserId(rs.getInt("idUser"));
                testConsentBase64.setUserName(rs.getString("lab04c4"));
                testConsentBase64.setRegistrationDate(rs.getTimestamp("dateRegistration"));
                String document64 = "";
                byte[] documentBytes = rs.getBytes("document");
                if (documentBytes != null)
                {
                    document64 = Base64.getEncoder().encodeToString(documentBytes);
                }
                testConsentBase64.setDocument(document64);

                return testConsentBase64;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene lista de ordenes que esta en consentimiento informado con toda la informacion
     *
     * @param history
     * @param documenttype
     * @return Lista de TestConsentBase64
     * @throws Exception Error en la base de datos.
     */
    default List<OrderConsentBase64> litsConsentOrders(String history, int documenttype) throws Exception
    {
        try
        {

            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab21.lab21c1 as idPatient, lab22.lab21c1 as idHistory, lab21.lab21c2 as nameHistory, "
                    + "lab21.lab54c1 as idTypeDocument, lab22.lab22c1 as order, "
                    + "lab21.lab21c3 as firstName, lab21.lab21c4 as secondName, "
                    + "lab21.lab21c5 as firstSubName, lab21.lab21c6 as secondSubName, lab21.lab21c14  ")
                    .append("FROM lab21 ")
                    .append("INNER JOIN lab22 ON lab21.lab21c1 =  lab22.lab21c1 ")
                    .append("INNER JOIN lab188 ON lab22.lab22c1 = lab188.lab22c1 ")
                    .append("WHERE lab21.lab21c2 = '").append(Tools.encrypt(history))
                    .append("' AND lab21.lab54c1 = ").append(documenttype);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                OrderConsentBase64 orderConsentBase64 = new OrderConsentBase64();

                orderConsentBase64.setOder(rs.getLong("order"));
                orderConsentBase64.setIdPatient(rs.getInt("idHistory"));
                orderConsentBase64.setHistory(Tools.decrypt(rs.getString("nameHistory")));
                orderConsentBase64.setHistory(Tools.decrypt(rs.getString("nameHistory")));
                orderConsentBase64.setNames(Tools.decrypt(rs.getString("firstName")) + " " + Tools.decrypt(rs.getString("secondName")));
                orderConsentBase64.setSubnames(Tools.decrypt(rs.getString("firstSubName")) + " " + Tools.decrypt(rs.getString("secondSubName")));
                orderConsentBase64.setPhoto(rs.getString("lab21c14"));

                return orderConsentBase64;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene lista de examenes
     *
     * @param order
     * @param idSample
     * @return Lista de TestConsent
     * @throws Exception Error en la base de datos.
     */
    default List<TestConsent> getListTestsSample(long order, int idSample) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57.lab39c1 as idTestResult, "
                    + "lab39.lab39c1 as idTest, "
                    + "lab39.lab39c2, "
                    + "lab39.lab39c3, "
                    + "lab39.lab39c4, "
                    + "lab39.lab43c1 idAreaTest, "
                    + "lab43.lab43c1 idArea, "
                    + "lab43c4 as nameArea, "
                    + "lab39.lab39c52, "
                    + "lab39.lab39c37,"
                    + "lab57c14, "
                    + "lab57c16 AS sampleState, "
                    + "p.lab39c38 as testconsent ")
                    .append("FROM lab57 ")
                    .append("INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 ")
                    .append("INNER JOIN lab43 ON lab39.lab43c1 = lab43.lab43c1 ")
                    .append("LEFT JOIN lab39 AS p ON p.lab39c1 = lab57.lab57c14 ")
                    .append("WHERE lab57.lab22c1 =").append(order)
                    .append(" AND lab57.lab24c1 =").append(idSample);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                TestConsent testConsent = new TestConsent();
                testConsent.setId(rs.getInt("idTest"));
                testConsent.setCode(rs.getString("lab39c2"));
                testConsent.setAbbr(rs.getString("lab39c3"));
                testConsent.setName(rs.getString("lab39c4"));
                testConsent.setSection(rs.getString("nameArea"));
                testConsent.setProfile(rs.getInt("lab57c14"));
                testConsent.setType(rs.getInt("lab39c37"));
                testConsent.setDependentTest(rs.getInt("testconsent"));
                testConsent.getResult().setSampleState(rs.getInt("sampleState"));
                return testConsent;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
    
    
     /**
     * Obtiene lista de examenes pendientes del paciente 
     *
     * @param document
     * @return pendingExams
     * @throws Exception Error en la base de datos.
     */
    default List<patientTestPending> getPatientTestPending(String document)throws Exception {
        
        try {
            StringBuilder query = new StringBuilder();
            query.append("select distinct l21.lab21c1 as idPaciente, "
                        + "l21.lab21c2 as NoDocumento, "
                        + "l21.lab21c3 as nombre1, "
                        + "l21.lab21c4 as nombre2, "
                        + "l21.lab21c5 as apellido1, "
                        + "l21.lab21c6 as apellido2, "
                        + "l54.lab54c2 as codTipoDoc, "
                        + "l54.lab54c3 as nomTipoDoc, "
                        + "l22.lab22c1 as noOrden ")
                        .append("from lab22 as l22 ")
                        .append("inner join lab21 AS l21 on l22.lab21c1 = l21.lab21c1 ")
                        .append("inner join lab54 as l54 on l21.lab54c1 = l54.lab54c1 ")
                        .append("inner join lab57 as l57 on l57.lab22c1 = l22.lab22c1 ")
                        .append("where  l21.lab21c2 = '").append(Tools.encrypt(document)).append("' ")
                        .append("and l57.lab57c16 in (-1,2) ")
                        .append("order by l22.lab22c1 desc");
            
           return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                patientTestPending penExa = new patientTestPending();
                penExa.setIdPatient(rs.getInt("idPaciente"));
                penExa.setIdentification(Tools.decrypt(rs.getString("NoDocumento")));
                penExa.setName1(Tools.decrypt(rs.getString("nombre1")));
                penExa.setName2(Tools.decrypt(rs.getString("nombre2")));
                penExa.setLastName1(Tools.decrypt(rs.getString("apellido1")));
                penExa.setLastName2(Tools.decrypt(rs.getString("apellido2")));
                penExa.setCodTypeDoc(rs.getString("codTipoDoc"));
                penExa.setNameTypeDoc(rs.getString("nomTipoDoc"));
                penExa.setNumOrder(rs.getString("noOrden"));
                try {
                    penExa.setPendExam(testPendingOrder(rs.getLong("noOrden")));
                } catch (Exception ex) {
                    Logger.getLogger(IntegrationSklDao.class.getName()).log(Level.SEVERE, null, ex);
                }
                return penExa;
            });
            
        } catch (Exception e) {
            return new ArrayList<>();
        }
        
    }
    
    default boolean testPendingOrder(Long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT 1 AS EXIST ")
                 .append("FROM lab57 ")
                 .append("WHERE lab57c16 = -1 AND LAB22C1 = ").append(order);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("EXIST") == 1;
            });
        } catch (Exception e)
        {
            return false;
        }
    }
    
    default boolean testPendingSample(Long order, int sample) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT 1 AS EXIST ")
                 .append("FROM lab57 ")
                 .append("WHERE (lab57c16 = -1 OR lab57c16 = 2) AND ( LAB22C1 = ").append(order).append(" AND LAB24C1 = ").append(sample).append(")");

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("EXIST") == 1;
            });
        } catch (DataAccessException e)
        {
            return false;
        }
    }
}
