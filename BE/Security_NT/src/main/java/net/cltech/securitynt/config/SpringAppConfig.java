/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import net.cltech.securitynt.tools.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Clase de configuracion general de spring en la aplicaciòn
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/03/2017
 * @see Creacion
 */
@Configuration
@EnableWebMvc
public class SpringAppConfig extends WebMvcConfigurerAdapter implements WebApplicationInitializer
{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException
    {
        //Revisa la configuracion jndi en el servidor de aplicaciones para cargar el archivo applicationContext adecuado
        String appContext = servletContext.getRealPath("");
        appContext = appContext.startsWith("/") ? appContext.substring(1).trim() : appContext;
        System.out.println("Context: " + appContext);
        String uriEngine = appContext + "/WEB-INF/engine.properties";
        Properties properties = new Properties();
        FileReader reader = null;
        String engine = "sqlserver";
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
        Log.info(getClass(), "Seleccionado motor de base de datos: " + engine);

        switch (engine)
        {
            case "postgresql":
                servletContext.setInitParameter("contextConfigLocation", "/WEB-INF/applicationContextPostgreSQL.xml");
                break;
            case "sqlserver":
                servletContext.setInitParameter("contextConfigLocation", "/WEB-INF/applicationContextSQLServer.xml");
                break;
        }
    }
}
