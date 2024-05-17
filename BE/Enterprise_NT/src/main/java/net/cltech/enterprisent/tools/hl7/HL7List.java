/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools.hl7;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;

/**
 * Esta clase forma la cadena de texto para exportar a HL7 los listados por
 * laboratorio de referecia
 *
 * @version 1.0.0
 * @author cmartin
 * @since 25/09/2017
 * @see Creación
 */
public class HL7List
{

    //Constantes
    public static final char SOH = '\u0001';
    public static final char STX = '\u0002';
    public static final char ETX = '\u0003';
    public static final char EOT = '\u0004';
    public static final char ENQ = '\u0005';
    public static final char ACK = '\u0006';
    public static final char LF = '\n';
    public static final char VT = '\u000b';
    public static final char CR = '\r';
    public static final char DLE = '\u0010';
    public static final char DC1 = '\u0011';
    public static final char NAK = '\u0015';
    public static final char SYN = '\u0016';
    public static final char ETB = '\u0017';
    public static final char FS = '\u001c';
    public static final char GS = '\u001d';
    public static final char RS = '\u001e';

    private String MSH = "MSH|^~\\&|EnterpriseNT|CLTech|||{date}||ORU^R01^ORU_R01|59689|P|2.5" + CR;
    private String PID = "PID|{indexP}||{historia}|{demograficoP}|{apellido1}^{apellido2}^{nombre1}^{nombre2}||{fechaNacimiento}|{sexo}||{raza}" + CR;
    private String ORC = "ORC|NW|{ordenLIS}||||||{demograficoO}|{fechaRegistro}|{usuario}" + CR;
    private String OBX = "OBX|{index}||{ordenLIS}^{codigoExamenLIS}^{nombreExamen}" + CR;

    public String hl7List(List<Order> orders) throws Exception
    {
        if (!orders.isEmpty())
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String hl7 = "";
            hl7 = hl7 + MSH;
            hl7 = hl7.replace("{date}", format.format(new Date()));
            int j = 1;
            for (Order order : orders)
            {
                String hl7Partial = "";
                hl7Partial += PID;
                hl7Partial += ORC;
                String demoPatient = "";
                if (order.getPatient().getDemographics() != null)
                {
                    for (DemographicValue demographic : order.getPatient().getDemographics())
                    {
                        if (demographic.isEncoded())
                        {
                            demoPatient += demographic.getCodifiedId() == null ? "" : demographic.getIdDemographic() + "@" + demographic.getCodifiedId() + "*" + demographic.getCodifiedCode() + "^";
                        } else
                        {
                            demoPatient += demographic.getNotCodifiedValue() == null ? "" : demographic.getIdDemographic() + "@" + demographic.getNotCodifiedValue() + "^";
                        }
                    }
                    demoPatient = demoPatient.isEmpty() ? "" : demoPatient.substring(0, demoPatient.length() - 1);
                }
                //Paciente
                hl7Partial = hl7Partial.replace("{indexP}", j + "");
                hl7Partial = hl7Partial.replace("{historia}", order.getPatient().getPatientId());
                hl7Partial = hl7Partial.replace("{apellido1}", order.getPatient().getLastName());
                hl7Partial = hl7Partial.replace("{apellido2}", order.getPatient().getSurName());
                hl7Partial = hl7Partial.replace("{nombre1}", order.getPatient().getName1());
                hl7Partial = hl7Partial.replace("{nombre2}", order.getPatient().getName2());
                hl7Partial = hl7Partial.replace("{fechaNacimiento}", format.format(order.getPatient().getBirthday()));
                hl7Partial = hl7Partial.replace("{sexo}", order.getPatient().getSex().getId() == 7 ? "M" : order.getPatient().getSex().getId() == 8 ? "F" : order.getPatient().getSex().getId() == 9 ? "U" : "");
                hl7Partial = hl7Partial.replace("{raza}", (order.getPatient().getRace().getName() == null) ? "" : order.getPatient().getRace().getName());
                hl7Partial = hl7Partial.replace("{demograficoP}", demoPatient);

                String demoOrder = "";
                if (order.getDemographics() != null)
                {
                    for (DemographicValue demographic : order.getDemographics())
                    {
                        if (demographic.isEncoded())
                        {
                            demoOrder += demographic.getCodifiedId() == null ? "" : demographic.getIdDemographic() + "@" + demographic.getCodifiedId() + "*" + demographic.getCodifiedCode() + "^";
                        } else
                        {
                            demoOrder += demographic.getNotCodifiedValue() == null ? "" : demographic.getIdDemographic() + "@" + demographic.getNotCodifiedValue() + "^";
                        }
                    }
                    demoOrder = demoOrder.isEmpty() ? "" : demoOrder.substring(0, demoOrder.length() - 1);
                }
                //Orden
                hl7Partial = hl7Partial.replace("{ordenLIS}", order.getOrderNumber().toString());
                hl7Partial = hl7Partial.replace("{fechaRegistro}", format.format(order.getCreatedDate()));
                hl7Partial = hl7Partial.replace("{usuario}", order.getLastUpdateUser().getUserName());
                hl7Partial = hl7Partial.replace("{demograficoO}", demoOrder);
                int i = 1;
                for (Test test : order.getTests())
                {
                    String hl7Test = "";
                    hl7Test += OBX;
                    hl7Test = hl7Test.replace("{index}", i + "");
                    hl7Test = hl7Test.replace("{ordenLIS}", order.getOrderNumber().toString());
                    hl7Test = hl7Test.replace("{codigoExamenLIS}", test.getCode());
                    hl7Test = hl7Test.replace("{nombreExamen}", test.getName());
                    hl7Partial += hl7Test;
                    i++;
                }
                hl7 += hl7Partial;
                j++;
            }
            hl7 = "" + VT + hl7 + FS + CR;
            return hl7;
        } else
        {
            return "";
        }
    }
}
