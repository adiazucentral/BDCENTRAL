package net.cltech.enterprisent.domain.integration.ingreso;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la importacion de examenes por sistema central a homologar
 * 
 * @version 1.0.0
 * @author javila
 * @since 30/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Examenes homologados para la interfaz de ingreso",
        description = "Sera el objeto de respuesta que obtendra los datos requeridos por el F.E"
)
public class ResponsHomologationTestIngreso
{
    @ApiObjectField(name = "idSystem", description = "El id del sistema central (CUPS)", order = 1)
    private int idSystem;
    @ApiObjectField(name = "test", description = "Arreglo de examenes homologados", order = 2)
    private List<ResponsTestIngreso> test;

    public ResponsHomologationTestIngreso()
    {
    }

    public ResponsHomologationTestIngreso(int idSystem, List<ResponsTestIngreso> test)
    {
        this.idSystem = idSystem;
        this.test = test;
    }

    public List<ResponsTestIngreso> getTest()
    {
        return test;
    }

    public void setTest(List<ResponsTestIngreso> test)
    {
        this.test = test;
    }

    public int getIdSystem()
    {
        return idSystem;
    }

    public void setIdSystem(int idSystem)
    {
        this.idSystem = idSystem;
    }
}
