package net.cltech.enterprisent.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.operation.common.PrintOrder;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa La impresion con el nodejs
 *
 * @version 1.0.0
 * @author eaquijano
 * @since 03/05/2019
 * @see Creacion
 */
@ApiObject(
        group = "Común",
        name = "Imprimir con nodejs",
        description = "Representa para imprimir en nodejs"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrintNode
{

    @ApiObjectField(name = "printOrder", description = "Orden ha imprimir", required = true, order = 1)
    private PrintOrder printOrder;
    @ApiObjectField(name = "labelsreport", description = "Etiquetas(traducciones) que se pintan en el reporte", required = false, order = 2)
    private String labelsreport;
    @ApiObjectField(name = "variables", description = "Variables que se usan en el encabezado del reporte", required = false, order = 3)
    private String variables;
    @ApiObjectField(name = "numberCopies", description = "Cantidad de copias", required = false, order = 4)
    private int numberCopies;
    @ApiObjectField(name = "attached", description = "Adjuntos", required = false, order = 5)
    private boolean attached;
    @ApiObjectField(name = "printingMedium", description = "Tipo de impresion impreso -> 1 pdf -> 2 email -> 3", required = false, order = 6)
    private int printingMedium;
    @ApiObjectField(name = "sendEmail", description = "Enviar correo a paciente -> 1 medico -> 2 ambos -> 3", required = false, order = 7)
    private int sendEmail;
    @ApiObjectField(name = "emailBody", description = "Cuerpo del correo", required = false, order = 8)
    private String emailBody;
    @ApiObjectField(name = "emailSubjectPatient", description = "Asunto del correo del paciente", required = false, order = 9)
    private String emailSubjectPatient;
    @ApiObjectField(name = "emailSubjectPhysician", description = "Asunto del correo del medico", required = false, order = 10)
    private String emailSubjectPhysician;
    @ApiObjectField(name = "encryptionReportResult", description = "Envio final o previo", required = true, order = 32)
    private boolean encryptionReportResult;
    @ApiObjectField(name = "bufferReport", description = "reporte en formato buffer", required = true, order = 32)
    private String bufferReport;
    @ApiObjectField(name = "serial", description = "Serial de impresion", required = true, order = 33)
    private String serial;
    @ApiObjectField(name = "typePrint", description = "tipo de impresion", required = false, order = 10)
    private String typePrint;
    @ApiObjectField(name = "branch", description = "Sede de la orden", required = false, order = 11)
    private int branch;
    @ApiObjectField(name = "service", description = "Servicio de la orden", required = false, order = 12)
    private int service;
    @ApiObjectField(name = "nameFile", description = "Nombre del archivo", required = false, order = 13)
    private String nameFile;
    @ApiObjectField(name = "patientEmail", description = "Correo del paciente", required = false, order = 14)
    private String patientEmail;
    @ApiObjectField(name = "physicianEmail", description = "Correo del medico", required = false, order = 15)
    private String physicianEmail;
    @ApiObjectField(name = "order", description = "Numero de la orden", required = false, order = 16)
    private String order;
    @ApiObjectField(name = "patientName", description = "Nombre del paciente", required = false, order = 14)
    private String patientName;

    public PrintNode()
    {
    }

    public PrintNode(PrintOrder printOrder, String labelsreport, String variables, int numberCopies, boolean attached, int printingMedium, int sendEmail, String emailBody, String emailSubjectPatient, String emailSubjectPhysician, boolean encryptionReportResult, String bufferReport, String nameFile, String patientEmail, String physicianEmail, String order)
    {
        this.printOrder = printOrder;
        this.labelsreport = labelsreport;
        this.variables = variables;
        this.numberCopies = numberCopies;
        this.attached = attached;
        this.printingMedium = printingMedium;
        this.sendEmail = sendEmail;
        this.emailBody = emailBody;
        this.emailSubjectPatient = emailSubjectPatient;
        this.emailSubjectPhysician = emailSubjectPhysician;
        this.encryptionReportResult = encryptionReportResult;
        this.bufferReport = bufferReport;
        this.nameFile = nameFile;
        this.patientEmail = patientEmail;
        this.physicianEmail = physicianEmail;
        this.order = order;
    }

    public PrintOrder getPrintOrder()
    {
        return printOrder;
    }

    public void setPrintOrder(PrintOrder printOrder)
    {
        this.printOrder = printOrder;
    }

    public String getLabelsreport()
    {
        return labelsreport;
    }

    public void setLabelsreport(String labelsreport)
    {
        this.labelsreport = labelsreport;
    }

    public String getVariables()
    {
        return variables;
    }

    public void setVariables(String variables)
    {
        this.variables = variables;
    }

    public int getNumberCopies()
    {
        return numberCopies;
    }

    public void setNumberCopies(int numberCopies)
    {
        this.numberCopies = numberCopies;
    }

    public boolean isAttached()
    {
        return attached;
    }

    public void setAttached(boolean attached)
    {
        this.attached = attached;
    }

    public int getPrintingMedium()
    {
        return printingMedium;
    }

    public void setPrintingMedium(int printingMedium)
    {
        this.printingMedium = printingMedium;
    }

    public int getSendEmail()
    {
        return sendEmail;
    }

    public void setSendEmail(int sendEmail)
    {
        this.sendEmail = sendEmail;
    }

    public String getEmailBody()
    {
        return emailBody;
    }

    public void setEmailBody(String emailBody)
    {
        this.emailBody = emailBody;
    }

    public String getEmailSubjectPatient()
    {
        return emailSubjectPatient;
    }

    public void setEmailSubjectPatient(String emailSubjectPatient)
    {
        this.emailSubjectPatient = emailSubjectPatient;
    }

    public String getEmailSubjectPhysician()
    {
        return emailSubjectPhysician;
    }

    public void setEmailSubjectPhysician(String emailSubjectPhysician)
    {
        this.emailSubjectPhysician = emailSubjectPhysician;
    }

    public boolean getEncryptionReportResult()
    {
        return encryptionReportResult;
    }

    public void setEncryptionReportResult(boolean encryptionReportResult)
    {
        this.encryptionReportResult = encryptionReportResult;
    }

    public String getBufferReport()
    {
        return bufferReport;
    }

    public void setBufferReport(String bufferReport)
    {
        this.bufferReport = bufferReport;
    }

    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public String getTypePrint()
    {
        return typePrint;
    }

    public void setTypePrint(String typePrint)
    {
        this.typePrint = typePrint;
    }

    public int getBranch()
    {
        return branch;
    }

    public void setBranch(int branch)
    {
        this.branch = branch;
    }

    public int getService()
    {
        return service;
    }

    public void setService(int service)
    {
        this.service = service;
    }

    public String getNameFile()
    {
        return nameFile;
    }

    public void setNameFile(String nameFile)
    {
        this.nameFile = nameFile;
    }

    public String getPatientEmail()
    {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail)
    {
        this.patientEmail = patientEmail;
    }

    public String getPhysicianEmail()
    {
        return physicianEmail;
    }

    public void setPhysicianEmail(String physicianEmail)
    {
        this.physicianEmail = physicianEmail;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getPatientName()
    {
        return patientName;
    }

    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

}
