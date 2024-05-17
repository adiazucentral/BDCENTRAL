/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.pathology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Schedule;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de la configuracion de agenda de patologos
 *
 * @version 1.0.0
 * @author omendez
 * @see 22/04/2021
 * @see Creaciòn
 */
public interface ScheduleDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default Schedule get(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat17c1, pat17.lab04c1a, pat17c2, pat17c3, pat17c4, pat17c5, pat17.pat21c1, pat21c2, pat21c3, pat21c4, pat21c5, pat17.lab04c1b, pat17c6, pat17.lab04c1c "
                    + " FROM pat17 "
                    + " LEFT JOIN pat21 ON pat21.pat21c1 = pat17.pat21c1 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat17c1 = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            
            return getJdbcTemplatePat().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Schedule schedule = new Schedule();
                
                schedule.setId(rs.getInt("pat17c1"));
                schedule.setPathologist(rs.getInt("lab04c1a"));
                schedule.setInit(rs.getTimestamp("pat17c2"));
                schedule.setEnd(rs.getTimestamp("pat17c3"));
                schedule.setStatus(rs.getInt("pat17c4"));
                schedule.getEvent().setId(rs.getInt("pat21c1"));
                schedule.getEvent().setCode(rs.getString("pat21c2"));
                schedule.getEvent().setName(rs.getString("pat21c3"));
                schedule.getEvent().setColour(rs.getString("pat21c4"));
                schedule.getEvent().setStatus(rs.getInt("pat21c5"));
                schedule.setCreatedAt(rs.getTimestamp("pat17c5"));
                schedule.getUserCreated().setId(rs.getInt("lab04c1b"));
                schedule.getUserUpdated().setId(rs.getInt("lab04c1c"));
                if (rs.getTimestamp("pat17c6") != null) {
                    schedule.setUpdatedAt(rs.getTimestamp("pat17c6"));
                }
                return schedule;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    default Schedule create(Schedule schedule) throws Exception {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat17")
                .usingColumns("lab04c1a", "pat17c2", "pat17c3", "pat17c4", "pat21c1" ,"pat17c5", "lab04c1b")
                .usingGeneratedKeyColumns("pat17c1");

        HashMap parameters = new HashMap();
        
        parameters.put("lab04c1a", schedule.getPathologist());
        parameters.put("pat17c2", schedule.getInit());
        parameters.put("pat17c3", schedule.getEnd());
        parameters.put("pat17c4", schedule.getStatus());
        parameters.put("pat21c1", schedule.getEvent().getId());
        parameters.put("pat17c5", timestamp);
        parameters.put("lab04c1b", schedule.getUserCreated().getId());

        Number key = insert.executeAndReturnKey(parameters);
        schedule.setId(key.intValue());
        schedule.setCreatedAt(timestamp);
        return schedule;
    }
    
    default List<Schedule> getByPathologist(Integer idPathologist, FilterPathology filter) throws Exception
    {
        try
        {
            String query = "SELECT pat17c1, pat17.lab04c1a, pat17c2, pat17c3, pat17c4, pat17c5, pat17.pat21c1, pat21c2, pat21c3, pat21c4, pat21c5, pat17.lab04c1b, pat17c6, pat17.lab04c1c "
                    + " FROM pat17 "
                    + " LEFT JOIN pat21 ON pat21.pat21c1 = pat17.pat21c1 "
                    + " WHERE pat17.lab04c1a = ? ";
                
            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");
            
            parametersList.add(idPathologist);
                     
            if (filter != null)
            {
                //--------Filtro de fechas 
                if (filter.getInitDate() != null && filter.getEndDate() != null )
                {
                    where.append(" AND pat17c2 BETWEEN ? AND ? ");
                    parametersList.add(filter.getInitDate());
                    parametersList.add(filter.getEndDate());
                }
            }
            
            Object[] parametersArr = new Object[parametersList.size()];
            parametersArr = parametersList.toArray(parametersArr);
            
            return getJdbcTemplatePat().query(query + where.toString(),
                    parametersArr, (ResultSet rs, int i) ->
            {
                Schedule schedule = new Schedule();
                
                schedule.setId(rs.getInt("pat17c1"));
                schedule.setPathologist(rs.getInt("lab04c1a"));
                schedule.setInit(rs.getTimestamp("pat17c2"));
                schedule.setEnd(rs.getTimestamp("pat17c3"));
                schedule.setStatus(rs.getInt("pat17c4"));
                schedule.getEvent().setId(rs.getInt("pat21c1"));
                schedule.getEvent().setCode(rs.getString("pat21c2"));
                schedule.getEvent().setName(rs.getString("pat21c3"));
                schedule.getEvent().setColour(rs.getString("pat21c4"));
                schedule.getEvent().setStatus(rs.getInt("pat21c5"));
                schedule.setCreatedAt(rs.getTimestamp("pat17c5"));
                schedule.getUserCreated().setId(rs.getInt("lab04c1b"));
                schedule.getUserUpdated().setId(rs.getInt("lab04c1c"));
                if (rs.getTimestamp("pat17c6") != null) {
                    schedule.setUpdatedAt(rs.getTimestamp("pat17c6"));
                }
                return schedule;
            });
            
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
    
    default Schedule update(Schedule schedule) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat17 SET lab04c1a = ?, pat17c2 = ?, pat17c3 = ?, pat17c4 = ?, pat21c1 = ?, pat17c6 = ?, lab04c1c = ? "
                + "WHERE pat17c1 = ?",
                schedule.getPathologist(), schedule.getInit(), schedule.getEnd(), schedule.getStatus(), schedule.getEvent().getId() ,timestamp , schedule.getUserUpdated().getId(), schedule.getId());
        schedule.setUpdatedAt(timestamp);
        return schedule;
    }
    
    default int delete(int id) throws Exception
    {
        return getJdbcTemplatePat().update("DELETE FROM pat17 WHERE pat17c1 = ? ", id);
    }
}
