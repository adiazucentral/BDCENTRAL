package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.AnatomicalSite;

/**
 * Interfaz de servicios a la informacion del maestro Sitio Anatomico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/06/2017
 * @see Creación
 */
public interface AnatomicalSiteService
{
    /**
     * Lista los sitios anatomicos desde la base de datos.
     *
     * @return Lista de sitios anatomicos.
     * @throws Exception Error en la base de datos.
     */
    public List<AnatomicalSite> list() throws Exception;

    /**
     * Registra un nuevo sitio anatomico en la base de datos.
     *
     * @param anatomicalSite Instancia con los datos del sitio anatomico.
     * @return Instancia con los datos del sitio anatomico.
     * @throws Exception Error en la base de datos.
     */
    public AnatomicalSite create(AnatomicalSite anatomicalSite) throws Exception;
    
    /**
     * Obtener información de un sitio anatomico por un campo especifico.
     *
     * @param id ID del sitio anatomico a ser consultado.
     * @param name Nombre del sitio anatomico a ser consultado.
     * @param abbr Abreviacion del sitio anatomico a ser consultado.
     * @return Instancia con los datos del sitio anatomico.
     * @throws Exception Error en la base de datos.
     */
    public AnatomicalSite get(Integer id, String name, String abbr) throws Exception;

    /**
     * Actualiza la información de un sitio anatomico en la base de datos.
     *
     * @param anatomicalSite Instancia con los datos del sitio anatomico.
     * @return Objeto del sitio anatomico modificada.
     * @throws Exception Error en la base de datos.
     */
    public AnatomicalSite update(AnatomicalSite anatomicalSite) throws Exception;

    /**
     *
     * Elimina un sitio anatomico de la base de datos.
     *
     * @param id ID del sitio anatomico.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
     * Obtener información de un sitio anatomico por estado.
     *
     * @param state Estado de los sitios anatomicos a ser consultados
     * @return Instancia con los datos de los sitios anatomicos.
     * @throws Exception Error en la base de datos.
     */
    public List<AnatomicalSite> list(boolean state) throws Exception;
}
