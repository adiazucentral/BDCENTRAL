package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una orden en el sistema
 *
 * @version 1.0.0
 * @author dcortes
 * @since 5/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Orden de Laboratorio",
        description = "Representa una orden de laboratorio de la aplicación"
)
@JsonInclude(Include.NON_NULL)
@Data
public class OrderReportINS
{

    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = false, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "createdDateShort", description = "Fecha Creación en Formato yyyymmdd", required = true, order = 2)
    private Integer createdDateShort;
    @ApiObjectField(name = "type", description = "Tipo de la orden", required = true, order = 3)
    private OrderType type = new OrderType();
    @ApiObjectField(name = "createdDate", description = "Fecha de Creación", required = true, order = 4)
    private Date createdDate;
    @ApiObjectField(name = "patient", description = "Paciente de la orden", required = true, order = 5)
    private Patient patient = new Patient();
    @ApiObjectField(name = "lastUpdateDate", description = "Fecha Ultima Modificación", required = false, order = 8)
    private Date lastUpdateDate;
    @ApiObjectField(name = "lastUpdateUser", description = "Usuario Ultima Modificación", required = false, order = 9)
    private User lastUpdateUser = new User();
    @ApiObjectField(name = "active", description = "Si esta activa o inactiva", required = true, order = 10)
    private boolean active;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 12)
    private Branch branch = new Branch();
    @ApiObjectField(name = "service", description = "Servicio", required = false, order = 13)
    private ServiceLaboratory service = new ServiceLaboratory();
    @ApiObjectField(name = "physician", description = "Medico", required = false, order = 14)
    private Physician physician = new Physician();
    @ApiObjectField(name = "account", description = "Cuenta", required = false, order = 15)
    private Account account = new Account();
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 17)
    private List<DemographicValue> demographics;
    @ApiObjectField(name = "tests", description = "examenes", required = false, order = 19)
    private List<Test> tests = new ArrayList<>();
    @ApiObjectField(name = "state", description = "Estado de la orden", required = false, order = 22)
    private Integer state;
}
