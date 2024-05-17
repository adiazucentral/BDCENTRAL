package net.cltech.enterprisent.service.interfaces.masters.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.masters.tracking.Refrigerator;

/**
 * Interfaz de servicios a la informacion del maestro Neveras
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/06/2017
 * @see Creación
 */
public interface RefrigeratorService
{

    /**
     * Lista neveras desde la base de datos.
     *
     * @return Lista de neveras.
     * @throws Exception Error en la base de datos.
     */
    public List<Refrigerator> list() throws Exception;

    /**
     * Registra neveras en la base de datos.
     *
     * @param create Instancia con los datos del neveras.
     *
     * @return Instancia con los datos del neveras.
     * @throws Exception Error en la base de datos.
     */
    public Refrigerator create(Refrigerator create) throws Exception;

    /**
     * Obtener información de neveras por un campo especifico.
     *
     * @param id ID de neveras a consultar.
     *
     * @return Instancia con los datos del neveras.
     * @throws Exception Error en la base de datos.
     */
    public Refrigerator filterById(Integer id) throws Exception;

    /**
     * Obtener información de neveras por un campo especifico.
     *
     * @param name name de neveras a consultar.
     *
     * @return Instancia con los datos del neveras.
     * @throws Exception Error en la base de datos.
     */
    public Refrigerator filterByName(String name) throws Exception;

    /**
     * Obtener información de neveras por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del neveras.
     * @throws Exception Error en la base de datos.
     */
    public List<Refrigerator> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un neveras en la base de datos.
     *
     * @param update Instancia con los datos del neveras.
     *
     * @return Objeto de neveras modificada.
     * @throws Exception Error en la base de datos.
     */
    public Refrigerator update(Refrigerator update) throws Exception;

}
