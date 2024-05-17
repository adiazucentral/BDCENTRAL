package net.cltech.enterprisent.start;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.security.SessionService;
import net.cltech.enterprisent.service.interfaces.start.StartAppService;
import net.cltech.enterprisent.service.interfaces.start.jobs.SchedulerService;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author dcortes
 */
public class StartApp
{

    @Autowired
    public StartAppService service;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private SchedulerService schedulerService;
    public static final Logger logger = Logger.getLogger("EnterpriseNT - App");

    @Value("${jdbc.rep.url}")
    private String jdbcRep;

    @PostConstruct
    public void execScript()
    {
        try
        {
            String appContext = servletContext.getRealPath("");
            appContext = appContext.startsWith("/") ? appContext.substring(1).trim() : appContext;
            System.out.println("Context: " + appContext);
            String uriEngine = appContext + "/WEB-INF/engine.properties";
            Properties properties = new Properties();
            FileReader reader = null;
            String engine = StartAppService.SQL_SERVER;
            try
            {
                reader = new FileReader(uriEngine);
                properties.load(reader);
                engine = properties.getProperty("engine");

                if (engine.equals("sqlserver"))
                {
                    ISOLATION_READ_UNCOMMITTED = "SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; ";
                }
            } catch (IOException ex)
            {
                ex.getMessage();
            } finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    } catch (IOException ex)
                    {
                        ex.getMessage();
                    }
                }
            }
            service.execStartScript(engine);
            service.execStartScriptData(engine);
            service.execStartScriptDocs(engine);
            service.execStartScriptStat(engine);
            service.execStartScriptControl(engine);
            service.execStartScriptPat(engine);
            if (engine.equals("sqlserver"))
            {
                service.execScriptMaintenanceDBDaily();
            }
            // Script de creación de las funciones o procedimientos almacenados
            // para renombrear las tablas de operacion de NT
            // Variables para el renombre de tablas de la BD de NT
            String fileNameRename = "scriptRenameNewYear.sql";
            String operationRename = "Renombre Tablas De Operación NT (Cada Año)";
            service.executeAnyScript(engine, fileNameRename, operationRename);
            // Variables para el renombre de tablas de la BD de NT - Estadisticas
            String fileNameRenameStat = "scriptRenameNewYearStat.sql";
            String operationRenameStat = "Renombre Tablas De Operación NT - Estadistica (Cada Año)";
            service.executeAnyScript(engine, fileNameRenameStat, operationRenameStat);

            if (!jdbcRep.equals("NA"))
            {
                service.execStartScriptRepo(engine);
            }
            try
            {
                sessionService.deleteAll();
            } catch (Exception ex)
            {
                logger.log(Level.SEVERE, "[EnterpriseNT - App] : Error al eliminar todas las sesiones activas", ex);
            }

            System.out.println("entro a crear");
            // Job para reinicio de órdenes diario
            schedulerService.createThread(configurationService.getValue("HorarioReinicioOrdenes"));
            // Job para programar la limpieza de la tabla de excepciones
            schedulerService.jobTableCleaning();
            // Job para programar el renombre de todas las tablas de operación en NT:
            schedulerService.jobRenameOperationTablesByYear();
        } catch (Exception ex)
        {
            logger.log(Level.SEVERE, "[EnterpriseNT - App] : Error ejecutando el script de actualizacion", ex);
        }
    }
}
