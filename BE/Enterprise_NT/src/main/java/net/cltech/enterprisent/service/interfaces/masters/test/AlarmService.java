package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Alarm;

/**
 * Interfaz de servicios a la informacion del maestro Alarmas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/06/2017
 * @see Creación
 */
public interface AlarmService
{

    /**
     * Lista alarma desde la base de datos.
     *
     * @return Lista de alarma.
     * @throws Exception Error en la base de datos.
     */
    public List<Alarm> list() throws Exception;

    /**
     * Registra alarma en la base de datos.
     *
     * @param create Instancia con los datos del alarma.
     *
     * @return Instancia con los datos del alarma.
     * @throws Exception Error en la base de datos.
     */
    public Alarm create(Alarm create) throws Exception;

    /**
     * Obtener información de alarma por un campo especifico.
     *
     * @param id ID de alarma a consultar.
     *
     * @return Instancia con los datos del alarma.
     * @throws Exception Error en la base de datos.
     */
    public Alarm filterById(Integer id) throws Exception;

    /**
     * Obtener información de alarma por un campo especifico.
     *
     * @param name name de alarma a consultar.
     *
     * @return Instancia con los datos del alarma.
     * @throws Exception Error en la base de datos.
     */
    public Alarm filterByName(String name) throws Exception;

    /**
     * Obtener información de alarma por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del alarma.
     * @throws Exception Error en la base de datos.
     */
    public List<Alarm> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un alarma en la base de datos.
     *
     * @param update Instancia con los datos del alarma.
     *
     * @return Objeto de alarma modificada.
     * @throws Exception Error en la base de datos.
     */
    public Alarm update(Alarm update) throws Exception;

}
