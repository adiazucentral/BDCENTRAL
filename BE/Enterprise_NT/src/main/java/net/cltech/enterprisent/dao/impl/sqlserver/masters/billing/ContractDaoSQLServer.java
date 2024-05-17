package net.cltech.enterprisent.dao.impl.sqlserver.masters.billing;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.ContractDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a datos de la BD en SQL Server
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 30/06/2021
 * @see Creación
 */
@Repository
public class ContractDaoSQLServer implements ContractDao
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
