/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.masters.configuration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.RipsDao;
import net.cltech.enterprisent.domain.masters.configuration.RIPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementa los metodos de base de datos de Configuracion RIPS para SQL
 * Server
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/01/2021
 * @see Creacion
 */
@Repository
public class RipsDaoSQLServer implements RipsDao
{
    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<RIPS> get() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT   lab980c1, lab980c2, lab980c3, lab980c4 "
                    + "FROM     lab980", (ResultSet rs, int i) ->
            {
                RIPS rips = new RIPS();
                rips.setKey(rs.getString("lab980c1"));
                rips.setValue(rs.getString("lab980c2"));
                rips.setType(rs.getInt("lab980c3"));
                rips.setFixedValue(rs.getString("lab980c4"));
                return rips;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public RIPS get(String key) throws Exception
    {
        try
        {
            return jdbc.queryForObject(""
                    + "SELECT   lab980c1, lab980c2, lab980c3, lab980c4 "
                    + "FROM     lab980 "
                    + "WHERE    lab980c1 = ?",
                    new Object[]
                    {
                        key
                    }, (ResultSet rs, int i) ->
            {
                RIPS rips = new RIPS();
                rips.setKey(rs.getString("lab980c1"));
                rips.setValue(rs.getString("lab980c2"));
                rips.setType(rs.getInt("lab980c3"));
                rips.setFixedValue(rs.getString("lab980c4"));
                return rips;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public void update(RIPS rips) throws Exception
    {
        jdbc.update("UPDATE lab980 SET lab980c2 = ?, lab980c3 = ?, lab980c4 = ? WHERE lab980c1 = ?", rips.getValue(), rips.getType(), rips.getFixedValue(), rips.getKey());
    }
    
    @Override
    public List<RIPS> getDemographic() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT   lab980c1, lab980c2, lab980c3, lab980c4 "
                    + "FROM     lab980 WHERE lab980c3 = 1", (ResultSet rs, int i) ->
            {
                RIPS rips = new RIPS();
                rips.setKey(rs.getString("lab980c1"));
                rips.setValue(rs.getString("lab980c2"));
                rips.setType(rs.getInt("lab980c3"));
                rips.setFixedValue(rs.getString("lab980c4"));
                return rips;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
}
