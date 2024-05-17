package net.cltech.enterprisent.tools.log.integration;

import java.io.File;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Representa el log para la integracion con el middleware
 *
 * @version 1.0.0
 * @author equijano
 * @since 29/11/2018
 * @see Creación
 */
public class MiddlewareLog
{

    private static Logger logger;
    private static final String project = "EnterpriseNT";

    private static Logger getLogger()
    {
        if (logger == null)
        {
            try
            {
                File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "logs\\EnterpriseNT\\Middleware");
                if (!file.exists())
                {
                    file.mkdir();
                }

                System.setProperty("ProyectName", "EnterpriseNT");
                logger = LogManager.getLogger("Middlewarefile");

            } catch (Exception e)
            {
                java.util.logging.Logger.getLogger(MiddlewareLog.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return logger;
    }

    public static void error(Throwable ex)
    {
        System.err.println("Error " + ex);
        System.err.println("Se registro un error en " + MiddlewareLog.class.getSimpleName());
        getLogger().error(ex.getMessage(), ex);
    }

    public static void error(String msj)
    {
        System.err.println("Message " + msj);
        System.err.println("Se registro un error en " + MiddlewareLog.class.getSimpleName());
        getLogger().error(msj);
    }

    public static void info(String msj)
    {
        getLogger().info(msj);
    }
}
