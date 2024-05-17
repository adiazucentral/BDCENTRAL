/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.masters.configuration;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto de configuracion general
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creacion
 */
@ApiObject(
        group = "Configuracion",
        name = "Configuracion",
        description = "Representa una llave de configuracion general"
)
public class Configuration
{

    @ApiObjectField(name = "key", description = "Llave de configuracion", order = 1)
    private String key;
    @ApiObjectField(name = "value", description = "Valor de configuracion", order = 2)
    private String value;
    @ApiObjectField(name = "origin", description = "Origen -> 1 Consulta Web, 2-> LIS", order = 3)
    private Integer origin;
    
    public Configuration()
    {
    }

    public Configuration(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public static final String KEY_BILLING = "Facturacion";
    /**
     * Plano whonet <br>
     * Id antibiotico THM(Test de Hodge Modificado)
     */
    public static final String KEY_WHONET_THM = "WhonetTHM";
    /**
     * Plano whonet <br>
     * Id antibiotico EDTA(Prueba confirmatoria)
     */
    public static final String KEY_WHONET_EDTA = "WhonetEDTA";
    /**
     * Plano whonet <br>
     * Id antibiotico APB(Acido Boronico)
     */
    public static final String KEY_WHONET_APB = "WhonetAPB";
    /**
     * Plano whonet <br>
     * Establece que código se muestra en el plano whonet(spec_type)<br>
     * 1 - Sitio anatomico <br>
     * 2 - Submuestra
     *
     */
    public static final String KEY_WHONET_TYPE = "WhonetTipo";

    public static final String WHONET_TYPE_ANATOMICAL = "1";
    public static final String WHONET_TYPE_SAMPLE = "2";

    public static final String BILLING_NONE = "0";
    public static final String BILLING_GENERAL = "1";
    public static final String BILLING_USA = "2";
}
