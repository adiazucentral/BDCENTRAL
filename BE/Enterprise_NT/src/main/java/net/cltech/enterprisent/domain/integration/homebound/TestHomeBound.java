package net.cltech.enterprisent.domain.integration.homebound;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.test.Sample;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion basica del maestro Pruebas
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 17/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Pruebas",
        name = "Prueba Home Bound",
        description = "Muestra informacion basica del maestro Pruebas que usa el API"
)
public class TestHomeBound
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "info", description = "Información", required = true, order = 5)
    private String info;
    @ApiObjectField(name = "sample", description = "Muestra", required = true, order = 6)
    private Sample sample;    
    @ApiObjectField(name = "requirement", description = "Requerimientos de la prueba", required = true, order = 42)
    private List<Requirement> requirement;    
    @ApiObjectField(name = "active", description = "Indica si esta activo", required = true, order = 8)
    private boolean active;
    @ApiObjectField(name = "type", description = "Tipo de Examen", required = true, order = 9)
    private Integer type;
    @ApiObjectField(name = "viewInOrder", description = "Ver en orden", required = true, order = 10)
    private boolean viewInOrder;
    @ApiObjectField(name = "sex", description = "Sexo", required = true, order = 11)
    private Item sex;
    @ApiObjectField(name = "area", description = "Area", required = true, order = 12)
    private Area area;
    @ApiObjectField(name = "ageUnit", description = "Unidad de edad para la prueba 0 -> Dias, 1 -> Años", required = true, order = 13)
    private Integer ageUnit;
    @ApiObjectField(name = "ageMin", description = "Edad minima para la prueba", required = true, order = 14)
    private Integer ageMin;
    @ApiObjectField(name = "ageMax", description = "Edad maxima para la prueba", required = true, order = 15)
    private Integer ageMax;
    @ApiObjectField(name = "parentId", description = "Id del perfil padre homebound", required = true, order = 16)
    private Integer parentId;
    @ApiObjectField(name = "parent", description = "Id del perfil padre toma de muestra", required = true, order = 17)
    private Integer parent;

    public TestHomeBound()
    {
        sample = new Sample();
        sex = new Item();
        area = new Area();
        requirement = new ArrayList<>();

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

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

    public List<Requirement> getRequirements()
    {
        return requirement;
    }

    public void setRequirements(List<Requirement> requirements)
    {
        this.requirement = requirements;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public boolean isViewInOrder()
    {
        return viewInOrder;
    }

    public void setViewInOrder(boolean viewInOrder)
    {
        this.viewInOrder = viewInOrder;
    }

    public Item getSex()
    {
        return sex;
    }

    public void setSex(Item sex)
    {
        this.sex = sex;
    }

    public Area getArea()
    {
        return area;
    }

    public void setArea(Area area)
    {
        this.area = area;
    }

    public Integer getAgeUnit()
    {
        return ageUnit;
    }

    public void setAgeUnit(Integer ageUnit)
    {
        this.ageUnit = ageUnit;
    }

    public Integer getAgeMin()
    {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin)
    {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax()
    {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax)
    {
        this.ageMax = ageMax;
    }

    public Integer getParentId()
    {
        return parentId;
    }

    public void setParentId(Integer parentId)
    {
        this.parentId = parentId;
    }

    public Integer getParent()
    {
        return parent;
    }

    public void setParent(Integer parent)
    {
        this.parent = parent;
    }

}
