/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Pathologist;
import net.cltech.enterprisent.domain.masters.pathology.Organ;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;

/**
 * interface de servicios para el maestro de patologos
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/04/2021
 * @see Creacion
 */
public interface PathologistService 
{
    
    /**
    * lista de patologos
    *
    *
    * @return la lista de patologos.
    * @throws Exception Error en la base de datos.
    */
    public List<Pathologist> list() throws Exception;
    
    /**
    * lista de patologos mediante filtros
    *
    * @param filter Filtros
    * @return la lista de patologos.
    * @throws Exception Error en la base de datos.
    */
    public List<Pathologist> list(FilterPathology filter) throws Exception;
    
    /**
    * Obtener información de un patologo
    *
    * @param id ID del patologo a ser consultado.
    * @return Instancia con los datos del patologo.
    * @throws Exception Error en la base de datos.
    */
    public Pathologist get(Integer id) throws Exception;
    
    /**
    * Obtiene los organos de un patologo
    *
    * @param pathologistId id del patologo
    *
    * @return lista de organos
    * @throws Exception Error de base de datos
    */
    public List<Organ> organs(int pathologistId) throws Exception;

    /**
     * Asigna organos a un patologo
     *
     * @param pathologist Entidad con la información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public int assignOrgans(Pathologist pathologist) throws Exception;
    
}
