/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.document;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.document.DocumentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de
 * documentos para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/12/2017
 * @see Creación
 */
@Repository
public class DocumentDaoPostgreSQL implements DocumentDao
{
    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSourceDocs") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getConnection()
    {
        return jdbc;
    }
}
