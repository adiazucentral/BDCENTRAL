package net.cltech.enterprisent.domain.masters.test;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la relación de diagnosticos por pruebas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 01/03/2018
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Diagnostico por Prueba",
        description = "Objeto para relación de diagnosticos por prueba"
)
public class DiagnosticByTest
{

    @ApiObjectField(name = "idTest", description = "Id del examen", required = true, order = 1)
    private int idTest;
    @ApiObjectField(name = "diagnostics", description = "Diagnosticos", required = true, order = 2)
    private List<Diagnostic> diagnostics;
    @ApiObjectField(name = "nameTest", description = "Nombre del examen", required = false, order = 3)
    private String nameTest;

    public DiagnosticByTest()
    {

    }

    public int getIdTest()
    {
        return idTest;
    }

    public void setIdTest(int idTest)
    {
        this.idTest = idTest;
    }

    public List<Diagnostic> getDiagnostics()
    {
        return diagnostics;
    }

    public void setDiagnostics(List<Diagnostic> diagnostics)
    {
        this.diagnostics = diagnostics;
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
