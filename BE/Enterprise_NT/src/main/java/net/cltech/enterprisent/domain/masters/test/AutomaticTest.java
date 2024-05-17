package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.Item;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Pruebas Automaticas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 27/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Pruebas Automaticos",
        description = "Muestra informacion del maestro Pruebas Automaticas que usa el API"
)
public class AutomaticTest extends TestBasic
{

    @ApiObjectField(name = "sign", description = "Signo", required = true, order = 1)
    private Item sign;
    @ApiObjectField(name = "result1", description = "Resultado 1", required = true, order = 2)
    private String result1;
    @ApiObjectField(name = "result2", description = "Resultado 2", required = true, order = 3)
    private String result2;
    @ApiObjectField(name = "automaticTest", description = "Prueba Automatica", required = true, order = 4)
    private TestBasic automaticTest;

    public AutomaticTest()
    {
        sign = new Item();
        automaticTest = new TestBasic();
    }

    public Item getSign()
    {
        return sign;
    }

    public void setSign(Item sign)
    {
        this.sign = sign;
    }

    public String getResult1()
    {
        return result1;
    }

    public void setResult1(String result1)
    {
        this.result1 = result1;
    }

    public String getResult2()
    {
        return result2;
    }

    public void setResult2(String result2)
    {
        this.result2 = result2;
    }

    public TestBasic getAutomaticTest()
    {
        return automaticTest;
    }

    public void setAutomaticTest(TestBasic automaticTest)
    {
        this.automaticTest = automaticTest;
    }

}
