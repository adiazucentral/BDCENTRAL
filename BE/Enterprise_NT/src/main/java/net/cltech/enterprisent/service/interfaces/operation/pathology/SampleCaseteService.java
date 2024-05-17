/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.domain.operation.pathology.SampleCasete;

/**
 *
 * @author omendez
 */
public interface SampleCaseteService {
    
    /**
    * Registra una lista de casetes para un especimen en la base de datos.
    *
    * @param samples Instancia con los datos de los casetes.
    * @return Instancia con los datos de los casetes.
    * @throws Exception Error en la base de datos.
    */
    public List<SampleCasete> create(List<SampleCasete> samples) throws Exception;
    
    /**
    * Obtiene la lista de casetes de las muestras de un caso
    *
    * @param idSample Id de la muestra.
    * @param idCase Id del caso.
    * @return Retorna la lista de casetes para las muestras de un caso.
    * @throws Exception Error en la base de datos.
    * 
    */
    public List<SampleCasete> getBySample(int idSample, int idCase) throws Exception;
    
    /**
     * Obtiene la lista de casetes de los casos a partir de filtros realizados
     *
     * @param filter filtros
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.pathology.SampleCasete} null en caso de no encontrar resultados
     * @throws Exception Error en el servicio
     */
    public List<SampleCasete> getCasetesByFilterCases(FilterPathology filter) throws Exception;
    
    
    /**
    * Cambia el estado de los casetes
    *
    * @param list Lista de casetes a cambiar el estado
    * @return {@link net.cltech.enterprisent.domain.operation.pathology.SampleCasete}
    * @throws Exception Error en la base de datos.
    * 
    */
    public List<SampleCasete> changeStatus(List<SampleCasete> list) throws Exception;
}
