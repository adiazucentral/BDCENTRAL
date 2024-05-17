package net.cltech.enterprisent.domain.integration.skl;

import java.util.ArrayList;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una orden para el registro de resultados
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 14/04/2020
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Orden Resultados",
        description = "Representa una orden para el filtro de ordenes dentro del módulo de registro de resultados"
)

public class ListTestOrderSkl {

    @ApiObjectField(name = "tests", description = "Cadena de examenes", required = true, order = 1)
    private List<String> tests = new ArrayList<>();
    @ApiObjectField(name = "idSample", description = "Id de la muestra", required = true, order = 2)
    private int idSample;

    public ListTestOrderSkl() {
        tests = new ArrayList<>();
    }

    public ListTestOrderSkl(List<String> tests, int idSample) {
        this.tests = tests;
        this.idSample = idSample;

    }

    public List<String> getTests() {
        return tests;
    }

    public void setTests(List<String> tests) {
        this.tests = tests;
    }

    public int getIdSample() {
        return idSample;
    }

    public void setIdSample(int idSample) {
        this.idSample = idSample;
    }

}
