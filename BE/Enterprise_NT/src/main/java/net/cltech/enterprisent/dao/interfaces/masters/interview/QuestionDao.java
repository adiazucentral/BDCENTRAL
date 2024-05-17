/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.interview;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Question;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * preguntas y respuestas.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/08/2017
 * @see Creación
 */
public interface QuestionDao
{

    //PREGUNTA
    /**
     * Lista las preguntas desde la base de datos.
     *
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    default List<Question> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab70c1, lab70c2, lab70c3, lab70c4, lab70c5, lab70c6, lab70.lab04c1, lab04c2, lab04c3, lab04c4, lab70.lab07c1 "
                    + "FROM lab70 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab70.lab04c1", (ResultSet rs, int i) ->
            {
                Question question = new Question();
                question.setId(rs.getInt("lab70c1"));
                question.setName(rs.getString("lab70c2"));
                question.setQuestion(rs.getString("lab70c3"));
                question.setOpen(rs.getInt("lab70c4") == 1);
                question.setControl(rs.getShort("lab70c5"));
                /*Usuario*/
                question.getUser().setId(rs.getInt("lab04c1"));
                question.getUser().setName(rs.getString("lab04c2"));
                question.getUser().setLastName(rs.getString("lab04c3"));
                question.getUser().setUserName(rs.getString("lab04c4"));

                question.setLastTransaction(rs.getTimestamp("lab70c6"));
                question.setState(rs.getInt("lab07c1") == 1);

                return question;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva pregunta en la base de datos.
     *
     * @param question Instancia con los datos de la pregunta.
     *
     * @return Instancia con las datos de la pregunta.
     * @throws Exception Error en la base de datos.
     */
    default Question create(Question question) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab70")
                .usingGeneratedKeyColumns("lab70c1");

        HashMap parameters = new HashMap();
        parameters.put("lab70c2", question.getName().trim());
        parameters.put("lab70c3", question.getQuestion().trim());
        parameters.put("lab70c4", question.isOpen() ? 1 : 0);
        parameters.put("lab70c5", question.getControl());
        parameters.put("lab70c6", timestamp);
        parameters.put("lab04c1", question.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        question.setId(key.intValue());
        question.setLastTransaction(timestamp);

        insertAnswer(question);

        return question;
    }

