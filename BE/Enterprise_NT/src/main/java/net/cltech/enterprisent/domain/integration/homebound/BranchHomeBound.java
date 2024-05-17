package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Sedes
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Configuracion",
        name = "Sede desde Home Bound",
        description = "Muestra informacion del maestro Sedes que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchHomeBound
{

    @ApiObjectField(name = "id", description = "Id de la sede", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la sede", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la sede", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "address", description = "Direccion de la sede", required = true, order = 4)
    private String address;
    @ApiObjectField(name = "phone1", description = "Telefono 1 de la sede", required = true, order = 5)
    private String phone1;
    @ApiObjectField(name = "phone2", description = "Telefono 2 de la sede", required = true, order = 6)
    private String phone2;
    @ApiObjectField(name = "responsable", description = "Responsable de la sede", required = true, order = 7)
    private String responsable;
    @ApiObjectField(name = "latitude", description = "Latitud de la sede", required = true, order = 8)
    private Float latitude;
    @ApiObjectField(name = "longitude", description = "Longitud de la sede", required = true, order = 9)
    private Float longitude;
    @ApiObjectField(name = "photo", description = "Foto de la sede", required = true, order = 10)
    private String photo;

    public BranchHomeBound()
    {
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

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone1()
    {
        return phone1;
    }

    public void setPhone1(String phone1)
    {
        this.phone1 = phone1;
    }

    public String getPhone2()
    {
        return phone2;
    }

    public void setPhone2(String phone2)
    {
        this.phone2 = phone2;
    }

    public String getResponsable()
    {
        return responsable;
    }

    public void setResponsable(String responsable)
    {
        this.responsable = responsable;
    }

    public Float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Float latitude)
    {
        this.latitude = latitude;
    }

    public Float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Float longitude)
    {
        this.longitude = longitude;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

}
