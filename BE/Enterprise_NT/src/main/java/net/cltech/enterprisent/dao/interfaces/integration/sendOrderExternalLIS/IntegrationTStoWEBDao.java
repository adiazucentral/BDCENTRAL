/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.integration.sendOrderExternalLIS;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author hpoveda
 */
public interface IntegrationTStoWEBDao
{


    /**
     * Obtiene la coneccion a la base de datos IntegrationTStoWEBDaoPostgreSQL
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();
   

    /**
     * Obtiene lista de ids de las ordenes por rango de fechas Y laboratorio
     *
     * @param startDate
     * @param endDate
     * @return Lista de ordenes
     * @throws Exception Error presentado en base de datos
     */
    default List<Long> getIdOrdersByLaboratory(long startDate, long endDate, int laboratoryID) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab22c1 ")
                    .append("FROM LAB57 ")
                    .append("WHERE lab57.lab22c1 between ")
                    .append(String.valueOf(startDate))
                    .append(" AND ")
                    .append(String.valueOf(endDate))
                    .append(" AND lab57.lab40C1 = ").append(String.valueOf(laboratoryID));
            
            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getLong("lab22c1");
            });
        } catch (Exception ex)
        {
            OrderCreationLog.info("ERROR POR " + ex);
            return new ArrayList<>();
        }
    }
    
    
     /**
     * Obtiene lista de ids de las ordenes por rango de fechas
     *
     * @param startDate
     * @param endDate
     * @return Lista de ordenes
     * @throws Exception Error presentado en base de datos
     */
    default List<Long> getIdOrders(String startDate, String endDate) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab22c1 ")
                    .append("FROM lab22 ")
                    .append("WHERE lab22c2 between ").append(startDate).append(" AND ")
                    .append(endDate)
                    .append(" AND (lab22c19 = 0 or lab22c19 is null)  ");

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getLong("lab22c1");
            });
        } catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }
    
}
