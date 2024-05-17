package net.cltech.enterprisent.domain.masters.tracking;

import java.sql.Timestamp;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Destinos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 27/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Destino",
        description = "Muestra informacion del maestro Destinos que usa el API"
)
public class Destination extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "description", description = "Descripción", required = true, order = 4)
    private String description;
    @ApiObjectField(name = "type", description = "Tipo del destino", required = true, order = 5)
    private Item type;
    @ApiObjectField(name = "color", description = "Color", required = true, order = 6)
    private String color;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 7)
    private boolean state;
    @ApiObjectField(name = "verified", description = "Si se encuentra verificado, null si no esta", required = true, order = 8)
    private Boolean verified;
    @ApiObjectField(name = "checkAmount", description = "Cantidad de verificados", required = false, order = 9)
    private Integer checkAmount;
    @ApiObjectField(name = "uncheckAmount", description = "Cantidad de pendienteds por verificar", required = false, order = 10)
    private Integer uncheckAmount;
    @ApiObjectField(name = "lastModifyDate", description = "Ultima fecha de modificación", required = false, order = 11)
    private Timestamp lastModifyDate;
    @ApiObjectField(name = "lastUserModify", description = "Ultimo usuario en modificar", required = false, order = 12)
    private int lastUserModify;
    @ApiObjectField(name = "typeOfDestination", description = "tipo de destino", required = false, order = 13)
    private int typeOfDestination;
    
    public Destination(Integer id)
    {
        this.id = id;
    }

    public Destination()
    {
        type = new Item();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Item getType()
    {
        return type;
    }

    public void setType(Item type)
    {
        this.type = type;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Boolean getVerified()
    {
        return verified;
    }

    public void setVerified(Boolean verified)
    {
        this.verified = verified;
    }

    public Integer getCheckAmount()
    {
        return checkAmount;
    }

    public void setCheckAmount(Integer checkAmount)
    {
        this.checkAmount = checkAmount;
    }

    public Integer getUncheckAmount()
    {
        return uncheckAmount;
    }

    public void setUncheckAmount(Integer uncheckAmount)
    {
        this.uncheckAmount = uncheckAmount;
    }
    
    public Timestamp getLastModifyDate()
    {
        return lastModifyDate;
    }

    public void setLastModifyDate(Timestamp lastModifyDate)
    {
        this.lastModifyDate = lastModifyDate;
    }

    public int getLastUserModify()
    {
        return lastUserModify;
    }

    public void setLastUserModify(int lastUserModify)
    {
        this.lastUserModify = lastUserModify;
    }
    
    public int getTypeOfDestination()
    {
        return typeOfDestination;
    }

    public void setTypeOfDestination(int typeOfDestination)
    {
        this.typeOfDestination = typeOfDestination;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
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
        final Destination other = (Destination) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
