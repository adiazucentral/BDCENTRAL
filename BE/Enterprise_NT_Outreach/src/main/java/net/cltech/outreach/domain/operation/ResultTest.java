/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un examen para el registro de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 02/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Resultados Examen",
        description = "Representa una examen para el registro de resultados"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultTest
{
    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "areaId", description = "Id del area", required = true, order = 2)
    private int areaId;
    @ApiObjectField(name = "areaCode", description = "Codigo del area", required = true, order = 3)
    private String areaCode;
    @ApiObjectField(name = "areaAbbr", description = "Abreviación del area", required = true, order = 4)
    private String areaAbbr;
    @ApiObjectField(name = "areaName", description = "Nombre del area", required = true, order = 5)
    private String areaName;
    @ApiObjectField(name = "sampleId", description = "Id del area", required = true, order = 6)
    private int sampleId;
    @ApiObjectField(name = "sampleCode", description = "Codigo del area", required = true, order = 7)
    private String sampleCode;
    @ApiObjectField(name = "sampleName", description = "Nombre del area", required = true, order = 8)
    private String sampleName;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 9)
    private int testId;
    @ApiObjectField(name = "testCode", description = "Código del examen", required = true, order = 10)
    private String testCode;
    @ApiObjectField(name = "result", description = "Resultado del examen", required = false, order = 11)
    private String result;
    @ApiObjectField(name = "resultDate", description = "Fecha del resultado del examen", required = false, order = 12)
    private Date resultDate;
    @ApiObjectField(name = "resultUserId", description = "Usuario que ha reportado el resultado", required = false, order = 13)
    private int resultUserId;
    @ApiObjectField(name = "state", description = "Estado del examen", required = true, order = 14)
    private int state;
    @ApiObjectField(name = "newState", description = "Nuevo estado del examen", required = true, order = 15)
    private int newState;
    @ApiObjectField(name = "userId", description = "Usuario que realiza la acción", required = true, order = 16)
    private int userId;
    @ApiObjectField(name = "resultType", description = "Tipo de resultado", required = false, order = 17)
    private short resultType;
    @ApiObjectField(name = "pathology", description = "Patología del examen con respecto al valor de referencia", required = true, order = 18)
    private int pathology;
    @ApiObjectField(name = "refMin", description = "Referencia mínima", required = false, order = 19)
    private Float refMin;
    @ApiObjectField(name = "refMax", description = "Referecia máxima", required = false, order = 20)
    private Float refMax;
    @ApiObjectField(name = "refInterval", description = "Intervalo de referencia", required = false, order = 21)
    private String refInterval;
    @ApiObjectField(name = "panicMin", description = "Pánico mínimo", required = false, order = 22)
    private Float panicMin;
    @ApiObjectField(name = "panicMax", description = "Pánico máximo", required = false, order = 23)
    private Float panicMax;
    @ApiObjectField(name = "panicInterval", description = "Intervalo de pánico", required = false, order = 24)
    private String panicInterval;
    @ApiObjectField(name = "reportedMin", description = "Reportable mínimo", required = false, order = 25)
    private Float reportedMin;
    @ApiObjectField(name = "reportedMax", description = "Reportable máximo", required = false, order = 26)
    private Float reportedMax;
    @ApiObjectField(name = "reportedInterval", description = "Intervalo de reporte", required = false, order = 27)
    private String reportedInterval;
    @ApiObjectField(name = "critic", description = "Indicador de criticiadad del pánico", required = false, order = 28)
    private short critic;
    @ApiObjectField(name = "refLiteral", description = "Referencia normal para resultado literal", required = false, order = 29)
    private String refLiteral;
    @ApiObjectField(name = "panicLiteral", description = "Panico para resultado literal", required = false, order = 30)
    private String panicLiteral;
    @ApiObjectField(name = "unit", description = "Unidades del resultado", required = false, order = 31)
    private String unit;
    @ApiObjectField(name = "abbreviation", description = "Abreviatura del examen", required = false, order = 32)
    private String abbreviation;
    @ApiObjectField(name = "digits", description = "Dígitos decimales", required = false, order = 33)
    private short digits;
    @ApiObjectField(name = "hasComment", description = "Indicador de resultado con comentario", required = true, order = 35)
    private boolean hasComment;
    @ApiObjectField(name = "resultChanged", description = "Indicador de modificación del resultado", required = true, order = 36)
    private boolean resultChanged;
    @ApiObjectField(name = "sampleState", description = "Estado de la muestra", required = true, order = 37)
    private int sampleState;
    @ApiObjectField(name = "confidential", description = "Indica si es confidencial", required = true, order = 38)
    private Boolean confidential;
    @ApiObjectField(name = "repeatAmmount", description = "Cantidad de repeticiones", required = true, order = 39)
    private Integer repeatAmmount;
    @ApiObjectField(name = "modificationAmmount", description = "Cantidad de modificaciones", required = true, order = 40)
    private Integer modificationAmmount;
    @ApiObjectField(name = "testName", description = "Nombre del examen", required = true, order = 41)
    private String testName;
    @ApiObjectField(name = "orderPathology", description = "Patología general de la orden", required = true, order = 42)
    private int orderPathology;
    @ApiObjectField(name = "deltaMin", description = "Delta mínimo", required = false, order = 43)
    private Float deltaMin;
    @ApiObjectField(name = "deltaMax", description = "Delta máximo", required = false, order = 44)
    private Float deltaMax;
    @ApiObjectField(name = "deltaInterval", description = "Intervalo de delta", required = false, order = 45)
    private String deltaInterval;
    @ApiObjectField(name = "lastResult", description = "Último resultado", required = false, order = 46)
    private String lastResult;
    @ApiObjectField(name = "lastResultDate", description = "Fecha último resultado", required = false, order = 47)
    private Date lastResultDate;
    @ApiObjectField(name = "secondLastResult", description = "Penúltimo resultado", required = false, order = 48)
    private String secondLastResult;
    @ApiObjectField(name = "secondLastResultDate", description = "Fecha penúltimo resultado", required = false, order = 49)
    private Date secondLastResultDate;
    @ApiObjectField(name = "currentState", description = "Estado actual del examen", required = true, order = 50)
    private int currentState;
    @ApiObjectField(name = "validationDate", description = "Fecha de validación", required = false, order = 51)
    private Date validationDate;
    @ApiObjectField(name = "validationUserId", description = "Usuario que validó el exámen", required = false, order = 52)
    private Integer validationUserId;
    @ApiObjectField(name = "entryDate", description = "Fecha de ingreso", required = false, order = 53)
    private Date entryDate;
    @ApiObjectField(name = "entryUserId", description = "Usuario que ingreso exámen", required = false, order = 54)
    private int entryUserId;
    @ApiObjectField(name = "takenDate", description = "Fecha de toma de la muestra", required = false, order = 55)
    private Date takenDate;
    @ApiObjectField(name = "takenUserId", description = "Usuario que tomó la muestra ", required = false, order = 56)
    private Integer takenUserId;
    @ApiObjectField(name = "verificationDate", description = "Fecha de verificación", required = false, order = 57)
    private Date verificationDate;
    @ApiObjectField(name = "verificationUserId", description = "Usuario que verifica", required = false, order = 58)
    private Integer verificationUserId;
    @ApiObjectField(name = "printDate", description = "Fecha de impresion", required = false, order = 59)
    private Date printDate;
    @ApiObjectField(name = "printUserId", description = "Usuario que imprime", required = false, order = 60)
    private Integer printUserId;
    @ApiObjectField(name = "unitId", description = "Id de la unidad", required = true, order = 61)
    private int unitId;
    @ApiObjectField(name = "unitInternational", description = "Unidad internacional", required = true, order = 62)
    private String unitInternational;
    @ApiObjectField(name = "unitConversionFactor", description = "Factor de conversion de la unidad", required = true, order = 63)
    private Float unitConversionFactor;
    @ApiObjectField(name = "laboratoryType", description = "Tipos de laboratorio de la muestra", required = false, order = 66)
    private String laboratoryType;
    @ApiObjectField(name = "entryType", description = "Tipo de entrada del resultado. Indica si es de forma automatizada.", required = false, order = 67)
    private short entryType;
    @ApiObjectField(name = "entryTestType", description = "Tipo de entrada del examen. Indica si el exámen fue ingresado directo(1) o es un cultivo(2).", required = false, order = 67)
    private Short entryTestType;
    @ApiObjectField(name = "testType", description = "Tipo del examen", required = false, order = 70)
    private int testType;
    @ApiObjectField(name = "entry", description = "Si fue un examen ingresado", required = false, order = 71)
    private boolean entry;
    @ApiObjectField(name = "formula", description = "Tipos de laboratorio de la muestra", required = false, order = 72)
    private String formula;
    @ApiObjectField(name = "areaPartialValidation", description = "Validacion parcial", required = true, order = 10)
    private boolean areaPartialValidation;
    @ApiObjectField(name = "hasAntibiogram", description = "Indicador de resultado con antibiograma", required = true, order = 74)
    private Boolean hasAntibiogram;
    @ApiObjectField(name = "profileId", description = "Identificador del perfil", required = true, order = 83)
    private int profileId;
    @ApiObjectField(name = "profileName", description = "Nombre del perfil", required = true, order = 84)
    private String profileName;
    @ApiObjectField(name = "resultComment", description = "Comentario del resultado", required = false, order = 34)
    private ResultTestComment resultComment;
    @ApiObjectField(name = "blockTest", description = "Identificador del bloqueo del examen", required = true, order = 83)
    private int blockTest;
    
    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getAreaId()
    {
        return areaId;
    }

    public void setAreaId(int areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaCode()
    {
        return areaCode;
    }

    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }

    public String getAreaAbbr()
    {
        return areaAbbr;
    }

    public void setAreaAbbr(String areaAbbr)
    {
        this.areaAbbr = areaAbbr;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public int getSampleId()
    {
        return sampleId;
    }

    public void setSampleId(int sampleId)
    {
        this.sampleId = sampleId;
    }

    public String getSampleCode()
    {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode)
    {
        this.sampleCode = sampleCode;
    }

    public String getSampleName()
    {
        return sampleName;
    }

    public void setSampleName(String sampleName)
    {
        this.sampleName = sampleName;
    }

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    public String getTestCode()
    {
        return testCode;
    }

    public void setTestCode(String testCode)
    {
        this.testCode = testCode;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public Date getResultDate()
    {
        return resultDate;
    }

    public void setResultDate(Date resultDate)
    {
        this.resultDate = resultDate;
    }

    public int getResultUserId()
    {
        return resultUserId;
    }

    public void setResultUserId(int resultUserId)
    {
        this.resultUserId = resultUserId;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getNewState()
    {
        return newState;
    }

    public void setNewState(int newState)
    {
        this.newState = newState;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public short getResultType()
    {
        return resultType;
    }

    public void setResultType(short resultType)
    {
        this.resultType = resultType;
    }

    public int getPathology()
    {
        return pathology;
    }

    public void setPathology(int pathology)
    {
        this.pathology = pathology;
    }

    public Float getRefMin()
    {
        return refMin;
    }

    public void setRefMin(Float refMin)
    {
        this.refMin = refMin;
    }

    public Float getRefMax()
    {
        return refMax;
    }

    public void setRefMax(Float refMax)
    {
        this.refMax = refMax;
    }

    public String getRefInterval()
    {
        return refInterval;
    }

    public void setRefInterval(String refInterval)
    {
        this.refInterval = refInterval;
    }

    public Float getPanicMin()
    {
        return panicMin;
    }

    public void setPanicMin(Float panicMin)
    {
        this.panicMin = panicMin;
    }

    public Float getPanicMax()
    {
        return panicMax;
    }

    public void setPanicMax(Float panicMax)
    {
        this.panicMax = panicMax;
    }

    public String getPanicInterval()
    {
        return panicInterval;
    }

    public void setPanicInterval(String panicInterval)
    {
        this.panicInterval = panicInterval;
    }

    public Float getReportedMin()
    {
        return reportedMin;
    }

    public void setReportedMin(Float reportedMin)
    {
        this.reportedMin = reportedMin;
    }

    public Float getReportedMax()
    {
        return reportedMax;
    }

    public void setReportedMax(Float reportedMax)
    {
        this.reportedMax = reportedMax;
    }

    public String getReportedInterval()
    {
        return reportedInterval;
    }

    public void setReportedInterval(String reportedInterval)
    {
        this.reportedInterval = reportedInterval;
    }

    public short getCritic()
    {
        return critic;
    }

    public void setCritic(short critic)
    {
        this.critic = critic;
    }

    public String getRefLiteral()
    {
        return refLiteral;
    }

    public void setRefLiteral(String refLiteral)
    {
        this.refLiteral = refLiteral;
    }

    public String getPanicLiteral()
    {
        return panicLiteral;
    }

    public void setPanicLiteral(String panicLiteral)
    {
        this.panicLiteral = panicLiteral;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    public short getDigits()
    {
        return digits;
    }

    public void setDigits(short digits)
    {
        this.digits = digits;
    }

    public boolean isHasComment()
    {
        return hasComment;
    }

    public void setHasComment(boolean hasComment)
    {
        this.hasComment = hasComment;
    }

    public boolean isResultChanged()
    {
        return resultChanged;
    }

    public void setResultChanged(boolean resultChanged)
    {
        this.resultChanged = resultChanged;
    }

    public int getSampleState()
    {
        return sampleState;
    }

    public void setSampleState(int sampleState)
    {
        this.sampleState = sampleState;
    }

    public Boolean getConfidential()
    {
        return confidential;
    }

    public void setConfidential(Boolean confidential)
    {
        this.confidential = confidential;
    }

    public Integer getRepeatAmmount()
    {
        return repeatAmmount;
    }

    public void setRepeatAmmount(Integer repeatAmmount)
    {
        this.repeatAmmount = repeatAmmount;
    }

    public Integer getModificationAmmount()
    {
        return modificationAmmount;
    }

    public void setModificationAmmount(Integer modificationAmmount)
    {
        this.modificationAmmount = modificationAmmount;
    }

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

    public int getOrderPathology()
    {
        return orderPathology;
    }

    public void setOrderPathology(int orderPathology)
    {
        this.orderPathology = orderPathology;
    }

    public Float getDeltaMin()
    {
        return deltaMin;
    }

    public void setDeltaMin(Float deltaMin)
    {
        this.deltaMin = deltaMin;
    }

    public Float getDeltaMax()
    {
        return deltaMax;
    }

    public void setDeltaMax(Float deltaMax)
    {
        this.deltaMax = deltaMax;
    }

    public String getDeltaInterval()
    {
        return deltaInterval;
    }

    public void setDeltaInterval(String deltaInterval)
    {
        this.deltaInterval = deltaInterval;
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

    public int getCurrentState()
    {
        return currentState;
    }

    public void setCurrentState(int currentState)
    {
        this.currentState = currentState;
    }

    public Date getValidationDate()
    {
        return validationDate;
    }

    public void setValidationDate(Date validationDate)
    {
        this.validationDate = validationDate;
    }

    public Integer getValidationUserId()
    {
        return validationUserId;
    }

    public void setValidationUserId(Integer validationUserId)
    {
        this.validationUserId = validationUserId;
    }

    public Date getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(Date entryDate)
    {
        this.entryDate = entryDate;
    }

    public int getEntryUserId()
    {
        return entryUserId;
    }

    public void setEntryUserId(int entryUserId)
    {
        this.entryUserId = entryUserId;
    }

    public Date getTakenDate()
    {
        return takenDate;
    }

    public void setTakenDate(Date takenDate)
    {
        this.takenDate = takenDate;
    }

    public Integer getTakenUserId()
    {
        return takenUserId;
    }

    public void setTakenUserId(Integer takenUserId)
    {
        this.takenUserId = takenUserId;
    }

    public Date getVerificationDate()
    {
        return verificationDate;
    }

    public void setVerificationDate(Date verificationDate)
    {
        this.verificationDate = verificationDate;
    }

    public Integer getVerificationUserId()
    {
        return verificationUserId;
    }

    public void setVerificationUserId(Integer verificationUserId)
    {
        this.verificationUserId = verificationUserId;
    }

    public Date getPrintDate()
    {
        return printDate;
    }

    public void setPrintDate(Date printDate)
    {
        this.printDate = printDate;
    }

    public Integer getPrintUserId()
    {
        return printUserId;
    }

    public void setPrintUserId(Integer printUserId)
    {
        this.printUserId = printUserId;
    }

    public int getUnitId()
    {
        return unitId;
    }

    public void setUnitId(int unitId)
    {
        this.unitId = unitId;
    }

    public String getUnitInternational()
    {
        return unitInternational;
    }

    public void setUnitInternational(String unitInternational)
    {
        this.unitInternational = unitInternational;
    }

    public Float getUnitConversionFactor()
    {
        return unitConversionFactor;
    }

    public void setUnitConversionFactor(Float unitConversionFactor)
    {
        this.unitConversionFactor = unitConversionFactor;
    }

    public String getLaboratoryType()
    {
        return laboratoryType;
    }

    public void setLaboratoryType(String laboratoryType)
    {
        this.laboratoryType = laboratoryType;
    }

    public short getEntryType()
    {
        return entryType;
    }

    public void setEntryType(short entryType)
    {
        this.entryType = entryType;
    }

    public Short getEntryTestType()
    {
        return entryTestType;
    }

    public void setEntryTestType(Short entryTestType)
    {
        this.entryTestType = entryTestType;
    }

    public int getTestType()
    {
        return testType;
    }

    public void setTestType(int testType)
    {
        this.testType = testType;
    }

    public boolean isEntry()
    {
        return entry;
    }

    public void setEntry(boolean entry)
    {
        this.entry = entry;
    }

    public String getFormula()
    {
        return formula;
    }

    public void setFormula(String formula)
    {
        this.formula = formula;
    }

    public boolean isAreaPartialValidation()
    {
        return areaPartialValidation;
    }

    public void setAreaPartialValidation(boolean areaPartialValidation)
    {
        this.areaPartialValidation = areaPartialValidation;
    }

    public Boolean getHasAntibiogram() {
        return hasAntibiogram;
    }

    public void setHasAntibiogram(Boolean hasAntibiogram) {
        this.hasAntibiogram = hasAntibiogram;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public ResultTestComment getResultComment() {
        return resultComment;
    }

    public void setResultComment(ResultTestComment resultComment) {
        this.resultComment = resultComment;
    }

    public int getBlockTest() {
        return blockTest;
    }

    public void setBlockTest(int blockTest) {
        this.blockTest = blockTest;
    }
    
    

            
}
