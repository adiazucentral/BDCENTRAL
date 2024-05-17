/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.common;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de la Auditoria de patologia
 * para Sqlserver
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/02/2021
 * @see Creación
 */
@Repository
public class TrackingPathologyDaoSQLServer implements TrackingPathologyDao
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
