/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.service.impl.securitynt.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.securitynt.dao.interfaces.security.SessionDao;
import net.cltech.securitynt.domain.common.AuthenticationSession;
import net.cltech.securitynt.domain.masters.user.User;
import net.cltech.securitynt.service.interfaces.security.SessionService;
import net.cltech.securitynt.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de sesiones activas para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @since 30/11/2018
 * @see Creacion
 */
@Service
public class SessionServiceSecurityNT implements SessionService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SessionDao sessionDao;

    @Override
    public List<AuthenticationSession> list() throws Exception
    {
        return sessionDao.list();
    }

    @Override
    public AuthenticationSession create(AuthenticationSession authenticationSession) throws Exception
    {
        if (authenticationSession.getUser() == null)
        {
            User user = new User();
            user.setId(JWT.decode(request).getId());
            authenticationSession.setUser(user);
        }
        deleteBySession(authenticationSession, false);
        return sessionDao.create(authenticationSession);
    }

    @Override
    public int deleteBySession(AuthenticationSession idSession, boolean deleteWs) throws Exception
    {
        int resp = sessionDao.deleteBySession(idSession.getIdSession());
        return resp;
    }

    @Override
    public void deleteAll() throws Exception
    {
        sessionDao.deleteAll();
        System.out.println("Se eliminaron las sesiones activas");
    }

}
