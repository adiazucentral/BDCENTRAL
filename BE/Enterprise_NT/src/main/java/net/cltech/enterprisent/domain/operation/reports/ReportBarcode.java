package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeSample;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase del reporte por barcode
 *
 * @version 1.0.0
 * @author equijano
 * @since 27/02/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Informes",
        name = "Reporte zpl",
        description = "Representa el objeto del reporte por barcode."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportBarcode
{

    @ApiObjectField(name = "init", description = "Numero de la orden o fecha de orden inicial", order = 1)
    private Long init;
    @ApiObjectField(name = "end", description = "Numero de la orden o fecha de orden final", order = 2)
    private Long end;
    @ApiObjectField(name = "samples", description = "Muestras a imprimri", order = 3)
    private List<BarcodeSample> samples;
    @ApiObjectField(name = "count", description = "Cantidad de impresiones", order = 4)
    private int count;
    @ApiObjectField(name = "demographics", description = "Lista de demograficos", order = 5)
    private List<Demographic> demographics;
    @ApiObjectField(name = "serial", description = "Id unico para imprimir", order = 6)
    private String serial;
    @ApiObjectField(name = "rangeType", description = "Rango de busqueda: 0 -> Fecha<br>1->Orden", order = 7)
    private Integer rangeType;
    @ApiObjectField(name = "printAddLabel", description = "Imprime etiquta adicional.", order = 8)
    private boolean printAddLabel;
    @ApiObjectField(name = "ordersprint", description = "Lista de ordenes ha imprimir", required = false, order = 9)
    private List<Order> ordersprint;
    @ApiObjectField(name = "idPatient", description = "Id del paciente", order = 10)
    private Integer idPatient;

    public ReportBarcode(Long init, Long end, List<BarcodeSample> samples, String serial, Integer rangeType, boolean printAddLabel, List<Demographic> demographics)
    {
        this.init = init;
        this.end = end;
        this.samples = samples;
        this.serial = serial;
        this.rangeType = rangeType;
        this.printAddLabel = printAddLabel;
        this.demographics = demographics;
    }

    public ReportBarcode(String serial, Integer idPatient) {
        this.serial = serial;
        this.idPatient = idPatient;
    }
    
    public ReportBarcode()
    {
    }

    public Long getInit()
    {
        return init;
    }

    public void setInit(Long init)
    {
        this.init = init;
    }

    public Long getEnd()
    {
        return end;
    }

    public void setEnd(Long end)
    {
        this.end = end;
    }

    public List<BarcodeSample> getSamples()
    {
        return samples;
    }

    public void setSamples(List<BarcodeSample> samples)
    {
        this.samples = samples;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public List<Demographic> getDemographics()
    {
        return demographics;
    }

    public void setDemographics(List<Demographic> demographics)
    {
        this.demographics = demographics;
    }

    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public Integer getRangeType()
    {
        return rangeType;
    }

    public void setRangeType(Integer rangeType)
    {
        this.rangeType = rangeType;
    }

    public boolean isPrintAddLabel()
    {
        return printAddLabel;
    }

    public void setPrintAddLabel(boolean printAddLabel)
    {
        this.printAddLabel = printAddLabel;
    }

    public List<Order> getOrdersprint()
    {
        return ordersprint;
    }

    public void setOrdersprint(List<Order> ordersprint)
    {
        this.ordersprint = ordersprint;
    }

    public Integer getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(Integer idPatient) {
        this.idPatient = idPatient;
    }
}
