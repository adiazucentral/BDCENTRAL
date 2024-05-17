package net.cltech.enterprisent.service.interfaces.tools.events;

/**
 * Interfaz de servicios a la informacion de Eventos Orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 11/01/2019
 * @see Creación
 */
public interface EventsSampleTrackingService
{

    /**
     * Inserta la toma de la muestra
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @return
     */
    public void sampleTracking(long idOrder, String codeSample);

    /**
     * Verificar la muestra
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @return
     */
    public void sampleVerify(long idOrder, String codeSample);

}
