/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Casete;

/**
 * Interface de servicios para el maestro de casetes de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 07/04/2021
 * @see Creacion
*/
public interface CaseteService 
{
    /**
    * lista de casetes de patologia.
    *
    *
    * @return la lista de casetes.
    * @throws Exception Error en la base de datos.
    */
    public List<Casete> list() throws Exception;

    /**
    * Registra un nuevo casete en la base de datos.
    *
    * @param casete Instancia con los datos del casete.
    * @return Instancia con los datos del casete.
    * @throws Exception Error en la base de datos.
    */
    public Casete create(Casete casete) throws Exception;

    /**
    * Obtener información de un casete por un campo especifico.
    *
    * @param id ID del casete a ser consultado.
    * @param name Nombre del casete a ser consultado.
    * @param code Codigo del casete a ser consultado.
    * @return Instancia con los datos del casete.
    * @throws Exception Error en la base de datos.
    */
    public Casete get(Integer id, String name, String code) throws Exception;

    /**
    * Actualiza un casete de la base de datos.
    *
    * @param casete Instancia con los datos del casete.
    * @return la instancia del objeto casete.
    * @throws Exception Error en la base de datos.
    */
    public Casete update(Casete casete) throws Exception;
    
    /**
    * Obtener información de un casete por estado.
    *
    * @param state Estado de los casetes a ser consultados
    * @return Instancia con los datos del casete.
    * @throws Exception Error en la base de datos.
    */
    public List<Casete> list(int state) throws Exception;
}
