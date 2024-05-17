/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.operation.reports;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.dao.interfaces.operation.reports.DeliveryResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de entrega de
 * resultados de ordenes para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/11/2017
 * @see Creación
 */
@Repository
public class DeliveryResultDaoPostgreSQL implements DeliveryResultDao
{

    private JdbcTemplate jdbc;
    private JdbcTemplate jdbcRep;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ToolsDao toolsDao;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setDataSourceRep(@Qualifier("dataSourceRep") DataSource ds)
    {
        jdbcRep = new JdbcTemplate(ds);
    }

    @Override
    public JdbcTemplate getConnection()
    {
        return jdbc;
    }

    @Override
    public JdbcTemplate getConnectionRep()
    {
        return jdbcRep;
    }

    @Override
    public CommentDao getCommentDao()
    {
        return commentDao;
    }

    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }
}
