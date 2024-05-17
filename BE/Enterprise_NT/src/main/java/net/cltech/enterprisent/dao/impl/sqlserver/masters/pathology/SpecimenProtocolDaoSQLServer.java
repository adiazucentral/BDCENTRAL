/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.masters.pathology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.SpecimenProtocolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de los protocolos de los especimenes de patologia
 * para SQLServer
 *
 * @version 1.0.0
 * @author omendez
 * @see 20/04/2021
 * @see Creaciòn
 */
@Repository
public class SpecimenProtocolDaoSQLServer implements SpecimenProtocolDao 
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
