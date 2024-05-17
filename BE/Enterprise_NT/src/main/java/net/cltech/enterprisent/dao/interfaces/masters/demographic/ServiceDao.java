package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 11/05/2017
 * @see Creación
 */
public interface ServiceDao
{

    /**
     * Lista servicios desde la base de datos.
     *
     * @return Lista de servicios.
     * @throws Exception Error en la base de datos.
     */
    public List<ServiceLaboratory> list() throws Exception;

    /**
     * Registra servicio en la base de datos.
     *
     * @param create Instancia con los datos de servicio.
     *
     * @return Instancia con los datos de servicio.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory create(ServiceLaboratory create) throws Exception;

    /**
     * Obtener información de servicio por nombre.
     *
     * @param name Nombre de servicio a ser consultada.
     *
     * @return Instancia con los datos de servicio.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory filterByName(String name) throws Exception;

    /**
     * Obtener información de servicio por nombre.
     *
     * @param id id de servicio.
     *
     * @return Instancia con los datos de servicio.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory filterById(Integer id) throws Exception;

    /**
     * Actualiza la información de servicio en la base de datos.
     *
     * @param update Instancia con los datos de servicio.
     *
     * @return Objeto de servicio modificada.
     * @throws Exception Error en la base de datos.
     */
    public ServiceLaboratory update(ServiceLaboratory update) throws Exception;

    /**
     * Obtiene servicio por su id, sistema central y numero de orden solo si este 
     * servicio se encuentra homologado retornara el mismo, de lo contrario sera null
     *
     * @param idService
     * @param centralSystem
     * @return Instancia con los datos del servicio.
     * @throws Exception Error en la base de datos.
     */
    default ServiceLaboratory filterHomoligationById(int idService, int centralSystem) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT S.lab10c1 AS lab10c1, ");
            query.append("S.lab10c2 AS lab10c2, ");
            query.append("S.lab10c3 AS lab10c3, ");
            query.append("S.lab10c4 AS lab10c4, ");
            query.append("S.lab10c5 AS lab10c5, ");
            query.append("S.lab07c1 AS lab07c1, ");
            query.append("S.lab10c7 AS lab10c7, ");
            query.append("S.lab10c8 AS lab10c8, ");
            query.append("S.lab10c9 AS lab10c9 ");
            query.append("FROM lab10 S ");
            query.append("JOIN lab117 HS ON (HS.lab117c1 = -6 AND HS.lab117c2 = ").append(idService).append(" AND HS.lab118c1 = ").append(centralSystem).append(") ");
            query.append("WHERE S.lab10c1 = ").append(idService);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                ServiceLaboratory serviceLaboratory = new ServiceLaboratory();
                serviceLaboratory.setId(rs.getInt("lab10c1"));
                serviceLaboratory.setCode(rs.getString("lab10c7"));
                serviceLaboratory.setName(rs.getString("lab10c2"));
                serviceLaboratory.setMin(rs.getInt("lab10c3"));
                serviceLaboratory.setMax(rs.getInt("lab10c4"));
                serviceLaboratory.setExternal((rs.getInt("lab10c5") == 1));
                serviceLaboratory.setState((rs.getInt("lab07c1") == 1));
                serviceLaboratory.setHospitalSampling((rs.getInt("lab10c8") == 1));
                serviceLaboratory.setPriorityAlarm((rs.getInt("lab10c9") == 1));
                return serviceLaboratory; //To change body of generated lambdas, choose Tools | Templates.
            });
        } catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene la conexión a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
