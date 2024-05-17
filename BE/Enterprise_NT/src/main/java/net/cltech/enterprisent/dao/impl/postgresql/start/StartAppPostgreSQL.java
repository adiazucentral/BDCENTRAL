package net.cltech.enterprisent.dao.impl.postgresql.start;

import java.util.Date;
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
public class StartAppPostgreSQL implements StartAppDao
{

    @Value("${jdbc.rep.url}")
    private String jdbcRepUrl;

    private JdbcTemplate jdbc;
    private JdbcTemplate jdbcDocs;
    private JdbcTemplate jdbcStat;
    private JdbcTemplate jdbcControl;
    private JdbcTemplate jdbcRep;
    private JdbcTemplate jdbcPat;

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
    public void setDataSourceControl(@Qualifier("dataSourceCont") DataSource ds)
    {
        jdbcControl = new JdbcTemplate(ds);
    }

    @Autowired
    public void setDataSourcePat(@Qualifier("dataSourcePat") DataSource ds)
    {
        jdbcPat = new JdbcTemplate(ds);
    }

    @Override
    public void execStartScript(String startScript) throws Exception
    {
        startScript = startScript.substring(startScript.indexOf("CREATE"));
        jdbc.execute(startScript);
    }

    @Override
    public void execStartScriptDocs(String startScript) throws Exception
    {
        startScript = startScript.substring(startScript.indexOf("CREATE"));
        jdbcDocs.execute(startScript);
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
    public void execStartScriptStat(String startScript) throws Exception
    {
        startScript = startScript.substring(startScript.indexOf("CREATE"));
        jdbcStat.execute(startScript);
    }

    @Override
    public void execStartScriptControl(String startScript) throws Exception
    {
        startScript = startScript.substring(startScript.indexOf("CREATE"));
        jdbcControl.execute(startScript);
    }

    @Override
    public void execStartScriptStatAgile() throws Exception
    {
        jdbcStat.execute(getInitAgileStatistics().substring(getInitAgileStatistics().indexOf("CREATE")));
    }

    @Override
    public void execStartScriptPat(String startScript) throws Exception
    {
        startScript = startScript.substring(startScript.indexOf("CREATE"));
        jdbcPat.execute(startScript);
    }

    private String getInitAgileStatistics()
    {
        int currentYear = DateTools.dateToNumberYear(new Date());
        String postgresql = ""
                + "CREATE OR REPLACE FUNCTION scriptStat() RETURNS integer AS $$ "
                + "BEGIN"
                + "     IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'sta5" + currentYear + "') "
                + "     THEN "
                + "         CREATE TABLE sta5" + currentYear + " ( "
                + "             sta5c1    INTEGER NOT NULL, "
                + "             sta5c2    INTEGER NOT NULL, "
                + "             sta5c3    INTEGER NOT NULL, "
                + "             sta5c4    VARCHAR(16) NOT NULL, "
                + "             sta5c5    VARCHAR(150) NOT NULL, "
                + "             sta5c6    VARCHAR(16) NOT NULL, "
                + "             sta5c7    VARCHAR(150) NOT NULL, "
                + "             sta5c8    INTEGER NOT NULL, "
                + "             sta5c9    INTEGER NOT NULL, "
                + "             sta5c10   INTEGER NOT NULL, "
                + "             sta5c11   INTEGER NOT NULL "
                + "         );"
                + "         ALTER TABLE sta5" + currentYear + " ADD CONSTRAINT sta5" + currentYear + "_pk PRIMARY KEY ( sta5c1,sta5c2,sta5c3 );"
                + "     END IF; "
                + "     IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'sta6" + currentYear + "') "
                + "     THEN "
                + "         CREATE TABLE sta6" + currentYear + " ("
                + "             sta6c1    INTEGER NOT NULL,"
                + "             sta6c2    INTEGER NOT NULL,"
                + "             sta6c3    INTEGER NOT NULL,"
                + "             sta6c4    VARCHAR(16) NOT NULL,"
                + "             sta6c5    VARCHAR(64) NOT NULL,"
                + "             sta6c6    VARCHAR(16) NOT NULL,"
                + "             sta6c7    VARCHAR(64) NOT NULL,"
                + "             sta6c8    INTEGER NOT NULL,"
                + "             sta6c9    INTEGER NOT NULL,"
                + "             sta6c10   INTEGER NOT NULL,"
                + "             sta6c11   INTEGER NOT NULL"
                + "         );"
                + "         ALTER TABLE sta6" + currentYear + " ADD CONSTRAINT sta6" + currentYear + "_pk PRIMARY KEY ( sta6c1,sta6c2,sta6c3 );"
                + "     END IF; "
                + "     IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'sta7" + currentYear + "') "
                + "     THEN "
                + "         CREATE TABLE sta7" + currentYear + " ("
                + "             sta7c1   INTEGER NOT NULL,"
                + "             sta7c2   INTEGER NOT NULL,"
                + "             sta7c3   VARCHAR(16) NOT NULL,"
                + "             sta7c4   VARCHAR(64) NOT NULL,"
                + "             sta7c5   INTEGER NOT NULL,"
                + "             sta7c6   INTEGER NOT NULL,"
                + "             sta7c7   INTEGER NOT NULL,"
                + "             sta7c8   INTEGER NOT NULL,"
                + "             sta7c9   INTEGER NOT NULL"
                + "         );"
                + "         ALTER TABLE sta7" + currentYear + " ADD CONSTRAINT sta7" + currentYear + "_pk PRIMARY KEY ( sta7c1,sta7c2 );"
                + "     END IF; "
                + "    RETURN 0;"
                + "END;"
                + "$$ LANGUAGE plpgsql;"
                + "select scriptStat();";

        return postgresql;
    }

    @Override
    public void execStartScriptMaintenanceDB(boolean isSQL, String script, boolean isTaskJob) throws Exception
    {
        if (!isSQL)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
