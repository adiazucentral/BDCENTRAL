package config;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.cltech.enterprisent.start.StartApp;
import net.cltech.enterprisent.websocket.ChatHandler;
import net.cltech.enterprisent.websocket.PrintEventHandler;
import net.cltech.enterprisent.websocket.PrintHandler;
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
    "net.cltech.enterprisent.dao.impl.postgresql",
    "net.cltech.enterprisent.service.impl.enterprisent",
    "net.cltech.enterprisent.controllers"
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
            TestScript.execTestScript();
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
            TestScript.execTestScript();
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
            TestScript.execTestScript();
            return dataSource;
        } catch (NamingException ex)
        {
            Logger.getLogger(TestAppContext.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Bean(name = "dataSourceCont")
    public DataSource dataSourceCont()
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
            TestScript.execTestScript();
            return dataSource;
        } catch (NamingException ex)
        {
            Logger.getLogger(TestAppContext.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Bean(name = "dataSourceRep")
    public DataSource dataSourceRep()
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
            TestScript.execTestScript();
            return dataSource;
        } catch (NamingException ex)
        {
            Logger.getLogger(TestAppContext.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Bean(name = "dataSourcePat")
    public DataSource dataSourcePat()
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
            TestScript.execTestScript();
            return dataSource;
        } catch (NamingException ex)
        {
            Logger.getLogger(TestAppContext.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Bean(name = "socketHandler")
    public PrintEventHandler socketHandler()
    {
        return new PrintEventHandler();
    }

    @Bean(name = "chatHandler")
    public ChatHandler chatHandler()
    {
        return new ChatHandler();
    }

    @Bean(name = "printHandler")
    public PrintHandler printHandler()
    {
        return new PrintHandler();
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
