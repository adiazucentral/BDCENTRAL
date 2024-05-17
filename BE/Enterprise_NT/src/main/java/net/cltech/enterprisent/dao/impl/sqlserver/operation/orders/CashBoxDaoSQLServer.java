/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.orders;

import java.sql.ResultSet;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.orders.CashBoxDao;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Agregar una descripcion de la clase
 *
 * @version 1.0.0
 * @author dcortes
 * @since 2/05/2018
 * @see Para cuando se crea una clase incluir
 */
/**
 * Implementa el acceso a base de datos para SQL Server para caja
 *
 * @version 1.0.0
 * @author dcortes
 * @since 2/05/2018
 * @see Creacion
 */
@Repository
public class CashBoxDaoSQLServer implements CashBoxDao
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
            query.append("SELECT TOP 1 lab118c1 FROM lab118 WHERE lab118c3 = 1");
            
            return jdbc.queryForObject(query.toString(), (ResultSet rs, int i) -> 
            {
                return rs.getInt("lab118c1");
            });
        }
        catch (DataAccessException e)
        {
            OrderCreationLog.error(e);
            return -1;
        }
    }
}
