package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Pruebas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Prueba",
        description = "Muestra informacion del maestro Pruebas que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Test extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "area", description = "Area de la prueba", required = true, order = 2)
    private Area area;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 3)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 5)
    private String abbr;
    @ApiObjectField(name = "level", description = "Nivel de complejidad de la prueba", required = true, order = 6)
    private Item level;
    @ApiObjectField(name = "sample", description = "Muestra de la prueba", required = true, order = 7)
    private Sample sample;
    @ApiObjectField(name = "interview", description = "Entrevista de la prueba", required = true, order = 8)
    private Interview interview;
    @ApiObjectField(name = "gender", description = "Genero de la prueba", required = true, order = 9)
    private Item gender;
    @ApiObjectField(name = "minAge", description = "Edad minima de la prueba", required = true, order = 10)
    private Integer minAge;
    @ApiObjectField(name = "maxAge", description = "Edad maxima de la prueba", required = true, order = 11)
    private Integer maxAge;
    @ApiObjectField(name = "unitAge", description = "Unidad edad de la prueba", required = true, order = 12)
    private Short unitAge;
    @ApiObjectField(name = "formula", description = "Formula de la prueba", required = true, order = 13)
    private String formula;
    @ApiObjectField(name = "resultType", description = "Tipo de resultado de la prueba :  1 -> Numerico, 2 -> Texto", required = true, order = 14)
    private Short resultType;
    @ApiObjectField(name = "decimal", description = "Decimal de la prueba", required = true, order = 15)
    private Short decimal;
    @ApiObjectField(name = "technique", description = "Tecnica o metodo de la prueba", required = true, order = 16)
    private Technique technique;
    @ApiObjectField(name = "unit", description = "Unidad de la prueba", required = true, order = 17)
    private Unit unit;
    @ApiObjectField(name = "volume", description = "Volumen de la prueba", required = true, order = 18)
    private String volume;
    @ApiObjectField(name = "automaticResult", description = "Resultado automatico de la prueba", required = true, order = 19)
    private String automaticResult;
    @ApiObjectField(name = "maxDays", description = "Dias maximos para la modificacion despues de ser validada la prueba", required = true, order = 20)
    private Integer maxDays;
    @ApiObjectField(name = "deliveryDays", description = "Dias de entrega de la prueba", required = true, order = 21)
    private Integer deliveryDays;
    @ApiObjectField(name = "selfValidation", description = "Autovalidacion de la prueba: 1 -> Manualmente, 2 -> Valor de Referencia, 3 -> Delta Check, 4 -> Resultado Analizador, 5 -> Siempre", required = true, order = 22)
    private Integer selfValidation;
    @ApiObjectField(name = "statistics", description = "Estadisticas", required = true, order = 23)
    private boolean statistics;
    @ApiObjectField(name = "billing", description = "Facturacion", required = true, order = 24)
    private boolean billing;
    @ApiObjectField(name = "statisticalTitle", description = "Titulo estadistico de la prueba", required = true, order = 25)
    private String statisticalTitle;
    @ApiObjectField(name = "multiplyBy", description = "Multiplicar por", required = true, order = 26)
    private Integer multiplyBy;
    @ApiObjectField(name = "deleteProfile", description = "Eliminar de perfil", required = true, order = 27)
    private boolean deleteProfile;
    @ApiObjectField(name = "showEntry", description = "Ver en ingreso de ordenes", required = true, order = 28)
    private boolean showEntry;
    @ApiObjectField(name = "printOnReport", description = "Imprimir en informes: 1 -> Imprimir, 2 -> No Imprimir, 3 -> Imprimir en modificaciones", required = true, order = 29)
    private Short printOnReport;
    @ApiObjectField(name = "showInQuery", description = "Ver en consulta", required = true, order = 30)
    private boolean showInQuery;
    @ApiObjectField(name = "confidential", description = "Confidencial", required = true, order = 31)
    private boolean confidential;
    @ApiObjectField(name = "resultRequest", description = "Solicitar resultado en ingreso de ordenes", required = true, order = 32)
    private boolean resultRequest;
    @ApiObjectField(name = "printGraph", description = "Imprimir grafica", required = true, order = 33)
    private boolean printGraph;
    @ApiObjectField(name = "processingBy", description = "Procesamiento por: 1 -> Manualmente, 2 -> Analizador, 3 -> Externamente", required = true, order = 35)
    private Short processingBy;
    @ApiObjectField(name = "groupTitle", description = "Titulo de grupo de la prueba", required = true, order = 36)
    private String groupTitle;
    @ApiObjectField(name = "fixedComment", description = "Comentario fijo de la prueba", required = true, order = 37)
    private String fixedComment;
    @ApiObjectField(name = "printComment", description = "Comentario al imprimir de la prueba", required = true, order = 38)
    private String printComment;
    @ApiObjectField(name = "generalInformation", description = "Informacion general de la prueba", required = true, order = 39)
    private String generalInformation;
    @ApiObjectField(name = "state", description = "Estado", required = true, order = 40)
    private boolean state;
    @ApiObjectField(name = "processingDays", description = "Dias de procesamiento de la prueba", required = true, order = 41)
    private String processingDays;
    @ApiObjectField(name = "requirements", description = "Requerimientos de la prueba", required = true, order = 42)
    private List<Requirement> requirements;
    @ApiObjectField(name = "concurrences", description = "Concurrencias de la prueba", required = true, order = 43)
    private List<Concurrence> concurrences;
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = true, order = 44)
    private Short testType;
    @ApiObjectField(name = "dependentExam", description = "Examenes dependientes", required = true, order = 45)
    private boolean dependentExam;
    @ApiObjectField(name = "excludeAnalytes", description = "Excluir analitos en listado", required = true, order = 46)
    private boolean excludeAnalytes;
    @ApiObjectField(name = "statisticsProcessed", description = "Estadisticas de procesados", required = true, order = 47)
    private boolean statisticsProcessed;
    @ApiObjectField(name = "maxPrintDays", description = "Dias maximos para la modificacion despues de ser impresa la prueba", required = true, order = 48)
    private Integer maxPrintDays;
    @ApiObjectField(name = "printOrder", description = "Orden de impresión", required = true, order = 49)
    private Integer printOrder;
    @ApiObjectField(name = "conversionFactor", description = "Factor de conversión", required = true, order = 50)
    private Float conversionFactor;
    @ApiObjectField(name = "deltacheckDays", description = "Días para validación deltacheck", required = true, order = 51)
    private Integer deltacheckDays;
    @ApiObjectField(name = "deltacheckMin", description = "Porcentaje deltacheck por debajo", required = true, order = 52)
    private Float deltacheckMin;
    @ApiObjectField(name = "deltacheckMax", description = "Porcentaje deltacheck por encima", required = true, order = 53)
    private Float deltacheckMax;
    @ApiObjectField(name = "selected", description = "Esta seleccionado", required = true, order = 54)
    private boolean selected;
    @ApiObjectField(name = "basic", description = "Es Basico (Alerta temprana)", required = true, order = 55)
    private boolean basic;
    @ApiObjectField(name = "validResult", description = "Vigencia del resultado (Días)", required = true, order = 56)
    private Long validResult;
    @ApiObjectField(name = "preliminaryValidation", description = "Requiere validación preliminar", required = true, order = 57)
    private boolean preliminaryValidation;
    @ApiObjectField(name = "trendAlert", description = "Id de la alarma por tendencia", required = true, order = 58)
    private int trendAlert;
    @ApiObjectField(name = "informedConsent", description = "Consentimiento informado", required = true, order = 59)
    private boolean informedConsent;
    @ApiObjectField(name = "licuota", description = "Indica si el examen es una licuota. 1 -> Si, 0 -> No", required = true, order = 60)
    private boolean licuota;
    @ApiObjectField(name = "profileTest", description = "Examen del perfil", required = true, order = 61)
    private Integer profileTest;
    @ApiObjectField(name = "Diagnostic", description = "Id de los diagnosticos  ", required = false, order = 21)
    private Integer Diagnostic;
    @ApiObjectField(name = "Temperatura", description = "Indica si el examen debe contener una temperatura", required = false, order = 62)
    private Integer temperatureTest;
    @ApiObjectField(name = "excludeHoliday", description = "Excluir dias dias festivos de dias de procesamiento", required = true, order = 36)
    private boolean excludeHoliday;
    @ApiObjectField(name = "nameEnglish", description = "Nombre de la prueba en ingles", required = false, order = 63)
    private String nameEnglish;
    @ApiObjectField(name = "fixedCommentEn", description = "Comentario fijo de la prueba en ingles", required = true, order = 37)
    private String fixedCommentEn;
    @ApiObjectField(name = "printCommentEn", description = "Comentario al imprimir de la prueba en ingles", required = true, order = 38)
    private String printCommentEn;
    @ApiObjectField(name = "generalInformationEn", description = "Informacion general de la prueba en ingles", required = true, order = 39)
    private String generalInformationEn;
    @ApiObjectField(name = "cpt", description = "Còdigo CPT", required = true, order = 40)
    private String cpt;
    @ApiObjectField(name = "commentResult", description = "comentario del resultado", required = true, order = 41)
    private String commentResult;
    @ApiObjectField(name = "deployPackages", description = "Desplegar paquetes en talon", required = true, order = 42)
    private Integer deployPackages;
    @ApiObjectField(name = "sampleState", description = "Desplegar paquetes en talon", required = true, order = 42)
    private Integer sampleState;

    public Test()
    {
        area = new Area();
        level = new Item();
        sample = new Sample();
        gender = new Item();
        technique = new Technique();
        interview = new Interview();
        unit = new Unit();
        requirements = new ArrayList<>();
        concurrences = new ArrayList<>();
        licuota = false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Test other = (Test) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    public boolean isLicuota()
    {
        return licuota;
    }

    public void setLicuota(boolean licuota)
    {
        this.licuota = licuota;
    }

    public Integer getProfileTest()
    {
        return profileTest;
    }

    public void setProfileTest(Integer profileTest)
    {
        this.profileTest = profileTest;
    }

}
