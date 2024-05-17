package net.cltech.enterprisent.print_nt;

import com.google.gson.Gson;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.cltech.enterprisent.print_nt.bussines.HttpException;
import net.cltech.enterprisent.print_nt.bussines.PrintLogic;
import net.cltech.enterprisent.print_nt.bussines.connectors.TrayConnector;
import net.cltech.enterprisent.print_nt.bussines.domain.GeneratePrintConfiguration;
import net.cltech.enterprisent.print_nt.bussines.domain.Print;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintConfiguration;
import net.cltech.enterprisent.print_nt.bussines.persistence.NTNodePersistence;
import net.cltech.enterprisent.print_nt.bussines.persistence.NTPersistence;
import net.cltech.enterprisent.print_nt.bussines.socket.TestEndpoint;
import net.cltech.enterprisent.print_nt.bussines.storage.Storage;
import net.cltech.enterprisent.print_nt.forms.FrmConfiguration;
import net.cltech.enterprisent.print_nt.forms.FrmLog;
import net.cltech.enterprisent.print_nt.forms.FrmMain;
import net.cltech.enterprisent.print_nt.tools.Cache;
import net.cltech.enterprisent.print_nt.tools.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Inicio aplicación.
 *
 * @author equijano
 * @since 17/01/2019
 * @see creacion
 * @version 1.0
 */
public class App implements TrayConnector
{

    public static final String VERSION = "2.0";
    public static final String APPLICATION_TITLE = "Aplicacion para imprimir." + VERSION;
    private TrayIcon trayIcon;
    private TestEndpoint testEndpoint;
    private Gson gson;
    private FrmMain main;
    private NTPersistence backend;
    private PrintLogic printLogic;
    private boolean conect;
    private static PrintConfiguration printConfiguration;
    private static Scheduler scheduler;
    private final Integer minutes = 1;
    private Integer countJob = 0;
    private MenuItem crearLogs = new MenuItem("Crear Logs");
    private PopupMenu menu = new PopupMenu();

