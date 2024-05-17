package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.ingresoLIH.OrdenLaboratorio;

/**
 * Interface para integracion de interfaz de ingreso
 *
 * @version 1.0.0
 * @author BValero
 * @since 22/04/2020
 * @see Creación
 */
public interface IntegrationIngresoLIHService {
    
    /**
     * Obtiene la data para el ingreso del LIH
     * Systema central del LIH
     * @param CentralSystem
     * @param orderInitial
     * @param orderFinal
     * @return 
     * @throws Exception Error de base de datos 
     * o al procesar alguna de las peticiones 
     */
    public List<OrdenLaboratorio> getDataByCentralSystem(int CentralSystem, long orderInitial, long orderFinal) throws Exception;
    
     /**
     * actualiza el estado de la orden enviada al LIH
     * Systema central del LIH
     * @param order
     * @return 
     * @throws Exception Error de base de datos 
     * o al procesar alguna de las peticiones 
     */
    public int updateOrderStateSendLIH(OrdenLaboratorio order) throws Exception;
    
}
