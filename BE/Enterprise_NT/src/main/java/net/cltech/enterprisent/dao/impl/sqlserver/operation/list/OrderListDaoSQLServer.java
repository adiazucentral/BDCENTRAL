/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.list;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de listado de
 * ordenes para SQLServer
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/09/2017
 * @see Creación
 */
@Repository
public class OrderListDaoSQLServer implements OrderListDao
{

    private JdbcTemplate jdbc;
    @Autowired
    private ToolsDao toolsDao;

    @Autowired
    private CommentDao commentDao;

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
