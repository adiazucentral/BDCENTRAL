package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un usuario del dashboard
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 13/08/2020
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Usuario Dashboard",
        description = "Usuario para dashboard"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDashboard
{

    @ApiObjectField(name = "lastName", description = "Apellido del usuario", required = true, order = 1)
    private String lastName;
    @ApiObjectField(name = "password", description = "Password del usuario", required = true, order = 2)
    private String password;
    @ApiObjectField(name = "passwordExpiration", description = "fecha de expiracion de la clave", required = true, order = 3)
    private Timestamp passwordExpiration;
    @ApiObjectField(name = "success", description = "Proceso completado", required = true, order = 4)
    private boolean success;
    @ApiObjectField(name = "valorIv", description = "Valor del IV", required = true, order = 5)
    private Integer valorIv;
    @ApiObjectField(name = "valorSalr", description = "Valor del salario", required = true, order = 6)
    private Integer valorSalr;
    @ApiObjectField(name = "name", description = "Nombre del usuario", required = true, order = 7)
    private String name;
    @ApiObjectField(name = "id", description = "Id del usuario", required = true, order = 8)
    private Integer id;
    @ApiObjectField(name = "key", description = "Llave", required = true, order = 9)
    private boolean key;
    @ApiObjectField(name = "user", description = "Usuario", required = true, order = 10)
    private String user;
    @ApiObjectField(name = "accessDirect", description = "Acceso directo", required = true, order = 11)
    private boolean accessDirect;
    @ApiObjectField(name = "administrator", description = "Administrador", required = true, order = 12)
    private boolean administrator;

    public UserDashboard()
    {
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Timestamp getPasswordExpiration()
    {
        return passwordExpiration;
    }

    public void setPasswordExpiration(Timestamp passwordExpiration)
    {
        this.passwordExpiration = passwordExpiration;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public Integer getValorIv()
    {
        return valorIv;
    }

    public void setValorIv(Integer valorIv)
    {
        this.valorIv = valorIv;
    }

    public Integer getValorSalr()
    {
        return valorSalr;
    }

    public void setValorSalr(Integer valorSalr)
    {
        this.valorSalr = valorSalr;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public boolean isKey()
    {
        return key;
    }

    public void setKey(boolean key)
    {
        this.key = key;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public boolean isAccessDirect()
    {
        return accessDirect;
    }

    public void setAccessDirect(boolean accessDirect)
    {
        this.accessDirect = accessDirect;
    }

    public boolean isAdministrator()
    {
        return administrator;
    }

    public void setAdministrator(boolean administrator)
    {
        this.administrator = administrator;
    }

}
