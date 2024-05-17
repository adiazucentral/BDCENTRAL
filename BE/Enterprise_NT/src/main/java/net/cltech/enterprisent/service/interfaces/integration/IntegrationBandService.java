package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.band.BandSample;
import net.cltech.enterprisent.domain.integration.band.BandSampleCheck;
import net.cltech.enterprisent.domain.integration.band.BandVerifiedSample;
import net.cltech.enterprisent.domain.integration.statusBandReason.StatusBandReason;
import net.cltech.enterprisent.domain.integration.statusBandReason.StatusBandReasonUser;
import net.cltech.enterprisent.domain.masters.common.Motive;

/**
 * Interfaz de los servicios para la banda transportadora 
 * y su integración con NT
 * 
 * @version 1.0.0
 * @author Julian
 * @since 22/05/2020
 * @see Creación
 */
public interface IntegrationBandService
{
    /**
     * Verificacion de la muestra desde banda transportadora
     * 
     * @param band
     * @return 1 (Se verifico la muestra), 0 (No se verifico la muestra)
     * @throws java.lang.Exception Error al realizar la verificacion de la muestra
     */
    public Integer bandSampleCheck(BandSampleCheck band) throws Exception;
    
    /**
     * Verificacion de la muestra en el destino desde banda transportadora
     * 
     * @param band
     * @return 1 (Se verifico la muestra), 0 (No se verifico la muestra)
     * @throws java.lang.Exception Error al realizar la verificacion de la muestra
     */
    public Integer bandVerifyDestination(BandSampleCheck band) throws Exception;
    
    /**
     * Obtiene un listado con las muestras verificadas el dia actual (HOY)
     * 
     * @param idDestination
     * @param idBranch
     * @return 
     * @throws Exception Error al obtener las muestras verificadas
     */
    public List<BandVerifiedSample> listVerifiedSamples(int idDestination, int idBranch) throws Exception;
    
    /**
     * Obtiene un listado con las muestras no verificadas el dia actual (HOY)
     * 
     * @param idDestination
     * @param idBranch
     * @return 
     * @throws Exception Error al obtener las muestras verificadas
     */
    public List<BandVerifiedSample> listUnverifiedSamples(int idDestination, int idBranch) throws Exception;
    
    /**
     * Obtiene el listado de muestras
     * @return Lista de muestras
     * @throws Exception Error al obtener las muestras
     */
    public List<BandSample> listSamples() throws Exception;
    
    /**
     * Crea el estado de banda y Motivo
     * @return Lista de estado de banda y Motivo
     * @throws Exception Error al crear
     */
    public StatusBandReason createStatusBandReason(StatusBandReason statusBandReason) throws Exception;
    
    
    /**
     * Obtiene el listado de estado de Banda y Motivo
     * @return Lista de estado de Banda y Motivo
     * @throws Exception Error al obtener el estado de Banda y Motivo
     */
    public List<StatusBandReasonUser> listReason() throws Exception;
    
    
    /**
     * Obtiene la lista de Motivo de la Banda
     * @return la lista de Motivo de la Banda
     * @throws Exception Error al obtener la lista de Motivo de la Banda
     */
    public List<Motive> listReasonBand() throws Exception;
    
    
    
}
