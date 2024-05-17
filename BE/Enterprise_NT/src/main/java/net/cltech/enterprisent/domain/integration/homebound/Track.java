package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa detalle de trazabilidad para homebound
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Homebound",
        name = "Trazabilidad Cabecera",
        description = "Información de la orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Track
{

    @ApiObjectField(name = "orderNumber", description = "Numero de cita", order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "orderDate", description = "Fecha de cita", order = 2)
    private Integer orderDate;
    @ApiObjectField(name = "isActive", description = "Indica si la orde esta activa", order = 3)
    private Boolean isActive;
    @ApiObjectField(name = "type", description = "Tipo de orden", order = 4)
    private String type;
    @ApiObjectField(name = "patientId", description = "Identificación paciente", order = 5)
    private String patientId;
    @ApiObjectField(name = "names", description = "Nombres paciente", order = 6)
    private String names;
    @ApiObjectField(name = "lastName", description = "Apellidos del paciente", order = 7)
    private String lastName;
    @ApiObjectField(name = "gender", description = "Genero del paciente", order = 8)
    private String gender;
    @ApiObjectField(name = "birthday", description = "Fecha de nacimiento yyyy-MM-dd", order = 9)
    private String birthday;
    private Integer patientIdDB;
    @ApiObjectField(name = "tracking", description = "Lista de trazabilidad", order = 10)
    private List<TrackDetail> tracking;

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public List<TrackDetail> getTracking()
    {
        return tracking;
    }

    public void setTracking(List<TrackDetail> tracking)
    {
        this.tracking = tracking;
    }

    public Integer getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Integer orderDate)
    {
        this.orderDate = orderDate;
    }

    public Boolean getIsActive()
    {
        return isActive;
    }

    public void setIsActive(Boolean isActive)
    {
        this.isActive = isActive;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getNames()
    {
        return names;
    }

    public void setNames(String names)
    {
        this.names = names;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public Integer getPatientIdDB()
    {
        return patientIdDB;
    }

    public void setPatientIdDB(Integer patientIdDB)
    {
        this.patientIdDB = patientIdDB;
    }

}
