package net.cltech.enterprisent.print_nt.bussines.domain;

/**
 * Representa La impresion con el nodejs
 *
 * @version 1.0.0
 * @author eaquijano
 * @since 03/05/2019
 * @see Creacion
 */
public class PrintNode
{

    private PrintOrder printOrder;
    private String labelsreport;
    private String variables;
    private int numberCopies;
    private boolean attached;
    private int printingMedium;
    private String order;
    private int sendEmail;
    private String emailBody;
    private String emailSubjectPatient;
    private String emailSubjectPhysician;
    private boolean encryptionReportResult;
    private String urlApi;
    private String bufferReport;
    private String typePrint;
    private String nameFile;
    private String patientEmail;
    private String physicianEmail;
    private int branch;

    public PrintNode(String labelsreport, String variables, boolean attached, boolean encryptionReportResult, String urlApi)
    {
        this.labelsreport = labelsreport;
        this.variables = variables;
        this.attached = attached;
        this.encryptionReportResult = encryptionReportResult;
        this.urlApi = urlApi;
    }

    public PrintNode()
    {
    }

    public PrintOrder getPrintOrder()
    {
        return printOrder;
    }

    public String getUrlApi()
    {
        return urlApi;
    }

    public void setUrlApi(String urlApi)
    {
        this.urlApi = urlApi;
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

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
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

    public void setEmailBody(String EmailBody)
    {
        this.emailBody = EmailBody;
    }

    public String getEmailSubjectPatient()
    {
        return emailSubjectPatient;
    }

    public void setEmailSubjectPatient(String EmailSubjectPatient)
    {
        this.emailSubjectPatient = EmailSubjectPatient;
    }

    public String getEmailSubjectPhysician()
    {
        return emailSubjectPhysician;
    }

    public void setEmailSubjectPhysician(String EmailSubjectPhysician)
    {
        this.emailSubjectPhysician = EmailSubjectPhysician;
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

    public String getTypePrint()
    {
        return typePrint;
    }

    public void setTypePrint(String typePrint)
    {
        this.typePrint = typePrint;
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

    public int getBranch()
    {
        return branch;
    }

    public void setBranch(int branch)
    {
        this.branch = branch;
    }

}
