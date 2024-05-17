package net.cltech.enterprisent.dao.impl.sqlserver.masters.billing;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.HomeboundBoxDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a datos de la BD en PostgreSQL
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 15/07/2021
 * @see Creación
 */
@Repository
public class HomeboundBoxDaoSQLServer implements HomeboundBoxDao
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
