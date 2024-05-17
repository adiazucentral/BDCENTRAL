/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.common;

import java.sql.ResultSet;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa los metodos de utilidad en base de datos para Postgre SQL
 *
 * @version 1.0.0
 * @author dcortes
 * @since 24/10/2017
 * @see Creacion
 */
@Repository
public class ToolsDaoSQLServer implements ToolsDao
{

    private JdbcTemplate jdbc, jdbcPat;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource ds)
    {
        jdbc = new JdbcTemplate(ds);
    }

    @Autowired
    public void setDataSourcePat(@Qualifier("dataSourcePat") DataSource dataSourcePat)
    {
        jdbcPat = new JdbcTemplate(dataSourcePat);
    }

    @Override
    public void createSequence(String name, int start, int increment, int maximum) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbc.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        if (!exists)
        {
            jdbc.execute(""
                    + "CREATE SEQUENCE " + name + " "
                    + "START WITH " + start + " "
                    + "INCREMENT BY " + increment + " "
                    + "MINVALUE " + start + " "
                    + "MAXVALUE " + maximum + " "
                    + ";");
        }
    }

    @Override
    public void resetSequence(String name, int start) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbc.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        if (exists)
        {
            jdbc.execute(""
                    + "ALTER SEQUENCE " + name + " "
                    + "RESTART WITH " + start);
        }
    }

    @Override
    public void resetSequence(String name, int start, int maximum) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbc.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        if (exists)
        {
            jdbc.execute(""
                    + "ALTER SEQUENCE " + name + " "
                    + "RESTART WITH " + start
                    + "MINVALUE " + start + "  MAXVALUE " + maximum
            );
        }
    }

    @Override
    public boolean validateSequence(String name) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbc.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        return exists;
    }

    @Override
    public int nextVal(String name) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbc.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        if (exists)
        {
            try
            {
                return jdbc.queryForObject(""
                        + "SELECT NEXT VALUE FOR " + name + " AS Counter",
                        (ResultSet rs, int i) ->
                {
                    return rs.getInt("Counter");
                });
            } catch (UncategorizedSQLException s)
            {
                return -2;
            }
        } else
        {
            return -1;
        }
    }

    @Override
    public int nextValQuery(String name) throws Exception
    {
        return nextVal(name);
    }

    @Override
    public boolean validateSequencePathology(String name) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbcPat.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        return exists;
    }

    @Override
    public void createSequencePathology(String name, int start, int increment, int maximum) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbcPat.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        if (!exists)
        {
            jdbcPat.execute(""
                    + "CREATE SEQUENCE " + name + " "
                    + "START WITH " + start + " "
                    + "INCREMENT BY " + increment + " "
                    + "MINVALUE " + start + " "
                    + "MAXVALUE " + maximum + " "
                    + ";");
        }
    }

    @Override
    public void resetSequencePathology(String name, int start, int maximum) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbcPat.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        if (exists)
        {
            jdbcPat.execute(""
                    + "ALTER SEQUENCE " + name + " "
                    + "RESTART WITH " + start
                    + "MINVALUE " + start + "  MAXVALUE " + maximum
            );
        }
    }

    @Override
    public void resetSequencePathology(String name, int start) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbc.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        if (exists)
        {
            jdbc.execute(""
                    + "ALTER SEQUENCE " + name + " "
                    + "RESTART WITH " + start);
        }
    }

    @Override
    public int nextValPathology(String name) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbcPat.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }
        if (exists)
        {
            try
            {
                return jdbcPat.queryForObject(""
                        + "SELECT NEXT VALUE FOR " + name + " AS Counter",
                        (ResultSet rs, int i) ->
                {
                    return rs.getInt("Counter");
                });
            } catch (UncategorizedSQLException s)
            {
                return -2;
            }
        } else
        {
            return -1;
        }
    }

    @Override
    public int getValQuery(String name) throws Exception
    {
        boolean exists = true;
        try
        {
            jdbc.queryForObject(""
                    + "SELECT   name "
                    + "FROM     sys.sequences "
                    + "WHERE    name = ? ", new Object[]
                    {
                        name
                    }, (ResultSet rs, int i) ->
            {
                String s = rs.getString("name");
                return s;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            exists = false;
        }

        if (exists)
        {
            try
            {
                return jdbc.queryForObject(""
                        + "SELECT   current_value "
                        + "FROM     sys.sequences "
                        + "WHERE    name = ? ", new Object[]
                        {
                            name
                        }, (ResultSet rs, int i) ->
                {
                    return rs.getInt("current_value");

                });
            } catch (UncategorizedSQLException e)
            {
                return -2;
            }

        } else
        {
            return -1;
        }

    }
}
