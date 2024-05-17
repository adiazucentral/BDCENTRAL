/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import net.cltech.enterprisent.domain.operation.pathology.CaseteTracking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de la trazabilidad de los casetes
 *
 * @version 1.0.0
 * @author omendez
 * @see 26/07/2021
 * @see Creaciòn
 */
public interface CaseteTrackingDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default CaseteTracking create(CaseteTracking tracking) 
    {      
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat34")
                .usingColumns("pat32c1", "pat34c2", "lab04c1a", "pat34c3")
                .usingGeneratedKeyColumns("pat34c1");

        HashMap parameters = new HashMap();
        parameters.put("pat32c1", tracking.getCasete());
        parameters.put("pat34c2", tracking.getStatus());
        parameters.put("lab04c1a", tracking.getCauser().getId());
        parameters.put("pat34c3", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        tracking.setId(key.intValue());
        tracking.setDate(timestamp);
        return tracking;
    }
    
    /**
     * Obtiene la cantidad de casetes procesados en un estado especifico
     *
     * @param status Estado
     * @param init Fecha Inicial
     * @param end Fecha Final
     *
     * @return cantidad
     * @throws java.lang.Exception
     */
    default Integer getCount(int status, String init, String end) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT COUNT(pat34c1) FROM pat34 ")
                    .append("WHERE pat34c2 = ").append(status)
                    .append(" AND pat34c3 > '").append(init).append(" 00:00:00'")
                    .append(" AND pat34c3 < '").append(end).append(" 23:59:59'"); 

            return getJdbcTemplatePat().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("count");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }
}
