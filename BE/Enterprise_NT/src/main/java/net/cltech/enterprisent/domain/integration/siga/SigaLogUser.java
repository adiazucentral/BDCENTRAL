package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un log para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "LogUser",
        description = "Representa el objeto de log de usuario."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaLogUser
{
    @ApiObjectField(name = "id", description = "Id del Log Usuario")
    private Integer id;
    @ApiObjectField(name = "registerDate", description = "Fecha de registro del log")
    private Date registerDate;
    @ApiObjectField(name = "action", description = "Accion realizada")
    private Integer action;
    @ApiObjectField(name = "branch", description = "Sede a la que pertenece el usuario")
    private SigaBranch branch;
    @ApiObjectField(name = "point", description = "Taquilla desde donde realizo accion")
    private SigaPointOfCare point;
    @ApiObjectField(name = "user", description = "Usuario")
    private SigaUser user;
    @ApiObjectField(name = "reason", description = "Motivo de receso")
    private SigaReason reason;
    @ApiObjectField(name = "difference", description = "Minutos entre la fecha actual y la fecha de registro")
    private Integer difference;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public SigaBranch getBranch() {
        return branch;
    }

    public void setBranch(SigaBranch branch) {
        this.branch = branch;
    }

    public SigaPointOfCare getPoint() {
        return point;
    }

    public void setPoint(SigaPointOfCare point) {
        this.point = point;
    }

    public SigaUser getUser() {
        return user;
    }

    public void setUser(SigaUser user) {
        this.user = user;
    }

    public SigaReason getReason() {
        return reason;
    }

    public void setReason(SigaReason reason) {
        this.reason = reason;
    }

    public Integer getDifference() {
        return difference;
    }

    public void setDifference(Integer difference) {
        this.difference = difference;
    }

}
