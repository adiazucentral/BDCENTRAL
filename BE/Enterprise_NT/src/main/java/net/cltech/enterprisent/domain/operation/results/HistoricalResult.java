/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el resultado historico de un paciente
 *
 * @version 1.0.0
 * @author dcortes
 * @since 8/03/2018
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Historico Resultado",
        description = "Representa los ultimos resultados de un paciente para un examen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoricalResult
{

    @ApiObjectField(name = "patientId", description = "Id Paciente", required = true, order = 1)
    private int patientId;
    @ApiObjectField(name = "testId", description = "Id Prueba", required = true, order = 2)
    private int testId;
    @ApiObjectField(name = "lastResult", description = "Ultimo Resultado", required = true, order = 3)
    private String lastResult;
    @ApiObjectField(name = "lastResultDate", description = "Fecha Ultimo Resultado", required = true, order = 4)
    private Date lastResultDate;
    @ApiObjectField(name = "lastResultUser", description = "Usuario Ultimo Resultado", required = true, order = 5)
    private User lastResultUser;
    @ApiObjectField(name = "secondLastResult", description = "Penultimo Resultado", required = true, order = 6)
    private String secondLastResult;
    @ApiObjectField(name = "secondLastResultDate", description = "Fecha Penultimo Resultado", required = true, order = 7)
    private Date secondLastResultDate;
    @ApiObjectField(name = "secondLastResultUser", description = "Usuario Penultimo Resultado", required = true, order = 8)
    private User secondLastResultUser;
    @ApiObjectField(name = "secondLastResultTemp", description = "Penultimo Resultado Temporal", required = true, order = 9)
    private String secondLastResultTemp;
    @ApiObjectField(name = "secondLastResultDateTemp", description = "Fecha Penultimo Resultado Temporal", required = true, order = 10)
    private Date secondLastResultDateTemp;
    @ApiObjectField(name = "secondLastResultUserTemp", description = "Usuario Penultimo Resultado Temporal", required = true, order = 11)
    private User secondLastResultUserTemp;
    @ApiObjectField(name = "testCode", description = "Código del examen", required = true, order = 12)
    private String testCode;

    public int getPatientId()
    {
        return patientId;
    }

    public void setPatientId(int patientId)
    {
        this.patientId = patientId;
    }

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    public String getLastResult()
    {
        return lastResult;
    }

    public void setLastResult(String lastResult)
    {
        this.lastResult = lastResult;
    }

    public Date getLastResultDate()
    {
        return lastResultDate;
    }

    public void setLastResultDate(Date lastResultDate)
    {
        this.lastResultDate = lastResultDate;
    }

    public User getLastResultUser()
    {
        return lastResultUser;
    }

    public void setLastResultUser(User lastResultUser)
    {
        this.lastResultUser = lastResultUser;
    }

    public String getSecondLastResult()
    {
        return secondLastResult;
    }

    public void setSecondLastResult(String secondLastResult)
    {
        this.secondLastResult = secondLastResult;
    }

    public Date getSecondLastResultDate()
    {
        return secondLastResultDate;
    }

    public void setSecondLastResultDate(Date secondLastResultDate)
    {
        this.secondLastResultDate = secondLastResultDate;
    }

    public User getSecondLastResultUser()
    {
        return secondLastResultUser;
    }

    public void setSecondLastResultUser(User secondLastResultUser)
    {
        this.secondLastResultUser = secondLastResultUser;
    }

    public String getSecondLastResultTemp()
    {
        return secondLastResultTemp;
    }

    public void setSecondLastResultTemp(String secondLastResultTemp)
    {
        this.secondLastResultTemp = secondLastResultTemp;
    }

    public Date getSecondLastResultDateTemp()
    {
        return secondLastResultDateTemp;
    }

    public void setSecondLastResultDateTemp(Date secondLastResultDateTemp)
    {
        this.secondLastResultDateTemp = secondLastResultDateTemp;
    }

    public User getSecondLastResultUserTemp()
    {
        return secondLastResultUserTemp;
    }

    public void setSecondLastResultUserTemp(User secondLastResultUserTemp)
    {
        this.secondLastResultUserTemp = secondLastResultUserTemp;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }
}
