/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.masters.microbiology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.EtiologicalAgentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro de agente etiologico para
 * PostgreSQL
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/06/2022
 * @see Creación
 */
@Repository
public class EtiologicalAgentDaoPostgreSQL implements EtiologicalAgentDao {
    
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
