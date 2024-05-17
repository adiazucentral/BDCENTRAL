package net.cltech.enterprisent.start.jobs;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.tools.log.jobs.SchedulerLog;
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
public class JobRestartSequence implements Job
{

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        try
        {
            configurationService.restartSequence();
            configurationService.restartSequenceRecalled();
            configurationService.restartSequenceAppointment();
            configurationService.restartSequencePathology();
            SchedulerLog.info("Job: Se reinicio el contador de secuencia " + new Date());
        } catch (Exception ex)
        {
            Logger.getLogger(JobRestartSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
