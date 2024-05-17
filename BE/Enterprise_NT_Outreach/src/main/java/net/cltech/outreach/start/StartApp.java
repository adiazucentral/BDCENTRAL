/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.start;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import net.cltech.outreach.service.interfaces.start.StartAppService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author dcortes
 */
public class StartApp
{

    @Autowired
    public StartAppService service;
    public static final Logger logger = Logger.getLogger("EnterpriseNT_Outreach - App");

    @PostConstruct
    public void execScript()
    {
        try
        {
            service.execStartScript(service.getDataBasaEngine());
            System.out.println("entro a crear");
        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "[EnterpriseNT_Outreach - App] : Error ejecutando el script de actualizacion", ex);
        }
    }

}
