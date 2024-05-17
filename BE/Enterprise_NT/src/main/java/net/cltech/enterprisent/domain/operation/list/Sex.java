package net.cltech.enterprisent.domain.operation.list;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un tipo de documento
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 17/02/2020
 * @see Creación
 */
@ApiObject(
        group = "LIS",
        name = "Sexo",
        description = "Datos de un sexo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sex
{

    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 1)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "nameEn", description = "Nombre en ingles", required = true, order = 3)
    private String nameEn;


    public Sex(Sex sex)
    {

        this.code = sex.getCode();
        this.name = sex.getName();
        this.nameEn = sex.getNameEn();

    }

    public Sex()
    {
        
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
