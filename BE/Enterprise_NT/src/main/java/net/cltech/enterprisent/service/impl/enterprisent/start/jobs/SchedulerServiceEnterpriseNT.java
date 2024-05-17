package net.cltech.enterprisent.service.impl.enterprisent.start.jobs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.config.AutowiringSpringBeanJobFactory;
import net.cltech.enterprisent.service.interfaces.start.jobs.SchedulerService;
import net.cltech.enterprisent.start.jobs.JobRenameOperationTables;
import net.cltech.enterprisent.start.jobs.JobRenameOperationTablesStat;
import net.cltech.enterprisent.start.jobs.JobRestartSequence;
import net.cltech.enterprisent.start.jobs.JobTableCleaning;
import net.cltech.enterprisent.tools.log.jobs.SchedulerLog;
import org.quartz.CronScheduleBuilder;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import org.quartz.JobBuilder;
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
    // Constantes para la limpieza de la tabla de excepciones:
    private static final String JOB_NAME_CLEAN_TABLE = "Qrtz_Job_Clean_Table";
    private static final String TRIGGER_NAME_CLEAN_TABLE = "Qrtz_Trigger_Clean_Table";
    // Constantes para el renombre de las tablas de operacion anualmente:
    private static final String JOB_RENAME_OPERATION_TABLES = "Qrtz_Job_Rename_Operation_Tables";
    private static final String TRIGGER_RENAME_OPERATION_TABLES = "Qrtz_Trigger_Rename_Operation_Tables";
    // Constantes para el renombre de las tablas de operacion anualmente (Estadistica):
    private static final String JOB_RENAME_OPERATION_TABLES_STAT = "Qrtz_Job_Rename_Operation_Tables_Stat";
    private static final String TRIGGER_RENAME_OPERATION_TABLES_STAT = "Qrtz_Trigger_Rename_Operation_Tables_Stat";
    private static Scheduler scheduler;
    private static Scheduler schedulerToTableCleaning;
    private static Scheduler schToRenameOperationTables;

    private SpringBeanJobFactory springBeanJobFactory()
    {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        SchedulerLog.info("Job: Configurando Job factory para reiniciar el numero de ordenes");
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    private void scheduler(Trigger trigger, JobDetail job) throws SchedulerException, IOException
    {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        factory.initialize(new ClassPathResource("quartz.properties").getInputStream());
        SchedulerLog.info("Job: Asignar identificador para el programador - Reinicio del numero de ordenes");
        scheduler = factory.getScheduler();
        scheduler.setJobFactory(springBeanJobFactory());
        scheduler.scheduleJob(job, trigger);
        SchedulerLog.info("Job: Iniciar hilo de la tarea programada - Reinicio del numero de ordenes");
        scheduler.start();
    }

    private JobDetail jobDetail()
    {
        return newJob()
                .ofType(JobRestartSequence.class)
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
        SchedulerLog.info("Hours restart order : " + hour);
        if (hour != null && !hour.isEmpty())
        {
            try
            {
                SchedulerLog.info("Job: Se inicio el servicio de programador de tareas - Reinicio del numero de ordenes ");
                shutdown();
                Thread.sleep(1000);
                JobDetail job = jobDetail();
                scheduler(trigger(job, hour), job);
            }
            catch (InterruptedException ex)
            {
                SchedulerLog.info("Error restart order");
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
        }
        catch (SchedulerException e)
        {
            SchedulerLog.info("Job: El Programador no se cerró limpiamente - Reinicio del numero de ordenes : " + e.toString());
        }
        SchedulerLog.info("Job: El Programador se cerró de manera exitosa - Reinicio del numero de ordenes.");
    }

    /**
     * Este constructor es igual a springBeanJobFactory a excepción de que este
     * no registra en el log de SchedulerLog
     *
     * @return
     */
    private SpringBeanJobFactory springBeanJobFactoryAll()
    {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    private JobDetail jobDetailToTableCleaning()
    {
        return JobBuilder
                .newJob(JobTableCleaning.class)
                .storeDurably()
                .withIdentity(JobKey.jobKey(JOB_NAME_CLEAN_TABLE))
                .build();
    }

    private Trigger triggerToTableCleaning()
    {
        SchedulerLog.info("Job: Configurando trigger para disparar cada Mes la limpieza de la tabla de excepciones");
        return newTrigger()
                .withIdentity(TriggerKey.triggerKey(TRIGGER_NAME_CLEAN_TABLE))
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 59 23 L * ?"))
                .build();
    }

    @Override
    public void jobTableCleaning() throws SchedulerException, IOException
    {
        SchedulerLog.info("Cleaning exceptions table");
        try
        {
            SchedulerLog.info("Job: Limpieza de la tabla de excepciones");
            Thread.sleep(1000);
            JobDetail job = jobDetailToTableCleaning();
            Trigger trigger = triggerToTableCleaning();
            schedulerToTableCleaning(trigger, job);
        }
        catch (InterruptedException ex)
        {
            SchedulerLog.info("Error cleaning exceptions table");
            Logger.getLogger(SchedulerServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void schedulerToTableCleaning(Trigger trigger, JobDetail job) throws SchedulerException, IOException
    {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        factory.initialize(new ClassPathResource("quartz.properties").getInputStream());
        SchedulerLog.info("Job: Asignar identificador para el programador - Limpieza de la tabla que guarda las excepciones que se generan en el API");
        schedulerToTableCleaning = factory.getScheduler();
        schedulerToTableCleaning.setJobFactory(springBeanJobFactoryAll());
        schedulerToTableCleaning.scheduleJob(job, trigger);
        SchedulerLog.info("Job: Iniciar hilo de la tarea programada - Limpieza de la tabla de excepciones");
        schedulerToTableCleaning.start();
    }
    
    /**
     * Este constructor es igual a springBeanJobFactory 
     * pero este tiene la particularidad de que podemos enviar el mensaje 
     * que queremos que se escriba en el LOG de SchedulerLog como parametro
     *
     * @param message Mensaje a escribir en el Log
     */
    private SpringBeanJobFactory springBeanJobFactory(String message)
    {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        SchedulerLog.info(message);
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
    
    private Trigger generateTrigger(String message, String triggerKey, String cronFormat)
    {
        SchedulerLog.info(message);
        return newTrigger()
                .withIdentity(TriggerKey.triggerKey(triggerKey))
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronFormat))
                .build();
    }
    
    private JobDetail jobToRenameOperationTablesByYear()
    {
        return JobBuilder
                .newJob(JobRenameOperationTables.class)
                .storeDurably()
                .withIdentity(JobKey.jobKey(JOB_RENAME_OPERATION_TABLES))
                .build();
    }
    
    @Override
    public void jobRenameOperationTablesByYear() throws SchedulerException, IOException
    {
        SchedulerLog.info("Executing the task of renaming all operation tables");
        try
        {
            SchedulerLog.info("Job: Renombre de todas las tablas de operación de NT");
            Thread.sleep(1000);
            // Generar Job:
            JobDetail job = jobToRenameOperationTablesByYear();
            // Generar Trigger:
            String msgTrigger = "Job: Configurando trigger para la ejecución anual del renombre de las tablas de operacion y sus constrains";
            String triggerKey = TRIGGER_RENAME_OPERATION_TABLES;
            String cronFormat = "0 0 0 1 1 ? *";
            Trigger trigger = generateTrigger(msgTrigger, triggerKey, cronFormat);
            
            String message1 = "Job: Asignar identificador para el programador - Renombre de las tablas de operacion de NT";
            String message2 = "Job: Configurando Job factory para el renombre de tablas de operación de NT al inicio de cada año";
            String message3 = "Job: Iniciar hilo de la tarea programada - Renombre de todas las tablas de operación";
            generalSchedulerExecution(trigger, job, message1, message2, message3);
            //---- Ejecución de la otra tarea programada que se encargara de renombrar las tablas de operacion en la BD de estadistica:
            jobRenameOperationTablesStat();
        }
        catch (InterruptedException ex)
        {
            SchedulerLog.info("Error executing the task of renaming all operation tables");
            Logger.getLogger(SchedulerServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generalSchedulerExecution(Trigger trigger, JobDetail job, String message1, String message2, String message3) throws SchedulerException, IOException
    {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        factory.initialize(new ClassPathResource("quartz.properties").getInputStream());
        SchedulerLog.info(message1);
        schToRenameOperationTables = factory.getScheduler();
        schToRenameOperationTables.setJobFactory(springBeanJobFactory(message2));
        schToRenameOperationTables.scheduleJob(job, trigger);
        SchedulerLog.info(message3);
        schToRenameOperationTables.start();
    }
    
    private JobDetail jobToRenameOperationTablesStat()
    {
        return JobBuilder
                .newJob(JobRenameOperationTablesStat.class)
                .storeDurably()
                .withIdentity(JobKey.jobKey(JOB_RENAME_OPERATION_TABLES_STAT))
                .build();
    }
    
    private void jobRenameOperationTablesStat() throws SchedulerException, IOException
    {
        SchedulerLog.info("Executing the task of renaming all operation tables (statistics)");
        try
        {
            SchedulerLog.info("Job: Renombre de todas las tablas de operación de NT - Estadistica");
            Thread.sleep(1000);
            // Generar Job:
            JobDetail job = jobToRenameOperationTablesStat();
            // Generar Trigger:
            String msgTrigger = "Job: Configurando trigger para la ejecución anual del renombre de las tablas de operacion y sus constrains (Estadistica)";
            String triggerKey = TRIGGER_RENAME_OPERATION_TABLES_STAT;
            String cronFormat = "0 0 0 1 1 ? *";
            Trigger trigger = generateTrigger(msgTrigger, triggerKey, cronFormat);
            
            String message1 = "Job: Asignar identificador para el programador - Renombre de las tablas de operacion de NT - Estadistica";
            String message2 = "Job: Configurando Job factory para el renombre de tablas de operación de NT al inicio de cada año (Estadistica)";
            String message3 = "Job: Iniciar hilo de la tarea programada - Renombre de todas las tablas de operación (Estadistica)";
            generalSchedulerExecution(trigger, job, message1, message2, message3);
        }
        catch (InterruptedException ex)
        {
            SchedulerLog.info("Error executing the task of renaming all operation tables");
            Logger.getLogger(SchedulerServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
