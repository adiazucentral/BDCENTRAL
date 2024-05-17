package net.cltech.enterprisent.domain.masters.test;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro resultados literales por
 * examen
 *
 * @author eacuna
 * @since 07/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Resultado literal por prueba",
        description = "Realación de resultados literales por prueba"
)
public class LiteralByTest extends TestBasic
{

    @ApiObjectField(name = "literalResult", description = "Resultados literales asignados al examen", required = true, order = 1)
    private LiteralResult literalResult;
    @ApiObjectField(name = "assign", description = "Asignado", required = false, order = 2)
    private boolean assign;

    public LiteralByTest(Integer literalResultId, Integer id)
    {
        super(id, 0);
        literalResult = new LiteralResult(literalResultId, "");
    }

    public LiteralByTest()
    {
        literalResult = new LiteralResult();
    }

    public boolean isAssign()
    {
        return assign;
    }

    public void setAssign(boolean assign)
    {
        this.assign = assign;
    }

    public LiteralResult getLiteralResult()
    {
        return literalResult;
    }

    public void setLiteralResult(LiteralResult literalResult)
    {
        this.literalResult = literalResult;
    }

}
