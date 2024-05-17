package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.HomeboundBox;

/**
 * Interfaz de servicios para la información del maestro caja homebound
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 16/07/2021
 * @see Creación
 */
public interface HomeboundBoxService
{
    /**
     * Consulta la lista de registros de la caja por el id de la orden
     * 
     * @param order id de la orden
     * @return Lista de registros en caja para esa orden
     * @throws Exception 
     */
    public List<HomeboundBox> get(long order) throws Exception;
    
     /**
     * Ocrea uan caja de homebound
     * 
     * @param homeboundBox
     * @return HomeboundBox
     * @throws Exception 
     */
     public HomeboundBox create(HomeboundBox homeboundBox) throws Exception;
}
