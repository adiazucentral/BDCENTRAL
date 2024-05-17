/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.domain.masters.pathology.MacroscopyTemplate;


/**
 * Interface de servicios para el maestro para las plantillas de macroscopia
 *
 * @version 1.0.0
 * @author omendez
 * @since 09/06/2021
 * @see Creacion
 */
public interface MacroscopyTemplateService 
{
    /**
    * Obtiene los campos de la plantilla de un especimen
    *
    * @param specimenId id del especimen
    *
    * @return lista de campos
    * @throws Exception Error de base de datos
    */
    public List<Field> fields(int specimenId) throws Exception;
    
    /**
     * Asigna campos a una plantilla
     *
     * @param template Entidad con la información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public int assignFields(MacroscopyTemplate template) throws Exception;
    
    /**
    * Obtiene las plantillas de un caso
    *
    * @param caseId id del caso
    *
    * @return lista de plantillas
    * @throws Exception Error de base de datos
    */
    public List<MacroscopyTemplate> templates(int caseId) throws Exception;
    
}
