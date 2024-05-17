/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.integration;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationHisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
* Implementa para el acceso a la base de datos de SQLServer
* @version 1.0.0
* @author omendez
* @see 01/02/2021
* @see Creaciòn
*/
@Repository
public class IntegrationHisSQLServer implements IntegrationHisDao 
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
