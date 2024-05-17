package net.cltech.enterprisent.dao.impl.sqlserver.start;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.start.StartAppDao;
import net.cltech.enterprisent.tools.DateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    private JdbcTemplate jdbcCont;
    private JdbcTemplate jdbcRep;
    private JdbcTemplate jdbcPat;

    @Value("${jdbc.rep.url}")
    private String jdbcRepUrl;

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
    public void setDataSourceRep(@Qualifier("dataSourceRep") DataSource ds)
    {
        if (!jdbcRepUrl.equals("NA"))
        {
            jdbcRep = new JdbcTemplate(ds);
        } else
        {
            jdbcRep = null;
        }
    }

    @Autowired
    public void setDataSourceStat(@Qualifier("dataSourceStat") DataSource ds)
    {
        jdbcStat = new JdbcTemplate(ds);
    }

    @Autowired
    public void setDataSourceCont(@Qualifier("dataSourceCont") DataSource ds)
    {
        jdbcCont = new JdbcTemplate(ds);
    }

    @Autowired
    public void setDataSourcePat(@Qualifier("dataSourcePat") DataSource ds)
    {
        jdbcPat = new JdbcTemplate(ds);
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

    @Override
    public void execStartScriptControl(String startScript) throws Exception
    {
        final String sentences[] = startScript.split("--SEGMENT");
        for (String sentence : sentences)
        {
            jdbcCont.execute(sentence);
        }
    }

    @Override
    public void execStartScriptRep(String startScript) throws Exception
    {
        if (jdbcRep != null)
        {
            final String sentences[] = startScript.split("--SEGMENT");
            for (String sentence : sentences)
            {
                jdbcRep.execute(sentence);
            }
        }
    }

    @Override
    public void execStartScriptStatAgile() throws Exception
    {
        final String sentences[] = getInitAgileStatistics().split("--SEGMENT");
        for (String sentence : sentences)
        {
            jdbcStat.execute(sentence);
        }
    }

    @Override
    public void execStartScriptPat(String startScript) throws Exception
    {
        final String sentences[] = startScript.split("--SEGMENT");
        for (String sentence : sentences)
        {
            jdbcPat.execute(sentence);
        }
    }

    private String getInitAgileStatistics()
    {
        int currentYear = DateTools.dateToNumberYear(new Date());
        String sqlserver = ""
                + "IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[sta5" + currentYear + "]') )"
                + "BEGIN "
                + " CREATE TABLE sta5" + currentYear + " ("
                + "     sta5c1 INTEGER NOT NULL , "
                + "     sta5c2 INTEGER NOT NULL , "
                + "     sta5c3 INTEGER NOT NULL , "
                + "     sta5c4 VARCHAR (16) NOT NULL , "
                + "     sta5c5 VARCHAR (150) NOT NULL , "
                + "     sta5c6 VARCHAR (16) NOT NULL , "
                + "     sta5c7 VARCHAR (150) NOT NULL , "
                + "     sta5c8 INTEGER NOT NULL , "
                + "     sta5c9 INTEGER NOT NULL , "
                + "     sta5c10 INTEGER NOT NULL , "
                + "     sta5c11 INTEGER NOT NULL "
                + "    )"
                + " ALTER TABLE sta5" + currentYear + " ADD CONSTRAINT sta5" + currentYear + "_PK PRIMARY KEY CLUSTERED (sta5c1, sta5c2, sta5c3) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )"
                + "END "
                + "--SEGMENT "
                + "IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[sta6" + currentYear + "]') )"
                + "BEGIN "
                + " CREATE TABLE sta6" + currentYear + " ("
                + "     sta6c1 INTEGER NOT NULL , "
                + "     sta6c2 INTEGER NOT NULL , "
                + "     sta6c3 INTEGER NOT NULL , "
                + "     sta6c4 VARCHAR (16) NOT NULL , "
                + "     sta6c5 VARCHAR (64) NOT NULL , "
                + "     sta6c6 VARCHAR (16) NOT NULL , "
                + "     sta6c7 VARCHAR (64) NOT NULL , "
                + "     sta6c8 INTEGER NOT NULL , "
                + "     sta6c9 INTEGER NOT NULL , "
                + "     sta6c10 INTEGER NOT NULL , "
                + "     sta6c11 INTEGER NOT NULL "
                + " )"
                + " ALTER TABLE sta6" + currentYear + " ADD CONSTRAINT sta6" + currentYear + "_PK PRIMARY KEY CLUSTERED (sta6c1, sta6c2, sta6c3) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )"
                + "END "
                + "--SEGMENT "
                + "IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[sta7" + currentYear + "]') )"
                + "BEGIN "
                + " CREATE TABLE sta7" + currentYear + " ("
                + "     sta7c1 INTEGER NOT NULL , "
                + "     sta7c2 INTEGER NOT NULL , "
                + "     sta7c3 VARCHAR (16) NOT NULL , "
                + "     sta7c4 VARCHAR (64) NOT NULL , "
                + "     sta7c5 INTEGER NOT NULL , "
                + "     sta7c6 INTEGER NOT NULL , "
                + "     sta7c7 INTEGER NOT NULL , "
                + "     sta7c8 INTEGER NOT NULL , "
                + "     sta7c9 INTEGER NOT NULL "
                + "    )"
                + " ALTER TABLE sta7" + currentYear + "  ADD CONSTRAINT sta7" + currentYear + "_PK PRIMARY KEY CLUSTERED (sta7c1, sta7c2) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )"
                + "END ";

        return sqlserver;
    }

    @Override
    public void execStartScriptMaintenanceDB(boolean isSQL, String script, boolean isTaskJob) throws Exception
    {
        if (isSQL && !isTaskJob)
        {
            final String sentences[] = script.split("--SEGMENT");
            for (String sentence : sentences)
            {
                jdbc.execute(sentence);
            }
        } else if (isSQL && isTaskJob)
        {
            List<String> type = new ArrayList<>();
            type.add("Pequenos");
            type.add("Medianos");
            type.add("Grandes");
            type.forEach(string ->
            {
                jdbc.execute("EXEC DefragmentaIndices @Tipo = '" + string + "';");
            });
        }
    }
}
