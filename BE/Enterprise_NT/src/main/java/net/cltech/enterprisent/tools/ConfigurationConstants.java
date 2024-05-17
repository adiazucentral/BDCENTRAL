package net.cltech.enterprisent.tools;

/**
 * Valores constantes de las llaves de configuracion (lab98)
 *
 * @version 1.0.0
 * @author eacuna
 * @since 13/08/2018
 * @see Creación
 */
public class ConfigurationConstants
{

    /**
     * Almacena el tipo de Numeracion
     */
    public final static String KEY_NUMERATION = "TipoNumeroOrden";
    /**
     * Tipo de numeración General
     */
    public final static String NUMERATION_GENERAL = "General";
    /**
     * Tipo de numeración por servicio
     */
    public final static String NUMERATION_SERVICE = "Servicio";
    /**
     * Tipo de numeración por sede
     */
    public final static String NUMERATION_BRANCH = "Sede";
    /**
     * Almacena el tipo de Numeracion para factura
     */

    /**
     * Propiedad que indica si se maneja tarifa en el sistema (true/false)
     */
    public final static String KEY_RATE_ACTIVE = "ManejoTarifa";

    /**
     * Almacena el tipo de numeracion de los casos de patologia
     */
    public final static String CASE_NUMERATION = "TipoNumeroCaso";
    /**
     * Tipo de numeración diaria
     */
    public final static String NUMERATION_DAILY = "Diario";
    /**
     * Tipo de numeración mensual
     */
    public final static String NUMERATION_MONTHLY = "Mensual";
    /**
     * Tipo de numeración anual
     */
    public final static String NUMERATION_YEARLY = "Anual";
    /**
     * Almacena si el nombre del laboratorio para los tiempos de oportunidad es
     * requerido o no
     */
    public final static String NAME_LABORATORY_TOP = "NombreLaboratorioTOP";

}
