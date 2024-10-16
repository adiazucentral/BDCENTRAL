package net.cltech.enterprisent.dao.impl.sqlserver.operation.billing;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.billing.InvoiceDao;
import net.cltech.enterprisent.dao.interfaces.operation.billing.StatementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
* Realiza la implementación para el acceso a los datos del estado de la cuenta en SQLServer
*
* @version 1.0.0
* @author Julian
* @since 13/04/2021
* @see Creación
*/

@Repository
public class StatementDaoSQLServer implements StatementDao
{
    private JdbcTemplate jdbc;
    
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
}