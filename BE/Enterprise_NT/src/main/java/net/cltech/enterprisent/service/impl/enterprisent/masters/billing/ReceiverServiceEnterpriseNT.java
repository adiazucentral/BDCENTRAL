/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.billing.ReceiverDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.Receiver;
import net.cltech.enterprisent.service.interfaces.masters.billing.ReceiverService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Receptor para Enterprise
 * NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 05/07/2017
 * @see Creación
 */
@Service
public class ReceiverServiceEnterpriseNT implements ReceiverService
{

    @Autowired
    private ReceiverDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Receiver> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Receiver create(Receiver receiver) throws Exception
    {
        List<String> errors = validateFields(false, receiver);
        if (errors.isEmpty())
        {
            Receiver created = dao.create(receiver);
            trackingService.registerConfigurationTracking(null, created, Receiver.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Receiver get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public Receiver update(Receiver receiver) throws Exception
    {
        List<String> errors = validateFields(true, receiver);
        if (errors.isEmpty())
        {
            Receiver receiverC = dao.get(receiver.getId(), null);
            Receiver modifited = dao.update(receiver);
            trackingService.registerConfigurationTracking(receiverC, modifited, Receiver.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        dao.delete(id);
    }

    @Override
    public List<Receiver> list(boolean state) throws Exception
    {
        List<Receiver> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Receiver) o).isState() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, Receiver receiver) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (receiver.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(receiver.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (receiver.getName() != null && !receiver.getName().isEmpty())
        {
            Receiver receiverC = dao.get(null, receiver.getName());
            if (receiverC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(receiver.getId(), receiverC.getId()))
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
        
        if (receiver.getApplicationReceiverCode() == null || receiver.getApplicationReceiverCode().isEmpty())
        {
            errors.add("0|application receiver code");
        }
        
        if (receiver.getInterchangeReceiver()== null || receiver.getInterchangeReceiver().isEmpty())
        {
            errors.add("0|interchange receiver");
        }

        if (receiver.getUser().getId() == null || receiver.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
}
