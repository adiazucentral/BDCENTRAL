/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.BarcodeLog;
import net.cltech.enterprisent.domain.operation.pathology.FilterBarcodePathology;

/**
 * Interfaz de servicios a la informacion de informes de patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 10/05/2021
 * @see Creación
 */

public interface ReportPathologyService {
    
    /**
     * Metodo de impresion por tipo de codigo de barras.
     *
     * @param filter {@link net.cltech.enterprisent.domain.operation.pathology.FilterBarcodePathology}
     * @return Lista de reporte de impresion
     * {@link net.cltech.enterprisent.domain.operation.pathology.BarcodeLog}
     *
     * @throws Exception Error en la base de datos.
     */
    public List<BarcodeLog> printingByBarcode(FilterBarcodePathology filter) throws Exception;
    
    
    /**
     * Lista las cadenas de ZPL con los datos del caso
     * @param report
     * @return
     * @throws Exception Error en la base de datos.
     */
    public List<BarcodeLog> zplReports(FilterBarcodePathology report) throws Exception;
    
    
     /**
     * Metodo que envia json al cliente websocket para su impresion
     *
     * @param json Objeto json con data necesaria
     * @param serial Numero de serial
     * @return
     * @throws Exception Error en la base de datos.
     */
    public boolean sendPrinting(String json, String serial) throws Exception;
}
