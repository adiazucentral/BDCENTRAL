package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Antibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.AntibioticBySensitivity;

/**
 * Interfaz de servicios a la informacion del maestro Antibioticos
 *
 * @version 1.0.0
 * @author eacuna
 * @since 07/06/2017
 * @see Creación
 */
public interface AntibioticService
{

    /**
     * Lista antibioticos desde la base de datos.
     *
     * @return Lista de antibioticos.
     * @throws Exception Error en la base de datos.
     */
    public List<Antibiotic> list() throws Exception;

    /**
     * Registra antibioticos en la base de datos.
     *
     * @param create Instancia con los datos del antibioticos.
     *
     * @return Instancia con los datos del antibioticos.
     * @throws Exception Error en la base de datos.
     */
    public Antibiotic create(Antibiotic create) throws Exception;

    /**
     * Obtener información de antibioticos por un campo especifico.
     *
     * @param id ID de antibioticos a consultar.
     *
     * @return Instancia con los datos del antibioticos.
     * @throws Exception Error en la base de datos.
     */
    public Antibiotic findById(Integer id) throws Exception;

    /**
     * Obtener información de antibioticos por un campo especifico.
     *
     * @param name name de antibioticos a consultar.
     *
     * @return Instancia con los datos del antibioticos.
     * @throws Exception Error en la base de datos.
     */
    public Antibiotic findByName(String name) throws Exception;

    /**
     * Obtener información de antibioticos por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del antibioticos.
     * @throws Exception Error en la base de datos.
     */
    public List<Antibiotic> filterByState(boolean state) throws Exception;

    /**
     * Obtener información de antibioticos por un campo especifico.
     *
     * @param id Id del antibiograma.
     *
     * @return Instancia con los datos del antibioticos.
     * @throws Exception Error en la base de datos.
     */
    public List<AntibioticBySensitivity> filterBySensitivity(int id) throws Exception;

    /**
     * Actualiza la información de un antibioticos en la base de datos.
     *
     * @param update Instancia con los datos del antibioticos.
     *
     * @return Objeto de antibioticos modificada.
     * @throws Exception Error en la base de datos.
     */
    public Antibiotic update(Antibiotic update) throws Exception;

}
