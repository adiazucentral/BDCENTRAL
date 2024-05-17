package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de relacion de items y exámenes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 03/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Excluir Pruebas",
        description = "Muestra informacion de relacion de items y exámenes"
)
public class ExcludeTest extends Demographic
{

    @ApiObjectField(name = "test", description = "examen", required = true, order = 1)
    private TestBasic test;

    public ExcludeTest()
    {
        test = new TestBasic();

    }

    public TestBasic getTest()
    {
        return test;
    }

    public void setTest(TestBasic test)
    {
        this.test = test;
    }

}
