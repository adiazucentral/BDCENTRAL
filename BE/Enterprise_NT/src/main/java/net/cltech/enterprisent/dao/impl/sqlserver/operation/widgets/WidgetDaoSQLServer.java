/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.widgets;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.widgets.WidgetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de widgets para
 * SQLServer
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/12/2017
 * @see Creación
 */
@Repository
public class WidgetDaoSQLServer implements WidgetDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getConnection()
    {
        return jdbc;
    }
}
