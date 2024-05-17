package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.AntibioticBySensitivity;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;

/**
 * Interfaz de servicios a la informacion del maestro Antibiogramas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 27/06/2017
 * @see Creación
 */
public interface SensitivityService
{

    /**
     * Lista antibiogramas desde la base de datos.
     *
     * @return Lista de antibiogramas.
     * @throws Exception Error en la base de datos.
     */
    public List<Sensitivity> list() throws Exception;

    /**
     * Registra antibiogramas en la base de datos.
     *
     * @param create Instancia con los datos del antibiogramas.
     *
     * @return Instancia con los datos del antibiogramas.
     * @throws Exception Error en la base de datos.
     */
    public Sensitivity create(Sensitivity create) throws Exception;

    /**
     * Obtener información de antibiogramas por un campo especifico.
     *
     * @param id ID de antibiogramas a consultar.
     *
     * @return Instancia con los datos del antibiogramas.
     * @throws Exception Error en la base de datos.
     */
    public Sensitivity findById(Integer id) throws Exception;

    /**
     * Obtener información de antibiogramas por un campo especifico.
     *
     * @param name name de antibiogramas a consultar.
     *
     * @return Instancia con los datos del antibiogramas.
     * @throws Exception Error en la base de datos.
     */
    public Sensitivity findByName(String name) throws Exception;

    /**
     * Obtener información de antibiogramas por un campo especifico.
     *
     * @param code código de antibiograma.
     *
     * @return Instancia con los datos del antibiogramas.
     * @throws Exception Error en la base de datos.
     */
    public Sensitivity findByCode(String code) throws Exception;

    /**
     * Obtener información de antibiogramas por un campo especifico.
     *
     * @param abbr Abreviatura.
     *
     * @return Instancia con los datos del antibiogramas.
     * @throws Exception Error en la base de datos.
     */
    public Sensitivity findByAbbr(String abbr) throws Exception;

    /**
     * Obtener información de antibiogramas por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del antibiogramas.
     * @throws Exception Error en la base de datos.
     */
    public List<Sensitivity> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un antibiogramas en la base de datos.
     *
     * @param update Instancia con los datos antibiograma.
     *
     * @return Objeto de antibiogramas modificada.
     * @throws Exception Error en la base de datos.
     */
    public Sensitivity update(Sensitivity update) throws Exception;

    /**
     * Actualiza la información de un antibiogramas en la base de datos.
     *
     * @param update Lista de antibiogramas para asignar.
     *
     * @return Lista de antibioticos asignados
     *
     * @throws Exception Error en la base de datos.
     */
    public int assignAntibiotics(List<AntibioticBySensitivity> update) throws Exception;

    /**
     * Elimina la relación de antibioticos
     *
     * @param id id del antibiograma
     *
     * @return
     *
     * @throws Exception
     */
    public int deleteAntibiotics(Integer id) throws Exception;
    
    /**
     * Obtiene los antibiogramas relacionados a una orden y a un examen
     *
     * @param idOrder
     * @param idTest
     * @return numero de registros eliminados
     * @throws Exception
     */
    public List<Sensitivity> getAntibiogramByOrderIdByTestId(long idOrder, int idTest) throws Exception;
    
    /**
    * Actualiza el antibiograma de la lista de microorganismos
    *
    * @param sensitivity Instancia con los datos antibiograma.
    *
    * @return
    *
    * @throws Exception
    */
    public int generalSensitivity(Sensitivity sensitivity) throws Exception;
}
