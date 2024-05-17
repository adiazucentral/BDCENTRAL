package net.cltech.enterprisent.service.impl.enterprisent.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ShortcutDao;
import net.cltech.enterprisent.dao.interfaces.masters.user.ModuleDao;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.domain.access.Shortcut;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.user.Module;
import net.cltech.enterprisent.service.interfaces.common.ShortcutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios shortcut para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 19/09/2017
 * @see Creaciòn
 */
@Service
public class ShortcutServiceEnterpriseNT implements ShortcutService
{

    @Autowired
    private ShortcutDao dao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ModuleDao moduleDao;

    @Override
    public List<Module> list(int user, int module) throws Exception
    {
        return dao.list(user)
                .stream()
                .filter(filter -> module == 0 || filter.getIdFather().equals(module))
                .collect(Collectors.toList());
    }

    @Override
    public int add(Shortcut shortcut) throws Exception
    {
        List<String> errors = validateFields(shortcut);
        if (errors.isEmpty())
        {
            return dao.create(shortcut);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public int delete(Shortcut shortcut) throws Exception
    {
        return dao.delete(shortcut);
    }

    /**
     * Valida los campos enviados del rol
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(Shortcut shortcut) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (!moduleDao.list().contains(new Module(shortcut.getForm())))
        {
            errors.add("2|module");
        }

        boolean userExists = userDao.list()
                .stream()
                .anyMatch(filter -> filter.getId().equals(shortcut.getUser()));
        if (!userExists)
        {
            errors.add("2|user");

        }

        return errors;
    }

}
