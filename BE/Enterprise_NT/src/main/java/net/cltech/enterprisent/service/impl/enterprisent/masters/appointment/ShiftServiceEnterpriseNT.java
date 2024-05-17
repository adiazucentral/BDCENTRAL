/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.appointment.ShiftDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.service.interfaces.masters.appointment.ShiftService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftServiceEnterpriseNT implements ShiftService
{

    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ShiftDao dao;
    @Autowired(required = true)
    private HttpServletRequest request;

    @Override
    public List<Shift> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Shift create(Shift shift) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        shift.setUser(user);
        List<String> errors = validateFields(false, shift);
        if (errors.isEmpty())
        {
            Shift created = dao.create(shift);
            trackingService.registerConfigurationTracking(null, created, Shift.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Shift get(Integer id, String name) throws Exception
    {
        return list().stream()
                .filter(shift -> id == null || shift.getId().equals(id))
                .filter(shift -> name == null || shift.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Shift update(Shift shift) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        shift.setUser(user);
        List<String> errors = validateFields(true, shift);
        if (errors.isEmpty())
        {
            Shift newBean = get(shift.getId(), null);
            Shift modified = dao.update(shift);
            trackingService.registerConfigurationTracking(newBean, modified, Shift.class);
            return modified;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Shift> list(boolean state) throws Exception
    {
        return dao.list().stream().filter(shift -> shift.isState() == state).collect(Collectors.toList());
    }

    /**
     * Validacion de campos
     *
     * @param isEdit es edición
     * @param shift campos a validar
     *
     * @return LIsta de errores 0 -> Datos vacios 1 -> Esta duplicado 2 -> Id no
     * existe solo aplica para modificar
     * @throws Exception Error
     */
    private List<String> validateFields(boolean isEdit, Shift shift) throws Exception
    {

        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (shift.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (get(shift.getId(), null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (shift.getName() != null && !shift.getName().isEmpty())
        {
            Shift shiftC = get(null, shift.getName());
            if (shiftC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(shift.getId(), shiftC.getId()))
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

        if (shift.getUser().getId() == null || shift.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
    
    @Override
    public int insertShiftsbyBranch(Branch branch) throws Exception
    {
        //User userC = dao.get(user.getId(), null);
        int quantity = dao.insertShiftsbyBranch(branch.getShifts(), branch.getId());
        //User modifited = dao.get(user.getId(), null);

        //auditService.saveAuditMaster(userC, modifited, User.class);
        return quantity;
    }
    
    @Override
    public List<Shift> listShiftsbyBranch(Integer idBranch) throws Exception
    {
        return dao.listShiftbyBranch(idBranch);
    }
    
    @Override
    public List<Shift> listShift(Integer branch, int date, int day) throws Exception
    {
        return dao.listShift( branch,  date,  day);
    }

}
