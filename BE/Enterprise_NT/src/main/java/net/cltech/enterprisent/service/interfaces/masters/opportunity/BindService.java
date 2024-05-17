package net.cltech.enterprisent.service.interfaces.masters.opportunity;

import java.util.List;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;

/**
 * Interfaz de servicios a la informacion del maestro Sedes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 15/02/2018
 * @see Creación
 */
public interface BindService
{

    /**
     * Lista las clases desde la base de datos.
     *
     * @return Lista de clases.
     * @throws Exception Error en la base de datos.
     */
    public List<Bind> list() throws Exception;

    /**
     * Registra una nueva clase en la base de datos.
     *
     * @param bind Instancia con los datos de la clase.
     *
     * @return Instancia con los datos de lka clase.
     * @throws Exception Error en la base de datos.
     */
    public Bind create(Bind bind) throws Exception;

    /**
     * Obtener información de una clase por un campo especifico.
     *
     * @param id ID de la clase a ser consultada.
     * @param name Nombre de la clase a ser consultada.
     *
     * @return Instancia con los datos del clase.
     * @throws Exception Error en la base de datos.
     */
    public Bind get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de una clase en la base de datos.
     *
     * @param bind Instancia con los datos de la clase.
     *
     * @return Objeto de la clase modificada.
     * @throws Exception Error en la base de datos.
     */
    public Bind update(Bind bind) throws Exception;

    /**
     * Lista las clases desde la base de datos por estado.
     *
     * @param state Estado.
     *
     * @return Lista de clases.
     * @throws Exception Error en la base de datos.
     */
    public List<Bind> list(boolean state) throws Exception;

}
