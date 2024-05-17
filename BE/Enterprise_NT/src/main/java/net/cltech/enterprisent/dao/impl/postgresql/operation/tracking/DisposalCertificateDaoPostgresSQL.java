package net.cltech.enterprisent.dao.impl.postgresql.operation.tracking;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.DisposalCertificateDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro para
 * PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/05/2018
 * @see Creación
 */
@Repository
public class DisposalCertificateDaoPostgresSQL implements DisposalCertificateDao
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
