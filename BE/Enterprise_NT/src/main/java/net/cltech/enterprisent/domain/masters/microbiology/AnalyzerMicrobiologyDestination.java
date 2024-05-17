package net.cltech.enterprisent.domain.masters.microbiology;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Este objeto representa un analizador con destino en microbiologia
 * 
 * @version 1.0.0
 * @author Julian
 * @since 06/05/2020
 * @see Creación
 */

@ApiObject(
        group = "Microbiología",
        name = "Analizadores en destinos de microbiología",
        description = "Analizadores que corresponden al destino de microbiologia"
)
public class AnalyzerMicrobiologyDestination
{
    @ApiObjectField(name = "idMicrobiologyDestination", description = "Id del destino de microbiologia", required = true, order = 1)
    private Integer idMicrobiologyDestination;
    @ApiObjectField(name = "userId", description = "Id del usuario", required = true, order = 2)
    private Integer userId;
    @ApiObjectField(name = "userName", description = "Nombre de usuario", required = true, order = 3)
    private String userName;
    @ApiObjectField(name = "referenceLaboratory", description = "Laboratorio de referencia", required = true, order = 4)
    private Integer referenceLaboratory;
    @ApiObjectField(name = "nameReferenceLaboratory", description = "Nombre del laboratorio de referencia", required = true, order = 5)
    private String nameReferenceLaboratory;
    @ApiObjectField(name = "selected", description = "Relacion activa del usuaro analizador", required = true, order = 5)
    private Boolean selected;

    public AnalyzerMicrobiologyDestination()
    {
    }

    public Integer getIdMicrobiologyDestination()
    {
        return idMicrobiologyDestination;
    }

    public void setIdMicrobiologyDestination(Integer idMicrobiologyDestination)
    {
        this.idMicrobiologyDestination = idMicrobiologyDestination;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Integer getReferenceLaboratory()
    {
        return referenceLaboratory;
    }

    public void setReferenceLaboratory(Integer referenceLaboratory)
    {
        this.referenceLaboratory = referenceLaboratory;
    }

    public String getNameReferenceLaboratory()
    {
        return nameReferenceLaboratory;
    }

    public void setNameReferenceLaboratory(String nameReferenceLaboratory)
    {
        this.nameReferenceLaboratory = nameReferenceLaboratory;
    }
    
    public Boolean getSelected()
    {
        return selected;
    }

    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }
}
