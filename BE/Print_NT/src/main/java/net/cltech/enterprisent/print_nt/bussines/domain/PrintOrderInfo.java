package net.cltech.enterprisent.print_nt.bussines.domain;

import java.util.List;

/**
 * Representa clase para tener informacion ha imprimir con el json de la orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 27/05/2019
 * @see Creación
 */
public class PrintOrderInfo
{

    private String jsonOrder;
    private String patientName;
    private String patientEmail;
    private List<Attachment> attachments;

    
    private String nameFile;
    private Long orderNumber;
    private String patientHistory;
    private Physician physician;

    public String getJsonOrder()
    {
        return jsonOrder;
    }

    public void setJsonOrder(String jsonOrder)
    {
        this.jsonOrder = jsonOrder;
    }

    public String getPatientName()
    {
        return patientName;
    }

    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

    public String getPatientEmail()
    {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail)
    {
        this.patientEmail = patientEmail;
    }

    public List<Attachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments)
    {
        this.attachments = attachments;
    }

    public String getNameFile()
    {
        return nameFile;
    }

    public void setNameFile(String nameFile)
    {
        this.nameFile = nameFile;
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }
    
    public String getPatientHistory()
    {
        return patientHistory;
    }

    public void setPatientHistory(String patientHistory)
    {
        this.patientHistory = patientHistory;
    }
    
    public Physician getPhysician() {
        return physician;
    }

    public void setPhysician(Physician physician) {
        this.physician = physician;
    }

}
