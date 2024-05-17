package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Specialist;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 10/05/2017
 * @see Creación
 */
public interface SpecialistDao
{

    /**
     * Lista especialistas desde la base de datos.
     *
     * @return Lista de especialistas.
     * @throws Exception Error en la base de datos.
     */
    public List<Specialist> list() throws Exception;

    /**
     * Registra especialista en la base de datos.
     *
     * @param create Instancia con los datos de especialista.
     *
     * @return Instancia con los datos de especialista.
     * @throws Exception Error en la base de datos.
     */
    public Specialist create(Specialist create) throws Exception;

    /**
     * Obtener información de especialista por nombre.
     *
     * @param name Nombre de especialista a ser consultada.
     *
     * @return Instancia con los datos de especialista.
     * @throws Exception Error en la base de datos.
     */
    public Specialist filterByName(String name) throws Exception;

    /**
     * Obtener información de especialista por nombre.
     *
     * @param id id de especialista.
     *
     * @return Instancia con los datos de especialista.
     * @throws Exception Error en la base de datos.
     */
    public Specialist filterById(Integer id) throws Exception;

    /**
     * Actualiza la información de especialista en la base de datos.
     *
     * @param update Instancia con los datos de especialista.
     *
     * @return Objeto de especialista modificada.
     * @throws Exception Error en la base de datos.
     */
    public Specialist update(Specialist update) throws Exception;

}
