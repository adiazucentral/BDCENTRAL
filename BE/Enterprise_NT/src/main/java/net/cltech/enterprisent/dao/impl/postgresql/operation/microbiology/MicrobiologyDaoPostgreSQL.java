/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.operation.microbiology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.dao.interfaces.operation.microbiology.MicrobiologyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de microbiologia
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/01/2018
 * @see Creación
 */
@Repository
public class MicrobiologyDaoPostgreSQL implements MicrobiologyDao
{

    private JdbcTemplate jdbc;

    @Autowired
    private CommentDao commentDao;

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

    @Override
    public CommentDao getCommentDao()
    {
        return commentDao;
    }

}
