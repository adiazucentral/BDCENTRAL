package net.cltech.enterprisent.service.interfaces.tools;

import java.util.List;
import net.cltech.enterprisent.domain.tools.BarcodeDesigner;

/**
 * Interfaz de servicios a la informacion barcode
 *
 * @version 1.0.0
 * @author eacuna
 * @since 10/12/2018
 * @see Creación
 */
public interface BarcodeService
{

    /**
     * Lista barcode desde la base de datos.
     *
     * @return Lista de barcodes.
     * @throws Exception Error en la base de datos.
     */
    public List<BarcodeDesigner> list() throws Exception;

    /**
     * Registra nueva barcode en la base de datos.
     *
     * @param barcode Instancia con los datos de la barcode.
     *
     * @return Instancia con los datos de la barcode.
     * @throws Exception Error en la base de datos.
     */
    public BarcodeDesigner create(BarcodeDesigner barcode) throws Exception;

    /**
     * Obtener información de una barcode por un campo especifico.
     *
     * @param id ID de la barcode a ser consultada.
     *
     * @return Instancia con los datos de la barcode.
     * @throws Exception Error en la base de datos.
     */
    public BarcodeDesigner getById(Integer id) throws Exception;

    /**
     * Actualiza la información de una barcode en la base de datos.
     *
     * @param barcode Instancia con los datos de la barcode.
     *
     * @return Objeto de la barcode modificada.
     * @throws Exception Error en la base de datos.
     */
    public BarcodeDesigner update(BarcodeDesigner barcode) throws Exception;

    /**
     * Obtener información de una barcode por estado.
     *
     * @param state Estado de los barcodes a ser consultadas
     *
     * @return Instancia con los datos de los barcodes.
     * @throws Exception Error en la base de datos.
     */
    public List<BarcodeDesigner> list(boolean state) throws Exception;

}
