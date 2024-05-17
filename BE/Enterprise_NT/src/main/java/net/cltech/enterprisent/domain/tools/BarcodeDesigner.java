package net.cltech.enterprisent.domain.tools;

import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Diseñador de códigos de barras
 *
 * @version 1.0.0
 * @author eacuna
 * @since 10/12/2018
 * @see Creacion
 */
@ApiObject(
        group = "Herramientas",
        name = "Diseñador Barras",
        description = "Representa informacion del diseñador de códigos de barras"
)
public class BarcodeDesigner
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "template", description = "Plantilla del diseñador", required = true, order = 2)
    private String template;
    @ApiObjectField(name = "command", description = "Commando epl/zpl para la impresión del código de barras", required = true, order = 3)
    private String command;
    @ApiObjectField(name = "type", description = "Tipo código de barras General = 1, Etiqueta Adicional = 2", required = true, order = 4)
    private Integer type;
    @ApiObjectField(name = "active", description = "Indica si el registro se encuentra activo", required = true, order = 5)
    private boolean active;
    @ApiObjectField(name = "version", description = "Version de la etiqueta para el codigo de barras", required = true, order = 6)
    private Integer version;
    @ApiObjectField(name = "orderType", description = "Tipo de orden", required = true, order = 7)
    private String orderType;
        
    public BarcodeDesigner()
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

    public String getTemplate()
    {
        return template;
    }

    public void setTemplate(String template)
    {
        this.template = template;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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
        final BarcodeDesigner other = (BarcodeDesigner) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    public static final Integer SAMPLE = 1;
    public static final Integer ADDITIONAL = 2;

}
