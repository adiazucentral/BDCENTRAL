package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Grupos poblacionales
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/04/2017
 * @see Creacion
 */
@ApiObject(
        group = "Prueba",
        name = "Grupo Poblacional",
        description = "Representa un grupo poblacional"
)
public class PopulationGroup extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "sex", description = "Genero ", required = true, format = "0-Ambos</br>1-Masculino</br>2-Femenino", order = 3)
    private Integer sex;
    @ApiObjectField(name = "unit", description = "Unidad de tiempo ", format = "1-Dias</br>2-Años", required = true, order = 4)
    private Integer unit;
    @ApiObjectField(name = "initialRange", description = "Rango inical de la unidad", required = true, order = 5)
    private Integer initialRange;
    @ApiObjectField(name = "finalRange", description = "Rango final de la unidad", required = true, order = 6)
    private Integer finalRange;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 7)
    private boolean state;

    public PopulationGroup()
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getSex()
    {
        return sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Integer getUnit()
    {
        return unit;
    }

    public void setUnit(Integer unit)
    {
        this.unit = unit;
    }

    public Integer getInitialRange()
    {
        return initialRange;
    }

    public void setInitialRange(Integer initialRange)
    {
        this.initialRange = initialRange;
    }

    public Integer getFinalRange()
    {
        return finalRange;
    }

    public void setFinalRange(Integer finalRange)
    {
        this.finalRange = finalRange;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "PopulationGroup{" + "id=" + id + ", name=" + name + ", sex=" + sex + ", unit=" + unit + ", initialRange=" + initialRange + ", finalRange=" + finalRange + ", state=" + state + '}';
    }

}
