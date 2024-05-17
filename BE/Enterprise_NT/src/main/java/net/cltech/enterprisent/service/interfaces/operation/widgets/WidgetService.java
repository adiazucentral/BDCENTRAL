package net.cltech.enterprisent.service.interfaces.operation.widgets;

import net.cltech.enterprisent.domain.operation.widgets.WidgetEntry;
import net.cltech.enterprisent.domain.operation.widgets.WidgetOrderEntry;
import net.cltech.enterprisent.domain.operation.widgets.WidgetSample;

/**
 * Interfaz de servicios a la informacion de los widgets
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/12/2017
 * @see Creación
 */
public interface WidgetService
{

    /**
     * Obtiene los valores para los widgets de ruta de la muestra.
     *
     * @param idBranch Id de la sede.
     * @return Valores para los widgets de ruta de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public WidgetSample getWidgetSample(int idBranch) throws Exception;

    /**
     * Incrementar muestras ingresadas en widgets.
     *
     * @param idBranch Id de la sede.
     * @throws Exception Error en la base de datos.
     */
    public void addOrderedWidget(int idBranch) throws Exception;

    /**
     * Incrementar muestras verificadas en widgets.
     *
     * @param idBranch Id de la sede.
     * @throws Exception Error en la base de datos.
     */
    public void addVerifiedWidget(int idBranch) throws Exception;

    /**
     * Incrementar muestras rechazadas en widgets.
     *
     * @param idBranch Id de la sede.
     * @throws Exception Error en la base de datos.
     */
    public void addRejectedWidget(int idBranch) throws Exception;

    /**
     * Incrementar muestras retomadas en widgets.
     *
     * @param idBranch Id de la sede.
     * @throws Exception Error en la base de datos.
     */
    public void addRetakedWidget(int idBranch) throws Exception;

    /**
     * Ingresar muestras retradas en widgets.
     *
     * @param idBranch Id de la sede.
     * @throws Exception Error en la base de datos.
     */
    public void delayedWidget(int idBranch) throws Exception;

    /**
     * Actualizar muestras vencidas en widgets.
     *
     * @throws Exception Error en la base de datos.
     */
    public void expiredWidget() throws Exception;

    /**
     * Obtener la informacion del widget de ingreso de ordenes.
     *
     * @param date
     * @param idBranch
     * @param idUser
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.widgets.WidgetOrderEntry}
     * @throws Exception Error en la base de datos.
     */
    public WidgetOrderEntry widgetOrderEntry(int date, int idBranch, int idUser) throws Exception;

    /**
     * Obtener la informacion del widget de ingreso.
     *
     * @param date
     * @param idBranch
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.widgets.WidgetOrderEntry}
     * @throws Exception Error en la base de datos.
     */
    public WidgetEntry widgetEntry(int date, int idBranch) throws Exception;
}
