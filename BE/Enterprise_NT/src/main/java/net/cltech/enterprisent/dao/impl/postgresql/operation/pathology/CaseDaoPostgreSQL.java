/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.operation.pathology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.CaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de los casos de patologia
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author omendez
 * @see 23/02/2021
 * @see Creaciòn
 */
@Repository
public class CaseDaoPostgreSQL implements CaseDao 
{
    private JdbcTemplate jdbcPat, jdbc;

    @Autowired
    public void setDataSourcePat(@Qualifier("dataSourcePat") DataSource dataSource) {
        jdbcPat = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplatePat() {
        return jdbcPat;
    }
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbc;
    }
}
