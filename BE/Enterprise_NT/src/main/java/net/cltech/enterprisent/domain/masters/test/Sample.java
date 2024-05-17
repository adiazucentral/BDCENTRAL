package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.tracking.SampleState;
import net.cltech.enterprisent.domain.operation.tracking.SampleTracking;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de Muestra
 *
 * @author enavas
 * @version 1.0.0
 * @since 28/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Muestra",
        description = "Representa una Muestra"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sample extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id de la muestra", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la muestra", order = 2)
    private String name;
    @ApiObjectField(name = "printable", description = "Opcion si imprime el cogido de barras", order = 3)
    private boolean printable;
    @ApiObjectField(name = "canstiker", description = "Cantidad de stike", order = 4)
    private Integer canstiker;
    @ApiObjectField(name = "check", description = "Opcion si se verifica la muestra", order = 5)
    private boolean check;
    @ApiObjectField(name = "managementsample", description = "Descripción del manejo de la muestra", order = 6)
    private String managementsample;
    @ApiObjectField(name = "daysstored", description = "Numero de dias de almacenamiento", order = 7)
    private Integer daysstored;
    @ApiObjectField(name = "state", description = "Estado de la muestra", order = 8)
    private boolean state;
    @ApiObjectField(name = "container", description = "Id del recipiente", order = 9)
    private Container container = new Container();
    @ApiObjectField(name = "codesample", description = "Codigo de la muestra", order = 10)
    private String codesample;
    @ApiObjectField(name = "laboratorytype", description = "Tipo de laboratorio", order = 11)
    private String laboratorytype;
    @ApiObjectField(name = "typebarcode", description = "Tipo de codigo de barras 0 : 39, 1:128 ", order = 13)
    private boolean typebarcode;
    @ApiObjectField(name = "subSamples", description = "Lista de sub-muestras asignadas ", order = 14)
    private List<Sample> subSamples = new ArrayList<>();
    @ApiObjectField(name = "selected", description = "Si esta asignada la muestra", order = 15)
    private boolean selected;
    @ApiObjectField(name = "tests", description = "Lista de Examenes - Operación.", order = 14)
    private List<Test> tests = new ArrayList<>();
    @ApiObjectField(name = "destinatios", description = "Lista de destinos de la muestra", order = 14)
    private List<Destination> destinatios;
    @ApiObjectField(name = "sampleTrackings", description = "Trazabilidad de la Muestra - Operación.", order = 14)
    private List<SampleTracking> sampleTrackings = new ArrayList<>();
    @ApiObjectField(name = "quantityDestination", description = "Cantidad de Destinos", order = 15)
    private Integer quantityDestination;
    @ApiObjectField(name = "quantityVerifyDestination", description = "Cantidad de Destinos Verificados", order = 16)
    private Integer quantityVerifyDestination;
    @ApiObjectField(name = "sampleState", description = "Estado Actual de la Muestra", order = 17)
    private SampleState sampleState;
    @ApiObjectField(name = "qualityTime", description = "Tiempo en minutos de calidad de la muestra", order = 18)
    private Long qualityTime;
    @ApiObjectField(name = "qualitypercentage", description = "Porcentaje para alarma de calidad de la muestra", order = 18)
    private Integer qualityPercentage;
    @ApiObjectField(name = "qualityFlag", description = "Identifica la calidad de la muestra<br>"
            + "1 - Si el tiempo transcurrido no ha sobrepasadoel porcentaje de alarma<br>"
            + "2 - Si el tiempo se encuentra entre el porcentaje de alarma y la caducidad de la muestra<br> "
            + "3 - Si se sobrepasa el tiempo en que caduca la muestra", order = 18)
    private Integer qualityFlag;
    @ApiObjectField(name = "takeDate", description = "Fecha de la toma", order = 19)
    private Date takeDate;
    @ApiObjectField(name = "specialStorage", description = "Indica si se almacena en una gradilla especial ", order = 20)
    private Boolean specialStorage = false;
    @ApiObjectField(name = "minimumTemperature", description = "Numero de dias de almacenamiento", order = 21)
    private Float minimumTemperature;
    @ApiObjectField(name = "maximumTemperature", description = "Numero de dias de almacenamiento", order = 22)
    private Float maximumTemperature;
    @ApiObjectField(name = "coveredSample", description = "Muestra tapada", order = 23)
    private boolean coveredSample;
    @ApiObjectField(name = "temperature", description = "Temperatura de la muestra", order = 24)
    private Double temperature;

    public Sample()
    {
        tests = new ArrayList<>(0);
    }

    public Sample(Integer id)
    {
        this();
        this.id = id;
    }

    public Sample(Sample sample)
    {
        this();
        this.id = sample.getId();
        this.codesample = sample.getCodesample();
        this.name = sample.getName();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isPrintable()
    {
        return printable;
    }

    public void setPrintable(boolean printable)
    {
        this.printable = printable;
    }

    public Integer getCanstiker()
    {
        return canstiker;
    }

    public void setCanstiker(Integer canstiker)
    {
        this.canstiker = canstiker;
    }

    public boolean isCheck()
    {
        return check;
    }

    public void setCheck(boolean check)
    {
        this.check = check;
    }

    public String getManagementsample()
    {
        return managementsample;
    }

    public void setManagementsample(String managementsample)
    {
        this.managementsample = managementsample;
    }

    public Integer getDaysstored()
    {
        return daysstored;
    }

    public void setDaysstored(Integer daysstored)
    {
        this.daysstored = daysstored;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Container getContainer()
    {
        return container;
    }

    public void setContainer(Container container)
    {
        this.container = container;
    }

    public String getCodesample()
    {
        return codesample;
    }

    public void setCodesample(String codesample)
    {
        this.codesample = codesample;
    }

    public boolean isTypebarcode()
    {
        return typebarcode;
    }

    public void setTypebarcode(boolean typebarcode)
    {
        this.typebarcode = typebarcode;
    }

    public String getLaboratorytype()
    {
        return laboratorytype;
    }

    public void setLaboratorytype(String laboratorytype)
    {
        this.laboratorytype = laboratorytype;
    }

    public List<Sample> getSubSamples()
    {
        return subSamples;
    }

    public void setSubSamples(List<Sample> subSamples)
    {
        this.subSamples = subSamples;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public List<Test> getTests()
    {
        return tests;
    }

    public void setTests(List<Test> tests)
    {
        this.tests = tests;
    }

    public Integer getQuantityDestination()
    {
        return quantityDestination;
    }

    public void setQuantityDestination(Integer quantityDestination)
    {
        this.quantityDestination = quantityDestination;
    }

    public Integer getQuantityVerifyDestination()
    {
        return quantityVerifyDestination;
    }

    public void setQuantityVerifyDestination(Integer quantityVerifyDestination)
    {
        this.quantityVerifyDestination = quantityVerifyDestination;
    }

    public List<SampleTracking> getSampleTrackings()
    {
        return sampleTrackings;
    }

    public void setSampleTrackings(List<SampleTracking> sampleTrackings)
    {
        this.sampleTrackings = sampleTrackings;
    }

    public SampleState getSampleState()
    {
        return sampleState;
    }

    public void setSampleState(SampleState sampleState)
    {
        this.sampleState = sampleState;
    }

    public Long getQualityTime()
    {
        return qualityTime;
    }

    public void setQualityTime(Long qualityTime)
    {
        this.qualityTime = qualityTime;
    }

    public Integer getQualityPercentage()
    {
        return qualityPercentage;
    }

    public void setQualityPercentage(Integer qualityPercentage)
    {
        this.qualityPercentage = qualityPercentage;
    }

    public Integer getQualityFlag()
    {
        return qualityFlag;
    }

    public void setQualityFlag(Integer qualityFlag)
    {
        this.qualityFlag = qualityFlag;
    }

    public List<Destination> getDestinatios()
    {
        return destinatios;
    }

    public void setDestinatios(List<Destination> destinatios)
    {
        this.destinatios = destinatios;
    }

    public Date getTakeDate()
    {
        return takeDate;
    }

    public void setTakeDate(Date takeDate)
    {
        this.takeDate = takeDate;
    }

    public Boolean getSpecialStorage()
    {
        return specialStorage;
    }

    public void setSpecialStorage(Boolean specialStorage)
    {
        this.specialStorage = specialStorage;
    }

    public Float getMinimumTemperature()
    {
        return minimumTemperature;
    }

    public void setMinimumTemperature(Float minimumTemperature)
    {
        this.minimumTemperature = minimumTemperature;
    }

    public Float getMaximumTemperature()
    {
        return maximumTemperature;
    }

    public void setMaximumTemperature(Float maximumTemperature)
    {
        this.maximumTemperature = maximumTemperature;
    }

    public boolean isCoveredSample()
    {
        return coveredSample;
    }

    public void setCoveredSample(boolean coveredSample)
    {
        this.coveredSample = coveredSample;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
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
        final Sample other = (Sample) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    public Double getTemperature()
    {
        return temperature;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }
}
