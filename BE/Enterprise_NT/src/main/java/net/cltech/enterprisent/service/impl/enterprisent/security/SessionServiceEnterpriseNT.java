/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.common.AuthenticationSession;
import net.cltech.enterprisent.domain.common.AuthenticationUser;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.security.SessionService;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.websocket.ChatHandler;
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
public class SessionServiceEnterpriseNT implements SessionService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ChatHandler chatHandler;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationService integrationService;

    @Override
    public List<AuthenticationSession> list() throws Exception
    {
        final String url = configurationService.getValue("UrlSecurity") + "/api/sessionviewer";
        return integrationService.getList(AuthenticationSession.class, url);
    }

    @Override
    public AuthenticationSession create(AuthenticationSession authenticationSession) throws Exception
    {
        final String url = configurationService.getValue("UrlSecurity") + "/api/sessionviewer/create";
        return integrationService.post(Tools.jsonObject(authenticationSession), AuthenticationSession.class, url);
    }

    @Override
    public int deleteBySession(AuthenticationSession idSession, boolean deleteWs) throws Exception
    {
        final String url = configurationService.getValue("UrlSecurity") + "/api/sessionviewer/deleteBySession";
        Integer response = integrationService.post(Tools.jsonObject(idSession), Integer.class, url);
        if (deleteWs)
        {
            chatHandler.deleteSessionById(idSession.getIdSession(), false);
        }
        return response == null ? 0 : response;
    }

    @Override
    public void deleteAll() throws Exception
    {
        final String url = configurationService.getValue("UrlSecurity") + "/api/sessionviewer";
        String token = "";
        try
        {
            token = request.getHeader("Authorization");
        } catch (Exception e)
        {
            AuthenticationUser user = new AuthenticationUser();
            user.setUser("lismanager");
            user.setPassword("cltechmanager");
            user.setBranch(null);
            token = userService.authenticate(user).getToken();
        }
        integrationService.delete(url, token);
        chatHandler.deleteAll();
    }

}
