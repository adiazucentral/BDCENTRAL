/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la repetición o modificación del resultado de un examen
 *
 * @version 1.0.0
 * @author jblanco
 * @since Mar 2, 2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Estado del examen de una orden ",
        description = "Representa el estado del examen de una orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class ResultTestStateOrder {
    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 2)
    private int testId;
    @ApiObjectField(name = "state", description = "Estado del examen", required = true, order = 3)
    private int state;
    @ApiObjectField(name = "result", description = "Resultado del examen ", required = true, order = 4)
    private String result;
    @ApiObjectField(name = "patientId", description = "Paciente relacionado a la orden ", required = true, order = 5)
    private int patientId;
    @ApiObjectField(name = "dateResult", description = "Fecha Resultado", required = false, order = 9)
    private Date dateResult;
    @ApiObjectField(name = "userResult", description = "Usuario Resultado", required = false, order = 18)
    private int userResult;
    @ApiObjectField(name = "previousResult", description = "Resultado anterior", required = true, order = 58)
    private String previousResult;

    @Override
    public String toString()
    {
        return "ResultTestStateOrder{" + "order=" + order + ", testId=" + testId + ", state=" + state + ", result=" + result + ", patientId=" + patientId + ", dateResult=" + dateResult + ", userResult=" + userResult + '}';
    }
    
}
