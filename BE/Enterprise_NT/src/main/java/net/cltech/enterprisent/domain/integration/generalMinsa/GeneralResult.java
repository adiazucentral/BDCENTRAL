package net.cltech.enterprisent.domain.integration.generalMinsa;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */
@ApiObject(
        group = "Integración (General)",
        name = "Resultado General",
        description = "Representa un resultado general"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GeneralResult
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 2)
    private String result;
    @ApiObjectField(name = "validation", description = "Fecha de validación", required = true, order = 3)
    private Timestamp validation;
    @ApiObjectField(name = "regist", description = "Fecha de registro", required = true, order = 4)
    private Timestamp regist;
    @ApiObjectField(name = "testCode", description = "Codigo LIS del examen", required = true, order = 5)
    private String testCode;
    @ApiObjectField(name = "resultLiteral", description = "Resultado literal", required = true, order = 6)
    private String resultLiteral;
    @ApiObjectField(name = "comment", description = "Comentario", required = true, order = 7)
    private String comment;
    @ApiObjectField(name = "interview", description = "Entrevista", required = true, order = 8)
    private GeneralInterview interview;
}
