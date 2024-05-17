package net.cltech.enterprisent.domain.masters.test;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la relación de diagnosticos por pruebas
 *
 * @version 1.0.0
 * @author equijano
 * @since 01/03/2018
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Diagnosticos y Pruebas",
        description = "Objeto para obtener un listado de pruebas y de diagnosticos"
)
public class ListDiagnosticByTest
{

    @ApiObjectField(name = "tests", description = "Pruebas", required = true, order = 1)
    private List<TestBasic> tests;
    @ApiObjectField(name = "diagnostics", description = "Diagnosticos", required = true, order = 2)
    private List<Diagnostic> diagnostics;

    public List<TestBasic> getTests()
    {
        return tests;
    }

    public void setTests(List<TestBasic> tests)
    {
        this.tests = tests;
    }

    public List<Diagnostic> getDiagnostics()
    {
        return diagnostics;
    }

    public void setDiagnostics(List<Diagnostic> diagnostics)
    {
        this.diagnostics = diagnostics;
    }

}
