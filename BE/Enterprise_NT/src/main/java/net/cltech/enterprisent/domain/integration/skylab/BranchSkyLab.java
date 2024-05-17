package net.cltech.enterprisent.domain.integration.skylab;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Sedes SkyLab
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 09/07/2020
 * @see Creación
 */
@ApiObject(
        group = "Maestro - Rutero",
        name = "Sede",
        description = "Muestra informacion del maestro Sedes que usa el API para SkyLab"
)
public class BranchSkyLab
{

    @ApiObjectField(name = "id", description = "Id de la sede", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la sede", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbreviation", description = "Abreviación de la sede", required = true, order = 2)
    private String abbreviation;
    @ApiObjectField(name = "responsable", description = "Responsable de la sede", required = true, order = 2)
    private String responsable;
    @ApiObjectField(name = "name", description = "Nombre de la sede", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "address", description = "Dirección de la sede", required = true, order = 5)
    private String address;
    @ApiObjectField(name = "phone", description = "Telefono de la sede", required = true, order = 6)
    private String phone;
    @ApiObjectField(name = "email", description = "Email de la sede", required = true, order = 6)
    private String email;
    @ApiObjectField(name = "coords", description = "Cordenadas Latitud,Longitud", required = false, order = 7)
    private String coords;
    @ApiObjectField(name = "state", description = "Estado de la sede", required = true, order = 9)
    private boolean state;
    @ApiObjectField(name = "selected", description = "Indica la sede asignada", required = true, order = 9)
    private boolean selected;
    @ApiObjectField(name = "description", description = "Descripcion de la sede", required = false, order = 10)
    private String description;
    @ApiObjectField(name = "sensors", description = "Lista de sensores en la sede", required = true, order = 8)
    private List<Sensor> sensors;
    @ApiObjectField(name = "sort", description = "Orden de la ruta", required = true, order = 15)
    private Integer sort;
    @ApiObjectField(name = "configured", description = "Indica si la sede se encuentra configurada", required = true, order = 16)
    private boolean configured;
    @ApiObjectField(name = "photo", description = "Foto de la sede", required = false, order = 10)
    private String photo;
    @ApiObjectField(name = "visit", description = "Estado de la sede (1-visitado, 2-No visitada, 3-Inabilitada)", required = true, order = 17)
    private Integer visit;
    @ApiObjectField(name = "date", description = "Fecha ", required = true, order = 18)
    private Date date;
    @ApiObjectField(name = "idLis", description = "Id del LIS", required = true, order = 19)
    private Integer idLis;
    @ApiObjectField(name = "dateModification", description = "Fecha de modificacion", required = true, order = 20)
    private Timestamp dateModification;
    @ApiObjectField(name = "userModification", description = "Usuario de modificacion", required = true, order = 21)
    private int userModification;

    public BranchSkyLab()
    {
    }

    public BranchSkyLab(Integer id)
    {

        this.id = id;
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

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    public String getResponsable()
    {
        return responsable;
    }

    public void setResponsable(String responsable)
    {
        this.responsable = responsable;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getCoords()
    {
        return coords;
    }

    public void setCoords(String coords)
    {
        this.coords = coords;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<Sensor> getSensors()
    {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors)
    {
        this.sensors = sensors;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public boolean isConfigured()
    {
        return configured;
    }

    public void setConfigured(boolean configured)
    {
        this.configured = configured;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public Integer getVisit()
    {
        return visit;
    }

    public void setVisit(Integer visit)
    {
        this.visit = visit;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Integer getIdLis()
    {
        return idLis;
    }

    public void setIdLis(Integer idLis)
    {
        this.idLis = idLis;
    }

    public Timestamp getDateModification()
    {
        return dateModification;
    }

    public void setDateModification(Timestamp dateModification)
    {
        this.dateModification = dateModification;
    }

    public int getUserModification()
    {
        return userModification;
    }

    public void setUserModification(int userModification)
    {
        this.userModification = userModification;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final BranchSkyLab other = (BranchSkyLab) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}
