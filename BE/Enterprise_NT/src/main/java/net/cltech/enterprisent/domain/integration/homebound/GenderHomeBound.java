package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto requerido por HomeBound
 * 
 * @version 1.0.0
 * @author Julian
 * @since 2020/06/08
 * @see Creación
 */

@ApiObject(
        group = "HomeBound",
        name = "Cliente",
        description = "Datos de un cliente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenderHomeBound
{
    @ApiObjectField(name = "id", description = "Identificador de la base de datos", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 1)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "nameEn", description = "Nombre en ingles", required = true, order = 3)
    private String nameEn;

    public GenderHomeBound()
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }
}
