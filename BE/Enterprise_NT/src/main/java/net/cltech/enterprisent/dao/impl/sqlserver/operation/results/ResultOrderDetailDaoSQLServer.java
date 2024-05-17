/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cltech.enterprisent.dao.impl.sqlserver.operation.results;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultOrderDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a datos para el detalle de las órdenes en el módulo 
 * de registro de resultados en SQLServer 
 * @version 1.0.0
 * @author jblanco
 * @since Jan 22, 2018
 * @see Creación
 */
@Repository
public class ResultOrderDetailDaoSQLServer  implements ResultOrderDetailDao {
    
    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbc;
    }
}