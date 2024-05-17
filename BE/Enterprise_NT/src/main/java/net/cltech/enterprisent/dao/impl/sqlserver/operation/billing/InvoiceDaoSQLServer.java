package net.cltech.enterprisent.dao.impl.sqlserver.operation.billing;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.operation.billing.InvoiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
* Realiza la implementación para el acceso a los datos de la factura en SQLServer
*
* @version 1.0.0
* @author Julian
* @since 13/04/2021
* @see Creación
*/

@Repository
public class InvoiceDaoSQLServer implements InvoiceDao
{
    private JdbcTemplate jdbc;

    @Autowired
    private ConfigurationDao daoConfig;
    
    @Autowired
    private ToolsDao toolsDao;
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    } 
    
    @Override
    public ConfigurationDao getDaoConfig() {
        return daoConfig;
    }
    
    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }
}