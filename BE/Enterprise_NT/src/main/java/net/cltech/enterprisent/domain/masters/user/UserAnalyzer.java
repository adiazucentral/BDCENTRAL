package net.cltech.enterprisent.domain.masters.user;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un usuario con su respectivo analizardor en destinos de microbiologia 
 * 
 * @version 1.0.0
 * @author Julian
 * @since 08/05/2020
 * @see Creación
 */

@ApiObject(
        group = "Usuario",
        name = "Analizador De Usuarios",
        description = "Representa un usuario con su respectivo analizador en destinos de microbiologia"
)
public class UserAnalyzer
{
    @ApiObjectField(name = "userId", description = "Identificador del usuario", required = true, order = 1)
    private int userId;
    @ApiObjectField(name = "userName", description = "Nombre de usuario", required = true, order = 2)
    private String userName;
    @ApiObjectField(name = "names", description = "Nombres del usuario", required = true, order = 3)
    private String names;
    @ApiObjectField(name = "lastNames", description = "Apellidos del usuario", required = true, order = 4)
    private String lastNames;
    @ApiObjectField(name = "identification", description = "Identificación del usuario", required = true, order = 5)
    private String identification;
    @ApiObjectField(name = "referenceLaboratory", description = "Laboratorio de referencia", required = true, order = 4)
    private Integer referenceLaboratory;
    @ApiObjectField(name = "nameReferenceLaboratory", description = "Nombre del laboratorio de referencia", required = true, order = 7)
    private String nameReferenceLaboratory;
    @ApiObjectField(name = "destination", description = "Destino que verifica", required = true, order = 8)
    private Integer destination;
    
    public UserAnalyzer()
    {
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getNames()
    {
        return names;
    }

    public void setNames(String names)
    {
        this.names = names;
    }

    public String getLastNames()
    {
        return lastNames;
    }

    public void setLastNames(String lastNames)
    {
        this.lastNames = lastNames;
    }

    public String getIdentification()
    {
        return identification;
    }

    public void setIdentification(String identification)
    {
        this.identification = identification;
    }

    public Integer getReferenceLaboratory()
    {
        return referenceLaboratory;
    }

    public void setReferenceLaboratory(Integer referenceLaboratory)
    {
        this.referenceLaboratory = referenceLaboratory;
    }

    public String getNameReferenceLaboratory()
    {
        return nameReferenceLaboratory;
    }

    public void setNameReferenceLaboratory(String nameReferenceLaboratory)
    {
        this.nameReferenceLaboratory = nameReferenceLaboratory;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }
}
