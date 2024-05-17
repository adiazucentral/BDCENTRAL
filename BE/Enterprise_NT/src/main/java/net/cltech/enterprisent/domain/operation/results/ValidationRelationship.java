/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cltech.enterprisent.domain.operation.results;

import java.util.Objects;
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
public class ValidationRelationship
{

    @ApiObjectField(name = "type", description = "Tipo de regla(0-Prueba,1-Pregunta)", required = true, order = 1)
    private Integer type;
    @ApiObjectField(name = "operator", description = "Operador para la primera regla", required = true, order = 2)
    private String operator;
    @ApiObjectField(name = "result", description = "Valor inicial", required = true, order = 3)
    private String result;
    @ApiObjectField(name = "result2", description = "Valor final(aplica para operador entre)", required = false, order = 4)
    private String result2;
    @ApiObjectField(name = "alarm", description = "Nombre de la alarma", required = false, order = 5)
    private String alarm;
    @ApiObjectField(name = "surveyText", description = "Resultado texto de la entrevista", required = false, order = 6)
    private String surveyText;
    @ApiObjectField(name = "surveyCode", description = "Resultado de lista de la entrevista", required = false, order = 7)
    private int surveyCode;

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
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

    public String getAlarm()
    {
        return alarm;
    }

    public void setAlarm(String alarm)
    {
        this.alarm = alarm;
    }

    public String getSurveyText()
    {
        return surveyText;
    }

    public void setSurveyText(String surveyText)
    {
        this.surveyText = surveyText;
    }

    public int getSurveyCode()
    {
        return surveyCode;
    }

    public void setSurveyCode(int surveyCode)
    {
        this.surveyCode = surveyCode;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ValidationRelationship other = (ValidationRelationship) obj;
        if (!Objects.equals(this.alarm, other.alarm))
        {
            return false;
        }
        if (!Objects.equals(this.type, other.type))
        {
            return false;
        }
        return true;
    }
}
