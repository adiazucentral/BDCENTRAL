/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.operation.pathology.OrderPathology;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;

/**
 * Interfaz de servicios a la informacion de las ordenes de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 06/10/2020
 * @see Creación
 */
public interface OrderPathologyService 
{
    /**
    * Lista las ordenes de patologia desde la base de datos.
    *
    * @return Lista de ordenes.
    * @throws Exception Error en la base de datos.
    */
    public List<OrderPathology> list() throws Exception;
    
    /**
    * Obtiene la lista de órdenes filtradas para el módulo de patologia
    *
    * @param filter filtro para el modulo de patologia
    *
    * @return
    * {@link net.cltech.enterprisent.domain.operation.pathology.OrderPathology} null
    * en caso de no exisitir datos
    * @throws Exception Error en el servicio
    */
    public List<OrderPathology> listByFilters(ResultFilter filter) throws Exception;
    
    /**
    * Obtiene la lista de especimenes de una orden
    *
    * @param idOrder Orden.
    * @return Retorna la lista de especimenes de la orden
    * @throws Exception Error en la base de datos.
    */
    public List<Specimen> specimenByOrden(long idOrder) throws Exception;

    /**
    * Obtiene los datos de un especimen
    * @param specimen Id de la muestra
    *
    * @return Instancia con los datos de la muestra.
    * @throws Exception Error en la base de datos.
    */
    public Specimen getSpecimenDataLis(Integer specimen) throws Exception;
}
