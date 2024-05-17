/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.test.WorksheetDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.Worksheet;
import net.cltech.enterprisent.service.interfaces.masters.test.WorksheetService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Hojas de Trabajo para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 31/07/2017
 * @see Creaciòn
 */
@Service
public class WorksheetServiceEnterpriseNT implements WorksheetService
{

    @Autowired
    private WorksheetDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Worksheet> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Worksheet create(Worksheet worksheet) throws Exception
    {
        List<String> errors = validateFields(false, worksheet);
        if (errors.isEmpty())
        {
            Worksheet created = dao.create(worksheet);
            trackingService.registerConfigurationTracking(null, created, Worksheet.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Worksheet get(Integer id, String name) throws Exception
    {
        return dao.get(id, name);
    }

    @Override
    public Worksheet update(Worksheet worksheet) throws Exception
    {
        List<String> errors = validateFields(true, worksheet);
        if (errors.isEmpty())
        {
            Worksheet worksheetC = dao.get(worksheet.getId(), null);
            Worksheet modifited = dao.update(worksheet);
            trackingService.registerConfigurationTracking(worksheetC, modifited, Worksheet.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Worksheet> list(boolean state) throws Exception
    {
        List<Worksheet> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Worksheet) o).isState() == state));
        return filter;
    }

    /**
     * Valida campos obligatorios para la homologación
     *
     *
     *
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, Worksheet worksheet) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (worksheet.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(worksheet.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (worksheet.getName() != null && !worksheet.getName().isEmpty())
        {
            Worksheet worksheetC = dao.get(null, worksheet.getName());
            if (worksheetC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(worksheet.getId(), worksheetC.getId()))
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

        if (!worksheet.isMicrobiology())
        {
            if (worksheet.getType() == null || worksheet.getType() == 0)
            {
                errors.add("0|type");
            }

            if (worksheet.getOrientation() == null || worksheet.getOrientation() == 0)
            {
                errors.add("0|orientation");
            }
        }

        if (worksheet.getTests().isEmpty())
        {
            errors.add("0|tests");
        }

        if (worksheet.getUser().getId() == null || worksheet.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    @Override
    public List<TestBasic> testsByWorkSheets(int id) throws Exception
    {
        return dao.testsByWorkSheets(id);
    }

}
