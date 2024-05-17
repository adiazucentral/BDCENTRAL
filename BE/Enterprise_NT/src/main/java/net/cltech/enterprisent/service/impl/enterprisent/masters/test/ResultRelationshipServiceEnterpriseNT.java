package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.test.ResultRelationshipDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Alarm;
import net.cltech.enterprisent.domain.masters.test.ResultRelationship;
import net.cltech.enterprisent.service.interfaces.masters.test.AlarmService;
import net.cltech.enterprisent.service.interfaces.masters.test.ResultRelationshipService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a relacion resultados
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 28/07/2017
 * @see Creación
 */
@Service
public class ResultRelationshipServiceEnterpriseNT implements ResultRelationshipService
{

    @Autowired
    private ResultRelationshipDao dao;
    @Autowired
    private AlarmService serviceAlarm;

    @Autowired
    private TrackingService trackingService;

    @Override
    public int create(Alarm create) throws Exception
    {
        List<String> errors = validateFields(create);
        if (errors.isEmpty())
        {
            create.setUser(trackingService.getRequestUser());
            dao.delete(create.getId());
            trackingService.registerConfigurationTracking(null, create, Alarm.class);
            return dao.create(create);

        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<ResultRelationship> list(int alarm) throws Exception
    {
        return dao.list(alarm);
    }

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(Alarm validate) throws Exception
    {

        List<String> errors = new ArrayList<>();
        List<Alarm> all = serviceAlarm.list();

        if (validate.getId() == null)
        {
            errors.add("0|alarm");
        } else if (!all.contains(new Alarm(validate.getId())))
        {
            errors.add("3|alarm");
        }

        int index = 0;
        if (validate.getRules() != null)
        {
            for (ResultRelationship rule : validate.getRules())
            {
                if (rule.getType() == 1 && rule.getTest() == null)
                {
                    errors.add("0|test|" + index);
                }
                if (rule.getType() == 2 && rule.getQuestion() == null)
                {
                    errors.add("0|question|" + index);
                }
                if (!Arrays.asList(1, 2).contains(rule.getType()))
                {
                    errors.add("3|type|" + index);
                }
                index++;
            }
        }

        return errors;

    }

}
