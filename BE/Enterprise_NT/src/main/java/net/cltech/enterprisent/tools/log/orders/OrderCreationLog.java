package net.cltech.enterprisent.tools.log.orders;

import java.io.File;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Representa el log para llevar la trazabilidad de la creación de una orden
 *
 * @version 1.0.0
 * @author JAVILA
 * @since 05/11/2020
 * @see Creación
 */
public class OrderCreationLog
{
    private static Logger logger;
      
    private static Logger getLogger()
    {
        if (logger == null)
        {
            try
            {
                File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "logs\\EnterpriseNT\\OrderCreation");
                if (!file.exists())
                {
                    file.mkdir();
                }
                
                System.setProperty("ProyectName", "EnterpriseNT");
                logger = LogManager.getLogger("OrderCreationfile");
                
            } catch (Exception e)
            {
                java.util.logging.Logger.getLogger(OrderCreationLog.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return logger;
    }

    public static void error(Throwable ex)
    {
        System.err.println("Error " + ex);
        System.err.println("Se registro un error en " + OrderCreationLog.class.getSimpleName());
        getLogger().error(ex.getMessage(), ex);
    }

    public static void error(String msj)
    {
        System.err.println("Message " + msj);
        System.err.println("Se registro un error en " + OrderCreationLog.class.getSimpleName());
        getLogger().error(msj);
    }

    public static void info(String msj)
    {
        getLogger().info(msj);
    }
}