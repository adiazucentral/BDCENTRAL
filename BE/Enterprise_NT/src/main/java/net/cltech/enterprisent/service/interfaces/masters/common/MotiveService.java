package net.cltech.enterprisent.service.interfaces.masters.common;

import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Motive;

/**
 * Interfaz de servicios a la informacion del maestro Motivo
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/06/2017
 * @see Creación
 */
public interface MotiveService
{

    /**
     * Lista los motivos desde la base de datos.
     *
     * @return Lista de motivos.
     * @throws Exception Error en la base de datos.
     */
    public List<Motive> list() throws Exception;
    
    
    /**
     * Lista los motivos desde la base de datos.
     *     
     * @return Lista de motivos listMotivePendingTest.
     * @throws Exception Error en la base de datos.
     */
 public List<Motive> listMotivePendingTest() throws Exception;

    /**
     * Registra un nuevo motivo en la base de datos.
     *
     * @param motive Instancia con los datos del motivo.
     * @return Instancia con los datos del motivo.
     * @throws Exception Error en la base de datos.
     */
    public Motive create(Motive motive) throws Exception;

    /**
     * Obtener información de un motivo por un campo especifico.
     *
     * @param id ID del motivo a ser consultada.
     * @param name Nombre del motivo a ser consultada.
     * @return Instancia con los datos del motivo.
     * @throws Exception Error en la base de datos.
     */
    public Motive get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de un motivo en la base de datos.
     *
     * @param motive Instancia con los datos del motivo.
     * @return Objeto del motivo modificada.
     * @throws Exception Error en la base de datos.
     */
    public Motive update(Motive motive) throws Exception;

    /**
     *
     * Elimina un motivo de la base de datos.
     *
     * @param id ID del motivo.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Obtener información de un motivo por estado.
     *
     * @param state Estado de los motivos a ser consultados
     * @return Instancia con los datos del motivo.
     * @throws Exception Error en la base de datos.
     */
    public List<Motive> list(boolean state) throws Exception;
    
    
    /**
    * Obtener la lista de motivos de patologia por estado.
    *
    * @param state Estado de los motivos de patologia a ser consultados
    * @return Instancia con los datos del motivo de patologia.
    * @throws Exception Error en la base de datos.
    */
    public List<Motive> listMotivePathology(boolean state) throws Exception;
    
    /**
     * Obtener lista de motivos por el tipo de motivo 
     *
     * @param type
     * 
     * @return Lista de motivos
     * @throws Exception Error en la base de datos.
     */
    public List<Motive> getListMotivesByType(Integer type) throws Exception;
}
