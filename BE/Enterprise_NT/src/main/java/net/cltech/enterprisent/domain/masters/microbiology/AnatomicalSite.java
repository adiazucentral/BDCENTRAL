package net.cltech.enterprisent.domain.masters.microbiology;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Sitio Anatomico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Microbiología",
        name = "Sitio Anatomico",
        description = "Muestra informacion del maestro Sitio Anatomico que usa el API"
)
public class AnatomicalSite extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id del sitio anatomico", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del sitio anatomico", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "abbr", description = "Abreviación del sitio anatomico", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "state", description = "Estado del sitio anatomico", required = true, order = 4)
    private boolean state;

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

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

}
