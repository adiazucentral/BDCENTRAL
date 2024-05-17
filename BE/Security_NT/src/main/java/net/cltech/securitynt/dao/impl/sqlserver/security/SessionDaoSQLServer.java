/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.dao.impl.sqlserver.security;

import javax.sql.DataSource;
import net.cltech.securitynt.dao.interfaces.security.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de sesiones
 * activas para SQLServer
 *
 * @version 1.0.0
 * @author equijano
 * @since 30/11/2018
 * @see Creación
 */
@Repository
public class SessionDaoSQLServer implements SessionDao
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
