/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Hojas de trabajo
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Hoja de Trabajo",
        description = "Muestra informacion del maestro Hojas de Trabajo que usa el API"
)
public class Worksheet extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id de la hoja de trabajo", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la hoja de trabajo", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "type", description = "Tipo: 1 -> Hoja de trabajo, 2 -> Listado", required = true, order = 3)
    private Short type;
    @ApiObjectField(name = "orientation", description = "Orientacion: 1 -> Horizontal, 2 -> Vertical", required = true, order = 4)
    private Short orientation;
    @ApiObjectField(name = "exclusive", description = "Excluyente", required = true, order = 5)
    private boolean exclusive;
    @ApiObjectField(name = "microbiology", description = "Microbiologia", required = true, order = 6)
    private boolean microbiology;
    @ApiObjectField(name = "state", description = "Estado", required = true, order = 7)
    private boolean state;
    @ApiObjectField(name = "tests", description = "Pruebas", required = true, order = 8)
    private List<TestBasic> tests;
    
    public Worksheet()
    {
        tests = new ArrayList<>();
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

    public Short getType()
    {
        return type;
    }

    public void setType(Short type)
    {
        this.type = type;
    }

    public Short getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Short orientation)
    {
        this.orientation = orientation;
    }

    public boolean isExclusive()
    {
        return exclusive;
    }

    public void setExclusive(boolean exclusive)
    {
        this.exclusive = exclusive;
    }

    public boolean isMicrobiology()
    {
        return microbiology;
    }

    public void setMicrobiology(boolean microbiology)
    {
        this.microbiology = microbiology;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public List<TestBasic> getTests()
    {
        return tests;
    }

    public void setTests(List<TestBasic> tests)
    {
        this.tests = tests;
    }
        
}
