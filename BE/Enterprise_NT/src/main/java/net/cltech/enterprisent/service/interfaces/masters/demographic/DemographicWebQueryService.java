package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DemographicWebQuery;

/**
 * Interfaz de servicio para la configuracion adicional del demografico con
 * gestion de la consulta web
 *
 * @version 1.0.0
 * @author javila
 * @since 23/01/2020
 * @see Creación
 */
public interface DemographicWebQueryService
{

    /**
     * Lista todos los demograficos consulta web de la base de datos
     *
     * @return
     * @throws Exception Error en la base de datos
     */
    public List<DemographicWebQuery> list() throws Exception;

    /**
     * Crear nuevo registro de demografico consulta web
     *
     * @param demographicWebQuery
     * @return Objeto demografico consulta web
     * @throws Exception Error de base de datos
     */
    public DemographicWebQuery create(DemographicWebQuery demographicWebQuery) throws Exception;

    /**
     * Modificar o Actualizar datos de demograficos consulta web
     *
     * @param demographicWebQuery
     * @return Demografico consulta web
     * @throws Exception Error en la base de datos
     */
    public DemographicWebQuery update(DemographicWebQuery demographicWebQuery) throws Exception;

    /**
     * Retorna el objeto cargado encontrado con el mismo nombre de usuario para
     * validar si son el mismo objeto
     *
     * @param userName
     * @return
     * @throws Exception Error en base de datos
     */
    public DemographicWebQuery get(String userName) throws Exception;

    /**
     * Obtiene una lista de demograficos consulta web según sea su id
     * demografico y su demografico
     *
     * @param idDemographicItem
     * @param demographicwebquery
     * @return
     * @throws Exception Error de base de datos
     */
    public DemographicWebQuery filterByIdDemographic(int idDemographicItem, int demographicwebquery) throws Exception;
    
    
    /**
     * Lista de usuarios tipo demografico consulta web con id y fecha de ultimo ingreso al sistema, para verifiacion de estado
     * @return
     * @throws Exception Error de base de datos
     */
    public List<DemographicWebQuery> listDeactivate() throws Exception;
    
     /**
     * Lista de usuarios tipo demografico consulta web con id y fecha de ultimo ingreso al sistema, para verifiacion de estado
     * @param demographicWebQuery
     * @return
     * @throws Exception Error de base de datos
     */
    public DemographicWebQuery changeStateUser(DemographicWebQuery demographicWebQuery) throws Exception;
}
