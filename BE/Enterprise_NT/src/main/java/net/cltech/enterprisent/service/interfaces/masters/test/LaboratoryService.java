package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Laboratory;

/**
 * Interfaz de servicios a la informacion del maestro Laboratorio
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/05/2017
 * @see Creación
 */
public interface LaboratoryService
{

    /**
     * Lista laboratorio desde la base de datos.
     *
     * @return Lista de laboratorio.
     * @throws Exception Error en la base de datos.
     */
    public List<Laboratory> list() throws Exception;

    /**
     * Registra laboratorio en la base de datos.
     *
     * @param create Instancia con los datos del laboratorio.
     *
     * @return Instancia con los datos del laboratorio.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory create(Laboratory create) throws Exception;

    /**
     * Obtener información de laboratorio por un campo especifico.
     *
     * @param id ID de laboratorio a consultar.
     *
     * @return Instancia con los datos del laboratorio.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory filterById(Integer id) throws Exception;

    /**
     * Obtener información de laboratorio por un campo especifico.
     *
     * @param name name de laboratorio a consultar.
     *
     * @return Instancia con los datos del laboratorio.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory filterByName(String name) throws Exception;

    /**
     * Obtener información de laboratorio por un campo especifico.
     *
     * @param code codigo de laboratorio a consultar.
     *
     * @return Instancia con los datos del laboratorio.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory filterByCode(Integer code) throws Exception;

    /**
     * Obtener información de laboratorio por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del laboratorio.
     * @throws Exception Error en la base de datos.
     */
    public List<Laboratory> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un laboratorio en la base de datos.
     *
     * @param update Instancia con los datos del laboratorio.
     * @param type
     *
     * @return Objeto de laboratorio modificada.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory update(Laboratory update, Integer type) throws Exception;

    /**
     * Lista de laboratorios con procesamiento
     *
     *
     * @return Lista de laboratorios
     * @throws Exception Error en la base de datos.
     */
    public List<Laboratory> listLaboratorysProcessing() throws Exception;

}
