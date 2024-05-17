/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.tools.log;

import java.util.logging.Level;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Representa el log para la integracion con el aplicativo de seguridad
 *
 * @version 1.0.0
 * @author equijano
 * @since 17/10/2019
 * @see Creación
 */
public class SecurityLog
{

    private static Logger logger;
    private static final String project = "EnterpriseNT";
    private static String folder;

    private static Logger getLogger()
    {
        if (logger == null)
        {
            try
            {
                folder = SecurityLog.class.getSimpleName();
                logger = LogManager.getLogger(SecurityLog.class);
                logger.setLevel(org.apache.log4j.Level.TRACE);
                PatternLayout l = new PatternLayout();
                l.setConversionPattern("[%-5p %d{dd MMM yyyy - HH:mm:ss}] %c{2} - %m%n");
                DailyRollingFileAppender appender = new DailyRollingFileAppender();
                appender.setLayout(l);
                StringBuilder route = new StringBuilder();
                route.append(System.getProperty("user.dir"));
                route.append(System.getProperty("file.separator"));
                route.append(project);
                route.append(System.getProperty("file.separator"));
                route.append("logs");
                route.append(System.getProperty("file.separator"));
                route.append(folder.substring(0, folder.length() - 3));
                route.append(System.getProperty("file.separator"));
                route.append("Log.txt");
                appender.setFile(route.toString());
                appender.setDatePattern("'.'yyyy-MM-dd");
                appender.setAppend(true);
                appender.setImmediateFlush(true);
                appender.activateOptions();
                logger.addAppender(appender);
            } catch (Exception e)
            {
                java.util.logging.Logger.getLogger(SecurityLog.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return logger;
    }

    public static void error(Throwable ex)
    {
        System.err.println("Se registro un error en " + SecurityLog.class.getSimpleName());
        getLogger().error(ex.getMessage(), ex);
    }

    public static void error(String msj)
    {
        System.err.println("Se registro un error en " + SecurityLog.class.getSimpleName());
        getLogger().error(msj);
    }

    public static void info(String msj)
    {
        getLogger().info(msj);
    }
}
