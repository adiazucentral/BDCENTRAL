package net.cltech.enterprisent.service.interfaces.operation.reports;

import java.util.List;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;

/**
 * Interfaz de servicios para la impresion por servicios.
 *
 * @version 1.0.0
 * @author equijano
 * @since 20/06/2019
 * @see Creacion
 */
public interface ServicePrintService
{

    /**
     * Lista los seriales con sus servicios
     *
     *
     * @return Lista de SerialPrint
     * {@link net.cltech.enterprisent.domain.operation.reports.SerialPrint}.
     * @throws Exception Error en la base de datos.
     */
    public List<SerialPrint> list() throws Exception;

    /**
     * Trae serial por servicio y sede
     *
     * @param idBranch
     * @param Service
     * @return SerialPrint
     * {@link net.cltech.enterprisent.domain.operation.reports.SerialPrint}.
     * @throws Exception Error en la base de datos.
     */
    public SerialPrint getByService(int idBranch, int Service) throws Exception;

    /**
     * Registra un nuevo registro impresion por servicio.
     *
     * @param serialPrint informacion respectiva del serial con el servicio
     * {@link net.cltech.enterprisent.domain.operation.reports.SerialPrint}
     * @throws Exception Error en la base de datos.
     */
    public void create(SerialPrint serialPrint) throws Exception;

    /**
     * Registra una lista de registros impresion por servicio.
     *
     * @param list lista de seriales por servicio
     * {@link net.cltech.enterprisent.domain.operation.reports.SerialPrint}
     * @return
     * @throws Exception Error en la base de datos.
     */
    public int createAll(List<SerialPrint> list) throws Exception;

    /**
     * Eliminar un serial para impresion directa
     *
     * @param serialPrint informacion respectiva del serial con el servicio
     * {@link net.cltech.enterprisent.domain.operation.reports.SerialPrint}
     * @throws Exception Error en la base de datos.
     */
    public void delete(SerialPrint serialPrint) throws Exception;

    /**
     * Eliminar los seriales
     *
     * @throws Exception Error en la base de datos.
     */
    public void deleteAll() throws Exception;

}
