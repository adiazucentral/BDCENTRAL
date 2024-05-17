package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;

/**
 * Interfaz de servicios a la informacion del maestro Servicio
 *
 * @version 1.0.0
 * @author eacuna
 * @since 11/05/2017
 * @see Creación
 */
public interface ServiceService
{

    /**
     * Lista servicio desde la base de datos.
     *
     * @return Lista de servicio.
     * @throws Exception Error en la base de datos.
     */
    public List<ServiceLaboratory> list() throws Exception;
    
    /**
     * Lista servicio desde la base de datos.
     *
     * @return Lista de servicio.
     * @throws Exception Error en la base de datos.
     */
    public List<ServiceLaboratory> listHospitalary() throws Exception;
    
    
    /**
     * Lista servicio por estado desde la base de datos.
     *
     * @param state Estado para filtrar
     * @return Lista de servicio.
     * @throws Exception Error en la base de datos.
     */
    public List<ServiceLaboratory> list(boolean state) throws Exception;

    /**
     * Registra una nueva servicio en la base de datos.
     *
     * @param create Instancia con los datos del servicio.
     *
     * @return Instancia con los datos del servicio.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory create(ServiceLaboratory create) throws Exception;

    /**
     * Obtener información de servicio por un campo especifico.
     *
     * @param id ID de servicio a consultar.
     *
     * @return Instancia con los datos del servicio.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory filterById(Integer id) throws Exception;

    /**
     * Obtener información de servicio por un campo especifico.
     *
     * @param name name de servicio a consultar.
     *
     * @return Instancia con los datos del servicio.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory filterByName(String name) throws Exception;

    /**
     * Obtener información de servicio por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del servicio.
     * @throws Exception Error en la base de datos.
     */
    public List<ServiceLaboratory> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un servicio en la base de datos.
     *
     * @param update Instancia con los datos del servicio.
     *
     * @return Objeto de servicio modificada.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory update(ServiceLaboratory update) throws Exception;
    
    /**
     * Obtiene servicio por su id, y sistema central solo si este 
     * servicio se encuentra homologado retornara el mismo, de lo contrario sera null
     *
     * @param idService
     * @param centralSystem
     * @return Instancia con los datos del servicio.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory filterHomoligationById(int idService, int centralSystem) throws Exception;
}
