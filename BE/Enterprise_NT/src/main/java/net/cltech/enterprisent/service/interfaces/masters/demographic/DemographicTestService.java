/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DemographicTest;

/**
 * Interfaz de servicios a la informacion del maestro Examenes por demograficos
 *
 * @version 1.0.0
 * @author omendez
 * @since 31/01/2022
 * @see Creación
 */
public interface DemographicTestService {
    
    /**
    * Lista las relaciones desde la base de datos.
    *
    * @return Lista de relaciones.
    * @throws Exception Error en la base de datos.
    */
    public List<DemographicTest> list() throws Exception;
    
    /**
     * Registra una nueva relacion examenes - demograficos en la base de datos.
     *
     * @param demographics Instancia con los datos de los demograficos y los examenes.
     *
     * @return Instancia con los datos del demografico y los examenes.
     * @throws Exception Error en la base de datos.
     */
    public DemographicTest create(DemographicTest demographics) throws Exception;
    
    /**
    * Lee la información de una relación por id.
    *
    * @param id ID de la relación.
    *
    * @return Instancia con los datos de la relación.
    * @throws Exception Error en la base de datos.
    */
    public DemographicTest findById(Integer id) throws Exception;
    
    /**
    * Actualiza la relacion examenes - demograficos en la base de datos.
    *
    * @param demographics Instancia con los datos de la relacion examenes - demograficos a actualizar.
    *
    * @return Instancia con los datos de la relación
    *
    * @throws Exception Error en la base de datos.
    */
    public DemographicTest update(DemographicTest demographics) throws Exception;
}
