/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.dao.impl.sqlserver.start;

import javax.sql.DataSource;
import net.cltech.securitynt.dao.interfaces.start.StartAppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dcortes
 */
@Repository
public class StartAppSQLServer implements StartAppDao
{

    private JdbcTemplate jdbc;
    private JdbcTemplate jdbcDocs;
    private JdbcTemplate jdbcStat;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource ds)
    {
        jdbc = new JdbcTemplate(ds);
    }

    @Autowired
    public void setDataSourceDocs(@Qualifier("dataSourceDocs") DataSource ds)
    {
        jdbcDocs = new JdbcTemplate(ds);
    }

    @Autowired
    public void setDataSourceStat(@Qualifier("dataSourceStat") DataSource ds)
    {
        jdbcStat = new JdbcTemplate(ds);
    }

    @Override
    public void execStartScript(String startScript) throws Exception
    {
        final String sentences[] = startScript.split("--SEGMENT");
        for (String sentence : sentences)
        {
            jdbc.execute(sentence);
        }
    }

    @Override
    public void execStartScriptDocs(String startScript) throws Exception
    {
        final String sentences[] = startScript.split("--SEGMENT");
        for (String sentence : sentences)
        {
            jdbcDocs.execute(sentence);
        }
    }

    @Override
    public void execStartScriptStat(String startScript) throws Exception
    {
        final String sentences[] = startScript.split("--SEGMENT");
        for (String sentence : sentences)
        {
            jdbcStat.execute(sentence);
        }
    }

}
