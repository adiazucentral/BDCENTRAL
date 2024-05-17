/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.masters.pathology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.ProcessingTimeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de las horas de procesamiento de las muestras de patologia
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author omendez
 * @see 13/07/2021
 * @see Creaciòn
 */
@Repository
public class ProcessingTimeDaoPostgreSQL implements ProcessingTimeDao
{
    private JdbcTemplate jdbcPat;

    @Autowired
    public void setDataSourcePat(@Qualifier("dataSourcePat") DataSource dataSource) {
        jdbcPat = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplatePat() {
        return jdbcPat;
    }
}
