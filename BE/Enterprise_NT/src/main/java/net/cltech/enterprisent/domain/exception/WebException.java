package net.cltech.enterprisent.domain.exception;

import java.util.Date;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Administra un error generado por el servidor
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/03/2017
 * @see Creación
 */
@ApiObject(
        group = "Errores",
        name = "Error",
        description = "Representa un error en servidor"
)
@Document(collection = "Application_Errors")
public class WebException
{

    @ApiObjectField(name = "id", description = "Id unico del error", order = 0)
    private Integer id;
    @ApiObjectField(name = "date", description = "Fecha del error", order = 1)
    private Date date;
    @ApiObjectField(name = "code", description = "Codigo del error, tipos de error:</br>1- SQLException</br>2-EnterpriseNTException</br>0-Error no controlado", order = 2)
    private Integer code;
    @ApiObjectField(name = "message", description = "Mensaje del error", order = 3)
    private String message;
    @ApiObjectField(name = "host", description = "Equipo cliente donde se genera el error", order = 4)
    private String host;
    @ApiObjectField(name = "url", description = "Url que genera el error", order = 5)
    private String url;
    @ApiObjectField(name = "detail", description = "Detalle completo del error", order = 6)
    private String detail;
    @ApiObjectField(name = "idUser", description = "Id usuario que se le presento el error", order = 7)
    private Integer idUser;
    @ApiObjectField(name = "detail", description = "Usuario que se le presento el error", order = 8)
    private String user;
    @ApiObjectField(name = "errorfields", description = "Campos que contienen error para EnterpriseNTException, formato <b>cod|campo</b></br> 0- Campo vacio</br> 1- Campo Duplicado</br> 2- Campo no encontrado en BD"
            + "</br> 3 - Dato invalido </br> 4 - Usuario invalido </br> 5 -  Password incorrecto </br> 6 - Modificación no permitida", order = 9)
    private List<String> errorFields;
    @ApiObjectField(name = "type", description = "Tipo de error: 0 -> Back-End, 1 -> Front-End", order = 10)
    private Integer type;

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public List<String> getErrorFields()
    {
        return errorFields;
    }

    public void setErrorFields(List<String> errorFields)
    {
        this.errorFields = errorFields;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Integer getIdUser()
    {
        return idUser;
    }

    public void setIdUser(Integer idUser)
    {
        this.idUser = idUser;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

}
