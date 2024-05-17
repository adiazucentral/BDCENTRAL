package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un examen para el Dashboard
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 14/08/2020
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Examen Dashboard",
        description = "Examen para dashboard"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestDashboard
{

    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "minAge", description = "Edad minima", required = true, order = 5)
    private Integer minAge;
    @ApiObjectField(name = "maxAge", description = "Edad maxima", required = true, order = 6)
    private Integer maxAge;
    @ApiObjectField(name = "ageUnit", description = "Unidad de edad del valor de referencia", required = true, order = 7)
    private Integer ageUnit;
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = true, order = 8)
    private Short testType;
    @ApiObjectField(name = "area", description = "Area de la prueba", required = true, order = 9)
    private AreaDashboard area;
    @ApiObjectField(name = "active", description = "Estado", required = true, order = 10)
    private boolean active;
    @ApiObjectField(name = "viewInOrder", description = "0->Ver en ingreso de ordenes, 1->No ver en ingreso de ordenes", required = false, order = 11)
    private boolean viewInOrder;

    public TestDashboard()
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

    public Integer getMinAge()
    {
        return minAge;
    }

    public void setMinAge(Integer minAge)
    {
        this.minAge = minAge;
    }

    public Integer getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge)
    {
        this.maxAge = maxAge;
    }

    public Integer getAgeUnit()
    {
        return ageUnit;
    }

    public void setAgeUnit(Integer ageUnit)
    {
        this.ageUnit = ageUnit;
    }

    public Short getTestType()
    {
        return testType;
    }

    public void setTestType(Short testType)
    {
        this.testType = testType;
    }

    public AreaDashboard getArea()
    {
        return area;
    }

    public void setArea(AreaDashboard area)
    {
        this.area = area;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isViewInOrder()
    {
        return viewInOrder;
    }

    public void setViewInOrder(boolean viewInOrder)
    {
        this.viewInOrder = viewInOrder;
    }

}
