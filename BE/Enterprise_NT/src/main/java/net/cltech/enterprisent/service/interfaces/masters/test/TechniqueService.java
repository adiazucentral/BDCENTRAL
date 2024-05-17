package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Technique;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la
 * tecnica
 *
 * @version 1.0.0
 * @author mperdomo
 * @since 18/04/2017
 * @see Creación
 */
public interface TechniqueService
{

    /**
     * Registra una nueva tecnica en el servicio
     *
     * @param technique Instancia con los datos de la tecnica.
     *
     * @return {@link net.cltech.enterprisent.domain.masters.test.Technique}
     * @throws Exception Error en el servicio
     */
    public Technique create(Technique technique) throws Exception;

    /**
     * Lista las tecnicas desde el servicio
     *
     * @return Lista de tecnicas.
     * @throws Exception Error en el servicio
     */
    public List<Technique> list() throws Exception;

    /**
     * Lee la información de una tecnica.
     *
     * @param id ID de la tecnica a ser consultada.
     *
     * @return Instancia con los datos de la tecnica.
     * @throws Exception Error en el servicio
     */
    public Technique findById(Integer id) throws Exception;
    
    /**
     * Lee la información de una tecnica.
     *
     * @param code Código de la tecnica a ser consultada.
     *
     * @return Instancia con los datos de la tecnica.
     * @throws Exception Error en el servicio
     */
    public Technique findByCode(String code) throws Exception;

    /**
     * Lee la información de una tecnica.
     *
     * @param name Nombre de la tecnica a ser consultada.
     *
     * @return Instancia con los datos de la tecnica.
     * @throws Exception Error en el servicio
     */
    public Technique findByName(String name) throws Exception;

    /**
     * Actualiza la información de una tecnica en el servicio
     *
     * @param technique Instancia con los datos de la tecnica.
     * @return Objeto 
     * {@link net.cltech.enterprisent.domain.masters.test.Technique} Modificado
     *
     * @throws Exception Error en el servicio
     */
    public Technique update(Technique technique) throws Exception;

    /**
     *
     * Elimina una tecnica de el servicio
     *
     * @param id ID de la tecnica.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
     * Lista las tecnicas desde el servicio por estado
     *
     * @param state Estado de las tecnica a ser consultadas.
     * @return Lista de tecnicas.
     * @throws Exception Error en el servicio
     */
    public List<Technique> list(boolean state) throws Exception;
}
