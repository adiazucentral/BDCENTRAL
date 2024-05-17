package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un area para el Dashboard
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 14/08/2020
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Area Dashboard",
        description = "Area para dashboard"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AreaDashboard
{

    @ApiObjectField(name = "id", description = "Id del area", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código del area", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura del area", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre del area", required = true, order = 4)
    private String name;

    public AreaDashboard()
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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
