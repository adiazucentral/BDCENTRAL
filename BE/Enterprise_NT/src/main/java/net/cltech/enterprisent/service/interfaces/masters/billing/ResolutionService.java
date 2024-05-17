package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.FilterResolution;
import net.cltech.enterprisent.domain.masters.billing.Resolution;
import net.cltech.enterprisent.domain.masters.billing.SingularResolution;

/**
 * Interfaz de servicios a la informacion del maestro Resolucion
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/05/2018
 * @see Creación
 */
public interface ResolutionService
{
    /**
     * Lista las resoluciones desde la base de datos.
     *
     * @return Lista de resoluciones.
     * @throws Exception Error en la base de datos.
     */
    public List<Resolution> list() throws Exception;
    
    
    /**
     * obtiene la resolucion asociada a un proveedor(entidad).
     *
     * @param provider
     * @return Lista de resoluciones.
     * @throws Exception Error en la base de datos.
    */
    public int getResolutionIdByProvider(int provider) throws Exception;

    /**
     * Registra una nueva resolución en la base de datos.
     *
     * @param resolution Instancia con los datos de la resolución.
     * @return Instancia con los datos de la resolución.
     * @throws Exception Error en la base de datos.
     */
    public Resolution create(Resolution resolution) throws Exception;
    
    /**
     * Obtener información de una resolución por un campo especifico.
     *
     * @param id ID de la resolución a ser consultada.
     * 
     * @return Instancia con los datos del receptor.
     * @throws Exception Error en la base de datos.
     */
    public Resolution get(Integer id) throws Exception;

    /**
     * Actualiza la información de una resolución en la base de datos.
     *
     * @param resolution Instancia con los datos de la resolución.
     * @return Objeto de la resolución modificada.
     * @throws Exception Error en la base de datos.
     */
    public Resolution update(Resolution resolution) throws Exception;

    /**
     *
     * Elimina una resolución de la base de datos.
     *
     * @param id ID de la resolución.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
     * Obtener información de una resolución por estado.
     *
     * @param state Estado de las resoluciones a ser consultadas
     * @return Instancia con los datos de las resoluciones.
     * @throws Exception Error en la base de datos.
     */
    public List<Resolution> list(boolean state) throws Exception;
    
    /**
     * Consulta la resolución 4505 dependiendo de los filtros enviados bajo el servicio
     *
     * @param filter Filtros usados para obtener la información necesaria de la resolución
     * @return resolución 4505
     * @throws Exception Error en el servicio.
     */
    public List<SingularResolution> getResolution4505(FilterResolution filter) throws Exception;
}
