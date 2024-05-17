/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.ContainerPathology;


/**
 * interface de Servicios para el maestro de contenedores de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/10/2020
 * @see Creacion
 */
public interface ContainerPathologyService 
{
    /**
    * lista de contenedores de patologia.
    *
    *
    * @return Lista de contenedores.
    * @throws Exception Error en la base de datos.
    */
    public List<ContainerPathology> list() throws Exception;

    /**
    * Registra un nuevo contenedor en la base de datos.
    *
    * @param container Instancia con los datos del contenedor.
    * @return Instancia con los datos del contenedor.
    * @throws Exception Error en la base de datos.
    */
    public ContainerPathology create(ContainerPathology container) throws Exception;

    /**
    * Obtener información de un contenedor por un campo especifico.
    *
    * @param id ID del contenedor a ser consultado.
    * @param name Nombre del contenedor a ser consultado.
    * @return Instancia con los datos del contenedor.
    * @throws Exception Error en la base de datos.
    */
    public ContainerPathology get(Integer id, String name) throws Exception;

    /**
    * Actualiza un contenedor de la base de datos.
    *
    * @param container Instancia con los datos del contenedor.
    * @return la instancia del objeto contenedor.
    * @throws Exception Error en la base de datos.
    */
    public ContainerPathology update(ContainerPathology container) throws Exception;
    
    /**
    * Obtener información de un contenedor por estado.
    *
    * @param state Estado de los contenedores a ser consultados
    * @return Instancia con los datos del contenedor.
    * @throws Exception Error en la base de datos.
    */
    public List<ContainerPathology> list(int state) throws Exception;
}
