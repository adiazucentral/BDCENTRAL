package net.cltech.enterprisent.service.impl.enterprisent.masters.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.user.RolDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.user.Module;
import net.cltech.enterprisent.domain.masters.user.Role;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.masters.user.ModuleService;
import net.cltech.enterprisent.service.interfaces.masters.user.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de servicios rol
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 08/05/2017
 * @see Creación
 */
@Service
public class RolServiceEnterpriseNT implements RoleService
{

    @Autowired
    private RolDao dao;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private TrackingService trackingService;

    @Override
    public Role create(Role create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            Role created = dao.create(create);
            trackingService.registerConfigurationTracking(null, created, Role.class);
            return created;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public List<Role> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Role findById(Integer id) throws Exception
    {
        Role found = dao.list().stream()
                .filter(role -> role.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (found != null)
        {
            found.setModules(found.getAllowedModules(moduleService.list(), dao.listRoleModules(id)));
        }

        return found;
    }

    @Override
    public Role findByName(String name) throws Exception
    {
        Role found = dao.list().stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                //                .map(role -> role.setModules(dao.listRoleModules(role.getId())))
                .findFirst()
                .orElse(null);
        if (found != null)
        {
            found.setModules(found.getAllowedModules(moduleService.list(), dao.listRoleModules(found.getId())));
        }

        return found;
    }

    @Override
    public Role update(Role update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            Role old = findById(update.getId());
            Role updated = dao.update(update);
            trackingService.registerConfigurationTracking(old, update, Role.class);
            return updated;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Valida los campos enviados del rol
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(Role validate, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (findById(validate.getId()) == null)
                {
                    errors.add("2|id");//No existe id
                } else if (validate.getId().equals(1) || validate.getId().equals(2))
                {
                    errors.add("6|Administrator");//No permite modificación
                }
            }
        }
        if (validate.getName() == null || validate.getName().trim().isEmpty())
        {
            errors.add("0|name");
        } else
        {
            Role unitFromDB = findByName(validate.getName());
            if (unitFromDB != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(unitFromDB.getId())))
                {
                    errors.add("1|name");
                }
            }
        }

        if (validate.getName() == null || validate.getName().trim().isEmpty())
        {
            errors.add("0|requirement");
        }

        if (validate.getUser().getId() == null || validate.getUser().getId().equals(0))
        {
            errors.add("0|userId");
        }
        errors.addAll(validateFieldsModules(validate));

        return errors;

    }

    /**
     * Valida los campos enviados del rol
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFieldsModules(Role validate) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (validate.getModules().size() > 0)
        {
            int row = 0;
            for (Module mod : validate.getModules())
            {
                if (mod.getId() == null || mod.getId() == 0)
                {
                    errors.add("0|moduleId|" + row);
                }
                row++;
            }
        } else
        {
            errors.add("0|Modules");
        }

        return errors;
    }

    @Override
    public List<Role> filterByState(boolean state) throws Exception
    {
        List<Role> filter = dao.list().stream()
                .filter(role -> role.isState() == state)
                .collect(Collectors.toList());
        return filter;
    }

    @Override
    public int createModules(Role createModules) throws Exception
    {
        List<String> errors = validateFieldsModules(createModules);
        if (errors.isEmpty())
        {
            return dao.createRoleModules(createModules);

        } else
        {
            throw new EnterpriseNTException(errors);

        }
    }

    public List<Module> getAllowedModules(List<Module> all, List<Module> allowed)
    {
        for (Module module : all)
        {
            if (allowed.contains(module))
            {
                all.get(all.indexOf(module)).setAccess(true);
            } else
            {
                getAllowedModules(module.getSubmodules(), allowed);
            }
        }
        return all;
    }
}
