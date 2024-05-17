package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeSample;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Filtro Busquedas",
        description = "Representa filtro con parametros para busquedas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter
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
    @ApiObjectField(name = "demographics", description = "Lista de filtro por demograficos", order = 10)
    private List<FilterDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "packageDescription", description = "Maneja descripción de paquetes", order = 11)
    private boolean packageDescription;
    @ApiObjectField(name = "listType", description = "Tipo de Listado : 0 -> Normal, 1 -> Pacientes por Area, 2 -> Pacientes por Examen, 3 -> Normal Sin Agrupar", order = 12)
    private int listType;
    @ApiObjectField(name = "laboratories", description = "Laboratorios de referencia", order = 13)
    private List<Integer> laboratories;
    @ApiObjectField(name = "samples", description = "Muestras", order = 14)
    private List<BarcodeSample> samples = new ArrayList<>();
    @ApiObjectField(name = "apply", description = "Indica si se aplica Impresion de etiqueta adicional.", order = 15)
    private boolean apply;
    @ApiObjectField(name = "printerId", description = "Ip equipo", order = 16)
    private String printerId;
    @ApiObjectField(name = "printAddLabel", description = "Imprime etiquta adicional.", order = 17)
    private boolean printAddLabel;
    @ApiObjectField(name = "basic", description = "Indica si se mostrara información basica de la orden, es decir, no seran enviados los examenes.", order = 18)
    private boolean basic;
    @ApiObjectField(name = "orders", description = "Lista de ordenes", order = 19)
    private List<Long> orders;
    @ApiObjectField(name = "testState", description = "Estado del examen, vacio - Todos, 0-Ordenado, 1 - Repeticion, 2 - Reportado,3 - Preliminar, 4 - Validado, 5 - Impreso ", order = 20)
    private List<Integer> testState;
    @ApiObjectField(name = "sampleState", description = "Estado de la muestra, vacio - Todos, 0 - Rechazada, 1 - Nueva muestra(retoma), 2 - Ordenada, 3 - Tomada, 4 - Verificada ", order = 21)
    private List<Integer> sampleState;
    @ApiObjectField(name = "resultState", description = "Tipo resultado, vacio - Todos, 1 - Modificado, 2 - Repetición, 3 - Patologíco, 4 - Pánico, 5 - Delta Check ", order = 22)
    private List<Integer> resultState;
    @ApiObjectField(name = "reprintFinalReport", description = "Reimprimir informe final", order = 23)
    private boolean reprintFinalReport;
    @ApiObjectField(name = "attached", description = "Adjuntos", order = 24)
    private boolean attached;
    @ApiObjectField(name = "typeReport", description = "Indica si se imprimen informes finales(1), previos(4), copias(3) preliminar(2) o re impresion(0)", order = 25)
    private Integer typeReport;
    @ApiObjectField(name = "filterState", description = "Estado del examen/muestra, 0 - Todos, 1-Toma, 2 - Verificación, 3 - Reportado,4 - Validación, 5 - Impresión", order = 26)
    private List<Integer> filterState;
    @ApiObjectField(name = "numberCopies", description = "Cantidad de copias", order = 27)
    private int numberCopies;
    @ApiObjectField(name = "printingType", description = "Tipo de impresion: 1 -> Reporte, 2-> Codigo de barras", order = 28)
    private int printingType;
    @ApiObjectField(name = "serial", description = "Serial del equipo donde se va a enviar la impresion", order = 29)
    private String serial;
    @ApiObjectField(name = "orderingPrint", description = "Ordenamiento de impresión por orden -> 1 y historia -> 2 ", order = 30)
    private int orderingPrint;
    @ApiObjectField(name = "orderingfilterDemo", description = "Validar si ordenamiento por demografico", order = 31)
    private boolean orderingfilterDemo;
    @ApiObjectField(name = "remission", description = "Remision", order = 32)
    private int remission;
    @ApiObjectField(name = "laboratory", description = "Laboratorio", order = 33)
    private int laboratory;
    @ApiObjectField(name = "attended", description = "Panicos Atendidos o no atendidos", order = 34)
    private int attended;
    @ApiObjectField(name = "filterType", description = "Tipo de filtro por edad: 1- Por edad", order = 35)
    private List<Integer> filterType;
    @ApiObjectField(name = "initAge", description = "Edad inicial", order = 36)
    private int initAge;
    @ApiObjectField(name = "endAge", description = "Edad final", order = 37)
    private int endAge;
    @ApiObjectField(name = "sex", description = "Sexo", order = 38)
    private int sex;
    @ApiObjectField(name = "groupProfiles", description = "Agrupar por perfiles", order = 39)
    private boolean groupProfiles;
    @ApiObjectField(name = "typeDelivery", description = "Tipo de entrega", order = 37)
    private int typeDelivery;
    @ApiObjectField(name = "patientId", description = "Id del paciente", order = 38)
    private Integer patientId;
    @ApiObjectField(name = "userId", description = "Id usuario", order = 39)
    private Integer userId;
    @ApiObjectField(name = "appointmenttype", description = "Tipo de cita", order = 39)
    private Integer appointmenttype;
    
    public Filter()
    {
    }

    public Filter(Integer rangeType, Long init, Long end)
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

    public List<FilterDemographic> getDemographics()
    {
        return demographics;
    }

    public void setDemographics(List<FilterDemographic> demographics)
    {
        this.demographics = demographics;
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

    public List<Integer> getLaboratories()
    {
        return laboratories;
    }

    public void setLaboratories(List<Integer> laboratories)
    {
        this.laboratories = laboratories;
    }

    public boolean isApply()
    {
        return apply;
    }

    public void setApply(boolean apply)
    {
        this.apply = apply;
    }

    public List<BarcodeSample> getSamples()
    {
        return samples;
    }

    public void setSamples(List<BarcodeSample> samples)
    {
        this.samples = samples;
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

    public List<Long> getOrders()
    {
        return orders;
    }

    public void setOrders(List<Long> orders)
    {
        this.orders = orders;
    }

    public List<Integer> getTestState()
    {
        return testState;
    }

    public void setTestState(List<Integer> testState)
    {
        this.testState = testState;
    }

    public List<Integer> getResultState()
    {
        return resultState;
    }

    public void setResultState(List<Integer> resultState)
    {
        this.resultState = resultState;
    }

    public List<Integer> getSampleState()
    {
        return sampleState;
    }

    public void setSampleState(List<Integer> sampleState)
    {
        this.sampleState = sampleState;
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

    public List<Integer> getFilterState()
    {
        return filterState;
    }

    public void setFilterState(List<Integer> filterState)
    {
        this.filterState = filterState;
    }

    public int getNumberCopies()
    {
        return numberCopies;
    }

    public void setNumberCopies(int numberCopies)
    {
        this.numberCopies = numberCopies;
    }

    public int getPrintingType()
    {
        return printingType;
    }

    public void setPrintingType(int printingType)
    {
        this.printingType = printingType;
    }

    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public int getOrderingPrint()
    {
        return orderingPrint;
    }

    public void setOrderingPrint(int orderingPrint)
    {
        this.orderingPrint = orderingPrint;
    }

    public boolean isOrderingfilterDemo()
    {
        return orderingfilterDemo;
    }

    public void setOrderingfilterDemo(boolean orderingfilterDemo)
    {
        this.orderingfilterDemo = orderingfilterDemo;
    }

    public int getRemission() {
        return remission;
    }

    public void setRemission(int remission) {
        this.remission = remission;
    }

    public int getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(int laboratory) {
        this.laboratory = laboratory;
    }

    public int getAttended() {
        return attended;
    }

    public void setAttended(int attended) {
        this.attended = attended;
    }

    public List<Integer> getFilterType() {
        return filterType;
    }

    public void setFilterType(List<Integer> filterType) {
        this.filterType = filterType;
    }

    public int getInitAge() {
        return initAge;
    }

    public void setInitAge(int initAge) {
        this.initAge = initAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public void setEndAge(int endAge) {
        this.endAge = endAge;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }    

    public boolean isGroupProfiles() {
        return groupProfiles;
    }

    public void setGroupProfiles(boolean groupProfiles) {
        this.groupProfiles = groupProfiles;
    }

    public int getTypeDelivery()
    {
        return typeDelivery;
    }

    public void setTypeDelivery(int typeDelivery)
    {
        this.typeDelivery = typeDelivery;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAppointmenttype() {
        return appointmenttype;
    }

    public void setAppointmenttype(Integer appointmenttype) {
        this.appointmenttype = appointmenttype;
    }
    
    
    
    public static int RANGE_TYPE_DATE = 0;
    public static int RANGE_TYPE_ORDER = 1;

}
