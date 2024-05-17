package net.cltech.enterprisent.service.impl.enterprisent.masters.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.interview.QuestionDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.service.interfaces.masters.interview.QuestionService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Procedimiento para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 11/08/2017
 * @see Creaciòn
 */
@Service
public class QuestionServiceEnterpriseNT implements QuestionService
{

    @Autowired
    private QuestionDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Question> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Question create(Question question) throws Exception
    {
        List<String> errors = validateFields(false, question);
        if (errors.isEmpty())
        {
            Question created = dao.create(question);
            trackingService.registerConfigurationTracking(null, created, Question.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Question get(Integer id, String name, String question) throws Exception
    {
        return dao.get(id, name, question);
    }

    @Override
    public Question update(Question question) throws Exception
    {
        List<String> errors = validateFields(true, question);
        if (errors.isEmpty())
        {
            Question questionC = dao.get(question.getId(), null, null);
            Question modifited = dao.update(question);
            trackingService.registerConfigurationTracking(questionC, modifited, Question.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Question> list(boolean state) throws Exception
    {
        List<Question> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Question) o).isState() == state));
        return filter;
    }

    @Override
    public List<Answer> listAnswer() throws Exception
    {
        return dao.listAnswer();
    }

    @Override
    public Answer createAnswer(Answer answer) throws Exception
    {
        List<String> errors = validateFieldsAnswer(false, answer);
        if (errors.isEmpty())
        {
            Answer created = dao.createAnswer(answer);
            trackingService.registerConfigurationTracking(null, created, Answer.class);
            return answer;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Answer getAnswer(Integer id, String name) throws Exception
    {
        return dao.getAnswer(id, name);
    }

    @Override
    public Answer updateAnswer(Answer answer) throws Exception
    {
        List<String> errors = validateFieldsAnswer(true, answer);
        if (errors.isEmpty())
        {
            Answer answerC = dao.getAnswer(answer.getId(), null);
            Answer modifited = dao.updateAnswer(answer);
            trackingService.registerConfigurationTracking(answerC, modifited, Answer.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Answer> listAnswer(boolean state) throws Exception
    {
        List<Answer> filter = new ArrayList<>(CollectionUtils.filter(dao.listAnswer(), (Object o) -> ((Answer) o).isState() == state));
        return filter;
    }

    /**
     * Valida los campos de la clase.
     *
     * @param question Objeto.
     * @param isEdit   Identifica si se esta editando el objeto
     *
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, Question question) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (question.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(question.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (question.getName() != null && !question.getName().isEmpty())
        {
            Question questionC = dao.get(null, question.getName(), null);
            if (questionC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(question.getId(), questionC.getId()))
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

        if (question.getQuestion() != null && !question.getQuestion().isEmpty())
        {
            Question questionC = dao.get(null, null, question.getQuestion());
            if (questionC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(question.getId(), questionC.getId()))
                    {
                        errors.add("1|question");
                    }
                } else
                {
                    errors.add("1|question");
                }
            }
        } else
        {
            errors.add("0|question");
        }

        if (!question.isOpen())
        {
            if (question.getAnswers().size() < 2)
            {
                errors.add("0|answers");
            }
        }

        if (question.getControl() != null && question.getControl() != 0)
        {
            if (question.getControl() < 1 && question.getControl() > 6)
            {
                errors.add("3|control");
            }
        } else
        {
            errors.add("0|control");
        }

        if (question.getUser().getId() == null || question.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    /**
     * Valida los campos de la clase.
     *
     * @param answer Objeto.
     * @param isEdit Identifica si se esta editando el objeto
     *
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFieldsAnswer(boolean isEdit, Answer answer) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (answer.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.getAnswer(answer.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (answer.getName() != null && !answer.getName().isEmpty())
        {
            Answer answerC = dao.getAnswer(null, answer.getName());
            if (answerC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(answer.getId(), answerC.getId()))
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

        if (answer.getUser().getId() == null || answer.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    @Override
    public List<Question> listClose() throws Exception
    {
        return dao.list().stream()
                .filter(question -> question.isState())
                .filter(question -> !question.isOpen())
                .collect(Collectors.toList());
    }

}
