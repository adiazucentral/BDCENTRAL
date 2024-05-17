/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;
import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.CaseSearch;
import net.cltech.enterprisent.domain.operation.pathology.Case;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;

/**
 * Interfaz de servicios a la informacion de los casos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 23/02/2020
 * @see Creación
 */
public interface CaseService 
{
    /**
    * Busca un caso por id
    *
    * @param id Id del caso
    * @param studyType Tipo de estudio   
    * @param number Numero del caso
    * @param order Numero de orden
    * @return Instancia con los datos del caso
    * @throws Exception Error en el servicio
    */
    public Case get(Integer id, Integer studyType, Long number, Long order) throws Exception;
    
    /**
    * Crea un caso de patologia en el sistema
    *
    * @param casePat
    * {@link net.cltech.enterprisent.domain.operation.pathology.Case} Caso a ser
    * guardado
    *
    * @return {@link net.cltech.enterprisent.domain.operation.pathology.Case} con
    * el numero de caso
    * @throws Exception Error presentado en el servicio
    */
    public Case create(Case casePat) throws Exception;
    
    /**
    * Busca los casos de patologia por fecha de ingreso
    *
    * @param date Fecha en formato YYYYMMDD
    * @param branch Id Sede, -1 en caso de no realizar filtro por sede
    * @return Lista de
    * {@link net.cltech.enterprisent.domain.operation.pathology.CaseSearch}
    * @throws Exception Error en el servicio
    */
    public List<CaseSearch> getByEntryDate(int date, int branch) throws Exception;
    
    /**
    * Actualiza un caso
    * {@link net.cltech.enterprisent.domain.operation.pathology.Case} caso a ser
    * modificado
    *
    * @param casePat
    *
    * @return {@link net.cltech.enterprisent.domain.operation.pathology.Case} con el caso 
    * modificado
    * @throws Exception Error presentado en el servicio
    */
    public Case update(Case casePat) throws Exception;
    
    /**
     * Obtiene la lista de casos a partir de filtros realizados
     *
     * @param filter filtros
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.pathology.CaseSearch} null en caso de no encontrar resultados
     * @throws Exception Error en el servicio
     */
    public List<CaseSearch> getFilterCases(FilterPathology filter) throws Exception;
    
    /**
    * Cambiar el estado de un caso
    *
    * @param casePat  
    * @throws Exception Error en el servicio
    */
    public void changeStatus(Case casePat) throws Exception;
    
}
