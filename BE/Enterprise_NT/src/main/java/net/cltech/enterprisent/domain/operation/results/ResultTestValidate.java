/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cltech.enterprisent.domain.operation.results;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.domain.masters.interview.Question;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 * @version 1.0.0
 * @author jblanco
 * @since 29/04/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Validación Resultados Examen",
        description = "Representa la lista de pruebas para realizar la validación"
)
public class ResultTestValidate 
{
    @ApiObjectField(name = "finalValidate", description = "Indica si la validación es final (true) o preliminar (false)", required = true, order = 1)
    private boolean finalValidate;
    @ApiObjectField(name = "orderId", description = "Número de orden de laboratorio", required = true, order = 2)
    private Long orderId;
    @ApiObjectField(name = "sex", description = "Descipción del sexo del paciente", required = false, order = 3)
    private Item sex = new Item();
    @ApiObjectField(name = "race", description = "Información de la raza del paciente", required = false, order = 4)
    private Race race;
    @ApiObjectField(name = "size", description = "Talla del paciente", required = false, order = 5)
    private Float size;
    @ApiObjectField(name = "weight", description = "Peso del paciente", required = false, order = 6)
    private Float weight;
    @ApiObjectField(name = "tests", description = "Lista de pruebas para validar", required = true, order = 7)
    private List<ResultTest> tests;
    @ApiObjectField(name = "questions", description = "Resultado de la entrevista de pánico para realizar la validación", required = false, order = 8)
    private List<Question> questions;
    @ApiObjectField(name = "alarms", description = "Alarmas generadas por la validación realizada", required = false, order = 9)
    private List<ValidationRelationship> alarms;
    @ApiObjectField(name = "trends", description = "Alarmas por tendencia del resultado", required = false, order = 10)
    private List<Integer> trends;
    @ApiObjectField(name = "serial", description = "Serial de impresion", required = false, order = 10)
    private String serial;
    @ApiObjectField(name = "reportedDoctor", description = "Fecha reporte al doctor", required = false, order = 11)
    private Date reportedDoctor;

    public boolean isFinalValidate()
    {
        return finalValidate;
    }

    public void setFinalValidate(boolean finalValidate)
    {
        this.finalValidate = finalValidate;
    }
    
    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Item getSex()
    {
        return sex;
    }

    public void setSex(Item sex)
    {
        this.sex = sex;
    }

    public Race getRace()
    {
        return race;
    }

    public void setRace(Race race)
    {
        this.race = race;
    }

    public Float getSize()
    {
        return size;
    }

    public void setSize(Float size)
    {
        this.size = size;
    }

    public Float getWeight()
    {
        return weight;
    }

    public void setWeight(Float weight)
    {
        this.weight = weight;
    }
    
    public List<ResultTest> getTests()
    {
        return tests;
    }

    public void setTests(List<ResultTest> tests)
    {
        this.tests = tests;
    }

    public List<Question> getQuestions()
    {
        return questions;
    }

    public void setQuestions(List<Question> questions)
    {
        this.questions = questions;
    }

    public List<ValidationRelationship> getAlarms()
    {
        return alarms;
    }

    public void setAlarms(List<ValidationRelationship> alarms)
    {
        this.alarms = alarms;
    }

    public List<Integer> getTrends()
    {
        return trends;
    }

    public void setTrends(List<Integer> trends)
    {
        this.trends = trends;
    }
    
    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public Date getReportedDoctor() {
        return reportedDoctor;
    }

    public void setReportedDoctor(Date reportedDoctor) {
        this.reportedDoctor = reportedDoctor;
    }
}
