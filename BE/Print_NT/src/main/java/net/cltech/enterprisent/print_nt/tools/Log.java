package net.cltech.enterprisent.print_nt.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintLog;
import net.cltech.enterprisent.print_nt.bussines.domain.PrintNode;
import net.cltech.enterprisent.print_nt.bussines.storage.Storage;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author equijano
 */
public class Log
{

    private static Logger logger;
    private static final String project = "Print_NT";
    private static String folder;

    private static Logger getLogger()
    {
        if (logger == null)
        {
            try
            {
                folder = Log.class.getSimpleName();
                logger = LogManager.getLogger(Log.class);
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
                route.append(getName());
                appender.setFile(route.toString());
                appender.setDatePattern("'.'yyyy-MM-dd");
                appender.setAppend(true);
                appender.setImmediateFlush(true);
                appender.activateOptions();
                logger.addAppender(appender);
            } catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }
        return logger;
    }

    public static String getName()
    {
        return "Log.txt";
    }

    public static String path()
    {
        return pathBasic() + "Log.txt";
    }

    public static String pathBasic()
    {
        return System.getProperty("user.dir") + System.getProperty("file.separator") + project + System.getProperty("file.separator") + "logs" + System.getProperty("file.separator");
    }

    public static String pathDir()
    {
        return System.getProperty("user.dir") + System.getProperty("file.separator") + project + System.getProperty("file.separator");
    }

    public static void error(Throwable ex)
    {
        System.err.println("Se registro un error en " + pathBasic());
        getLogger().error(ex.getMessage(), ex);
    }

    public static void error(String msj)
    {
        System.err.println("Se registro un error en " + pathBasic());
        getLogger().error(msj);
    }

    public static void info(String msj)
    {
        getLogger().info(msj);

    }

    /**
     * Guarda log generado al enviar correo en base de datos
     *
     * @param message mensaje del log a guardar
     * @param printdel correo a enviar
     * @return void
     * @since 1.0
     */
    public static void saveLogs(String message, PrintNode print) throws IOException
    {
        String correo = Arrays.asList(print.getPatientEmail()).get(0).split(";")[0];
        PrintLog printLog = new PrintLog(Long.parseLong(print.getOrder()), print.getBranch(), message, correo, "");
        Properties properties = Storage.loadConfigurationCredentials();

        if (Tools.saveLogPrint(properties.getProperty(Storage.SERVERBACKEND), printLog))
        {
            createFileByEmail(print.getNameFile(), message, correo);
        }
    }

    /**
     * Crea logs de todas la sedes en la carpeta de de clientPrint que lo
     * ejecute
     *
     * @return void
     * @since 1.0
     */
    public static String createLogsBranch() throws IOException
    {
        Properties properties = Storage.loadConfigurationCredentials();
        Tools.listLogsPrint(properties.getProperty(Storage.SERVERBACKEND)).forEach(print ->
        {
            try
            {
                String nameFile = String.valueOf(print.getOrder()) + "_" + print.getBranchName();
                createFileByEmail(nameFile, print.getMessage(), print.getCorreos());
            } catch (IOException ex)
            {
                Log.error("ERRORF : " + ex);
            }

        });

        return "Logs Creados";

    }

    /**
     * Crea carpeta y logs por correo enviado
     *
     * @param nameFile nombre del file creado
     * @param message mensaje del log
     * @param correo correo al que va dirigido el file
     * @return void
     * @since 1.0
     */
    public static void createFileByEmail(String nameFile, String message, String correo) throws IOException
    {
        Date dateDir = new Date();
        String nameArchivo1 = nameFile.replace(".pdf", ".txt").replace(".zip", ".txt");
        String nameArchivo = !Objects.equals(correo, null) ? correo + nameArchivo1 : "correoNULL" + nameArchivo1;
        String dirForday = new SimpleDateFormat("yyyyMMdd").format(dateDir);
        StringBuilder path = new StringBuilder();
        path.append(System.getProperty("user.dir"))
                .append(System.getProperty("file.separator"))
                .append(project)
                .append(System.getProperty("file.separator"))
                .append("logs")
                .append(System.getProperty("file.separator"))
                .append("spool")
                .append(System.getProperty("file.separator"))
                .append(dirForday);
        File dir = new File(path.toString());
        File file;
        if (dir.mkdirs())
        {
            path.append(System.getProperty("file.separator"))
                    .append(nameArchivo);
            file = new File(path.toString());
            file.createNewFile();
        } else
        {
            path.append(System.getProperty("file.separator"))
                    .append(nameArchivo);
            file = new File(path.toString());
            file.createNewFile();
        }
        if (file.exists())
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            Date date = new Date();
            bw.write(date.toString() + ": " + message);
            bw.close();
        }

    }
}
