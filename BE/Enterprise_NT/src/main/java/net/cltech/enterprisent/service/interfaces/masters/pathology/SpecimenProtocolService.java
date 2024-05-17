/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.SpecimenProtocol;

/**
 * interface de servicios para el maestro de configuracion de protocolo de los especimenes de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/04/2021
 * @see Creacion
 */
public interface SpecimenProtocolService 
{
    /**
    * lista de protocolos.
    *
    *
    * @return la lista de protocolos.
    * @throws Exception Error en la base de datos.
    */
    public List<SpecimenProtocol> list() throws Exception;

    /**
    * Registra un nuevo protocolo en la base de datos.
    *
    * @param protocol Instancia con los datos del protocolo.
    * @return Instancia con los datos del protocolo.
    * @throws Exception Error en la base de datos.
    */
    public SpecimenProtocol create(SpecimenProtocol protocol) throws Exception;

    /**
    * Obtener información de un protocolo por un campo especifico.
    *
    * @param id ID del protocolo a ser consultado
    * @param specimen ID del especimen asociado al protocolo
    * @return Instancia con los datos del protocolo.
    * @throws Exception Error en la base de datos.
    */
    public SpecimenProtocol get(Integer id, Integer specimen) throws Exception;

    /**
    * Actualiza un protocolo de la base de datos.
    *
    * @param protocol Instancia con los datos del protocolo.
    * @return la instancia del objeto protocolo.
    * @throws Exception Error en la base de datos.
    */
    public SpecimenProtocol update(SpecimenProtocol protocol) throws Exception;
    
  
    /**
    * Nuevo registro de laminas de un protocolo en la base de datos.
    *
    * @param protocol Instancia con los datos del protocolo y laminas a insertar.
    *
    * @return Registros afectados
    * @throws Exception Error en la base de datos.
    */
    public int createSheets(SpecimenProtocol protocol) throws Exception;
}
