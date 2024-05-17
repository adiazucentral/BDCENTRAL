package net.cltech.enterprisent.print_nt.bussines.domain;

/**
 * Representa un objeto para generar la configuracion del aplicativo de
 * impresion
 *
 * @version 1.0.0
 * @author equijano
 * @since 13/05/2019
 * @see Creacion
 */
public class GeneratePrintConfiguration
{

    private Boolean serial;
    private String urlBackend;

    public Boolean getSerial()
    {
        return serial;
    }

    public void setSerial(Boolean serial)
    {
        this.serial = serial;
    }

    public String getUrlBackend()
    {
        return urlBackend;
    }

    public void setUrlBackend(String urlBackend)
    {
        this.urlBackend = urlBackend;
    }

}
