/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.dao.impl.postgresql.start;

import javax.sql.DataSource;
import net.cltech.outreach.dao.interfaces.start.StartAppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dcortes
 */
@Repository
public class StartAppPostgreSQL implements StartAppDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource ds)
    {
        jdbc = new JdbcTemplate(ds);
    }

    @Override
    public void execStartScript(String startScript) throws Exception
    {
        startScript = startScript.substring(startScript.indexOf("CREATE"));
        jdbc.execute(startScript);
    }

}
