package net.cltech.enterprisent.domain.integration.resultados;

import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa la respuesta con los datos necesarios para la lab57
 * para la interfaz de resultados
 *
 * @version 1.0.0
 * @author javila
 * @since 28/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Resultados por orden y por sistema central",
        description = "Detalle de los resultados por orden y coddigo central"
)
@Getter
@Setter
public class ResponseOrderDetailResult
{

    @ApiObjectField(name = "sectionCode", description = "Codigo del area", required = true)
    private String sectionCode;
    @ApiObjectField(name = "sectionName", description = "Nombre del area", required = true)
    private String sectionName;
    @ApiObjectField(name = "userCode", description = "Email por defecto del usuario", required = true)
    private String userCode;
    @ApiObjectField(name = "userName", description = "Usuario de validación", required = true)
    private String userName;
    @ApiObjectField(name = "userLastName", description = "Usuario de validación", required = true)
    private String userLastName;
    @ApiObjectField(name = "testId", description = "Id del examen", required = true)
    private int testId;
    @ApiObjectField(name = "testCode", description = "Codigo del examen", required = true)
    private String testCode;
    @ApiObjectField(name = "testName", description = "Nombre del examen", required = true)
    private String testName;
    @ApiObjectField(name = "testResult", description = "Resultado del examen", required = true)
    private String testResult;
    @ApiObjectField(name = "profileId", description = "Id del perfil", required = true)
    private Integer profileId;
    @ApiObjectField(name = "profileName", description = "Nombre del perfil", required = true)
    private String profileName;
    @ApiObjectField(name = "profileCups", description = "Cups del perfil", required = true)
    private String profileCups;
    @ApiObjectField(name = "interfaceTestID", description = "Codigo central", required = true)
    private String interfaceTestID;
    @ApiObjectField(name = "antibiogram", description = "Antibiograma", required = true)
    private int antibiogram;
    @ApiObjectField(name = "cups", description = "Codigo de homologación", required = true)
    private String cups;
    @ApiObjectField(name = "confidential", description = "Confidencialidad del examen", required = true)
    private int confidential;
    @ApiObjectField(name = "validateDate", description = "Fecha de validación", required = true)
    private String validateDate;
    @ApiObjectField(name = "resultDate", description = "Fecha en la que se asigno ese resultado", required = true)
    private String resultDate;
    @ApiObjectField(name = "sampleCode", description = "Codigo de la muestra", required = true)
    private int sampleCode;
    @ApiObjectField(name = "sampleName", description = "Nombre de la muestra", required = true)
    private String sampleName;
    @ApiObjectField(name = "comment", description = "Comentario", required = true)
    private String comment;
    @ApiObjectField(name = "units", description = "Unidades de medición", required = true)
    private String units;
    @ApiObjectField(name = "patological", description = "Patologia", required = true)
    private int patological;
    @ApiObjectField(name = "methodCode", description = "Codigo de la tecnica", required = true)
    private String methodCode;
    @ApiObjectField(name = "methodName", description = "Nombre de la tecnica", required = true)
    private String methodName;
    @ApiObjectField(name = "normalMin", description = "Valor de referencia normal minimo", required = true)
    private Double normalMin;
    @ApiObjectField(name = "normalMax", description = "Valor de referencia normal maximo", required = true)
    private Double normalMax;
    @ApiObjectField(name = "panicMin", description = "Valor de panico minimo", required = true)
    private Double panicMin;
    @ApiObjectField(name = "panicMax", description = "Valor de panico maximo", required = true)
    private Double panicMax;
    @ApiObjectField(name = "refText", description = "Texto de referencia", required = true)
    private String refText;
    @ApiObjectField(name = "refLiteral", description = "Texto de referencia predefinido", required = true)
    private String refLiteral;
    @ApiObjectField(name = "decimalDigits", description = "Digitos decimales", required = true)
    private int decimalDigits;
    @ApiObjectField(name = "resultType", description = "Tipo de resultado", required = true)
    private int resultType;
    @ApiObjectField(name = "his", description = "Sistema de información hospitalario", required = true)
    private int his;
    @ApiObjectField(name = "userNickName", description = "Nombre del nick del usuario", required = true)
    private String userNickName;
    @ApiObjectField(name = "validateDateFormat", description = "Fecha de validación", required = true)
    private String validateDateFormat;
    @ApiObjectField(name = "resultDateFormat", description = "Fecha en la que se asigno ese resultado", required = true)
    private String resultDateFormat;
    @ApiObjectField(name = "dateTaken", description = "Fecha en la que se asigno ese resultado", required = true)
    private String dateTaken;
    @ApiObjectField(name = "correction", description = "Resultado corregido: 0 -> Resultado no corregido, 1 -> Corrección de resultado", required = true)
    private int correction;
    @ApiObjectField(name = "stateResult", description = "Resultado corregido: P -> PRELIMINAR, 1 -> FINAL", required = true)
    private String stateResult;
    @ApiObjectField(name = "recount", description = "Comentario de recuentos", required = false, order = 8)
    private String recount;
    @ApiObjectField(name = "determinations", description = "Comentario de determinaciones", required = false, order = 8)
    private String determinations;
    @ApiObjectField(name = "complementations", description = "Comentario de complementaciones", required = false, order = 8)
    private String complementations;

