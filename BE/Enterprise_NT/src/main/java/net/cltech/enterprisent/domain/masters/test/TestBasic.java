package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.operation.orders.Result;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion basica del maestro Pruebas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Prueba Basica",
        description = "Muestra informacion basica del maestro Pruebas que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestBasic extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "area", description = "Area de la prueba", required = true, order = 2)
    private Area area;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 3)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 4)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 5)
    private String name;
    @ApiObjectField(name = "state", description = "Estado", required = true, order = 6)
    private boolean state;
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = true, order = 7)
    private Short testType;
    @ApiObjectField(name = "resultType", description = "Tipo de resultado de la prueba :  1 -> Numerico, 2 -> Texto", required = true, order = 8)
    private Short resultType;
    @ApiObjectField(name = "printOrder", description = "Orden de impresión", required = true, order = 9)
    private Integer printOrder;
    @ApiObjectField(name = "conversionFactor", description = "Factor de conversión", required = true, order = 10)
    private BigDecimal conversionFactor;
    @ApiObjectField(name = "processingBy", description = "Procesamiento por: 1 -> Manualmente, 2 -> Middleware", required = true, order = 11)
    private Short processingBy;
    @ApiObjectField(name = "deltacheckDays", description = "Días para validación deltacheck", required = true, order = 12)
    private Integer deltacheckDays;
    @ApiObjectField(name = "deltacheckMin", description = "Porcentaje deltacheck por debajo", required = true, order = 13)
    private BigDecimal deltacheckMin;
    @ApiObjectField(name = "deltacheckMax", description = "Porcentaje deltacheck por encima", required = true, order = 14)
    private BigDecimal deltacheckMax;
    @ApiObjectField(name = "processingDays", description = "Dias de procesamiento de la prueba", required = true, order = 15)
    private String processingDays;
    @ApiObjectField(name = "decimal", description = "Decimal de la prueba", required = true, order = 15)
    private Short decimal;
    @ApiObjectField(name = "selected", description = "Esta seleccionado", required = true, order = 16)
    private boolean selected;
    @ApiObjectField(name = "confidential", description = "Indica si el examen es confidencial", required = true, order = 16)
    private boolean confidential;
    @ApiObjectField(name = "sample", description = "Muestra", required = true, order = 17)
    private Sample sample;
    @ApiObjectField(name = "unit", description = "Unidad", required = false, order = 18)
    private Unit unit;
    @ApiObjectField(name = "technique", description = "Tecnica", required = false, order = 19)
    private Technique technique;
    @ApiObjectField(name = "print", description = "0->No Imprime,1->Imprime,2->Imprime con resultado", required = false, order = 20)
    private int print;
    @ApiObjectField(name = "historicGraphic", description = "0->No imprime graficas en reporte, 1->Si imprime graficas", required = false, order = 21)
    private int printHistoricGraphic;
    @ApiObjectField(name = "viewInOrderEntry", description = "0->Ver en ingreso de ordenes, 1->No ver en ingreso de ordenes", required = false, order = 22)
    private int viewInOrderEntry;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 23)
    private Result result;
    @ApiObjectField(name = "tax", description = "Impuesto para la prueba", required = true, order = 24)
    private BigDecimal tax;
    @ApiObjectField(name = "billing", description = "Facturacion", required = true, order = 25)
    private boolean billing;
    @ApiObjectField(name = "gender", description = "Genero", required = true, order = 26)
    private Item gender = new Item();
    @ApiObjectField(name = "minAge", description = "Edad minima", required = true, order = 27)
    private Integer minAge;
    @ApiObjectField(name = "maxAge", description = "Edad maxima", required = true, order = 28)
    private Integer maxAge;
    @ApiObjectField(name = "unitAge", description = "Unidad de edad del valor de referencia", required = true, order = 29)
    private Short unitAge;
    @ApiObjectField(name = "typeOrder", description = "Tipo de orden", required = true, order = 30)
    private Integer typeOrder;
    @ApiObjectField(name = "externalSystemOrder", description = "Orden sistema externo", required = true, order = 30)
    private String externalSystemOrder;
    @ApiObjectField(name = "informedConsent", description = "Consentimiento informado", required = true, order = 31)
    private boolean informedConsent;
    @ApiObjectField(name = "profile", description = "Perfil", required = true, order = 32)
    private Integer profile;
    @ApiObjectField(name = "nameprofile", description = "Codigo del Perfil", required = true, order = 33)
    private String nameprofile;
    @ApiObjectField(name = "abbprofile", description = "abreviatura Perfil", required = true, order = 34)
    private String abbrprofile;
    @ApiObjectField(name = "testalarm", description = "Alarma en ingreso de ordenes", required = true, order = 35)
    private boolean testalarm;
    @ApiObjectField(name = "excludeHoliday", description = "Excluir dias dias festivos de dias de procesamiento", required = true, order = 36)
    private boolean excludeHoliday;
    @ApiObjectField(name = "deleteProfile", description = "Eliminar de perfil", required = true, order = 27)
    private Integer deleteProfile;
    

    public TestBasic()
    {
        area = new Area();
        sample = new Sample();
        unit = new Unit();
    }

    public TestBasic(Integer id)
    {
        this.id = id;
    }

    public TestBasic(Integer id, Integer printOrder)
    {
        this.id = id;
        this.printOrder = printOrder;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Area getArea()
    {
        return area;
    }

    public void setArea(Area area)
    {
        this.area = area;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Short getTestType()
    {
        return testType;
    }

    public void setTestType(Short testType)
    {
        this.testType = testType;
    }

    public Short getResultType()
    {
        return resultType;
    }

    public void setResultType(Short resultType)
    {
        this.resultType = resultType;
    }

    public Integer getPrintOrder()
    {
        return printOrder;
    }

    public void setPrintOrder(Integer printOrder)
    {
        this.printOrder = printOrder;
    }

    public BigDecimal getConversionFactor()
    {
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimal conversionFactor)
    {
        this.conversionFactor = conversionFactor;
    }

    public Short getProcessingBy()
    {
        return processingBy;
    }

    public void setProcessingBy(Short processingBy)
    {
        this.processingBy = processingBy;
    }

    public Integer getDeltacheckDays()
    {
        return deltacheckDays;
    }

    public void setDeltacheckDays(Integer deltacheckDays)
    {
        this.deltacheckDays = deltacheckDays;
    }

    public BigDecimal getDeltacheckMin()
    {
        return deltacheckMin;
    }

    public void setDeltacheckMin(BigDecimal deltacheckMin)
    {
        this.deltacheckMin = deltacheckMin;
    }

    public BigDecimal getDeltacheckMax()
    {
        return deltacheckMax;
    }

    public void setDeltacheckMax(BigDecimal deltacheckMax)
    {
        this.deltacheckMax = deltacheckMax;
    }

    public String getProcessingDays()
    {
        return processingDays;
    }

    public void setProcessingDays(String processingDays)
    {
        this.processingDays = processingDays;
    }

    public Short getDecimal()
    {
        return decimal;
    }

    public void setDecimal(Short decimal)
    {
        this.decimal = decimal;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

    public Unit getUnit()
    {
        return unit;
    }

    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }

    public boolean isConfidential()
    {
        return confidential;
    }

    public void setConfidential(boolean confidential)
    {
        this.confidential = confidential;
    }

    public Technique getTechnique()
    {
        return technique;
    }

    public void setTechnique(Technique technique)
    {
        this.technique = technique;
    }

    public int getPrint()
    {
        return print;
    }

    public void setPrint(int print)
    {
        this.print = print;
    }

    public int getPrintHistoricGraphic()
    {
        return printHistoricGraphic;
    }

    public void setPrintHistoricGraphic(int printHistoricGraphic)
    {
        this.printHistoricGraphic = printHistoricGraphic;
    }

    public int getViewInOrderEntry()
    {
        return viewInOrderEntry;
    }

    public void setViewInOrderEntry(int viewInOrderEntry)
    {
        this.viewInOrderEntry = viewInOrderEntry;
    }

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }

    public BigDecimal getTax()
    {
        return tax;
    }

    public void setTax(BigDecimal tax)
    {
        this.tax = tax;
    }

    public boolean isBilling()
    {
        return billing;
    }

    public void setBilling(boolean billing)
    {
        this.billing = billing;
    }

    public Item getGender()
    {
        return gender;
    }

    public void setGender(Item gender)
    {
        this.gender = gender;
    }

    public Integer getMinAge()
    {
        return minAge;
    }

    public void setMinAge(Integer minAge)
    {
        this.minAge = minAge;
    }

    public Integer getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge)
    {
        this.maxAge = maxAge;
    }

    public Short getUnitAge()
    {
        return unitAge;
    }

    public void setUnitAge(Short unitAge)
    {
        this.unitAge = unitAge;
    }

    public Integer getTypeOrder()
    {
        return typeOrder;
    }

    public void setTypeOrder(Integer typeOrder)
    {
        this.typeOrder = typeOrder;
    }

    public String getExternalSystemOrder()
    {
        return externalSystemOrder;
    }

    public void setExternalSystemOrder(String externalSystemOrder)
    {
        this.externalSystemOrder = externalSystemOrder;
    }

    public boolean isInformedConsent()
    {
        return informedConsent;
    }

    public void setInformedConsent(boolean informedConsent)
    {
        this.informedConsent = informedConsent;
    }
    
    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }
    
    public String getNameprofile() {
        return nameprofile;
    }

    public void setNameprofile(String nameprofile) {
        this.nameprofile = nameprofile;
    }

    public String getAbbrprofile() {
        return abbrprofile;
    }

    public void setAbbrprofile(String abbrprofile) {
        this.abbrprofile = abbrprofile;
    }

    public boolean isTestalarm() {
        return testalarm;
    }

    public void setTestalarm(boolean testalarm) {
        this.testalarm = testalarm;
    }

    public boolean isExcludeHoliday()
    {
        return excludeHoliday;
    }

    public void setExcludeHoliday(boolean excludeHoliday)
    {
        this.excludeHoliday = excludeHoliday;
    }

    public Integer getDeleteProfile() {
        return deleteProfile;
    }

    public void setDeleteProfile(Integer deleteProfile) {
        this.deleteProfile = deleteProfile;
    }
    
    
        
    @Override
    public int hashCode()
    {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TestBasic other = (TestBasic) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
