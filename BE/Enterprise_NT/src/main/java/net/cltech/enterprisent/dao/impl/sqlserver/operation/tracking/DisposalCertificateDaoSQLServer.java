package net.cltech.enterprisent.dao.impl.sqlserver.operation.tracking;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.DisposalCertificateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro para
 * SQL Server
 *
 * @version 1.0.0
 * @author eacuna
 * @since 29/06/2018
 * @see Creación
 */
@Repository
public class DisposalCertificateDaoSQLServer implements DisposalCertificateDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getConnection()
    {
        return jdbc;
    }

}
