/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.masters.microbiology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.MediaCultureDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro Medio
 * de cultivo para PostgreSQL
 *
 * @version 1.0.0
 * @author enavas
 * @since 11/08/2017
 * @see Creación
 */
@Repository
public class MediaCultureDaoPostgreSQL implements MediaCultureDao
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
