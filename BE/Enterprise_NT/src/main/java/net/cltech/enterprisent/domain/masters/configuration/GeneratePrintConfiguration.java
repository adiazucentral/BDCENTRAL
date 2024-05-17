package net.cltech.enterprisent.domain.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto para generar la configuracion del aplicativo de
 * impresion
 *
 * @version 1.0.0
 * @author equijano
 * @since 13/05/2019
 * @see Creacion
 */
@ApiObject(
        group = "Configuracion",
        name = "Para configurar la impresion",
        description = "Objeto para establecer la configuración para imprimir"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratePrintConfiguration
{

    @ApiObjectField(name = "serial", description = "Si se genera el serial", order = 1)
    private Boolean serial;
    @ApiObjectField(name = "urlBackend", description = "Url del Backend", order = 2)
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
