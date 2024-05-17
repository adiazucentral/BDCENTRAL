/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.operation.orders;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.orders.BillingTestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa metodos de acceso a datos para los examenes con precios de una
 * orden para el motor postgreSQL
 *
 * @version 1.0.0
 * @author dcortes
 * @since 10/04/2018
 * @see Creacion
 */
@Repository
public class BillingTestDaoPostgreSQL implements BillingTestDao
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
