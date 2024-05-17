package net.cltech.enterprisent.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Tipo de Documento
 *
 * @version 1.0.0
 * @author cmartin
 * @since 29/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Tipo de Documento",
        description = "Muestra informacion del maestro Tipos de Documento que usa el API"
)
@JsonInclude(Include.NON_NULL)
public class DocumentType extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del tipo de documento", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "abbr", description = "Abreviatura del tipo de documento", required = true, order = 2)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre del tipo de documento", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "state", description = "Estado ", required = true, order = 9)
    private boolean state;
    @ApiObjectField(name = "codeSiga", description = "Codigo del tipo de documento en el sistema de SIGA", required = true, order = 10)
    private Integer codeSiga;
    @ApiObjectField(name = "email", description = "Email", required = true, order = 6)
    private String email;

    public DocumentType()
    {
    }

    public DocumentType(Integer id, String abbr, String name, boolean state, Integer codeSiga)
    {
        this.id = id;
        this.abbr = abbr;
        this.name = name;
        this.state = state;
        this.codeSiga = codeSiga;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Integer getCodeSiga()
    {
        return codeSiga;
    }

    public void setCodeSiga(Integer codeSiga)
    {
        this.codeSiga = codeSiga;
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
