/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.start.jobs;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.securitynt.domain.masters.user.User;
import net.cltech.securitynt.domain.masters.demographic.DemographicWebQuery;
import net.cltech.securitynt.service.interfaces.integration.IntegrationService;
import net.cltech.securitynt.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.securitynt.service.interfaces.masters.demographic.DemographicWebQueryService;
import net.cltech.securitynt.service.interfaces.masters.user.UserService;
import net.cltech.securitynt.tools.Constants;
import net.cltech.securitynt.tools.JWT;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tarea programada para reinicio de secuencia del numero de orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 04/12/2018
 * @see Creación
 */
public class JobDeactivateUsers implements Job
{

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private UserService userService;
    @Autowired
    private DemographicWebQueryService demographicWebQueryService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        try
        {
            String tokenExpiration = configurationService.get(Constants.TOKEN_EXPIRATION_TIME).getValue();
            String token = JWT.generate(userService.authenticate("lismanager", "cltechmanager", null), tokenExpiration.equals("") ? 60 : Integer.parseInt(tokenExpiration), Constants.TOKEN_AUTH_USER);
            
            userService.deactivateUsers(integrationService.getList(User.class, configurationService.get("UrlLIS").getValue() + "/api/users/listdeactivate", token), token);
            userService.updateCountFail();
            
            demographicWebQueryService.deactivateUsersDemographicwebquery(integrationService.getList(DemographicWebQuery.class, configurationService.get("UrlLIS").getValue() + "/api/demographicwebquery/listdeactivate", token), token);
            userService.updateCountFailWeb();
        } catch (Exception ex)
        {
            Logger.getLogger(JobDeactivateUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