    /**
     * Obtener información de un pregunta por un campo especifico.
     *
     * @param id ID del pregunta a ser consultada.
     * @param name Nombre del pregunta a ser consultada.
     * @param question Pregunta del pregunta a ser consultada.
     *
     * @return Instancia con las datos de la pregunta.
     * @throws Exception Error en la base de datos.
     */
    default Question get(Integer id, String name, String question) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab70c1, lab70c2, lab70c3, lab70c4, lab70c5, lab70c6, lab70.lab04c1, lab04c2, lab04c3, lab04c4, lab70.lab07c1 "
                    + "FROM lab70 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab70.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab70c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab70c2) = ? ";
            }
            if (question != null)
            {
                query = query + "WHERE UPPER(lab70c3) = ? ";
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
            if (question != null)
            {
                object = question.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Question question1 = new Question();
                question1.setId(rs.getInt("lab70c1"));
                question1.setName(rs.getString("lab70c2"));
                question1.setQuestion(rs.getString("lab70c3"));
                question1.setOpen(rs.getInt("lab70c4") == 1);
                question1.setControl(rs.getShort("lab70c5"));
                /*Usuario*/
                question1.getUser().setId(rs.getInt("lab04c1"));
                question1.getUser().setName(rs.getString("lab04c2"));
                question1.getUser().setLastName(rs.getString("lab04c3"));
                question1.getUser().setUserName(rs.getString("lab04c4"));
                question1.setLastTransaction(rs.getTimestamp("lab70c6"));
                question1.setState(rs.getInt("lab07c1") == 1);
                if (!question1.isOpen())
                {
                    readAnswer(question1);
                }
                return question1;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un pregunta en la base de datos.
     *
     * @param question Instancia con los datos de la pregunta.
     *
     * @return Objeto de la pregunta modificada.
     * @throws Exception Error en la base de datos.
     */
    default Question update(Question question) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab70 SET lab70c2 = ?, lab70c3 = ?, lab70c4 = ?, lab70c5 = ?, lab70c6 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab70c1 = ?",
                question.getName().trim(), question.getQuestion().trim(), question.isOpen() ? 1 : 0, question.getControl(), timestamp, question.getUser().getId(), question.isState() ? 1 : 0, question.getId());

        question.setLastTransaction(timestamp);

        insertAnswer(question);

        return question;
    }

    //RESPUESTA
    /**
     * Lista las respuestas desde la base de datos.
     *
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    default List<Answer> listAnswer() throws Exception
    {
        try
        {
            String quantity = "(SELECT COUNT(*) AS quantity FROM lab91 WHERE lab90c1 = lab90.lab90c1) AS quantity";
            return getJdbcTemplate().query(""
                    + "SELECT lab90c1, lab90c2, lab90c3, lab90.lab04c1, lab04c2, lab04c3, lab04c4, lab90.lab07c1, " + quantity
                    + " FROM lab90"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab90.lab04c1", (ResultSet rs, int i) ->
            {
                Answer answer = new Answer();
                answer.setId(rs.getInt("lab90c1"));
                answer.setName(rs.getString("lab90c2"));
                answer.setQuantity(rs.getInt("quantity"));
                /*Usuario*/
                answer.getUser().setId(rs.getInt("lab04c1"));
                answer.getUser().setName(rs.getString("lab04c2"));
                answer.getUser().setLastName(rs.getString("lab04c3"));
                answer.getUser().setUserName(rs.getString("lab04c4"));

                answer.setLastTransaction(rs.getTimestamp("lab90c3"));
                answer.setState(rs.getInt("lab07c1") == 1);

                return answer;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva respuesta en la base de datos.
     *
     * @param answer Instancia con los datos de la respuesta.
     *
     * @return Instancia con las datos de la respuesta.
     * @throws Exception Error en la base de datos.
     */
    default Answer createAnswer(Answer answer) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab90")
                .usingGeneratedKeyColumns("lab90c1");

        HashMap parameters = new HashMap();
        parameters.put("lab90c2", answer.getName().trim());
        parameters.put("lab90c3", timestamp);
        parameters.put("lab04c1", answer.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        answer.setId(key.intValue());
        answer.setLastTransaction(timestamp);

        return answer;
    }

    /**
     * Obtener información de una respuesta por un campo especifico.
     *
     * @param id ID del respuesta a ser consultada.
     * @param name Nombre del respuesta a ser consultada.
     *
     * @return Instancia con las datos de la pregunta.
     * @throws Exception Error en la base de datos.
     */
    default Answer getAnswer(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String quantity = "(SELECT COUNT(*) AS quantity FROM lab91 WHERE lab90c1 = lab90.lab90c1) AS quantity";
            String query = "SELECT lab90c1, lab90c2, lab90c3, lab90.lab04c1, lab04c2, lab04c3, lab04c4, lab90.lab07c1, " + quantity
                    + " FROM lab90"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab90.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab90c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab90c2) = ? ";
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
                Answer answer = new Answer();
                answer.setId(rs.getInt("lab90c1"));
                answer.setName(rs.getString("lab90c2"));
                answer.setQuantity(rs.getInt("quantity"));
                /*Usuario*/
                answer.getUser().setId(rs.getInt("lab04c1"));
                answer.getUser().setName(rs.getString("lab04c2"));
                answer.getUser().setLastName(rs.getString("lab04c3"));
                answer.getUser().setUserName(rs.getString("lab04c4"));

                answer.setLastTransaction(rs.getTimestamp("lab90c3"));
                answer.setState(rs.getInt("lab07c1") == 1);

                return answer;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un respuesta en la base de datos.
     *
     * @param answer Instancia con los datos de la respuesta.
     *
     * @return Objeto de la respuesta modificada.
     * @throws Exception Error en la base de datos.
     */
    default Answer updateAnswer(Answer answer) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab90 SET lab90c2 = ?, lab90c3 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab90c1 = ?",
                answer.getName().trim(), timestamp, answer.getUser().getId(), answer.isState() ? 1 : 0, answer.getId());

        answer.setLastTransaction(timestamp);

        return answer;
    }

    /**
     * Asociar respuestas a una pregunta.
     *
     * @param question Instancia con los datos de la prgeunta.
     * @throws java.lang.Exception
     */
    default void insertAnswer(Question question) throws Exception
    {
        deleteAnswer(question.getId());
        for (Answer answer : question.getAnswers())
        {
            if (answer.isSelected())
            {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                        .withTableName("lab91");

                HashMap parameters = new HashMap();
                parameters.put("lab70c1", question.getId());
                parameters.put("lab90c1", answer.getId());
                parameters.put("lab70c5", question.getControl());

                insert.execute(parameters);
            }
        }
    }

    /**
     * Obtener respuestas asociadas a una pregunta.
     *
     * @param question Instancia con los datos de la pregunta.
     */
    default void readAnswer(Question question)
    {
        try
        {
            String quantity = "(SELECT COUNT(*) AS quantity FROM lab91 WHERE lab90c1 = lab90.lab90c1 AND lab70c1 != ?) AS quantity";
            question.setAnswers(getJdbcTemplate().query("SELECT lab90.lab90c1, lab90c2, lab91.lab70c1, lab90.lab07c1, " + quantity + " FROM lab90 "
                    + "LEFT JOIN lab91 ON lab90.lab90c1 = lab91.lab90c1 AND lab91.lab70c1 = ?",
                    new Object[]
                    {
                        question.getId(),
                        question.getId()
                    }, (ResultSet rs, int i) ->
            {
                Answer answer = new Answer();
                answer.setId(rs.getInt("lab90c1"));
                answer.setName(rs.getString("lab90c2"));
                answer.setState(rs.getInt("lab07c1") == 1);
                answer.setSelected(rs.getString("lab70c1") != null);
                answer.setQuantity(rs.getInt("quantity"));
                return answer;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            question.setAnswers(new ArrayList<>());
        }
    }

    /**
     * Eliminar respuestas a preguntas.
     *
     * @param id Id de la pregunta a la que se le borraran las respuestas
     * @throws java.lang.Exception
     */
    default void deleteAnswer(Integer id) throws Exception
    {
        String query = "DELETE FROM lab91 WHERE lab70c1 = " + id;
        getJdbcTemplate().execute(query);
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
