package net.cltech.enterprisent.service.interfaces.masters.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;

/**
 * Interfaz de servicios a la informacion del maestro Destinos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 27/07/2017
 * @see Creación
 */
public interface DestinationService
{

    /**
     * Lista los destinos desde la base de datos.
     *
     * @return Lista de destinos.
     * @throws Exception Error en la base de datos.
     */
    public List<Destination> list() throws Exception;

    /**
     * Registra un nuevo destino en la base de datos.
     *
     * @param destination Instancia con los datos del destino.
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    public Destination create(Destination destination) throws Exception;

    /**
     * Obtener información de un destino por un campo especifico.
     *
     * @param id ID del destino a ser consultado.
     * @param code Codigo del destino a ser consultado.
     * @param name Nombre del destino a ser consultado.
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    public Destination get(Integer id, String code, String name) throws Exception;

    /**
     * Actualiza la información de un destino en la base de datos.
     *
     * @param destination Instancia con los datos del destino.
     * @return Objeto del destino modificada.
     * @throws Exception Error en la base de datos.
     */
    public Destination update(Destination destination) throws Exception;

    /**
     *
     * Elimina un destino de la base de datos.
     *
     * @param id ID del destino.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Obtener información de un destino por estado.
     *
     * @param state Estado de los destinos a ser consultados
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    public List<Destination> list(boolean state) throws Exception;

    /**
     * Lista la ruta asociada a una sede, una muestra y un tipo de orden desde
     * la base de datos.
     *
     * @param idBranch Id de la sede en la que se debe hacer la consulta.
     * @param idSample Id de la muestra en la que se debe hacer la consulta.
     * @param idOrderType Id del tipo de orden en la que se debe hacer la
     * consulta.
     * @param assignment Indica si la consulta trae examenes o servicios.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    public AssignmentDestination getRoute(Integer idBranch, Integer idSample, Integer idOrderType, boolean assignment) throws Exception;

    /**
     * Registra un nuevo asignacion de destino en la base de datos.
     *
     * @param assignment Instancia con los datos de la asignacion de destinos.
     *
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    public AssignmentDestination createAssignment(AssignmentDestination assignment) throws Exception;

    /**
     * Registra una nueva oportunidad de la muestra en la base de datos.
     *
     * @param destinations Instancia con los datos de los destinos.
     *
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    public int createSampleOportunity(List<DestinationRoute> destinations) throws Exception;

}
