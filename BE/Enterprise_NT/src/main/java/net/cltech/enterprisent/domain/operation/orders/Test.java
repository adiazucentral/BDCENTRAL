package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.TestNT;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.LiteralResult;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.Technique;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.Unit;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.list.TestBranchCheck;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que represent un resultado
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 5/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Examen de Laboratorio",
        description = "Representa un Examen"
)
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Test
{

    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "area", description = "Area ", required = true, order = 2)
    private Area area;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 3)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura", required = true, order = 4)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 5)
    private String name;
    @ApiObjectField(name = "state", description = "Estado", required = true, order = 40)
    private boolean state;
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = true, order = 6)
    private Short testType;
    @ApiObjectField(name = "unit", description = "Unidad", required = false, order = 7)
    private Unit unit;
    @ApiObjectField(name = "sample", description = "Muestra", required = true, order = 8)
    private Sample sample;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 9)
    private Result result;
    @ApiObjectField(name = "panel", description = "Perfil del examen", required = true, order = 10)
    private Test panel;
    @ApiObjectField(name = "pack", description = "Paquete", required = true, order = 11)
    private Test pack;
    @ApiObjectField(name = "tests", description = "Lista de examenes para perfiles /paquetes", required = true, order = 12)
    private List<Test> tests;
    @ApiObjectField(name = "confidential", description = "Identifica si es exámen confidencial", required = true, order = 12)
    private boolean confidential;
    @ApiObjectField(name = "laboratory", description = "Laboratorio de Referencia", required = false, order = 9)
    private Laboratory laboratory;
    @ApiObjectField(name = "laboratoryOrigin", description = "Laboratorio de Origen", required = false, order = 10)
    private String laboratoryOrigin;
    @ApiObjectField(name = "worklist", description = "Hoja de trabajo", required = false, order = 11)
    private Worklist worklist;
    @ApiObjectField(name = "lastResult", description = "Ultimo Resultado", required = false, order = 12)
    private Result lastResult;
    @ApiObjectField(name = "secondLastResult", description = "Segundo Ultimo Resultado", required = false, order = 12)
    private Result secondLastResult;
    @ApiObjectField(name = "selected", description = "Identifica esta seleccionado", required = true, order = 16)
    private boolean selected;
    @ApiObjectField(name = "reference", description = "Valores de referencia", required = false, order = 17)
    private ReferenceValue reference;
    @ApiObjectField(name = "technique", description = "Tecnica", required = false, order = 18)
    private Technique technique;
    @ApiObjectField(name = "print", description = "Si se imprime el examen", required = true, order = 19)
    private int print;
    @ApiObjectField(name = "historicGraphic", description = "Se muestran grafica de historicos en el reporte", required = true, order = 19)
    private boolean historicGraphic;
    @ApiObjectField(name = "volume", description = "Volumen de la prueba", required = true, order = 18)
    private String volume;
    @ApiObjectField(name = "gender", description = "Genero de la prueba", required = true, order = 9)
    private Item gender;
    @ApiObjectField(name = "minAge", description = "Edad minima de la prueba", required = true, order = 10)
    private Integer minAge;
    @ApiObjectField(name = "maxAge", description = "Edad maxima de la prueba", required = true, order = 11)
    private Integer maxAge;
    @ApiObjectField(name = "unitAge", description = "Unidad edad de la prueba", required = true, order = 12)
    private Short unitAge;
    @ApiObjectField(name = "testState", description = "Estado del examen para la orden Estado: 0 -> Ordenado, 1 -> Repeticion, 2 -> Reportado, 3 -> Preliminar, 4 -> Validado, 5 -> Impreso.", required = true, order = 13)
    private int testState;
    @ApiObjectField(name = "sampleState", description = "Estado Muestra:  0 -> Rechazado, 1 -> Nueva Muestra, 2 -> Ordenado, 3 -> Tomado, 4 -> Verificado.", required = true, order = 14)
    private int sampleState;
    @ApiObjectField(name = "rate", description = "Tarifa", required = true, order = 15)
    private Rate rate;
    @ApiObjectField(name = "price", description = "Precio", required = true, order = 16)
    private BigDecimal price;
    @ApiObjectField(name = "literalResults", description = "Lista de resultados literales", required = false, order = 17)
    private List<String> literalResults;
    @ApiObjectField(name = "resultType", description = "Tipo de Resultado: 1->Numerico, 2->Texto", required = false, order = 18)
    private Integer resultType;
    @ApiObjectField(name = "decimals", description = "Decimales del resultado", required = false, order = 19)
    private Integer decimals;
    @ApiObjectField(name = "literalResultsLiteral", description = "Lista de resultados literales como objeto", required = false, order = 20)
    private List<LiteralResult> literalResultsLiteral;
    @ApiObjectField(name = "Diagnostic", description = "Id de los diagnosticos  ", required = false, order = 21)
    private Integer Diagnostic;
    @ApiObjectField(name = "action", description = "Accion de examen", required = true, order = 22)
    private String action;
    @ApiObjectField(name = "service", description = "Servicio del examen", required = true, order = 23)
    private String service;
    @ApiObjectField(name = "sampleIdMicro", description = "Muesta microbiologia", required = true, order = 24)
    private String sampleIdMicro;
    @ApiObjectField(name = "anatomicalSite", description = "Sitio anatomico", required = true, order = 25)
    private String anatomicalSite;
    @ApiObjectField(name = "rackStore", description = "Nombre de la gradilla donde se almaceno", required = false, order = 37)
    private String rackStore;
    @ApiObjectField(name = "positionStore", description = "posicion donde fue almacenada", required = false, order = 38)
    private String positionStore;
    @ApiObjectField(name = "testCheckByBranch", description = "sede del test", required = false, order = 39)
    private List<TestBranchCheck> testCheckByBranch;
    @ApiObjectField(name = "dependentTest", description = "Examenes dependientes", required = true, order = 40)
    private Integer dependentTest;
    @ApiObjectField(name = "profile", description = "perfil", required = true, order = 41)
    private Integer profile;
    @ApiObjectField(name = "licuota", description = "Indica si el examen es una licuota. 1 -> Si, 0 -> No", required = true, order = 42)
    private boolean licuota;
    @ApiObjectField(name = "automaticResult", description = "Resultado automatico de la prueba", required = true, order = 19)
    private String automaticResult;
    @ApiObjectField(name = "patientPercentage", description = "Porcentaje del paciente", required = true, order = 20)
    private BigDecimal patientPercentage;
    @ApiObjectField(name = "priceDetail", description = "Detalle de Precios del examen", required = true, order = 21)
    private TestPrice priceDetail;
    @ApiObjectField(name = "fixedComment", description = "Comentario fijo", required = true, order = 20)
    private String fixedComment;
    @ApiObjectField(name = "excluideTestProfile", description = "Excluir analistos del perfil", required = true, order = 21)
    private int excluideTestProfile;
    @ApiObjectField(name = "discount", description = "Descuento", required = true, order = 22)
    private BigDecimal discount;
    @ApiObjectField(name = "remission", description = "Remision", required = true, order = 23)
    private int remission;
    @ApiObjectField(name = "originRemission", description = "Laboratorio de Orgien Remisiones", required = false, order = 24)
    private Laboratory originRemission;
    @ApiObjectField(name = "profileId", description = "Identificador del perfil", required = true, order = 83)
    private int profileId;
    @ApiObjectField(name = "temperatureTest", description = "Indica si el examen debe contener una temperatura", required = false, order = 62)
    private Integer temperatureTest;
    @ApiObjectField(name = "deliveryDays", description = "Dias de Entrega", required = true, order = 40)
    private Integer deliveryDays;
    @ApiObjectField(name = "ProccessDays", description = "Dias de Procesamiento", required = true, order = 41)
    private String ProccessDays;
    @ApiObjectField(name = "excludeHoliday", description = "Excluir dias dias festivos de dias de procesamiento", required = true, order = 36)
    private boolean excludeHoliday;
    @ApiObjectField(name = "tentativeDeliveryDate", description = "Fecha estimada de entrega", required = false, order = 9)
    private Date tentativeDeliveryDate;
    @ApiObjectField(name = "showInQuery", description = "Ver en consulta", required = true, order = 25)
    private boolean showInQuery;
    @ApiObjectField(name = "worksheet", description = "Indica si el examen esta incluido en una hoja de trabajo", required = true, order = 26)
    private boolean worksheet;
    @ApiObjectField(name = "complexityLevel", description = "Indica el nivel de complejidad del examen", required = true, order = 26)
    private String complexityLevel;
    @ApiObjectField(name = "codeCups", description = "Indica el codigo cups del examen", required = true, order = 26)
    private String codeCups;
    @ApiObjectField(name = "nameStadistic", description = "Nombre Estadistico del examen", required = true, order = 26)
    private String nameStadistic;
    @ApiObjectField(name = "account", description = "Cuenta", required = false, order = 8)
    private Account account = new Account();
    @ApiObjectField(name = "printComment", description = "Comentario al imprimir de la prueba", required = true, order = 38)
    private String printComment;
    @ApiObjectField(name = "motive", description = "Motivo de marca pendiente", required = false, order = 39)
    private Motive motive;
    @ApiObjectField(name = "userpending", description = "Usuario marcado pendiente", required = false, order = 40)
    private User userpending;
    @ApiObjectField(name = "hasprofile", description = "tiene perfil", required = false, order = 41)
    private Boolean hasprofile;
    @ApiObjectField(name = "cpt", description = "Còdigo CPT", required = true, order = 40)
    private String cpt;
     @ApiObjectField(name = "commentResult", description = "Comentario al imprimir de la prueba 2", required = true, order = 38)
    private String commentResult;

    public Test()
    {
        area = new Area();
        unit = new Unit();
        sample = new Sample();
        result = new Result();
        laboratory = new Laboratory();
        literalResults = new ArrayList<>(0);
        originRemission = new Laboratory();
        motive = new Motive();
        userpending = new User();
    }

    public Test(TestNT testNT)
    {
        this.code = testNT.getCode();
        this.name = testNT.getName();
        this.action = testNT.getActionCode();

    }

    public Test(TestBasic testBasic)
    {
        this.id = testBasic.getId();
        this.code = testBasic.getCode();
        this.abbr = testBasic.getAbbr();
        this.name = testBasic.getName();
    }

    public Test(Integer id)
    {
        this.id = id;
        area = new Area();
        unit = new Unit();
        sample = new Sample();
        result = new Result();
        laboratory = new Laboratory();
    }

    public Test(String code, String name, String actionCode)
    {
        this.code = code;
        this.name = name;
        this.action = action;
    }

    public Test setAction(String action)
    {
        this.action = action;
        return this;
    }

    public Test setLaboratoryOrigin(String laboratoryOrigin)
    {
        this.laboratoryOrigin = laboratoryOrigin;
        return this;
    }

    public Test setSelected(boolean selected)
    {
        this.selected = selected;
        return this;
    }

    public Test setTestType(Short testType)
    {
        this.testType = testType;
        return this;
    }

    public Test setWorksheet(boolean worksheet)
    {
        this.worksheet = worksheet;
        return this;
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
        final Test other = (Test) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}
