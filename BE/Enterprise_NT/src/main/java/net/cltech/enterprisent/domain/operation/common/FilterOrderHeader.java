package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 21/05/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Filtro Busquedas",
        description = "Representa filtro con parametros para busquedas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterOrderHeader extends Filter
{

    @ApiObjectField(name = "printOrder", description = "Lista de ordenes ha imprimir", required = true, order = 1)
    private List<PrintOrder> printOrder;
    @ApiObjectField(name = "labelsreport", description = "Etiquetas(traducciones) que se pintan en el reporte", required = false, order = 2)
    private String labelsreport;
    @ApiObjectField(name = "variables", description = "Variables que se usan en el encabezado del reporte", required = false, order = 3)
    private String variables;
    @ApiObjectField(name = "printingMedium", description = "Tipo de impresion impreso -> 1 pdf -> 2 email -> 3 consulta web -> 4", required = false, order = 4)
    private int printingMedium;
    @ApiObjectField(name = "typeNameFile", description = "Tipo de nombre del archivo solo orden -> 0 order_historia -> 1 order_patient -> 2", required = false, order = 5)
    private int typeNameFile;
    @ApiObjectField(name = "sendEmail", description = "Enviar correo a paciente-> 1 medico -> 2 ambos -> 3", required = false, order = 6)
    private int sendEmail;
    @ApiObjectField(name = "completeOrder", description = "Validar si la orden se muestra completa, incompleta-> 1 completa -> 2", required = false, order = 7)
    private int completeOrder;
    @ApiObjectField(name = "order", description = "Listado de orden a imprimir", required = false, order = 8)
    private String order;
    @ApiObjectField(name = "urlApi", description = "Url del api de los servicios", required = false, order = 9)
    private String urlApi;
    @ApiObjectField(name = "attachments", description = "Adjuntos de la orden", required = false, order = 10)
    private String attachments;
    @ApiObjectField(name = "userlist", description = "Lista de usuarios", required = false, order = 11)
    private String userlist;
    @ApiObjectField(name = "personReceive", description = "Persona que recibe", required = false, order = 12)
    private String personReceive;
    @ApiObjectField(name = "sendAutomaticResult", description = "Envio automatico de resultado", required = false, order = 13)
    private boolean sendAutomaticResult;
    
    

    public FilterOrderHeader()
    {
    }

    public String getUrlApi() {
        return urlApi;
    }

    public void setUrlApi(String urlApi) {
        this.urlApi = urlApi;
    }

    public List<PrintOrder> getPrintOrder()
    {
        return printOrder;
    }

    public void setPrintOrder(List<PrintOrder> printOrder)
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

    public int getPrintingMedium()
    {
        return printingMedium;
    }

    public void setPrintingMedium(int printingMedium)
    {
        this.printingMedium = printingMedium;
    }

    public int getTypeNameFile()
    {
        return typeNameFile;
    }

    public void setTypeNameFile(int typeNameFile)
    {
        this.typeNameFile = typeNameFile;
    }

    public int getSendEmail()
    {
        return sendEmail;
    }

    public void setSendEmail(int sendEmail)
    {
        this.sendEmail = sendEmail;
    }

    public int getCompleteOrder()
    {
        return completeOrder;
    }

    public void setCompleteOrder(int completeOrder)
    {
        this.completeOrder = completeOrder;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getUserlist() {
        return userlist;
    }

    public void setUserlist(String userlist) {
        this.userlist = userlist;
    }

    public String getPersonReceive() {
        return personReceive;
    }

    public void setPersonReceive(String personReceive) {
        this.personReceive = personReceive;
    }

    public boolean getSendAutomaticResult() {
        return sendAutomaticResult;
    }

    public void setSendAutomaticResult(boolean sendAutomaticResult) {
        this.sendAutomaticResult = sendAutomaticResult;
    }
    
    
    
    
}
