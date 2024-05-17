package net.cltech.enterprisent.domain.operation.results;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un filtro de órdenes para el módulo de registro de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 02/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados - Filtro",
        name = "Filtro Órdenes",
        description = "Representa un filtro órdenes dentro del módulo de registro de resultados"
)
public class ResultFilter
{
    @ApiObjectField(name = "filterId", description = "Identificador del filtro Si es 0 -> por fecha, 1 -> es por ordenes", required = true, order = 1) // 5 -> No realice filtros (Estadisticas Especiales)
    private int filterId;
    @ApiObjectField(name = "firstOrder", description = "Número de orden inicial", required = false, order = 2)
    private long firstOrder;
    @ApiObjectField(name = "lastOrder", description = "Número de orden final", required = false, order = 3)
    private long lastOrder;
    @ApiObjectField(name = "orderType", description = "Tipo de orden", required = false, order = 4)
    private int orderType;
    @ApiObjectField(name = "areaList", description = "Lista de identificadores de áreas", required = false, order = 5)
    private List<Integer> areaList;
    @ApiObjectField(name = "testList", description = "Lista de identificadores de exámenes", required = false, order = 6)
    private List<Integer> testList;
    @ApiObjectField(name = "intResultFilter", description = "Filtro interno para del estado del resultado", required = false, order = 7)
    private int intResultFilter;
    @ApiObjectField(name = "panicFilter", description = "Filtro interno de pánicos sin validar", required = false, order = 8)
    private boolean panicFilter;
    @ApiObjectField(name = "criticFilter", description = "Filtro interno de pánicos críticos sin validar", required = false, order = 9)
    private boolean criticFilter;
    @ApiObjectField(name = "statFilter", description = "Filtro interno de urgencias sin validar", required = false, order = 10)
    private boolean statFilter;
    @ApiObjectField(name = "timeFilter", description = "Filtro interno de oportunidad sin validar", required = false, order = 11)
    private boolean timeFilter;
    @ApiObjectField(name = "attachFilter", description = "Filtro interno de adjunto de resultados sin validar ", required = false, order = 12)
    private boolean attachFilter;
    @ApiObjectField(name = "applyGrowth", description = "Aplica Siembra de Microbiologia en el filtro", required = false, order = 13)
    private boolean applyGrowth;
    @ApiObjectField(name = "sampleStates", description = "Estados de la muestra", order = 14)
    private List<Integer> sampleStates;
    @ApiObjectField(name = "testStates", description = "Estados del la prueba", order = 15)
    private List<Integer> testStates;
    @ApiObjectField(name = "firstDate", description = "Número de orden inicial", required = false, order = 16)
    private int firstDate;
    @ApiObjectField(name = "lastDate", description = "Número de orden final", required = false, order = 17)
    private int lastDate;
    @ApiObjectField(name = "filterByResult", description = "Filtro por resultado de la prueba", required = false, order = 18)
    private List<ResultFilterByResult> filterByResult;
    @ApiObjectField(name = "filterByDemo", description = "Filtro por demográficos", required = false, order = 19)
    private List<FilterDemographic> filterByDemo;
    //----------FILTRO POR HISTORIA-------------
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 20)
    private String patientId;
    @ApiObjectField(name = "documentType", description = "Tipo de Documento", required = false, order = 21)
    private Integer documentType;
    //----------FILTRO POR HISTORIA-------------
    @ApiObjectField(name = "user", description = "Usuario", required = false, order = 22)
    private AuthorizedUser user;
    @ApiObjectField(name = "userId", description = "Identificador del usuario", required = false, order = 23)
    private int userId;
    @ApiObjectField(name = "laboratorys", description = "Lista de laboratorios", required = false, order = 24)
    private List<Laboratory> laboratorys;
    @ApiObjectField(name = "workSheets", description = "Lista de id de las hojas de trabajo", required = false, order = 25)
    private List<Integer> workSheets;
    //----------FILTRO POR ORDEN EXTERNA-------------
    @ApiObjectField(name = "idExternalOrder", description = "Id de la orden externa", required = false, order = 26)
    private String idExternalOrder;
    @ApiObjectField(name = "confidential", description = "Es confidencial", required = false, order = 26)
    private boolean confidential = false;

    public ResultFilter()
    {
        this.areaList = new ArrayList<>(0);
        this.testList = new ArrayList<>(0);
        this.sampleStates = new ArrayList<>(0);
        this.testStates = new ArrayList<>(0);
        this.filterByResult = new ArrayList<>(0);
        this.filterByDemo = new ArrayList<>(0);
        this.laboratorys = new ArrayList<>(0);
        this.workSheets = new ArrayList<>(0);
    }

    public List<Laboratory> getLaboratorys()
    {
        return laboratorys;
    }

    public void setLaboratorys(List<Laboratory> laboratorys)
    {
        this.laboratorys = laboratorys;
    }

    public int getFilterId()
    {
        return filterId;
    }

    public void setFilterId(int filterId)
    {
        this.filterId = filterId;
    }

    public long getFirstOrder()
    {
        return firstOrder;
    }

    public void setFirstOrder(long firstOrder)
    {
        this.firstOrder = firstOrder;
    }

    public long getLastOrder()
    {
        return lastOrder;
    }

    public void setLastOrder(long lastOrder)
    {
        this.lastOrder = lastOrder;
    }

    public int getOrderType()
    {
        return orderType;
    }

    public void setOrderType(int orderType)
    {
        this.orderType = orderType;
    }

    public List<Integer> getTestsId()
    {
        return testList;
    }

    public void setTestsId(List<Integer> testList)
    {
        this.testList = testList;
    }

    public int getIntResultFilter()
    {
        return intResultFilter;
    }

    public void setIntResultFilter(int intResultFilter)
    {
        this.intResultFilter = intResultFilter;
    }

    public boolean isPanicFilter()
    {
        return panicFilter;
    }

    public void setPanicFilter(boolean panicFilter)
    {
        this.panicFilter = panicFilter;
    }

    public boolean isCriticFilter()
    {
        return criticFilter;
    }

    public void setCriticFilter(boolean criticFilter)
    {
        this.criticFilter = criticFilter;
    }

    public boolean isStatFilter()
    {
        return statFilter;
    }

    public void setStatFilter(boolean statFilter)
    {
        this.statFilter = statFilter;
    }

    public boolean isTimeFilter()
    {
        return timeFilter;
    }

    public void setTimeFilter(boolean timeFilter)
    {
        this.timeFilter = timeFilter;
    }

    public boolean isAttachFilter()
    {
        return attachFilter;
    }

    public void setAttachFilter(boolean attachFilter)
    {
        this.attachFilter = attachFilter;
    }

    public boolean isApplyGrowth()
    {
        return applyGrowth;
    }

    public void setApplyGrowth(boolean applyGrowth)
    {
        this.applyGrowth = applyGrowth;
    }

    public List<Integer> getTestList()
    {
        return testList;
    }

    public void setTestList(List<Integer> testList)
    {
        this.testList = testList;
    }

    public List<Integer> getSampleStates()
    {
        return sampleStates;
    }

    public void setSampleStates(List<Integer> sampleStates)
    {
        this.sampleStates = sampleStates;
    }

    public List<Integer> getTestStates()
    {
        return testStates;
    }

    public void setTestStates(List<Integer> testStates)
    {
        this.testStates = testStates;
    }

    public int getFirstDate()
    {
        return firstDate;
    }

    public void setFirstDate(int firstDate)
    {
        this.firstDate = firstDate;
    }

    public int getLastDate()
    {
        return lastDate;
    }

    public void setLastDate(int lastDate)
    {
        this.lastDate = lastDate;
    }

    public List<Integer> getAreaList()
    {
        return areaList;
    }

    public void setAreaList(List<Integer> areaList)
    {
        this.areaList = areaList;
    }

    public List<ResultFilterByResult> getFilterByResult()
    {
        return filterByResult;
    }

    public void setFilterByResult(List<ResultFilterByResult> filterByResult)
    {
        this.filterByResult = filterByResult;
    }

    public List<FilterDemographic> getFilterByDemo()
    {
        return filterByDemo;
    }

    public void setFilterByDemo(List<FilterDemographic> filterByDemo)
    {
        this.filterByDemo = filterByDemo;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public Integer getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(Integer documentType)
    {
        this.documentType = documentType;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public List<Integer> getWorkSheets()
    {
        return workSheets;
    }

    public void setWorkSheets(List<Integer> workSheets)
    {
        this.workSheets = workSheets;
    }

    public String getIdExternalOrder()
    {
        return idExternalOrder;
    }

    public void setIdExternalOrder(String idExternalOrder)
    {
        this.idExternalOrder = idExternalOrder;
    }

    public boolean isConfidential()
    {
        return confidential;
    }

    public void setConfidential(boolean confidential)
    {
        this.confidential = confidential;
    }
}
