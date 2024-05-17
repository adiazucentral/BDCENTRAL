package net.cltech.enterprisent.dao.impl.postgresql.operation.orders;

import java.sql.ResultSet;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.orders.CashBoxDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a base de datos para postgresSQL para caja
 *
 * @version 1.0.0
 * @author dcortes
 * @since 2/05/2018
 * @see Creacion
 */
@Repository
public class CashBoxDaoPostgreSQL implements CashBoxDao
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

    @Override
    public int getCentralSystemToExternalBilling() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab118c1 FROM lab118 WHERE lab118c3 = 1 LIMIT 1");
            
            return jdbc.queryForObject(query.toString(), (ResultSet rs, int i) -> 
            {
                return rs.getInt("lab118c1");
            });
        }
        catch (DataAccessException e)
        {
            return -1;
        }
    }
}
