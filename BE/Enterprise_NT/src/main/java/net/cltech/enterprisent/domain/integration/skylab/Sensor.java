package net.cltech.enterprisent.domain.integration.skylab;

/**
 * Representa la relacion de prueba y medio de cultivo
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 10/07/2020
 * @see Creacion
 */
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        name = "Sensor",
        group = "Demografico",
        description = "Muestra informacion del maestro Sedes que usa el API desde SkyLab"
)
public class Sensor
{

    @ApiObjectField(name = "id", description = "Id del sensor", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo del sensor", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la sede", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "communicationId", description = "Id comunicacion (Codigo bluetooth o IP)", required = true, order = 1)
    private String communicationId;
    @ApiObjectField(name = "type", description = "Tipo (1-Recolección, 2-Almacenamiento,3-Ambiente)", required = true, order = 2)
    private Short type;
    @ApiObjectField(name = "description", description = "Descripcion de la sede", required = false, order = 10)
    private String description;
    @ApiObjectField(name = "temperatureMin", description = "Temperatura normal minima", required = true, order = 8)
    private Float temperatureMin;
    @ApiObjectField(name = "temperatureMax", description = "Temperatura normal maxima", required = true, order = 8)
    private Float temperatureMax;
    @ApiObjectField(name = "humidityMin", description = "Humedad normal minima", required = true, order = 8)
    private Float humidityMin;
    @ApiObjectField(name = "humidityMax", description = "Humedad normal maxima", required = true, order = 8)
    private Float humidityMax;
    @ApiObjectField(name = "temperatureUnit", description = "Unidad de temperatura", required = true, order = 8)
    private String temperatureUnit;
    @ApiObjectField(name = "humidityUnit", description = "Unidad de humedad", required = true, order = 8)
    private String humidityUnit;
    @ApiObjectField(name = "branch", description = "Sede", required = true, order = 8)
    private BranchSkyLab branch;
    @ApiObjectField(name = "scanStart", description = "Inicio de medición", required = true, order = 8)
    private Date scanStart;
    @ApiObjectField(name = "scanEnd", description = "Fin de medición", required = true, order = 8)
    private Date scanEnd;
    @ApiObjectField(name = "frecuency", description = "Frecuenciá de la medición", required = true, order = 11)
    private Integer frecuency;
    @ApiObjectField(name = "typeMeasurement", description = "Tipo de medicion 0 -> ambos, 1 -> temperatura, 2-> humedad", required = true, order = 12)
    private Integer typeMeasurement;

    public Sensor()
    {
    }

    public Sensor(Sensor sensor)
    {
        if (sensor != null)
        {
            id = sensor.getId();
            code = sensor.getCode();
            name = sensor.getName();
            communicationId = sensor.getCommunicationId();
            type = sensor.getType();
            description = sensor.getDescription();
            temperatureMin = sensor.getTemperatureMin();
            temperatureMax = sensor.getTemperatureMax();
            humidityMin = sensor.getHumidityMin();
            humidityMax = sensor.getHumidityMax();
            temperatureUnit = sensor.getTemperatureUnit();
            humidityUnit = sensor.getHumidityUnit();
            branch = sensor.getBranch();
            scanStart = sensor.getScanStart();
            scanEnd = sensor.getScanEnd();
            frecuency = sensor.getFrecuency();
            typeMeasurement = sensor.getTypeMeasurement();
        }
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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

    public String getCommunicationId()
    {
        return communicationId;
    }

    public void setCommunicationId(String communicationId)
    {
        this.communicationId = communicationId;
    }

    public Short getType()
    {
        return type;
    }

    public void setType(Short type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Float getTemperatureMin()
    {
        return temperatureMin;
    }

    public void setTemperatureMin(Float temperatureMin)
    {
        this.temperatureMin = temperatureMin;
    }

    public Float getTemperatureMax()
    {
        return temperatureMax;
    }

    public void setTemperatureMax(Float temperatureMax)
    {
        this.temperatureMax = temperatureMax;
    }

    public Float getHumidityMin()
    {
        return humidityMin;
    }

    public void setHumidityMin(Float humidityMin)
    {
        this.humidityMin = humidityMin;
    }

    public Float getHumidityMax()
    {
        return humidityMax;
    }

    public void setHumidityMax(Float humidityMax)
    {
        this.humidityMax = humidityMax;
    }

    public String getTemperatureUnit()
    {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit)
    {
        this.temperatureUnit = temperatureUnit;
    }

    public String getHumidityUnit()
    {
        return humidityUnit;
    }

    public void setHumidityUnit(String humidityUnit)
    {
        this.humidityUnit = humidityUnit;
    }

    public BranchSkyLab getBranch()
    {
        return branch;
    }

    public void setBranch(BranchSkyLab branch)
    {
        this.branch = branch;
    }

    public Date getScanStart()
    {
        return scanStart;
    }

    public void setScanStart(Date scanStart)
    {
        this.scanStart = scanStart;
    }

    public Date getScanEnd()
    {
        return scanEnd;
    }

    public void setScanEnd(Date scanEnd)
    {
        this.scanEnd = scanEnd;
    }

    public Integer getFrecuency()
    {
        return frecuency;
    }

    public void setFrecuency(Integer frecuency)
    {
        this.frecuency = frecuency;
    }

    public Integer getTypeMeasurement()
    {
        return typeMeasurement;
    }

    public void setTypeMeasurement(Integer typeMeasurement)
    {
        this.typeMeasurement = typeMeasurement;
    }

}
