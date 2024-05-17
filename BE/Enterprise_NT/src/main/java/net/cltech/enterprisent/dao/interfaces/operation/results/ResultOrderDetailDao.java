/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cltech.enterprisent.dao.interfaces.operation.results;

import java.sql.ResultSet;
import java.util.Date;
import net.cltech.enterprisent.domain.operation.results.ResultOrderDetail;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Interfaz de acceso a datos para el detalle de las órdenes en el módulo 
 * de registro de resultados
 * @version 1.0.0
 * @author jblanco
 * @since Jan 22, 2018
 * @see Creación
 */
public interface ResultOrderDetailDao {

    /**
    * Obtiene el detalle de una orden para el procesamiento de los resultados.
    *
    * @param orderId Identificador de la orden
    * @return ResultOrderDetail de
    * {@link net.cltech.enterprisent.domain.operation.results.ResultOrderDetail}
    * @throws Exception Error en la base de datos.
    */
    default ResultOrderDetail get(long orderId) throws Exception {
        try 
        {
            
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1, lab21c13, lab60c3, lab60c5, lab60.lab04c1";
            String from = ""
                    + " FROM  lab22 "
                    + " INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1"
                    + " LEFT  JOIN lab60 ON lab60.lab60c2 = lab22.lab22c1"
                    + "";
            String where = ""
                    + " WHERE lab22.lab22C1 = ?  AND (lab22c19 = 0 or lab22c19 is null) ";

            return getJdbcTemplate().queryForObject(select + from + where, (ResultSet rs, int i) ->
            {
                ResultOrderDetail bean = new ResultOrderDetail();
                bean.setOrder(rs.getLong("lab22c1"));
                bean.setDiagnostic(rs.getString("lab21c13"));
                bean.setComment(rs.getString("lab60c3"));
                bean.setCommentDate(rs.getDate("lab60c5"));
                bean.setUserId(rs.getInt("lab04c1"));
                return bean;
            }, orderId);
            
        } catch (EmptyResultDataAccessException ex) {
            ex.getMessage();
            return null;
        }
    }   

    /**
     * Modificar el comentario de la orden.
     *
     * @param obj Objeto de tipo ResultOrderDetail
     *
     * @return Objeto de tipo ResultOrderDetail modificado.
     * @throws Exception Error en la base de datos.
     */
    default ResultOrderDetail update(ResultOrderDetail obj) throws Exception {
        try 
        {
            Date currentDate = new Date();
            
            String update = ""
                    + "UPDATE lab60 SET"
                    + " lab60c3 = ?,"
                    + " lab60c5 = ?,"
                    + " lab04c1 = ?";
            String from = ""
                    + " FROM  lab60";
            String where = ""
                    + " WHERE lab22C1 = ?";

            getJdbcTemplate().update(update + from +  where, new Object[]
            {
                obj.getComment(), currentDate, obj.getUserId(), obj.getOrder()
            });
            
            obj.setCommentDate(currentDate);
            
        } catch (EmptyResultDataAccessException ex) {
            ex.getMessage();
            return null;
        }
        return obj;
    }
    
    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}