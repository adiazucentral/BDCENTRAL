package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Alarm;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/06/2017
 * @see Creación
 */
public interface AlarmDao
{

    /**
     * Lista alarmas desde la base de datos.
     *
     * @return Lista de alarmas.
     * @throws Exception Error en la base de datos.
     */
    public List<Alarm> list() throws Exception;

    /**
     * Registra alarma en la base de datos.
     *
     * @param create Instancia con los datos de alarma.
     *
     * @return Instancia con los datos de alarma.
     * @throws Exception Error en la base de datos.
     */
    public Alarm create(Alarm create) throws Exception;

    /**
     * Actualiza la información de alarma en la base de datos.
     *
     * @param update Instancia con los datos de alarma.
     *
     * @return Objeto de alarma modificada.
     * @throws Exception Error en la base de datos.
     */
    public Alarm update(Alarm update) throws Exception;

}
