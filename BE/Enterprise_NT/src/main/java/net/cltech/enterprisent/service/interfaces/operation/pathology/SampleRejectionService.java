/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;
import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.pathology.SampleRejection;

/**
 * Interfaz de servicios a la informacion delas muestras rechazadas de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/02/2020
 * @see Creación
 */
public interface SampleRejectionService 
{
    /**
    * Crea un caso de patologia en el sistema
    *
    * @param rejection
    * {@link net.cltech.enterprisent.domain.operation.pathology.SampleRejection} Orden a ser
    * rechazada
    *
    * @return {@link net.cltech.enterprisent.domain.operation.pathology.SampleRejection}
    * @throws Exception Error presentado en el servicio
    */
    public SampleRejection create(SampleRejection rejection) throws Exception;
    
    /**
    * Busca muestras rechazadas por tipo de estudio y orden
    *
    * @param studyType Tipo de estudio   
    * @param order Numero de orden
    * @return Instancia con los datos del rechazo
    * @throws Exception Error en el servicio
    */
    public SampleRejection get(Integer studyType, Long order) throws Exception;
    
    /**
    * Obtiene la lista de órdenes rechazadas
    *
    * @param filter filtro para las ordenes
    *
    * @return
    * {@link net.cltech.enterprisent.domain.operation.pathology.SampleRejection} null
    * en caso de no exisitir datos
    * @throws Exception Error en el servicio
    */
    public List<SampleRejection> listByFilters(Filter filter) throws Exception;
    
    
    /**
    * Activa lista de muestras rechazadas
    *
    * @param rejectList lista de muestras a activar
    * @return{@link net.cltech.enterprisent.domain.operation.pathology.SampleRejection}
    * @throws Exception Error en el servicio
    */
    public List<SampleRejection> activeSamples(List<SampleRejection> rejectList) throws Exception;
}
