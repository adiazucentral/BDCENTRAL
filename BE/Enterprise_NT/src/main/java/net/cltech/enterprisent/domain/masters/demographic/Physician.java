package net.cltech.enterprisent.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de médico
 *
 * @author eacuna
 * @version 1.0.0
 * @since 24/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Médico",
        description = "Representa un médico"
)
@JsonInclude(Include.NON_NULL)
public class Physician extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de base de datos", order = 1)
    private Integer id;
    @ApiObjectField(name = "identification", description = "Número de Identificación", order = 2)
    private String identification;
    @ApiObjectField(name = "name", description = "Nombre ", order = 3)
    private String name;
    @ApiObjectField(name = "lastName", description = "Apellido ", order = 4)
    private String lastName;
    @ApiObjectField(name = "phone", description = "Telefono", order = 5)
    private String phone;
    @ApiObjectField(name = "fax", description = "Fax", order = 6)
    private String fax;
    @ApiObjectField(name = "address1", description = "Dirección 1", order = 7)
    private String address1;
    @ApiObjectField(name = "address2", description = "Dirección 2", order = 8)
    private String address2;
    @ApiObjectField(name = "city", description = "Ciudad", order = 9)
    private String city;
    @ApiObjectField(name = "zipCode", description = "Código postal", order = 10)
    private String zipCode;
    @ApiObjectField(name = "obs", description = "Observación", order = 11)
    private String obs;
    @ApiObjectField(name = "mmis", description = "MMIS", order = 12)
    private String mmis;
    @ApiObjectField(name = "license", description = "Número de licencia", order = 13)
    private String license;
    @ApiObjectField(name = "npi", description = "NPI", order = 14)
    private String npi;
    @ApiObjectField(name = "institutional", description = "Identifica si es institucional", order = 15)
    private boolean institutional;
    @ApiObjectField(name = "additionalReport", description = "Indica si genera una copia de reporte resultados para el médico ", order = 16)
    private boolean additionalReport;
    @ApiObjectField(name = "userName", description = "Usuario de outreach", order = 17)
    private String userName;
    @ApiObjectField(name = "password", description = "Contraseña para outreach", order = 18)
    private String password;
    @ApiObjectField(name = "providerId", description = "Id entidad proveedora de salud ", order = 19)
    private String providerId;
    @ApiObjectField(name = "specialty", description = "Especialidad del médico ", order = 20)
    private Specialist specialty;
    @ApiObjectField(name = "email", description = "Correo del médico", order = 21)
    private String email;
    @ApiObjectField(name = "state", description = "Indica si se  encuentra activo", order = 22)
    private boolean active;
    @ApiObjectField(name = "code", description = "Codigo ", order = 23)
    private String code;
    @ApiObjectField(name = "state", description = "Estado /Departamento ", order = 24)
    private String state;
    @ApiObjectField(name = "alternativeMails", description = "Correos alternativos", order = 25)
    private String alternativeMails;
    @ApiObjectField(name = "idDemoAux", description = "Id del demografico auxiliar", order = 1)
    private Integer idDemoAux;

    public Physician(Integer id)
    {
        this.id = id;
    }

    public Physician()
    {
        specialty = new Specialist();

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

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getIdentification()
    {
        return identification;
    }

    public void setIdentification(String identification)
    {
        this.identification = identification;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs(String obs)
    {
        this.obs = obs;
    }

    public String getMmis()
    {
        return mmis;
    }

    public void setMmis(String mmis)
    {
        this.mmis = mmis;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public String getNpi()
    {
        return npi;
    }

    public void setNpi(String npi)
    {
        this.npi = npi;
    }

    public boolean isInstitutional()
    {
        return institutional;
    }

    public void setInstitutional(boolean institutional)
    {
        this.institutional = institutional;
    }

    public boolean isAdditionalReport()
    {
        return additionalReport;
    }

    public void setAdditionalReport(boolean additionalReport)
    {
        this.additionalReport = additionalReport;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getProviderId()
    {
        return providerId;
    }

    public void setProviderId(String providerId)
    {
        this.providerId = providerId;
    }

    public Specialist getSpecialty()
    {
        return specialty;
    }

    public void setSpecialty(Specialist specialty)
    {
        this.specialty = specialty;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public Integer getIdDemoAux()
    {
        return idDemoAux;
    }

    public void setIdDemoAux(Integer idDemoAux)
    {
        this.idDemoAux = idDemoAux;
    }

    @Override
    public String toString()
    {
        return "Physician{" + "id=" + id + ", identification=" + identification + ", name=" + name + ", lastName=" + lastName + ", phone=" + phone + ", fax=" + fax + ", address1=" + address1 + ", address2=" + address2 + ", city=" + city + ", zipCode=" + zipCode + ", obs=" + obs + ", mmis=" + mmis + ", license=" + license + ", npi=" + npi + ", institutional=" + institutional + ", additionalReport=" + additionalReport + ", userName=" + userName + ", password=" + password + ", providerId=" + providerId + ", specialty=" + specialty + ", email=" + email + ", state=" + active + '}';
    }

    public String getAlternativeMails()
    {
        return alternativeMails;
    }

    public void setAlternativeMails(String alternativeMails)
    {
        this.alternativeMails = alternativeMails;
    }
}
