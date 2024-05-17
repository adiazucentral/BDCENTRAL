package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un usuario en Home Bound
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Usuarios",
        name = "Usuario Home Bound",
        description = "Usuarios de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHomeBound
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "lastName", description = "Apellido", required = true, order = 2)
    private String lastName;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "userName", description = "	Usuario de autenticación", required = true, order = 4)
    private String userName;
    @ApiObjectField(name = "photoBase64", description = "Foto del paciente en base64", required = true, order = 5)
    private String photoBase64;
    @ApiObjectField(name = "administrator", description = "Es administrador del sistema", required = true, order = 6)
    private boolean administrator;
    @ApiObjectField(name = "administrative", description = "Es administrativo", required = true, order = 7)
    private boolean administrative;
    @ApiObjectField(name = "phlebotomist", description = "Es flebotomista", required = true, order = 8)
    private boolean phlebotomist;
    @ApiObjectField(name = "branch", description = "Sede a la que pertenece el rutero", required = true, order = 9)
    private BranchHomeBound branch;
    @ApiObjectField(name = "zones", description = "Zonas en las que atiende el rutero", required = true, order = 10)
    private List<Zone> zones;
    @ApiObjectField(name = "shifts", description = "Jornadas asociadas a el rutero", required = true, order = 11)
    private List<Shift> shifts;
    @ApiObjectField(name = "administrativeHospitable", description = "administrativo hospitalario", required = true, order = 12)
    private boolean administrativeHospitable;
    @ApiObjectField(name = "phlebotomistHospitable", description = "Flebotomista hospitalario", required = true, order = 13)
    private boolean phlebotomistHospitable;
    @ApiObjectField(name = "state", description = "Estado de usuario 0 -> Desactivado, 1 -> Activo", required = true, order = 14)
    private Boolean state;
    @ApiObjectField(name = "email", description = "Correo del usuario", required = true, order = 15)
    private String email;

    public UserHomeBound()
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

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPhotoBase64()
    {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64)
    {
        this.photoBase64 = photoBase64;
    }

    public boolean isAdministrator()
    {
        return administrator;
    }

    public void setAdministrator(boolean administrator)
    {
        this.administrator = administrator;
    }

    public boolean isAdministrative()
    {
        return administrative;
    }

    public void setAdministrative(boolean administrative)
    {
        this.administrative = administrative;
    }

    public boolean isPhlebotomist()
    {
        return phlebotomist;
    }

    public void setPhlebotomist(boolean phlebotomist)
    {
        this.phlebotomist = phlebotomist;
    }

    public BranchHomeBound getBranch()
    {
        return branch;
    }

    public void setBranch(BranchHomeBound branch)
    {
        this.branch = branch;
    }

    public List<Zone> getZones()
    {
        return zones;
    }

    public void setZones(List<Zone> zones)
    {
        this.zones = zones;
    }

    public List<Shift> getShifts()
    {
        return shifts;
    }

    public void setShifts(List<Shift> shifts)
    {
        this.shifts = shifts;
    }

    public boolean isAdministrativeHospitable()
    {
        return administrativeHospitable;
    }

    public void setAdministrativeHospitable(boolean administrativeHospitable)
    {
        this.administrativeHospitable = administrativeHospitable;
    }

    public boolean isPhlebotomistHospitable()
    {
        return phlebotomistHospitable;
    }

    public void setPhlebotomistHospitable(boolean phlebotomistHospitable)
    {
        this.phlebotomistHospitable = phlebotomistHospitable;
    }

    public Boolean getState()
    {
        return state;
    }

    public void setState(Boolean state)
    {
        this.state = state;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final UserHomeBound other = (UserHomeBound) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    public UserHomeBound clean()
    {
        this.name = "";
        this.lastName = "";
        this.photoBase64 = "";
        this.administrator = false;
        this.administrative = false;
        this.phlebotomist = false;
        this.administrativeHospitable = false;
        this.phlebotomistHospitable = false;
        return this;
    }

    public final static int ACTIVE = 1;
    public final static int DESACTIVE = 0;

}
