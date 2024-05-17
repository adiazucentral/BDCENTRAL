package net.cltech.enterprisent.print_nt.bussines.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import net.cltech.enterprisent.print_nt.tools.Tools;

/**
 *
 *
 * @version 1.0
 * @author equijano
 * @since 17/01/2019
 */
public class Storage
{

    /**
     * Guardar la url del backend de configuracion
     *
     * @param serverBackend
     * @param printerLabel
     * @param printerReport
     * @param directory
     * @throws IOException Error guardando el archivo
     */
    public static void saveConfigurationCredentials(String serverBackend, String printerLabel, String printerReport, String directory) throws IOException
    {

        FileWriter writer = null;
        File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + NAMEFILE + ".properties");
        try
        {
            Properties properties = loadConfigurationCredentials();
            properties.put(SERVERBACKEND, serverBackend);
            properties.put(SERVERNODEJS, properties.getProperty(SERVERNODEJS) == null ? "" : properties.getProperty(SERVERNODEJS));
            properties.put(SERIAL, properties.getProperty(SERIAL) == null ? "" : properties.getProperty(SERIAL));
            properties.put(WS, properties.getProperty(WS) == null ? "" : properties.getProperty(WS));
            properties.put(PRINTERREPORT, printerReport);
            properties.put(PRINTERLABEL, printerLabel);
            properties.put(PRINTERLABEL, printerLabel);
            properties.put(DIRECTORY, directory);

            int valorSaveForLog = Tools.createFileByEmail(serverBackend);
            if (valorSaveForLog >= 0)
            {
                properties.put(CREATEFILEBYEMAIL, String.valueOf(valorSaveForLog));
            }
            writer = new FileWriter(file);
            properties.store(writer, "Save configuration credentials");
        } finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }
    }

    /**
     * Guardar las credenciales de configuracion
     *
     * @param serverBackend
     * @param serverNodeJs
     * @param serial
     * @param ws
     * @param printerLabel
     * @param printerReport
     * @param directory
     * @throws IOException Error guardando el archivo
     */
    public static void saveConfigurationCredentials(String serverBackend, String serverNodeJs, String serial, String ws, String printerLabel, String printerReport, String directory) throws IOException
    {
        FileWriter writer = null;
        File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + NAMEFILE + ".properties");
        try
        {
            Properties properties = new Properties();
            properties.put(SERVERBACKEND, serverBackend);
            if (serverNodeJs != null)
            {
                properties.put(SERVERNODEJS, serverNodeJs);
            }
            int valorSaveForLog = Tools.createFileByEmail(serverBackend);

            if (valorSaveForLog >= 0)
            {
                properties.put(CREATEFILEBYEMAIL, String.valueOf(valorSaveForLog));
            }
            properties.put(SERIAL, serial);
            properties.put(WS, ws);
            properties.put(PRINTERREPORT, printerReport);
            properties.put(PRINTERLABEL, printerLabel);
            properties.put(DIRECTORY, directory);
            writer = new FileWriter(file);
            properties.store(writer, "Save configuration credentials");
        } finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }
    }

    /**
     * Carga las credenciales de configuracion
     *
     * @return Properties con las credenciales
     * @throws FileNotFoundException No existe el archivo db.properties
     * @throws IOException Error de lectura en el archivo
     */
    public static Properties loadConfigurationCredentials() throws FileNotFoundException, IOException
    {
        FileReader reader = null;
        File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + NAMEFILE + ".properties");
        Properties properties = new Properties();
        try
        {
            reader = new FileReader(file);
            properties.load(reader);
        } finally
        {
            if (reader != null)
            {
                reader.close();
            }
            return properties;
        }
    }

    public static boolean checkConfigurationCredentials() throws FileNotFoundException, IOException
    {
        return !loadConfigurationCredentials().isEmpty();
    }

    public static String SERVERNODEJS = "serverNodeJs";
    public static String SERVERBACKEND = "serverBackend";
    public static String SERIAL = "serial";
    public static String WS = "ws";
    public static String NAMEFILE = "configuration";
    public static String PRINTERREPORT = "printerReport";
    public static String PRINTERLABEL = "printerLabel";
    public static String DIRECTORY = "directory";
    public static String CREATEFILEBYEMAIL = "createFileByEmail";

}
