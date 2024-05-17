/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.masters.pathology;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.SpecimenDao;
import javax.sql.DataSource;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información de los especimenes de patologia
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author etoro
 * @see 08/10/2020
 * @see Creaciòn
 */
@Repository
public class SpecimenDaoPostgreSQL implements SpecimenDao {

    private JdbcTemplate jdbcPat, jdbc;

    @Autowired
    public void setDataSourcePat(@Qualifier("dataSourcePat") DataSource dataSource) {
        jdbcPat = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplatePat() {
        return jdbcPat;
    }

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbc;
    }
    
    @Override
    public List<Specimen> list() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab24c1, lab24.lab07c1, lab24c2, lab24c9, lab24.lab56c1, lab56c2, lab56c3, lab24.lab04c1, lab24c8  "
                    + "FROM lab24 "
                    + "LEFT JOIN lab56 ON lab24.lab56c1 = lab56.lab56c1 "
                    + "WHERE '4' = any(string_to_array(lab24c10, ',')::text[]) ", (ResultSet rs, int i) ->
            {
                Specimen specimen = new Specimen();
                specimen.setId(rs.getInt("lab24c1"));
                specimen.setCode(rs.getString("lab24c9"));
                specimen.setName(rs.getString("lab24c2"));
                specimen.setState(rs.getBoolean("lab07c1"));
                specimen.getContainer().setId(rs.getInt("lab56c1"));
                specimen.getContainer().setName(rs.getString("lab56c2"));
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("lab56c3");
                if (ImaBytes != null)
                {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                specimen.getContainer().setImage(Imabas64);
                specimen.getUserCreated().setId(rs.getInt("lab04c1"));
                specimen.setCreatedAt(rs.getTimestamp("lab24c8"));
                return specimen;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    @Override
    public List<Study> studies() throws Exception
    {
        try
        {
            return jdbc.query(""
                    + "SELECT lab39c1, lab39c2, lab39c3, lab39c4, lab39.lab24c1, lab24c2 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab24 on lab39.lab24c1 = lab24.lab24c1 "
                    + "WHERE '4' = any(string_to_array(lab24c10, ',')::text[]) ", (ResultSet rs, int i) ->
            {
                Study study = new Study();
                study.setId(rs.getInt("lab39c1"));
                study.setCode(rs.getString("lab39c2"));
                study.setName(rs.getString("lab39c4"));
                study.setAbbr(rs.getString("lab39c3"));
                study.setSample(rs.getInt("lab24c1"));
                study.setSampleName(rs.getString("lab24c2"));
                
                return study;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

}
