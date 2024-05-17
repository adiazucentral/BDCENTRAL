/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Field;

/**
 * Interface de servicios para el maestro de campos para las plantillas de macroscopia
 *
 * @version 1.0.0
 * @author omendez
 * @since 08/06/2021
 * @see Creacion
 */
public interface FieldService 
{
    /**
    * lista de campos.
    *
    *
    * @return la lista de campos.
    * @throws Exception Error en la base de datos.
    */
    public List<Field> list() throws Exception;

    /**
    * Registra un nuevo campo en la base de datos.
    *
    * @param field Instancia con los datos del campo.
    * @return Instancia con los datos del campo.
    * @throws Exception Error en la base de datos.
    */
    public Field create(Field field) throws Exception;

    /**
    * Obtener información de un campo por un campo especifico.
    *
    * @param id ID del campo a ser consultado.
    * @param name Nombre del campo a ser consultado.
    * @return Instancia con los datos del campo.
    * @throws Exception Error en la base de datos.
    */
    public Field get(Integer id, String name) throws Exception;

    /**
    * Actualiza un campo en la base de datos.
    *
    * @param field Instancia con los datos del campo.
    * @return la instancia del objeto field.
    * @throws Exception Error en la base de datos.
    */
    public Field update(Field field) throws Exception;
    
    /**
    * Obtener información de un campo por estado.
    *
    * @param state Estado de los campos a ser consultados
    * @return Instancia con los datos de los campos.
    * @throws Exception Error en la base de datos.
    */
    public List<Field> list(int state) throws Exception;
    
}
