/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.dao.impl.postgresql.masters.configuration;

import javax.sql.DataSource;
import net.cltech.outreach.dao.interfaces.masters.configuration.UserTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa los metodos de base de datos de tipos de usuario de la consulta
 * web para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/04/2018
 * @see Creacion
 */
@Repository
public class UserTypeDaoPostgreSQL implements UserTypeDao
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
