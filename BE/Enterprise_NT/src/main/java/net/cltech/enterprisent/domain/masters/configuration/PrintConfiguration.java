package net.cltech.enterprisent.domain.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto para realizar la configuracion del aplicativo de
 * impresion
 *
 * @version 1.0.0
 * @author equijano
 * @since 13/05/2019
 * @see Creacion
 */
@ApiObject(
        group = "Configuracion",
        name = "Configuracion para imprimir",
        description = "Objeto para establecer la configuración para imprimir"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrintConfiguration
{

    @ApiObjectField(name = "serial", description = "Serial de la maquina", order = 1)
    private String serial;
    @ApiObjectField(name = "urlNode", description = "Url del NodeJs", order = 2)
    private String urlNode;
    @ApiObjectField(name = "urlWS", description = "Url del websocket", order = 3)
    private String urlWS;
    @ApiObjectField(name = "email", description = "Correo del sistema", order = 4)
    private String email;
    @ApiObjectField(name = "host", description = "Servidor de envio de correo", order = 5)
    private String host;
    @ApiObjectField(name = "password", description = "Contraseña del correo de sistema", order = 6)
    private String password;
    @ApiObjectField(name = "port", description = "Puerto para envio de correo", order = 7)
    private String port;
    @ApiObjectField(name = "ssl", description = "Tipo de certificado", order = 8)
    private String ssl;
    @ApiObjectField(name = "serverEmail", description = "Servidor de correo", order = 8)
    private String serverEmail;

    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public String getUrlNode()
    {
        return urlNode;
    }

    public void setUrlNode(String urlNode)
    {
        this.urlNode = urlNode;
    }

    public String getUrlWS()
    {
        return urlWS;
    }

    public void setUrlWS(String urlWS)
    {
        this.urlWS = urlWS;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getSsl()
    {
        return ssl;
    }

    public void setSsl(String ssl)
    {
        this.ssl = ssl;
    }
    
    public String getServerEmail()
    {
        return serverEmail;
    }

    public void setServerEmail(String serverEmail)
    {
        this.serverEmail = serverEmail;
    }
}
