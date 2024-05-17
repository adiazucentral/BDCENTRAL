/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.integration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationEpidemiologyDao;
import net.cltech.enterprisent.domain.integration.epidemiology.EpidemiologicalEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información 
 * para la BD de PostgreSQL
 * 
 * @version 1.0.0
 * @author omendez
 * @since 09/08/2021
 * @see Creación
 */
@Repository
public class IntegrationEpidemiologyDaoPostgreSQL implements IntegrationEpidemiologyDao
{
    private JdbcTemplate jdbc;
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource")DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
    
    /**
     *
     * Retorna listado de de examenes asociados al area de epidemiologia
     *
     * @param area Id del area de epidemiologia
     * @return Lista de examenes con la primera entrevista creada
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<EpidemiologicalEvents> getTestsByArea(int area) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39c1, lab39c2, lab39c3, lab66.lab44c1, lab44c2  ")
                    .append(" FROM lab39 ")
                    .append(" LEFT JOIN lab66 ON lab39.lab39c1 = lab66.lab66c1 ").append(" AND lab66.lab44c1 = ( SELECT  MIN(lab44c1) as lab44c1 FROM lab66 WHERE lab66c1 = lab39.lab39c1 LIMIT 1)")
                    .append(" LEFT JOIN lab44 ON lab44.lab44c1 = lab66.lab44c1 ")
                    .append(" WHERE lab43c1 = ").append(area);

            return jdbc.query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                EpidemiologicalEvents test = new EpidemiologicalEvents();
                
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c3"));
                test.getInterview().setId(rs.getInt("lab44c1"));
                test.getInterview().setName(rs.getString("lab44c2"));
                return test;
            });
        } catch (Exception e)
        {
            return new ArrayList<>(0);
        }
    }
    
}
