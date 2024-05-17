/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.masters.user;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.user.ModuleDao;
import net.cltech.enterprisent.domain.masters.user.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de los modulos
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author enavas
 * @since 26/05/2017
 * @see Creación
 */
@Repository
public class ModuleDaoPostgreSQL implements ModuleDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Module> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "  SELECT lab85c1"
                    + ", lab85c2"
                    + ", lab85c3"
                    + "  FROM lab85"
                    + "  ORDER BY lab85c1", (ResultSet rs, int i) ->
            {
                Module module = new Module();
                module.setId(rs.getInt("lab85c1"));
                module.setIdFather(rs.getInt("lab85c2"));
                module.setName(rs.getString("lab85c3"));
                return module;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    @Override
    public List<Module> findByUser(Integer id) throws Exception
    {
        try
        {
            String sql = "  SELECT lab85.lab85c1"
                    + ", lab85c2"
                    + ", lab85c3"
                    + "  FROM lab84 "
                    + "  INNER JOIN lab82 ON lab82.lab82c1 = lab84.lab82c1"
                    + "  INNER JOIN lab86 ON lab86.lab82c1 = lab82.lab82c1"
                    + "  INNER JOIN lab85 ON lab85.lab85c1 = lab86.lab85c1"
                    + "  WHERE lab84.lab04c1 = ?";
            return jdbc.query(sql, new Object[]
            {
                id
            }, (ResultSet rs, int i) ->
            {
                Module module = new Module();
                module.setId(rs.getInt("lab85c1"));
                module.setIdFather(rs.getInt("lab85c2"));
                module.setName(rs.getString("lab85c3"));
                module.setAccess(true);
                return module;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }

    }

}
