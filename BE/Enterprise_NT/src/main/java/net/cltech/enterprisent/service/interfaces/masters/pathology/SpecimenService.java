/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.Study;

/**
 * Interface de servicios para el maestro de especimenes de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 08/04/2021
 * @see Creacion
 */
public interface SpecimenService {

    /**
    * Obtiene la lista de especimenes de patologia.
    *
    * @return Retorna la lista de especimenes de patologia
    * @throws Exception Error en la base de datos.
    */
    public List<Specimen> list() throws Exception;
    
    /**
    * Obtiene las submuestras de un especimen
    *
    * @param specimenId id del especimen
    *
    * @return lista de submuestras
    * @throws Exception Error de base de datos
    */
    public List<Specimen> subSamples(int specimenId) throws Exception;
    
    /**
    * Obtiene las submuestras de un especimen
    *
    * @param specimenId id del especimen
    *
    * @return lista de submuestras del especimen
    * @throws Exception Error de base de datos
    */
    public List<Specimen> getSubsamplesBySpecimen(int specimenId) throws Exception;

    /**
     * Asigna submuestras a un especimen
     *
     * @param specimen Entidad con la información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public int assignSubSamples(Specimen specimen) throws Exception;
    
    
    /**
    * Lista los examenes con muestras de patologia desde la base de datos.
    *
    * @return Lista de examenes con muestras de patologia.
    * @throws Exception Error en la base de datos.
    */
    public List<Study> studies() throws Exception;
    
 
    /**
    * Lista los estudios de una muestra de patologia
    * @param sample Id de la muestra
    *
    * @return Lista de examenes de una muestra de patologia.
    * @throws Exception Error en la base de datos.
    */
    public List<Study> getStudiesBySample(Integer sample) throws Exception;
}
