package net.cltech.enterprisent.dao.impl.postgresql.masters.demographic;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicWebQueryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Demograficos con consulta web
 * 
 * @version 1.0.0
 * @author javila
 * @since 23/01/2020
 * @see Creación
 */
@Repository
public class DemographicWebQueryServicePostgreSQL implements DemographicWebQueryDao{
    
    private JdbcTemplate  jdbc;
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource){
        jdbc = new JdbcTemplate(dataSource);
    }
    
    @Override
    public JdbcTemplate getJdbcTemplate(){
        return jdbc;
    }
}
