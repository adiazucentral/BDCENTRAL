package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un demografico para tableros.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/07/2018
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Demograficos",
        description = "Representa un demografico para tableros."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashBoardCommon
{
    @ApiObjectField(name = "id", description = "Id del demografico", order = 1)
    private Integer id;
    @ApiObjectField(name = "codeDemographic", description = "Codigo del demografico", order = 2)
    private String code;
    @ApiObjectField(name = "nameDemographic", description = "Nombre del demografico", order = 3)
    private String name;

    public DashBoardCommon()
    {
    }

    public DashBoardCommon(Integer id, String code, String name)
    {
        this.id = id;
        this.code = code;
        this.name = name;
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
    
}
