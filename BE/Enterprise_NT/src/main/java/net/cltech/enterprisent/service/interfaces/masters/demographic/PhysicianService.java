package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Physician;

/**
 * Interfaz de servicios a la informacion del maestro Médico
 *
 * @version 1.0.0
 * @author eacuna
 * @since 24/05/2017
 * @see Creación
 */
public interface PhysicianService
{

    /**
     * Lista médico desde la base de datos.
     *
     * @return Lista de médico.
     * @throws Exception Error en la base de datos.
     */
    public List<Physician> list() throws Exception;

    /**
     * Registra médico en la base de datos.
     *
     * @param create Instancia con los datos del médico.
     *
     * @return Instancia con los datos del médico.
     * @throws Exception Error en la base de datos.
     */
    public Physician create(Physician create) throws Exception;

    /**
     * Obtener información de médico por un campo especifico.
     *
     * @param id ID de médico a consultar.
     *
     * @return Instancia con los datos del médico.
     * @throws Exception Error en la base de datos.
     */
    public Physician filterById(Integer id) throws Exception;

    /**
     * Obtener información de médico por un campo especifico.
     *
     * @param identification identificación de médico a consultar.
     *
     * @return Instancia con los datos del médico.
     * @throws Exception Error en la base de datos.
     */
    public Physician filterByIdentification(String identification) throws Exception;

    /**
     * Obtener información de médico por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del médico.
     * @throws Exception Error en la base de datos.
     */
    public List<Physician> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un médico en la base de datos.
     *
     * @param update Instancia con los datos del médico.
     *
     * @return Objeto de médico modificada.
     * @throws Exception Error en la base de datos.
     */
    public Physician update(Physician update) throws Exception;

}
