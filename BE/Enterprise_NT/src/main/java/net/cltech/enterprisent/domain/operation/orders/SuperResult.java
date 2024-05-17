/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Operación - Resultado",
        name = "Resultado",
        description = "Representa resultado de un exámen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperResult {
    @ApiObjectField(name = "state", description = "Estado del resultado", required = false, order = 1)
    private int state;
    @ApiObjectField(name = "sampleState", description = "Estado de la muestra", required = false, order = 2)
    private int sampleState;
    @ApiObjectField(name = "print", description = "Si se imprime", required = false, order = 6)
    private boolean print;
    @ApiObjectField(name = "result", description = "Resultado", required = false, order = 3)
    private String result;
    @ApiObjectField(name = "dateResult", description = "Fecha Resultado", required = false, order = 9)
    private Date dateResult;
    @ApiObjectField(name = "dateOrdered", description = "Fecha Ingreso", required = false, order = 4)
    private Date dateOrdered;
    @ApiObjectField(name = "dateTake", description = "Fecha Toma", required = false, order = 5)
    private Date dateTake;
    @ApiObjectField(name = "dateValidation", description = "Fecha Validación", required = false, order = 10)
    private Date dateValidation;
    @ApiObjectField(name = "dateVerific", description = "Fecha de Verificacion", required = false, order = 11)
    private Date dateVerific;
    @ApiObjectField(name = "pathology", description = "Patología del examen con respecto al valor de referencia", required = true, order = 12)
    private int pathology;
    @ApiObjectField(name = "datePrint", description = "Fecha Impresion", required = false, order = 13)
    private Date datePrint;
    @ApiObjectField(name = "userRes", description = "Usuario Resultado", required = false, order = 18)
    private AuthorizedUser userRes = new AuthorizedUser();
    @ApiObjectField(name = "userVal", description = "Usuario Validación", required = false, order = 19)
    private AuthorizedUser userVal = new AuthorizedUser();
    @ApiObjectField(name = "userPri", description = "Usuario Impresión", required = false, order = 21)
    private AuthorizedUser userPri = new AuthorizedUser();
    
}
