package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Sistema Central
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Sistema Central",
        description = "Muestra informacion del maestro Sistema Central que usa el API"
)
public class CentralSystem extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del sistema central", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del sistema central", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "ehr", description = "EHR/Identifica códigos cups", required = true, order = 3)
    private boolean ehr;
    @ApiObjectField(name = "repeatCode", description = "Permite códigos centrales repetidos en homologación", required = true, order = 4)
    private boolean repeatCode;
    @ApiObjectField(name = "state", description = "Estado del sistema central", required = true, order = 5)
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

    public boolean isEHR()
    {
        return ehr;
    }

    public void setEHR(boolean EHR)
    {
        this.ehr = EHR;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isRepeatCode()
    {
        return repeatCode;
    }

    public void setRepeatCode(boolean repeatCode)
    {
        this.repeatCode = repeatCode;
    }

}
