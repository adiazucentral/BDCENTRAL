package net.cltech.enterprisent.service.interfaces.operation.reports;

import java.util.List;
import net.cltech.enterprisent.domain.common.PrintNode;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.common.FilterOrder;
import net.cltech.enterprisent.domain.operation.common.FilterOrderBarcode;
import net.cltech.enterprisent.domain.operation.common.FilterOrderHeader;
import net.cltech.enterprisent.domain.operation.common.PrintOrder;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderReportINS;
import net.cltech.enterprisent.domain.operation.orders.SuperTest;
import net.cltech.enterprisent.domain.operation.orders.excel.OrderReportAidaAcs;
import net.cltech.enterprisent.domain.operation.reports.PrintBarcodeLog;
import net.cltech.enterprisent.domain.operation.reports.PrintReportLog;
import net.cltech.enterprisent.domain.operation.reports.ReportBarcode;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;

/**
 * Interfaz de servicios a la informacion de informes.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/11/2017
 * @see Creación
 */
public interface ReportService
{

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de informes.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listFilters(Filter search) throws Exception;

    /**
     * Lista las cadenas de ZPL con los datos de la orden
     *
     *
     * @param report
     * @return
     * @throws Exception Error en la base de datos.
     */
    public List<PrintBarcodeLog> zplReports(ReportBarcode report) throws Exception;

    /**
     * Lista de seriales para imprimir registrados
     *
     *
     * @return
     * @throws Exception Error en la base de datos.
     */
    public List<String> listSerials() throws Exception;

    /**
     * Generar un serial para imprimir
     *
     *
     * @return
     * @throws Exception Error en la base de datos.
     */
    public SerialPrint createSerial() throws Exception;

    /**
     * Generar un serial para imprimir
     *
     *
     * @param serial
     * @return
     * @throws Exception Error en la base de datos.
     */
    public boolean isSerial(String serial) throws Exception;

    /**
     * Metodo de impresion por tipo reporte.
     *
     * @param search Clase con los filtros definidos.
     * @return
     *
     * @throws Exception Error en la base de datos.
     */
    public List<PrintReportLog> printingByType(FilterOrderHeader search) throws Exception;
    
    /**
     * Metodo de impresion por tipo reporte.
     *
     * @param search Clase con los filtros definidos.
     * @return
     *
     * @throws Exception Error en la base de datos.
     */
    public PrintOrder finalReport(FilterOrderHeader search) throws Exception;
    
    /**
     * Metodo de impresion por tipo reporte.
     *
     * @param filterOrder
     * @param ordernew
     * @param iduser
     *
     * @throws Exception Error en la base de datos.
     */
    public void changeStateTest(FilterOrderHeader filterOrder, Order ordernew, int iduser) throws Exception;
    
    /**
     * Metodo de impresion por tipo reporte.
     *
     * @param print Clase con los filtros definidos.
     * @return
     *
     * @throws Exception Error en la base de datos.
     */
    public boolean printFinalReport(PrintNode print) throws Exception;

    /**
     * Metodo de impresion por tipo de codigo de barras.
     *
     * @param search Clase con los filtros definidos.
     * {@link net.cltech.enterprisent.domain.operation.common.FilterOrderBarcode}
     * @return Lista de reporte de impresion
     * {@link net.cltech.enterprisent.domain.operation.reports.PrintBarcodeLog}
     *
     * @throws Exception Error en la base de datos.
     */
    public List<PrintBarcodeLog> printingByBarcode(FilterOrderBarcode search) throws Exception;

    /**
     * Obtener las ordenes a imprimir en el codigo de barras.
     *
     * @param search Clase con los filtros definidos.
     * {@link net.cltech.enterprisent.domain.operation.common.FilterOrderBarcode}
     * @return Lista de Ordenes
     * {@link net.cltech.enterprisent.domain.operation.reports.PrintReportLog}
     *
     * @throws Exception Error en la base de datos.
     */
    public List<Order> ordersBarcode(FilterOrderBarcode search) throws Exception;

    /**
     * Metodo que lista las cabecera de la orden
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> orderHeader(Filter search) throws Exception;

    /**
     * Metodo que añade el cuepor de la orden a la cabecera
     *
     * @param filterOrder filtros del cuerpo de la orden.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public Order orderBody(FilterOrder filterOrder) throws Exception;

    /**
     * Metodo que envia json al cliente websocket para su impresion
     *
     * @param json Objeto json con data necesaria
     * @param serial Numero de serial
     * @param type
     * @return
     * @throws Exception Error en la base de datos.
     */
    public boolean sendPrinting(String json, String serial, int type) throws Exception;

    /**
     * Obtener informacion completa de la orden para imprimir
     *
     * @param filter
     * @param order
     * @return
     * @throws Exception Error en la base de datos.
     */
    public Order printReporByOrder(FilterOrderHeader filter, Order order) throws Exception;
    
    /**
     * Recive un id de una orden y retorna un dato en base64
     *
     * @param idOrderHis
     * @param userName
     * @param password
     * @return Dato en base 64
     * @throws Exception Error presentado en el servicio
     */
    public String getBase64(String idOrderHis, String userName, String password) throws Exception;
    
    /**
     * Recive un id de una orden y retorna un dato en base64
     *
     * @param filter
     * @return Dato en base 64
     * @throws Exception Error presentado en el servicio
     */
    public FilterOrderBarcode orderPrintbarcode(FilterOrderBarcode filter) throws Exception;
    
    
    /**
     * Lista las ordenes por un filtro especifico dado por la clase de informes.
     *
     *
     * @param init
     * @param end
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderReportINS> listFiltersOrderRange(long init, long end) throws Exception;
    
    /**
     * Lista las cadenas de ZPL con los datos del paciente
     *
     *
     * @param report
     * @return
     * @throws Exception Error en la base de datos.
     */
    public List<PrintBarcodeLog> barcodePatient(ReportBarcode report) throws Exception;
    
    /**
     * Metodo de impresion por tipo de codigo de barras del paciente
     *
     * @param search Clase con los filtros definidos.
     * {@link net.cltech.enterprisent.domain.operation.common.FilterOrderBarcode}
     * @return Lista de reporte de impresion
     * {@link net.cltech.enterprisent.domain.operation.reports.PrintBarcodeLog}
     *
     * @throws Exception Error en la base de datos.
     */
    public List<PrintBarcodeLog> printByBarcodePatient(FilterOrderBarcode search) throws Exception;
    
    /**
     * Lista las ordenes por un filtro especifico dado por la clase de informes.
     * @param init
     * @param end
     * @param isSearchOrder
     * @param groupByProfil
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderReportAidaAcs> getListOrderFacturation(long init, long end, boolean isSearchOrder, boolean groupByProfil) throws Exception;
    
    
     /**
     * Lista las ordenes por un filtro especifico dado por la clase de informes.
     * @param order
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public Boolean getValidOrderComplete(long order) throws Exception;
    
    
         /**
     * Lista las ordenes por un filtro especifico dado por la clase de informes.
     * @param order
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<SuperTest> gettestpendingorder(long order) throws Exception;
    
}
