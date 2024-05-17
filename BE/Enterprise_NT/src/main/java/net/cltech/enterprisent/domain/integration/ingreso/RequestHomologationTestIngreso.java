package net.cltech.enterprisent.domain.integration.ingreso;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la importación de examenes por sistema central a homologar
 * 
 * @version 1.0.0
 * @author javila
 * @since 30/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name =  "Lista test a homologar",
        description = "Obtendra la petición de los examenes a homologar"
)
public class RequestHomologationTestIngreso
{
    @ApiObjectField(name = "idSystem", description = "Id del sistema central (CUPS)", order = 1)
    private int idSystem;
    @ApiObjectField(name = "test", description = "Es la lista de examenes a homologar", order = 2)
    private List<RequestTestIngreso> test;

    public RequestHomologationTestIngreso()
    {
    }
    
    public RequestHomologationTestIngreso(int idSystem, List<RequestTestIngreso> test)
    {
        this.idSystem = idSystem;
        this.test = test;
    }

    public List<RequestTestIngreso> getTest()
    {
        return test;
    }

    public void setTest(List<RequestTestIngreso> test)
    {
        this.test = test;
    }

    public int getIdSystem()
    {
        return idSystem;
    }

    public void setIdSystem(int idSystemCentral)
    {
        this.idSystem = idSystemCentral;
    }
}
