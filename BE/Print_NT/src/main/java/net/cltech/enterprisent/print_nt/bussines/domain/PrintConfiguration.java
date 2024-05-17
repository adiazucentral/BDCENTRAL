package net.cltech.enterprisent.print_nt.bussines.domain;

/**
 * Representa un objeto para realizar la configuracion del aplicativo de
 * impresion
 *
 * @version 1.0.0
 * @author equijano
 * @since 13/05/2019
 * @see Creacion
 */
public class PrintConfiguration
{

    private String serial;
    private String urlNode;
    private String urlWS;
    private String email;
    private String host;
    private String password;
    private String port;
    private String ssl;
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
