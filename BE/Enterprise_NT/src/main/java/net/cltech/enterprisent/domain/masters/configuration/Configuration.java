package net.cltech.enterprisent.domain.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class Configuration
{

    @ApiObjectField(name = "key", description = "Llave de configuracion", required = true, order = 1)
    private String key;
    @ApiObjectField(name = "value", description = "Valor de configuracion", required = true, order = 2)
    private String value;
    @ApiObjectField(name = "name", description = "Nombre descriptivo de la llave", required = false, order = 3)
    private String name;

    public Configuration(String key)
    {
        this.key = key;
    }

    public Configuration(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Configuration other = (Configuration) obj;
        if (!Objects.equals(this.key, other.key))
        {
            return false;
        }
        return true;
    }

    /**
     * Nombre del laboratorio
     */
    public static final String KEY_ENTITY = "Entidad";
    /**
     * Abreviatura del laboratorio
     */
    public static final String KEY_ABVR = "Abreviatura";
    public static final String KEY_PHONE_FORMAT = "FormatoTelefono";

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

    public static final String KEY_RACK_PENDING = "GradillaPendiente";
    public static final String KEY_RACK_CONFIDENTIAL = "GradillaConfidenciales";
    public static final String KEY_STORE_DESTINATION = "DestinoVerificaCentralMuestras";
    public static final String KEY_DISPOSAL_DESTINATION = "DestinoVerificaDesecho";
}
