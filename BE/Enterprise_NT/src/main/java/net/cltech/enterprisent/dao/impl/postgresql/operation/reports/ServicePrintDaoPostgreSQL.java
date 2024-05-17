/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.operation.reports;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.reports.ServicePrintDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos para la impresion de servicios
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author equijano
 * @since 20/06/2019
 * @see Creación
 */
@Repository
public class ServicePrintDaoPostgreSQL implements ServicePrintDao
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
