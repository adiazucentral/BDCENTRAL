package net.cltech.enterprisent.dao.impl.sqlserver.integration;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationMobileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * pacientes para al app Móvil.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 20/08/2020
 * @see Creación
 */
@Repository
public class MobileDaoSQLServer implements IntegrationMobileDao
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
