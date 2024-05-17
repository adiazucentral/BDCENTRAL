/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.resultados;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Examen cabecera
 * 
 * @version 1.0.0
 * @author omendez
 * @since 17/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Detalle de los examenes integración interfaz de resultados",
        description = "Detalle de los examenes integración interfaz de resultados"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class TestDetail {
    
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
    @ApiObjectField(name = "resultDate", description = "Fecha del resultado del examen", required = false, order = 12)
    private String resultDate;
    @ApiObjectField(name = "resultUserId", description = "Usuario que ha reportado el resultado", required = false, order = 13)
    private int resultUserId;
    @ApiObjectField(name = "state", description = "Estado del examen", required = true, order = 14)
    private int state;
    @ApiObjectField(name = "resultType", description = "Tipo de resultado", required = false, order = 17)
    private short resultType;
    @ApiObjectField(name = "pathology", description = "Patología del examen con respecto al valor de referencia", required = true, order = 18)
    private int pathology;
    @ApiObjectField(name = "refMin", description = "Referencia mínima", required = false, order = 19)
    private BigDecimal refMin;
    @ApiObjectField(name = "refMin", description = "Referencia mínima", required = false, order = 19)
    private String refMinT;
    @ApiObjectField(name = "refMax", description = "Referecia máxima", required = false, order = 20)
    private BigDecimal refMax;
    @ApiObjectField(name = "refMax", description = "Referecia máxima", required = false, order = 20)
    private String refMaxT;
    @ApiObjectField(name = "refInterval", description = "Intervalo de referencia", required = false, order = 21)
    private String refInterval;
    @ApiObjectField(name = "panicMin", description = "Pánico mínimo", required = false, order = 22)
    private BigDecimal panicMin;
    @ApiObjectField(name = "panicMinT", description = "Pánico mínimo", required = false, order = 22)
    private String panicMinT;
    @ApiObjectField(name = "panicMax", description = "Pánico máximo", required = false, order = 23)
    private BigDecimal panicMax;
    @ApiObjectField(name = "panicMaxT", description = "Pánico máximo", required = false, order = 23)
    private String panicMaxT;
    @ApiObjectField(name = "panicInterval", description = "Intervalo de pánico", required = false, order = 24)
    private String panicInterval;
    @ApiObjectField(name = "reportedMin", description = "Reportable mínimo", required = false, order = 25)
    private BigDecimal reportedMin;
    @ApiObjectField(name = "reportedMinT", description = "Reportable mínimo", required = false, order = 25)
    private String reportedMinT;
    @ApiObjectField(name = "reportedMax", description = "Reportable máximo", required = false, order = 26)
    private BigDecimal reportedMax;
    @ApiObjectField(name = "reportedMaxt", description = "Reportable máximo", required = false, order = 26)
    private String reportedMaxT;
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
    @ApiObjectField(name = "deltaMinT", description = "Delta mínimo", required = false, order = 43)
    private String deltaMinT;
    @ApiObjectField(name = "deltaMax", description = "Delta máximo", required = false, order = 44)
    private BigDecimal deltaMax;
    @ApiObjectField(name = "deltaMaxT", description = "Delta máximo", required = false, order = 44)
    private String deltaMaxT;
    @ApiObjectField(name = "deltaInterval", description = "Intervalo de delta", required = false, order = 45)
    private String deltaInterval;
    @ApiObjectField(name = "lastResult", description = "Último resultado", required = false, order = 46)
    private String lastResult;
    @ApiObjectField(name = "lastResultDate", description = "Fecha último resultado", required = false, order = 47)
    private String lastResultDate;
    @ApiObjectField(name = "secondLastResult", description = "Penúltimo resultado", required = false, order = 48)
    private String secondLastResult;
    @ApiObjectField(name = "secondLastResultDate", description = "Fecha penúltimo resultado", required = false, order = 49)
    private String secondLastResultDate;
    @ApiObjectField(name = "validationDate", description = "Fecha de validación", required = false, order = 51)
    private String validationDate;
    @ApiObjectField(name = "validationUserId", description = "Usuario que validó el exámen", required = false, order = 52)
    private Integer validationUserId;
    @ApiObjectField(name = "validationUserIdentification", description = "Identificación del ususario que validó el exámen", required = true, order = 52)
    private String validationUserIdentification;
    @ApiObjectField(name = "validationUserName", description = "Nombre de usuario que validó el exámen", required = false, order = 53)
    private String validationUserName;
    @ApiObjectField(name = "validationUserLastName", description = "Apellidos de usuario que validó el exámen", required = false, order = 54)
    private String validationUserLastName; 
    @ApiObjectField(name = "entryDate", description = "Fecha de ingreso", required = false, order = 57)
    private String entryDate;
    @ApiObjectField(name = "entryUserId", description = "Usuario que ingreso exámen", required = false, order = 57)
    private int entryUserId;
    @ApiObjectField(name = "takenDate", description = "Fecha de toma de la muestra", required = false, order = 57)
    private String takenDate;
    @ApiObjectField(name = "takenUserId", description = "Usuario que tomó la muestra ", required = false, order = 57)
    private Integer takenUserId;
    @ApiObjectField(name = "verificationDate", description = "Fecha de verificación", required = false, order = 57)
    private String verificationDate;
    @ApiObjectField(name = "verificationUserId", description = "Usuario que verifica", required = false, order = 58)
    private Integer verificationUserId;
    @ApiObjectField(name = "printDate", description = "Fecha de impresion", required = false, order = 59)
    private String printDate;
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
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = false, order = 70)
    private int testType;
    @ApiObjectField(name = "formula", description = "Formula del examen", required = false, order = 72)
    private String formula;
    @ApiObjectField(name = "areaPartialValidation", description = "Validacion parcial", required = true, order = 73)
    private boolean areaPartialValidation;
    @ApiObjectField(name = "hasAntibiogram", description = "Indicador de resultado con antibiograma", required = true, order = 74)
    private Boolean hasAntibiogram;
    @ApiObjectField(name = "hasTemplate", description = "Indicador de resultado con platilla", required = true, order = 75)
    private Boolean hasTemplate;
    @ApiObjectField(name = "optionsTemplate", description = "Plantilla de Resultados", required = true, order = 77)
    private List<OptionTemplate> optionsTemplate;
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
    @ApiObjectField(name = "delta", description = "Indicador de valor anormal por delta", required = true, order = 87)
    private Boolean delta;
    @ApiObjectField(name = "packageId", description = "Id del paquete ", required = true, order = 89)
    private int packageId;
    @ApiObjectField(name = "packageName", description = "Nombre del paquete", required = true, order = 84)
    private String packageName;
    @ApiObjectField(name = "preliminaryValidation", description = "Requiere validación preliminar", required = false, order = 93)
    private boolean preliminaryValidation;
    @ApiObjectField(name = "preliminaryUser", description = "Usuario que prevalido", required = false, order = 94)
    private int preliminaryUser;
    @ApiObjectField(name = "preliminaryDate", description = "Fecha de prevalidacion", required = false, order = 95)
    private String preliminaryDate;
    @ApiObjectField(name = "repeatedResultValue", description = "Valor del ultimo resultado repetido", required = false, order = 96)
    private String repeatedResultValue;
    @ApiObjectField(name = "Micro", description = "Si es o no de microbiologia ", order = 97)
    private boolean micro;
    @ApiObjectField(name = "refComment", description = "Comentario del valor de referencia", required = false, order = 28)
    private String refComment;
    @ApiObjectField(name = "dilution", description = "Dilución. 0 - No diluido, 1 - Diluido", required = false, order = 102)
    private boolean dilution;
    @ApiObjectField(name = "fixedComment", description = "Comentario fijo de la prueba", required = true, order = 37)
    private String fixedComment;
    @ApiObjectField(name = "laboratoryId", description = "Id del laboratorio", required = true, order = 43)
    private int laboratoryId;
    @ApiObjectField(name = "nameAreaEnglish", description = "Nombre del area en ingles", required = false, order = 48)
    private String nameAreaEnglish;
    @ApiObjectField(name = "nameTestEnglish", description = "Nombre del examen en ingles", required = false, order = 49)
    private String nameTestEnglish;
    @ApiObjectField(name = "fixedCommentEnglish", description = "Comentario fijo de la prueba en ingles", required = true, order = 50)
    private String fixedCommentEnglish;
    @ApiObjectField(name = "printComment", description = "Comentario a imprimir de la prueba", required = true, order = 51)
    private String printComment;
    @ApiObjectField(name = "printCommentEnglish", description = "Comentario preliminar de la prueba en ingles", required = true, order = 51)
    private String printCommentEnglish;
    @ApiObjectField(name = "generalInformationEnglish", description = "Comentario general de la prueba en ingles", required = true, order = 52)
    private String generalInformationEnglish;
    @ApiObjectField(name = "userRetake", description = "Usuario que realiza la Retoma", required = true, order = 53)
    private int userRetake;
    @ApiObjectField(name = "homologationCode", description = "Codigo de homologacion asignado", required = true, order = 58)
    private String homologationCode;
    @ApiObjectField(name = "previousResult", description = "Resultado anterior", required = true, order = 59)
    private String previousResult;
    @ApiObjectField(name = "resultEnglish", description = "Resultado del examen en ingles", required = false, order = 63)
    private String resultEnglish;
    @ApiObjectField(name = "packageNameEnglish", description = "Nombre del paquete en ingles", required = true, order = 64)
    private String packageNameEnglish;
    @ApiObjectField(name = "profileNameEnglish", description = "Nombre del perfil en ingles", required = true, order = 65)
    private String profileNameEnglish;
    @ApiObjectField(name = "homologationProfile", description = "Codigo de homologacion asignado al perfil", required = true, order = 66)
    private String homologationProfile;
    @ApiObjectField(name = "microorganism", description = "Nombre del microorganismo", required = true, order = 1)
    private String microorganism;
    @ApiObjectField(name = "antibiotic", description = "Nombre del antibiotico", required = true, order = 2)
    private String antibiotic;
    @ApiObjectField(name = "interpretation", description = "Interpretacion", required = true, order = 3)
    private String interpretation;
    @ApiObjectField(name = "antibiogram", description = "Nombre del antibiograma", required = true, order = 4)
    private String antibiogram;
    @ApiObjectField(name = "microbialDetectionComment", description = "Comentario de deteccion microbiana", required = true, order = 5)
    private String microbialDetectionComment;
    @ApiObjectField(name = "cmi", description = "Resultado del antibiotico", required = true, order = 6)
    private String cmi;
    @ApiObjectField(name = "microorganismId", description = "Id del microorganismo", required = true, order = 7)
    private int microorganismId;
    
}
