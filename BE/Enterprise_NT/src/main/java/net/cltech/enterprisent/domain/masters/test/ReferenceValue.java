package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Valores de Referencia
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Valor de Referencia",
        description = "Muestra informacion del maestro Valores de Referencia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceValue extends MasterAudit {

    @ApiObjectField(name = "id", description = "Id del valor de referencia", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "test", description = "Prueba del valor de referencia", required = true, order = 2)
    private TestBasic test;
    @ApiObjectField(name = "unitAge", description = "Unidad de edad del valor de referencia", required = true, order = 3)
    private Short unitAge;
    @ApiObjectField(name = "ageMin", description = "Edad minima", required = true, order = 4)
    private Integer ageMin;
    @ApiObjectField(name = "ageMax", description = "Edad maxima", required = true, order = 5)
    private Integer ageMax;
    @ApiObjectField(name = "normalMin", description = "Normal minimo", required = true, order = 6)
    private BigDecimal normalMin;
    @ApiObjectField(name = "normalMax", description = "Normal maximo", required = true, order = 7)
    private BigDecimal normalMax;
    @ApiObjectField(name = "panicMin", description = "Panico minimo", required = true, order = 8)
    private BigDecimal panicMin;
    @ApiObjectField(name = "panicMax", description = "Panico maximo", required = true, order = 9)
    private BigDecimal panicMax;
    @ApiObjectField(name = "reportableMin", description = "Reportable minimo", required = true, order = 12)
    private BigDecimal reportableMin;
    @ApiObjectField(name = "reportableMax", description = "Reportable maximo", required = true, order = 13)
    private BigDecimal reportableMax;
    @ApiObjectField(name = "comment", description = "Comentario del valor de referencia", required = true, order = 14)
    private String comment;
    @ApiObjectField(name = "commentEnglish", description = "Comentario del valor de referencia en ingles", required = true, order = 14)
    private String commentEnglish;
    @ApiObjectField(name = "race", description = "Raza del valor de referencia", required = true, order = 15)
    private Race race;
    @ApiObjectField(name = "gender", description = "Genero del valor de referencia", required = true, order = 16)
    private Item gender;
    @ApiObjectField(name = "panic", description = "Panico Resultado Literal", required = true, order = 17)
    private LiteralResult panic;
    @ApiObjectField(name = "normal", description = "Normal Resultado Literal", required = true, order = 19)
    private LiteralResult normal;
    @ApiObjectField(name = "criticalCh", description = "Es Critico", required = true, order = 20)
    private boolean criticalCh;
    @ApiObjectField(name = "state", description = "Estado 0 -> Desactivado 1 -> activado ", required = true, order = 20)
    private boolean state;
    @ApiObjectField(name = "mandatoryNotation", description = "Notación Obligatoria", required = true, order = 59)
    private boolean mandatoryNotation;
    @ApiObjectField(name = "analizerUserId", description = "Edad minima", required = false, order = 60)
    private Integer analizerUserId;

    public ReferenceValue() {
        test = new TestBasic();
        gender = new Item();
        race = new Race();
        panic = new LiteralResult();
        normal = new LiteralResult();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TestBasic getTest() {
        return test;
    }

    public void setTest(TestBasic test) {
        this.test = test;
    }

    public Short getUnitAge() {
        return unitAge;
    }

    public void setUnitAge(Short unitAge) {
        this.unitAge = unitAge;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public BigDecimal getNormalMin() {
        return normalMin;
    }

    public void setNormalMin(BigDecimal normalMin) {
        this.normalMin = normalMin;
    }

    public BigDecimal getNormalMax() {
        return normalMax;
    }

    public void setNormalMax(BigDecimal normalMax) {
        this.normalMax = normalMax;
    }

    public BigDecimal getPanicMin() {
        return panicMin;
    }

    public void setPanicMin(BigDecimal panicMin) {
        this.panicMin = panicMin;
    }

    public BigDecimal getPanicMax() {
        return panicMax;
    }

    public void setPanicMax(BigDecimal panicMax) {
        this.panicMax = panicMax;
    }

    public BigDecimal getReportableMin() {
        return reportableMin;
    }

    public void setReportableMin(BigDecimal reportableMin) {
        this.reportableMin = reportableMin;
    }

    public BigDecimal getReportableMax() {
        return reportableMax;
    }

    public void setReportableMax(BigDecimal reportableMax) {
        this.reportableMax = reportableMax;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentEnglish() {
        return commentEnglish;
    }

    public void setCommentEnglish(String commentEnglish) {
        this.commentEnglish = commentEnglish;
    }
    
    

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Item getGender() {
        return gender;
    }

    public void setGender(Item gender) {
        this.gender = gender;
    }

    public LiteralResult getPanic() {
        return panic;
    }

    public void setPanic(LiteralResult panic) {
        this.panic = panic;
    }

    public LiteralResult getNormal() {
        return normal;
    }

    public void setNormal(LiteralResult normal) {
        this.normal = normal;
    }

    public boolean isCriticalCh() {
        return criticalCh;
    }

    public void setCriticalCh(boolean criticalCh) {
        this.criticalCh = criticalCh;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isMandatoryNotation() {
        return mandatoryNotation;
    }

    public void setMandatoryNotation(boolean mandatoryNotation) {
        this.mandatoryNotation = mandatoryNotation;
    }

    public Integer getAnalizerUserId() {
        return analizerUserId;
    }

    public void setAnalizerUserId(Integer analizerUserId) {
        this.analizerUserId = analizerUserId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReferenceValue other = (ReferenceValue) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
