package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase que obtendra la petición de examenes a homologar
 * 
 * @version 1.0.0
 * @author javila
 * @since 30/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name =  "Objeto test a homologar",
        description = "Obtendra la petición de test a homologar"
)
public class RequestTestIngreso
{
    @ApiObjectField(name = "idTestHis", description = "El id del HIS", order = 1)
    private String idTestHis;
    @ApiObjectField(name = "nameTest", description = "Nombre del examen", order = 2)
    private String nameTest;

    public RequestTestIngreso()
    {
    }

    public RequestTestIngreso(String idTestHis, String nameTest)
    {
        this.idTestHis = idTestHis;
        this.nameTest = nameTest;
    }

    public String getIdTestHis()
    {
        return idTestHis;
    }

    public void setIdTestHis(String idTestHis)
    {
        this.idTestHis = idTestHis;
    }

    public String getNameTest()
    {
        return nameTest;
    }

    public void setNameTest(String nameTest)
    {
        this.nameTest = nameTest;
    }
}
