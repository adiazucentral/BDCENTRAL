package net.cltech.enterprisent.tools.log.integration;

import java.io.File;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Representa el Log para la integración con un API de facturación externa
*
* @version 1.0.0
* @author Julian
* @since 14/07/2021
* @see Creación
*/
public class ExternalBillingLog
{
    private static Logger logger;
    private static Logger getLogger()
    {
        if (logger == null)
        {
            try
            {
                File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "logs\\EnterpriseNT\\ExternalBilling");
                if (!file.exists())
                {
                    file.mkdir();
                }
                
                System.setProperty("ProyectName", "EnterpriseNT");
                logger = LogManager.getLogger("ExternalBillingfile");
                
            } catch (Exception e)
            {
                java.util.logging.Logger.getLogger(ExternalBillingLog.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return logger;
    }

    public static void error(Throwable ex)
    {
        System.err.println("Error " + ex);
        System.err.println("Se registro un error en " + ExternalBillingLog.class.getSimpleName());
        getLogger().error(ex.getMessage(), ex);
    }

    public static void error(String msj)
    {
        System.err.println("Message " + msj);
        System.err.println("Se registro un error en " + ExternalBillingLog.class.getSimpleName());
        getLogger().error(msj);
    }

    public static void info(String msj)
    {
        getLogger().info(msj);
    }
}