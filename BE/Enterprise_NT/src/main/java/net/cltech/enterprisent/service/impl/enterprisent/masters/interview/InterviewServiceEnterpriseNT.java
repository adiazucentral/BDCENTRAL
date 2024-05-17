/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.interview.InterviewDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.interview.InterviewDestinations;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.interview.TypeInterview;
import net.cltech.enterprisent.service.interfaces.masters.interview.InterviewService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Entrevista para Enterprise NT
 *
 * @version 1.0.0
 * @author enavas
 * @see 18/08/2017
 * @see Creaciòn
 */
@Service
public class InterviewServiceEnterpriseNT implements InterviewService
{

    @Autowired
    private InterviewDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Interview> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Interview get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public List<Question> listQuestion(Integer idInterview) throws Exception
    {
        return dao.listQuestion(idInterview);
    }

    @Override
    public List<TypeInterview> listTypeInterview(Integer idInterview, Integer type) throws Exception
    {
        return dao.listTypeInterview(idInterview, type);
    }

    @Override
    public Interview create(Interview interview) throws Exception
    {
        List<String> errors = validateFields(false, interview);
        if (errors.isEmpty())
        {
            Interview created = dao.create(interview);
            trackingService.registerConfigurationTracking(null, created, Interview.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Interview update(Interview interview) throws Exception
    {
        List<String> errors = validateFields(true, interview);
        if (errors.isEmpty())
        {
            Interview interviewC = dao.get(interview.getId(), null);
            //Preguntas asociadas
            List<Question> filterQuestion = new ArrayList<>(CollectionUtils.filter(dao.listQuestion(interviewC.getId()), (Object o) -> ((Question) o).isSelect() == true));
            interviewC.setQuestions(filterQuestion);
            //Tipo de Entrevista Asociadas
            List<TypeInterview> filterTypeInterview = new ArrayList<>(CollectionUtils.filter(dao.listTypeInterview(interviewC.getId(), interviewC.getType().intValue()), (Object o) -> ((TypeInterview) o).isSelect() == true));
            interviewC.setTypeInterview(filterTypeInterview);
            Interview modifited = dao.update(interview);
            trackingService.registerConfigurationTracking(interviewC, modifited, Interview.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Valida campos obligatorios para la Entrevista
     *
     * @param isEdit bandera si es editable
     * @param interview el objeto de la entrevista
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, Interview interview) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (interview.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(interview.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (interview.getName() != null && !interview.getName().isEmpty())
        {
            Interview interviewC = dao.get(null, interview.getName());
            if (interviewC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(interview.getId(), interviewC.getId()))
                    {
                        errors.add("1|name");
                    }
                } else
                {
                    errors.add("1|name");
                }
            }
        } else
        {
            errors.add("0|name");
        }

        if (interview.getType() == null || interview.getType() == 0)
        {
            errors.add("0|Type");
        }

        if (interview.getQuestions().size() == 0)
        {
            errors.add("0|Questions");
        }

        if (interview.getTypeInterview().size() == 0)
        {
            errors.add("0|TypeInterview");
        }

        return errors;
    }

    /**
     * Obtiene todas las respuestas para una orden determinada
     *
     * @param idOrder
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    @Override
    public List<Answer> getAnswersByOrder(long idOrder) throws Exception
    {
        try
        {
            // Obtengo las respuestas abiertas
            List<Answer> listOpenAnswers = dao.getOpenAnswersByOrder(idOrder);
            // Luego obtengo las respuestas cerradas
            List<Answer> listCloseAnswers = dao.getCloseAnswersByOrder(idOrder);
            List<Answer> listAllResponse = new ArrayList<>();
            if (!listOpenAnswers.isEmpty())
            {
                listAllResponse.addAll(listOpenAnswers);
            }
            if (!listCloseAnswers.isEmpty())
            {
                listAllResponse.addAll(listCloseAnswers);
            }
            return listAllResponse;
        } catch (Exception e)
        {
            return null;
        }
    }
    
     /**
     * Obtiene las respuestas de las preguntas de un listado de ordenes
     *
     * @param orders
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    @Override
    public List<Question> getInterviewOrders(String orders) throws Exception
    {
        try
        {
            // Obtengo las respuestas abiertas
            return dao.getInterviewOrders(orders);
        } catch (Exception e)
        {
            return null;
        }
    }

    
    @Override
    public int createDestinationsInterview(InterviewDestinations interviewDestinations) throws Exception
    {
       return 1;
    }
    
    @Override
    public List<Question> listQuestionByInterview(Integer idInterview) throws Exception
    {
        return dao.listQuestionByInterview(idInterview);
    }
}
