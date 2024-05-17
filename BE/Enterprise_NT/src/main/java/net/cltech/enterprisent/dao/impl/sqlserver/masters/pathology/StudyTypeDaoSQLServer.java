/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.masters.pathology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.StudyTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de los tipos de estudio de patología
 * para SQLServer
 *
 * @version 1.0.0
 * @author omendez
 * @see 27/10/2020
 * @see Creaciòn
 */
@Repository
public class StudyTypeDaoSQLServer implements StudyTypeDao
{
    private JdbcTemplate jdbc, jdbcPat;

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
    
    @Autowired
    public void setDataSourcePat(@Qualifier("dataSourcePat") DataSource dataSourcePat)
    {
        jdbcPat = new JdbcTemplate(dataSourcePat);
    }

    @Override
    public JdbcTemplate getJdbcPatTemplate()
    {
        return jdbcPat;
    }
}
