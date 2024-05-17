/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.interview;

import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de las entrevistas de panico
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/10/2021
 * @see Creación
 */
@ApiObject(
        group = "Entrevista",
        name = "Entrevista de Panico",
        description = "Muestra informacion de las entrevistas de panico que usa el API"
)
public class PanicInterview {

    @ApiObjectField(name = "order", description = "Orden", required = true, order = 1)
    private Long order;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 2)
    private String patientId;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 3)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = false, order = 4)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 5)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Apellido 2", required = false, order = 6)
    private String surName;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 7)
    private int testId;
    @ApiObjectField(name = "testCode", description = "Código del examen", required = true, order = 8)
    private String testCode;
    @ApiObjectField(name = "testName", description = "Nombre del examen", required = true, order = 9)
    private String testName;
    @ApiObjectField(name = "questionId", description = "Id de la pregunta", required = true, order = 10)
    private Integer questionId;
    @ApiObjectField(name = "question", description = "Pregunta", required = true, order = 11)
    private String question;
    @ApiObjectField(name = "answerId", description = "Id de la respuesta", required = true, order = 12)
    private Integer answerId;
    @ApiObjectField(name = "answerClose", description = "Respuesta Cerrada", required = true, order = 13)
    private String answerClose;
    @ApiObjectField(name = "answer", description = "Respuesta", required = true, order = 14)
    private String answer;
    @ApiObjectField(name = "date", description = "Fecha de resultado", required = false, order = 15)
    private Date date;
    @ApiObjectField(name = "datevalidated", description = "Fecha de validación", required = false, order = 15)
    private Date dateValidated;
    @ApiObjectField(name = "panic", description = "Panico", required = true, order = 16)
    private Integer panic;
    @ApiObjectField(name = "delta", description = "Delta", required = true, order = 17)
    private Integer delta;
    @ApiObjectField(name = "critic", description = "Critico", required = true, order = 18)
    private Integer critic;
    @ApiObjectField(name = "userId", description = "Id Usuario", required = true, order = 19)
    private Integer userId;
    @ApiObjectField(name = "username", description = "Nombre de usuario", required = true, order = 20)
    private String username;

    public Date getDateValidated() {
        return dateValidated;
    }

    public void setDateValidated(Date dateValidated) {
        this.dateValidated = dateValidated;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getAnswerClose() {
        return answerClose;
    }

    public void setAnswerClose(String answerClose) {
        this.answerClose = answerClose;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPanic() {
        return panic;
    }

    public void setPanic(Integer panic) {
        this.panic = panic;
    }

    public Integer getDelta() {
        return delta;
    }

    public void setDelta(Integer delta) {
        this.delta = delta;
    }

    public Integer getCritic() {
        return critic;
    }

    public void setCritic(Integer critic) {
        this.critic = critic;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
