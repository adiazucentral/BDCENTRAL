package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de resultado literal
 *
 * @author eacuna
 * @version 1.0.0
 * @since 22/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Resultado Literal",
        description = "Representa un resultado literal"
)
public class LiteralResult extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de base de datos", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre ", order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Id del estado", order = 3)
    private boolean state;
    @ApiObjectField(name = "testId", description = "Identificador del examen que tiene asignado el resultado literal", order = 4)
    private int testId;
    @ApiObjectField(name = "test", description = "Informacion de la prueb", required = false, order = 5)
    private Test test;
    @ApiObjectField(name = "nameEnglish", description = "Nombre en ingles ", order = 6)
    private String nameEnglish;

    public LiteralResult()
    {
    }

    public LiteralResult(Integer id, String name)
    {
        this.id = id;
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

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    @Override
    public String toString()
    {
        return "LiteralResult{" + "id=" + id + ", name=" + name + ", state=" + state + '}';
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }
}
