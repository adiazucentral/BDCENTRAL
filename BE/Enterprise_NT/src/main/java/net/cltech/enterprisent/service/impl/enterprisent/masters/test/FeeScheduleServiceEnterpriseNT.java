/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.test.FeeScheduleDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.FeeSchedule;
import net.cltech.enterprisent.service.interfaces.masters.test.FeeScheduleService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro vigencias para Enterprise
 * NT
 *
 * @version 1.0.0
 * @author enavas
 * @see 10/08/2017
 * @see Creaciòn
 */
@Service
public class FeeScheduleServiceEnterpriseNT implements FeeScheduleService
{

    @Autowired
    private FeeScheduleDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<FeeSchedule> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public FeeSchedule create(FeeSchedule feeSchedule) throws Exception
    {
        List<String> errors = validateFields(false, feeSchedule);
        if (errors.isEmpty())
        {
            FeeSchedule created = dao.create(feeSchedule);
            trackingService.registerConfigurationTracking(null, created, FeeSchedule.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public FeeSchedule get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public FeeSchedule update(FeeSchedule feeSchedule) throws Exception
    {

        List<String> errors = validateFields(true, feeSchedule);
        if (errors.isEmpty())
        {
            FeeSchedule feeScheduleC = dao.get(feeSchedule.getId(), null);
            FeeSchedule modifited = dao.update(feeSchedule);
            trackingService.registerConfigurationTracking(feeScheduleC, modifited, FeeSchedule.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    @Override
    public List<FeeSchedule> list(boolean state) throws Exception
    {
        List<FeeSchedule> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((FeeSchedule) o).isState() == state));
        return filter;
    }

    /**
     * Valida campos obligatorios para la vigencia
     *
     *
     *
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, FeeSchedule feeSchedule) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (feeSchedule.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(feeSchedule.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (feeSchedule.getName() != null && !feeSchedule.getName().isEmpty())
        {
            FeeSchedule feeScheduleC = dao.get(null, feeSchedule.getName());
            if (feeScheduleC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(feeSchedule.getId(), feeScheduleC.getId()))
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

        return errors;
    }

}
