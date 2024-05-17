/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.pathology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SampleRejectionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de las muestras rechazadas de patología
 * para SQLServer
 *
 * @version 1.0.0
 * @author omendez
 * @see 26/02/2021
 * @see Creaciòn
 */
@Repository
public class SampleRejectionDaoSQLServer implements SampleRejectionDao
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
