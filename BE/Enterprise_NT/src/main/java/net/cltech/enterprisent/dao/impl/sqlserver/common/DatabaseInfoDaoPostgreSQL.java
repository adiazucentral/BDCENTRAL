/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.common;

import java.sql.ResultSet;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.DatabaseInfoDao;
import net.cltech.enterprisent.domain.info.DatabaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementacion de el acceso a datos de informacion de la base de
 * datos para PostgreSQL
 *
 * @version 1.0.0
 * @author dcortes
 * @since 06/04/2017
 * @see Creación
 */
@Repository
public class DatabaseInfoDaoPostgreSQL implements DatabaseInfoDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public DatabaseInfo getDatabaseInfo() throws Exception
    {
        final String databaseName = (String) JdbcUtils.extractDatabaseMetaData(jdbc.getDataSource(), "getDatabaseProductName");
        return jdbc.queryForObject(""
                + "SELECT   @@VERSION AS VERSION, DB_NAME() AS DB", (ResultSet rs, int i) ->
        {
            DatabaseInfo record = new DatabaseInfo();
            record.setName("Microsoft SQL Server");
            record.setVersion(rs.getString("VERSION"));
            record.setDb(rs.getString("DB"));
            return record;
        });
    }

}
