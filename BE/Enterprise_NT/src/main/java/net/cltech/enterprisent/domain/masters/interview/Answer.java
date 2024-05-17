package net.cltech.enterprisent.domain.masters.interview;

import java.sql.Timestamp;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Respuesta
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Entrevista",
        name = "Respuesta",
        description = "Muestra informacion del maestro Respuesta que usa el API"
)
public class Answer extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id de la respuesta", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la respuesta", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Estado de la respuesta", required = true, order = 3)
    private boolean state;
    @ApiObjectField(name = "selected", description = "Indica si la respuesta esta seleccionada en la pregunta", required = true, order = 4)
    private boolean selected;
    @ApiObjectField(name = "quantity", description = "Cantidad de preguntas en las que se encuentra la respuesta", required = true, order = 5)
    private Integer quantity;
    @ApiObjectField(name = "lastModificationDate", description = "Ultima fecha de modificación", required = false, order = 6)
    private Timestamp lastModificationDate;
    @ApiObjectField(name = "lastUserModify", description = "Ultimo usuario en modificar la pregunta", required = false, order = 7)
    private int lastUserModify;
    @ApiObjectField(name = "control", description = "Control de la Pregunta: <br> <b>Abierta:</b> <br> 1 -> Texto. <br> 2 -> Numerico. <br> 3 -> Comentario. <br> 4 -> Fecha. <br> <b>Cerrada:</b> <br> 5 -> Combobox <br> 6 -> Combobox Multiple.", required = true, order = 8)
    private Short control;
    @ApiObjectField(name = "isOpen", description = "Tipo respuesta", required = true, order = 9)
    private boolean isOpen;
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }    

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Timestamp getLastModificationDate()
    {
        return lastModificationDate;
    }

    public void setLastModificationDate(Timestamp lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

    public int getLastUserModify()
    {
        return lastUserModify;
    }

    public void setLastUserModify(int lastUserModify)
    {
        this.lastUserModify = lastUserModify;
    }

    public Short getControl()
    {
        return control;
    }

    public void setControl(Short control)
    {
        this.control = control;
    }

    public boolean isIsOpen()
    {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen)
    {
        this.isOpen = isOpen;
    }
}
