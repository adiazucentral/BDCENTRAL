/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.File;

/**
 * Interfaz de servicios a la informacion de documentos del caso.
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/03/2021
 * @see Creación
 */
public interface FileService 
{
    
    /**
     * Lista los documentos de un caso
     *
     * @param idCase Caso.
     *
     * @return Lista de documentos.
     * @throws Exception Error en la base de datos.
     */
    public List<File> list(Integer idCase) throws Exception;
    
    /**
    * Guardar archivo de un caso
    *
    * @param file Archivo del caso.
    *
    * @return Archivo del caso.
    * @throws Exception Error en la base de datos.
    */
    public File save(File file) throws Exception;
    
    /**
    * Eliminar documento de un caso
    *
    * @param file Archivo del caso.
    *
    * @return Archivo del caso.
    * @throws Exception Error en la base de datos.
    */
    public File delete(File file) throws Exception;
}
