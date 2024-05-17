/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.service.impl.enterprisent.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import net.cltech.outreach.dao.interfaces.start.StartAppDao;
import net.cltech.outreach.service.interfaces.start.StartAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de inicio de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 12/04/2017
 * @see Creacion
 */
@Service
public class StartAppServiceImpl implements StartAppService
{

    @Autowired
    private StartAppDao dao;
    @Autowired
    private ServletContext servletContext;
    private static final Logger logger = Logger.getLogger("EnterpriseNT_Outreach - App");

    @Override
    public void execStartScript(String sqlEngine) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath("/scripts/postgresql/script.sql"));
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath("/scripts/sqlserver/script.sql"));
        }
        if (scriptFile.exists())
        {
            logger.log(Level.INFO, "[EnterpriseNT_Outreach - App] : Ejecutando el script de actualizacion");
            FileInputStream reader = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try
            {

                reader = new FileInputStream(scriptFile);
                isr = new InputStreamReader(reader, "UTF-8");
                br = new BufferedReader(isr);
                String script = "";
                String line = br.readLine();
                while (line != null)
                {
                    script += line + "\n";
                    line = br.readLine();
                }

                if (!script.trim().isEmpty())
                {
                    dao.execStartScript(script);
                } else
                {
                    logger.log(Level.SEVERE, "[EnterpriseNT_Outreach - App] : El archivo de script de actualizacion se encuentra vacio");
                }
            } catch (FileNotFoundException fileNotFoundException)
            {
                logger.log(Level.SEVERE, "[EnterpriseNT_Outreach - App] : El archivo de script de actualizacion no ha sido encontrado");
            } catch (IOException iOException)
            {
                logger.log(Level.SEVERE, "[EnterpriseNT_Outreach - App] : Error de lectura del archivo de script de actualizacion");
            } finally
            {
                if (br != null)
                {
                    br.close();
                }

                if (reader != null)
                {
                    br.close();
                }
                if (isr != null)
                {
                    isr.close();
                }
            }
        } else
        {
            logger.log(Level.SEVERE, "[EnterpriseNT_Outreach - App] : El archivo de script de actualizacion no ha sido encontrado");
        }
    }

    @Override
    public String getDataBasaEngine() throws Exception
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
        return engine;
    }

}
