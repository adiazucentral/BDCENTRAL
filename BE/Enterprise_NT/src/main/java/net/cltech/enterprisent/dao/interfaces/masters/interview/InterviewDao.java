package net.cltech.enterprisent.dao.interfaces.masters.interview;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.interview.TypeInterview;
import static net.cltech.enterprisent.tools.Constants.DESTINATION;
import static net.cltech.enterprisent.tools.Constants.LABORATORY;
import static net.cltech.enterprisent.tools.Constants.ORDER;
import static net.cltech.enterprisent.tools.Constants.TEST;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * entrevistas.
 *
 * @version 1.0.0
 * @author enavas
 * @since 17/08/2017
 * @see Creación
 */
public interface InterviewDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista las entrevistas desde la base de datos.
     *
     * @return Lista de entrevista.
     * @throws Exception Error en la base de datos.
     */
    default List<Interview> list() throws Exception
    {
        try
        {
            String query = "SELECT"
                    + " lab44c1"
                    + " , lab44c2"
                    + " , lab44c3"
                    + " , lab44c4"
                    + " , lab44.lab07c1"
                    + " , lab44.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab44c5"
                    + " , lab44c6"
                    + " , lab44c7"
                    + " FROM lab44"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab44.lab04c1 ";
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                Interview interview = new Interview();

                interview.setId(rs.getInt("lab44c1"));
                interview.setName(rs.getString("lab44c2"));
                interview.setType(rs.getShort("lab44c3"));
                interview.setPanic(rs.getInt("lab44c4") == 1);
                interview.setState(rs.getInt("lab07c1") == 1);
                interview.setLastTransaction(rs.getTimestamp("lab44c5"));
                interview.setInformedConsent(rs.getBoolean("lab44c6"));
                interview.setOrdering(rs.getShort("lab44c7"));
                /*Usuario*/
                interview.getUser().setId(rs.getInt("lab04c1"));
                interview.getUser().setName(rs.getString("lab04c2"));
                interview.getUser().setLastName(rs.getString("lab04c3"));
                interview.getUser().setUserName(rs.getString("lab04c4"));

                return interview;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener información de una entrevista por un campo especifico.
     *
     * @param id ID de la entrevista a ser consultada.
     * @param name Nombre de la entrevista a ser consultada.
     *
     * @return Instancia con las datos de la entrevista.
     * @throws Exception Error en la base de datos.
     */
    default Interview get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT"
                    + " lab44c1"
                    + " , lab44c2"
                    + " , lab44c3"
                    + " , lab44c4"
                    + " , lab44.lab07c1"
                    + " , lab44.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab44c5"
                    + " , lab44c6"
                    + " , lab44c7"
                    + " FROM lab44"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab44.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab44c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab44c2) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Interview interview = new Interview();

                interview.setId(rs.getInt("lab44c1"));
                interview.setName(rs.getString("lab44c2"));
                interview.setType(rs.getShort("lab44c3"));
                interview.setPanic(rs.getInt("lab44c4") == 1);
                interview.setState(rs.getInt("lab07c1") == 1);
                interview.setLastTransaction(rs.getTimestamp("lab44c5"));
                interview.setInformedConsent(rs.getBoolean("lab44c6"));
                interview.setOrdering(rs.getShort("lab44c7"));
                /*Usuario*/
                interview.getUser().setId(rs.getInt("lab04c1"));
                interview.getUser().setName(rs.getString("lab04c2"));
                interview.getUser().setLastName(rs.getString("lab04c3"));
                interview.getUser().setUserName(rs.getString("lab04c4"));

                return interview;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista las preguntas Asociadas a la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    default List<Question> listQuestion(Integer idInterview) throws Exception
    {
        try
        {
            String query = "SELECT "
                    + " lab70.lab70c1"
                    + " , lab70c2"
                    + " , lab70c3"
                    + " , lab70c4"
                    + " , lab70c5"
                    + " , lab70c6"
                    + " , lab92.lab92c2"
                    + " , lab70.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab70.lab07c1"
                    + " , lab92.lab92c1"
                    + " FROM lab70 "
                    + " LEFT JOIN lab92 ON lab92.lab70c1=lab70.lab70c1 AND lab92.lab44c1=" + idInterview
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab70.lab04c1 "
                    + " WHERE lab92.lab44c1 = " + idInterview;
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                Question question = new Question();
                question.setId(rs.getInt("lab70c1"));
                question.setName(rs.getString("lab70c2"));
                question.setQuestion(rs.getString("lab70c3"));
                question.setOpen(rs.getInt("lab70c4") == 1);
                question.setControl(rs.getShort("lab70c5"));
                question.setRequired(rs.getInt("lab92c2") == 1);
                /*Usuario*/
                question.getUser().setId(rs.getInt("lab04c1"));
                question.getUser().setName(rs.getString("lab04c2"));
                question.getUser().setLastName(rs.getString("lab04c3"));
                question.getUser().setUserName(rs.getString("lab04c4"));
                question.setLastTransaction(rs.getTimestamp("lab70c6"));
                question.setState(rs.getInt("lab07c1") == 1);
                if (rs.getString("lab92c1") != null)
                {
                    question.setSelect(true);
                    question.setOrder(rs.getInt("lab92c1"));
                }
                return question;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los tipo de entrevista (Tipo orden-Laboratorio-Examen) Asociadas a
     * la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @param type Tipo de entrevista 1 -> Orden. 2 -> Laboratorio. 3 -> Examen.
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    default List<TypeInterview> listTypeInterview(Integer idInterview, Integer type) throws Exception
    {
        switch (type)
        {
            case ORDER:
                return listTypeOrder(idInterview);
            case LABORATORY:
                return listLaboratory(idInterview);
            case TEST:
                return listTest(idInterview);
            case DESTINATION:
                return listDestination(idInterview);
            default:
                return new ArrayList<>(0);
        }
    }

    /**
     * Lista los tipo de Tipo orden Asociadas a la entrevista desde la base de
     * datos.
     *
     * @param idInterview Id de la entrevista
     * @return Lista de tipo de entrevista.
     * @throws Exception Error en la base de datos.
     */
    default List<TypeInterview> listTypeOrder(Integer idInterview) throws Exception
    {
        try
        {
            String query = ""
                    + " SELECT lab103.lab103c1, lab103c2, lab103c3, lab66.lab66c1"
                    + " FROM lab103 "
                    + " LEFT JOIN lab66 ON lab66.lab66c1=lab103.lab103c1  AND lab66.lab44c1=" + idInterview;
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                TypeInterview typeInterview = new TypeInterview();

                typeInterview.setId(rs.getInt("lab103c1"));
                typeInterview.setCode(rs.getString("lab103c2"));
                typeInterview.setName(rs.getString("lab103c3"));
                typeInterview.setSelect(rs.getString("lab66c1") != null);

                return typeInterview;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los Examenes Asociadas a la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @return Lista de tipo de entrevista.
     * @throws Exception Error en la base de datos.
     */
    default List<TypeInterview> listTest(Integer idInterview) throws Exception
    {
        try
        {
            String query = ""
                    + " SELECT lab39c1 , lab39c2 , lab39c3 , lab39c4 , lab39.lab43c1 , lab66c1  "
                    + " FROM lab39 "
                    + " LEFT JOIN lab66 ON lab66.lab66c1=lab39.lab39c1 AND lab66.lab44c1=" + idInterview;
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                TypeInterview typeInterview = new TypeInterview();

                typeInterview.setId(rs.getInt("lab39c1"));
                typeInterview.setCode(rs.getString("lab39c2"));
                typeInterview.setAbbr(rs.getString("lab39c3"));
                typeInterview.setName(rs.getString("lab39c4"));
                typeInterview.setArea(rs.getInt("lab43c1"));
                typeInterview.setSelect(rs.getString("lab66c1") != null);
                return typeInterview;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los Examenes Asociadas a la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @return Lista de tipo de entrevista.
     * @throws Exception Error en la base de datos.
     */
    default List<TypeInterview> listDestination(Integer idInterview) throws Exception
    {
        try
        {
            String query = ""
                    + " SELECT lab53c1, lab53c2, lab53c3,  lab66c1  "
                    + " FROM lab53 "
                    + " LEFT JOIN lab66 ON lab66.lab66c1 = lab53.lab53c1 AND lab66.lab44c1 = " + idInterview;
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                TypeInterview typeInterview = new TypeInterview();

                typeInterview.setId(rs.getInt("lab53c1"));
                typeInterview.setCode(rs.getString("lab53c2"));
                typeInterview.setName(rs.getString("lab53c3"));
                typeInterview.setSelect(rs.getString("lab66c1") != null);
                return typeInterview;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los Laboratorios Asociadas a la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @return Lista de tipo de entrevista.
     * @throws Exception Error en la base de datos.
     */
    default List<TypeInterview> listLaboratory(Integer idInterview) throws Exception
    {
        try
        {
            String query = ""
                    + " SELECT lab40c1 , lab40c2, lab40c3, lab40c7 , lab66c1 "
                    + " FROM lab40 "
                    + " LEFT JOIN lab66 ON lab66.lab66c1=lab40.lab40c1  AND lab66.lab44c1=" + idInterview;
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                TypeInterview typeInterview = new TypeInterview();

                typeInterview.setId(rs.getInt("lab40c1"));
                typeInterview.setCode(rs.getString("lab40c2"));
                typeInterview.setName(rs.getString("lab40c3"));
                typeInterview.setType(rs.getInt("lab40c7"));
                typeInterview.setSelect(rs.getString("lab66c1") != null);
                return typeInterview;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Eliminar las preguntas asociadas a una prueba.
     *
     * @param idInterview Id de la entrevista
     *
     * @throws java.lang.Exception
     */
    default void deleteQuestion(Integer idInterview) throws Exception
    {
        String query = "DELETE FROM lab92 WHERE lab44c1 = " + idInterview;
        getJdbcTemplate().execute(query);
    }

    /**
     * Eliminar los tipos de entrevista asociadas a una Entrevista.
     *
     * @param idInterview Id de la entrevista
     *
     * @throws java.lang.Exception
     */
    default void deleteTypeInterview(Integer idInterview) throws Exception
    {
        String query = "DELETE FROM lab66 WHERE lab44c1 = " + idInterview;
        getJdbcTemplate().execute(query);
    }

    /**
     * Registra una nueva Entrevista en la base de datos.
     *
     * @param interview Instancia con los datos de la entrevista.
     *
     * @return Instancia con las datos de la entrevista.
     * @throws Exception Error en la base de datos.
     */
    default Interview create(Interview interview) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab44")
                .usingGeneratedKeyColumns("lab44c1");

        HashMap parameters = new HashMap();
        parameters.put("lab44c2", interview.getName());
        parameters.put("lab44c3", interview.getType());
        parameters.put("lab44c4", interview.isPanic() ? 1 : 0);
        parameters.put("lab07c1", 1);
        parameters.put("lab44c5", timestamp);
        parameters.put("lab44c6", interview.isInformedConsent() ? 1 : 0);
        parameters.put("lab44c7", interview.getOrdering());

        parameters.put("lab04c1", interview.getUser().getId());

        Number key = insert.executeAndReturnKey(parameters);
        interview.setId(key.intValue());
        interview.setLastTransaction(timestamp);
        //INSERTAMOS LAS PREGUNTAS
        insertQuestion(interview);
        //INSERTAMOS LOS TIPOS DE ENTREVISTA
        insertTypeInterview(interview);
        return interview;
    }

    /**
     * Registra las preguntas asociadas a una nueva Entrevista en la base de
     * datos.
     *
     * @param interview Instancia con los datos de la entrevista.
     *
     * @return Cantidad de registros Insertados.
     * @throws Exception Error en la base de datos.
     */
    default int insertQuestion(Interview interview) throws Exception
    {

        //ELIMINAMOS LAS PREGUNTAS ASOCIADAS
        deleteQuestion(interview.getId());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab92");
        for (Question question : interview.getQuestions())
        {
            HashMap parameters = new HashMap();

            parameters.put("lab44c1", interview.getId());
            parameters.put("lab70c1", question.getId());
            parameters.put("lab92c1", question.getOrder());
            parameters.put("lab92c2", question.isRequired() ? 1 : 0);

            batchArray.add(parameters);
        }

        return insert.executeBatch(batchArray.toArray(new HashMap[interview.getQuestions().size()])).length;

    }

    /**
     * Registra los tipos de entrevista asociadas a una nueva Entrevista en la
     * base de datos.
     *
     * @param interview Instancia con los datos de la entrevista.
     *
     * @return Cantidad de registros Insertados.
     * @throws Exception Error en la base de datos.
     */
    default int insertTypeInterview(Interview interview) throws Exception
    {
        //ELIMINAMOS LOS TIPOS DE ENTREVISTA
        deleteTypeInterview(interview.getId());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab66");
        for (TypeInterview typeInterview : interview.getTypeInterview())
        {
            HashMap parameters = new HashMap();

            parameters.put("lab44c1", interview.getId());
            parameters.put("lab66c1", typeInterview.getId());
            batchArray.add(parameters);
        }
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[interview.getTypeInterview().size()]));
        return inserted.length;
    }

    /**
     * Modifica una nueva Entrevista en la base de datos.
     *
     * @param interview Instancia con los datos de la entrevista.
     *
     * @return Instancia con las datos de la entrevista.
     * @throws Exception Error en la base de datos.
     */
    default Interview update(Interview interview) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        String query = ""
                + " UPDATE lab44"
                + " SET lab44c2=?"
                + " , lab44c3=? "
                + " , lab44c4=? "
                + " , lab07c1=? "
                + " , lab04c1=? "
                + " , lab44c5=? "
                + " , lab44c6=? "
                + " , lab44c7=? "
                + " WHERE  lab44c1=? ";

        getJdbcTemplate().update(query,
                interview.getName(),
                interview.getType(),
                interview.isPanic() ? 1 : 0,
                interview.isState() ? 1 : 0,
                interview.getUser().getId(),
                timestamp,
                interview.isInformedConsent() ? 1 : 0,
                interview.getOrdering(),
                interview.getId());
        interview.setLastTransaction(timestamp);
        //INSERTAMOS LAS PREGUNTAS
        int regQuestion = insertQuestion(interview);
        System.out.println("Se inserto  " + regQuestion + " preguntas a la Entrevista id :" + interview.getId());
        //INSERTAMOS LOS TIPOS DE ENTREVISTA
        int regTypeInterview = insertTypeInterview(interview);
        System.out.println("Se inserto  " + regTypeInterview + " Tipos de entrevista a la Entrevista id :" + interview.getId());

        return interview;
    }

    /**
     * Obtiene todas las respuestas para una orden determinada
     *
     * @param idOrder
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    default List<Answer> getCloseAnswersByOrder(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Pre.lab70c4 AS lab70c4, ")
                    .append("Pre.lab70c5 AS lab70c5, ")
                    .append("Res.lab90c1 AS lab90c1, ")
                    .append("Res.lab90c2 AS lab90c2, ")
                    .append("Res.lab90c3 AS lab90c3, ")
                    .append("Res.lab04c1 AS lab04c1, ")
                    .append("Res.lab07c1 AS lab07c1 ")
                    .append("FROM lab23 E ")
                    .append("JOIN lab70 Pre ON (E.lab70c1 = Pre.lab70c1) ")
                    .append("JOIN lab90 Res ON (E.lab90c1 = Res.lab90c1) ")
                    .append("WHERE E.lab22c1 = ").append(idOrder);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                Answer answersForOrder = new Answer();
                answersForOrder.setId(rs.getInt("lab90c1"));
                answersForOrder.setName(rs.getString("lab90c2"));
                answersForOrder.setLastModificationDate(rs.getTimestamp("lab90c3"));
                answersForOrder.setLastUserModify(rs.getInt("lab04c1"));
                answersForOrder.setState((rs.getInt("lab07c1") == 1));
                answersForOrder.setIsOpen((rs.getInt("lab70c4") == 1));
                answersForOrder.setControl(rs.getShort("lab70c5"));
                return answersForOrder;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene las respuestas de las preguntas de un listado de ordenes
     *
     * @param listOrder Orden a la cual se le realizara la entrevista.
     *
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     *
     * @throws Exception Error en la base de datos.
     */
    default List<Question> getInterviewOrders(String listOrder) throws Exception
    {
        List<Question> listquestion = new LinkedList<>();
        List<String> listQuestionString = new LinkedList<>();
        String query = "SELECT lab70.lab70c1,"
                + "lab70.lab70c2, "
                + "lab70.lab70c4, "
                + "lab90.lab90c1, "
                + "lab90.lab90c2, "
                + "lab23.lab23c1 AS lab23c1, "
                + "lab23.lab22c1 "
                + " FROM lab23 "
                + " LEFT JOIN lab70 ON lab70.lab70c1 = lab23.lab70c1 "
                + " LEFT JOIN lab90 ON lab90.lab90c1 = lab23.lab90c1 "
                + " WHERE lab22c1 IN (" + listOrder + ")"
                + " ";
        getJdbcTemplate().query(query, (ResultSet rs, int i) ->
        {
            Question question = new Question();
            Answer answer = new Answer();
            String uniqueKeyOrderQuestion = String.valueOf(rs.getInt("lab70c1")) + String.valueOf(rs.getLong("lab22c1"));
            question.setId(rs.getInt("lab70c1"));
            if (rs.getInt("lab70c4") != 1)
            {
                answer.setId(rs.getInt("lab90c1"));
                answer.setName(rs.getString("lab90c2"));

            }

            if (!listQuestionString.contains(uniqueKeyOrderQuestion))
            {

                listQuestionString.add(question.getId().toString());

                question.setName(rs.getString("lab70c2"));

                question.setOpen(rs.getInt("lab70c4") == 1);
                question.setInterviewAnswer(rs.getString("lab23c1"));
                question.setOrderNumber(rs.getLong("lab22c1"));
                if (answer.getId() != null)
                {
                    question.getAnswers().add(answer);
                }
                listquestion.add(question);
            } else
            {
                listquestion.get(listQuestionString.indexOf(question.getId().toString())).getAnswers().add(answer);
            }

            return question;
        });

        return listquestion;
    }

    /**
     * Obtiene todas las abiertas respuestas para una orden determinada
     *
     * @param idOrder
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    default List<Answer> getOpenAnswersByOrder(long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT Pre.lab70c4 AS lab70c4, ")
                    .append("Pre.lab70c5 AS lab70c5, ")
                    .append("E.lab23c1 AS lab23c1, ")
                    .append("E.lab23c2 AS lab23c2, ")
                    .append("E.lab04c1 AS lab04c1 ")
                    .append("FROM lab23 E ")
                    .append("JOIN lab70 Pre on (Pre.lab70c1 = E.lab70c1)  ")
                    .append("WHERE Pre.lab70c4 > 0 AND E.lab22c1 = ").append(idOrder);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                Answer answersForOrder = new Answer();
                answersForOrder.setName(rs.getString("lab23c1"));
                answersForOrder.setLastModificationDate(rs.getTimestamp("lab23c2"));
                answersForOrder.setLastUserModify(rs.getInt("lab04c1"));
                answersForOrder.setIsOpen((rs.getInt("lab70c4") == 1));
                answersForOrder.setControl(rs.getShort("lab70c5"));
                return answersForOrder;
            });
        } catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Lista las preguntas Asociadas a la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    default List<Question> listQuestionByInterview(Integer idInterview) throws Exception
    {
        try
        {
            String query = "SELECT "
                    + " lab70.lab70c1"
                    + " , lab70c2"
                    + " , lab70c3"
                    + " , lab70c4"
                    + " , lab70c5"
                    + " , lab70c6"
                    + " , lab92.lab92c2"
                    + " , lab70.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab70.lab07c1"
                    + " , lab92.lab92c1"
                    + " FROM lab70 "
                    + " LEFT JOIN lab92 ON lab92.lab70c1=lab70.lab70c1 AND lab92.lab44c1=" + idInterview
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab70.lab04c1 ";
            
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                Question question = new Question();
                question.setId(rs.getInt("lab70c1"));
                question.setName(rs.getString("lab70c2"));
                question.setQuestion(rs.getString("lab70c3"));
                question.setOpen(rs.getInt("lab70c4") == 1);
                question.setControl(rs.getShort("lab70c5"));
                question.setRequired(rs.getInt("lab92c2") == 1);
                /*Usuario*/
                question.getUser().setId(rs.getInt("lab04c1"));
                question.getUser().setName(rs.getString("lab04c2"));
                question.getUser().setLastName(rs.getString("lab04c3"));
                question.getUser().setUserName(rs.getString("lab04c4"));
                question.setLastTransaction(rs.getTimestamp("lab70c6"));
                question.setState(rs.getInt("lab07c1") == 1);
                if (rs.getString("lab92c1") != null)
                {
                    question.setSelect(true);
                    question.setOrder(rs.getInt("lab92c1"));
                }
                return question;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
}
