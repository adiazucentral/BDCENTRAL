/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.ProcedureDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.microbiology.TestProcedure;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.ProcedureService;
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
public class ProcedureServiceEnterpriseNT implements ProcedureService
{

    @Autowired
    private ProcedureDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<Procedure> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Procedure create(Procedure procedure) throws Exception
    {
        List<String> errors = validateFields(false, procedure);
        if (errors.isEmpty())
        {
            Procedure created = dao.create(procedure);
            trackingService.registerConfigurationTracking(null, created, Procedure.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Procedure get(Integer id, String code, String name) throws Exception
    {
        return dao.get(id, code, name);
    }

    @Override
    public Procedure update(Procedure procedure) throws Exception
    {
        List<String> errors = validateFields(true, procedure);
        if (errors.isEmpty())
        {
            Procedure procedureC = dao.get(procedure.getId(), null, null);
            Procedure modifited = dao.update(procedure);
            trackingService.registerConfigurationTracking(procedureC, modifited, Procedure.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Procedure> list(boolean state) throws Exception
    {
        List<Procedure> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Procedure) o).isState() == state));
        return filter;
    }

    /**
     * Valida los campos de la clase.
     *
     * @param procedure Objeto.
     * @param isEdit Identifica si se esta editando el objeto
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, Procedure procedure) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (procedure.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(procedure.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (procedure.getCode() != null && !procedure.getCode().isEmpty())
        {
            Procedure procedureC = dao.get(null, procedure.getCode(), null);
            if (procedureC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(procedure.getId(), procedureC.getId()))
                    {
                        errors.add("1|code");
                    }
                } else
                {
                    errors.add("1|code");
                }
            }
        } else
        {
            errors.add("0|code");
        }

        if (procedure.getName() != null && !procedure.getName().isEmpty())
        {
            Procedure procedureC = dao.get(null, null, procedure.getName());
            if (procedureC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(procedure.getId(), procedureC.getId()))
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

        if (procedure.getUser().getId() == null || procedure.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    @Override
    public List<TestProcedure> listTestProcedure(Integer idtest) throws Exception
    {
        return dao.listTestProcedure(idtest);
    }

    @Override
    public int insertTestProcedure(List<TestProcedure> testProcedures) throws Exception
    {
        List<TestProcedure> listPrevious = listTestProcedure(testProcedures.get(0).getTest().getId());
        Integer insert = dao.insertTestProcedure(testProcedures);
        trackingService.registerConfigurationTracking(listPrevious, testProcedures, TestProcedure.class);
        return insert;
    }
}
