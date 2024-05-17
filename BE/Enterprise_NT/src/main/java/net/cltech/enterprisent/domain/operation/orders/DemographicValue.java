package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.DemographicNT;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el valor de un demografico seleccionado de una orden o un paciente
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Demografico Valor",
        description = "Representa el valor de un demografico asociado al paciente o a la orden"
)
@JsonInclude(Include.NON_NULL)
public class DemographicValue
{

    @ApiObjectField(name = "idDemographic", description = "Id Demografico", required = true, order = 1)
    private int idDemographic;
    @ApiObjectField(name = "demographic", description = "Nombre Demografico", required = true, order = 2)
    private String demographic;
    @ApiObjectField(name = "encoded", description = "Es codificado", required = true, order = 3)
    private boolean encoded;
    @ApiObjectField(name = "notCodifiedValue", description = "Valor del demografico si es no codificado", required = false, order = 5)
    private String notCodifiedValue;
    @ApiObjectField(name = "codifiedId", description = "Si es codificado, envia el id del item seleccionado", required = false, order = 6)
    private Integer codifiedId;
    @ApiObjectField(name = "codifiedCode", description = "Si es codificado, envia el codigo del item seleccionado", required = false, order = 7)
    private String codifiedCode;
    @ApiObjectField(name = "codifiedName", description = "Si es codificado, envia el nombre del item seleccionado", required = false, order = 8)
    private String codifiedName;
    @ApiObjectField(name = "codifiedNameEnglish", description = "Si es codificado, envia el nombre en ingles del item seleccionado", required = false, order = 8)
    private String codifiedNameEnglish;
    @ApiObjectField(name = "homologationCodeCentralSystem", description = "Codigos de homologacion para sistema central", required = false, order = 9)
    private List<String> homologationCodeCentralSystem;
    @ApiObjectField(name = "codifiedDescription", description = "Si es codificado, envia la descripcion del item seleccionado", required = false, order = 10)
    private String codifiedDescription;
    @ApiObjectField(name = "email", description = "Email item", required = true, order = 6)
    private String email;

    

    public DemographicValue()
    {
        this.idDemographic = 0;
        this.demographic = "";
        this.encoded = false;
        this.notCodifiedValue = "";
        this.codifiedId = 0;
        this.codifiedCode = "";
        this.codifiedName = "";
        this.homologationCodeCentralSystem = new ArrayList<>();
        this.codifiedDescription = "";
    }

    public DemographicValue(DemographicNT demographicNT)
    {
        if (!demographicNT.getEncode())
        {
            this.notCodifiedValue = demographicNT.getCode();
        }

        this.idDemographic = demographicNT.getId();
        this.codifiedName = demographicNT.getName();
        this.codifiedId = demographicNT.getId();
        this.codifiedCode = demographicNT.getCode();

    }

    public DemographicValue(Integer idDemographic)
    {
        this.idDemographic = idDemographic;
    }

    public DemographicValue(int id, String name, boolean encoded, String code)
    {
        this.idDemographic = id;
        this.demographic = name;
        this.encoded = encoded;
        this.codifiedCode = code;
    }

    public int getIdDemographic()
    {
        return idDemographic;
    }

    public void setIdDemographic(int idDemographic)
    {
        this.idDemographic = idDemographic;
    }

    public String getDemographic()
    {
        return demographic;
    }

    public void setDemographic(String demographic)
    {
        this.demographic = demographic;
    }

    public boolean isEncoded()
    {
        return encoded;
    }

    public void setEncoded(boolean encoded)
    {
        this.encoded = encoded;
    }

    public String getNotCodifiedValue()
    {
        return notCodifiedValue;
    }

    public void setNotCodifiedValue(String notCodifiedValue)
    {
        this.notCodifiedValue = notCodifiedValue;
    }

    public Integer getCodifiedId()
    {
        return codifiedId;
    }

    public void setCodifiedId(Integer codifiedId)
    {
        this.codifiedId = codifiedId;
    }

    public String getCodifiedCode()
    {
        return codifiedCode;
    }

    public void setCodifiedCode(String codifiedCode)
    {
        this.codifiedCode = codifiedCode;
    }

    public String getCodifiedName()
    {
        return codifiedName;
    }

    public void setCodifiedName(String codifiedName)
    {
        this.codifiedName = codifiedName;
    }

    public String getCodifiedNameEnglish() {
        return codifiedNameEnglish;
    }

    public void setCodifiedNameEnglish(String codifiedNameEnglish) {
        this.codifiedNameEnglish = codifiedNameEnglish;
    }
    
    public String getValue()
    {
        if (encoded)
        {
            return codifiedCode == null ? null : codifiedCode + "." + getCodifiedName();
        }
        return notCodifiedValue;
    }

    public List<String> getHomologationCodeCentralSystem()
    {
        return homologationCodeCentralSystem;
    }

    public void setHomologationCodeCentralSystem(List<String> homologationCodeCentralSystem)
    {
        this.homologationCodeCentralSystem = homologationCodeCentralSystem;
    }

    public String getCodifiedDescription() {
        return codifiedDescription;
    }

    public void setCodifiedDescription(String codifiedDescription) {
        this.codifiedDescription = codifiedDescription;
    }
    
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + this.idDemographic;
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
        final DemographicValue other = (DemographicValue) obj;
        if (this.idDemographic != other.idDemographic)
        {
            return false;
        }
        return true;
    }

}
