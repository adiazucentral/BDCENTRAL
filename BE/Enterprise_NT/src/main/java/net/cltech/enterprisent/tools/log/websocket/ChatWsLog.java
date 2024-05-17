/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools.log.websocket;

import java.util.logging.Level;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Representa el log para la utilizacion del chat
 *
 * @version 1.0.0
 * @author equijano
 * @since 03/12/2018
 * @see Creación
 */
public class ChatWsLog
{
    private static Logger logger;
    private static Logger getLogger()
    {
        if (logger == null)
        {
            try
            {
                File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "logs\\EnterpriseNT\\ChatWs");
                if (!file.exists())
                {
                    file.mkdir();
                }
                
                System.setProperty("ProyectName", "EnterpriseNT");
                logger = LogManager.getLogger("ChatWsfile");
                
            } catch (Exception e)
            {
                java.util.logging.Logger.getLogger(ChatWsLog.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return logger;
    }

    public static void error(Throwable ex)
    {
        System.err.println("Error " + ex);
        System.err.println("Se registro un error en " + ChatWsLog.class.getSimpleName());
        getLogger().error(ex.getMessage(), ex);
    }

    public static void error(String msj)
    {
        System.err.println("Message " + msj);
        System.err.println("Se registro un error en " + ChatWsLog.class.getSimpleName());
        getLogger().error(msj);
    }

    public static void info(String msj)
    {
        getLogger().info(msj);
    }
}