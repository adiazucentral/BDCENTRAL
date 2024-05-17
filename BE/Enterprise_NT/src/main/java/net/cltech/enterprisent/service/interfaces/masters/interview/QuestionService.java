/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.interview;

import java.util.List;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Question;

/**
 * Interfaz de servicios a la informacion del maestro Preguntas y Respuestas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 15/08/2017
 * @see Creación
 */
public interface QuestionService
{

    /**
     * Lista las preguntas desde la base de datos.
     *
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> list() throws Exception;

    /**
     * Registra una nueva pregunta en la base de datos.
     *
     * @param question Instancia con los datos de la pregunta.
     *
     * @return Instancia con las datos de la pregunta.
     * @throws Exception Error en la base de datos.
     */
    public Question create(Question question) throws Exception;

    /**
     * Obtener información de un pregunta por un campo especifico.
     *
     * @param id       ID del pregunta a ser consultada.
     * @param name     Nombre del pregunta a ser consultada.
     * @param question Pregunta del pregunta a ser consultada.
     *
     * @return Instancia con las datos de la pregunta.
     * @throws Exception Error en la base de datos.
     */
    public Question get(Integer id, String name, String question) throws Exception;

    /**
     * Actualiza la información de un pregunta en la base de datos.
     *
     * @param question Instancia con los datos de la pregunta.
     *
     * @return Objeto de la pregunta modificada.
     * @throws Exception Error en la base de datos.
     */
    public Question update(Question question) throws Exception;

    /**
     * Obtener información de preguntas por estado.
     *
     * @param state Estado de las preguntas a ser consultados
     *
     * @return Instancia con los datos de la Pregunta.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> list(boolean state) throws Exception;

    /**
     * Obtener información de preguntas por estado.
     *
     * @param type Tipo de pregunta
     *
     * @return Instancia con los datos de la Pregunta.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> listClose() throws Exception;

    /**
     * Lista las respuestas desde la base de datos.
     *
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    public List<Answer> listAnswer() throws Exception;

    /**
     * Registra una nueva respuesta en la base de datos.
     *
     * @param answer Instancia con los datos de la respuesta.
     *
     * @return Instancia con las datos de la respuesta.
     * @throws Exception Error en la base de datos.
     */
    public Answer createAnswer(Answer answer) throws Exception;

    /**
     * Obtener información de una respuesta por un campo especifico.
     *
     * @param id   ID del respuesta a ser consultada.
     * @param name Nombre del respuesta a ser consultada.
     *
     * @return Instancia con las datos de la pregunta.
     * @throws Exception Error en la base de datos.
     */
    public Answer getAnswer(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de un respuesta en la base de datos.
     *
     * @param answer Instancia con los datos de la respuesta.
     *
     * @return Objeto de la respuesta modificada.
     * @throws Exception Error en la base de datos.
     */
    public Answer updateAnswer(Answer answer) throws Exception;

    /**
     * Obtener información de respuestas por estado.
     *
     * @param state Estado de las respuestas a ser consultados
     *
     * @return Instancia con los datos de la Respuesta.
     * @throws Exception Error en la base de datos.
     */
    public List<Answer> listAnswer(boolean state) throws Exception;
}
