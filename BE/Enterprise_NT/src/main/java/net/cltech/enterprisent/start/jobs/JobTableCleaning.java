package net.cltech.enterprisent.start.jobs;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tarea programada para la limpieza de tablas
 * 
 * @version 1.0.0
 * @author JAVILA
 * @since 04/12/2020
 * @see Creación
 */
public class JobTableCleaning implements Job
{
    @Autowired
    private ConfigurationService configurationService;
    
    @Override
    public void execute(JobExecutionContext job) throws JobExecutionException
    {
        try
        {
            configurationService.tableToTruncate("lab151");
        }
        catch (Exception e)
        {
            Logger.getLogger(JobTableCleaning.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
