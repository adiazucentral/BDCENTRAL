/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.masters.pathology;

import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.ContainerPathologyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Realiza la implementación del acceso a datos de información de los contenedores de patología
 * para SQLServer
 *
 * @version 1.0.0
 * @author omendez
 * @see 19/10/2020
 * @see Creaciòn
 */
@Repository
public class ContainerPathologyDaoSQLServer implements ContainerPathologyDao
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
