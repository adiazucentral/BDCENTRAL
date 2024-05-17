/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.cltech.securitynt.start.StartApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import tools.TestScript;

/**
 * Agregar una descripcion de la clase
 *
 * @version 1.0.0
 * @author dcortes
 * @since 18/04/2017
 * @see Para cuando se crea una clase incluir
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages =
{
    "net.cltech.securitynt.dao.impl.postgresql",
    "net.cltech.securitynt.service.impl.securitynt",
    "net.cltech.securitynt.controllers"
})
public class TestAppContext extends WebMvcConfigurerAdapter
{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/front-end/**").addResourceLocations("/front-end/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
    {
        configurer.enable();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource()
    {
        try
        {
            SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            DataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/enterprise_nt_test", "postgres", "12345");
            builder.bind("jdbc/EnterpriseNTPostgreSQL", ds);
            builder.bind("jndi//dbtype", "postgresql");
            final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
            DataSource dataSource = dsLookup.getDataSource("jdbc/EnterpriseNTPostgreSQL");

            TestScript.setDataSource(ds);
//            TestScript.execTestScript();
            return dataSource;
        } catch (NamingException ex)
        {
            Logger.getLogger(TestAppContext.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Bean(name = "dataSourceDocs")
    public DataSource dataSourceDocs()
    {
        try
        {
            SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            DataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/enterprise_nt_test", "postgres", "12345");
            builder.bind("jdbc/EnterpriseNTPostgreSQL", ds);
            builder.bind("jndi//dbtype", "postgresql");
            final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
            DataSource dataSource = dsLookup.getDataSource("jdbc/EnterpriseNTPostgreSQL");

            TestScript.setDataSource(ds);
//            TestScript.execTestScript();
            return dataSource;
        } catch (NamingException ex)
        {
            Logger.getLogger(TestAppContext.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Bean(name = "dataSourceStat")
    public DataSource dataSourceStat()
    {
        try
        {
            SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            DataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/enterprise_nt_test", "postgres", "12345");
            builder.bind("jdbc/EnterpriseNTPostgreSQL", ds);
            builder.bind("jndi//dbtype", "postgresql");
            final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
            DataSource dataSource = dsLookup.getDataSource("jdbc/EnterpriseNTPostgreSQL");

            TestScript.setDataSource(ds);
//            TestScript.execTestScript();
            return dataSource;
        } catch (NamingException ex)
        {
            Logger.getLogger(TestAppContext.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager()
    {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "startApp")
    public StartApp startApp()
    {
        return new StartApp();
    }
}
