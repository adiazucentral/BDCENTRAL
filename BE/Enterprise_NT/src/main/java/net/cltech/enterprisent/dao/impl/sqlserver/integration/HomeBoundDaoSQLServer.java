package net.cltech.enterprisent.dao.impl.sqlserver.integration;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.HomeBoundDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a datos para examenes y demograficos en PostgreSQL
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 17/02/2020
 * @see Creación
 */
@Repository
public class HomeBoundDaoSQLServer implements HomeBoundDao
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
    public JdbcTemplate getConnection()
    {
        return jdbc;
    }

    @Override
    public CommentDao getCommentDao()
    {
        return commentDao;
    }

}
