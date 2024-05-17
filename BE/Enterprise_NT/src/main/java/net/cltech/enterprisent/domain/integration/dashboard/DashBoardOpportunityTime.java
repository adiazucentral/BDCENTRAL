package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion necesario para el tablero tiempos de oportunidad.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/07/2018
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Tiempo de Oportunidad",
        description = "Representa la informacion necesario para el tablero tiempos de oportunidad."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class DashBoardOpportunityTime
{
    @ApiObjectField(name = "order", description = "Numero de la orden", order = 1)
    private Long order;
    @ApiObjectField(name = "idService", description = "Id del servicio", order = 2)
    private Integer idService;
    @ApiObjectField(name = "serviceCode", description = "Codigo del servicio", order = 3)
    private String serviceCode;
    @ApiObjectField(name = "serviceName", description = "Nombre del servicio", order = 4)
    private String serviceName;
    @ApiObjectField(name = "idTest", description = "Id del examen", order = 5)
    private Integer idTest;
    @ApiObjectField(name = "codeTest", description = "Codigo del examen", order = 6)
    private String testCode;
    @ApiObjectField(name = "nameTest", description = "Nombre del examen", order = 7)
    private String testName;
    @ApiObjectField(name = "testAbbreviation", description = "Abreviatura del examen", order = 8)
    private String testAbbreviation;
    @ApiObjectField(name = "idProfile", description = "Id del perfil", order = 5)
    private Integer idProfile;
    @ApiObjectField(name = "codeProfile", description = "Codigo del examen", order = 6)
    private String codeProfile;
    @ApiObjectField(name = "nameProfile", description = "Nombre del examen", order = 7)
    private String nameProfile;
    @ApiObjectField(name = "abbreviationProfile", description = "Abreviatura del examen", order = 8)
    private String abbreviationProfile;
    @ApiObjectField(name = "idBranch", description = "Id de la sede", order = 8)
    private Integer idBranch;
    @ApiObjectField(name = "branchName", description = "Nombre de la sede", order = 9)
    private String branchName;
    @ApiObjectField(name = "orderType", description = "Tipo de orden", order = 10)
    private String orderType;
    @ApiObjectField(name = "patientName", description = "Nombre del paciente", order = 11)
    private String patientName;
    @ApiObjectField(name = "PatientHistory", description = "Historia del paciente", order = 11)
    private String PatientHistory;
    @ApiObjectField(name = "orderDate", description = "Fecha de creación de la orden", order = 12)
    private Date orderDate;
    @ApiObjectField(name = "validated", description = "Indica si el examen ya fue verificado", order = 13)
    private boolean validated;
    @ApiObjectField(name = "validateDate", description = "Fecha de validación del examen", order = 14)
    private Date validateDate;
    @ApiObjectField(name = "resultDate", description = "Fecha del resultado del examen", order = 14)
    private Date resultDate;
    @ApiObjectField(name = "idSection", description = "Id de la seccion o area", order = 15)
    private Integer idSection;
    @ApiObjectField(name = "state", description = "estado del examen", order = 15)
    private Integer state;
    @ApiObjectField(name = "sectionName", description = "Nombre de la seccion o area", order = 16)
    private String sectionName;
    @ApiObjectField(name = "section", description = "Id de la seccion o area", order = 15)
    private String section;
    @ApiObjectField(name = "verifyDate", description = "Fecha de verificacion", order = 17)
    private Date verifyDate;
    @ApiObjectField(name = "dateTake", description = "Fecha de toma", order = 18)
    private Date dateTake;

    public DashBoardOpportunityTime()
    {
    }
    
    public DashBoardOpportunityTime clean()
    {
        this.idSection = null;
        this.sectionName = null;
        
        return this;
    }

    public static int ACTION_INSERT = 1;
    public static int ACTION_UPDATE = 2;
    public static int ACTION_DELETE = 3;
    public static int ACTION_INSERT_HOSPITAL_SAMPLING = 4;
    public static int ACTION_UPDATE_HOSPITAL_SAMPLING = 5;
    public static int ACTION_DELETE_HOSPITAL_SAMPLING = 6;

}
