package net.cltech.enterprisent.domain.operation.tracking;

import net.cltech.enterprisent.domain.masters.common.Motive;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.jsondoc.core.pojo.ApiVisibility;

/**
 * Representa un examen que sera retomado
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 04/03/2020
 * @see Creacion
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Trazabilidad de la Muestra",
        description = "Muestra la Trazabilidad de la muestra que usa el API",
        visibility = ApiVisibility.PRIVATE
)
public class TestSampleTakeTracking
{

    @ApiObjectField(name = "idTest", description = "Id del examen", order = 1)
    private Integer idTest;
    @ApiObjectField(name = "state", description = "Estado de la Muestra: -1 -> Pendiente 0 -> Rechazado, 1 -> Nueva Muestra, 2 -> Ordenado, 3 -> Tomado, 4 -> Verificado", required = false, order = 2)
    private Integer state;
    @ApiObjectField(name = "motive", description = "motivo ", required = false, order = 3)
    private Integer motive;
    @ApiObjectField(name = "name", description = "nombre ", required = false, order = 4)
    private String name;

    public TestSampleTakeTracking(Integer idTest, Integer state, Integer motive, String name)
    {
        this.idTest = idTest;
        this.state = state;
        this.motive = motive;
        this.name = name;

    }

    public TestSampleTakeTracking()
    {

    }

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getMotive()
    {
        return motive;
    }

    public void setMotive(Integer motive)
    {
        this.motive = motive;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
