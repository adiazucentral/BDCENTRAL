package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el item de la actualizacion de codigo central
 * 
 * @version 1.0.0
 * @author javila
 * @since 05/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Items de actualizacion de codigo central",
        description = "Auto creacion de items"
)
public class RequestItemCentralCode
{
    @ApiObjectField(name = "idTest", description = "Identificador del examen", required = true, order = 1)
    private int idTest;
    @ApiObjectField(name = "centralCode", description = "Codigo Cups para homologación", required = true, order = 2)
    private String centralCode;

    public RequestItemCentralCode()
    {
    }

    public RequestItemCentralCode(int idTest, String centralCode)
    {
        this.idTest = idTest;
        this.centralCode = centralCode;
    }

    public int getIdTest()
    {
        return idTest;
    }

    public void setIdTest(int idTest)
    {
        this.idTest = idTest;
    }

    public String getCentralCode()
    {
        return centralCode;
    }

    public void setCentralCode(String centralCode)
    {
        this.centralCode = centralCode;
    }
}
