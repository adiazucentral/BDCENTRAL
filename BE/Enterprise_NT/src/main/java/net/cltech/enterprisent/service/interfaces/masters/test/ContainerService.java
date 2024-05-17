package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Container;

/**
 * Interfaz de servicios a la informacion del maestro Recipiente
 *
 * @version 1.0.0
 * @author enavas
 * @since 12/04/2017
 * @see Creación
 */
public interface ContainerService {

    /**
     * Lista los recipientes desde la base de datos
     *
     * @return Lista de recipientes
     * @throws Exception Error en base de datos
     */
    public List<Container> list() throws Exception;

    /**
     * Registra un nuevo recipiente en la base de datos
     *
     * @param container Instancia con los datos del recipiente.
     * @return Instancia con los datos del recipiente.
     * @throws Exception Error en base de datos
     */
    public Container create(Container container) throws Exception;

    /**
     * Obtener informacion de un recipiente en la base de datos
     *
     * @param id Id del resipiente consultado.
     * @param name Nombre del resipiente consultado.
     * @param priority Prioridad consultada.
     * @return Instancia con los datos del recipiente.
     * @throws Exception Error en base de datos
     */
    public Container get(Integer id, String name, Integer priority) throws Exception;

    /**
     * Actualiza la informacion de un recipiente en la base de datos
     *
     * @param container Instancia con los datos del recipiente.
     * @return
     * @throws Exception Error en base de datos
     */
    public Container update(Container container) throws Exception;

    /**
     * Elimina un recipiente en la base de datos
     *
     * @param id Id del resipiente consultado.
     * @throws Exception Error en base de datos
     */
    public void delete(Integer id) throws Exception;

    /**
     * Lista los recipientes desde la base de datos por estado
     *
     * @param state Estado del recipiente
     * @return Lista de recipientes
     * @throws Exception Error en base de datos
     */
    public List<Container> list(boolean state) throws Exception;

    /**
     * Actualizar la prioridad de los recipientes
     *
     * @param containers
     * @throws Exception Error en base de datos
     */
    public void updatePriority(List<Container> containers) throws Exception;


}
