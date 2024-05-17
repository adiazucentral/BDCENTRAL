package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Provider;

/**
 * Interfaz de servicios a la informacion del maestro Clientes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/05/2018
 * @see Creación
 */
public interface ProviderService
{

    /**
     * Lista los emisores desde la base de datos.
     *
     * @return Lista de emisores.
     * @throws Exception Error en la base de datos.
     */
    public List<Provider> list() throws Exception;

    /**
     * Registra un nuevo emisor en la base de datos.
     *
     * @param provider Instancia con los datos del emisor.
     *
     * @return Instancia con los datos del emisor.
     * @throws Exception Error en la base de datos.
     */
    public Provider create(Provider provider) throws Exception;

    /**
     * Obtener información de un emisor por un campo especifico.
     *
     * @param id ID del emisor a ser consultado.
     * @param name Nombre del emisor a ser consultado.
     * @param nit Nit del emisor a ser consultado.
     * @param codeEps Codigo EPS del emisor a ser consultado.
     *
     * @return Instancia con los datos del emisor.
     * @throws Exception Error en la base de datos.
     */
    public Provider get(Integer id, String name, String nit, String codeEps) throws Exception;

    /**
     * Actualiza la información de un emisor en la base de datos.
     *
     * @param provider Instancia con los datos del emisor.
     *
     * @return Objeto del provider modificada.
     * @throws Exception Error en la base de datos.
     */
    public Provider update(Provider provider) throws Exception;

    /**
     * Obtener información de un emisor por estado.
     *
     * @param state Estado de los emisores a ser consultadas
     *
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public List<Provider> list(boolean state) throws Exception;
    
    /**
     * Obtener información de un emisor por estado.
     *
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public Provider getProviderParticular() throws Exception;

}
