/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.service.impl.enterprisent.masters.configuration;

import java.util.List;
import net.cltech.outreach.dao.interfaces.masters.configuration.UserTypeDao;
import net.cltech.outreach.domain.masters.configuration.UserType;
import net.cltech.outreach.service.interfaces.masters.configuration.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a configuracion general para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/04/2018
 * @see Creación
 */
@Service
public class UserTypeServiceEnterpriseNT implements UserTypeService
{

    @Autowired
    private UserTypeDao dao;

    @Override
    public List<UserType> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public int update(List<UserType> userTypes) throws Exception
    {
        return dao.update(userTypes);
    }

}
