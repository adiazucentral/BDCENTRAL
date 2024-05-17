package net.cltech.enterprisent.domain.masters.microbiology;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Antibiograma
 *
 * @version 1.0.0
 * @author eacuna
 * @since 27/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Microbiología",
        name = "Antibiograma",
        description = "Muestra informacion del maestro Antibiograma que usa el API"
)
public class Sensitivity extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id ", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "abbr", description = "Abreviatura", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "code", description = "Código", required = true, order = 4)
    private String code;
    @ApiObjectField(name = "suppressionRule", description = "Si se realiza control de calidad", required = true, order = 6)
    private boolean suppressionRule;
    @ApiObjectField(name = "state", description = "Si aplica reglas de supresion", required = true, order = 7)
    private boolean state;
    // @ApiObjectField(name = "idMicrobialDeteccion", description = "Id de deteccion microbiana", required = false, order = 8)
    private int idMicrobialDeteccion;

    public Sensitivity()
    {
    }

    public Sensitivity(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Sensitivity(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isSuppressionRule()
    {
        return suppressionRule;
    }

    public void setSuppressionRule(boolean suppressionRule)
    {
        this.suppressionRule = suppressionRule;
    }

    public int getIdMicrobialDeteccion()
    {
        return idMicrobialDeteccion;
    }

    public void setIdMicrobialDeteccion(int idMicrobialDeteccion)
    {
        this.idMicrobialDeteccion = idMicrobialDeteccion;
    }
    
    @Override
    public String toString()
    {
        return "Sensitivity{" + "id=" + id + ", name=" + name + ", abbr=" + abbr + ", code=" + code + ", state=" + state + '}';
    }
}
