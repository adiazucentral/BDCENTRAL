/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.pathology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SampleCaseteDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.TissueProcessorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del procesador de los casetes de las muestras de un caso de patologia
 * para SQLServer
 *
 * @version 1.0.0
 * @author omendez
 * @see 19/07/2021
 * @see Creaciòn
 */
@Repository
public class TissueProcessorDaoSQLServer implements TissueProcessorDao
{
    private JdbcTemplate jdbcPat;
  
    @Autowired
    private SampleCaseteDao sampleCaseteDao;
    
    @Autowired
    public void setDataSourcePat(@Qualifier("dataSourcePat") DataSource dataSource) {
        jdbcPat = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplatePat() {
        return jdbcPat;
    }
    
    @Override
    public SampleCaseteDao getSampleCaseteDao() {
        return sampleCaseteDao;
    }
}
