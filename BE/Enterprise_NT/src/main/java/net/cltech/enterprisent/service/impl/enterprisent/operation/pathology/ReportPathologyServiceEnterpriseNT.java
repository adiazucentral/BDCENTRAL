/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.BarcodePathologyDao;
import net.cltech.enterprisent.domain.common.Print;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.operation.pathology.BarcodeLog;
import net.cltech.enterprisent.domain.operation.pathology.FilterBarcodePathology;
import net.cltech.enterprisent.domain.operation.pathology.Case;
import net.cltech.enterprisent.domain.operation.pathology.BarcodePathologyDesigner;
import net.cltech.enterprisent.service.impl.enterprisent.operation.reports.ReportServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.ReportPathologyService;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.websocket.PrintWsLog;
import net.cltech.enterprisent.websocket.PrintHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * Implementacion de informes para Patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 10/05/2021
 * @see Creacion
 */
@Service
public class ReportPathologyServiceEnterpriseNT implements ReportPathologyService
{
    @Autowired
    private BarcodePathologyDao barcodeDao;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private PrintHandler printHandler;
    
    @Override
    public List<BarcodeLog> printingByBarcode(FilterBarcodePathology filter) throws Exception
    {
        return printByBarcode(filter);
    }
    
    /**
     * Logica para la impresion de una etiqueta de barras
     *
     * @param search {@link net.cltech.enterprisent.domain.operation.common.FilterOrderHeader}
     * @param list Lista de informacion de impresion {@link net.cltech.enterprisent.domain.operation.reports.PrintReportLog}
     * @throws Exception
     */
    private List<BarcodeLog> printByBarcode(FilterBarcodePathology filter) throws Exception
    {
        List<BarcodeLog> jsons = zplReports(filter);
        for (BarcodeLog json : jsons)
        {
            json.setPrinting(sendPrinting(json.getZpl(), filter.getSerial()));
            json.setZpl("");
        }
        return jsons;
    }
    
    @Override
    public List<BarcodeLog> zplReports(FilterBarcodePathology report) throws Exception
    {
        final List<BarcodeLog> barcodes = new ArrayList<>(0);
        
        final List<BarcodePathologyDesigner> listBarcode = barcodeDao.barcodePredetermined();
        
        
        final BarcodePathologyDesigner barcode = listBarcode.size() > 0 ? listBarcode.get(0) : null;
        
        barcodes.addAll(buildZplDesigner(barcode, report.getCasePat(), report.getCount() ));

        return barcodes;
    }
   
    private List<BarcodeLog> buildZplDesigner(BarcodePathologyDesigner barcode, Case c, int count)
    {
        final List<BarcodeLog> barcodes = new ArrayList<>(0);
        
        if (barcode != null)
        {
            final String barcodeDesigner = barcode.getCommand();
            
            if (barcodeDesigner != null && barcodeDesigner.trim().equals("") == false)
            {
                try
                {
                    Date dateCurrent = new Date();
                    final String dataandtime = new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue() + " hh:mm").format(dateCurrent);
                    final String date = new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue()).format(dateCurrent);
                    final String time = new SimpleDateFormat("hh:mm").format(dateCurrent);
                    
                    Long years = c.getOrder().getBirthday() == null ? null : Tools.calculateAge(c.getOrder().getBirthday());
                 
                    String zpl = buildZpl(barcodeDesigner, dataandtime, date, time, years, c);
                                                   
                    for (int i = 0; i < count; i++)
                    {
                        barcodes.add(new BarcodeLog(c.getNumberCase(), false, zpl));
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return barcodes;
    }
    
    private String buildZpl(String barcodeDesigner, String dataandtime, String date, String time, Long years, Case c)
    {
        try
        {
            String orderdigit = configurationServices.get("DigitoAño").getValue();
            
            String barcode = barcodeDesigner
                    .replace("{dateandtime}", dataandtime)
                    .replace("{date}", date)
                    .replace("{time}", time)
                    .replace("{case}", String.valueOf(c.getNumberCase()).substring(4 - Integer.parseInt(orderdigit)))
                    .replace("{barcode}", String.valueOf(c.getStudyType().getCode() + c.getNumberCase()))
                    .replace("{studytypecode}", String.valueOf(c.getStudyType().getCode()))
                    .replace("{studytypename}", String.valueOf(c.getStudyType().getName()))
                    .replace("{quantity}", String.valueOf(c.getQuantity()))
                    .replace("{order}", String.valueOf(c.getOrder().getNumberOrder()).substring(4 - Integer.parseInt(orderdigit)))
                    .replace("{history}", c.getOrder().getPatientId())
                    .replace("{namePatient}", c.getOrder().getName1().toUpperCase() + " " + c.getOrder().getName2().toUpperCase() + " " + c.getOrder().getLastName().toUpperCase() + " " + c.getOrder().getSurName().toUpperCase())
                    .replace("{age}", years == null ? "" : String.valueOf(years))
                    .replace("{gender}", c.getOrder().getSexCode().equals("2") ? "F" : c.getOrder().getSexCode().equals("1") ? "M" : "I")
                    .replace("{birthday}", String.valueOf(c.getOrder().getBirthday()))
                    .replace("{documenttypecode}", String.valueOf(c.getOrder().getDocumentTypeCode()))
                    .replace("{documenttypename}", String.valueOf(c.getOrder().getDocumentType()));

            return barcode;
        }
        catch (Exception ex)
        {
            Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public boolean sendPrinting(String json, String serial) throws Exception
    {
        boolean send = false;
        if (!json.equals(""))
        {
            Print message = new Print();
            message.setMessage(json);
            message.setSerial(serial);
            List<WebSocketSession> sessions = printHandler.findSerialSessions(serial);
            if (sessions != null && !sessions.isEmpty() && sessions.size() > 0)
            {
                PrintWsLog.info("Sessiones Encontradas  " + sessions.size());
                message.setReceivers(Arrays.asList(serial));
                send = printHandler.sendPrint(message, sessions.get(0));
            }
            else
            {
                throw new EnterpriseNTException(Arrays.asList("0| the client is not connected"));
            }
        }
        return send;
    }
}
