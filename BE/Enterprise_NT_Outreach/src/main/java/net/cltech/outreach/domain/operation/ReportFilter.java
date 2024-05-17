package net.cltech.outreach.domain.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa filtro con parametros para busquedas de reportes.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/05/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Filtro Reportes",
        description = "Representa filtro con parametros para busquedas de reportes."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportFilter
{

    @ApiObjectField(name = "rangeType", description = "Rango de busqueda: 0 -> Fecha<br>1->Orden<br>2->Fecha y Orden", order = 1)
    private Integer rangeType;
    @ApiObjectField(name = "init", description = "Rango inicial", order = 2)
    private Long init;
    @ApiObjectField(name = "end", description = "Rango final", order = 3)
    private Long end;
    @ApiObjectField(name = "initDate", description = "Fecha inicial (Solo aplica para filtros con fecha y numero de orden)", order = 4)
    private Integer initDate;
    @ApiObjectField(name = "endDate", description = "Fecha final (Solo aplica para filtros con fecha y numero de orden)", order = 5)
    private Integer endDate;
    @ApiObjectField(name = "orderType", description = "Id Tipo de Orden: 0 para todos ", order = 6)
    private int orderType;
    @ApiObjectField(name = "check", description = "Filtro muestras verificadas:<br>0-Todas<br>1-Verificadas<br>2-Pendientes Verificación ", order = 7)
    private int check;
    @ApiObjectField(name = "testFilterType", description = "Filtro por examen: 0 - Todos<br> 1 - Sección<br> 2-Examen<br> 3-Confidenciales", order = 8)
    private int testFilterType;
    @ApiObjectField(name = "tests", description = "id´s Examenes/Secciónes a filtrar", order = 9)
    private List<Integer> tests;
    @ApiObjectField(name = "packageDescription", description = "Maneja descripción de paquetes", order = 11)
    private boolean packageDescription;
    @ApiObjectField(name = "listType", description = "Tipo de Listado : 0 -> Normal, 1 -> Pacientes por Area, 2 -> Pacientes por Examen, 3 -> Normal Sin Agrupar", order = 12)
    private int listType;
    @ApiObjectField(name = "apply", description = "Indica si se aplica Impresion de etiqueta adicional.", order = 15)
    private boolean apply;
    @ApiObjectField(name = "printerId", description = "Ip equipo", order = 16)
    private String printerId;
    @ApiObjectField(name = "printAddLabel", description = "Imprime etiquta adicional.", order = 17)
    private boolean printAddLabel;
    @ApiObjectField(name = "basic", description = "Indica si se mostrara información basica de la orden, es decir, no seran enviados los examenes.", order = 18)
    private boolean basic;
    @ApiObjectField(name = "reprintFinalReport", description = "Reimprimir informe final", order = 23)
    private boolean reprintFinalReport;
    @ApiObjectField(name = "attached", description = "Adjuntos", order = 24)
    private boolean attached;
    @ApiObjectField(name = "typeReport", description = "Indica si se imprimen informes finales(1), previos(2) o copias(3)", order = 25)
    private Integer typeReport;
    @ApiObjectField(name = "printedOrders", description = "Control de ordenes impresas", order = 26)
    private boolean printedOrders;

    public ReportFilter()
    {
    }

    public ReportFilter(Integer rangeType, Long init, Long end)
    {
        this.rangeType = rangeType;
        this.init = init;
        this.end = end;
    }

    public Integer getRangeType()
    {
        return rangeType;
    }

    public void setRangeType(Integer rangeType)
    {
        this.rangeType = rangeType;
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

    public Integer getInitDate()
    {
        return initDate;
    }

    public void setInitDate(Integer initDate)
    {
        this.initDate = initDate;
    }

    public Integer getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Integer endDate)
    {
        this.endDate = endDate;
    }

    public int getOrderType()
    {
        return orderType;
    }

    public void setOrderType(int orderType)
    {
        this.orderType = orderType;
    }

    public int getCheck()
    {
        return check;
    }

    public void setCheck(int check)
    {
        this.check = check;
    }

    public int getTestFilterType()
    {
        return testFilterType;
    }

    public void setTestFilterType(int testFilterType)
    {
        this.testFilterType = testFilterType;
    }
    
    public List<Integer> getTests()
    {
        return tests;
    }

    public void setTests(List<Integer> tests)
    {
        this.tests = tests;
    }
    public boolean isPackageDescription()
    {
        return packageDescription;
    }

    public void setPackageDescription(boolean packageDescription)
    {
        this.packageDescription = packageDescription;
    }

    public int getListType()
    {
        return listType;
    }

    public void setListType(int listType)
    {
        this.listType = listType;
    }

    public boolean isApply()
    {
        return apply;
    }

    public void setApply(boolean apply)
    {
        this.apply = apply;
    }

    public String getPrinterId()
    {
        return printerId;
    }

    public void setPrinterId(String printerId)
    {
        this.printerId = printerId;
    }

    public boolean isPrintAddLabel()
    {
        return printAddLabel;
    }

    public void setPrintAddLabel(boolean printAddLabel)
    {
        this.printAddLabel = printAddLabel;
    }

    public boolean isBasic()
    {
        return basic;
    }

    public void setBasic(boolean basic)
    {
        this.basic = basic;
    }

    public boolean isReprintFinalReport()
    {
        return reprintFinalReport;
    }

    public void setReprintFinalReport(boolean reprintFinalReport)
    {
        this.reprintFinalReport = reprintFinalReport;
    }

    public boolean isAttached()
    {
        return attached;
    }

    public void setAttached(boolean attached)
    {
        this.attached = attached;
    }

    public Integer getTypeReport()
    {
        return typeReport;
    }

    public void setTypeReport(Integer typeReport)
    {
        this.typeReport = typeReport;
    }

    public boolean isPrintedOrders()
    {
        return printedOrders;
    }

    public void setPrintedOrders(boolean printedOrders)
    {
        this.printedOrders = printedOrders;
    }

    public static int RANGE_TYPE_DATE = 0;
    public static int RANGE_TYPE_ORDER = 1;

    @Override
    public String toString()
    {
        return "ReportFilter{" + "rangeType=" + rangeType + ", init=" + init + ", end=" + end + ", initDate=" + initDate + ", endDate=" + endDate + ", orderType=" + orderType + ", check=" + check + ", testFilterType=" + testFilterType + ", tests=" + tests + ", packageDescription=" + packageDescription + ", listType=" + listType + ", apply=" + apply + ", printerId=" + printerId + ", printAddLabel=" + printAddLabel + ", basic=" + basic + ", reprintFinalReport=" + reprintFinalReport + ", attached=" + attached + ", typeReport=" + typeReport + ", printedOrders=" + printedOrders + '}';
    }
    
    
    

}
