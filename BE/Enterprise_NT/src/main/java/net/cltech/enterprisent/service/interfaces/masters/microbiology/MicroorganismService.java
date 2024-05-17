package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.MicroorganismAntibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;

/**
 * Interfaz de servicios a la informacion del maestro Microorganismos
 *
 * @version 1.0.0
 * @author eacuna
 * @since 20/06/2017
 * @see Creación
 */
public interface MicroorganismService
{

    /**
     * Lista microorganismos desde la base de datos.
     *
     * @return Lista de microorganismos.
     * @throws Exception Error en la base de datos.
     */
    public List<Microorganism> list() throws Exception;

    /**
     * Registra microorganismos en la base de datos.
     *
     * @param create Instancia con los datos del microorganismos.
     *
     * @return Instancia con los datos del microorganismos.
     * @throws Exception Error en la base de datos.
     */
    public Microorganism create(Microorganism create) throws Exception;

    /**
     * Registra microorganismos en la base de datos.
     *
     * @param create Lista microorganismos para insertar.
     *
     * @return cantidad de registros insertados
     *
     * @throws Exception Error en la base de datos.
     */
    public int createAll(List<Microorganism> create) throws Exception;

    /**
     * Obtener información de microorganismos por un campo especifico.
     *
     * @param id ID de microorganismos a consultar.
     *
     * @return Instancia con los datos del microorganismos.
     * @throws Exception Error en la base de datos.
     */
    public Microorganism findById(Integer id) throws Exception;

    /**
     * Obtener información de microorganismos por un campo especifico.
     *
     * @param name name de microorganismos a consultar.
     *
     * @return Instancia con los datos del microorganismos.
     * @throws Exception Error en la base de datos.
     */
    public Microorganism findByName(String name) throws Exception;

    /**
     * Obtener información de microorganismos por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del microorganismos.
     * @throws Exception Error en la base de datos.
     */
    public List<Microorganism> filterByState(boolean state) throws Exception;

    /**
     * Lista relacion antibiograma - prueba de un microorganismo
     *
     * @param id ID del microorganismo.
     *
     * @return Lista microorganismos.
     * @throws Exception Error en la base de datos.
     */
    public List<Microorganism> filterByMicroorganism(Integer id) throws Exception;

    /**
     * Obtiene el antibiograma en el cual se encuentra asociado el
     * microorganismo y el examen.
     *
     * @param idMicroorganism ID del microorganism.
     * @param idTest Id del examen
     *
     * @return Antibiograma.
     * @throws Exception Error en la base de datos.
     */
    public Sensitivity getSensitivity(Integer idMicroorganism, Integer idTest) throws Exception;

    /**
     * Actualiza la información de un microorganismos en la base de datos.
     *
     * @param update Instancia con los datos del microorganismos.
     *
     * @return Objeto de microorganismos modificada.
     * @throws Exception Error en la base de datos.
     */
    public Microorganism update(Microorganism update) throws Exception;

    /**
     * Actualiza antibiograma de los microorganismos enviados.
     *
     * @param update Lista microorganismos.
     *
     * @return Numero de registros actualizados.
     * @throws Exception Error en la base de datos.
     */
    public Integer sensitivityUpdate(List<Microorganism> update) throws Exception;

    /**
     * Lista de antibioticos por microorganismo desde la base de datos.
     *
     * @return Lista de microorganismos.
     * @throws Exception Error en la base de datos.
     */
    public List<MicroorganismAntibiotic> listMicroorganimAntibiotic() throws Exception;

    /**
     * Lista de antibioticos por microorganismo desde la base de datos.
     *
     * @param idMicroorganism Id del microorganismo.
     * @return Lista de microorganismos.
     * @throws Exception Error en la base de datos.
     */
    public List<MicroorganismAntibiotic> listMicroorganimAntibiotic(int idMicroorganism) throws Exception;

    /**
     * Obtiene un microorganismo - antibiotico desde la base de datos.
     *
     * @param idMicroorganism Id del Microorganismo
     * @param idAntibiotic Id del Antibiotico
     * @param method Metodo
     * @param interpretation Interpretación
     *
     * @return Microorganismo - Antiotico.
     *
     * @throws Exception Error en la base de datos.
     */
    public MicroorganismAntibiotic getMicroorganismAntibiotic(int idMicroorganism, int idAntibiotic, short method, short interpretation) throws Exception;

    /**
     * Registra microorganismo - antibiograma en la base de datos.
     *
     * @param microAntibiotic Instancia microorganismo - antibiotico.
     *
     * @return Instancia Microorganismo - Antibiotico.
     * @throws Exception Error en la base de datos.
     */
    public MicroorganismAntibiotic create(MicroorganismAntibiotic microAntibiotic) throws Exception;

    /**
     * Actualiza microorganismo - antibiograma en la base de datos.
     *
     * @param microAntibiotic Instancia microorganismo - antibiograma.
     *
     * @return Objeto microorganismo - antibiograma.
     * @throws Exception Error en la base de datos.
     */
    public MicroorganismAntibiotic update(MicroorganismAntibiotic microAntibiotic) throws Exception;

    /**
     * Elimina microorganismo - antibiograma
     *
     * @param idMicroorganism Id del Microorganismo
     * @param idAntibiotic Id del Antibiotico
     * @param method Metodo
     * @param interpretation Interpretación
     *
     */
    public void delete(int idMicroorganism, int idAntibiotic, short method, short interpretation) throws Exception;

}
