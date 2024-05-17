package net.cltech.outreach.domain.demographic;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el demographic
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 27/01/2020
 * @see Creacion
 */
@ApiObject(
        group = "Demograficos",
        name = "Demograficos",
        description = "Representa una entidad del demografico"
)
public class Demographic
{

    @ApiObjectField(name = "id", description = "id del demografico", order = 1)
    private int id;
    @ApiObjectField(name = "code", description = "codigo del demografico", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "nombre del demografico", order = 3)
    private String name;
    @ApiObjectField(name = "type", description = "Tipo", order = 4)
    private String type;

    public Demographic(int id)
    {
        this.id = id;
    }

    public Demographic()
    {

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
