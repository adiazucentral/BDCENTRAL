package net.cltech.enterprisent.domain.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Areas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Demografico",
        description = "Muestra informacion del maestro Demografico que usa el API"
)
@Getter
@Setter

public class Demographic extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del demografico", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del demografico", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "origin", description = "Origen", required = true, order = 3)
    private String origin;
    @ApiObjectField(name = "encoded", description = "Tipo del demografico", required = true, order = 4)
    private boolean encoded;
    @ApiObjectField(name = "obligatory", description = "Obligatorio", required = true, order = 5)
    private Short obligatory;
    @ApiObjectField(name = "ordering", description = "Ordenamiento", required = true, order = 6)
    private Short ordering;
    @ApiObjectField(name = "format", description = "Formato", required = true, order = 7)
    private String format;
    @ApiObjectField(name = "defaultValue", description = "Valor por defecto", required = true, order = 8)
    private String defaultValue;
    @ApiObjectField(name = "statistics", description = "Para estadisticas", required = true, order = 9)
    private boolean statistics;
    @ApiObjectField(name = "lastOrder", description = "Ultima Orden", required = true, order = 10)
    private boolean lastOrder;
    @ApiObjectField(name = "canCreateItemInOrder", description = "Ingreso de items en orden", required = true, order = 11)
    private boolean canCreateItemInOrder;
    @ApiObjectField(name = "modify", description = "Modificar en ingreso y verificación", required = true, order = 12)
    private boolean modify;
    @ApiObjectField(name = "state", description = "Estado del demografico", required = true, order = 13)
    private boolean state;
    @ApiObjectField(name = "demographicItem", description = "Demografico Item", required = true, order = 14)
    private Integer demographicItem;
    @ApiObjectField(name = "items", description = "Items del demografico", required = true, order = 15)
    private List<DemographicItem> items = new ArrayList();
    @ApiObjectField(name = "placeholder", description = "Formato en que se muestra el campo", required = true, order = 16)
    private String placeholder;
    @ApiObjectField(name = "demographicItemName", description = "Nombre del Demografico Item", required = false, order = 17)
    private String demographicItemName;
    @ApiObjectField(name = "defaultValueRequired", description = "Valor por defecto Requerido", required = true, order = 18)
    private String defaultValueRequired;
    @ApiObjectField(name = "item", description = "Id del item para demograficos codificados", required = false, order = 19)
    private Integer item;
    @ApiObjectField(name = "value", description = "Valor del demografico para demografico abierto", required = false, order = 20)
    private String value;
    @ApiObjectField(name = "code", description = "Código del item", required = false, order = 21)
    private String code;
    @ApiObjectField(name = "source", description = "Origen del demografico: H-Historia, O-Orden", required = false, order = 22)
    private String source;
    @ApiObjectField(name = "type", description = "Tipo de demografico: 0-No Codificado, 1-Codificado", required = false, order = 23)
    private Integer type;
    @ApiObjectField(name = "coded", description = "Codificado: False - No Codificado, True - Codificado", required = false, order = 24)
    private boolean coded;
    @ApiObjectField(name = "orderingDemo", description = "Ordenamiento demografico", required = true, order = 6)
    private Integer orderingDemo;
    @ApiObjectField(name = "promiseTime", description = "Hora prometida", required = true, order = 25)
    private boolean promiseTime;

    public Demographic(Integer id, String name, boolean encoded)
    {
        this.id = id;
        this.name = name;
        this.encoded = encoded;
    }

    public Demographic(Integer id, String name, boolean encoded, String code)
    {
        this.id = id;
        this.name = name;
        this.encoded = encoded;
        this.code = code;
    }

    public Demographic(Integer id)
    {
        this.id = id;
    }

    public Demographic()
    {
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
        final Demographic other = (Demographic) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
