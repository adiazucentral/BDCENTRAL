/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.minsa.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
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
        name = "QuestionPendingResults",
        description = "Muestra informacion del maestro Areas que usa el API"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionPendingResults
{

    @ApiObjectField(name = "order", description = "Numero de orden", required = false, order = 1)
    private String order;
    @ApiObjectField(name = "idQuestion", description = "ID Pregunta", required = false, order = 2)
    private int idQuestion;
    @ApiObjectField(name = "question", description = "Pregunta", required = false, order = 3)
    private String question;
    @ApiObjectField(name = "openAnswer", description = "Respuesta abierta", required = false, order = 4)
    private String openAnswer;
    @ApiObjectField(name = "closedAnswer", description = "Respuesta cerrada", required = false, order = 5)
    private List<String> closedAnswer;
    @ApiObjectField(name = "typeQuestion", description = "Tipo de pregunta", required = false, order = 6)
    private String typeQuestion;

    public QuestionPendingResults()
    {
        this.closedAnswer = new ArrayList<>();
    }
}
