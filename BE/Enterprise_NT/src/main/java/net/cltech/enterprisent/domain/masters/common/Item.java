package net.cltech.enterprisent.domain.masters.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa las listas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 17/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Utilidades",
        name = "Listas",
        description = "Muestra las listas que usa el API"
)
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Item
{
    @ApiObjectField(name = "id", description = "Id del item", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "idParent", description = "Id del item padre", required = true, order = 2)
    private Integer idParent;
    @ApiObjectField(name = "code", description = "Codigo del item", required = true, order = 3)
    private String code;
    @ApiObjectField(name = "esCo", description = "Español-Colombia", required = true, order = 4)
    private String esCo;
    @ApiObjectField(name = "enUsa", description = "Ingles-Estados Unidos", required = true, order = 5)
    private String enUsa;
    @ApiObjectField(name = "additional", description = "Información adicional", required = false, order = 6)
    private String additional;

    public Item()
    {
    }
    
    public Item(Integer id)
    {
        this.id = id;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Item other = (Item) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
