package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.databank.TemplateDatabank;
import net.cltech.enterprisent.domain.integration.databank.TestCross;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobialDetection;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
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
        description = "Representa un examen para el registro de resultados"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ResultTest implements Cloneable
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
    @ApiObjectField(name = "sampleId", description = "Id del muestra", required = true, order = 6)
    private int sampleId;
    @ApiObjectField(name = "sampleCode", description = "Codigo del muestra", required = true, order = 7)
    private String sampleCode;
    @ApiObjectField(name = "sampleName", description = "Nombre del muestra", required = true, order = 8)
    private String sampleName;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 9)
    private int testId;
    @ApiObjectField(name = "testCode", description = "Código del examen", required = true, order = 10)
    private String testCode;
    @ApiObjectField(name = "result", description = "Resultado del examen", required = false, order = 11)
    private String result;
    @ApiObjectField(name = "resultencript", description = "Resultado del examen encriptado", required = false, order = 11)
    private String resultencript;
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
    private BigDecimal refMin;
    @ApiObjectField(name = "refMax", description = "Referecia máxima", required = false, order = 20)
    private BigDecimal refMax;
    @ApiObjectField(name = "refInterval", description = "Intervalo de referencia", required = false, order = 21)
    private String refInterval;
    @ApiObjectField(name = "panicMin", description = "Pánico mínimo", required = false, order = 22)
    private BigDecimal panicMin;
    @ApiObjectField(name = "panicMax", description = "Pánico máximo", required = false, order = 23)
    private BigDecimal panicMax;
    @ApiObjectField(name = "panicInterval", description = "Intervalo de pánico", required = false, order = 24)
    private String panicInterval;
    @ApiObjectField(name = "reportedMin", description = "Reportable mínimo", required = false, order = 25)
    private BigDecimal reportedMin;
    @ApiObjectField(name = "reportedMax", description = "Reportable máximo", required = false, order = 26)
    private BigDecimal reportedMax;
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
    @ApiObjectField(name = "resultComment", description = "Comentario del resultado", required = false, order = 34)
    private ResultTestComment resultComment;
    @ApiObjectField(name = "hasComment", description = "Indicador de resultado con comentario", required = true, order = 35)
    private Boolean hasComment;
    @ApiObjectField(name = "resultChanged", description = "Indicador de modificación del resultado", required = true, order = 36)
    private Boolean resultChanged;
    @ApiObjectField(name = "sampleState", description = "Estado de la muestra", required = true, order = 37)
    private int sampleState;
    @ApiObjectField(name = "confidential", description = "Indica si es confidencial", required = true, order = 38)
    private boolean confidential;
    @ApiObjectField(name = "repeatAmmount", description = "Cantidad de repeticiones", required = true, order = 39)
    private Integer repeatAmmount;
    @ApiObjectField(name = "modificationAmmount", description = "Cantidad de modificaciones", required = true, order = 40)
    private Integer modificationAmmount;
    @ApiObjectField(name = "testName", description = "Nombre del examen", required = true, order = 41)
    private String testName;
    @ApiObjectField(name = "orderPathology", description = "Patología general de la orden", required = true, order = 42)
    private int orderPathology;
    @ApiObjectField(name = "deltaMin", description = "Delta mínimo", required = false, order = 43)
    private BigDecimal deltaMin;
    @ApiObjectField(name = "deltaMax", description = "Delta máximo", required = false, order = 44)
    private BigDecimal deltaMax;
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
    @ApiObjectField(name = "validationUserIdentification", description = "Identificación del ususario que validó el exámen", required = true, order = 52)
    private String validationUserIdentification;
    @ApiObjectField(name = "validationUserName", description = "Nombre de usuario que validó el exámen", required = false, order = 53)
    private String validationUserName;
    @ApiObjectField(name = "validationUserLastName", description = "Apellidos de usuario que validó el exámen", required = false, order = 54)
    private String validationUserLastName;
    @ApiObjectField(name = "validationUserSignature", description = "Firma del usuario que valido el examen (Imagen)", order = 55)
    private String validationUserSignature;
    @ApiObjectField(name = "validationUserSignatureCode", description = "Codigo firma del usuario que valido el examen", order = 56)
    private String validationUserSignatureCode;  
    @ApiObjectField(name = "validationUserType", description = "tipo de usuario que valido", order = 56)
    private int validationUserType;
    @ApiObjectField(name = "entryDate", description = "Fecha de ingreso", required = false, order = 57)
    private Date entryDate;
    @ApiObjectField(name = "entryUserId", description = "Usuario que ingreso exámen", required = false, order = 57)
    private int entryUserId;
    @ApiObjectField(name = "takenDate", description = "Fecha de toma de la muestra", required = false, order = 57)
    private Date takenDate;
    @ApiObjectField(name = "takenUserId", description = "Usuario que tomó la muestra ", required = false, order = 57)
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
    private BigDecimal unitConversionFactor;
    @ApiObjectField(name = "microbiologyGrowth", description = "Siembra de Microbiologia", required = true, order = 64)
    private MicrobiologyGrowth microbiologyGrowth;
    @ApiObjectField(name = "resultRepetition", description = "Repetición de un resultado", required = false, order = 65)
    private ResultTestRepetition resultRepetition;
    @ApiObjectField(name = "laboratoryType", description = "Tipos de laboratorio de la muestra", required = false, order = 66)
    private String laboratoryType;
    @ApiObjectField(name = "entryType", description = "Tipo de entrada del resultado. Indica si es de forma automatizada.", required = false, order = 67)
    private short entryType;
    @ApiObjectField(name = "entryTestType", description = "Tipo de entrada del examen. Indica si el exámen fue ingresado directo(1) o es un cultivo(2).", required = false, order = 67)
    private Short entryTestType;
    @ApiObjectField(name = "reportTask", description = "Indicador si tiene tareas asociadas.", required = true, order = 68)
    private boolean reportTask;
    @ApiObjectField(name = "billingTest", description = "Precio y tarifa del examen", required = false, order = 69)
    private BillingTest billing;
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = false, order = 70)
    private int testType;
    @ApiObjectField(name = "entry", description = "Si fue un examen ingresado", required = false, order = 71)
    private boolean entry;
    @ApiObjectField(name = "formula", description = "Formula del examen", required = false, order = 72)
    private String formula;
    @ApiObjectField(name = "areaPartialValidation", description = "Validacion parcial", required = true, order = 73)
    private boolean areaPartialValidation;
    @ApiObjectField(name = "hasAntibiogram", description = "Indicador de resultado con antibiograma", required = true, order = 74)
    private Boolean hasAntibiogram;
    @ApiObjectField(name = "hasTemplate", description = "Indicador de resultado con platilla", required = true, order = 75)
    private Boolean hasTemplate;
    @ApiObjectField(name = "microbialDetection", description = "Detección microbiana", required = true, order = 76)
    private MicrobialDetection microbialDetection;
    @ApiObjectField(name = "optionsTemplate", description = "Plantilla de Resultados", required = true, order = 77)
    private List<OptionTemplate> optionsTemplate;
    @ApiObjectField(name = "block", description = "Indicador de bloqueo de la prueba", required = true, order = 78)
    private ResultTestBlock block;
    @ApiObjectField(name = "grantAccess", description = "Indicador de permiso sobre los exámenes del área", required = true, order = 79)
    private boolean grantAccess;
    @ApiObjectField(name = "grantValidate", description = "Indicador de permiso de validación sobre los exámenes del área", required = true, order = 80)
    private boolean grantValidate;
    @ApiObjectField(name = "storageDays", description = "Dias de almacenamiento de la muestra", required = true, order = 81)
    private Integer storageDays;
    @ApiObjectField(name = "tecnique", description = "Nombre de la tecnica", required = true, order = 82)
    private String technique;
    @ApiObjectField(name = "codetechnique", description = "Codigo de la tecnica", required = true, order = 82)
    private String codetechnique;
    @ApiObjectField(name = "profileId", description = "Identificador del perfil", required = true, order = 83)
    private int profileId;
    @ApiObjectField(name = "profileName", description = "Nombre del perfil", required = true, order = 84)
    private String profileName;
    @ApiObjectField(name = "printSortProfile", description = "Orden de impresión del perfil", required = true, order = 85)
    private Integer printSortProfile;
    @ApiObjectField(name = "printSort", description = "Orden de impresión", required = true, order = 85)
    private Integer printSort;
    @ApiObjectField(name = "groupTitle", description = "Titulo de grupo de la prueba", required = true, order = 86)
    private String groupTitle;
    @ApiObjectField(name = "delta", description = "Indicador de valor anormal por delta", required = true, order = 87)
    private Boolean delta;
    @ApiObjectField(name = "attachmentTest", description = "Cantidad adjuntos de la prueba", required = true, order = 88)
    private int attachmentTest;
    @ApiObjectField(name = "packageId", description = "Id del paquete ", required = true, order = 89)
    private int packageId;
    @ApiObjectField(name = "packageName", description = "Nombre del paquete", required = true, order = 84)
    private String packageName;
    @ApiObjectField(name = "validatedChilds", description = "# de hijos validados ", required = true, order = 89)
    private Integer validatedChilds;
    @ApiObjectField(name = "maxDays", description = "Dias maximos para la modificacion despues de ser validada la prueba", required = true, order = 90)
    private Integer maxDays;
    @ApiObjectField(name = "maxPrintDays", description = "Dias maximos para la modificacion despues de ser impresa la prueba", required = true, order = 91)
    private Integer maxPrintDays;
    @ApiObjectField(name = "resultRequest", description = "Solicitar resultado en ingreso de ordenes", required = false, order = 92)
    private boolean resultRequest;
    @ApiObjectField(name = "preliminaryValidation", description = "Requiere validación preliminar", required = false, order = 93)
    private boolean preliminaryValidation;
    @ApiObjectField(name = "preliminaryUser", description = "Usuario que prevalido", required = false, order = 94)
    private int preliminaryUser;
    @ApiObjectField(name = "preliminaryDate", description = "Fecha de prevalidacion", required = false, order = 95)
    private Date preliminaryDate;
    @ApiObjectField(name = "repeatedResultValue", description = "Valor del ultimo resultado repetido", required = false, order = 96)
    private String repeatedResultValue;
    @ApiObjectField(name = "Micro", description = "Si es o no de microbiologia ", order = 97)
    private boolean micro;
    @ApiObjectField(name = "rate", description = "Tarifa", required = true, order = 98)
    private Rate rate;
    @ApiObjectField(name = "sentCentralSystem", description = "0 - False No enviado al sistema central, 1 - True Enviado al sistema central", required = true, order = 99)
    private Integer sentCentralSystem;
    @ApiObjectField(name = "hasImage", description = "Tiene grafica de analizador", required = false, order = 100)
    private boolean hasImage;
    @ApiObjectField(name = "images", description = "Gráficas de analizador", required = false, order = 101)
    private List<ImageTest> images;
    @ApiObjectField(name = "refComment", description = "Comentario del valor de referencia", required = false, order = 28)
    private String refComment;
    @ApiObjectField(name = "refCommentEnglish", description = "Comentario del valor de referencia en ingles", required = false, order = 28)
    private String refCommentEnglish;
    @ApiObjectField(name = "dilution", description = "Dilución. 0 - No diluido, 1 - Diluido", required = false, order = 102)
    private boolean dilution;
    @ApiObjectField(name = "fixedComment", description = "Comentario fijo de la prueba", required = true, order = 37)
    private String fixedComment;
    @ApiObjectField(name = "testCrossArrayList", description = "Lista de pruebas cruzadas enviadas desde databank", required = true, order = 38)
    private List<TestCross> testCrossArrayList;
    @ApiObjectField(name = "print", description = "Impresion en informe de resultados", required = true, order = 39)
    private Integer print;
    @ApiObjectField(name = "deliveryDays", description = "Dias de Entrega", required = true, order = 40)
    private Integer deliveryDays;
    @ApiObjectField(name = "ProccessDays", description = "Dias de Procesamiento", required = true, order = 41)
    private String ProccessDays;
    @ApiObjectField(name = "patient", description = "Paciente de la orden", required = true, order = 42)
    private Patient patient = new Patient();
    @ApiObjectField(name = "laboratoryId", description = "Id del laboratorio", required = true, order = 43)
    private int laboratoryId;
    @ApiObjectField(name = "homologationCodes", description = "Códigos de homologación", required = true, order = 44)
    private List<String> homologationCodes;
    @ApiObjectField(name = "serviceId", description = "Id del servicio", required = true, order = 45)
    private int serviceId;
    @ApiObjectField(name = "service", description = "Servicio", required = true, order = 46)
    private String service;
    @ApiObjectField(name = "reportedDoctor", description = "Fecha reporte al doctor", required = false, order = 47)
    private Date reportedDoctor;
    @ApiObjectField(name = "nameAreaEnglish", description = "Nombre del area en ingles", required = false, order = 48)
    private String nameAreaEnglish;
    @ApiObjectField(name = "nameTestEnglish", description = "Nombre del examen en ingles", required = false, order = 49)
    private String nameTestEnglish;
    @ApiObjectField(name = "fixedCommentEnglish", description = "Comentario fijo de la prueba en ingles", required = true, order = 50)
    private String fixedCommentEnglish;
    @ApiObjectField(name = "printComment", description = "Comentario a imprimir de la prueba", required = true, order = 51)
    private String printComment;
    @ApiObjectField(name = "templateComment", description = "Comentario de la plantilla de resultados prueba", required = true, order = 51)
    private String templateComment;
    @ApiObjectField(name = "printCommentEnglish", description = "Comentario preliminar de la prueba en ingles", required = true, order = 51)
    private String printCommentEnglish;
    @ApiObjectField(name = "generalInformationEnglish", description = "Comentario general de la prueba en ingles", required = true, order = 52)
    private String generalInformationEnglish;
    @ApiObjectField(name = "userRetake", description = "Usuario que realiza la Retoma", required = true, order = 53)
    private int userRetake;
    @ApiObjectField(name = "applyInterview", description = "Indica si al examen se aplica la entrevista de panico", required = true, order = 54)
    private int applyInterview;
    @ApiObjectField(name = "templates", description = "Lista de plantillas de la prueba enviada desde databank", required = true, order = 55)
    private List<TemplateDatabank> templates;
    @ApiObjectField(name = "hasCommentInternal", description = "Indica si el examen tiene comentario interno", required = true, order = 56)
    private boolean hasCommentInternal;
    @ApiObjectField(name = "dependentExam", description = "Examenes dependientes", required = true, order = 57)
    private boolean dependentExam;
    @ApiObjectField(name = "homologationCode", description = "Codigo de homologacion asignado", required = true, order = 58)
    private String homologationCode;
    @ApiObjectField(name = "previousResult", description = "Resultado anterior", required = true, order = 59)
    private String previousResult;
    @ApiObjectField(name = "printsample", description = "indicador si el codigo de barras ya fue impreso", required = true, order = 60)
    private boolean printsample;
    @ApiObjectField(name = "determinations", description = "Comentario de determinaciones", required = false, order = 61)
    private String determinations;
    @ApiObjectField(name = "user", description = "Usuario de validacion DATABANK", required = false, order = 62)
    private AuthorizedUser user;
    @ApiObjectField(name = "usertest", description = "Usuario de validacion DATABANK para pruebas cruzadas", required = false, order = 62)
    private AuthorizedUser usertest;
    @ApiObjectField(name = "resultEnglish", description = "Resultado del examen en ingles", required = false, order = 63)
    private String resultEnglish;
    @ApiObjectField(name = "packageNameEnglish", description = "Nombre del paquete en ingles", required = true, order = 64)
    private String packageNameEnglish;
    @ApiObjectField(name = "profileNameEnglish", description = "Nombre del perfil en ingles", required = true, order = 65)
    private String profileNameEnglish;
    @ApiObjectField(name = "homologationProfile", description = "Codigo de homologacion asignado al perfil", required = true, order = 66)
    private String homologationProfile;
    @ApiObjectField(name = "commentResult", description = "Comentario resultado 2", required = true, order = 64)
    private String commentResult;
    @ApiObjectField(name = "laboratoryCode", description = "Código del laboratorio", required = true, order = 43)
    private String laboratoryCode;
    @ApiObjectField(name = "laboratoryName", description = "Nombre del laboratorio", required = true, order = 43)
    private String laboratoryName;
    @ApiObjectField(name = "remission", description = "indicador si el examen fue remitido", required = true, order = 60)
    private boolean remission;
    @ApiObjectField(name = "deleteProfile", description = "Eliminar de perfil", required = true, order = 27)
    private Integer deleteProfile;
    
    public ResultTest()
    {
        testCrossArrayList = new ArrayList<>();
        templates = new ArrayList<>();
        resultComment = new ResultTestComment();
        user = new AuthorizedUser();
    }

    public ResultTest setValidatedChilds(Integer validatedChilds)
    {
        this.validatedChilds = validatedChilds;
        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

}
