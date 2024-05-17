/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.print_nt.tools;

import net.cltech.enterprisent.print_nt.App;
import net.cltech.enterprisent.print_nt.bussines.connectors.TrayConnector;

/**
 *
 * @author dcortes
 */
public class Cache
{

    private static TrayConnector trayConnector;
    public static final String TIPO_ORDEN_CONSULTA_EXTERNA = "C";
    public static final String TIPO_ORDEN_HOSPITALIZADOS = "H";
       
    private static App appSocket;

    public static TrayConnector getTrayConnector()
    {
        return trayConnector;
    }

    public static void setTrayConnector(TrayConnector trayConnector)
    {
        Cache.trayConnector = trayConnector;
    }
    
    public static App getAppSocket() {
        return appSocket;
    }

    public static void setAppSocket(App appSocket) {
        Cache.appSocket = appSocket;
    }
    
}
