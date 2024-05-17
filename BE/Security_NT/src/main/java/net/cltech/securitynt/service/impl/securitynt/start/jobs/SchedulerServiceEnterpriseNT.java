/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.service.impl.securitynt.start.jobs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.securitynt.config.AutowiringSpringBeanJobFactory;
import net.cltech.securitynt.service.interfaces.start.jobs.SchedulerService;
import net.cltech.securitynt.start.jobs.JobDeactivateUsers;
import net.cltech.securitynt.tools.log.jobs.SchedulerLog;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Service;

/**
 * Clase de configuracion general del Quartz para tareas programadas
 *
 * @version 1.0.0
 * @author equijano
 * @since 04/12/2018
 * @see Creacion
 */
@Service
public class SchedulerServiceEnterpriseNT implements SchedulerService
{

    @Autowired
    private ApplicationContext applicationContext;
    private static final String TRIGGER_NAME = "Qrtz_Trigger_Restart_Order";
    private static final String JOB_NAME = "Qrtz_Job_Restart_Order";
    private static Scheduler scheduler;

    private SpringBeanJobFactory springBeanJobFactory()
    {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        SchedulerLog.info("Job: Configurando Job factory para desactivar usuarios");
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    private void scheduler(Trigger trigger, JobDetail job) throws SchedulerException, IOException
    {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        factory.initialize(new ClassPathResource("quartz.properties").getInputStream());
        SchedulerLog.info("Job: Asignar identificador para el programador - Desactivar usuarios");
        scheduler = factory.getScheduler();
        scheduler.setJobFactory(springBeanJobFactory());
        scheduler.scheduleJob(job, trigger);
        SchedulerLog.info("Job: Iniciar hilo de la tarea programada - Desactivar usuarios");
        scheduler.start();
    }

    private JobDetail jobDetail()
    {
        return newJob()
                .ofType(JobDeactivateUsers.class)
                .storeDurably()
                .withIdentity(JobKey.jobKey(JOB_NAME))
                .build();
    }

    private Trigger trigger(JobDetail job, String hour)
    {
        String minutes = hour.substring(2, 4);
        String hours = hour.substring(0, 2);
        SchedulerLog.info("Job: Configurando trigger para disparar cada HHMM: " + hour);
        return newTrigger()
                .forJob(job)
                .withIdentity(TriggerKey.triggerKey(TRIGGER_NAME))
                .withSchedule(dailyAtHourAndMinute(Integer.valueOf(hours), Integer.valueOf(minutes)))
                .build();
    }

    @Override
    public void createThread(String hour) throws SchedulerException, IOException
    {
        if (hour.isEmpty() != true && hour.equals("") == false)
        {
            try
            {
                SchedulerLog.info("Job: Se inicio el servicio de programador de tareas - Desactivar usuarios ");
                shutdown();
                Thread.sleep(1000);
                JobDetail job = jobDetail();
                scheduler(trigger(job, hour), job);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(SchedulerServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void shutdown()
    {
        try
        {
            if (scheduler != null)
            {
                scheduler.shutdown();
            }
        } catch (SchedulerException e)
        {
            SchedulerLog.info("Job: El Programador no se cerró limpiamente - Desactivar usuarios : " + e.toString());
        }
        SchedulerLog.info("Job: El Programador se cerró de manera exitosa - Desactivar usuarios.");
    }

}
