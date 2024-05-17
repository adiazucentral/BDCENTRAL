package net.cltech.enterprisent.domain.masters.demographic;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro demografico Item
 *
 * @version 1.0.0
 * @author enavas
 * @since 08/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "DemograficoItem",
        description = "Muestra informacion del maestro DemograficoItem que usa el API"
)
public class DemographicItem extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del demografico Item", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo del demografico Item", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del demografico Item", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "nameEnglish", description = "Nombre del demografico Item en ingles", required = true, order = 3)
    private String nameEnglish;
    @ApiObjectField(name = "state", description = "Estado del demografico Item", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "description", description = "Description del demografico Item", required = true, order = 5)
    private String description;
    @ApiObjectField(name = "demographic", description = "Id del demografico", required = true, order = 6)
    private Integer demographic;
    @ApiObjectField(name = "defaultItem", description = "default Item del demografico", required = true, order = 7)
    private Boolean defaultItem;
    @ApiObjectField(name = "demographicName", description = "Nombre del demografico", required = false, order = 8)
    private String demographicName;
    @ApiObjectField(name = "userNameWeQuery", description = "Usuario de la consulta web", required = false, order = 8)
    private String userNameWeQuery;
    @ApiObjectField(name = "email", description = "Email item", required = true, order = 6)
    private String email;

    public Integer getId()
    {
        return id;
    }

    public String getUserNameWeQuery()
    {
        return userNameWeQuery;
    }

    public void setUserNameWeQuery(String userNameWeQuery)
    {
        this.userNameWeQuery = userNameWeQuery;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }
    
    public Integer getDemographic()
    {
        return demographic;
    }

    public void setDemographic(Integer demographic)
    {
        this.demographic = demographic;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Boolean getDefaultItem()
    {
        return defaultItem;
    }

    public void setDefaultItem(Boolean defaultItem)
    {
        this.defaultItem = defaultItem;
    }

    public String getDemographicName()
    {
        return demographicName;
    }

    public void setDemographicName(String demographicName)
    {
        this.demographicName = demographicName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

}
