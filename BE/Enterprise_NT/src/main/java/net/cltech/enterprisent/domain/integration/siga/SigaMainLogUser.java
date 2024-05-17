package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el historial de un usuario en SIGA
*
* @version 1.0.0
* @author Julian
* @since 22/01/2021
* @see Creación
*/
@ApiObject(
        group = "Siga",
        name = "Usuario del registro principal de Siga",
        description = "Servira para el mapeo del usuario y su historial principal en Siga"
)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SigaMainLogUser 
{
    @ApiObjectField(name = "logUserInPoint", description = "Historial del usuario en taquilla")
    private SigaLogUser logUserInPoint;
    @ApiObjectField(name = "turnInPoint", description = "Historial del usuario en el turno")
    private SigaTurn turnInPoint;

    public SigaMainLogUser()
    {
    }

    public SigaMainLogUser(SigaLogUser logUserInPoint, SigaTurn turnInPoint)
    {
        this.logUserInPoint = logUserInPoint;
        this.turnInPoint = turnInPoint;
    }

    public SigaLogUser getLogUserInPoint()
    {
        return logUserInPoint;
    }

    public void setLogUserInPoint(SigaLogUser logUserInPoint)
    {
        this.logUserInPoint = logUserInPoint;
    }

    public SigaTurn getTurnInPoint()
    {
        return turnInPoint;
    }

    public void setTurnInPoint(SigaTurn turnInPoint)
    {
        this.turnInPoint = turnInPoint;
    }
}