package net.cltech.enterprisent.dao.impl.postgresql.integration;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationConsultaWebHisDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa para el acceso a la base de datos de Sql Server
 *
 * @version 1.0.0
 * @author Julian
 * @since 26/09/2020
 * @see Creación
 */
@Repository
public class IntegrationConsultaWebHisDaoPostgreSQL implements IntegrationConsultaWebHisDao
{

    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CommentDao commentDao;
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbcTemplate;
    }
    
    @Override
    public CommentDao getCommentDao()
    {
        return commentDao;
    }
}
