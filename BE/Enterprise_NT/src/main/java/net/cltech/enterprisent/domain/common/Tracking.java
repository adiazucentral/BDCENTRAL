/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.common;

import java.util.Date;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa la auditoria de un usuario en maestros
 *
 * @version 1.0.0
 * @author enavas
 * @since 14/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Auditoria",
        name = "Auditoria",
        description = "Representa un objeto de auditoria"
)
@Document(collection = "Configuration_Tracking")
public class Tracking
{

    public static final String STATE_INSERT = "I";
    public static final String STATE_UPDATE = "U";
    public static final String STATE_DELETE = "D";

    @ApiObjectField(name = "id", description = "Id de la auditoria", order = 1)
    private Integer id;
    @ApiObjectField(name = "date", description = "Fecha en que se genero el cambio", order = 2)
    private Date date;
    @ApiObjectField(name = "userId", description = "Id del usuario que genero el cambio", order = 3)
    private int userId;
    @ApiObjectField(name = "user", description = "Usuario que genero el cambio", order = 4)
    private String user;
    @ApiObjectField(name = "userName", description = "Nombre usuario que genero el cambio", order = 5)
    private String userName;
    @ApiObjectField(name = "url", description = "Url que genero el cambio", order = 6)
    private String url;
    @ApiObjectField(name = "host", description = "Host que genero el cambio", order = 7)
    private String host;
    @ApiObjectField(name = "module", description = "Modulo donde genero el cambio", order = 8)
    private String module;
    @ApiObjectField(name = "state", description = "Estado: I=Insertado, U=Actualizado, D=Eliminado", order = 9)
    private String state;
    @ApiObjectField(name = "fields", description = "Listado de campos cambiados", order = 10)
    private List<TrackingDetail> fields;
    @ApiObjectField(name = "type", description = "Tipo de auditoria", order = 11)
    private String type;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public List<TrackingDetail> getFields()
    {
        return fields;
    }

    public void setFields(List<TrackingDetail> fields)
    {
        this.fields = fields;
    }

    public String getModule()
    {
        return module;
    }

    public void setModule(String module)
    {
        this.module = module;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

}