    /**
     * Constructor
     */
    public App()
    {
        if (SystemTray.isSupported())
        {

            MenuItem mainMenu = new MenuItem("Ventana Principal");
            MenuItem configurarationMenu = new MenuItem("Configurar");
            MenuItem logMenu = new MenuItem("Log de Aplicación");
            MenuItem exitMenu = new MenuItem("Salir");

            crearLogs.setEnabled(false);

            mainMenu.addActionListener((ActionEvent e) ->
            {
                mainMenuActionPerformed(e);
            });

            configurarationMenu.addActionListener((ActionEvent e) ->
            {
                configurarationMenuActionPerformed(e);
            });

            logMenu.addActionListener((ActionEvent e) ->
            {
                logMenuActionPerformed(e);
            });

            exitMenu.addActionListener((ActionEvent e) ->
            {
                exitMenuActionPerformed(e);
            });

            crearLogs.addActionListener((ActionEvent e) ->
            {
                try
                {
                    crearLogs.setEnabled(false);
                    showMessage(creatLogsActionPerformed(e), TrayConnector.INFO_MESSAGE);
                } catch (IOException ex)
                {
                    Log.error("ERRORF : " + ex);
                }
                crearLogs.setEnabled(true);
            });

            menu.add(mainMenu);
            menu.add(configurarationMenu);
            menu.add(logMenu);
            menu.add(exitMenu);

            trayIcon = new TrayIcon(new ImageIcon(getClass().getResource("/image/iconoEclipseHis.png")).getImage(), APPLICATION_TITLE, menu);
            trayIcon.setImageAutoSize(true);
            trayIcon.getPopupMenu().getItem(0).setEnabled(true);
            trayIcon.getPopupMenu().getItem(1).setEnabled(true);
            trayIcon.getPopupMenu().getItem(2).setEnabled(true);
            trayIcon.getPopupMenu().getItem(3).setEnabled(true);
        } else
        {
            JOptionPane.showMessageDialog(null, "Tray Icons no soportado por su sistema operativo", App.APPLICATION_TITLE, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Accion al seleccionar la opcion de ventana principal
     *
     * @param evt
     */
    private void mainMenuActionPerformed(ActionEvent evt)
    {
        main = new FrmMain(this);
        main.changeState(conect);
    }

    /**
     * Accion al seleccionar la opcion de Configuracion de la aplicación
     *
     * @param evt
     */
    private void configurarationMenuActionPerformed(ActionEvent evt)
    {
        FrmConfiguration configuration = new FrmConfiguration();
        //OPCION PARA CREAR LOGS DE TODAS LA SEDES
        try
        {
            Properties properties = Storage.loadConfigurationCredentials();
            if ((Integer.parseInt(properties.getProperty(Storage.CREATEFILEBYEMAIL)) == 2))
            {
                menu.add(crearLogs);
                crearLogs.setEnabled(true);

            }
        } catch (IOException ex)
        {
            Log.error("ERRORF : " + ex);
        }

    }

    /**
     * Accion al seleccionar la opcion de Log
     *
     * @param evt
     */
    private void logMenuActionPerformed(ActionEvent evt)
    {
        FrmLog log = new FrmLog();
    }

    /**
     * Accion al seleccionar la opcion de salir
     *
     * @param evt
     */
    private void exitMenuActionPerformed(ActionEvent evt)
    {
        if (JOptionPane.showConfirmDialog(null, "¿Esta seguro de cerrar la aplicación?", App.APPLICATION_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }

    /**
     * Accion para crear logs todas las sedes
     *
     * @param evt
     */
    private String creatLogsActionPerformed(ActionEvent evt) throws IOException
    {
        return Log.createLogsBranch();
    }

    /**
     * Inicio de la aplicación
     *
     * @throws org.quartz.SchedulerException
     */
    public void start() throws SchedulerException, URISyntaxException
    {
        try
        {
            SystemTray.getSystemTray().add(trayIcon);
            main = new FrmMain(this);
            gson = new Gson();
            showMessage("Se inicio la aplicación para imprimir", TrayConnector.INFO_MESSAGE);
            configuration();

            Timer timer = new Timer(300000, new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    Properties properties;
                    try
                    {
                        properties = Storage.loadConfigurationCredentials();
                        testEndpoint.sendMessage("{\"receivers\":[],\"serial\":\"" + properties.getProperty(Storage.SERIAL).trim() + "\",\"type\":2,\"message\":\"Sigo aqui\"}");
                    } catch (IOException ex)
                    {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    showMessage("Verificacion de la conexion realizada.", TrayConnector.INFO_MESSAGE);
                }
            });

            timer.start();

        } catch (AWTException ex)
        {
            Log.error(ex);
        }
    }

    /**
     * Ajustar la configuracion de la aplicación y conectarse al websocket
     *
     * @throws org.quartz.SchedulerException
     */
    public void configuration() throws SchedulerException, URISyntaxException
    {
        try
        {
            Properties properties = Storage.loadConfigurationCredentials();
            if (properties.getProperty(Storage.SERVERBACKEND) == null || properties.getProperty(Storage.SERVERBACKEND).equals(""))
            {
                showMessage("No se ha configurado el servidor", TrayConnector.ERROR_MESSAGE);
                conect = false;
                main.changeState(conect);
            } else
            {
                backend = new NTPersistence(properties.getProperty(Storage.SERVERBACKEND));
                GeneratePrintConfiguration generate = new GeneratePrintConfiguration();
                boolean generateSerial = (properties.getProperty(Storage.SERIAL) == null || properties.getProperty(Storage.SERIAL).equals(""));
                generate.setSerial(generateSerial);
                generate.setUrlBackend(properties.getProperty(Storage.SERVERBACKEND));
                PrintConfiguration printConfiguration = backend.generateConfig(gson.toJson(generate));
                String serial = properties.getProperty(Storage.SERIAL);
                if (generateSerial)
                {
                    serial = printConfiguration.getSerial();
                }
//                printConfiguration.setUrlNode(".");
                Storage.saveConfigurationCredentials(properties.getProperty(Storage.SERVERBACKEND), printConfiguration.getUrlNode(), serial, printConfiguration.getUrlWS(), properties.getProperty(Storage.PRINTERLABEL), properties.getProperty(Storage.PRINTERREPORT), properties.getProperty(Storage.DIRECTORY));
                App.printConfiguration = printConfiguration;

                startWs(printConfiguration);

            }
        } catch (IOException | HttpException ex)
        {
            showMessage("No se ha podido conectar al servidor", TrayConnector.ERROR_MESSAGE);
            conect = false;
            main.changeState(conect);
            Log.error(ex);
        }
    }

    /**
     * Conectarse el websocket
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    private void startWs(PrintConfiguration printConfiguration) throws IOException, URISyntaxException
    {

        Properties properties = Storage.loadConfigurationCredentials();
        printLogic = new PrintLogic(new NTNodePersistence(properties.getProperty(Storage.SERVERNODEJS)), this, backend, printConfiguration, main);

        if (validatedConfig(properties))
        {

            String ws = properties.getProperty(Storage.WS).trim();
            testEndpoint = new TestEndpoint(new URI(ws), (String message) ->
            {

                Print print = gson.fromJson(message, Print.class);
                try
                {
                    main.changeCard("card4");
                    main.changeStateLoading(true);
                    printLogic.print(print.getMessage(), print.getTypePrinter());
                    main.changeOrderNumber("");
                    main.changeStateLoading(false);
                    main.changeCard("card3");

                } catch (Exception ex)
                {

                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            if (testEndpoint.getUserSession() != null)
            {

                testEndpoint.sendMessage("{\"receivers\":[],\"serial\":\"" + properties.getProperty(Storage.SERIAL).trim() + "\",\"type\":0,\"message\":\"\"}");
                showMessage("Se realizo la conexion al websocket", TrayConnector.INFO_MESSAGE);
                conect = true;
                Log.info("Se verifico la conexión correctamente");
                main.changeState(conect);
            } else
            {
                showMessage("No se pudo conectar al websocket", TrayConnector.ERROR_MESSAGE);
                conect = false;
                Log.info("Error -> No se pudo verificar la conexion de manera correcta");
                main.changeState(conect);
            }
        } else
        {
            showMessage("No se han configurado la aplicación", TrayConnector.ERROR_MESSAGE);
        }
    }

    public void verifyConnection() throws SchedulerException, URISyntaxException
    {
        Log.info("INGRESA A VERIFICAR ");
        if (testEndpoint != null)
        {
            try
            {
                testEndpoint.getUserSession().close();
            } catch (IOException ex)
            {
                Log.info("Error -> No se pudo verificar la conexion de manera correcta");
            }
        }

        try
        {
            Properties properties = Storage.loadConfigurationCredentials();
            testEndpoint.sendMessage("{\"receivers\":[],\"serial\":\"" + properties.getProperty(Storage.SERIAL).trim() + "\",\"type\":0,\"message\":\"\"}");
            showMessage("Verificacion de la conexion realizada.", TrayConnector.INFO_MESSAGE);
        } catch (IOException ex)
        {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Validar configuracion de la aplicación
     *
     * @param properties
     * @return boolean
     */
    private boolean validatedConfig(Properties properties)
    {
        boolean aux = true;
        if (properties.getProperty(Storage.SERIAL) == null || properties.getProperty(Storage.SERIAL).equals(""))
        {
            aux = false;
        }
        if (properties.getProperty(Storage.SERVERBACKEND) == null || properties.getProperty(Storage.SERVERBACKEND).equals(""))
        {
            aux = false;
        }
        if (properties.getProperty(Storage.SERVERNODEJS) == null || properties.getProperty(Storage.SERVERNODEJS).equals(""))
        {
            aux = false;
        }
        if (properties.getProperty(Storage.WS) == null || properties.getProperty(Storage.WS).equals(""))
        {
            aux = false;
        }
        return aux;
    }

    /**
     *
     * @param args
     * @throws org.quartz.SchedulerException
     */
    public static void main(String[] args) throws SchedulerException, URISyntaxException
    {
        Log.info("App Iniciada");
        App.nimbusLookAndFeel();
        App app = new App();
        Cache.setTrayConnector(app);
        app.start();
    }

    /**
     * Metodo para mostrar mensajes al usuario.
     *
     * @param message
     * @param type
     */
    @Override
    public void showMessage(String message, TrayIcon.MessageType type)
    {
        trayIcon.displayMessage(App.APPLICATION_TITLE, message, type);
    }

    /**
     * Establece valores del look and feel
     */
    public static void nimbusLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            JFrame.setDefaultLookAndFeelDecorated(false);
            UIManager.put("PopupMenu.consumeEventOnClose", "false");
            UIManager.put("PopupMenu.opaque", "false");
            UIManager.put("TaskPaneGroup.titleBackgroundGradientStart", new java.awt.Color(153, 184, 215));
            UIManager.put("TaskPaneGroup.titleBackgroundGradientEnd", new java.awt.Color(225, 234, 243));
            UIManager.put("TaskPaneGroup.titleForeground", new java.awt.Color(0, 0, 0));
            UIManager.put("TaskPaneGroup.opaque", Boolean.FALSE);
            UIManager.put("TextField.inactiveBackground", new javax.swing.plaf.ColorUIResource(Color.BLUE));
            UIManager.put("TextField.disabledBackground", new javax.swing.plaf.ColorUIResource(Color.BLUE));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            Log.error(ex);
        }
    }

    public static class ConnectionCheck implements Job
    {

        private final App appSocket = new App();

        @Override
        public void execute(JobExecutionContext jec) throws JobExecutionException
        {
            try
            {
                appSocket.verifyConnection();
            } catch (SchedulerException ex)
            {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex)
            {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
