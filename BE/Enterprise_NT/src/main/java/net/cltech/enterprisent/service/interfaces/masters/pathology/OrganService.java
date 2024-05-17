/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Organ;

/**
 * interface de servicios para el maestro de organos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/10/2020
 * @see Creacion
 */
public interface OrganService 
{
    /**
    * lista de organos de patologia.
    *
    *
    * @return la lista de organos.
    * @throws Exception Error en la base de datos.
    */
    public List<Organ> list() throws Exception;

    /**
    * Registra un nuevo organo en la base de datos.
    *
    * @param organ Instancia con los datos del organo.
    * @return Instancia con los datos del organo.
    * @throws Exception Error en la base de datos.
    */
    public Organ create(Organ organ) throws Exception;

    /**
    * Obtener información de un organo por un campo especifico.
    *
    * @param id ID del organo a ser consultado.
    * @param name Nombre del organo a ser consultado.
    * @param code Codigo del organo a ser consultado.
    * @return Instancia con los datos del organo.
    * @throws Exception Error en la base de datos.
    */
    public Organ get(Integer id, String name, String code) throws Exception;

    /**
    * Actualiza un organo de la base de datos.
    *
    * @param organ Instancia con los datos del organo.
    * @return la instancia del objeto organo.
    * @throws Exception Error en la base de datos.
    */
    public Organ update(Organ organ) throws Exception;
    
    /**
    * Obtener información de un organo por estado.
    *
    * @param state Estado de los organos a ser consultados
    * @return Instancia con los datos del organo.
    * @throws Exception Error en la base de datos.
    */
    public List<Organ> list(int state) throws Exception;
    
}
