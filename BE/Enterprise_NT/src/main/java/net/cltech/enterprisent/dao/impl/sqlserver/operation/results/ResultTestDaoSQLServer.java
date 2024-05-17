/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.results;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa el acceso a datos para el registro de resultados en SQLServer
 *
 * @version 1.0.0
 * @author jblanco
 * @since 03/07/2017
 * @see Creación
 */
@Repository
public class ResultTestDaoSQLServer implements ResultTestDao
{

    @Autowired
    private HttpServletRequest request;
    private JdbcTemplate jdbc;
    private JdbcTemplate docsJdbc;
    @Autowired
    private ToolsDao toolsDao;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setDocsDataSource(@Qualifier("dataSourceDocs") DataSource dataSource)
    {
        docsJdbc = new JdbcTemplate(dataSource);
    }
    
    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

    @Override
    public JdbcTemplate getDocsJdbcTemplate()
    {
        return docsJdbc;
    }
    
    @Override
    public AuthorizedUser getUser() throws Exception
    {
        return JWT.decode(request);
    }

    @Override
    public ToolsDao getToolsDao()
    {
        return toolsDao;
    }
    
 
}
