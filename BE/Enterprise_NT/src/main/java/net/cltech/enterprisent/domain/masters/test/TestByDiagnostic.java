package net.cltech.enterprisent.domain.masters.test;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la relación de pruebas por diagnostico
 *
 * @version 1.0.0
 * @author equijano
 * @since 30/07/2019
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Prueba por Diagnostico",
        description = "Objeto para relación de prueba por diagnosticos"
)
public class TestByDiagnostic
{

    @ApiObjectField(name = "idDiagnostic", description = "Id del diagnostico", required = true, order = 1)
    private int idDiagnostic;
    @ApiObjectField(name = "tests", description = "tests", required = true, order = 2)
    private List<Test> tests;
    @ApiObjectField(name = "nameDiagnostic", description = "Nombre del diagnostico", required = false, order = 3)
    private String nameDiagnostic;

    public TestByDiagnostic()
    {

    }

    public int getIdDiagnostic()
    {
        return idDiagnostic;
    }

    public void setIdDiagnostic(int idDiagnostic)
    {
        this.idDiagnostic = idDiagnostic;
    }

    public List<Test> getTests()
    {
        return tests;
    }

    public void setTests(List<Test> tests)
    {
        this.tests = tests;
    }

    public String getNameDiagnostic()
    {
        return nameDiagnostic;
    }

    public void setNameDiagnostic(String nameDiagnostic)
    {
        this.nameDiagnostic = nameDiagnostic;
    }

}
