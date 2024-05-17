/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.minsa.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 *
 * @version 1.0.0
 * @author bbonilla
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "ResultsTestPendingResults",
        description = "Muestra informacion del maestro Areas que usa el API"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultsTestPendingResults
{

    @ApiObjectField(name = "id", description = "Id del examen", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 2)
    private String result;
    @ApiObjectField(name = "validation", description = "Fecha de validacion", required = true, order = 3)
    private Date validation;
    @ApiObjectField(name = "regist", description = "Fecha de resgistro", required = true, order = 4)
    private Date regist;
    @ApiObjectField(name = "testCode", description = "Codigo del examen", required = true, order = 5)
    private String testCode;
    @ApiObjectField(name = "resultLiteral", description = "resultado literal", required = true, order = 6)
    private String resultLiteral;
    @ApiObjectField(name = "type", description = "Tipo de resultado", required = true, order = 7)
    private String type;
    @ApiObjectField(name = "patologic", description = "Patologico", required = true, order = 8)
    private String patologic;
    @ApiObjectField(name = "comment", description = "Comentario", required = true, order = 9)
    private String comment;

    public ResultsTestPendingResults()
    {
    }
}
