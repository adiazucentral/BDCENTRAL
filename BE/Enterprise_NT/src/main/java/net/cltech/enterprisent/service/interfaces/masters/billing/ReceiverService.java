/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Receiver;

/**
 * Interfaz de servicios a la informacion del maestro Receptores
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/07/2017
 * @see Creación
 */
public interface ReceiverService
{
    /**
     * Lista los receptores desde la base de datos.
     *
     * @return Lista de receptores.
     * @throws Exception Error en la base de datos.
     */
    public List<Receiver> list() throws Exception;

    /**
     * Registra un nuevo receptor en la base de datos.
     *
     * @param receiver Instancia con los datos del receptor.
     * @return Instancia con los datos del receptor.
     * @throws Exception Error en la base de datos.
     */
    public Receiver create(Receiver receiver) throws Exception;
    
    /**
     * Obtener información de un receptor por un campo especifico.
     *
     * @param id ID del receptor a ser consultada.
     * @param name Nombre del receptor a ser consultada.
     * @return Instancia con los datos del receptor.
     * @throws Exception Error en la base de datos.
     */
    public Receiver get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de un receptor en la base de datos.
     *
     * @param receiver Instancia con los datos del receptor.
     * @return Objeto del receptor modificada.
     * @throws Exception Error en la base de datos.
     */
    public Receiver update(Receiver receiver) throws Exception;

    /**
     *
     * Elimina un receptor de la base de datos.
     *
     * @param id ID del receptor.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
     * Obtener información de un receptor por estado.
     *
     * @param state Estado de los receptores a ser consultadas
     * @return Instancia con los datos de los receptores.
     * @throws Exception Error en la base de datos.
     */
    public List<Receiver> list(boolean state) throws Exception;
}
