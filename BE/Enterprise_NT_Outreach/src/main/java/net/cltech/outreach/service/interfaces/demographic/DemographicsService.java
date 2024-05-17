package net.cltech.outreach.service.interfaces.demographic;

import net.cltech.outreach.domain.demographic.Demographic;
import net.cltech.outreach.domain.demographic.QueryDemographic;

/**
 * Interfaz de servicios a la informacion para integrar con otros sistemas.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 29/01/2020
 * @see Creación
 */
public interface DemographicsService {
    
    
      /**
     * Metodo para consumir servicios get que devuelve un demografico
     * manejar los errores
     *
     
    
     * @return
     * @throws Exception
     */
    
    public Demographic queryDemographic () throws Exception;

    /**
     * Metodo para consumir servicios get que devuelve la informacion de un demografico consulta web por id
     * @param id
     * @return
     * @throws Exception
     */
    
    public QueryDemographic getQueryDemographic(Integer id) throws Exception;    
    
    
}
