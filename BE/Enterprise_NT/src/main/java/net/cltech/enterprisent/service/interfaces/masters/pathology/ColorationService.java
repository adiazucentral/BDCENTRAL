/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Coloration;

/**
 * Interface de servicios para el maestro de coloraciones de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 07/04/2021
 * @see Creacion
 */
public interface ColorationService 
{
    /**
    * lista de coloraciones de patologia.
    *
    *
    * @return la lista de coloraciones.
    * @throws Exception Error en la base de datos.
    */
    public List<Coloration> list() throws Exception;

    /**
    * Registra una nueva coloracion en la base de datos.
    *
    * @param coloration Instancia con los datos de la coloracion.
    * @return Instancia con los datos de la coloracion.
    * @throws Exception Error en la base de datos.
    */
    public Coloration create(Coloration coloration) throws Exception;

    /**
    * Obtener información de una coloracion por un campo especifico.
    *
    * @param id ID de la coloracion a ser consultada.
    * @param name Nombre de la coloracion a ser consultada.
    * @param code Codigo de la coloracion a ser consultada.
    * @return Instancia con los datos de la coloracion.
    * @throws Exception Error en la base de datos.
    */
    public Coloration get(Integer id, String name, String code) throws Exception;

    /**
    * Actualiza una coloracion de la base de datos.
    *
    * @param coloration Instancia con los datos de la coloracion.
    * @return la instancia del objeto coloracion.
    * @throws Exception Error en la base de datos.
    */
    public Coloration update(Coloration coloration) throws Exception;
    
    /**
    * Obtener información de una coloracion por estado.
    *
    * @param state Estado de las coloraciones a ser consultadas
    * @return Instancia con los datos de la coloracion.
    * @throws Exception Error en la base de datos.
    */
    public List<Coloration> list(int state) throws Exception;
}
