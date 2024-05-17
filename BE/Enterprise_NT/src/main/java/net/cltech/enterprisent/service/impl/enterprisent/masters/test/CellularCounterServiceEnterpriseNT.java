/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.test.CellularCounterDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.CellularCounter;
import net.cltech.enterprisent.service.interfaces.masters.test.CellularCounterService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Contador Hematologico
 * para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 01/08/2017
 * @see Creaciòn
 */
@Service
public class CellularCounterServiceEnterpriseNT implements CellularCounterService
{

    @Autowired
    private CellularCounterDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<CellularCounter> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public CellularCounter create(CellularCounter cellularCounter) throws Exception
    {
        List<String> errors = validateFields(false, cellularCounter);
        if (errors.isEmpty())
        {
            CellularCounter created = dao.create(cellularCounter);
            trackingService.registerConfigurationTracking(null, created, CellularCounter.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public CellularCounter get(Integer id, String key) throws Exception
    {
        return dao.get(id, key);
    }
    
    @Override
    public List<CellularCounter> geTest(Integer id) throws Exception
    {
        return dao.getTest(id);
    }

    @Override
    public CellularCounter update(CellularCounter cellularCounter) throws Exception
    {
        List<String> errors = validateFields(true, cellularCounter);
        if (errors.isEmpty())
        {
            CellularCounter cellularCounterC = dao.get(cellularCounter.getId(), null);
            CellularCounter modifited = dao.update(cellularCounter);
            trackingService.registerConfigurationTracking(cellularCounterC, modifited, CellularCounter.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<CellularCounter> list(boolean state) throws Exception
    {
        List<CellularCounter> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((CellularCounter) o).isState() == state));
        return filter;
    }

    /**
     * Valida los campos de la clase.
     *
     * @param cellularCounter Objeto Contador Hematologico
     * @param isEdit Identifica si se esta editando el objeto
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, CellularCounter cellularCounter) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (cellularCounter.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(cellularCounter.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (cellularCounter.getKey() != null && !cellularCounter.getKey().isEmpty())
        {
            CellularCounter worksheetC = dao.get(null, cellularCounter.getKey());
            if (worksheetC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(cellularCounter.getId(), worksheetC.getId()))
                    {
                        errors.add("1|key");
                    }
                } else
                {
                    errors.add("1|key");
                }
            }
        } else
        {
            errors.add("0|key");
        }

        if (cellularCounter.getText() == null || cellularCounter.getText().isEmpty())
        {
            errors.add("0|text");
        }

        if (cellularCounter.getType() != null && cellularCounter.getType() != 0)
        {
            if (cellularCounter.getType() != 1 && cellularCounter.getType() != 2)
            {
                errors.add("3|type");
            }
        } else
        {
            errors.add("0|type");
        }

        if (cellularCounter.getUser().getId() == null || cellularCounter.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
}
