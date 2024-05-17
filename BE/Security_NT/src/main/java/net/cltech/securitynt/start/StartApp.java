/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.start;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import net.cltech.securitynt.service.interfaces.start.StartAppService;
import net.cltech.securitynt.service.interfaces.start.jobs.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author dcortes
 */
public class StartApp
{

    @Autowired
    public StartAppService service;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private SchedulerService schedulerService;
    public static final Logger logger = Logger.getLogger("SecurityNT - App");

    @PostConstruct
    public void execScript()
    {
        try
        {
            String appContext = servletContext.getRealPath("");
            appContext = appContext.startsWith("/") ? appContext.substring(1).trim() : appContext;
            System.out.println("Context: " + appContext);
            String uriEngine = appContext + "/WEB-INF/engine.properties";
            Properties properties = new Properties();
            FileReader reader = null;
            String engine = StartAppService.SQL_SERVER;
            try
            {
                reader = new FileReader(uriEngine);
                properties.load(reader);
                engine = properties.getProperty("engine");
            } catch (IOException ex)
            {
                ex.getMessage();
            } finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    } catch (IOException ex)
                    {
                        ex.getMessage();
                    }
                }
            }
            service.execStartScript(engine);
            service.execStartScriptData(engine);
            service.execStartScriptDocs(engine);
            System.out.println("entro a crear");
            schedulerService.createThread("0000");
        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "[SecurityNT - App] : Error ejecutando el script de actualizacion", ex);
        }
    }

    @PreDestroy
    private void shutdown()
    {
        schedulerService.shutdown();
    }

}
