package net.cltech.enterprisent.service.interfaces.masters.interview;

import java.util.List;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.interview.InterviewDestinations;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.interview.TypeInterview;

/**
 * Interfaz de servicios a la informacion del maestro de la entrevista
 *
 * @version 1.0.0
 * @author enavas
 * @since 18/08/2017
 * @see Creación
 */
public interface InterviewService
{

    /**
     * Lista las entrevistas desde la base de datos.
     *
     * @return Lista de entrevista.
     * @throws Exception Error en la base de datos.
     */
    public List<Interview> list() throws Exception;

    /**
     * Obtener información de una entrevista por un campo especifico.
     *
     * @param id ID de la entrevista a ser consultada.
     * @param name Nombre de la entrevista a ser consultada.
     *
     * @return Instancia con las datos de la entrevista.
     * @throws Exception Error en la base de datos.
     */
    public Interview get(Integer id, String name) throws Exception;

    /**
     * Lista las preguntas Asociadas a la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> listQuestion(Integer idInterview) throws Exception;

    /**
     * Lista los tipo de entrevista (Tipo orden-Laboratorio-Examen) Asociadas a
     * la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @param type Tipo de entrevista 1 -> Orden. 2 -> Laboratorio. 3 -> Examen.
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    public List<TypeInterview> listTypeInterview(Integer idInterview, Integer type) throws Exception;

    /**
     * Registra una nueva Entrevista en la base de datos.
     *
     * @param interview Instancia con los datos de la entrevista.
     *
     * @return Instancia con las datos de la entrevista.
     * @throws Exception Error en la base de datos.
     */
    public Interview create(Interview interview) throws Exception;

    /**
     * Modifica una nueva Entrevista en la base de datos.
     *
     * @param interview Instancia con los datos de la entrevista.
     *
     * @return Instancia con las datos de la entrevista.
     * @throws Exception Error en la base de datos.
     */
    public Interview update(Interview interview) throws Exception;
    
    /**
     * Obtiene todas las respuestas para una orden determinada
     * 
     * @param idOrder
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    public List<Answer> getAnswersByOrder(long idOrder) throws Exception;
    
    
    /**
     * Obtiene las respuestas de las preguntas de un listado de ordenes
     * 
     * @param orders
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    public List<Question> getInterviewOrders(String orders) throws Exception;
    
      /**
     * Asociar los destinos a una entrevista.
     *
     * @param interviewDestinations instancia con los datos de la entrevista y 
     * destinos
     * @return cantidad de registros insertados
     * @throws java.lang.Exception
     */
    public int createDestinationsInterview(InterviewDestinations interviewDestinations) throws Exception;
    
    /**
     * Lista las preguntas Asociadas a la entrevista desde la base de datos.
     *
     * @param idInterview Id de la entrevista
     * @return Lista de preguntas.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> listQuestionByInterview(Integer idInterview) throws Exception;
    
    
}
