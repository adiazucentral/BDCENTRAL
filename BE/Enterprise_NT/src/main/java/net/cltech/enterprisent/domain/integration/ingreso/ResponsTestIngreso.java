package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase de respuesta para el examen
 *
 * @version 1.0.0
 * @author javila
 * @since 30/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Examen de ingreso test a retornar",
        description = "Sera el objeto de respuesta que obtendra los datos requeridos por el F.E"
)
public class ResponsTestIngreso {

    @ApiObjectField(name = "idItemTestHis", description = "El id HIS del item del examen", order = 1)
    private String idItemTestHis;
    @ApiObjectField(name = "name", description = "El nombre del examen", order = 2)
    private String name;
    @ApiObjectField(name = "idItemTestLis", description = "El id LIS del item del examen", order = 3)
    private int idItemTestLis;
    @ApiObjectField(name = "codeTestLis", description = "El codigo del LIS", order = 4)
    private String codeTestLis;

    public ResponsTestIngreso() {
    }

    public ResponsTestIngreso(String name) {
        this.name = name;

    }

    public ResponsTestIngreso(String idItemTestHis, String name, int idItemTestLis, String codeTestLis) {
        this.idItemTestHis = idItemTestHis;
        this.name = name;
        this.idItemTestLis = idItemTestLis;
        this.codeTestLis = codeTestLis;
    }

    public String getIdItemTestHis() {
        return idItemTestHis;
    }

    public void setIdItemTestHis(String idItemTestHis) {
        this.idItemTestHis = idItemTestHis;
    }

    public String getName() {
        return name;
    }

    public void setnName(String name) {
        this.name = name;
    }

    public int getIdItemTestLis() {
        return idItemTestLis;
    }

    public void setIdItemTestLis(int idItemTestLis) {
        this.idItemTestLis = idItemTestLis;
    }

    public String getCodeTestLis() {
        return codeTestLis;
    }

    public void setCodeTestLis(String codeTestLis) {
        this.codeTestLis = codeTestLis;
    }
}
