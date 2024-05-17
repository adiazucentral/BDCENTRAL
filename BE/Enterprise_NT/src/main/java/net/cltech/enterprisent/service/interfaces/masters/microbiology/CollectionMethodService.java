/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.CollectionMethod;

/**
 * Interfaz de servicios a la informacion del maestro Metodo de Recolección
 *
 * @version 1.0.0
 * @author cmartin
 * @since 18/01/2018
 * @see Creación
 */
public interface CollectionMethodService
{
    /**
     * Lista los metodos de recolección desde la base de datos.
     *
     * @return Lista de metodos de recolección.
     * @throws Exception Error en la base de datos.
     */
    public List<CollectionMethod> list() throws Exception;

    /**
     * Registra un nuevo metodo de recolección en la base de datos.
     *
     * @param collectionMethod Instancia con los datos del metodo de recolección.
     * @return Instancia con los datos del metodo de recolección.
     * @throws Exception Error en la base de datos.
     */
    public CollectionMethod create(CollectionMethod collectionMethod) throws Exception;
    
    /**
     * Obtener información de un metodo de recolección por un campo especifico.
     *
     * @param id ID del metodo de recolección a ser consultado.
     * @param name Nombre del metodo de recolección a ser consultado.
     * @return Instancia con los datos del metodo de recolección.
     * @throws Exception Error en la base de datos.
     */
    public CollectionMethod get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de un metodo de recolección en la base de datos.
     *
     * @param collectionMethod Instancia con los datos del metodo de recolección.
     * @return Objeto del metodo de recolección modificada.
     * @throws Exception Error en la base de datos.
     */
    public CollectionMethod update(CollectionMethod collectionMethod) throws Exception;

    /**
     * Elimina un metodo de recolección de la base de datos.
     *
     * @param id ID del metodo de recolección.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
     * Obtener información de un metodo de recolección por estado.
     *
     * @param state Estado de los metodos de recolección a ser consultados
     * @return Instancia con los datos de los metodos de recolección.
     * @throws Exception Error en la base de datos.
     */
    public List<CollectionMethod> list(boolean state) throws Exception;
}