    public ResponseOrderDetailResult()
    {
        this.sectionCode = "";
        this.sectionName = "";
        this.userCode = "";
        this.userName = "";
        this.userLastName = "";
        this.testId = 0;
        this.testCode = "";
        this.testName = "";
        this.testResult = "";
        this.profileId = 0;
        this.profileName = "";
        this.profileCups = "";
        this.interfaceTestID = "";
        this.antibiogram = 0;
        this.cups = "";
        this.confidential = 0;
        this.validateDate = null;
        this.resultDate = null;
        this.sampleCode = 0;
        this.sampleName = "";
        this.comment = "";
        this.units = "";
        this.patological = 0;
        this.methodCode = "";
        this.methodName = "";
        this.refText = "";
        this.refLiteral = "";
        this.decimalDigits = 0;
        this.resultType = 0;
        this.his = 0;
        this.validateDateFormat = null;
        this.resultDateFormat = null;
        this.dateTaken = null;

    }

    public ResponseOrderDetailResult(String sectionCode, String sectionName, String userCode, String userName, String userLastName, int testId, String testCode, String testName, String testResult, int profileId, String profileName, String profileCups, String interfaceTestID, int antibiogram, String cups, int confidential, String validateDate, String resultDate, int sampleCode, String sampleName, String comment, String units, int patological, String methodCode, String methodName, double normalMin, double normalMax, double panicMin, double panicMax, String refText, String refLiteral, int decimalDigits, int resultType, int his)
    {
        this.sectionCode = sectionCode;
        this.sectionName = sectionName;
        this.userCode = userCode;
        this.userName = userName;
        this.userLastName = userLastName;
        this.testId = testId;
        this.testCode = testCode;
        this.testName = testName;
        this.testResult = testResult;
        this.profileId = profileId;
        this.profileName = profileName;
        this.profileCups = profileCups;
        this.interfaceTestID = interfaceTestID;
        this.antibiogram = antibiogram;
        this.cups = cups;
        this.confidential = confidential;
        this.validateDate = validateDate;
        this.resultDate = resultDate;
        this.sampleCode = sampleCode;
        this.sampleName = sampleName;
        this.comment = comment;
        this.units = units;
        this.patological = patological;
        this.methodCode = methodCode;
        this.methodName = methodName;
        this.normalMin = normalMin;
        this.normalMax = normalMax;
        this.panicMin = panicMin;
        this.panicMax = panicMax;
        this.refText = refText;
        this.refLiteral = refLiteral;
        this.decimalDigits = decimalDigits;
        this.resultType = resultType;
        this.his = his;
    }

    public ResponseOrderDetailResult(ResponseOrderDetailResult detail)
    {
        this.sectionCode = detail.getSectionCode();
        this.sectionName = detail.getSectionName();
        this.userCode = detail.getUserCode();
        this.userName = detail.getUserName();
        this.userLastName = detail.getUserLastName();
        this.testId = detail.getTestId();
        this.testCode = detail.getTestCode();
        this.testName = detail.getTestName();
        this.testResult = detail.getTestResult();
        this.profileId = detail.getProfileId();
        this.profileName = detail.getProfileName();
        this.profileCups = detail.getProfileCups();
        this.interfaceTestID = detail.getInterfaceTestID();
        this.antibiogram = detail.getAntibiogram();
        this.cups = detail.getCups();
        this.confidential = detail.getConfidential();
        this.validateDate = detail.getValidateDate();
        this.resultDate = detail.getResultDate();
        this.sampleCode = detail.getSampleCode();
        this.sampleName = detail.getSampleName();
        this.comment = detail.getComment();
        this.units = detail.getUnits();
        this.patological = detail.getPatological();
        this.methodCode = detail.getMethodCode();
        this.methodName = detail.getMethodName();
        this.normalMin = detail.getNormalMin();
        this.normalMax = detail.getNormalMax();
        this.panicMin = detail.getPanicMin();
        this.panicMax = detail.getPanicMax();
        this.refText = detail.getRefLiteral();
        this.refLiteral = detail.getRefLiteral();
        this.decimalDigits = detail.getDecimalDigits();
        this.resultType = detail.getResultType();
        this.his = detail.getHis();
        this.userNickName = detail.getUserNickName();
        this.validateDateFormat = detail.getValidateDateFormat();
        this.resultDateFormat = detail.getResultDateFormat();
        this.dateTaken = detail.getDateTaken();
        this.correction = detail.getCorrection();
        this.stateResult = detail.getStateResult();
        this.recount = detail.getRecount();
        this.determinations = detail.getDeterminations();
        this.complementations = detail.getComplementations();

    }

}
