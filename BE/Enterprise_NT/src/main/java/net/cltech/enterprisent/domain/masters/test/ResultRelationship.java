package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.interview.Question;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Relación de resultados
 *
 * @version 1.0.0
 * @author eacuna
 * @since28/07/2017
 * @see Creacion
 */
@ApiObject(
        group = "Prueba",
        name = "Relación Resultados",
        description = "Representa informacion para la relación de resultados"
)
public class ResultRelationship extends MasterAudit
{

    @ApiObjectField(name = "type", description = "Tipo de regla(0-Prueba,1-Pregunta)", required = false, order = 1)
    private Integer type;
    @ApiObjectField(name = "test", description = "Examen para la regla", required = false, order = 2)
    private TestBasic test;
    @ApiObjectField(name = "question", description = "Pregunta para la regla", required = false, order = 3)
    private Question question;
    @ApiObjectField(name = "operator", description = "Operador para la primera regla", required = true, order = 4)
    private String operator;
    @ApiObjectField(name = "result", description = "Valor inicial", required = true, order = 5)
    private String result;
    @ApiObjectField(name = "result2", description = "Valor final(aplica para operador entre)", required = false, order = 6)
    private String result2;
    @ApiObjectField(name = "operatorName", description = "Nombre del Operador para la primera regla", required = false, order = 7)
    private String operatorName;

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public TestBasic getTest()
    {
        return test;
    }

    public void setTest(TestBasic test)
    {
        this.test = test;
    }

    public Question getQuestion()
    {
        return question;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getResult2()
    {
        return result2;
    }

    public void setResult2(String result2)
    {
        this.result2 = result2;
    }

    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }

}
