package net.cltech.enterprisent.service.interfaces.masters.tracking;

import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.common.Tracking;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.tracking.AuditFilter;

/**
 * Interfaz de servicios a la informacion de la auditoria
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creacion
 *
 * @version 1.0.0
 * @author enavas
 * @since 28/04/2017
 * @see Creacion de los Servicios de la auditoria con base de datos relacionales
 */
public interface TrackingService
{

    /**
     * Registra un objeto de configuracion en la auditoria
     *
     * @param oldObject Antiguo objeto
     * @param newObject Nuevo objeto
     * @param type Tipo de objeto
     */
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type);

    /**
     * Registra un objeto de configuracion en la auditoria
     *
     * @param oldObject Antiguo objeto
     * @param newObject Nuevo objeto
     * @param type Tipo de objeto
     * @param comment
     */
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type, String comment);

    /**
     * Registra un objeto de operación en la auditoria
     *
     * @param audit Representa la clase de la auditoria para la operación.
     * @throws Exception Error presentando registrando en la auditoria
     */
    public void registerOperationTracking(List<AuditOperation> audit) throws Exception;
    

        /**
     * Registra un objeto de operación en la auditoria
     *
     * @param audit Representa la clase de la auditoria para la operación.
     * @param mw
     *
     * @throws Exception Error presentando registrando en la auditoria
     */
    public void registerOperationTracking(List<AuditOperation> audit, int mw) throws Exception;

    
    /**
     * Obtener informacion de la auditoria en la base de datos
     *
     * @param initialDate Fecha inicial.
     * @param finalDate Fecha final.
     * @param module Nombre del modulo consultado.
     * @param iduser Id del usuario consultado.
     *
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    public List<Tracking> get(int initialDate, int finalDate, String module, Integer iduser) throws Exception;

    /**
     * Obtener informacion de la auditoria en la base de datos
     *
     * @param initialDate Fecha inicial.
     * @param finalDate Fecha final.
     * @param module Nombre del modulo consultado.
     * @param iduser Id del usuario consultado.
     *
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    public List<Tracking> get(String initialDate, String finalDate, String module, Integer iduser) throws Exception;

    /**
     * Obtener informacion de la auditoria en la base de datos
     *
     * @param initialDate Fecha inicial.
     * @param finalDate Fecha final.
     *
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    public List<Tracking> get(int initialDate, int finalDate) throws Exception;

    /**
     * Obtener informacion del usuario obtenida del token
     *
     * @return Instancia con los datos de la autenticacion del usuario.
     * @throws Exception Error en base de datos
     */
    public AuthorizedUser getRequestUser() throws Exception;

    /**
     * Obtener una lista de constantes de configuracion para la auditoria
     *
     * @return .
     * @throws Exception Error en base de datos
     */
    public HashMap<String, HashMap<String, String>> getConstants() throws Exception;
    
    /**
     * Registra un objeto de configuracion en la auditoria
     *
     * @param oldObject Antiguo objeto
     * @param newObject Nuevo objeto
     * @param type Tipo de objeto
     * @param userAuth
     */
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type, AuthorizedUser userAuth);
    
    /**
     * Registra un objeto de operación de la facturación en la auditoria correspondiente
     *
     * @param audit Representa la clase de la auditoria para la operación de facturación.
     * @throws Exception Error presentando registrando en la auditoria
     */
    public void registerInvoiceAudit(AuditOperation audit) throws Exception;
    
    /**
     * Obtener informacion de la auditoria en la base de datos
     *
     * @param filters
     *
     * @return Instancia con los datos de la auditoria.
     * @throws Exception Error en base de datos
     */
    public List<Tracking> getAuditFilter(AuditFilter filters) throws Exception;
}
