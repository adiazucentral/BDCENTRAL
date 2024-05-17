package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.MicrobiologyDestination;

/**
 * Interfaz de servicios a la informacion del maestro Destinos de Microbiologia
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/02/2018
 * @see Creación
 */
public interface MicrobiologyDestinationService
{

    /**
     * Lista los destinos desde la base de datos.
     *
     * @return Lista de destinos.
     * @throws Exception Error en la base de datos.
     */
    public List<MicrobiologyDestination> list() throws Exception;

    /**
     * Registra un nuevo destino en la base de datos.
     *
     * @param destination Instancia con los datos del destino.
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    public MicrobiologyDestination create(MicrobiologyDestination destination) throws Exception;

    /**
     * Obtener información de un destino por un campo especifico.
     *
     * @param id ID del destino a ser consultado.
     * @param code Codigo del destino a ser consultado.
     * @param name Nombre del destino a ser consultado.
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    public MicrobiologyDestination get(Integer id, String code, String name) throws Exception;

    /**
     * Actualiza la información de un destino en la base de datos.
     *
     * @param destination Instancia con los datos del destino.
     * @return Objeto del destino modificada.
     * @throws Exception Error en la base de datos.
     */
    public MicrobiologyDestination update(MicrobiologyDestination destination) throws Exception;

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
    public List<MicrobiologyDestination> list(boolean state) throws Exception;
}
