/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import net.cltech.enterprisent.domain.operation.pathology.OrderPathology;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de las ordenes de patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 06/10/2020
 * @see Creación
 */
public interface OrderPathologyDao 
{
    
    public JdbcTemplate getJdbcTemplate();
     
    /**
    * Obtiene la lista de órdenes filtradas para el módulo de patologia
    *
    * @param resultFilter filtro para el modulo de patologia
    * @param orderByOrderId ordenar por id de orden
    * @param branch Id sede usuario
    *
    * @return
    * {@link net.cltech.enterprisent.domain.operation.pathology.OrderPathology} null
    * en caso de no exisitir datos
    * @throws Exception Error en el servicio
    */
    public List<OrderPathology> list(ResultFilter resultFilter, boolean orderByOrderId, Integer branch) throws Exception;
    
    /**
    * Obtiene las muestras de patologia de una orden.
    *
    * @param idOrder Orden.
    * @return Retorna la lista de muestras con sus respectivos examenes de la
    * orden.
    * @throws Exception Error en la base de datos.
    */
    public List<Specimen> samplesByOrder(long idOrder) throws Exception;
    

    /**
    * Obtiene los examenes de una orden asociados a una muestra.
    *
    * @param idSample Muestra.
    * @param idOrder Orden.
    * @return Retorna la lista de examenes de una orden asociados a una muestra.
    * @throws Exception Error en la base de datos.
    */
    default List<Study> testsBySample(long idOrder, Integer idSample) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + " SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4 "
                    + " FROM lab39 "
                    + " JOIN lab57 ON lab57.lab39c1 = lab39.lab39c1 "
                    + " WHERE lab57.lab24c1  = ? AND lab57.lab22c1 = ? AND lab39.lab39c37 != 2 ",                           
                    new Object[]
                    {
                        idSample, idOrder
                    },(ResultSet rs, int i) ->
            {
                Study study = new Study();
                study.setId(rs.getInt("lab39c1"));
                study.setCode(rs.getString("lab39c2"));
                study.setAbbr(rs.getString("lab39c3"));
                study.setName(rs.getString("lab39c4"));
                return study;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
    * Obtiene la informacion de una muestra
    * 
    * @return 
    * @throws Exception Error al obtener la informacion de la muestra
    */
    default Specimen getSpecimenDataLis(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab24c1, lab24.lab07c1, lab24c2, lab24c9, lab24.lab56c1 " + 
                    "FROM lab24 ";
           
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab24c1 = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            
            return getJdbcTemplate().queryForObject(query, new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Specimen specimen = new Specimen();
                specimen.setId(rs.getInt("lab24c1"));
                specimen.setState(rs.getBoolean("lab07c1"));
                specimen.setName(rs.getString("lab24c2"));
                specimen.setCode(rs.getString("lab24c9"));
                return specimen;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
}
