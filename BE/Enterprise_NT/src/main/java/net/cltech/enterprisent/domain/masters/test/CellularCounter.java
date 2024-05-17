/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Contador Hematologico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Contador Hematologico",
        description = "Muestra informacion del maestro Contador Hematologico que usa el API"
)
public class CellularCounter extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id del contador hematologico", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "key", description = "Nombre del contador hematologico", required = true, order = 2)
    private String key;
    @ApiObjectField(name = "text", description = "Nombre del contador hematologico", required = true, order = 3)
    private String text;
    @ApiObjectField(name = "type", description = "Tipo: 1 -> Numerico, 2 -> Texto", required = true, order = 4)
    private Short type;
    @ApiObjectField(name = "sum", description = "Suma", required = true, order = 5)
    private boolean sum;
    @ApiObjectField(name = "test", description = "Prueba del contador hematologico", required = true, order = 6)
    private TestBasic test;
    @ApiObjectField(name = "state", description = "Estado del contador hematologico", required = true, order = 7)
    private boolean state;
    
    public CellularCounter()
    {
        test = new TestBasic();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Short getType()
    {
        return type;
    }

    public void setType(Short type)
    {
        this.type = type;
    }

    public boolean isSum()
    {
        return sum;
    }

    public void setSum(boolean sum)
    {
        this.sum = sum;
    }

    public TestBasic getTest()
    {
        return test;
    }

    public void setTest(TestBasic test)
    {
        this.test = test;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }    
    
}
