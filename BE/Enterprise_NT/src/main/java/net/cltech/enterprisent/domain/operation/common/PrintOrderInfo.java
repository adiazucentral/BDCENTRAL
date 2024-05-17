package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.document.Document;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.SuperTest;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase para tener informacion ha imprimir con el json de la orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 27/05/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Imprimir informacion de la orden",
        description = "Representa la informacion ha imprimir con el json de la orden."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrintOrderInfo
{
    @ApiObjectField(name = "jsonOrder", description = "json de la orden", required = false, order = 1)
    private String jsonOrder;
    @ApiObjectField(name = "patientName", description = "Nombre del paciente", required = false, order = 2)
    private String patientName;
    @ApiObjectField(name = "patientEmail", description = "Correo del paciente", required = false, order = 3)
    private String patientEmail;
    @ApiObjectField(name = "patientHistory", description = "Historia del paciente", required = false, order = 4)
    private String patientHistory;
    @ApiObjectField(name = "attachments", description = "Adjuntos", required = false, order = 5)
    private List<Document> attachments;
    @ApiObjectField(name = "order", description = "Informacion de la orden", required = false, order = 6)
    private Order order;
    @ApiObjectField(name = "nameFile", description = "Nombre del archivo", required = false, order = 7)
    private String nameFile;
    @ApiObjectField(name = "orderNumber", description = "Numero de la orden", required = false, order = 8)
    private Long orderNumber;
    @ApiObjectField(name = "encrypt", description = "Si el reporte se encripta o no", required = false, order = 10)
    private Boolean encrypt;
    @ApiObjectField(name = "listTestPending", description = "Listado de examenes pendientes", required = false, order = 11)
    private  List<SuperTest> listTestPending;


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

    public List<Document> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<Document> attachments)
    {
        this.attachments = attachments;
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

    public String getPatientHistory()
    {
        return patientHistory;
    }

    public void setPatientHistory(String patientHistory)
    {
        this.patientHistory = patientHistory;
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


    public Boolean getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Boolean encrypt) {
        this.encrypt = encrypt;
    }

    public List<SuperTest> getListTestPending() {
        return listTestPending;
    }

    public void setListTestPending(List<SuperTest> listTestPending) {
        this.listTestPending = listTestPending;
    }

}
