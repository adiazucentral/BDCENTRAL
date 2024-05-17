package net.cltech.enterprisent.start.jobs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.start.StartAppService;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tarea programada para renombrar todas las tablas de operación
 * sus llaves primarias e index de Estadistica
 * 
 * @version 1.0.0
 * @author javila
 * @since 23/06/2021
 * @see Creación
 */
public class JobRenameOperationTablesStat implements Job
{
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    public StartAppService service;
    @Autowired
    private ServletContext servletContext;
    
    @Override
    public void execute(JobExecutionContext job) throws JobExecutionException
    {
        try
        {
            configurationService.renameOperationTablesStat();
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

                if (engine.equals("sqlserver"))
                {
                    ISOLATION_READ_UNCOMMITTED = "SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; ";
                }
            }
            catch (IOException ex)
            {
                ex.getMessage();
            }
            finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (IOException ex)
                    {
                        ex.getMessage();
                    }
                }
            }
            service.execStartScript(engine);
        }
        catch (Exception e)
        {
            Logger.getLogger(JobTableCleaning.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
