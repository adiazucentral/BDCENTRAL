package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Laboratorio de referencia
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/04/2017
 * @see Creacion
 */
@ApiObject(
        group = "Prueba",
        name = "Laboratorio",
        description = "Representa un laboratorio de interno o de referencia"
)
public class Laboratory extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código", required = true, order = 2)
    private Integer code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "address", description = "Direccion", required = false, order = 4)
    private String address;
    @ApiObjectField(name = "phone", description = "Telefono", required = false, order = 5)
    private String phone;
    @ApiObjectField(name = "contact", description = "Contacto", required = false, order = 6)
    private String contact;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 7)
    private boolean state;
    @ApiObjectField(name = "type", description = "Tipo de laboratorio:<br>1-Interno<br>2-Externo", required = true, order = 8)
    private short type;
    @ApiObjectField(name = "path", description = "Ruta exportación archivos", required = false, order = 9)
    private String path;
    @ApiObjectField(name = "url", description = "Url del laboratorio", required = false, order = 10)
    private String url;
    @ApiObjectField(name = "entry", description = "Si es de ingreso", required = false, order = 11)
    private boolean entry;
    @ApiObjectField(name = "check", description = "Si es de verificacion", required = false, order = 12)
    private boolean check;
    @ApiObjectField(name = "middleware", description = "Si envía a Middleware externo:<br>1 - Si<br>2 - No", required = false, order = 13)
    private boolean middleware;
    @ApiObjectField(name = "winery", description = "Bodega", required = false, order = 14)
    private Integer winery;

    public Laboratory()
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

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
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

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public short getType()
    {
        return type;
    }

    public void setType(short type)
    {
        this.type = type;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public boolean isEntry()
    {
        return entry;
    }

    public void setEntry(boolean entry)
    {
        this.entry = entry;
    }

    public boolean isCheck()
    {
        return check;
    }

    public void setCheck(boolean check)
    {
        this.check = check;
    }

    public boolean isMiddleware()
    {
        return middleware;
    }

    public void setMiddleware(boolean middleware)
    {
        this.middleware = middleware;
    }

    @Override
    public String toString()
    {
        return "Laboratory{" + "id=" + id + ", code=" + code + ", name=" + name + ", address=" + address + ", phone=" + phone + ", contact=" + contact + ", state=" + state + ", type=" + type + ", path=" + path + '}';
    }

    public Integer getWinery()
    {
        return winery;
    }

    public void setWinery(Integer winery)
    {
        this.winery = winery;
    }
}
