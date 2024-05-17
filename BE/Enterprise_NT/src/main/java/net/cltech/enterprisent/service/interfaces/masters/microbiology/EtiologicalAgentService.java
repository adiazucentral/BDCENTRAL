/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.EtiologicalAgent;

/**
 * Interfaz de servicios a la informacion del maestro de agentes etiologicos
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/06/2022
 * @see Creación
 */
public interface EtiologicalAgentService {
    
    /**
     * Lista agentes etiologicos desde la base de datos.
     *
     * @return Lista de agentes etiologicos.
     * @throws Exception Error en la base de datos.
     */
    public List<EtiologicalAgent> list() throws Exception;

    /**
     * Registra agentes etiologicos en la base de datos.
     *
     * @param create Instancia con los datos del agente etiologico.
     *
     * @return Instancia con los datos del agente etiologico.
     * @throws Exception Error en la base de datos.
     */
    public EtiologicalAgent create(EtiologicalAgent create) throws Exception;

    /**
     * Obtener información de agente etiologico por id.
     *
     * @param id ID del agente etiologico a consultar.
     *
     * @return Instancia con los datos del agente etiologico.
     * @throws Exception Error en la base de datos.
     */
    public EtiologicalAgent findById(Integer id) throws Exception;

    /**
     * Actualiza la información de un agente etiologico en la base de datos.
     *
     * @param update Instancia con los datos del agente etiologico.
     *
     * @return Objeto de agente etiologico modificado.
     * @throws Exception Error en la base de datos.
     */
    public EtiologicalAgent update(EtiologicalAgent update) throws Exception;
}
