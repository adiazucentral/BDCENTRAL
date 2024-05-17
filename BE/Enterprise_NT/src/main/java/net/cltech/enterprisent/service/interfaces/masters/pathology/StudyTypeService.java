/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;

/**
 * Interface de servicios para el maestro de tipos de estudio de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/10/2020
 * @see Creacion
 */
public interface StudyTypeService 
{
    /**
    * Registra un nuevo tipo de estudio en la base de datos.
    *
    * @param studyType Instancia con los datos del tipo de estudio.
    *
    * @return Instancia con los datos del tipo de estudio.
    * @throws Exception Error en la base de datos.
    */
    public StudyType create(StudyType studyType) throws Exception;
    
    /**
    * Lista los tipos de estudio desde la base de datos.
    *
    * @return Lista de tipos de estudio.
    * @throws Exception Error en la base de datos.
    */
    public List<StudyType> list() throws Exception;

    /**
    * Lee la información del tipo de estudio por id.
    *
    * @param id ID del tipo de estudio.
    *
    * @return Instancia con los datos del tipo de estudio.
    * @throws Exception Error en la base de datos.
    */
    public StudyType findById(Integer id) throws Exception;
    
    /**
    * Lee la información del tipo de estudio por codigo.
    *
    * @param code Codigo del tipo de estudio.
    *
    * @return Instancia con los datos del tipo de estudio.
    * @throws Exception Error en la base de datos.
    */
    public StudyType findByCode(String code) throws Exception;

    /**
    * Lee la información del tipo de estudio por nombre.
    *
    * @param name Nombre del tipo de estudio.
    *
    * @return Instancia con los datos del tipo de estudio.
    * @throws Exception Error en la base de datos.
    */
    public StudyType findByName(String name) throws Exception;
    
    /**
    * Lee la información del tipo de estudio por estudio
    *
    * @param study ID del estudio
    *
    * @return Instancia con los datos del tipo de estudio.
    * @throws Exception Error en la base de datos.
    */
    public StudyType findstudyTypeByStudy(Integer study) throws Exception;
    
    /**
    * Lee la información de un tipo de estudio por id.
    *
    * @param study Id del estudio.
    *
    * @return Instancia con los datos del tipo de estudio.
    * @throws Exception Error en la base de datos.
    */
    public Study findStudyById(Integer study, Integer studyType) throws Exception;

    /**
    * Actualiza la información del tipo de estudio en la base de datos.
    *
    * @param studyType Instancia con los datos del tipo de estudio a actualizar.
    *
    * @return Instancia con los datos del tipo de estudio
    *
    * @throws Exception Error en la base de datos.
    */
    public StudyType update(StudyType studyType) throws Exception;

    /**
    * Obtiene tipos de estudio por su estado.
    *
    * @param status estado del tipo de estudio activo o inactivo.
    *
    * @return Instancia con los datos del tipo de estudio.
    * @throws Exception Error en la base de datos.
    */
    public List<StudyType> filterByState(int status) throws Exception;

    /**
    * Nuevo registro de estudios por tipo de estudio en la base de datos.
    *
    * @param studyType Instancia con los datos del tipo de estudio y estudios a insertar.
    *
    * @return Registros afectados
    * @throws Exception Error en la base de datos.
    */
    public int createStudies(StudyType studyType) throws Exception;
}
